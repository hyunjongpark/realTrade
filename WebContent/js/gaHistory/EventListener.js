/*
 * 각각의 마우스 움직임에 대한 Listener가
 * chartMouseMove함수를 차트 번호와 같이 호출한다.
 */

//스크롤이 얼만큼 내려갔는지 구한다.
function getScrollHeightPosition() {
    var de = document.documentElement;
    var b = document.body;
    var h = document.all ? (!de.scrollTop ? b.scrollTop : de.scrollTop) : (window.pageYOffset ? window.pageYOffset : window.scrollY);
    return h;
}

/**
  * 각 차트들에게서 마우스 위치와 차트 번호를 가져와
  * 해당하는 차트에 대해서만 추가 정보를 그리도록 한다.
 */
function chartMouseMove(evt) {
    /*
     * 차트의 왼쪽을 0으로 하나 사이트가 가운데 정렬 되어 있으므로
     * 0으로 수정하기 위해 여백의 크기를 구한다.
     */    
    mouseWidthGap = (document.documentElement.clientWidth - 910)/2;
    
    //X축의 시작 위치를 구한다.
    var posX = chartWidthPadding + chartWidthGap + chartWidthGap/4;
    //마우스 위치를 차트의 시작 위치에서 떨어진 위치로 수정한다.
    var mouseX = evt.x - mouseWidthGap - chartWidthPadding - chartWidthGap - chartWidthGap/2;
    
    //스크롤이 드래그 중이면 푼다.
    if ( isScrollDrag == true ) {
        isScrollDrag = false;
        scrollMouseUp();
    }
    
    //마우스위치를 chartWidthGap으로 나누어 몇번의 chartWidthGap만큼 떨어졌는지 구한다.
    var pos = roundXL(mouseX / chartWidthGap, 0) ;
    
    //음수이거나 dayCount를 넘어갈 경우 수정한다.
    if ( pos < 0 )
       pos = 0;
    else if ( pos >= dayCount )
       pos = dayCount-1;
       
    //X축의 위치를 계산한다. 
    posX += pos * chartWidthGap;
    //마우스가 가리키는 곳의 index를 계산한다.
    drawIndex = index + pos;
    
    //선택 위치 계산
    selectIndex = drawIndex;
        
    charts[0].draw();
    for ( var i = 1; i < chartCount; i++ ) {
        charts[i].draw();
        charts[i].drawInfo(posX);        
    }
    
    
    //하이라이트 그림    
    drawHighlight(posX);
    

    //날짜 그림
    charts[0].drawCalendar(posX);

    //주가 정보 그림
    charts[0].drawChartData();
    

//    
}


/**
 * 각각의 차트가 차트 시작 부터 격자 끝까지
 * 하이라이트 선을 그린다.
 */

function drawHighlight(posX) {
    //모든 차트
    for(var i = 0; i < chartCount; i++) {
        //그리도록 선택한 차트

        //차트에 하이라이트 선을 그린다.
        charts[i].ctx.beginPath();
        charts[i].ctx.strokeStyle = highlightColor;
        charts[i].ctx.lineWidth = highlightLineWidth;
        /*
         * 마우스가 가리키는 x, heightPadding에서부터
         * 위 여백 + 차트 높이의 위치 까지 직선을 긋는다.
         */
        charts[i].ctx.moveTo(posX, charts[i].heightPadding);
        charts[i].ctx.lineTo(posX, charts[i].heightPadding + charts[i].chartHeight);
        charts[i].ctx.stroke();
        charts[i].ctx.closePath();
    }
}