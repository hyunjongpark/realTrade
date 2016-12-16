var minLow;
var maxHigh;

var close = [];
var profit = [];
var status = [];
var yellowBoxWidth = 200;

function PaperTradeChart(c, pro, sta) {
    /* Variable */
    //Canvas�� ���� canvas�� context ���� ����
    this.canvas = document.getElementById("candleChartCanvas");
    this.ctx = this.canvas.getContext('2d');
    //���콺 �̵��� ���� �̺�Ʈ ó���� ����
    this.canvas.onmousemove = chartMouseMove;
    //ĵ���� ����
    this.canvasHeight = 410;
    //��Ʈ�� �� ����
    this.heightPadding = 50;
    //���ֿ��� ���� ����
    this.heightGap = 30;
    //���� ��
    this.gridLineCount = 12;
    //ĵ�������� ��Ʈ�� ���� ��, ���� ���� * ���ڼ� - 1 
    this.chartHeight = this.heightGap * (this.gridLineCount - 1);
    //ĵ���� ���̿� ���� ����
    this.ctx.canvas.width = canvasWidth;
    this.ctx.canvas.height = this.canvasHeight;
    this.convertFactor = 0;
    this.chartDiff = 0;
    
    patternFix = -1;
    
    /* Function */
    //������ -> �ȼ��� ��ȯ�ϱ����� Factor  �Լ�
    this.setFactor = setPaperTradeChartFactor;
    // ������ -> �ȼ� �Լ�
    this.convertData = convertPaperTradeChartData;
    
    //������Ʈ �׸��� �Լ�
    this.drawLineChart = drawLineChart;
    
    //��Ʈ ���� �׸��� �Լ�
    this.drawRange = drawPaperTradeChartRange;
    //��Ʈ ��� ���� �׸��� �Լ�
    this.drawBackground = drawBackground;
    //��Ʈ �̸� �׸��� �Լ�
    this.drawChartName = drawPaperTradeChartName;
    //��Ʈ�� �׸��µ� �ʿ��� ��� ��Ҹ� �׸��� �Լ�
    this.draw = drawPaperTradeChartAll;
    //���콺 ��ġ�� �ִ� ��¥ �����ִ� �Լ�
    this.drawCalendar = drawPaperTradeChartCalendar;
    //���콺 ��ġ�� �ִ� ���� ���� �����ִ� �Լ�
    this.drawChartData = drawPaperTradeChartData;
    
    //��Ʈ �׸��µ� �ʿ��� ������ ����
    
    close = c;
    profit = pro;
    status = sta;
}


/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawPaperTradeChartAll() {
    //��Ʈ ������ �����.
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);
    //ȭ����� �������� ������ �ٲ���� �� �����Ƿ� 
    //Factor�� �ٽ� ����Ѵ�.
    this.setFactor();
    //��� ���ڸ� �׸���.
    this.drawBackground();
    
    this.drawLineChart(close, textColor);
    this.drawLineChart(profit, "blue");
        
    //���ָ� �׸���.
    this.drawRange();
    //��Ʈ �̸����� �׸���.
    this.drawChartName();   
}


/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setPaperTradeChartFactor() {
    //�ְ����� �ִ� �ּ�
    minLow = Math.min(getMin(profit), getMin(close));
    maxHigh = Math.max(getMax(profit), getMax(close));
    
    //�ִ� �ּ��� ������ 10% ( ���� ������ ���� )
    this.chartDiff = roundXL( (maxHigh - minLow) * 0.11, money);   
    //���� �ְ� ���̸� �׷����� ���̷� ��ȯ�ϱ� ���� Factor
    this.convertFactor = this.heightGap / this.chartDiff ;
    
}

/**
 * ���� ��ǥ���� �̸����� ���� ���� ���� �׸���.
 */
function drawPaperTradeChartName() {
   /*
    * EMA, BollingerBend�� üũ �Ǿ����� �ش��ϴ� ���� ���� �����ش�.
    */
    var posX = chartWidthPadding + chartWidthGap/2+10;
    var posY = this.heightPadding + this.heightGap/2;
    this.ctx.font = chartFont;    
    this.ctx.fillStyle = textColor;
    
    //�ְ� ���� ǥ��
    this.ctx.fillText("����", posX, posY);
    this.ctx.fillStyle = "blue";
    posX = posX + 35;
    this.ctx.fillText("��������", posX, posY);
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */
function convertPaperTradeChartData(data) {
    var diff = maxHigh - data;          
    return this.heightPadding + this.heightGap + ( diff * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */
function drawPaperTradeChartRange() {
    //�ְ� �������� Y�� ������ �׷��� ȭ���� x, y��ǥ
    var fontPositionX = chartWidthPadding - 30;
    var fontPositionY = this.heightPadding;
    var xAxisGap, xAxixGapCount;
    var startDay;
    //�۾� ��Ʈ ����
    this.ctx.fillStyle = textColor;
    this.ctx.font = rangeFont;
    
    fontPositionY += this.heightGap;
    
    //�ְ� �������� Y�� ������ �׸���.
    var yAxisGap = maxHigh;
    for( var i = 0; i < this.gridLineCount - 2; i++) {
        yAxisGap = roundXL(yAxisGap, money);
        this.ctx.fillText(commify(yAxisGap), fontPositionX, fontPositionY +3);
        fontPositionY += this.heightGap;
        yAxisGap -= this.chartDiff;
    }
    
    //��¥�� X�� ������ �׷�¡ ȭ���� x, y ��ǥ  
    fontPositionY = this.heightPadding + this.chartHeight + 15 + 3;
    
    //60�� �̻��̸� 10������ ��¥�� �׸���.
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
        
    //��¥ ���    
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
    //�۾� ��Ʈ ����
    this.ctx.fillStyle = dateBoxTextColor;
    this.ctx.font = calendarFont;
    this.ctx.fillText(calendar[drawIndex], posX - 30, posY + 18);
}

function drawPaperTradeChartData() {
    //�۾� ��Ʈ ����
    var posX = (chartWidthPadding + (chartWidthGap / 2) ) - 30;
    var initPosX = posX;
    var posY = this.heightPadding - 30;
    
    this.ctx.fillStyle = textColor;
    this.ctx.font = dataFont;
    
    //�ְ� ���� ǥ��
    posX = initPosX;
    this.ctx.fillText("��¥ : " + calendar[drawIndex], posX, posY);
    posY = posY + 15;
    this.ctx.fillText("���� : " + commify(close[drawIndex]), posX, posY);
    this.ctx.fillText("���ڱ� : " + commify(roundXL(profit[drawIndex], money)), posX + 110, posY);
    
    if ( status[drawIndex] == "1" ) {
        this.ctx.fillText("�ֽ� �ż�", posX+240, posY);
    } else if ( status[drawIndex] == "2" ) {
        this.ctx.fillText("�ֽ� �ŵ�", posX+240, posY);
    } else if ( status[drawIndex] == "3" ) {
        this.ctx.fillText("�ֽ� ����", posX+240, posY);
    } else if ( status[drawIndex] == "4" ) {
        this.ctx.fillText("�ż� ���", posX+240, posY);
    }    
}
