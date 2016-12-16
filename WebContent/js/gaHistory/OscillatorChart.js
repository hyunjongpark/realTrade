var maxOsc;
var minOsc;

var oscillator = [];

function OscillatorChart(os) {
    /* Variable */
    this.canvas = document.getElementById("oscillatorChartCanvas");
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
    this.setFactor = setOscillatorChartFactor;
    this.convertData = convertOscillatorChartData;
    this.drawOscillatorChart = drawMacdOscillatorChart;
    this.drawRange = drawOscillatorChartRange;
    this.drawBackground = drawBackground;   
    this.drawChartName = drawOscillatorChartName;
    this.draw = drawOscillatorChartAll;
    this.drawInfo = drawOscillatorChartInfo;    
    oscillator = os; 
}

/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawOscillatorChartAll() {
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);
    
    this.setFactor();
    this.drawBackground();    
    this.drawOscillatorChart();
    this.drawChartName();
    this.drawRange();  
}

/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setOscillatorChartFactor() {
    //�ְ����� �ִ� �ּ�
    minOscillator = getMin(oscillator);
    maxOscillator = getMax(oscillator);

    minOscillator = Math.min(getMin(oscillator), minOscillator);
    maxOscillator = Math.max(getMax(oscillator), maxOscillator);
    
    maxOscillator = Math.round(maxOscillator);
    minOscillator = Math.round(minOscillator);

    //�ְ��� 3000���� ������ �������� ���� ����
    while ( maxOscillator % 2 != 0 ) {
        maxOscillator++;
    }
    if ( minOscillator < 0 ) {
        while ( minOscillator % 2 != 0 ) {
            minOscillator--;
        }   
    }
    //���� �Ǵ� ����� ������ �� �� ������ �����Ѵ�.
    if ( (maxOscillator) > (minOscillator*-1) ) 
        this.chartDiff = maxOscillator/2;
    else    
        this.chartDiff = (-minOscillator)/2;
        
    if ( this.chartDiff < 2 )  
         this.chartDiff = 1;
         
    else if ( this.chartDiff == 0 )
        this.chartDiff = 3000;
    
    this.convertFactor = this.heightGap / this.chartDiff;    
}

/**
 * Macd ������ ���� ���� ǥ���� �ش�.
 */
function drawOscillatorChartName() {    
    var posX = chartWidthPadding;
    var posY = this.heightPadding/2 +5;
    this.ctx.font = chartFont;
                
    this.ctx.fillStyle = oscillatorPositiveColor;
    this.ctx.fillText("Oscil", posX+5, posY);
    this.ctx.fillStyle = oscillatorNegativeColor;
    this.ctx.fillText("lator", posX+40, posY);   
}

//Quote ���� Graph�� ������ ����
function drawMacdOscillatorChart() {    
    var barGap = 1 - dayCount/250;
    var startX, barWidth, posBar, posBottom;
    posBottom = this.heightPadding + ( this.heightGap * 2 );
    
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

function convertOscillatorChartData(data) {
    return ( this.heightPadding + (this.heightGap*2) ) -  (data * this.convertFactor);      
}

/**
 * X�� Y���� ������ �׸���.
 */

function drawOscillatorChartRange() {
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

function drawOscillatorChartInfo(posX) {
    var posY = this.heightPadding + this.heightGap;
        
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
    this.ctx.fillText("Osc : " + commify(oscillator[drawIndex]), posX+15, posY+40);        
}



