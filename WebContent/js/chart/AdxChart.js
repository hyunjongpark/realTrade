var adx = [];
var adxMa = [];

var maxAdx;
var minAdx;

function AdxChart(ad, adm) {
	/* Variable */
	this.canvas = document.getElementById("adxChartCanvas");
	this.canvas.onmousemove = adxChartMouseMove;
	this.ctx = this.canvas.getContext('2d');
	this.canvasHeight = 120;
	this.heightPadding = 25;
	this.heightGap = 30;
	this.gridLineCount = 4;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setAdxChartFactor;
	this.convertData = convertAdxChartData;	
	this.drawLineChart = drawLineChart;
	this.drawRange = drawAdxChartRange;
	this.drawBackground = drawBackground;
	this.drawChartName = drawAdxChartName;	
	this.draw = drawAdxChartAll;
	this.drawInfo = drawAdxChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    adx = ad;
    adxMa = adm
    this.chartDisable();
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawAdxChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();
    this.drawChartName();    
    
    this.drawLineChart(adx, adxLineColor);
    this.drawLineChart(adxMa, adxMaLineColor);
	this.drawRange();  
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setAdxChartFactor() {
    //�ְ����� �ִ� �ּ�
    minAdx = Math.min(getMin(adx), getMin(adxMa));
    maxAdx = Math.max(getMax(adx), getMax(adxMa));    
    
    if ( maxAdx == 0 && minAdx == 0 )
        maxAdx = 100;
	
	this.chartDiff = (maxAdx-minAdx)/3;	   
	this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * ADX ��Ʈ�� �̸��� �׸���.
 */
function drawAdxChartName() {
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
     	    	
    this.ctx.fillStyle = adxLineColor;
    this.ctx.fillText("ADX", posX, posY);   
    
    this.ctx.fillStyle = adxMaLineColor;
    this.ctx.fillText("MA", posX+100, posY);   
}


/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertAdxChartData(data) {   
    return this.heightPadding + ( (maxAdx - data) * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawAdxChartRange() {
	var posX, posY;

	//�۾� ��Ʈ ����
	this.ctx.fillStyle = textColor;
	this.ctx.font = rangeFont;

	//�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
	posX = chartWidthPadding - 25;
	posY = this.heightPadding+3;

	//�ŷ����� Y�� ������ �׸���.
	var yAxisGap = maxAdx;
	for( var i = 4; i > 0; i--) {		
	    yAxisGap = roundXL(yAxisGap, 2);
		this.ctx.fillText(commify(yAxisGap), posX, posY );
		posY += this.heightGap;
		yAxisGap -= this.chartDiff;
	}
		
}

function drawAdxChartInfo(posX) {
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
    this.ctx.fillText("ADX : " + commify(adx[drawIndex]), posX, posY+35);
    this.ctx.fillText("MA : " + commify(adxMa[drawIndex]), posX, posY+50);  
}



