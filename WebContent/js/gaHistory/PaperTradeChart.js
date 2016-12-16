var minLow;
var maxHigh;

var close = [];
var profit = [];
var status = [];
var yellowBoxWidth = 200;

function PaperTradeChart(c, pro, sta) {
    /* Variable */
    //Canvas에 대한 canvas와 context 변수 설정
    this.canvas = document.getElementById("candleChartCanvas");
    this.ctx = this.canvas.getContext('2d');
    //마우스 이동에 대한 이벤트 처리기 설정
    this.canvas.onmousemove = chartMouseMove;
    //캔버스 높이
    this.canvasHeight = 410;
    //차트의 위 여백
    this.heightPadding = 50;
    //범주에서 상하 간격
    this.heightGap = 30;
    //격자 수
    this.gridLineCount = 12;
    //캔버스에서 차트의 높이 즉, 상하 간격 * 격자수 - 1 
    this.chartHeight = this.heightGap * (this.gridLineCount - 1);
    //캔버스 넓이와 높이 설정
    this.ctx.canvas.width = canvasWidth;
    this.ctx.canvas.height = this.canvasHeight;
    this.convertFactor = 0;
    this.chartDiff = 0;
    
    patternFix = -1;
    
    /* Function */
    //데이터 -> 픽셀로 변환하기위한 Factor  함수
    this.setFactor = setPaperTradeChartFactor;
    // 데이터 -> 픽셀 함수
    this.convertData = convertPaperTradeChartData;
    
    //라인차트 그리는 함수
    this.drawLineChart = drawLineChart;
    
    //차트 범주 그리는 함수
    this.drawRange = drawPaperTradeChartRange;
    //차트 배경 격자 그리는 함수
    this.drawBackground = drawBackground;
    //차트 이름 그리는 함수
    this.drawChartName = drawPaperTradeChartName;
    //차트를 그리는데 필요한 모든 요소를 그리는 함수
    this.draw = drawPaperTradeChartAll;
    //마우스 위치에 있는 날짜 보여주는 함수
    this.drawCalendar = drawPaperTradeChartCalendar;
    //마우스 위치에 있는 봉의 정보 보여주는 함수
    this.drawChartData = drawPaperTradeChartData;
    
    //차트 그리는데 필요한 데이터 저장
    
    close = c;
    profit = pro;
    status = sta;
}


/**
 * 차트를 그리는데 필요한 모든 정보를 그린다.
 */
function drawPaperTradeChartAll() {
    //차트 영역을 지운다.
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);
    //화면상의 보여지는 봉들이 바뀌었을 수 있으므로 
    //Factor을 다시 계산한다.
    this.setFactor();
    //배경 격자를 그린다.
    this.drawBackground();
    
    this.drawLineChart(close, textColor);
    this.drawLineChart(profit, "blue");
        
    //범주를 그린다.
    this.drawRange();
    //차트 이름들을 그린다.
    this.drawChartName();   
}


/**
 * 그래프에 나타낼 범위를 구하기 위한 최대 최소 값을 구하고
 * 주가를 그래프 상의 위치로 변환시키기 위한 factor을 구한다.
 */
function setPaperTradeChartFactor() {
    //주가들의 최대 최소
    minLow = Math.min(getMin(profit), getMin(close));
    maxHigh = Math.max(getMax(profit), getMax(close));
    
    //최대 최소의 차이의 10% ( 라인 사이의 간격 )
    this.chartDiff = roundXL( (maxHigh - minLow) * 0.11, money);   
    //실제 주가 차이를 그래프의 차이로 변환하기 위한 Factor
    this.convertFactor = this.heightGap / this.chartDiff ;
    
}

/**
 * 보조 지표들의 이름들을 선의 색에 따라 그린다.
 */
function drawPaperTradeChartName() {
   /*
    * EMA, BollingerBend가 체크 되었으면 해당하는 선의 색을 보여준다.
    */
    var posX = chartWidthPadding + chartWidthGap/2+10;
    var posY = this.heightPadding + this.heightGap/2;
    this.ctx.font = chartFont;    
    this.ctx.fillStyle = textColor;
    
    //주가 정보 표시
    this.ctx.fillText("종가", posX, posY);
    this.ctx.fillStyle = "blue";
    posX = posX + 35;
    this.ctx.fillText("모의투자", posX, posY);
}

/**
 * 실제 값을 차트의 위치로 변환
 */
function convertPaperTradeChartData(data) {
    var diff = maxHigh - data;          
    return this.heightPadding + this.heightGap + ( diff * this.convertFactor );    
}

/**
 * X축 Y축의 정보를 그린다.
 */
function drawPaperTradeChartRange() {
    //최고가 최저가의 Y축 정보가 그려질 화면의 x, y좌표
    var fontPositionX = chartWidthPadding - 30;
    var fontPositionY = this.heightPadding;
    var xAxisGap, xAxixGapCount;
    var startDay;
    //글씨 폰트 설정
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;
    
    fontPositionY += this.heightGap;
    
    //최고가 최저가의 Y축 정보를 그린다.
    var yAxisGap = maxHigh;
    for( var i = 0; i < this.gridLineCount - 2; i++) {
        yAxisGap = roundXL(yAxisGap, money);
        this.ctx.fillText(commify(yAxisGap), fontPositionX, fontPositionY +3);
        fontPositionY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }
    
    //날짜의 X축 정보가 그려징 화면의 x, y 좌표  
    fontPositionY = this.heightPadding + this.chartHeight + 15 + 3;
    
    //60일 이상이면 10개마다 날짜를 그린다.
    var gap = 1 +  roundXL(dayCount / 50, 0);
    xAxisGap = chartWidthGap * gap * 5;
    xAxixGapCount = gap * 5;
    fontPositionX = chartWidthPadding + (chartWidthGap * (gap* 5 - 2));
    startDay = index + (gap*5-3);
    
    // if ( dayCount < 60 ) {
        // xAxisGap = chartWidthGap * 5;
        // xAxixGapCount = 5;
        // fontPositionX = chartWidthPadding + (chartWidthGap * 3);  
        // startDay = index+2;
    // }
    // else {
        // xAxisGap = chartWidthGap * 10;
        // xAxixGapCount = 10;
        // fontPositionX = chartWidthPadding + (chartWidthGap * 8);
        // startDay = index + 7;
    // }
        
    //날짜 출력    
    for ( var d = startDay; d < index+dayCount && d < size; d+= xAxixGapCount) {       
        var c = calendar[d].substring(5, 10);
        this.ctx.fillText(c, fontPositionX - 16, fontPositionY);
        fontPositionX += xAxisGap;
    }
}


function drawPaperTradeChartCalendar(posX) {    
    var posY = this.heightPadding + this.chartHeight;

    this.ctx.fillStyle = dateBoxColor;
    if ( type == 1)
        this.ctx.fillRect(posX - 40, posY, 80, 30);
    else
        this.ctx.fillRect(posX - 45, posY, 100, 30);
    //글씨 폰트 설정
    this.ctx.fillStyle = dateBoxTextColor;
    this.ctx.font = calendarFont;
    this.ctx.fillText(calendar[drawIndex], posX - 30, posY + 18);
}

function drawPaperTradeChartData() {
    //글씨 폰트 설정
    var posX = (chartWidthPadding + (chartWidthGap / 2) ) - 30;
    var initPosX = posX;
    var posY = this.heightPadding - 30;
    
    this.ctx.fillStyle = textColor;
    this.ctx.font = dataFont;
    
    //주가 정보 표시
    posX = initPosX;
    this.ctx.fillText("날짜 : " + calendar[drawIndex], posX, posY);
    posY = posY + 15;
    this.ctx.fillText("종가 : " + commify(close[drawIndex]), posX, posY);
    this.ctx.fillText("투자금 : " + commify(roundXL(profit[drawIndex], money)), posX + 110, posY);
    
    if ( status[drawIndex] == "1" ) {
        this.ctx.fillText("주식 매수", posX+240, posY);
    } else if ( status[drawIndex] == "2" ) {
        this.ctx.fillText("주식 매도", posX+240, posY);
    } else if ( status[drawIndex] == "3" ) {
        this.ctx.fillText("주식 보유", posX+240, posY);
    } else if ( status[drawIndex] == "4" ) {
        this.ctx.fillText("매수 대기", posX+240, posY);
    }    
}
