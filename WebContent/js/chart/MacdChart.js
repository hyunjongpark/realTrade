var maxMacd;
var minMacd;

var macd = [];
var signal = [];
var oscillator = [];

function MacdChart(mac, si, os) {
	/* Variable */
	this.canvas = document.getElementById("macdChartCanvas");
	this.canvas.onmousemove = macdChartMouseMove;
	this.ctx = this.canvas.getContext('2d');
	this.canvasHeight = 155;
	this.heightPadding = 25;
	this.heightGap = 30;
	this.gridLineCount = 5;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setMacdChartFactor;
	this.convertData = convertMacdChartData;
	this.drawOscillatorChart = drawMacdOscillatorChart;
	this.drawLineChart = drawLineChart;
	this.drawRange = drawMacdChartRange;
	this.drawBackground = drawBackground;	
	this.drawChartName = drawMacdChartName;
	this.draw = drawMacdChartAll;
	this.drawInfo = drawMacdChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    macd = mac;
    signal = si;
    oscillator = os;
 
    this.chartDisable();
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
	this.drawOscillatorChart();
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
    minMacd = Math.min(getMin(oscillator), minMacd);
    maxMacd = Math.max(getMax(oscillator), maxMacd);
    
    maxMacd = Math.round(maxMacd);
    minMacd = Math.round(minMacd);

	//최고값을 3000으로 나누어 떨어지는 수로 설정
    while ( maxMacd % 3 != 0 ) {
        maxMacd++;
    }
    if ( minMacd < 0 ) {
   		while ( minMacd % 3 != 0 ) {
        	minMacd--;
    	}   
	}
	//음수 또는 양수의 간격중 더 긴 간격을 선택한다.
	if ( (maxMacd/3) > (minMacd*-1) ) 
		this.chartDiff = maxMacd/3;
	else	
		this.chartDiff = -minMacd;
		
      
         
    if ( this.chartDiff == 0 )
        this.chartDiff = 0.5;
    
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
    this.ctx.fillStyle = oscillatorPositiveColor;
    this.ctx.fillText("Oscil", posX+120, posY);
    this.ctx.fillStyle = oscillatorNegativeColor;
    this.ctx.fillText("lator", posX+155, posY);   
}

//Quote 값을 Graph의 값으로 변경
function drawMacdOscillatorChart() {
    var barGap = 1;
	var startX, barWidth, posBar, posBottom;
    posBottom = this.heightPadding + ( this.heightGap * 3 );
    
    startX = chartWidthPadding + ( chartWidthGap/2 + chartWidthGap/4 ) + barGap;
    barWidth = chartWidthGap - barGap*2;
    
    for ( var d = index; d < index+dayCount && d < size; d++ ) {
    	//oscillator 막대 색 결정
    	if ( oscillator[d] >= 0 ) 
    		this.ctx.fillStyle = oscillatorPositiveColor;
    	else
    		this.ctx.fillStyle = oscillatorNegativeColor;
    		
        posBar = this.convertData(oscillator[d]);
        //startX = chartWidthPadding +  d  * chartWidthGap + ( chartWidthGap/2 + chartWidthGap/4 );    
          
        this.ctx.fillRect(startX, posBar, barWidth, posBottom-posBar);
        startX += chartWidthGap;
        
    }  
}

/**
 * 실제 값을 차트의 위치로 변환
 */

function convertMacdChartData(data) {
	return ( this.heightPadding + (this.heightGap*3) ) -  (data * this.convertFactor);      
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
	for( var i = 3; i > 0; i--) {
		rangeNum = this.chartDiff * i;
		if ( this.chartDiff < 10 )
		  rangeNum = roundXL(rangeNum, 2);
		else
		  rangeNum = roundXL(rangeNum, 0);
		this.ctx.fillText(commify(rangeNum), posX, posY );
		posY += this.heightGap;
	}
		
	this.ctx.fillText(0, posX+10, posY);
	posY += this.heightGap;
	
    if ( this.chartDiff < 10 )
        this.ctx.fillText(commify(-roundXL(this.chartDiff, 2)), posX, posY);
    else
        this.ctx.fillText(commify(-roundXL(this.chartDiff, 0)), posX, posY);
        
	

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
    this.ctx.fillRect(posX, posY, 150, this.heightGap*3 ); 
    
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("날짜 : " + calendar[drawIndex], posX+15, posY+20);
    this.ctx.fillText("MACD : " + commify(macd[drawIndex]), posX+15, posY+40);
    this.ctx.fillText("Signal : " + commify(signal[drawIndex]), posX+15, posY+60);
    this.ctx.fillText("Osc : " + commify(oscillator[drawIndex]), posX+15, posY+80);        
}



