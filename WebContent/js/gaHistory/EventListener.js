/*
 * ������ ���콺 �����ӿ� ���� Listener��
 * chartMouseMove�Լ��� ��Ʈ ��ȣ�� ���� ȣ���Ѵ�.
 */

//��ũ���� ��ŭ ���������� ���Ѵ�.
function getScrollHeightPosition() {
    var de = document.documentElement;
    var b = document.body;
    var h = document.all ? (!de.scrollTop ? b.scrollTop : de.scrollTop) : (window.pageYOffset ? window.pageYOffset : window.scrollY);
    return h;
}

/**
  * �� ��Ʈ�鿡�Լ� ���콺 ��ġ�� ��Ʈ ��ȣ�� ������
  * �ش��ϴ� ��Ʈ�� ���ؼ��� �߰� ������ �׸����� �Ѵ�.
 */
function chartMouseMove(evt) {
    /*
     * ��Ʈ�� ������ 0���� �ϳ� ����Ʈ�� ��� ���� �Ǿ� �����Ƿ�
     * 0���� �����ϱ� ���� ������ ũ�⸦ ���Ѵ�.
     */    
    mouseWidthGap = (document.documentElement.clientWidth - 910)/2;
    
    //X���� ���� ��ġ�� ���Ѵ�.
    var posX = chartWidthPadding + chartWidthGap + chartWidthGap/4;
    //���콺 ��ġ�� ��Ʈ�� ���� ��ġ���� ������ ��ġ�� �����Ѵ�.
    var mouseX = evt.x - mouseWidthGap - chartWidthPadding - chartWidthGap - chartWidthGap/2;
    
    //��ũ���� �巡�� ���̸� Ǭ��.
    if ( isScrollDrag == true ) {
        isScrollDrag = false;
        scrollMouseUp();
    }
    
    //���콺��ġ�� chartWidthGap���� ������ ����� chartWidthGap��ŭ ���������� ���Ѵ�.
    var pos = roundXL(mouseX / chartWidthGap, 0) ;
    
    //�����̰ų� dayCount�� �Ѿ ��� �����Ѵ�.
    if ( pos < 0 )
       pos = 0;
    else if ( pos >= dayCount )
       pos = dayCount-1;
       
    //X���� ��ġ�� ����Ѵ�. 
    posX += pos * chartWidthGap;
    //���콺�� ����Ű�� ���� index�� ����Ѵ�.
    drawIndex = index + pos;
    
    //���� ��ġ ���
    selectIndex = drawIndex;
        
    charts[0].draw();
    for ( var i = 1; i < chartCount; i++ ) {
        charts[i].draw();
        charts[i].drawInfo(posX);        
    }
    
    
    //���̶���Ʈ �׸�    
    drawHighlight(posX);
    

    //��¥ �׸�
    charts[0].drawCalendar(posX);

    //�ְ� ���� �׸�
    charts[0].drawChartData();
    

//    
}


/**
 * ������ ��Ʈ�� ��Ʈ ���� ���� ���� ������
 * ���̶���Ʈ ���� �׸���.
 */

function drawHighlight(posX) {
    //��� ��Ʈ
    for(var i = 0; i < chartCount; i++) {
        //�׸����� ������ ��Ʈ

        //��Ʈ�� ���̶���Ʈ ���� �׸���.
        charts[i].ctx.beginPath();
        charts[i].ctx.strokeStyle = highlightColor;
        charts[i].ctx.lineWidth = highlightLineWidth;
        /*
         * ���콺�� ����Ű�� x, heightPadding��������
         * �� ���� + ��Ʈ ������ ��ġ ���� ������ �ߴ´�.
         */
        charts[i].ctx.moveTo(posX, charts[i].heightPadding);
        charts[i].ctx.lineTo(posX, charts[i].heightPadding + charts[i].chartHeight);
        charts[i].ctx.stroke();
        charts[i].ctx.closePath();
    }
}