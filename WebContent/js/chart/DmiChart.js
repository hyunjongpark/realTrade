var pDmi = [];
var mDmi = [];

var maxDmi;
var minDmi;

function DmiChart(pdm, mdm) {
    /* Variable */
    this.canvas = document.getElementById("dmiChartCanvas");
    this.canvas.onmousemove = dmiChartMouseMove;
    this.ctx = this.canvas.getContext('2d');
    this.canvasHeight = 120;
    this.heightPadding = 25;
    this.heightGap = 30;
    this.gridLineCount = 4;
    this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
    this.convertFactor = 0;
    this.chartDiff = 0;
    
    /* Function */
    this.setFactor = setDmiChartFactor;
    this.convertData = convertDmiChartData; 
    this.drawLineChart = drawLineChart;
    this.drawRange = drawDmiChartRange;
    this.drawBackground = drawBackground;
    this.drawChartName = drawDmiChartName;  
    this.draw = drawDmiChartAll;
    this.drawInfo = drawDmiChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    pDmi = pdm;
    mDmi = mdm;
    this.chartDisable();
}

/**
 * 차트를 그리는데 필요한 모든 정보를 그린다.
 */
function drawDmiChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();
    this.drawChartName();    
    
    this.drawLineChart(pDmi, pDmiLineColor);
    this.drawLineChart(mDmi, mDmiLineColor);
    this.drawRange();  
}

/**
 * 그래프에 나타낼 범위를 구하기 위한 최대 최소 값을 구하고
 * 주가를 그래프 상의 위치로 변환시키기 위한 factor을 구한다.
 */
function setDmiChartFactor() {
    //주가들의 최대 최소
    minDmi = Math.min(getMin(pDmi), getMin(mDmi));
    maxDmi = Math.max(getMax(pDmi), getMax(mDmi));    
    
    if ( maxDmi == 0 && minDmi == 0 )
        maxDmi = 100;
    
    this.chartDiff = (maxDmi-minDmi)/3;    
    this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * Plus DMI 차트의 이름을 그린다.
 */
function drawDmiChartName() {
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
                
    this.ctx.fillStyle = pDmiLineColor;
    this.ctx.fillText("Plus DMI", posX, posY);   
    
    this.ctx.fillStyle = mDmiLineColor;
    this.ctx.fillText("Minus DMI", posX+100, posY);   
}


/**
 * 실제 값을 차트의 위치로 변환
 */

function convertDmiChartData(data) {   
    return this.heightPadding + ( (maxDmi - data) * this.convertFactor );    
}

/**
 * X축 Y축의 정보를 그린다.
 */

function drawDmiChartRange() {
    var posX, posY;

    //글씨 폰트 설정
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //거래량의 Y축 정보가 그려징 화면의 x, y 좌표
    posX = chartWidthPadding - 25;
    posY = this.heightPadding+3;

    //거래량의 Y축 정보를 그린다.
    var yAxisGap = maxDmi;
    for( var i = 4; i > 0; i--) {       
        yAxisGap = roundXL(yAxisGap, 2);
        this.ctx.fillText(commify(yAxisGap), posX, posY );
        posY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }
        
}

function drawDmiChartInfo(posX) {
    var posY = this.heightPadding + this.heightGap/2;
        
    if ( posX < canvasWidth/2 ) {
        posX += 30;     
    }
    else {
        posX -= 180;
    }
    
    this.ctx.fillStyle = extraInfoBoxColor;
    this.ctx.fillRect(posX, posY, 150, this.heightGap*2 ); 
    posX += 15;
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("날짜 : " + calendar[drawIndex], posX, posY+20);
    this.ctx.fillText("Plus DMI : " + commify(pDmi[drawIndex]), posX, posY+35);
    this.ctx.fillText("Minus DMI : " + commify(mDmi[drawIndex]), posX, posY+50);  
}



