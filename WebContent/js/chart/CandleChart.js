var minLow;
var maxHigh;

var open = [];
var close = [];
var low = [];
var high = [];
var pattern = [];

var ma = [];
var maString = ["5", "20", "60", "120"];
var bollingerString = ["U", "M", "L"];
var bollinger = [];
var sar = [];

var patternFix;
var patternPosX;
var patternIndex;
var yellowBoxWidth = 200;

function CandleChart(o, c, l, h, p, e5, e20, e60, e120, bu, bm, bl, sa) {
	/* Variable */
	//Canvas에 대한 canvas와 context 변수 설정
	this.canvas = document.getElementById("candleChartCanvas");
	this.ctx = this.canvas.getContext('2d');
	//마우스 이동에 대한 이벤트 처리기 설정
	this.canvas.onmousemove = candleChartMouseMove;
	//마우스 클릭에 대한 이벤트 처리기 설정
	if ( type == 1)
	   this.canvas.onmousedown = candleChartMouseDown;
	//캔버스 높이
	this.canvasHeight = 410;
	//차트의 위 여백
	this.heightPadding = 50;
	//범주에서 상하 간격
	this.heightGap = 30;
	//격자 수
	this.gridLineCount = 12;
	//캔버스에서 차트의 높이 즉, 상하 간격 * 격자수 - 1 
	this.chartHeight = this.heightGap * (this.gridLineCount - 1);
    //캔버스 넓이와 높이 설정
	this.ctx.canvas.width = canvasWidth;
	this.ctx.canvas.height = this.canvasHeight;
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	patternFix = -1;
	
	/* Function */
	//데이터 -> 픽셀로 변환하기위한 Factor  함수
	this.setFactor = setCandleChartFactor;
	// 데이터 -> 픽셀 함수
	this.convertData = convertCandleChartData;
	//캔들차트 그리는 함수
	this.drawChart = drawCandleChart;
	//라인차트 그리는 함수
	this.drawLineChart = drawLineChart;
	//볼린져밴드 그리는 함수
	this.drawBollingerChart = drawBollingerChart;
	//Parabolic Sar 그리는 함수
	this.drawSar = drawCandleSarChart;
	//차트 범주 그리는 함수
	this.drawRange = drawCandleChartRange;
	//차트 배경 격자 그리는 함수
	this.drawBackground = drawBackground;
	//차트 이름 그리는 함수
	this.drawChartName = drawCandleChartName;
	//차트를 그리는데 필요한 모든 요소를 그리는 함수
	this.draw = drawCandleChartAll;
	//마우스 위치에 있는 봉의 패턴 보여주는 함수
	this.drawInfo = drawCandleChartInfo;
	//마우스 위치에 있는 날짜 보여주는 함수
	this.drawCalendar = drawCandleChartCalendar;
	//마우스 위치에 있는 봉의 정보 보여주는 함수
	this.drawChartData = drawCandleChartData;
	
    //차트 그리는데 필요한 데이터 저장
	open = o;
	close = c;
	low = l;
	high = h;
	pattern = p;
	ma[0] = e5;
	ma[1] = e20;
	ma[2] = e60;
	ma[3] = e120;
	bollinger[0] = bu;
	bollinger[1] = bm;
	bollinger[2] = bl;
	sar = sa;
}


/**
 * 차트를 그리는데 필요한 모든 정보를 그린다.
 */
function drawCandleChartAll() {
    //차트 영역을 지운다.
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);
    //화면상의 보여지는 봉들이 바뀌었을 수 있으므로 
    //Factor을 다시 계산한다.
    this.setFactor();
    //배경 격자를 그린다.
    this.drawBackground(); 
    //캔들 차트를 그린다.   
    this.drawChart(); 
    
    //MA가 체크 되었을 때 MA 라인 4 개를 그린다.
    if ( checkCandleLine[0].checked == true ) {
    	for ( var i = 0; i < 4; i++ )
    		this.drawLineChart(ma[i], maColor[i]);
    }
    //Bollinger Bend가 체크 되었을 때 
    if ( checkCandleLine[1].checked == true ) {
    	// for ( var i = 0; i < 3; i++ )
    		// this.drawLineChart(bollinger[i], bollingerColor[i]);
    	this.drawBollingerChart();
    }
    //Parabolic Sar이 체크 되었을 때
    if ( checkCandleLine[2].checked == true )  {
    	this.drawSar();
    }
    
    //범주를 그린다.
    this.drawRange();
    //차트 이름들을 그린다.
    this.drawChartName();
    
    //마우스가 클릭 되면 노란 정보창을 고정 시키고 그린다.
    if ( patternFix == 1 && checkOption[1].checked == true )
        this.drawInfo(patternPosX);
}

function drawCandleChart() {
    var candleGap = 1;
    var startX = chartWidthPadding + (chartWidthGap/2 + chartWidthGap/4) + candleGap;
    //var shadowLine = chartWidthPadding + chartWidthGap;
    var shadowLine = chartWidthPadding + chartWidthGap + chartWidthGap/4;
    var candleWidth = chartWidthGap - candleGap*2;
    
    for( var d = index; d < index+dayCount && d < size; d++) {
        //각각의 Data 화면상의 위치로 변환      
        var posOpen = this.convertData(open[d]);
        var posClose = this.convertData(close[d]);
        var posLow = this.convertData(low[d]);
        var posHigh = this.convertData(high[d]);
    
        //최고가 - 최저가
        this.ctx.beginPath();
        this.ctx.strokeStyle = shadowColor;
        this.ctx.lineWidth = shadowWidth;
        this.ctx.moveTo(shadowLine, posHigh);
        this.ctx.lineTo(shadowLine, posLow);
        this.ctx.stroke();
        
        //도지
        //if(Math.abs(posClose - posOpen) < dojiGap) {
        if(pattern[d].indexOf("Doji") != -1 || posOpen == posClose) {
            this.ctx.fillStyle = dojiColor;
            this.ctx.fillRect(startX, posOpen, candleWidth, 2);
        }
        //음봉
        else if(open[d] > close[d]) {
            this.ctx.fillStyle = negativeColor;
            this.ctx.fillRect(startX, posOpen, candleWidth, posClose - posOpen);
        }
        //양봉
        else {
            this.ctx.fillStyle = positiveColor;
            this.ctx.fillRect(startX, posClose, candleWidth, posOpen - posClose);
        }
        startX += chartWidthGap;
        shadowLine += chartWidthGap;
    }
}

function drawBollingerChart()
{
	//세로선->line->세로선->line fill()
	var posX, posY;
	var k,d,e;
	posX = chartWidthPadding + chartWidthGap + chartWidthGap/4;
	
	//선 두깨, 색 설정
	this.ctx.lineWidth = lineChartWidth;
	this.ctx.strokeStyle = "red";
	this.ctx.fillStyle = "rgba(255,255,102, 0.2)";
	
	//시작점찾기
	k = index; 
	while ( bollinger[1][k] == 0 && k < index + dayCount && k < size ) {
	   k++;
	}
	//그리기 시작
    if(k < index + dayCount && k < size) {
        posX += chartWidthGap * ( k - index );
        //세로줄긋기1
        this.ctx.beginPath();
        posY = this.convertData(bollinger[2][k]); 
        this.ctx.moveTo(posX,posY);
        posY = this.convertData(bollinger[0][k]); 
        this.ctx.lineTo(posX,posY);
        
        //윗라인그리기
        for(d = k; d < index + dayCount && d < size; d++) {            
            posY = this.convertData(bollinger[0][d]);
            this.ctx.lineTo(posX, posY);
            posX += chartWidthGap;
        }
        
        d = d-1;
        posX -=chartWidthGap;    
            
        posY=this.convertData(bollinger[2][d]);
        this.ctx.lineTo(posX,posY);
        
        //아랫라인그리기 인덱스뒤에서 앞으로
        for(e = d; e >= k ; e--)
        {	            
        	posY=this.convertData(bollinger[2][e]);
        	this.ctx.lineTo(posX,posY);        	
            posX -=chartWidthGap;        	
        }
        this.ctx.stroke();
        this.ctx.fill();
        this.ctx.closePath();
    }
    this.drawLineChart(bollinger[1], bollingerColor[1]);	
}

function drawCandleSarChart() {
    var startX = chartWidthPadding + (chartWidthGap/2 + chartWidthGap/4);   
    var sarWidth = chartWidthGap;
    
    for( var d = index; d < index+dayCount && d < size; d++) {
        //각각의 Data 화면상의 위치로 변환      
        var posSar = this.convertData(sar[d]);
        
        this.ctx.fillStyle = sarColor;
        this.ctx.fillRect(startX, posSar, sarWidth, 3);     
        
        startX += chartWidthGap;
    }
}


/**
 * 그래프에 나타낼 범위를 구하기 위한 최대 최소 값을 구하고
 * 주가를 그래프 상의 위치로 변환시키기 위한 factor을 구한다.
 */
function setCandleChartFactor() {
    //주가들의 최대 최소
    minLow = getMin(low);
    maxHigh = getMax(high);
    
    //MA가 체크 되었으면 MA의 값도 반영하여 최대 최소를 구한다.
    if ( checkCandleLine[0].checked == true ) {
    	for ( var i = 0; i < 4; i++ ) {
    		minLow = Math.min(getMin(ma[i]), minLow);
    		maxHigh = Math.max(getMax(ma[i]), maxHigh);
    	}    		
    }
    
    //BollingerBend가 체크 되었으면 BollingerBend의 값도 반영하여 최대 최소를 구한다.
    if ( checkCandleLine[1].checked == true ) {
    	for ( var i = 0; i < 3; i++ ) {
    		minLow = Math.min(getMin(bollinger[i]), minLow);
    		maxHigh = Math.max(getMax(bollinger[i]), maxHigh);
    	}    		
    }
    
    //Pabolic Sar이 체크 되었으면 Pabolic Sar의 값도 반영하여 최대 최소를 구한다.
    if ( checkCandleLine[2].checked == true ) {
 	   minLow = Math.min(getMin(sar), minLow);
 	   maxHigh = Math.max(getMax(sar), maxHigh);
    }
    
          
    //최대 최소의 차이의 10% ( 라인 사이의 간격 )
    this.chartDiff = roundXL( (maxHigh - minLow) * 0.11, money);   
    //실제 주가 차이를 그래프의 차이로 변환하기 위한 Factor
    this.convertFactor = this.heightGap / this.chartDiff ;
    
}

/**
 * 보조 지표들의 이름들을 선의 색에 따라 그린다.
 */
function drawCandleChartName() {
   /*
    * EMA, BollingerBend가 체크 되었으면 해당하는 선의 색을 보여준다.
    */
    var posX = chartWidthPadding + chartWidthGap/2;
    var posY = this.heightPadding + this.heightGap/2;
    this.ctx.font = chartFont;
    if ( checkCandleLine[0].checked == true ) {
    	for ( var k = 0; k < 4; k++ ) {    	    	
    		this.ctx.fillStyle = maColor[k];
    		this.ctx.fillText("MA" + maString[k], posX, posY);
    		posX += 60;
    	}
    	posX += 20;
    }
    if ( checkCandleLine[1].checked == true ) {
    	for ( var k = 0; k < 3; k++ ) {
    		this.ctx.fillStyle = bollingerColor[k];
    		this.ctx.fillText(bollingerString[k], posX, posY);
    		posX += 20;	
    	}
   }     
    
    if ( checkCandleLine[2].checked == true ) {
    	this.ctx.fillStyle = sarColor;
    	this.ctx.fillText("SAR", posX, posY);
    	posX += 20;
   }     
}

/**
 * 실제 값을 차트의 위치로 변환
 */
function convertCandleChartData(data) {
    var diff = maxHigh - data;          
    return this.heightPadding + this.heightGap + ( diff * this.convertFactor );    
}

/**
 * X축 Y축의 정보를 그린다.
 */
function drawCandleChartRange() {
    //최고가 최저가의 Y축 정보가 그려질 화면의 x, y좌표
    var fontPositionX = chartWidthPadding - 30;
    var fontPositionY = this.heightPadding;
    var xAxisGap, xAxixGapCount;
    var tick = 1;
    
    var startDay;
    //글씨 폰트 설정
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;
    
    fontPositionY += this.heightGap;
    
    //최고가 최저가의 Y축 정보를 그린다.
    var yAxisGap = maxHigh;
    for( var i = 0; i < this.gridLineCount - 2; i++) {
        yAxisGap = roundXL(yAxisGap, money);
        this.ctx.fillText(commify(yAxisGap), fontPositionX, fontPositionY +3);
        fontPositionY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }
    
    //날짜의 X축 정보가 그려징 화면의 x, y 좌표  
    fontPositionY = this.heightPadding + this.chartHeight + 15 + 3;
    
    var gap = 1 +  roundXL(dayCount / 50, 0);
    xAxisGap = chartWidthGap * gap * 5;
    xAxixGapCount = gap * 5;
    fontPositionX = chartWidthPadding + (chartWidthGap * (gap* 5 - 2));
    startDay = index + (gap*5-3);
    
    //날짜 출력    
    for ( var d = startDay; d < index+dayCount && d < size; d+= xAxixGapCount) {       
        var c;
        if ( type == 1 ) {
    	   c = calendar[d];
    	   this.ctx.fillText(c, fontPositionX - 22, fontPositionY);
    	}
    	else {
    	   if ( dayCount > 50 )
    	       tick++;
    	   if ( tick % 2 == 1 ) {  
    	       c = calendar[d].substring(0, 7);
    	       this.ctx.fillText(c, fontPositionX - 16, fontPositionY);
    	   }
    	}
        
        fontPositionX += xAxisGap;
    }
}

function drawCandleChartInfo(posX) {
	var posY = this.heightPadding + (this.heightGap * 2) + 2;
	var pos = drawIndex;
	
	//고정된 상태가 아니면 정보창의 값들을 저장한다. 
	if ( patternFix == -1 ) {
        patternPosX = posX;
        patternIndex = drawIndex;
    } else {
        pos = patternIndex;
        this.ctx.beginPath();
        this.ctx.strokeStyle = fixColor;
        this.ctx.lineWidth = highlightLineWidth;
        this.ctx.moveTo(posX, this.heightPadding);
        this.ctx.lineTo(posX, this.heightPadding + this.heightGap * (this.gridLineCount - 1));
        this.ctx.stroke();
    }
    
	// 중심 기준 좌우에 따라 위치 조정
	if (posX < canvasWidth / 2) {
		posX += 10;
	} else {
		posX -= (yellowBoxWidth + 10);
	}

	this.ctx.fillStyle = extraInfoBoxColor;
	this.ctx.fillRect(posX, posY, yellowBoxWidth, this.heightGap * 7);
    
	this.ctx.font = patternFont;
	posX += 15;
	
	/*
	 * 패턴 정보를 출력한다.
	 * 패턴이 M@Doji##P@Marubozu 이런 식으로 M, P, F 는 Minus, Plus, Find의 
	 * 타입을 나타내고 @ 다음의 문자는 패턴을 나타내며 각 패턴은 ##로 구분 된다.
	 * 
	 * split("##") 함수를 이용해 각각의 패턴을 분리하고 분리된 패턴을 다시 @로 분리하여
	 * 타입에 따라 색을 다르게하여 패턴을 출력한다.
	 */	
	if (pattern[pos] != "") {
		var parsePattern = pattern[pos].split("##");
		var k = 40;
		for ( var i = 0; i < parsePattern.length-1; i++) {
			var pt = parsePattern[i].split("@");
			if (pt[0] == "M") {
				this.ctx.fillStyle = negativeColor;				
			} else if (pt[0] == "P") {
				this.ctx.fillStyle = positiveColor;				
			} else if ( pt[0] == "F") {
				this.ctx.fillStyle = textColor;
			}
			this.ctx.fillText(pt[1], posX, posY + k);
			k += 20;
		}
	}
}

function drawCandleChartCalendar(posX) {	
	var posY = this.heightPadding + this.chartHeight;

	this.ctx.fillStyle = dateBoxColor;
	if ( type == 1)
		this.ctx.fillRect(posX - 40, posY, 80, 30);
	else {
 	    if ( dayCount >= 50 ) {
 	        if ( drawIndex - index < 5 ) {
 	           this.ctx.fillRect(posX - 25, posY, 100, 30);
 	           posX += 20;
 	        }
 	        else if ( drawIndex - index > dayCount-5 ) {
 	           this.ctx.fillRect(posX - 65, posY, 100, 30);
 	           posX -= 20;
 	        }
 	        else
 	           this.ctx.fillRect(posX - 45, posY, 100, 30);
 	    }
 	    else 
	       this.ctx.fillRect(posX - 45, posY, 100, 30);
	}
		
	//글씨 폰트 설정
	this.ctx.fillStyle = dateBoxTextColor;
	this.ctx.font = calendarFont;
	this.ctx.fillText(calendar[drawIndex], posX - 30, posY + 18);
}

function drawCandleChartData() {
	//글씨 폰트 설정
	var posX = (chartWidthPadding + (chartWidthGap / 2) ) - 30;
	var initPosX = posX;
	var posY = this.heightPadding;
	
	var gap = 100;
	
	this.ctx.fillStyle = textColor;
	this.ctx.font = dataFont;
	
	//Parabolic sar 의 값을 표시한다.
	if ( checkCandleLine[2].checked == true ) {
	    if ( type == 1 )
	       gap = 110;
	    else if ( type == 2)
	       gap = 100;
	    else if ( type == 3 )
	       gap = 120;
	       
		posY -= 15;
		this.ctx.fillStyle = textColor;
		this.ctx.fillText("Parabolic Sar > ", posX, posY);
		posX += gap;
		this.ctx.fillStyle = sarColor;
		this.ctx.fillText(commify(sar[drawIndex]), posX, posY);
	}
	
	//BollingerBend의 값을 표시한다.
	if ( checkCandleLine[1].checked == true ) {
		posY -= 15;
		posX = initPosX;
		this.ctx.fillStyle = textColor;
		this.ctx.fillText("BollingerBand > ", posX, posY);
		if ( type == 1 )
		  posX += 110;
		else if ( type == 2 )
	      posX += 100;
	    else if ( type == 3)
	       posX += 120;
		
		if ( type == 1 )
           gap = 140;
        else if ( type == 2) 
            gap = 100;
        else if ( type == 3)
           gap = 105;
		
		for ( var i = 0; i < 3; i++ ) {
		    this.ctx.fillStyle = bollingerColor[i];
		    this.ctx.fillText(bollingerString[i], posX, posY);		 
		    this.ctx.fillStyle = textColor;  
		    this.ctx.fillText(" : " + commify(roundXL(bollinger[i][drawIndex], 0) ), posX + 10, posY);
			posX += gap;	
		}
	}	

	//EMA의 값을 표시한다.
	if ( checkCandleLine[0].checked == true ) { 
		posY -= 15;
		posX = initPosX;
		this.ctx.fillText("MA > ", posX, posY);
		posX += 40;
		
		if ( type == 1 )
           gap = 120;
        else  if ( type == 2)
           gap = 90;
        else if ( type == 3 )
            gap = 110;
           
		for ( var i = 0; i < 4; i++ ) {
		    this.ctx.fillStyle = maColor[i];
            this.ctx.fillText(maString[i], posX, posY);       
            this.ctx.fillStyle = textColor;  
            if ( i == 3 )
                this.ctx.fillText("　: " + commify(roundXL(ma[i][drawIndex], 0)), posX + 12, posY);
            else
                this.ctx.fillText("　: " + commify(roundXL(ma[i][drawIndex], 0)), posX + 5, posY);
			posX += gap;
		}		
	}
	
	if ( posY == this.heightPadding ) {
		posY = this.heightPadding - 25;
	} else {
		posY -= 30;
	}
	
	//주가 정보 표시
	posX = initPosX;
	this.ctx.fillText("날짜 : " + calendar[drawIndex], posX, posY);
	this.ctx.fillText("거래량 : " + commify(volume[drawIndex]), posX + 130, posY);
	posY = posY + 15;	
	this.ctx.fillText("시가 : " + commify(open[drawIndex]), posX, posY);
	if ( type == 1 ) {
	    this.ctx.fillText("종가 : " + commify(close[drawIndex]), posX + 120, posY);
        this.ctx.fillText("저가 : " + commify(low[drawIndex]), posX + 240, posY);
        this.ctx.fillText("고가: " + commify(high[drawIndex]), posX + 360, posY);
	} else {
	   this.ctx.fillText("종가 : " + commify(close[drawIndex]), posX + 105, posY);
	   this.ctx.fillText("저가 : " + commify(low[drawIndex]), posX + 210, posY);
	   this.ctx.fillText("고가: " + commify(high[drawIndex]), posX + 315, posY);
	}
		
}

