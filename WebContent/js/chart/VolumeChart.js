var maxVolume;

var volume = [];

function VolumeChart(v) {
	/* Variable */
	this.canvas = document.getElementById("volumeChartCanvas");
	this.ctx = this.canvas.getContext('2d');
	this.canvas.onmousemove = volumeChartMouseMove;
	this.canvasHeight = 120;
	this.heightPadding = 25;
	this.heightGap = 30;
	this.gridLineCount = 4;
	this.chartHeight = this.heightGap * ( this.gridLineCount - 1);
	this.convertFactor = 0;
	this.chartDiff = 0;
	
	/* Function */
    this.setFactor = setVolumeChartFactor;
	this.convertData = convertVolumeChartData;
	this.drawChart = drawVolumeChart;
	this.drawRange = drawVolumeChartRange;
	this.drawBackground = drawBackground;	
	this.drawChartName = drawVolumeChartName;
	this.draw = drawVolumeChartAll;
	this.drawInfo = drawVolumeChartInfo;


    this.chartDisable = chartDisable;
    this.chartEnable = chartEnable;
	
    volume = v;
    
    this.chartEnable();
    
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawVolumeChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);
 	this.setFactor();
    this.drawBackground();    
    this.drawChart();   
    this.drawRange();     
    this.drawChartName();
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setVolumeChartFactor() {
    //�ְ����� �ִ� �ּ�
    maxVolume = getMax(volume);
    
    maxVolume = Math.round(maxVolume);
        
    //�ְ� �ŷ����� 3���� ������ �������� ���� ����
    while ( maxVolume % 3 != 0 ) {
        maxVolume++;
    }
    
    //0 ~ �ְ� �ŷ����� ���� 25% ( ���� ������ ���� )
    this.chartDiff = (maxVolume / 3) - ((maxVolume/3) % 1000);
    //�ŷ����� ���̸� �׷����� ���̷� ��ȯ�ϱ� ���� Factor
    convertFactor = (this.gridLineCount-1) * this.heightGap / maxVolume;    
}

/**
 * StockChart.drawBackground
 * ������ ���ڸ� �׸���.
 */
function drawVolumeChartName() {    
    var posX = chartWidthPadding + chartWidthGap/2+10;
    var posY = this.heightPadding/2 + 5;
    this.ctx.font = chartFont;
     	    	
    this.ctx.fillStyle = volumeBarColor;
    this.ctx.fillText("Volume", posX, posY);   
}



//Quote ���� Graph�� ������ ����
function drawVolumeChart() {
     var barGap = 1;    
 	var startX, barWidth, posBar, posBottom;
    posBottom = this.heightPadding + ( this.heightGap * (this.gridLineCount-1) );
    
    
    startX = chartWidthPadding + ( chartWidthGap/2 ) + chartWidthGap/4  + barGap ;
    barWidth = chartWidthGap - barGap*2;
    for ( var d = index; d < index+dayCount && d < size; d++ ) {
        posBar = this.convertData(volume[d]); 
        if ( open[d] > close[d] )
            this.ctx.fillStyle = negativeColor;
        else
            this.ctx.fillStyle = positiveColor;                    
        this.ctx.fillRect(startX, posBar, barWidth, posBottom-posBar);
        startX += chartWidthGap;
    }  
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */

function convertVolumeChartData(data) {
   return this.heightPadding +  ( (maxVolume-data) * convertFactor );      
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawVolumeChartRange() {
	var posX, posY;
	
	//�۾� ��Ʈ ����
	this.ctx.fillStyle = textColor;
	this.ctx.font = rangeFont;

	//�ŷ����� Y�� ������ �׷�¡ ȭ���� x, y ��ǥ
	posX = chartWidthPadding - 30;
	posY = this.heightPadding + 3;

	//�ŷ����� Y�� ������ �׸���.
	var yAxisGap = maxVolume;
	for( var i = 0; i < this.gridLineCount-1; i++) {
		//rangeNum = maxVolume - (this.chartDiff * i );
		yAxisGap = Math.round(yAxisGap);
		this.ctx.fillText(commify(yAxisGap), posX, posY);
		posY += this.heightGap;
		yAxisGap -= this.chartDiff;
	}
	this.ctx.fillText(0, posX+10, posY);
}

function drawVolumeChartInfo(posX) {
	var posY = this.heightPadding + this.heightGap/2;
	
    if ( posX < canvasWidth/2 ) {
        posX += 30;    	
    }
    else {
     	posX -= 180;
    }
    
    this.ctx.fillStyle = extraInfoBoxColor;
    this.ctx.fillRect(posX, posY, 150, this.heightGap * 2 ); 
    
    this.ctx.font = infoFont;    
    this.ctx.fillStyle = textColor;
    this.ctx.fillText("��¥ : " + calendar[drawIndex], posX+15, posY+25);
    this.ctx.fillText("�ŷ��� : " + commify(volume[drawIndex]), posX+15, posY+45);        
}
