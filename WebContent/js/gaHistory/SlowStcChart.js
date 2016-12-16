var slowStcD = [];
var slowStcK = [];

var maxSlowStc;
var minSlowStc;

function SlowStcChart(sk, sd) {
    /* Variable */
    this.canvas = document.getElementById("sstcChartCanvas");
    this.canvas.onmousemove = chartMouseMove;
    this.ctx = this.canvas.getContext('2d');
    this.canvasHeight = 120;
    this.heightPadding = 25;
    this.heightGap = 15;
    this.gridLineCount = 6;
    this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
    this.ctx.canvas.width = canvasWidth;
    this.ctx.canvas.height = this.canvasHeight;
    this.convertFactor = 0;
    this.chartDiff = 0;
    
    /* Function */
    this.setFactor = setSlowStcChartFactor;
    this.convertData = convertSlowStcChartData; 
    this.drawLineChart = drawLineChart;
    this.drawRange = drawSlowStcChartRange;
    this.drawBackground = drawBackground;
    this.drawChartName = drawSlowStcChartName;  
    this.draw = drawSlowStcChartAll;
    this.drawInfo = drawSlowStcChartInfo;
    
    slowStcK = sk;
    slowStcD = sd;
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawSlowStcChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();
    this.drawChartName();    
     
    this.drawLineChart(slowStcD, stcDLineColor);
    this.drawLineChart(slowStcK, stcKLineColor);
    this.drawRange();  
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setSlowStcChartFactor() {
    this.chartDiff = 20;       
    this.convertFactor = this.heightGap / this.chartDiff; 
}

/**
 * Plus DMI ��Ʈ�� �̸��� �׸���.
 */
function drawSlowStcChartName() {
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
                
    this.ctx.fillStyle = stcKLineColor;
    this.ctx.fillText("Slow %K", posX, posY);   
    
    this.ctx.fillStyle = stcDLineColor;
    this.ctx.fillText("Slow %D", posX+100, posY);   
}


/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertSlowStcChartData(data) {   
     return this.heightPadding + ( (100 - data) * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawSlowStcChartRange() {
    var posX, posY;

    //�۾� ��Ʈ ����
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
    posX = chartWidthPadding - 25;
    posY = this.heightPadding+3;

    //�ŷ����� Y�� ������ �׸���.
    var yAxisGap = 100;
    for( i = 6; i > 0; i--) {       
        yAxisGap = Math.round(yAxisGap);
        this.ctx.fillText(yAxisGap, posX, posY );
        posY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }        
}

function drawSlowStcChartInfo(posX) {
    var posY = this.heightPadding + this.heightGap/2;
        
    if ( posX < canvasWidth/2 ) {
        posX += 30;     
    }
    else {
        posX -= 180;
    }
    
    this.ctx.fillStyle = extraInfoBoxColor;
    this.ctx.fillRect(posX, posY, 150, this.heightGap*4 ); 
    posX += 15;
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("��¥ : " + calendar[drawIndex], posX, posY+20);
    this.ctx.fillText("%K : " + commify(slowStcK[drawIndex]), posX, posY+35);
    this.ctx.fillText("%D : " + commify(slowStcD[drawIndex]), posX, posY+50);  
}



