/*
 * ������ ���콺 �����ӿ� ���� Listener��
 * chartMouseMove�Լ��� ��Ʈ ��ȣ�� ���� ȣ���Ѵ�.
 */

//���� ���̶���Ʈ �ε���
var hilightPatternIndex;

function candleChartMouseMove(evt) {
	chartMouseMove(evt, chartCount);
	
    if(patternFix == 1 && checkOption[1].checked == true) {
        //���콺 ��ġ
        var mouseX = evt.x - mouseWidthGap;
        var areaX = patternPosX;
        //420�� ���� �޴��� ���� + ����ڽ� ���� �� ��ġ
        var areaY = 420 + charts[chartCount].heightPadding + ( charts[chartCount].heightGap) - getScrollHeightPosition();
        // �߽� ���� �¿쿡 ���� ��ġ ����
        if(areaX < canvasWidth / 2) {
            areaX += 10;
        } else {
            areaX -= (yellowBoxWidth + 10);
        }

        //���콺�� ��� �ڽ� �� �� ��
        if(areaX <= mouseX && mouseX <= areaX + yellowBoxWidth && areaY <= evt.y && evt.y <= areaY + charts[chartCount].heightGap * 7) {
  
            //������ ���� ��
            if(pattern[patternIndex] != "") {
                //����ڽ� ���� ��ġ�� ����
                var posY = evt.y - areaY - 30;
                //����ڽ� �ȿ��� ù��° ����
                
               
                //������ �ڸ���.
                var parsePattern = pattern[patternIndex].split("##");
                //���콺�� ù��° �۾����� ������ �˻� �Ѵ�.
                if ( 0 <= posY && posY <= 20*(parsePattern.length-1)-5 ) {
                    var y = charts[chartCount].heightPadding + (charts[chartCount].heightGap * 2) + 25;
                    var pos = Math.floor( posY / 20);
                    y = y + (pos*20) + 20;
                    
                    charts[chartCount].ctx.beginPath();
                    charts[chartCount].ctx.strokeStyle = underColor;
                    charts[chartCount].ctx.lineWidth = underLineWidth;
                    charts[chartCount].ctx.moveTo(areaX + 10, y);
                    charts[chartCount].ctx.lineTo(areaX + 190, y);
                    charts[chartCount].ctx.stroke();
                    charts[chartCount].ctx.closePath();
                    document.body.style.cursor = "pointer";
                    //���̶���Ʈ �� ���� �ε��� ����
                    hilightPatternIndex = pos;
                    return;
                }
            }
        }
    }
    hilightPatternIndex = -1;
    document.body.style.cursor = "default";    
}

//��ũ���� ��ŭ ���������� ���Ѵ�.
function getScrollHeightPosition() {
    var de = document.documentElement;
    var b = document.body;
    var h = document.all ? (!de.scrollTop ? b.scrollTop : de.scrollTop) : (window.pageYOffset ? window.pageYOffset : window.scrollY);
    return h;
}


function candleChartMouseDown(evt) {
    if ( patternFix == 1 && checkOption[1].checked == true ) {        
        //���̶���Ʈ �� ���� �������� �̵��Ѵ�.
        if ( hilightPatternIndex >= 0 ) {
            var parsePattern = pattern[patternIndex].split("##");
            var pt = parsePattern[hilightPatternIndex].split("@");
            var address = pt[1].replace(/ /g, "");
            document.location.href = "pattern.html#" + address.replace("/", "");
        }
        else 
            patternFix = -1;        
     }
     //����â�� üũ ������ �� ������ ���� ��Ų��.
    else if ( checkOption[1].checked == true )
        patternFix = 1;
    //�� �ܿ��� ������ Ǭ��.
    else
        patternFix = -1;
}

/*
 * �� ��Ʈ�� ĵ���� �������� ���콺 �̺�Ʈ�� ȣ���� �Լ��̹Ƿ�
 * chartMouiseMove �Լ��� ���콺 ������ ��Ʈ ��ȣ�� �˷��ش�.
 */
function volumeChartMouseMove(evt) {
	chartMouseMove(evt, 0);
}

function macdChartMouseMove(evt) {
	chartMouseMove(evt, 1);
}

function rsiChartMouseMove(evt) {
	chartMouseMove(evt, 2);
}

function obvChartMouseMove(evt) {
	chartMouseMove(evt, 3);
}

function adxChartMouseMove(evt) {
	chartMouseMove(evt, 4);
}

function cciChartMouseMove(evt) {
    chartMouseMove(evt, 5);
}

function williamsChartMouseMove(evt) {
    chartMouseMove(evt, 6);
}

function trixChartMouseMove(evt) {
    chartMouseMove(evt, 7);
}

function rocChartMouseMove(evt) {
    chartMouseMove(evt, 8);
}

function dmiChartMouseMove(evt) {
    chartMouseMove(evt, 9);
}

function fastStcChartMouseMove(evt) {
    chartMouseMove(evt, 10);
}

function slowStcChartMouseMove(evt) {
    chartMouseMove(evt, 11);
}

/**
  * �� ��Ʈ�鿡�Լ� ���콺 ��ġ�� ��Ʈ ��ȣ�� ������
  * �ش��ϴ� ��Ʈ�� ���ؼ��� �߰� ������ �׸����� �Ѵ�.
 */
function chartMouseMove(evt, chartNum) {
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
    
    //ĵ�� ��Ʈ�� �׸���.
    charts[chartCount].draw();

    //�׸����� ������ ��Ʈ�� �׸���.
    for(var j = 0; j < chartCount; j++) {
        if(checkChart[j].checked == true)
            charts[j].draw();
    }

    //���̶���Ʈ �׸�
    if(checkOption[0].checked == true) {
        drawHighlight(posX);
    }

    //��¥ �׸�
    charts[chartCount].drawCalendar(posX);

    //�ְ� ���� �׸�
    charts[chartCount].drawChartData();

    //���� ���� �׸�
    if(checkOption[1].checked == true) {
        //ĵ����Ʈ�̸鼭 ������ ���� �ȵ� ���� �� �׸���.
        if(chartNum == chartCount && patternFix == -1)
            charts[chartNum].drawInfo(posX);
        //���� �̺�Ʈ�� �߻��� ��Ʈ�� ����â�� �׸���.
        else if(chartNum < chartCount)
            charts[chartNum].drawInfo(posX);
    }    
}


/**
 * ������ ��Ʈ�� ��Ʈ ���� ���� ���� ������
 * ���̶���Ʈ ���� �׸���.
 */
function drawHighlight(posX) {
    //��� ��Ʈ
    for(var i = 0; i <= chartCount; i++) {
        //�׸����� ������ ��Ʈ
        if(i==chartCount || checkChart[i].checked == true) {
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
}