//Control Form
var controlForm;

//��Ʈ
var chartCount;
var checkChart = [];

//ĵ����Ʈ���� �������� �Ǵ� ��Ʈ
var candleLineCount;
var checkCandleLine = [];

//�����̳� ���̶���Ʈ�� ���� �ɼ�
var candleOptionCount;
var checkOption = [];

//�޴��� ���� ����޴��� ���̱� ���� ����
var menuCount;
var menu = [];
var subMenu = [];

/**
 * üũ �ڽ� ���� ���� ��ȭ
 */
function setControlChart() {  
    setElementId();    
    setConfiguration();
}

/**
 * ������ üũ �ڽ��� ���� id�� �����Ѵ�.
 */
function setElementId() {
	//Control Form�� ��ü ����
    controlForm = document.getElementById("control");
    //control ������ ��ȭ�� ����� controlChart �Լ��� ȣ���ϰ� �Ѵ�.   
    controlForm.addEventListener('change', controlChart, false);
    
    //��Ʈ üũ�� ���� ��ü ����
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
    
    //ĵ�� ��Ʈ �ְ� ����
    candleLineCount = 3;
    checkCandleLine[0] = document.getElementById("ma");
    checkCandleLine[1] = document.getElementById("bollinger");
    checkCandleLine[2] = document.getElementById("sar");
    
    //��Ʈ�� ���̶���Ʈ, ����â
    candleOptionCount = 2;
    checkOption[0] = document.getElementById("highlight");
    checkOption[1] = document.getElementById("pattern");
    
    //��Ʈ �޴�
    menuCount = 3;
    //ĵ����Ʈ
    menu[0] = document.getElementById("menu1");
    //������ǥ
    menu[1] = document.getElementById("menu2");
    //�ɼ�
    menu[2] = document.getElementById("menu3");
    //ĵ�� ��Ʈ ����޴�
    subMenu[0] = document.getElementById("subContainer1");
    //������ǥ ����޴�
    subMenu[1] = document.getElementById("subContainer2");
    //�ɼ� ����޴�
    subMenu[2] = document.getElementById("subContainer3");
    
    //������ �޴��� ���� ����޴��� �����ִ� �̺�Ʈ ó���� ���� 
    menu[0].addEventListener('mousemove', showMenu1, false);
    menu[1].addEventListener('mousemove', showMenu2, false);
    menu[2].addEventListener('mousemove', showMenu3, false);
        
    //��ũ�� �߰�
    initScroll();
}

/**
 * �ʱ⿡ ȯ�� ����
 */
function setConfiguration() {    
    checkOption[0].checked = true;
    checkOption[1].checked = true;   
    checkChart[0].checked = true;
}

/**
 * üũ �ڽ��� �������� �� ȣ�� �Ǵ� �Լ�
 */
function controlChart() {
    //ĵ����Ʈ�� �ɼǵ��� �߰� �� ������ ����� ũ�⸦ ���δ�.
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
 * ��¥ �̵�
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
 * ǥ�� �ϴ� �� ��¥ �� ��ȭ
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

