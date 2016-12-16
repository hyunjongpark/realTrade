

function drawLineChart(values, color) {
	var posX, posY;
	var k;
	posX = chartWidthPadding + chartWidthGap + chartWidthGap/4;
	
	//선 두깨, 색 설정
	this.ctx.lineWidth = lineChartWidth;
	this.ctx.strokeStyle = color;
	
	k = index;
	
    if(k < index + dayCount && k < size) {
        posX += chartWidthGap * ( k - index );
        //라인 시작
        this.ctx.beginPath();
        posY = this.convertData(values[k]);
        this.ctx.moveTo(posX, posY);

        for(var d = k + 1; d < index + dayCount && d < size; d++) {
            posX += chartWidthGap;
            posY = this.convertData(values[d]);
            this.ctx.lineTo(posX, posY);
        }
        this.ctx.stroke();
    }	
}

/**
 * StockChart.drawBackground
 * 바탕의 격자를 그린다.
 */
function drawBackground() {    
    var widthStart = chartWidthPadding + 5;
    var widthEnd = canvasWidth;
    
    this.ctx.beginPath();
    this.ctx.strokeStyle = gridLineColor;
    this.ctx.lineWidth = gridLineWidth;  
    
    //가로선
    for( i = this.heightPadding; i < this.heightPadding + this.heightGap * this.gridLineCount; i += this.heightGap) {
        drawDashedLine(this.ctx, widthStart, i, widthEnd, i, [3, 5]);
    }
       
    verticalPoint = chartWidthPadding + chartWidthGap/4;
    verticalEndPoint = chartWidthPadding + chartWidthGap * (dayCount-6);
    
    //세로선
    if ( dayCount > 5 ) {
        do {
            //다섯개 마다 구분
            if ( dayCount > 60 )
                verticalPoint += chartWidthGap * 10;
            else
                verticalPoint += chartWidthGap * 5;
            tempPoint = verticalPoint + chartWidthGap/2;
             /*
             * 마우스가 가리키는 x, heightPadding에서부터
             * 위 여백 + 차트 높이(heightGap * gindLineCount-1 ) 의 위치 까지 직선을 긋는다.
             */         
            drawDashedLine(this.ctx, tempPoint, this.heightPadding, tempPoint, this.heightPadding + this.chartHeight, [5, 5]);          
        }while ( verticalPoint < verticalEndPoint  )
    }
      
    this.ctx.stroke();
}



/**
 * 점선 그리는 함수
 */
function drawDashedLine(ctx, x, y, x2, y2, da) {
    if(!da)
        da = [10, 5];
    ctx.save();
    var dx = (x2 - x), dy = (y2 - y);
    var len = Math.sqrt(dx * dx + dy * dy);
    var rot = Math.atan2(dy, dx);
    ctx.translate(x, y);
    ctx.moveTo(0, 0);
    ctx.rotate(rot);
    var dc = da.length;
    var di = 0, draw = true;
    x = 0;
    while(len > x) {
        x += da[di++ % dc];
        if(x > len)
            x = len;
        draw ? ctx.lineTo(x, 0) : ctx.moveTo(x, 0);
        draw = !draw;
    }
    ctx.restore();
}

function getMin(values) {
	var min = values[index];
	for ( var i = index+1; i < index+dayCount && i < size; i++ ) {
		if ( min > values[i] && values[i] != 0 )
			min = values[i];
	}
		   
	return min;
}

function getMax(values) {
	var max = values[index];
	for ( var i = index+1; i < index+dayCount && i < size; i++ ) {
		if ( max < values[i] && values[i] != 0 )
			max = values[i];
	}
	return max;
}

  function commify(n) {
        var reg = /(^[+-]?\d+)(\d{3})/;   // 정규식
        n += '';                                       // 숫자를 문자열로 변환

        while (reg.test(n))
            n = n.replace(reg, '$1' + ',' + '$2');
        return n;
    }

// 엑셀 스타일의 반올림 함수 정의
function roundXL(n, digits) {
  if (digits >= 0) return parseFloat(n.toFixed(digits)); // 소수부 반올림

  digits = Math.pow(10, digits); // 정수부 반올림
  var t = Math.round(n * digits) / digits;

  return parseFloat(t.toFixed(0));
}
