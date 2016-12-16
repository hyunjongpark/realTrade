//Control Form
var controlForm;

//차트
var chartCount;
var checkChart = [];

//캔들차트에서 오버레이 되는 차트
var candleLineCount;
var checkCandleLine = [];

//패턴이나 하이라이트에 대한 옵션
var candleOptionCount;
var checkOption = [];

//메뉴에 대한 서브메뉴를 보이기 위한 변수
var menuCount;
var menu = [];
var subMenu = [];

/**
 * 체크 박스 설정 따라 변화
 */
function setControlChart() {  
    setElementId();    
    setConfiguration();
}

/**
 * 각각의 체크 박스에 대한 id를 지정한다.
 */
function setElementId() {
	//Control Form의 객체 설정
    controlForm = document.getElementById("control");
    //control 폼에서 변화가 생기면 controlChart 함수를 호출하게 한다.   
    controlForm.addEventListener('change', controlChart, false);
    
    //차트 체크에 대한 객체 설정
    chartCount = 12;
    checkChart[0] = document.getElementById("volume");
    checkChart[1] = document.getElementById("macd");
    checkChart[2] = document.getElementById("rsi");
    checkChart[3] = document.getElementById("obv");
    checkChart[4] = document.getElementById("adx");
    checkChart[5] = document.getElementById("cci");
    checkChart[6] = document.getElementById("williams");
    checkChart[7] = document.getElementById("trix");
    checkChart[8] = document.getElementById("roc");
    checkChart[9] = document.getElementById("dmi");
    checkChart[10] = document.getElementById("fstc");
    checkChart[11] = document.getElementById("sstc");
    
    //캔들 차트 주가 정보
    candleLineCount = 3;
    checkCandleLine[0] = document.getElementById("ma");
    checkCandleLine[1] = document.getElementById("bollinger");
    checkCandleLine[2] = document.getElementById("sar");
    
    //차트의 하이라이트, 정보창
    candleOptionCount = 2;
    checkOption[0] = document.getElementById("highlight");
    checkOption[1] = document.getElementById("pattern");
    
    //차트 메뉴
    menuCount = 3;
    //캔들차트
    menu[0] = document.getElementById("menu1");
    //보조지표
    menu[1] = document.getElementById("menu2");
    //옵션
    menu[2] = document.getElementById("menu3");
    //캔들 차트 서브메뉴
    subMenu[0] = document.getElementById("subContainer1");
    //보조지표 서브메뉴
    subMenu[1] = document.getElementById("subContainer2");
    //옵션 서브메뉴
    subMenu[2] = document.getElementById("subContainer3");
    
    //각각의 메뉴에 대한 서브메뉴를 보여주는 이벤트 처리기 설정 
    menu[0].addEventListener('mousemove', showMenu1, false);
    menu[1].addEventListener('mousemove', showMenu2, false);
    menu[2].addEventListener('mousemove', showMenu3, false);
        
    //스크롤 추가
    initScroll();
}

/**
 * 초기에 환경 설정
 */
function setConfiguration() {    
    checkOption[0].checked = true;
    checkOption[1].checked = true;   
    checkChart[0].checked = true;
}

/**
 * 체크 박스가 눌려졌을 때 호출 되는 함수
 */
function controlChart() {
    //캔들차트의 옵션들이 추가 될 때마다 여백과 크기를 늘인다.
    charts[chartCount].heightPadding = 45;
    charts[chartCount].canvas.height = 410;
    for ( var i = 0; i < candleLineCount; i++ ) {
        if ( checkCandleLine[i].checked == true ) {
            charts[chartCount].heightPadding += 15;
            charts[chartCount].canvas.height += 15;
        }
    }
    charts[chartCount].canvasHeight = charts[chartCount].canvas.height;
    
    if ( checkOption[1].checked == false )
        patternFix = -1;
        
    charts[chartCount].draw();         
    
    for ( var i = 0; i < chartCount; i++ ) {
        if ( checkChart[i].checked == false ) {
            charts[i].chartDisable();
        }
        else {
            charts[i].chartEnable();
        }
    }       
}

/**
 * 날짜 이동
 */
function changeIndex(change) {
    if(change == 100)
        change = dayCount;
    else if(change == -100)
        change = -dayCount;

    index += change;

    if(index < 0)
        index = 0;
    if(index + dayCount >= size)
        index = size - dayCount;
    
    applyScroll();
   
    charts[chartCount].draw();

    for(var i = 0; i < chartCount; i++) {
        if(checkChart[i].checked == true)
            charts[i].draw();
    }
}

/**
 * 표시 하는 총 날짜 수 변화
 */
function changeDayCount(change) {
    var temp = dayCount;
    temp += change;
    
    if ( temp > size )
        return;
    
    applyScroll();

    if(temp >= 5 && temp <= 250) {
        dayCount = temp;

        if(index + dayCount >= size) {
            while(index + dayCount > size) {
                index--;
            }
        }
        
        chartWidthGap = chartWidth / (dayCount + 1);
        
        charts[chartCount].draw();

        for(var i = 0; i < chartCount; i++) {
            if(checkChart[i].checked == true) {
                charts[i].draw();
            }
        }
    }
}

function showMenu1() {
    subMenu[0].style.visibility="visible";
    subMenu[1].style.visibility="hidden";
    subMenu[2].style.visibility="hidden";
}
function showMenu2() {
    subMenu[0].style.visibility="hidden";
    subMenu[1].style.visibility="visible";
    subMenu[2].style.visibility="hidden";
}
function showMenu3() {
    subMenu[0].style.visibility="hidden";
    subMenu[1].style.visibility="hidden";
    subMenu[2].style.visibility="visible";
}

