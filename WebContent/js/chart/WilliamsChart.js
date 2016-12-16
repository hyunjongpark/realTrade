var williams = [];

var minWilliams;
var maxWilliams;

function WilliamsChart(wi) {
    /* Variable */
    this.canvas = document.getElementById("williamsChartCanvas");
    this.canvas.onmousemove = williamsChartMouseMove;
    this.ctx = this.canvas.getContext('2d');
    this.canvasHeight = 120;
    this.heightPadding = 25;
    this.heightGap = 30;
    this.gridLineCount = 4;
    this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
    this.convertFactor = 0;
    this.chartDiff = 0;
    
    /* Function */
    this.setFactor = setWilliamsChartFactor;
    this.convertData = convertWilliamsChartData; 
    this.drawLineChart = drawLineChart;
    this.drawRange = drawWilliamsChartRange;
    this.drawBackground = drawBackground;   
    this.drawChartName = drawWilliamsChartName;
    this.draw = drawWilliamsChartAll;
    this.drawInfo = drawWilliamsChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    williams = wi;
    this.chartDisable();
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawWilliamsChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();    
    this.drawLineChart(williams, williamsLineColor);
    this.drawRange();  
    this.drawChartName();
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setWilliamsChartFactor() {
    //�ְ����� �ִ� �ּ�
    minWilliams = getMin(williams);
    maxWilliams = getMax(williams);    
    
    this.chartDiff = (maxWilliams-minWilliams)/3;    
    this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * ��Ʈ �̸��� ����.
 */
function drawWilliamsChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
                
    this.ctx.fillStyle = williamsLineColor;
    this.ctx.fillText("Williams", posX, posY);   
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertWilliamsChartData(data) {   
    return this.heightPadding + ( (maxWilliams - data) * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawWilliamsChartRange() {
    var posX, posY;

    //�۾� ��Ʈ ����
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
    posX = chartWidthPadding - 25;
    posY = this.heightPadding+3;

    //�ŷ����� Y�� ������ �׸���.
    var yAxisGap = maxWilliams;
    for( var i = 4; i > 0; i--) {       
        yAxisGap = Math.round(yAxisGap);
        this.ctx.fillText(commify(yAxisGap), posX, posY );
        posY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }
        
}

function drawWilliamsChartInfo(posX) {
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
    this.ctx.fillText("Williams : " + commify(williams[drawIndex]), posX+15, posY+40);        
}



