var minLow;
var maxHigh;

var open = [];
var close = [];
var low = [];
var high = [];
var pattern = [];

var ma = [];
var maString = ["5", "20", "60", "120"];
var bollingerString = ["U", "M", "L"];
var bollinger = [];
var sar = [];

var patternFix;
var patternPosX;
var patternIndex;
var yellowBoxWidth = 200;

function CandleChart(o, c, l, h, p, e5, e20, e60, e120, bu, bm, bl, sa) {
	/* Variable */
	//Canvas�� ���� canvas�� context ���� ����
	this.canvas = document.getElementById("candleChartCanvas");
	this.ctx = this.canvas.getContext('2d');
	//���콺 �̵��� ���� �̺�Ʈ ó���� ����
	this.canvas.onmousemove = candleChartMouseMove;
	//���콺 Ŭ���� ���� �̺�Ʈ ó���� ����
	if ( type == 1)
	   this.canvas.onmousedown = candleChartMouseDown;
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
	this.setFactor = setCandleChartFactor;
	// ������ -> �ȼ� �Լ�
	this.convertData = convertCandleChartData;
	//ĵ����Ʈ �׸��� �Լ�
	this.drawChart = drawCandleChart;
	//������Ʈ �׸��� �Լ�
	this.drawLineChart = drawLineChart;
	//��������� �׸��� �Լ�
	this.drawBollingerChart = drawBollingerChart;
	//Parabolic Sar �׸��� �Լ�
	this.drawSar = drawCandleSarChart;
	//��Ʈ ���� �׸��� �Լ�
	this.drawRange = drawCandleChartRange;
	//��Ʈ ��� ���� �׸��� �Լ�
	this.drawBackground = drawBackground;
	//��Ʈ �̸� �׸��� �Լ�
	this.drawChartName = drawCandleChartName;
	//��Ʈ�� �׸��µ� �ʿ��� ��� ��Ҹ� �׸��� �Լ�
	this.draw = drawCandleChartAll;
	//���콺 ��ġ�� �ִ� ���� ���� �����ִ� �Լ�
	this.drawInfo = drawCandleChartInfo;
	//���콺 ��ġ�� �ִ� ��¥ �����ִ� �Լ�
	this.drawCalendar = drawCandleChartCalendar;
	//���콺 ��ġ�� �ִ� ���� ���� �����ִ� �Լ�
	this.drawChartData = drawCandleChartData;
	
    //��Ʈ �׸��µ� �ʿ��� ������ ����
	open = o;
	close = c;
	low = l;
	high = h;
	pattern = p;
	ma[0] = e5;
	ma[1] = e20;
	ma[2] = e60;
	ma[3] = e120;
	bollinger[0] = bu;
	bollinger[1] = bm;
	bollinger[2] = bl;
	sar = sa;
}


/**
 * ��Ʈ�� �׸��µ� �ʿ��� ��� ������ �׸���.
 */
function drawCandleChartAll() {
    //��Ʈ ������ �����.
    this.ctx.clearRect(0,0,canvasWidth, this.canvasHeight);
    //ȭ����� �������� ������ �ٲ���� �� �����Ƿ� 
    //Factor�� �ٽ� ����Ѵ�.
    this.setFactor();
    //��� ���ڸ� �׸���.
    this.drawBackground(); 
    //ĵ�� ��Ʈ�� �׸���.   
    this.drawChart(); 
    
    //MA�� üũ �Ǿ��� �� MA ���� 4 ���� �׸���.
    if ( checkCandleLine[0].checked == true ) {
    	for ( var i = 0; i < 4; i++ )
    		this.drawLineChart(ma[i], maColor[i]);
    }
    //Bollinger Bend�� üũ �Ǿ��� �� 
    if ( checkCandleLine[1].checked == true ) {
    	// for ( var i = 0; i < 3; i++ )
    		// this.drawLineChart(bollinger[i], bollingerColor[i]);
    	this.drawBollingerChart();
    }
    //Parabolic Sar�� üũ �Ǿ��� ��
    if ( checkCandleLine[2].checked == true )  {
    	this.drawSar();
    }
    
    //���ָ� �׸���.
    this.drawRange();
    //��Ʈ �̸����� �׸���.
    this.drawChartName();
    
    //���콺�� Ŭ�� �Ǹ� ��� ����â�� ���� ��Ű�� �׸���.
    if ( patternFix == 1 && checkOption[1].checked == true )
        this.drawInfo(patternPosX);
}

function drawCandleChart() {
    var candleGap = 1;
    var startX = chartWidthPadding + (chartWidthGap/2 + chartWidthGap/4) + candleGap;
    //var shadowLine = chartWidthPadding + chartWidthGap;
    var shadowLine = chartWidthPadding + chartWidthGap + chartWidthGap/4;
    var candleWidth = chartWidthGap - candleGap*2;
    
    for( var d = index; d < index+dayCount && d < size; d++) {
        //������ Data ȭ����� ��ġ�� ��ȯ      
        var posOpen = this.convertData(open[d]);
        var posClose = this.convertData(close[d]);
        var posLow = this.convertData(low[d]);
        var posHigh = this.convertData(high[d]);
    
        //�ְ� - ������
        this.ctx.beginPath();
        this.ctx.strokeStyle = shadowColor;
        this.ctx.lineWidth = shadowWidth;
        this.ctx.moveTo(shadowLine, posHigh);
        this.ctx.lineTo(shadowLine, posLow);
        this.ctx.stroke();
        
        //����
        //if(Math.abs(posClose - posOpen) < dojiGap) {
        if(pattern[d].indexOf("Doji") != -1 || posOpen == posClose) {
            this.ctx.fillStyle = dojiColor;
            this.ctx.fillRect(startX, posOpen, candleWidth, 2);
        }
        //����
        else if(open[d] > close[d]) {
            this.ctx.fillStyle = negativeColor;
            this.ctx.fillRect(startX, posOpen, candleWidth, posClose - posOpen);
        }
        //���
        else {
            this.ctx.fillStyle = positiveColor;
            this.ctx.fillRect(startX, posClose, candleWidth, posOpen - posClose);
        }
        startX += chartWidthGap;
        shadowLine += chartWidthGap;
    }
}

function drawBollingerChart()
{
	//���μ�->line->���μ�->line fill()
	var posX, posY;
	var k,d,e;
	posX = chartWidthPadding + chartWidthGap + chartWidthGap/4;
	
	//�� �α�, �� ����
	this.ctx.lineWidth = lineChartWidth;
	this.ctx.strokeStyle = "red";
	this.ctx.fillStyle = "rgba(255,255,102, 0.2)";
	
	//������ã��
	k = index; 
	while ( bollinger[1][k] == 0 && k < index + dayCount && k < size ) {
	   k++;
	}
	//�׸��� ����
    if(k < index + dayCount && k < size) {
        posX += chartWidthGap * ( k - index );
        //�����ٱ߱�1
        this.ctx.beginPath();
        posY = this.convertData(bollinger[2][k]); 
        this.ctx.moveTo(posX,posY);
        posY = this.convertData(bollinger[0][k]); 
        this.ctx.lineTo(posX,posY);
        
        //�����α׸���
        for(d = k; d < index + dayCount && d < size; d++) {            
            posY = this.convertData(bollinger[0][d]);
            this.ctx.lineTo(posX, posY);
            posX += chartWidthGap;
        }
        
        d = d-1;
        posX -=chartWidthGap;    
            
        posY=this.convertData(bollinger[2][d]);
        this.ctx.lineTo(posX,posY);
        
        //�Ʒ����α׸��� �ε����ڿ��� ������
        for(e = d; e >= k ; e--)
        {	            
        	posY=this.convertData(bollinger[2][e]);
        	this.ctx.lineTo(posX,posY);        	
            posX -=chartWidthGap;        	
        }
        this.ctx.stroke();
        this.ctx.fill();
        this.ctx.closePath();
    }
    this.drawLineChart(bollinger[1], bollingerColor[1]);	
}

function drawCandleSarChart() {
    var startX = chartWidthPadding + (chartWidthGap/2 + chartWidthGap/4);   
    var sarWidth = chartWidthGap;
    
    for( var d = index; d < index+dayCount && d < size; d++) {
        //������ Data ȭ����� ��ġ�� ��ȯ      
        var posSar = this.convertData(sar[d]);
        
        this.ctx.fillStyle = sarColor;
        this.ctx.fillRect(startX, posSar, sarWidth, 3);     
        
        startX += chartWidthGap;
    }
}


/**
 * �׷����� ��Ÿ�� ������ ���ϱ� ���� �ִ� �ּ� ���� ���ϰ�
 * �ְ��� �׷��� ���� ��ġ�� ��ȯ��Ű�� ���� factor�� ���Ѵ�.
 */
function setCandleChartFactor() {
    //�ְ����� �ִ� �ּ�
    minLow = getMin(low);
    maxHigh = getMax(high);
    
    //MA�� üũ �Ǿ����� MA�� ���� �ݿ��Ͽ� �ִ� �ּҸ� ���Ѵ�.
    if ( checkCandleLine[0].checked == true ) {
    	for ( var i = 0; i < 4; i++ ) {
    		minLow = Math.min(getMin(ma[i]), minLow);
    		maxHigh = Math.max(getMax(ma[i]), maxHigh);
    	}    		
    }
    
    //BollingerBend�� üũ �Ǿ����� BollingerBend�� ���� �ݿ��Ͽ� �ִ� �ּҸ� ���Ѵ�.
    if ( checkCandleLine[1].checked == true ) {
    	for ( var i = 0; i < 3; i++ ) {
    		minLow = Math.min(getMin(bollinger[i]), minLow);
    		maxHigh = Math.max(getMax(bollinger[i]), maxHigh);
    	}    		
    }
    
    //Pabolic Sar�� üũ �Ǿ����� Pabolic Sar�� ���� �ݿ��Ͽ� �ִ� �ּҸ� ���Ѵ�.
    if ( checkCandleLine[2].checked == true ) {
 	   minLow = Math.min(getMin(sar), minLow);
 	   maxHigh = Math.max(getMax(sar), maxHigh);
    }
    
          
    //�ִ� �ּ��� ������ 10% ( ���� ������ ���� )
    this.chartDiff = roundXL( (maxHigh - minLow) * 0.11, money);   
    //���� �ְ� ���̸� �׷����� ���̷� ��ȯ�ϱ� ���� Factor
    this.convertFactor = this.heightGap / this.chartDiff ;
    
}

/**
 * ���� ��ǥ���� �̸����� ���� ���� ���� �׸���.
 */
function drawCandleChartName() {
   /*
    * EMA, BollingerBend�� üũ �Ǿ����� �ش��ϴ� ���� ���� �����ش�.
    */
    var posX = chartWidthPadding + chartWidthGap/2;
    var posY = this.heightPadding + this.heightGap/2;
    this.ctx.font = chartFont;
    if ( checkCandleLine[0].checked == true ) {
    	for ( var k = 0; k < 4; k++ ) {    	    	
    		this.ctx.fillStyle = maColor[k];
    		this.ctx.fillText("MA" + maString[k], posX, posY);
    		posX += 60;
    	}
    	posX += 20;
    }
    if ( checkCandleLine[1].checked == true ) {
    	for ( var k = 0; k < 3; k++ ) {
    		this.ctx.fillStyle = bollingerColor[k];
    		this.ctx.fillText(bollingerString[k], posX, posY);
    		posX += 20;	
    	}
   }     
    
    if ( checkCandleLine[2].checked == true ) {
    	this.ctx.fillStyle = sarColor;
    	this.ctx.fillText("SAR", posX, posY);
    	posX += 20;
   }     
}

/**
 * ���� ���� ��Ʈ�� ��ġ�� ��ȯ
 */
function convertCandleChartData(data) {
    var diff = maxHigh - data;          
    return this.heightPadding + this.heightGap + ( diff * this.convertFactor );    
}

/**
 * X�� Y���� ������ �׸���.
 */
function drawCandleChartRange() {
    //�ְ� �������� Y�� ������ �׷��� ȭ���� x, y��ǥ
    var fontPositionX = chartWidthPadding - 30;
    var fontPositionY = this.heightPadding;
    var xAxisGap, xAxixGapCount;
    var tick = 1;
    
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
    
    var gap = 1 +  roundXL(dayCount / 50, 0);
    xAxisGap = chartWidthGap * gap * 5;
    xAxixGapCount = gap * 5;
    fontPositionX = chartWidthPadding + (chartWidthGap * (gap* 5 - 2));
    startDay = index + (gap*5-3);
    
    //��¥ ���    
    for ( var d = startDay; d < index+dayCount && d < size; d+= xAxixGapCount) {       
        var c;
        if ( type == 1 ) {
    	   c = calendar[d];
    	   this.ctx.fillText(c, fontPositionX - 22, fontPositionY);
    	}
    	else {
    	   if ( dayCount > 50 )
    	       tick++;
    	   if ( tick % 2 == 1 ) {  
    	       c = calendar[d].substring(0, 7);
    	       this.ctx.fillText(c, fontPositionX - 16, fontPositionY);
    	   }
    	}
        
        fontPositionX += xAxisGap;
    }
}

function drawCandleChartInfo(posX) {
	var posY = this.heightPadding + (this.heightGap * 2) + 2;
	var pos = drawIndex;
	
	//������ ���°� �ƴϸ� ����â�� ������ �����Ѵ�. 
	if ( patternFix == -1 ) {
        patternPosX = posX;
        patternIndex = drawIndex;
    } else {
        pos = patternIndex;
        this.ctx.beginPath();
        this.ctx.strokeStyle = fixColor;
        this.ctx.lineWidth = highlightLineWidth;
        this.ctx.moveTo(posX, this.heightPadding);
        this.ctx.lineTo(posX, this.heightPadding + this.heightGap * (this.gridLineCount - 1));
        this.ctx.stroke();
    }
    
	// �߽� ���� �¿쿡 ���� ��ġ ����
	if (posX < canvasWidth / 2) {
		posX += 10;
	} else {
		posX -= (yellowBoxWidth + 10);
	}

	this.ctx.fillStyle = extraInfoBoxColor;
	this.ctx.fillRect(posX, posY, yellowBoxWidth, this.heightGap * 7);
    
	this.ctx.font = patternFont;
	posX += 15;
	
	/*
	 * ���� ������ ����Ѵ�.
	 * ������ M@Doji##P@Marubozu �̷� ������ M, P, F �� Minus, Plus, Find�� 
	 * Ÿ���� ��Ÿ���� @ ������ ���ڴ� ������ ��Ÿ���� �� ������ ##�� ���� �ȴ�.
	 * 
	 * split("##") �Լ��� �̿��� ������ ������ �и��ϰ� �и��� ������ �ٽ� @�� �и��Ͽ�
	 * Ÿ�Կ� ���� ���� �ٸ����Ͽ� ������ ����Ѵ�.
	 */	
	if (pattern[pos] != "") {
		var parsePattern = pattern[pos].split("##");
		var k = 40;
		for ( var i = 0; i < parsePattern.length-1; i++) {
			var pt = parsePattern[i].split("@");
			if (pt[0] == "M") {
				this.ctx.fillStyle = negativeColor;				
			} else if (pt[0] == "P") {
				this.ctx.fillStyle = positiveColor;				
			} else if ( pt[0] == "F") {
				this.ctx.fillStyle = textColor;
			}
			this.ctx.fillText(pt[1], posX, posY + k);
			k += 20;
		}
	}
}

function drawCandleChartCalendar(posX) {	
	var posY = this.heightPadding + this.chartHeight;

	this.ctx.fillStyle = dateBoxColor;
	if ( type == 1)
		this.ctx.fillRect(posX - 40, posY, 80, 30);
	else {
 	    if ( dayCount >= 50 ) {
 	        if ( drawIndex - index < 5 ) {
 	           this.ctx.fillRect(posX - 25, posY, 100, 30);
 	           posX += 20;
 	        }
 	        else if ( drawIndex - index > dayCount-5 ) {
 	           this.ctx.fillRect(posX - 65, posY, 100, 30);
 	           posX -= 20;
 	        }
 	        else
 	           this.ctx.fillRect(posX - 45, posY, 100, 30);
 	    }
 	    else 
	       this.ctx.fillRect(posX - 45, posY, 100, 30);
	}
		
	//�۾� ��Ʈ ����
	this.ctx.fillStyle = dateBoxTextColor;
	this.ctx.font = calendarFont;
	this.ctx.fillText(calendar[drawIndex], posX - 30, posY + 18);
}

function drawCandleChartData() {
	//�۾� ��Ʈ ����
	var posX = (chartWidthPadding + (chartWidthGap / 2) ) - 30;
	var initPosX = posX;
	var posY = this.heightPadding;
	
	var gap = 100;
	
	this.ctx.fillStyle = textColor;
	this.ctx.font = dataFont;
	
	//Parabolic sar �� ���� ǥ���Ѵ�.
	if ( checkCandleLine[2].checked == true ) {
	    if ( type == 1 )
	       gap = 110;
	    else if ( type == 2)
	       gap = 100;
	    else if ( type == 3 )
	       gap = 120;
	       
		posY -= 15;
		this.ctx.fillStyle = textColor;
		this.ctx.fillText("Parabolic Sar > ", posX, posY);
		posX += gap;
		this.ctx.fillStyle = sarColor;
		this.ctx.fillText(commify(sar[drawIndex]), posX, posY);
	}
	
	//BollingerBend�� ���� ǥ���Ѵ�.
	if ( checkCandleLine[1].checked == true ) {
		posY -= 15;
		posX = initPosX;
		this.ctx.fillStyle = textColor;
		this.ctx.fillText("BollingerBand > ", posX, posY);
		if ( type == 1 )
		  posX += 110;
		else if ( type == 2 )
	      posX += 100;
	    else if ( type == 3)
	       posX += 120;
		
		if ( type == 1 )
           gap = 140;
        else if ( type == 2) 
            gap = 100;
        else if ( type == 3)
           gap = 105;
		
		for ( var i = 0; i < 3; i++ ) {
		    this.ctx.fillStyle = bollingerColor[i];
		    this.ctx.fillText(bollingerString[i], posX, posY);		 
		    this.ctx.fillStyle = textColor;  
		    this.ctx.fillText(" : " + commify(roundXL(bollinger[i][drawIndex], 0) ), posX + 10, posY);
			posX += gap;	
		}
	}	

	//EMA�� ���� ǥ���Ѵ�.
	if ( checkCandleLine[0].checked == true ) { 
		posY -= 15;
		posX = initPosX;
		this.ctx.fillText("MA > ", posX, posY);
		posX += 40;
		
		if ( type == 1 )
           gap = 120;
        else  if ( type == 2)
           gap = 90;
        else if ( type == 3 )
            gap = 110;
           
		for ( var i = 0; i < 4; i++ ) {
		    this.ctx.fillStyle = maColor[i];
            this.ctx.fillText(maString[i], posX, posY);       
            this.ctx.fillStyle = textColor;  
            if ( i == 3 )
                this.ctx.fillText("��: " + commify(roundXL(ma[i][drawIndex], 0)), posX + 12, posY);
            else
                this.ctx.fillText("��: " + commify(roundXL(ma[i][drawIndex], 0)), posX + 5, posY);
			posX += gap;
		}		
	}
	
	if ( posY == this.heightPadding ) {
		posY = this.heightPadding - 25;
	} else {
		posY -= 30;
	}
	
	//�ְ� ���� ǥ��
	posX = initPosX;
	this.ctx.fillText("��¥ : " + calendar[drawIndex], posX, posY);
	this.ctx.fillText("�ŷ��� : " + commify(volume[drawIndex]), posX + 130, posY);
	posY = posY + 15;	
	this.ctx.fillText("�ð� : " + commify(open[drawIndex]), posX, posY);
	if ( type == 1 ) {
	    this.ctx.fillText("���� : " + commify(close[drawIndex]), posX + 120, posY);
        this.ctx.fillText("���� : " + commify(low[drawIndex]), posX + 240, posY);
        this.ctx.fillText("��: " + commify(high[drawIndex]), posX + 360, posY);
	} else {
	   this.ctx.fillText("���� : " + commify(close[drawIndex]), posX + 105, posY);
	   this.ctx.fillText("���� : " + commify(low[drawIndex]), posX + 210, posY);
	   this.ctx.fillText("��: " + commify(high[drawIndex]), posX + 315, posY);
	}
		
}

