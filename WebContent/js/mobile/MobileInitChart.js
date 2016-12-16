var calendar = [];

var chartCount; 
var candleChart;
var charts = [];

var dayCount;
var drawIndex;
var selectIndex;
var index;
var size;
var money;

var canvasWidth;
var chartWidthPadding;;
var chartWidth;
var chartWidthGap;
 
var controlChart;

var mouseWidthGap;

function init(s, m, w, cal, o, c, l, h, v, p, e5, e20, e60, e120, bu, bm, bl, sa, mac, si, os, rs, sk, sd) {
	dayCount = 60;
	selectIndex = -1;
	size = s;
	money = m;
	calendar = cal;
	   
    while ( dayCount > size )
       dayCount -= 5;
	
	index = size - dayCount;
	
	//주가 양이 모자를 경우 인덱스 수정
	if ( index < 0 )
		index = 0;
	
	chartWidthPadding = 30;
	canvasWidth = w - 30;
	//canvasWidth = window.innerWidth - chartWidthPadding;	
	chartWidth = canvasWidth - ( chartWidthPadding*2);
	chartWidthGap = chartWidth / (dayCount + 1);
	//mouseWidthGap = (document.documentElement.clientWidth - 1030)/2;
	mouseWidthGap = 0;	
		
	//각각의 차트 생성
	chartCount = 4;
	charts[chartCount] = new CandleChart(o, c, l, h, p, e5, e20, e60, e120, bu, bm, bl, sa);
	charts[0] = new VolumeChart(v);
	charts[1] = new MacdChart(mac, si, os);
	charts[2] = new RsiChart(rs);
	charts[3] = new SlowStcChart(sk, sd);
	
	setControlChart();
	
    charts[chartCount].draw();
	
	for(var i = 0; i < chartCount; i++) {     
        charts[i].chartEnable();
    }	
}

