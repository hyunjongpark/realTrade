var maxMacd;
var minMacd;

var macd = [];
var signal = [];
var oscillator = [];

function MacdChart(mac, si, os) {
	/* Variable */
	this.canvas = document.getElementById("macdChartCanvas");
	this.canvas.onmousemove = macdChartMouseMove;
	this.ctx = this.canvas.getContext('2d');
	this.canvasHeight = 155;
	this.heightPadding = 25;
	this.heightGap = 30;
	this.gridLineCount = 5;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setMacdChartFactor;
	this.convertData = convertMacdChartData;
	this.drawOscillatorChart = drawMacdOscillatorChart;
	this.drawLineChart = drawLineChart;
	this.drawRange = drawMacdChartRange;
	this.drawBackground = drawBackground;	
	this.drawChartName = drawMacdChartName;
	this.draw = drawMacdChartAll;
	this.drawInfo = drawMacdChartInfo;

    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;

    macd = mac;
    signal = si;
    oscillator = os;
 
    this.chartDisable();
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
	this.drawOscillatorChart();
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
    minMacd = Math.min(getMin(oscillator), minMacd);
    maxMacd = Math.max(getMax(oscillator), maxMacd);
    
    maxMacd = Math.round(maxMacd);
    minMacd = Math.round(minMacd);

	//�ְ��� 3000���� ������ �������� ���� ����
    while ( maxMacd % 3 != 0 ) {
        maxMacd++;
    }
    if ( minMacd < 0 ) {
   		while ( minMacd % 3 != 0 ) {
        	minMacd--;
    	}   
	}
	//���� �Ǵ� ����� ������ �� �� ������ �����Ѵ�.
	if ( (maxMacd/3) > (minMacd*-1) ) 
		this.chartDiff = maxMacd/3;
	else	
		this.chartDiff = -minMacd;
		
      
         
    if ( this.chartDiff == 0 )
        this.chartDiff = 0.5;
    
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
    this.ctx.fillStyle = oscillatorPositiveColor;
    this.ctx.fillText("Oscil", posX+120, posY);
    this.ctx.fillStyle = oscillatorNegativeColor;
    this.ctx.fillText("lator", posX+155, posY);   
}

//Quote ���� Graph�� ������ ����
function drawMacdOscillatorChart() {
    var barGap = 1;
	var startX, barWidth, posBar, posBottom;
    posBottom = this.heightPadding + ( this.heightGap * 3 );
    
    startX = chartWidthPadding + ( chartWidthGap/2 + chartWidthGap/4 ) + barGap;
    barWidth = chartWidthGap - barGap*2;
    
    for ( var d = index; d < index+dayCount && d < size; d++ ) {
    	//oscillator ���� �� ����
    	if ( oscillator[d] >= 0 ) 
    		this.ctx.fillStyle = oscillatorPositiveColor;
    	else
    		this.ctx.fillStyle = oscillatorNegativeColor;
    		
        posBar = this.convertData(oscillator[d]);
        //startX = chartWidthPadding +  d  * chartWidthGap + ( chartWidthGap/2 + chartWidthGap/4 );    
          
        this.ctx.fillRect(startX, posBar, barWidth, posBottom-posBar);
        startX += chartWidthGap;
        
    }  
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertMacdChartData(data) {
	return ( this.heightPadding + (this.heightGap*3) ) -  (data * this.convertFactor);      
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
	for( var i = 3; i > 0; i--) {
		rangeNum = this.chartDiff * i;
		if ( this.chartDiff < 10 )
		  rangeNum = roundXL(rangeNum, 2);
		else
		  rangeNum = roundXL(rangeNum, 0);
		this.ctx.fillText(commify(rangeNum), posX, posY );
		posY += this.heightGap;
	}
		
	this.ctx.fillText(0, posX+10, posY);
	posY += this.heightGap;
	
    if ( this.chartDiff < 10 )
        this.ctx.fillText(commify(-roundXL(this.chartDiff, 2)), posX, posY);
    else
        this.ctx.fillText(commify(-roundXL(this.chartDiff, 0)), posX, posY);
        
	

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
    this.ctx.fillRect(posX, posY, 150, this.heightGap*3 ); 
    
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("��¥ : " + calendar[drawIndex], posX+15, posY+20);
    this.ctx.fillText("MACD : " + commify(macd[drawIndex]), posX+15, posY+40);
    this.ctx.fillText("Signal : " + commify(signal[drawIndex]), posX+15, posY+60);
    this.ctx.fillText("Osc : " + commify(oscillator[drawIndex]), posX+15, posY+80);        
}



