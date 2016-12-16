

function drawLineChart(values, color) {
	var posX, posY;
	var k;
	posX = chartWidthPadding + chartWidthGap + chartWidthGap/4;
	
	//�� �α�, �� ����
	this.ctx.lineWidth = lineChartWidth;
	this.ctx.strokeStyle = color;
	
	k = index;
	
    if(k < index + dayCount && k < size) {
        posX += chartWidthGap * ( k - index );
        //���� ����
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
 * ������ ���ڸ� �׸���.
 */
function drawBackground() {    
    var widthStart = chartWidthPadding + 5;
    var widthEnd = canvasWidth;
    
    this.ctx.beginPath();
    this.ctx.strokeStyle = gridLineColor;
    this.ctx.lineWidth = gridLineWidth;  
    
    //���μ�
    for( i = this.heightPadding; i < this.heightPadding + this.heightGap * this.gridLineCount; i += this.heightGap) {
        drawDashedLine(this.ctx, widthStart, i, widthEnd, i, [3, 5]);
    }
       
    verticalPoint = chartWidthPadding + chartWidthGap/4;
    verticalEndPoint = chartWidthPadding + chartWidthGap * (dayCount-6);
    
    //���μ�
    if ( dayCount > 5 ) {
        do {
            //�ټ��� ���� ����
            if ( dayCount > 60 )
                verticalPoint += chartWidthGap * 10;
            else
                verticalPoint += chartWidthGap * 5;
            tempPoint = verticalPoint + chartWidthGap/2;
             /*
             * ���콺�� ����Ű�� x, heightPadding��������
             * �� ���� + ��Ʈ ����(heightGap * gindLineCount-1 ) �� ��ġ ���� ������ �ߴ´�.
             */         
            drawDashedLine(this.ctx, tempPoint, this.heightPadding, tempPoint, this.heightPadding + this.chartHeight, [5, 5]);          
        }while ( verticalPoint < verticalEndPoint  )
    }
      
    this.ctx.stroke();
}



/**
 * ���� �׸��� �Լ�
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
        var reg = /(^[+-]?\d+)(\d{3})/;   // ���Խ�
        n += '';                                       // ���ڸ� ���ڿ��� ��ȯ

        while (reg.test(n))
            n = n.replace(reg, '$1' + ',' + '$2');
        return n;
    }

// ���� ��Ÿ���� �ݿø� �Լ� ����
function roundXL(n, digits) {
  if (digits >= 0) return parseFloat(n.toFixed(digits)); // �Ҽ��� �ݿø�

  digits = Math.pow(10, digits); // ������ �ݿø�
  var t = Math.round(n * digits) / digits;

  return parseFloat(t.toFixed(0));
}
