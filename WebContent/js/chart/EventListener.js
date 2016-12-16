/*
 * 각각의 마우스 움직임에 대한 Listener가
 * chartMouseMove함수를 차트 번호와 같이 호출한다.
 */

//패턴 하이라이트 인덱스
var hilightPatternIndex;

function candleChartMouseMove(evt) {
	chartMouseMove(evt, chartCount);
	
    if(patternFix == 1 && checkOption[1].checked == true) {
        //마우스 위치
        var mouseX = evt.x - mouseWidthGap;
        var areaX = patternPosX;
        //420은 위의 메뉴들 높이 + 노란박스 가장 위 위치
        var areaY = 420 + charts[chartCount].heightPadding + ( charts[chartCount].heightGap) - getScrollHeightPosition();
        // 중심 기준 좌우에 따라 위치 조정
        if(areaX < canvasWidth / 2) {
            areaX += 10;
        } else {
            areaX -= (yellowBoxWidth + 10);
        }

        //마우스가 노란 박스 안 일 때
        if(areaX <= mouseX && mouseX <= areaX + yellowBoxWidth && areaY <= evt.y && evt.y <= areaY + charts[chartCount].heightGap * 7) {
  
            //패턴이 있을 때
            if(pattern[patternIndex] != "") {
                //노란박스 안의 위치로 수정
                var posY = evt.y - areaY - 30;
                //노란박스 안에서 첫번째 글자
                
               
                //패턴을 자른다.
                var parsePattern = pattern[patternIndex].split("##");
                //마우스가 첫번째 글씨보다 위인지 검사 한다.
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
                    //하이라이트 된 패턴 인덱스 저장
                    hilightPatternIndex = pos;
                    return;
                }
            }
        }
    }
    hilightPatternIndex = -1;
    document.body.style.cursor = "default";    
}

//스크롤이 얼만큼 내려갔는지 구한다.
function getScrollHeightPosition() {
    var de = document.documentElement;
    var b = document.body;
    var h = document.all ? (!de.scrollTop ? b.scrollTop : de.scrollTop) : (window.pageYOffset ? window.pageYOffset : window.scrollY);
    return h;
}


function candleChartMouseDown(evt) {
    if ( patternFix == 1 && checkOption[1].checked == true ) {        
        //하이라이트 된 곳의 패턴으로 이동한다.
        if ( hilightPatternIndex >= 0 ) {
            var parsePattern = pattern[patternIndex].split("##");
            var pt = parsePattern[hilightPatternIndex].split("@");
            var address = pt[1].replace(/ /g, "");
            document.location.href = "pattern.html#" + address.replace("/", "");
        }
        else 
            patternFix = -1;        
     }
     //정보창이 체크 되있을 때 패턴을 고정 시킨다.
    else if ( checkOption[1].checked == true )
        patternFix = 1;
    //그 외에는 고정을 푼다.
    else
        patternFix = -1;
}

/*
 * 각 차트의 캔버스 영역에서 마우스 이벤트를 호출한 함수이므로
 * chartMouiseMove 함수에 마우스 정보와 차트 번호를 알려준다.
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
  * 각 차트들에게서 마우스 위치와 차트 번호를 가져와
  * 해당하는 차트에 대해서만 추가 정보를 그리도록 한다.
 */
function chartMouseMove(evt, chartNum) {
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
    
    //캔들 차트를 그린다.
    charts[chartCount].draw();

    //그리도록 설정된 차트만 그린다.
    for(var j = 0; j < chartCount; j++) {
        if(checkChart[j].checked == true)
            charts[j].draw();
    }

    //하이라이트 그림
    if(checkOption[0].checked == true) {
        drawHighlight(posX);
    }

    //날짜 그림
    charts[chartCount].drawCalendar(posX);

    //주가 정보 그림
    charts[chartCount].drawChartData();

    //패턴 정보 그림
    if(checkOption[1].checked == true) {
        //캔들차트이면서 패턴이 고정 안되 있을 때 그린다.
        if(chartNum == chartCount && patternFix == -1)
            charts[chartNum].drawInfo(posX);
        //현재 이벤트가 발생한 차트에 정보창을 그린다.
        else if(chartNum < chartCount)
            charts[chartNum].drawInfo(posX);
    }    
}


/**
 * 각각의 차트가 차트 시작 부터 격자 끝까지
 * 하이라이트 선을 그린다.
 */
function drawHighlight(posX) {
    //모든 차트
    for(var i = 0; i <= chartCount; i++) {
        //그리도록 선택한 차트
        if(i==chartCount || checkChart[i].checked == true) {
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
}