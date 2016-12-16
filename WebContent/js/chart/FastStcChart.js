var fastStcD = [];
var fastStcK = [];

var maxFastStc;
var minFastStc;

function FastStcChart(fk, fd) {
    /* Variable */
    this.canvas = document.getElementById("fstcChartCanvas");
    this.canvas.onmousemove = fastStcChartMouseMove;
    this.ctx = this.canvas.getContext('2d');
    this.canvasHeight = 120;
    this.heightPadding = 25;
    this.heightGap = 30;
    this.gridLineCount = 4;
    this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
    this.convertFactor = 0;
    this.chartDiff = 0;
    
    /* Function */
    this.setFactor = setFastStcChartFactor;
    this.convertData = convertFastStcChartData; 
    this.drawLineChart = drawLineChart;
    this.drawRange = drawFastStcChartRange;
    this.drawBackground = drawBackground;
    this.drawChartName = drawFastStcChartName;  
    this.draw = drawFastStcChartAll;
    this.drawInfo = drawFastStcChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    fastStcD = fd;
    fastStcK = fk
    this.chartDisable();
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawFastStcChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();
    this.drawChartName();    
    
    this.drawLineChart(fastStcD, stcDLineColor);
    this.drawLineChart(fastStcK, stcKLineColor);
    this.drawRange();  
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setFastStcChartFactor() {
    //�ְ����� �ִ� �ּ�
    minFastStc = Math.min(getMin(fastStcD), getMin(fastStcK));
    maxFastStc = Math.max(getMax(fastStcD), getMax(fastStcK));    
    
    if ( maxFastStc == 0 && minFastStc == 0 )
        maxFastStc = 100;
    
    this.chartDiff = (maxFastStc-minFastStc)/3;    
    this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * Plus DMI ��Ʈ�� �̸��� �׸���.
 */
function drawFastStcChartName() {
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
                
    this.ctx.fillStyle = stcKLineColor;
    this.ctx.fillText("Fast %K", posX, posY);   
    
    this.ctx.fillStyle = stcDLineColor;
    this.ctx.fillText("Fast %D", posX+100, posY);   
}


/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertFastStcChartData(data) {   
    return this.heightPadding + ( (maxFastStc - data) * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawFastStcChartRange() {
    var posX, posY;

    //�۾� ��Ʈ ����
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
    posX = chartWidthPadding - 25;
    posY = this.heightPadding+3;

    //�ŷ����� Y�� ������ �׸���.
    var yAxisGap = maxFastStc;
    for( var i = 4; i > 0; i--) {       
        yAxisGap = roundXL(yAxisGap, 2);
        this.ctx.fillText(commify(yAxisGap), posX, posY );
        posY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }
        
}

function drawFastStcChartInfo(posX) {
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
    this.ctx.fillText("��¥ : " + calendar[drawIndex], posX, posY+20);
    this.ctx.fillText("%K : " + commify(fastStcK[drawIndex]), posX, posY+35);
    this.ctx.fillText("%D : " + commify(fastStcD[drawIndex]), posX, posY+50);  
}



