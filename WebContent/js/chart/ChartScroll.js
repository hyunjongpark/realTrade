var scrollWidth;
var scrollPadding;
var scrollCanvas;
var scrollCtx;
var scrollPos;
var scrollDiff;
var scrollSize;
var isScrollDrag;

function initScroll() {
    scrollCanvas =  document.getElementById("scrollCanvas");
    scrollCtx = scrollCanvas.getContext('2d');
    scrollWidth = chartWidth;
    scrollPadding = 30;
    scrollCanvas.onmousedown = scrollMouseDown;    
    scrollCanvas.onmouseup = scrollMouseUp;
    scrollCanvas.onmousemove = scrollMouseMove;
    scrollCtx.canvas.width = canvasWidth;
    scrollCtx.canvas.height = 30;
    
    isDrag = false;
    
    scrollDiff = 15;
    maxScrollPos = scrollWidth/scrollDiff;
    scrollPos = maxScrollPos;
    scrollSize = 15;
    drawBackgroundScroll();
    drawScroll();
}

function scrollMouseDown(evt) {
    var mouseX = evt.x - mouseWidthGap;
   
    scrollPos = Math.round(mouseX / scrollDiff);      
    isScrollDrag = true;
}

function scrollMouseUp(evt) {
    var mouseX = evt.x - mouseWidthGap;
    if ( isScrollDrag == true )
        scrollPos =Math.round(mouseX / scrollDiff);
    isScrollDrag = false;
    
    drawScrollAll();
    
    if(scrollPos == 3)
        index = 0;
    else
        index = Math.round((size - dayCount) * (scrollPos-2) / (maxScrollPos-2));
        
    charts[chartCount].draw();

    for(var i = 0; i < chartCount; i++) {
        if(checkChart[i].checked == true)
            charts[i].draw();
     }
    
}

function scrollMouseMove(evt) {
    var mouseX = evt.x - mouseWidthGap;
  
    if(isScrollDrag == true) {
        scrollPos = Math.round(mouseX / scrollDiff);
        drawScrollAll();
        if(scrollPos == 3)
            index = 0;
        else
            index = Math.round((size - dayCount) * (scrollPos - 2) / (maxScrollPos - 2));

        charts[chartCount].draw();

        for(var i = 0; i < chartCount; i++) {
            if(checkChart[i].checked == true)
                charts[i].draw();
        }
    }

    else if ( scrollPos > maxScrollPos)
        drawScrollAll();
    else if ( scrollPos < 3 ) 
        drawScrollAll();       
}

function drawScrollAll() {    
    if(scrollPos > maxScrollPos)
        scrollPos = maxScrollPos;
    else if(scrollPos < 3)
        scrollPos = 3;
    scrollCtx.clearRect(0, 0, canvasWidth, 30);
    drawBackgroundScroll();
    drawScroll();
}

function applyScroll() {
    if ( index == 0 )
        scrollPos = 3;
    else
        scrollPos = Math.round( (index * maxScrollPos-2) / (size-dayCount)) + 2;
        
    drawScrollAll();    
}

function drawScroll() {
    scrollCtx.fillStyle = scrollColor;
    scrollCtx.fillRect( (scrollPos-1)*scrollDiff, 0, scrollSize, 30 );    
}

function drawBackgroundScroll() {
    scrollCtx.lineWidth = scrollLineWidth;
    scrollCtx.strokeStyle = scrollBarColor;
    
    scrollCtx.beginPath();
    scrollCtx.moveTo(scrollPadding, 15);
    scrollCtx.lineTo(scrollWidth, 15);    
    scrollCtx.stroke();   
}
