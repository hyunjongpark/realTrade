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
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
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
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setCciChartFactor() {
    //�ְ����� �ִ� �ּ�
    minCci = getMin(cci);
    maxCci = getMax(cci);    
    
    this.chartDiff = (maxCci-minCci)/3;    
    this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * ��Ʈ �̸��� ����.
 */
function drawCciChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
                
    this.ctx.fillStyle = cciLineColor;
    this.ctx.fillText("CCI", posX, posY);   
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertCciChartData(data) {   
    return this.heightPadding + ( (maxCci - data) * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawCciChartRange() {
    var posX, posY;

    //�۾� ��Ʈ ����
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
    posX = chartWidthPadding - 25;
    posY = this.heightPadding+3;

    //�ŷ����� Y�� ������ �׸���.
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
    this.ctx.fillText("��¥ : " + calendar[drawIndex], posX+15, posY+20);
    this.ctx.fillText("CCI : " + commify(cci[drawIndex]), posX+15, posY+40);        
}



