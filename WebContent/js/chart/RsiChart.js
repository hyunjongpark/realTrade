var rsi = [];

var minRsi;
var maxRsi;

function RsiChart(rs) {
	/* Variable */
	this.canvas = document.getElementById("rsiChartCanvas");
	this.canvas.onmousemove = rsiChartMouseMove;
	this.ctx = this.canvas.getContext('2d');
	this.canvasHeight = 120;
	this.heightPadding = 25;
	this.heightGap = 20;
	this.gridLineCount = 5;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setRsiChartFactor;
	this.convertData = convertRsiChartData;	
	this.drawLineChart = drawLineChart;
	this.drawRange = drawRsiChartRange;
	this.drawBackground = drawBackground;
	this.drawChartName = drawRsiChartName;	
	this.draw = drawRsiChartAll;
	this.drawInfo = drawRsiChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    rsi = rs;

    this.chartDisable();
}

/**
 * 차트를 그리는데 필요한 모든 정보를 그린다.
 */
function drawRsiChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();    
    this.drawLineChart(rsi, rsiLineColor);
	this.drawRange();  
	this.drawChartName();
}

/**
 * 그래프에 나타낼 범위를 구하기 위한 최대 최소 값을 구하고
 * 주가를 그래프 상의 위치로 변환시키기 위한 factor을 구한다.
 */
function setRsiChartFactor() {
	this.chartDiff = 25;	   
	this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * 차트 이름을 그린다.
 */
function drawRsiChartName() {  
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
     	    	
    this.ctx.fillStyle = rsiLineColor;
    this.ctx.fillText("RSI", posX, posY);   
}

/**
 * 실제 값을 차트의 위치로 변환
 */

function convertRsiChartData(data) {   
    return this.heightPadding + ( (100 - data) * this.convertFactor );    
}

/**
 * X축 Y축의 정보를 그린다.
 */

function drawRsiChartRange() {
	var posX, posY;

	//글씨 폰트 설정
	this.ctx.fillStyle = textColor;
	this.ctx.font = rangeFont;

	//거래량의 Y축 정보가 그려징 화면의 x, y 좌표
	posX = chartWidthPadding - 25;
	posY = this.heightPadding+3;

	//거래량의 Y축 정보를 그린다.
	var yAxisGap = 100;
	for( i = 5; i > 0; i--) {		
		yAxisGap = Math.round(yAxisGap);
		this.ctx.fillText(yAxisGap, posX, posY );
		posY += this.heightGap;
		yAxisGap -= this.chartDiff;
	}
}


function drawRsiChartInfo(posX) {
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
    this.ctx.fillText("날짜 : " + calendar[drawIndex], posX+15, posY+25);
    this.ctx.fillText("RSI : " + commify(rsi[drawIndex]), posX+15, posY+45);        
}



