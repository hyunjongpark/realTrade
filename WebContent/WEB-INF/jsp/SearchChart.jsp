<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" errorPage="ServerInternalError.jsp"%>    
<%@ page import="java.util.*"%>
<%@ page import="com.vvip.quote.*"%>
<%@ page import="com.vvip.util.*"%>
<%@ page import="com.vvip.ga.*"%>
<!DOCTYPE HTML>
<html>
	    <head>
        <meta charset="euc-kr">       
        <link rel="stylesheet" type="text/css" href="css/default.css" >
        <link rel="stylesheet" type="text/css" href="css/search.css">
        <link rel="stylesheet" type="text/css" href="css/chartMenu.css">
            
		<title>차트</title>		
		<script src="./js/chart/Font.js"></script>
		<script src="./js/chart/Color.js"></script>
        <script src="./js/chart/InitChart.js"></script>
        <script src="./js/chart/ChartScroll.js"></script>
        <script src="./js/chart/DrawChart.js"></script>
        <script src="./js/chart/ControlChart.js"></script>
        <script src="./js/chart/CandleChart.js"></script>
        <script src="./js/chart/VolumeChart.js"></script>
        <script src="./js/chart/MacdChart.js"></script>
        <script src="./js/chart/RsiChart.js"></script>
        <script src="./js/chart/ObvChart.js"></script>
        <script src="./js/chart/AdxChart.js"></script>
        <script src="./js/chart/CciChart.js"></script>
        <script src="./js/chart/WilliamsChart.js"></script>
        <script src="./js/chart/TrixChart.js"></script>
        <script src="./js/chart/RocChart.js"></script>
        <script src="./js/chart/DmiChart.js"></script>
        <script src="./js/chart/FastStcChart.js"></script>
        <script src="./js/chart/SlowStcChart.js"></script>
        <script src="./js/chart/EventListener.js"></script>
		<%
		Market market = (Market)session.getAttribute("Market");
		Company company = (Company)session.getAttribute("Company");
		QuoteList quoteList = (QuoteList)session.getAttribute("QuoteList");
		GeneResult gr = (GeneResult)session.getAttribute("GeneResult");
		session.invalidate();
		
		TechnicalAnalysis ta = new TechnicalAnalysis(quoteList.getList(), 0);

		int size = quoteList.getSize();
		int money = market.getMoneyPosition();
		ArrayList<String> calendar = new ArrayList<String>();	
		ArrayList<Double> open = new ArrayList<Double>();
		ArrayList<Double> close = new ArrayList<Double>();
		ArrayList<Double> low = new ArrayList<Double>();
		ArrayList<Double> high = new ArrayList<Double>();
		ArrayList<Long> volume = new ArrayList<Long>();
		ArrayList<String> pattern = new ArrayList<String>();
		ArrayList<Double> e5 = new ArrayList<Double>();
		ArrayList<Double> e20 = new ArrayList<Double>();
		ArrayList<Double> e60 = new ArrayList<Double>();
		ArrayList<Double> e120 = new ArrayList<Double>();
		ArrayList<Double> bu = new ArrayList<Double>();
		ArrayList<Double> bm = new ArrayList<Double>();
		ArrayList<Double> bl = new ArrayList<Double>();
		ArrayList<Double> sa = new ArrayList<Double>();
		ArrayList<Double> macd = new ArrayList<Double>();
		ArrayList<Double> signal = new ArrayList<Double>();
		ArrayList<Double> oscillator = new ArrayList<Double>();
		ArrayList<Double> rsi = new ArrayList<Double>();
		ArrayList<Double> obv = new ArrayList<Double>();
		ArrayList<Double> adx = new ArrayList<Double>();
		ArrayList<Double> adxMa = new ArrayList<Double>();
		ArrayList<Double> sStcK = new ArrayList<Double>();
		ArrayList<Double> sStcD = new ArrayList<Double>();
		ArrayList<Double> fStcK = new ArrayList<Double>();
		ArrayList<Double> fStcD = new ArrayList<Double>();
		ArrayList<Double> cci = new ArrayList<Double>();
		ArrayList<Double> trix = new ArrayList<Double>();
		ArrayList<Double> roc = new ArrayList<Double>();
		ArrayList<Double> willams = new ArrayList<Double>();
		ArrayList<Double> pdmi = new ArrayList<Double>();
		ArrayList<Double> mdmi = new ArrayList<Double>();
		
		ta.sma(5, e5);
		ta.sma(20, e20);
		ta.sma(60, e60);
		ta.sma(120, e120);
		ta.bollingerBend(20, bu, bm, bl);
		ta.parabolicSar(sa);
		ta.macd(12, 26, 9, macd, signal, oscillator);
		ta.rsi(rsi);
		ta.obv(obv);
		ta.adx(adx, adxMa);
		ta.slowStc(sStcK, sStcD);
		ta.fastStc(fStcK, fStcD);
		ta.cci(cci);
		ta.trix(trix);
		ta.roc(roc);
		ta.willams(willams);
		ta.dma(pdmi, mdmi);
		
		ChartPattern chartPattern;
		chartPattern = ChartPattern.getInstance();

		for (int i = 0; i < size; i++) {
			Quote q = quoteList.getQuote(i);
			calendar.add("\"" + q.getTradeDate().toString() + "\"");			
			open.add(q.getOpen());
			close.add(q.getClose());
			low.add(q.getLow());
			high.add(q.getHigh());
			volume.add(q.getVolume());
			pattern.add(chartPattern.convertPattern(q.getPattern()));
		}
		
		%>
		<script type="text/javascript">			
	    	function LockF5() {
	    		if ( event.keyCode == 116 ) {
	    			event.keyCode = 0;
	    			return false;
	    		}
	    	}
	    
	 	   document.onkeydown=LockF5;
	
		
		
			var s = <%= size %>
			var m = <%= money %>
			var cal = <%= calendar %>
			var o = <%= open %>
			var c = <%= close %>
			var l = <%= low %>
			var h = <%=  high %>
			var v = <%= volume %>
			var p = <%= pattern %>
 			var e5 = <%= e5 %>
			var e20 = <%= e20 %>
			var e60 = <%= e60 %>
			var e120 = <%= e120 %>
			var bu = <%= bu %>
			var bm = <%= bm %>
			var bl = <%= bl %>	 
			var sa = <%= sa %>
			var mac = <%= macd %>	 
			var si = <%= signal %>	 
			var os = <%= oscillator %>	 
			var rs = <%= rsi %>	 
			var ob = <%= obv %>	 
			var ad = <%= adx %>
			var adMa = <%= adxMa %>
			var sk = <%= sStcK %>
			var sd = <%= sStcD %>
			var fk = <%= fStcK %>
			var fd = <%= fStcD %>
			var cc = <%= cci %>
			var tr = <%= trix %>
			var ro = <%= roc %>
			var wi = <%= willams %>
			var pdm = <%= pdmi %>
			var mdm = <%= mdmi %>		

			
			</SCRIPT>
		</script>
	</head>
<body onLoad="init(s, m, cal, o, c, l, h, v, p, e5, e20, e60, e120, bu, bm, bl, sa, mac, si, os, rs, ob, ad, adMa, sk, sd, fk, fd, cc, tr,ro, wi, pdm, mdm);"
	contextmenu="return false" onselectstart="return false" ondragstart="return false" onMouseDrag="false;">
	<!-- 떠다니는 메뉴 -->
	<div id="floatdiv">
		<ul>
			<li><a href="introduce.html">사이트소개</a></li>
			<li><a href="ItemRecommend?type=1">종목추천</a></li>
			<li><a href="indicator.html">지표설명</a></li>
			<li><a href="pattern.html">캔들차트설명</a></li>
			<li><a href="aboutUS.html">About US</a></li>
			<li style="font-weight: bold"><a href="#">▲TOP</a></li>
		</ul>
	</div>

	<div id="wrap">
		<header>
			<!-- 로고 들어갈 곳 -->
			<div id="logo">
				<a href="./"><img src="images/stock_vvip.png" alt="imsi_logo"
					width="160" height="100" border="0"></a>
			</div>

            <!-- 메뉴 -->
            <nav class=box>
                <ul>
                	<li><a href="aboutUS.html">About US</a></li>
                    <li><a href="pattern.html">캔들차트설명</a></li>
                    <li><a href="indicator.html">지표설명</a></li>
                    <li><a href="ItemRecommend?type=1">종목추천</a></li>
                    <li><a href="introduce.html">사이트소개</a></li>
                </ul>
            </nav>
            <div class="clear"></div>

			<!--검색창과 버튼-->
			<div id="search">
				<form action="Search">
					<input type="hidden" name="type" value="1"> 
					<select 	name="market" class="selector">
						<option value="ALL" selected>전체종목</option>
						<option value="KOSPI">KOSPI</option>
						<option value="KOSDAQ">KOSDAQ</option>
						<option value="NYSE">NYSE</option>
						<option value="NASDAQ">NASDAQ</option>
						<option value="AMEX">AMEX</option>
					</select> <span class="search_window"> <input type="text" size=50
						name=search id="searchInput" class="input_text">						
					</span> 
					<input type="submit" name="확인버튼" value="검색" class="submit_button">
				</form>
			</div>
		</header>

		<div class="clear"></div>

		<!-------chart ------->
		<article>
			<div id=chMenu>
				<ul>
					<li class=leftMenu><%= ( (company.getNameInEng() != null) ? ( company.getNameInEng() ) : ( company.getNameInEng() ) ) %>
					<%
						if ( gr!= null && gr.getGene().getProfit()>0 ) {
							if ( gr.getStatus() == GeneticAlgorithm.BUY )
								out.println(" / 매수 추천");
							else if ( gr.getStatus() == GeneticAlgorithm.SELL )
								out.println(" / 매도 추천");
						}							
					%>
					</li>									
					<li class=right1Menu><a href="#">차트</a></li>					
					<li class=right2Menu><a href="./SearchQuote?type=2&symbol=<%=company.getSymbol()%>">주가</a></li>
					<%
						if ( gr != null )
							out.println("<li class=right2Menu><a href=\"./RecommendHistory?type=1&symbol=" 
							+ company.getSymbol() + "\">추천 History</a></li>");
					%>
				</ul>
			</div>
			<div class="clear"> </div> 
			<div id ="controlMenu"> 
		<div id="menu1" class="menuContainer">
			캔들차트
		</div>
		<div id="menu2" class="menuContainer">
			보조지표
		</div>
		<div id="menu3" class="menuContainer">
			옵션
		</div >
		<div class="btnContainer" onclick="changeIndex(-100)"> << </div>
		<div class="btnContainer" onclick="changeIndex(-1)"> < </div>
		<div class="btnContainer" onclick="changeDayCount(-10)"> + </div>
		<div class="btnContainer" onclick="changeDayCount(10)"> - </div>
		<div class="btnContainer" onclick="changeIndex(1)"> > </div>
		<div class="btnContainer" onclick="changeIndex(100)"> >> </div>		
	</div>
	<div id = subControlMenu>
		<form id="control">
			<div id="subContainer1" >			
					이동평균<input type="checkbox" id="ma">
					Bollinger Band<input type="checkbox" id="bollinger">
					Parabolic Sar<input type="checkbox" id="sar">					
			</div>
			<div id="subContainer2">
				거래량<input type="checkbox" id="volume">
				MACD<input type="checkbox" id="macd">
				RSI<input type="checkbox" id="rsi">
				OBV<input type="checkbox" id="obv">
				ADX<input type="checkbox" id="adx">
				S STC<input type="checkbox" id="sstc">
				F STC<input type="checkbox" id="fstc">				
				CCI<input type="checkbox" id="cci">
				Williams<input type="checkbox" id="williams">
				DMI<input type="checkbox" id="dmi">
				TRIX<input type="checkbox" id="trix">
				ROC<input type="checkbox" id="roc">
											
			</div>
			<div id="subContainer3">
				하이라이트<input type="checkbox" id="highlight">
				정보창<input type="checkbox" id="pattern">		
			</div>			
			</form>
	</div>	
	<canvas id="candleChartCanvas">
            HTML5 X 지원
        </canvas>
		<canvas id="scrollCanvas">        	
        </canvas>        
	<canvas id="volumeChartCanvas">            
        </canvas>
	<div align="left" style="padding-right: 3%">
		
		
	</div>
		<canvas id="macdChartCanvas" hidden="true"></canvas>
		<canvas id="rsiChartCanvas" hidden="true"> </canvas>    
		<canvas id="obvChartCanvas" hidden="true"></canvas>
    	<canvas id="adxChartCanvas" hidden="true"></canvas>
    	<canvas id="sstcChartCanvas" hidden="true"></canvas>
    	<canvas id="fstcChartCanvas" hidden="true"></canvas>
    	<canvas id="cciChartCanvas" hidden="true"></canvas>
    	<canvas id="williamsChartCanvas" hidden="true"></canvas>
    	<canvas id="dmiChartCanvas" hidden="true"></canvas>
    	<canvas id="trixChartCanvas" hidden="true"></canvas>
    	<canvas id="rocChartCanvas" hidden="true"></canvas>
	</article>
	<footer>
           <hr>
           <div id = copy>
				<img src="images/footer.png"/>
			</div>
       </footer>
	</div>
</body>
</html>
