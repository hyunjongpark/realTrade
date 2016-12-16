var maxVolume;

var volume = [];

function VolumeChart(v) {
	/* Variable */
	this.canvas = document.getElementById("volumeChartCanvas");
	this.ctx = this.canvas.getContext('2d');
	this.canvas.onmousemove = volumeChartMouseMove;
	this.canvasHeight = 120;
	this.heightPadding = 25;
	this.heightGap = 30;
	this.gridLineCount = 4;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setVolumeChartFactor;
	this.convertData = convertVolumeChartData;
	this.drawChart = drawVolumeChart;
	this.drawRange = drawVolumeChartRange;
	this.drawBackground = drawBackground;	
	this.drawChartName = drawVolumeChartName;
	this.draw = drawVolumeChartAll;
	this.drawInfo = drawVolumeChartInfo;


    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;
	
    volume = v;
    
    this.chartEnable();
    
}

/**
 * 차트를 그리는데 필요한 모든 정보를 그린다.
 */
function drawVolumeChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);
 	this.setFactor();
    this.drawBackground();    
    this.drawChart();   
    this.drawRange();     
    this.drawChartName();
}

/**
 * 그래프에 나타낼 범위를 구하기 위한 최대 최소 값을 구하고
 * 주가를 그래프 상의 위치로 변환시키기 위한 factor을 구한다.
 */
function setVolumeChartFactor() {
    //주가들의 최대 최소
    maxVolume = getMax(volume);
    
    maxVolume = Math.round(maxVolume);
        
    //최고 거래량을 3으로 나누어 떨어지는 수로 설정
    while ( maxVolume % 3 != 0 ) {
        maxVolume++;
    }
    
    //0 ~ 최고 거래량의 차이 25% ( 라인 사이의 간격 )
    this.chartDiff = (maxVolume / 3) - ((maxVolume/3) % 1000);
    //거래량의 차이를 그래프의 차이로 변환하기 위한 Factor
    convertFactor = (this.gridLineCount-1) * this.heightGap / maxVolume;    
}

/**
 * StockChart.drawBackground
 * 바탕의 격자를 그린다.
 */
function drawVolumeChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2+10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
     	    	
    this.ctx.fillStyle = volumeBarColor;
    this.ctx.fillText("Volume", posX, posY);   
}



//Quote 값을 Graph의 값으로 변경
function drawVolumeChart() {
     var barGap = 1;    
 	var startX, barWidth, posBar, posBottom;
    posBottom = this.heightPadding + ( this.heightGap * (this.gridLineCount-1) );
    
    
    startX = chartWidthPadding + ( chartWidthGap/2 ) + chartWidthGap/4  + barGap ;
    barWidth = chartWidthGap - barGap*2;
    for ( var d = index; d < index+dayCount && d < size; d++ ) {
        posBar = this.convertData(volume[d]); 
        if ( open[d] > close[d] )
            this.ctx.fillStyle = negativeColor;
        else
            this.ctx.fillStyle = positiveColor;                    
        this.ctx.fillRect(startX, posBar, barWidth, posBottom-posBar);
        startX += chartWidthGap;
    }  
}

/**
 * 실제 값을 차트의 위치로 변환
 */

function convertVolumeChartData(data) {
   return this.heightPadding +  ( (maxVolume-data) * convertFactor );      
}

/**
 * X축 Y축의 정보를 그린다.
 */

function drawVolumeChartRange() {
	var posX, posY;
	
	//글씨 폰트 설정
	this.ctx.fillStyle = textColor;
	this.ctx.font = rangeFont;

	//거래량의 Y축 정보가 그려징 화면의 x, y 좌표
	posX = chartWidthPadding - 30;
	posY = this.heightPadding + 3;

	//거래량의 Y축 정보를 그린다.
	var yAxisGap = maxVolume;
	for( var i = 0; i < this.gridLineCount-1; i++) {
		//rangeNum = maxVolume - (this.chartDiff * i );
		yAxisGap = Math.round(yAxisGap);
		this.ctx.fillText(commify(yAxisGap), posX, posY);
		posY += this.heightGap;
		yAxisGap -= this.chartDiff;
	}
	this.ctx.fillText(0, posX+10, posY);
}

function drawVolumeChartInfo(posX) {
	var posY = this.heightPadding + this.heightGap/2;
	
    if ( posX < canvasWidth/2 ) {
        posX += 30;    	
    }
    else {
     	posX -= 180;
    }
    
    this.ctx.fillStyle = extraInfoBoxColor;
    this.ctx.fillRect(posX, posY, 150, this.heightGap * 2 ); 
    
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("날짜 : " + calendar[drawIndex], posX+15, posY+25);
    this.ctx.fillText("거래량 : " + commify(volume[drawIndex]), posX+15, posY+45);        
}
