var maxMacd;
var minMacd;

var macd = [];
var signal = [];

function MacdChart(mac, si) {
	/* Variable */
	this.canvas = document.getElementById("macdChartCanvas");
	this.canvas.onmousemove = chartMouseMove;
	this.ctx = this.canvas.getContext('2d');
	this.canvasHeight = 155;
	this.heightPadding = 25;
	this.heightGap = 30;
	this.gridLineCount = 5;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);	   
    this.ctx.canvas.width = canvasWidth;
    this.ctx.canvas.height = this.canvasHeight;
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setMacdChartFactor;
	this.convertData = convertMacdChartData;
	//this.drawOscillatorChart = drawMacdOscillatorChart;
	this.drawLineChart = drawLineChart;
	this.drawRange = drawMacdChartRange;
	this.drawBackground = drawBackground;	
	this.drawChartName = drawMacdChartName;
	this.draw = drawMacdChartAll;
	this.drawInfo = drawMacdChartInfo;
    macd = mac;
    signal = si;
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawMacdChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);
    
    this.setFactor();
    this.drawBackground();    
    this.drawLineChart(macd, macdColor);
	this.drawLineChart(signal, signalColor);
	//this.drawOscillatorChart();
	this.drawChartName();
	this.drawRange();  
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setMacdChartFactor() {
    //�ְ����� �ִ� �ּ�
    minMacd = getMin(macd);
    maxMacd = getMax(macd);
    
    minMacd = Math.min(getMin(signal), minMacd);
    maxMacd = Math.max(getMax(signal), maxMacd);
    
    maxMacd = Math.round(maxMacd);
    minMacd = Math.round(minMacd);

	//�ְ��� 2���� ������ �������� ���� ����
    while ( maxMacd % 2 != 0 ) {
        maxMacd++;
    }
    if ( minMacd < 0 ) {
   		while ( minMacd % 2 != 0 ) {
        	minMacd--;
    	}   
	}
	//���� �Ǵ� ����� ������ �� �� ������ �����Ѵ�.
	if ( (maxMacd/2) > (minMacd/2*-1) ) 
		this.chartDiff = maxMacd/2;
	else	
		this.chartDiff = (-minMacd)/2;
		
    if ( this.chartDiff <= 2 )  
         this.chartDiff = 1;
         
    else if ( this.chartDiff == 0 )
        this.chartDiff = 3000;
    
	this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * Macd ������ ���� ���� ǥ���� �ش�.
 */
function drawMacdChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2 +10;
    var posY = this.heightPadding/2 +5;
    this.ctx.font = chartFont;
     	    	
    this.ctx.fillStyle = macdColor;
    this.ctx.fillText("MACD", posX, posY);    
    this.ctx.fillStyle = signalColor;
    this.ctx.fillText("Signal", posX+60, posY);
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertMacdChartData(data) {
	return ( this.heightPadding + (this.heightGap*2) ) -  (data * this.convertFactor);      
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawMacdChartRange() {
	var posX, posY;

	//�۾� ��Ʈ ����
	this.ctx.fillStyle = textColor;
	this.ctx.font = rangeFont;

	//�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
	posX = chartWidthPadding - 30;
	posY = this.heightPadding+3;
	
	//�ŷ����� Y�� ������ �׸���.
	for( var i = 2; i > 0; i--) {
		rangeNum = this.chartDiff * i;
		rangeNum = roundXL(rangeNum, money);
		this.ctx.fillText(commify(rangeNum), posX, posY );
		posY += this.heightGap;
	}
		
	this.ctx.fillText(0, posX+10, posY);
	posY += this.heightGap;
	for ( var j = 1; j <= 2; j++ ) {
	    rangeNum = this.chartDiff * j * -1;
	    rangeNum = roundXL(rangeNum, money);
        this.ctx.fillText(commify(rangeNum), posX, posY );
        posY += this.heightGap;
	}
}

function drawMacdChartInfo(posX) {
	var posY = this.heightPadding + this.heightGap/2;
		
    if ( posX < canvasWidth/2 ) {
        posX += 30;    	
    }
    else {
     	posX -= 180;
    }
    
    this.ctx.fillStyle = extraInfoBoxColor;
    this.ctx.fillRect(posX, posY, 150, this.heightGap*2.5 ); 
    
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("��¥ : " + calendar[drawIndex], posX+15, posY+20);
    this.ctx.fillText("MACD : " + commify(macd[drawIndex]), posX+15, posY+40);
    this.ctx.fillText("Signal : " + commify(signal[drawIndex]), posX+15, posY+60);
  //  this.ctx.fillText("Osc : " + commify(oscillator[drawIndex]), posX+15, posY+80);        
}



