var calendar = [];

var chartCount;
var charts = [];

//화면에 보여지는 날짜 수
var dayCount;

//마우스에 따라 화면에 정보가 그려질 인덱스
var drawIndex;
var startIndex;
/*
 * 마우스의 위치가 변하지 않으면 새로 그리지 않기 위해
 * 이전에 그린 정보의 인덱스 저장하는 변수 
 */
var selectIndex;

/*
 * 모든 차트를 그리는데 시작하는 인덱스
 * 따라서 모든 차트는 index부터 dayCount만큼 그리며
 * index의 변화에 따라 날짜 이동을 구현한다.
 */
var index;

//데이터 전체 크기
var size;

//돈 소수 표시 단위
var money;

/*
 * 차트의 넓이, 폭과 관련된 변수
 * 캔들 차트 외에 모든 차트들의 넓이나
 * 사이의 간격은 일정해야 하므로 아래의 변수를
 * 모든 차트가 사용하며 높이에 관한 값은 각각 가진다.
 */
var canvasWidth;
var chartWidthPadding;
var chartWidth;
var chartWidthGap;
 
var controlChart;

//마우스의 위치를 조정한다.
var mouseWidthGap;
             
function init(s, m, minIndex, cal, c, pro, sta, sk, sd, mac, si, os) {
    dayCount = 80;
    //마우스가 위치한 곳의 봉 위치 
    selectIndex = -1;
    //데이터 전체 크기
    size = s;
    //돈 소수 표시 단위
    money = m;
    
    startIndex = minIndex;
    
    //날짜 "2012.04.04" 포맷
    calendar = cal;
        
    //전체 크기가 화면에 보여질 날짜 수 보다 작으면 조정
    while ( dayCount > size )
       dayCount -= 10;
    
    index = size - dayCount;
    
    //주가 양이 모자를 경우 인덱스 수정
    if ( index < startIndex )
        index = startIndex;
        
    //차트 왼쪽 여백  
    chartWidthPadding = 30;
    //캔버스 넓이
    canvasWidth = 900;  
    //canvasWidth = window.innerWidth - chartWidthPadding;
    //차트 넓이 
    chartWidth = canvasWidth - ( chartWidthPadding*2 + chartWidthPadding/2 ) ;
    /*
     * 하나의 봉이 그려질 공간의 넓이. 
     * 차트의 넓이에서 화면에 그려질 날짜 수로 나누어 구하므로
     * 날짜 수가 증가하면 갭이 작아져 더 많은 봉을 그리기 때문에 축소가 되고
     * 날짜 수가 작아지면 갭이 커져 더 큰 봉을 그리기 때문에 확대가 된다.
     */
    chartWidthGap = chartWidth / (dayCount + 1);
    
    //차트의 스크롤바, 메뉴 등에 대한 설정
    setControlChart();
    
    //각각의 차트를 생성하며 chartCount 번째에 캔들 차트를 생성한다.
    //각각의 차트들에 맞게 넘겨받은 파라미터들을 넘겨준다.
    chartCount = 4;
    charts[0] = new PaperTradeChart(c, pro, sta);    
    charts[1] = new MacdChart(mac, si);
    charts[2] = new OscillatorChart(os);
    charts[3] = new SlowStcChart(sk, sd);
    for ( var i = 0; i < chartCount; i++ )
        charts[i].draw();
}
