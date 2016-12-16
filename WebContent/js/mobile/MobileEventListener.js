/*
 * ������ ���콺 �����ӿ� ���� Listener��
 * chartMouseMove�Լ��� ��Ʈ ��ȣ�� ���� ȣ���Ѵ�.
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
 * �ش��ϴ� ��Ʈ�� ���ؼ��� �߰� ������ �׸����� �Ѵ�.
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

        //���̶���Ʈ �׸�
        drawHighlight(posX);

        //��¥ �׸�
        charts[chartCount].drawCalendar(posX);

        //�ְ� ���� �׸�
        charts[chartCount].drawChartData();

        //���� ���� �׸�
        charts[chartNum].drawInfo(posX);
    }
}

/**
 * ������ ��Ʈ�� ��Ʈ ���� ���� ���� ������
 * ���̶���Ʈ ���� �׸���.
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
