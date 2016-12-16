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
 * 차트를 그리는데 필요한 모든 정보를 그린다.
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
 * 그래프에 나타낼 범위를 구하기 위한 최대 최소 값을 구하고
 * 주가를 그래프 상의 위치로 변환시키기 위한 factor을 구한다.
 */
function setOscillatorChartFactor() {
    //주가들의 최대 최소
    minOscillator = getMin(oscillator);
    maxOscillator = getMax(oscillator);

    minOscillator = Math.min(getMin(oscillator), minOscillator);
    maxOscillator = Math.max(getMax(oscillator), maxOscillator);
    
    maxOscillator = Math.round(maxOscillator);
    minOscillator = Math.round(minOscillator);

    //최고값을 3000으로 나누어 떨어지는 수로 설정
    while ( maxOscillator % 2 != 0 ) {
        maxOscillator++;
    }
    if ( minOscillator < 0 ) {
        while ( minOscillator % 2 != 0 ) {
            minOscillator--;
        }   
    }
    //음수 또는 양수의 간격중 더 긴 간격을 선택한다.
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
 * Macd 각각의 선의 색을 표시해 준다.
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

//Quote 값을 Graph의 값으로 변경
function drawMacdOscillatorChart() {    
    var barGap = 1 - dayCount/250;
    var startX, barWidth, posBar, posBottom;
    posBottom = this.heightPadding + ( this.heightGap * 2 );
    
    startX = chartWidthPadding + ( chartWidthGap/2 + chartWidthGap/4 ) + barGap;
    barWidth = chartWidthGap - barGap*2;
    
    for ( var d = index; d < index+dayCount && d < size; d++ ) {
        //oscillator 막대 색 결정
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
 * 실제 값을 차트의 위치로 변환
 */

function convertOscillatorChartData(data) {
    return ( this.heightPadding + (this.heightGap*2) ) -  (data * this.convertFactor);      
}

/**
 * X축 Y축의 정보를 그린다.
 */

function drawOscillatorChartRange() {
    var posX, posY;

    //글씨 폰트 설정
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;

    //거래량의 Y축 정보가 그려징 화면의 x, y 좌표
    posX = chartWidthPadding - 30;
    posY = this.heightPadding+3;
    
    //거래량의 Y축 정보를 그린다.
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
    this.ctx.fillText("날짜 : " + calendar[drawIndex], posX+15, posY+20);
    this.ctx.fillText("Osc : " + commify(oscillator[drawIndex]), posX+15, posY+40);        
}



