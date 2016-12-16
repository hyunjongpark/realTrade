var trix = [];

var minTrix;
var maxTrix;

function TrixChart(tr) {
    /* Variable */
    this.canvas = document.getElementById("trixChartCanvas");
    this.canvas.onmousemove = trixChartMouseMove;
    this.ctx = this.canvas.getContext('2d');
    this.canvasHeight = 120;
    this.heightPadding = 25;
    this.heightGap = 30;
    this.gridLineCount = 4;
    this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
    this.convertFactor = 0;
    this.chartDiff = 0;
    
    /* Function */
    this.setFactor = setTrixChartFactor;
    this.convertData = convertTrixChartData; 
    this.drawLineChart = drawLineChart;
    this.drawRange = drawTrixChartRange;
    this.drawBackground = drawBackground;   
    this.drawChartName = drawTrixChartName;
    this.draw = drawTrixChartAll;
    this.drawInfo = drawTrixChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    trix = tr;
    this.chartDisable();
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawTrixChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();    
    this.drawLineChart(trix, trixLineColor);
    this.drawRange();  
    this.drawChartName();
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setTrixChartFactor() {
    //�ְ����� �ִ� �ּ�
    minTrix = getMin(trix);
    maxTrix = getMax(trix);    
    
    this.chartDiff = (maxTrix-minTrix)/3;    
    this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * ��Ʈ �̸��� ����.
 */
function drawTrixChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
                
    this.ctx.fillStyle = trixLineColor;
    this.ctx.fillText("TRIX", posX, posY);   
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertTrixChartData(data) {   
    return this.heightPadding + ( (maxTrix - data) * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawTrixChartRange() {
    var posX, posY;

    //�۾� ��Ʈ ����
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
    posX = chartWidthPadding - 25;
    posY = this.heightPadding+3;

    //�ŷ����� Y�� ������ �׸���.
    var yAxisGap = maxTrix;
    for( var i = 4; i > 0; i--) {       
        yAxisGap = roundXL(yAxisGap, 2);
        this.ctx.fillText(commify(yAxisGap), posX, posY );
        posY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }
        
}

function drawTrixChartInfo(posX) {
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
    this.ctx.fillText("TRIX : " + commify(trix[drawIndex]), posX+15, posY+40);        
}



