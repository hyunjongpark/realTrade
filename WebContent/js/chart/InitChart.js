var calendar = [];

var chartCount;
var charts = [];

//ȭ�鿡 �������� ��¥ ��
var dayCount;

//���콺�� ���� ȭ�鿡 ������ �׷��� �ε���
var drawIndex;

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
             
function init(s, m, cal, o, c, l, h, v, p, e5, e20, e60, e120, bu, bm, bl, sa, mac, si, os, rs, ob, ad, adMa, sk, sd, fk, fd, cc, tr,ro, wi, pdm, mdm) {
	dayCount = 80;
	//���콺�� ��ġ�� ���� �� ��ġ 
	selectIndex = -1;
	//������ ��ü ũ��
	size = s;
	//�� �Ҽ� ǥ�� ����
	money = m;
	//��¥ "2012.04.04" ����
	calendar = cal;
		
	//��ü ũ�Ⱑ ȭ�鿡 ������ ��¥ �� ���� ������ ����
	while ( dayCount > size )
	   dayCount -= 10;
	
	index = size - dayCount;
	
	//�ְ� ���� ���ڸ� ��� �ε��� ����
	if ( index < 0 )
		index = 0;
		
	//��Ʈ ���� ����	
	chartWidthPadding = 30;
	//ĵ���� ����
	canvasWidth = 900;	
	//canvasWidth = window.innerWidth - chartWidthPadding;
	//��Ʈ ����	
	chartWidth = canvasWidth - ( chartWidthPadding*2 + 7);
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
	chartCount = 12;
	charts[chartCount] = new CandleChart(o, c, l, h, p, e5, e20, e60, e120, bu, bm, bl, sa);
	charts[0] = new VolumeChart(v);	
	charts[1] = new MacdChart(mac, si, os);	
	charts[2] = new RsiChart(rs);	
	charts[3] = new ObvChart(ob);
	charts[4] = new AdxChart(ad, adMa);
	charts[5] = new CciChart(cc);
	charts[6] = new WilliamsChart(wi);
	charts[7] = new TrixChart(tr);
	charts[8] = new RocChart(ro);
	charts[9] = new DmiChart(pdm, mdm);
	charts[10] = new FastStcChart(fk, fd);
	charts[11] = new SlowStcChart(sk, sd);
	charts[chartCount].draw();
}

