var obv = [];

var minObv;
var maxObv;

function ObvChart(ob) {
	/* Variable */
	this.canvas = document.getElementById("obvChartCanvas");
	this.canvas.onmousemove = obvChartMouseMove;
	this.ctx = this.canvas.getContext('2d');
	this.canvasHeight = 120;
	this.heightPadding = 25;
	this.heightGap = 30;
	this.gridLineCount = 4;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setObvChartFactor;
	this.convertData = convertObvChartData;	
	this.drawLineChart = drawLineChart;
	this.drawRange = drawObvChartRange;
	this.drawBackground = drawBackground;	
	this.drawChartName = drawObvChartName;
	this.draw = drawObvChartAll;
	this.drawInfo = drawObvChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    obv = ob;
    this.chartDisable();
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawObvChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();    
    this.drawLineChart(obv, obvLineColor);
	this.drawRange();  
	this.drawChartName();
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setObvChartFactor() {
    //�ְ����� �ִ� �ּ�
    minObv = getMin(obv);
    maxObv = getMax(obv);    
	
	this.chartDiff = (maxObv-minObv)/3;	   
	this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * ��Ʈ �̸��� ����.
 */
function drawObvChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
     	    	
    this.ctx.fillStyle = obvLineColor;
    this.ctx.fillText("OBV", posX, posY);   
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertObvChartData(data) {   
    return this.heightPadding + ( (maxObv - data) * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawObvChartRange() {
	var posX, posY;

	//�۾� ��Ʈ ����
	this.ctx.fillStyle = textColor;
	this.ctx.font = rangeFont;

	//�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
	posX = chartWidthPadding - 25;
	posY = this.heightPadding+3;

	//�ŷ����� Y�� ������ �׸���.
	var yAxisGap = maxObv;
	for( var i = 4; i > 0; i--) {		
		yAxisGap = Math.round(yAxisGap);
		this.ctx.fillText(commify(yAxisGap), posX, posY );
		posY += this.heightGap;
		yAxisGap -= this.chartDiff;
	}
		
}

function drawObvChartInfo(posX) {
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
    this.ctx.fillText("OBV : " + commify(obv[drawIndex]), posX+15, posY+40);        
}



