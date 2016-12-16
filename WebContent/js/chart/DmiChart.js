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
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
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
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setDmiChartFactor() {
    //�ְ����� �ִ� �ּ�
    minDmi = Math.min(getMin(pDmi), getMin(mDmi));
    maxDmi = Math.max(getMax(pDmi), getMax(mDmi));    
    
    if ( maxDmi == 0 && minDmi == 0 )
        maxDmi = 100;
    
    this.chartDiff = (maxDmi-minDmi)/3;    
    this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * Plus DMI ��Ʈ�� �̸��� �׸���.
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
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertDmiChartData(data) {   
    return this.heightPadding + ( (maxDmi - data) * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawDmiChartRange() {
    var posX, posY;

    //�۾� ��Ʈ ����
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
    posX = chartWidthPadding - 25;
    posY = this.heightPadding+3;

    //�ŷ����� Y�� ������ �׸���.
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
    this.ctx.fillText("��¥ : " + calendar[drawIndex], posX, posY+20);
    this.ctx.fillText("Plus DMI : " + commify(pDmi[drawIndex]), posX, posY+35);
    this.ctx.fillText("Minus DMI : " + commify(mDmi[drawIndex]), posX, posY+50);  
}



