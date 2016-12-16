var maxMacd;
var minMacd;

var macd = [];
var signal = [];

function MacdChart(mac, si) {
	/* Variable */
	this.canvas = document.getElementById("macdChartCanvas");
	this.canvas.onmousemove = chartMouseMove;
	this.ctx = this.canvas.getContext('2d');
	this.canvasHeight = 155;
	this.heightPadding = 25;
	this.heightGap = 30;
	this.gridLineCount = 5;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);	   
    this.ctx.canvas.width = canvasWidth;
    this.ctx.canvas.height = this.canvasHeight;
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setMacdChartFactor;
	this.convertData = convertMacdChartData;
	//this.drawOscillatorChart = drawMacdOscillatorChart;
	this.drawLineChart = drawLineChart;
	this.drawRange = drawMacdChartRange;
	this.drawBackground = drawBackground;	
	this.drawChartName = drawMacdChartName;
	this.draw = drawMacdChartAll;
	this.drawInfo = drawMacdChartInfo;
    macd = mac;
    signal = si;
}

/**
 * 차트를 그리는데 필요한 모든 정보를 그린다.
 */
function drawMacdChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);
    
    this.setFactor();
    this.drawBackground();    
    this.drawLineChart(macd, macdColor);
	this.drawLineChart(signal, signalColor);
	//this.drawOscillatorChart();
	this.drawChartName();
	this.drawRange();  
}

/**
 * 그래프에 나타낼 범위를 구하기 위한 최대 최소 값을 구하고
 * 주가를 그래프 상의 위치로 변환시키기 위한 factor을 구한다.
 */
function setMacdChartFactor() {
    //주가들의 최대 최소
    minMacd = getMin(macd);
    maxMacd = getMax(macd);
    
    minMacd = Math.min(getMin(signal), minMacd);
    maxMacd = Math.max(getMax(signal), maxMacd);
    
    maxMacd = Math.round(maxMacd);
    minMacd = Math.round(minMacd);

	//최고값을 2으로 나누어 떨어지는 수로 설정
    while ( maxMacd % 2 != 0 ) {
        maxMacd++;
    }
    if ( minMacd < 0 ) {
   		while ( minMacd % 2 != 0 ) {
        	minMacd--;
    	}   
	}
	//음수 또는 양수의 간격중 더 긴 간격을 선택한다.
	if ( (maxMacd/2) > (minMacd/2*-1) ) 
		this.chartDiff = maxMacd/2;
	else	
		this.chartDiff = (-minMacd)/2;
		
    if ( this.chartDiff <= 2 )  
         this.chartDiff = 1;
         
    else if ( this.chartDiff == 0 )
        this.chartDiff = 3000;
    
	this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * Macd 각각의 선의 색을 표시해 준다.
 */
function drawMacdChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2 +10;
    var posY = this.heightPadding/2 +5;
    this.ctx.font = chartFont;
     	    	
    this.ctx.fillStyle = macdColor;
    this.ctx.fillText("MACD", posX, posY);    
    this.ctx.fillStyle = signalColor;
    this.ctx.fillText("Signal", posX+60, posY);
}

/**
 * 실제 값을 차트의 위치로 변환
 */

function convertMacdChartData(data) {
	return ( this.heightPadding + (this.heightGap*2) ) -  (data * this.convertFactor);      
}

/**
 * X축 Y축의 정보를 그린다.
 */

function drawMacdChartRange() {
	var posX, posY;

	//글씨 폰트 설정
	this.ctx.fillStyle = textColor;
	this.ctx.font = rangeFont;

	//거래량의 Y축 정보가 그려징 화면의 x, y 좌표
	posX = chartWidthPadding - 30;
	posY = this.heightPadding+3;
	
	//거래량의 Y축 정보를 그린다.
	for( var i = 2; i > 0; i--) {
		rangeNum = this.chartDiff * i;
		rangeNum = roundXL(rangeNum, money);
		this.ctx.fillText(commify(rangeNum), posX, posY );
		posY += this.heightGap;
	}
		
	this.ctx.fillText(0, posX+10, posY);
	posY += this.heightGap;
	for ( var j = 1; j <= 2; j++ ) {
	    rangeNum = this.chartDiff * j * -1;
	    rangeNum = roundXL(rangeNum, money);
        this.ctx.fillText(commify(rangeNum), posX, posY );
        posY += this.heightGap;
	}
}

function drawMacdChartInfo(posX) {
	var posY = this.heightPadding + this.heightGap/2;
		
    if ( posX < canvasWidth/2 ) {
        posX += 30;    	
    }
    else {
     	posX -= 180;
    }
    
    this.ctx.fillStyle = extraInfoBoxColor;
    this.ctx.fillRect(posX, posY, 150, this.heightGap*2.5 ); 
    
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("날짜 : " + calendar[drawIndex], posX+15, posY+20);
    this.ctx.fillText("MACD : " + commify(macd[drawIndex]), posX+15, posY+40);
    this.ctx.fillText("Signal : " + commify(signal[drawIndex]), posX+15, posY+60);
  //  this.ctx.fillText("Osc : " + commify(oscillator[drawIndex]), posX+15, posY+80);        
}



