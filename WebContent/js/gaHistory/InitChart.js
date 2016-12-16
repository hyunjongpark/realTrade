var calendar = [];

var chartCount;
var charts = [];

//ȭ�鿡 �������� ��¥ ��
var dayCount;

//���콺�� ���� ȭ�鿡 ������ �׷��� �ε���
var drawIndex;
var startIndex;
/*
 * ���콺�� ��ġ�� ������ ������ ���� �׸��� �ʱ� ����
 * ������ �׸� ������ �ε��� �����ϴ� ���� 
 */
var selectIndex;

/*
 * ��� ��Ʈ�� �׸��µ� �����ϴ� �ε���
 * ���� ��� ��Ʈ�� index���� dayCount��ŭ �׸���
 * index�� ��ȭ�� ���� ��¥ �̵��� �����Ѵ�.
 */
var index;

//������ ��ü ũ��
var size;

//�� �Ҽ� ǥ�� ����
var money;

/*
 * ��Ʈ�� ����, ���� ���õ� ����
 * ĵ�� ��Ʈ �ܿ� ��� ��Ʈ���� ���̳�
 * ������ ������ �����ؾ� �ϹǷ� �Ʒ��� ������
 * ��� ��Ʈ�� ����ϸ� ���̿� ���� ���� ���� ������.
 */
var canvasWidth;
var chartWidthPadding;
var chartWidth;
var chartWidthGap;
 
var controlChart;

//���콺�� ��ġ�� �����Ѵ�.
var mouseWidthGap;
             
function init(s, m, minIndex, cal, c, pro, sta, sk, sd, mac, si, os) {
    dayCount = 80;
    //���콺�� ��ġ�� ���� �� ��ġ 
    selectIndex = -1;
    //������ ��ü ũ��
    size = s;
    //�� �Ҽ� ǥ�� ����
    money = m;
    
    startIndex = minIndex;
    
    //��¥ "2012.04.04" ����
    calendar = cal;
        
    //��ü ũ�Ⱑ ȭ�鿡 ������ ��¥ �� ���� ������ ����
    while ( dayCount > size )
       dayCount -= 10;
    
    index = size - dayCount;
    
    //�ְ� ���� ���ڸ� ��� �ε��� ����
    if ( index < startIndex )
        index = startIndex;
        
    //��Ʈ ���� ����  
    chartWidthPadding = 30;
    //ĵ���� ����
    canvasWidth = 900;  
    //canvasWidth = window.innerWidth - chartWidthPadding;
    //��Ʈ ���� 
    chartWidth = canvasWidth - ( chartWidthPadding*2 + chartWidthPadding/2 ) ;
    /*
     * �ϳ��� ���� �׷��� ������ ����. 
     * ��Ʈ�� ���̿��� ȭ�鿡 �׷��� ��¥ ���� ������ ���ϹǷ�
     * ��¥ ���� �����ϸ� ���� �۾��� �� ���� ���� �׸��� ������ ��Ұ� �ǰ�
     * ��¥ ���� �۾����� ���� Ŀ�� �� ū ���� �׸��� ������ Ȯ�밡 �ȴ�.
     */
    chartWidthGap = chartWidth / (dayCount + 1);
    
    //��Ʈ�� ��ũ�ѹ�, �޴� � ���� ����
    setControlChart();
    
    //������ ��Ʈ�� �����ϸ� chartCount ��°�� ĵ�� ��Ʈ�� �����Ѵ�.
    //������ ��Ʈ�鿡 �°� �Ѱܹ��� �Ķ���͵��� �Ѱ��ش�.
    chartCount = 4;
    charts[0] = new PaperTradeChart(c, pro, sta);    
    charts[1] = new MacdChart(mac, si);
    charts[2] = new OscillatorChart(os);
    charts[3] = new SlowStcChart(sk, sd);
    for ( var i = 0; i < chartCount; i++ )
        charts[i].draw();
}
