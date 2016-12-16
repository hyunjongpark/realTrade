var controlForm;

var chartCount;
var candleLineCount;
var candleOptionCount;
var checkChart = [];
var checkCandleLine = [];
var checkOption = [];

var menuCount;
var menu = [];
var subMenu = [];

var chartArea;

var scale;
var touchX;
var touchY;
var touch;
var tempDay;
/**
 * 체크 박스 설정 따라 변화
 */
function setControlChart() {
	setElementId();
}

/**
 * 각각의 체크 박스에 대한 id를 지정한다.
 */
function setElementId() {
	controlForm = document.getElementById("control");
	controlForm.addEventListener('change', controlChart, false);

	candleLineCount = 3;
	checkCandleLine[0] = document.getElementById("ema");
	checkCandleLine[1] = document.getElementById("bollinger");
	checkCandleLine[2] = document.getElementById("sar");
	
	chartArea = document.getElementById("chartArea");
	if ( type == 3) {
	   chartArea.ongesturestart = gestureStartFunc;
    	chartArea.ongestureend = gestureEndFunc;
	}
	chartArea.ontouchstart = touchstart;
	chartArea.ontouchmove = touchmove;
	chartArea.ontouchend = touchend;

	initScroll();
}

function touchstart(event) {    
    if ( event.touches.length < 2 ) {
        touchX = event.touches[0].pageX;
        touchY = event.touches[0].pageY;
        touch = 0;
    }
}

function touchmove(event) {    
    if(event.touches.length < 2) {
        event.preventDefault();
        var xDiff = Math.abs(touchX - event.touches[0].pageX);
        if ( touch <= 0 ) 
        {
            touch = -1; 
            if ( touchY > event.touches[0].pageY && xDiff < 20 ) 
                changeDayCount(-10);
            else if ( touchY < event.touches[0].pageY && xDiff < 20 )
                changeDayCount(10);
            else   
                touch = 0;            
        }
        if ( touch >= 0 ) {
            touch = 1;         
            if(touchX > event.touches[0].pageX)
                changeIndex(1);
            else if((touchX < event.touches[0].pageX ))
                changeIndex(-1);
            else
                touch = 0;
            
        }        
    }
}

function touchend(event) {
    touch = 0;
}

function gestureStartFunc(event) {
    //scale = event.scale;
    tempDayCount = dayCount;
}

function gestureEndFunc(event) {
    // if ( scale > event.scale ) 
        // changeDayCount(10);
    // else if ( scale < event.scale ) 
        // changeDayCount(-10);
    dayCount = tempDayCount;
}

/**
 * 체크 박스가 눌려졌을 때 호출 되는 함수
 */
function controlChart() {
	charts[chartCount].heightPadding = 45;
	charts[chartCount].canvas.height = 410;

	for ( var i = 0; i < candleLineCount; i++) {
		if (checkCandleLine[i].checked == true) {
			charts[chartCount].heightPadding += 15;
			charts[chartCount].canvas.height += 15;
		}
	}    
	
	charts[chartCount].canvasHeight = charts[chartCount].canvas.height;
	
	for ( var i = 0; i <= chartCount; i++) {
			charts[i].draw();
	}
}

/**
 * 날짜 이동
 */
function changeIndex(change) {
	if (change == 100)
		change = dayCount;
	else if (change == -100)
		change = -dayCount;
    else {
        change *= dayCount/10;
    }

	index += change;
	
	if (index < 0)
		index = 0;
	if (index + dayCount >= size)
		index = size - dayCount;
	
	applyScroll();

	for ( var i = 0; i <= chartCount; i++) {	
		charts[i].draw();
	}
}

/**
 * 표시 하는 총 날짜 수 변화
 */
function changeDayCount(change) {
	var temp = dayCount;
	temp += change;
	
	applyScroll();

	if (temp >= 5 && temp <= 120) {
		dayCount = temp;

		if (index + dayCount >= size) {
			while (index + dayCount > size) {
				index--;
			}
		}

		chartWidthGap = chartWidth / (dayCount + 1);

		for ( var i = 0; i <= chartCount; i++) {	
			charts[i].draw();		
		}
	}
}