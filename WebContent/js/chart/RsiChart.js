var rsi = [];

var minRsi;
var maxRsi;

function RsiChart(rs) {
	/* Variable */
	this.canvas = document.getElementById("rsiChartCanvas");
	this.canvas.onmousemove = rsiChartMouseMove;
	this.ctx = this.canvas.getContext('2d');
	this.canvasHeight = 120;
	this.heightPadding = 25;
	this.heightGap = 20;
	this.gridLineCount = 5;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setRsiChartFactor;
	this.convertData = convertRsiChartData;	
	this.drawLineChart = drawLineChart;
	this.drawRange = drawRsiChartRange;
	this.drawBackground = drawBackground;
	this.drawChartName = drawRsiChartName;	
	this.draw = drawRsiChartAll;
	this.drawInfo = drawRsiChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    rsi = rs;

    this.chartDisable();
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawRsiChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);    
    this.setFactor();
    this.drawBackground();    
    this.drawLineChart(rsi, rsiLineColor);
	this.drawRange();  
	this.drawChartName();
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setRsiChartFactor() {
	this.chartDiff = 25;	   
	this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * ��Ʈ �̸��� �׸���.
 */
function drawRsiChartName() {  
    var posX = chartWidthPadding + chartWidthGap/2 + 10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
     	    	
    this.ctx.fillStyle = rsiLineColor;
    this.ctx.fillText("RSI", posX, posY);   
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertRsiChartData(data) {   
    return this.heightPadding + ( (100 - data) * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawRsiChartRange() {
	var posX, posY;

	//�۾� ��Ʈ ����
	this.ctx.fillStyle = textColor;
	this.ctx.font = rangeFont;

	//�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
	posX = chartWidthPadding - 25;
	posY = this.heightPadding+3;

	//�ŷ����� Y�� ������ �׸���.
	var yAxisGap = 100;
	for( i = 5; i > 0; i--) {		
		yAxisGap = Math.round(yAxisGap);
		this.ctx.fillText(yAxisGap, posX, posY );
		posY += this.heightGap;
		yAxisGap -= this.chartDiff;
	}
}


function drawRsiChartInfo(posX) {
	var posY = this.heightPadding + this.heightGap/2;
		
    if ( posX < canvasWidth/2 ) {
        posX += 30;    	
    }
    else {
     	posX -= 180;
    }
    
    this.ctx.fillStyle = extraInfoBoxColor;
    this.ctx.fillRect(posX, posY, 150, this.heightGap*3 ); 
    
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("��¥ : " + calendar[drawIndex], posX+15, posY+25);
    this.ctx.fillText("RSI : " + commify(rsi[drawIndex]), posX+15, posY+45);        
}



