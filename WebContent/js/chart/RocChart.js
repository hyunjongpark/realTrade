var roc = [];

var minRoc;
var maxRoc;

function RocChart(ro) {
    /* Variable */
    this.canvas = document.getElementById("rocChartCanvas");
    this.canvas.onmousemove = rocChartMouseMove;
    this.ctx = this.canvas.getContext('2d');
    this.canvasHeight = 120;
    this.heightPadding = 25;
    this.heightGap = 30;
    this.gridLineCount = 4;
    this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
    this.convertFactor = 0;
    this.chartDiff = 0;
    
    /* Function */
    this.setFactor = setRocChartFactor;
    this.convertData = convertRocChartData; 
    this.drawLineChart = drawLineChart;
    this.drawRange = drawRocChartRange;
    this.drawBackground = drawBackground;   
    this.drawChartName = drawRocChartName;
    this.draw = drawRocChartAll;
    this.drawInfo = drawRocChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    roc = ro;
    this.chartDisable();
}

/**
 * 차트를 그리는데 필요한 모든 정보를 그린다.
 */
function drawRocChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();    
    this.drawLineChart(roc, rocLineColor);
    this.drawRange();  
    this.drawChartName();
}

/**
 * 그래프에 나타낼 범위를 구하기 위한 최대 최소 값을 구하고
 * 주가를 그래프 상의 위치로 변환시키기 위한 factor을 구한다.
 */
function setRocChartFactor() {
    //주가들의 최대 최소
    minRoc = getMin(roc);
    maxRoc = getMax(roc);    
    
    this.chartDiff = (maxRoc-minRoc)/3;    
    this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * 차트 이름을 쓴다.
 */
function drawRocChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
                
    this.ctx.fillStyle = rocLineColor;
    this.ctx.fillText("ROC", posX, posY);   
}

/**
 * 실제 값을 차트의 위치로 변환
 */

function convertRocChartData(data) {   
    return this.heightPadding + ( (maxRoc - data) * this.convertFactor );    
}

/**
 * X축 Y축의 정보를 그린다.
 */

function drawRocChartRange() {
    var posX, posY;

    //글씨 폰트 설정
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //거래량의 Y축 정보가 그려징 화면의 x, y 좌표
    posX = chartWidthPadding - 25;
    posY = this.heightPadding+3;

    //거래량의 Y축 정보를 그린다.
    var yAxisGap = maxRoc;
    for( var i = 4; i > 0; i--) {       
        yAxisGap = roundXL(yAxisGap, 2);
        this.ctx.fillText(commify(yAxisGap), posX, posY );
        posY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }
        
}

function drawRocChartInfo(posX) {
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
    this.ctx.fillText("ROC : " + commify(roc[drawIndex]), posX+15, posY+40);        
}



