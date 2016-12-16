var cci = [];

var minCci;
var maxCci;

function CciChart(cc) {
    /* Variable */
    this.canvas = document.getElementById("cciChartCanvas");
    this.canvas.onmousemove = cciChartMouseMove;
    this.ctx = this.canvas.getContext('2d');
    this.canvasHeight = 120;
    this.heightPadding = 25;
    this.heightGap = 30;
    this.gridLineCount = 4;
    this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
    this.convertFactor = 0;
    this.chartDiff = 0;
    
    /* Function */
    this.setFactor = setCciChartFactor;
    this.convertData = convertCciChartData; 
    this.drawLineChart = drawLineChart;
    this.drawRange = drawCciChartRange;
    this.drawBackground = drawBackground;   
    this.drawChartName = drawCciChartName;
    this.draw = drawCciChartAll;
    this.drawInfo = drawCciChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    cci = cc;
    this.chartDisable();
}

/**
 * 차트를 그리는데 필요한 모든 정보를 그린다.
 */
function drawCciChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();    
    this.drawLineChart(cci, cciLineColor);
    this.drawRange();  
    this.drawChartName();
}

/**
 * 그래프에 나타낼 범위를 구하기 위한 최대 최소 값을 구하고
 * 주가를 그래프 상의 위치로 변환시키기 위한 factor을 구한다.
 */
function setCciChartFactor() {
    //주가들의 최대 최소
    minCci = getMin(cci);
    maxCci = getMax(cci);    
    
    this.chartDiff = (maxCci-minCci)/3;    
    this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * 차트 이름을 쓴다.
 */
function drawCciChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
                
    this.ctx.fillStyle = cciLineColor;
    this.ctx.fillText("CCI", posX, posY);   
}

/**
 * 실제 값을 차트의 위치로 변환
 */

function convertCciChartData(data) {   
    return this.heightPadding + ( (maxCci - data) * this.convertFactor );    
}

/**
 * X축 Y축의 정보를 그린다.
 */

function drawCciChartRange() {
    var posX, posY;

    //글씨 폰트 설정
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //거래량의 Y축 정보가 그려징 화면의 x, y 좌표
    posX = chartWidthPadding - 25;
    posY = this.heightPadding+3;

    //거래량의 Y축 정보를 그린다.
    var yAxisGap = maxCci;
    for( var i = 4; i > 0; i--) {       
        yAxisGap = roundXL(yAxisGap, 2);
        this.ctx.fillText(commify(yAxisGap), posX, posY );
        posY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }
        
}

function drawCciChartInfo(posX) {
    var posY = this.heightPadding + this.heightGap/2;
        
    if ( posX < canvasWidth/2 ) {
        posX += 30;     
    }
    else {
        posX -= 180;
    }
    
    this.ctx.fillStyle = extraInfoBoxColor;
    this.ctx.fillRect(posX, posY, 150, this.heightGap*2 ); 
    
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("날짜 : " + calendar[drawIndex], posX+15, posY+20);
    this.ctx.fillText("CCI : " + commify(cci[drawIndex]), posX+15, posY+40);        
}



