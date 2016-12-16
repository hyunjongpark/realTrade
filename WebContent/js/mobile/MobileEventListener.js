/*
 * 각각의 마우스 움직임에 대한 Listener가
 * chartMouseMove함수를 차트 번호와 같이 호출한다.
 */
function candleChartMouseMove(evt) {
	chartMouseMove(evt, chartCount);
}
function candleChartMouseDown(evt) {
    candleChartMouseMove(evt);
}

function volumeChartMouseMove(evt) {
	chartMouseMove(evt, 0);
}

function macdChartMouseMove(evt) {
	chartMouseMove(evt, 1);
}

function rsiChartMouseMove(evt) {
	chartMouseMove(evt, 2);
}

function slowStcChartMouseMove(evt) {
	chartMouseMove(evt, 3);
}

/**
 * 해당하는 차트에 대해서만 추가 정보를 그리도록 한다.
 */
function chartMouseMove(evt, chartNum) { 
    var posX = chartWidthPadding + chartWidthGap + chartWidthGap / 4;
    var mouseX = evt.x - chartWidthPadding - chartWidthGap - chartWidthGap / 2;

    if(isScrollDrag == true) {
        isScrollDrag = false;
        scrollMouseUp();
    }

    var pos = roundXL(mouseX / chartWidthGap, 0);
    if(pos < 0)
        pos = 0;
    else if(pos >= dayCount)
        pos = dayCount - 1;
    posX += pos * chartWidthGap;
    drawIndex = index + pos;

    if(selectIndex != drawIndex) {
        selectIndex = drawIndex;

        for(var j = 0; j <= chartCount; j++) {
            charts[j].draw();
        }

        //하이라이트 그림
        drawHighlight(posX);

        //날짜 그림
        charts[chartCount].drawCalendar(posX);

        //주가 정보 그림
        charts[chartCount].drawChartData();

        //패턴 정보 그림
        charts[chartNum].drawInfo(posX);
    }
}

/**
 * 각각의 차트가 차트 시작 부터 격자 끝까지
 * 하이라이트 선을 그린다.
 */

function drawHighlight(posX) {
    for(var i = 0; i <= chartCount; i++) {
        charts[i].ctx.beginPath();
        charts[i].ctx.strokeStyle = highlightColor;
        charts[i].ctx.lineWidth = highlightLineWidth;
        charts[i].ctx.moveTo(posX, charts[i].heightPadding);
        charts[i].ctx.lineTo(posX, charts[i].heightPadding + charts[i].chartHeight);
        charts[i].ctx.stroke();

    }
}
