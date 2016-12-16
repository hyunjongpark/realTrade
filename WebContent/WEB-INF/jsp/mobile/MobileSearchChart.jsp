<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" errorPage="MobileErrorPage.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.vvip.quote.*"%>
<%@ page import="com.vvip.util.*"%>
<!DOCTYPE HTML>
<html>
    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    	 
   	<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width, user-scalable=0">
    	 <%
    	 	String typeStr = request.getParameter("type");
 			int type;
 		 			
 			if ( typeStr == null )
 				type = 3;
 			else
 				type = Integer.valueOf(typeStr);
 			
 	       	String widthStr = (String)request.getParameter("w");
 	       	int width;
 	       	
 	       	if ( widthStr == null )
 	       		width = 480 - 30;
 	       	else
 	       		width = Integer.valueOf(widthStr) - 33;
    	 %>
        <title>스마일</title>
         <link rel="stylesheet" type="text/css" href="css/mobileChartMenu.css">
         <%
         	if ( type == 3  && width < 480 )
         		out.println("<script src=\"/js/mobile/AndroidMobileFont.js\"></script>");
         	else 
         		out.println("<script src=\"/js/mobile/IPhoneMobileFont.js\"></script>");
         %>
		<script src="/js/chart/Color.js"></script>
		<script src="/js/chart/DrawChart.js"></script>
		<script src="/js/mobile/MobileChartScroll.js"></script>
		<script src="/js/mobile/MobileEventListener.js"></script>		
        <script src="/js/mobile/MobileInitChart.js"></script>                
        <script src="/js/mobile/MobileControlChart.js"></script>
        <script src="/js/chart/CandleChart.js"></script>
        <script src="/js/chart/VolumeChart.js"></script>
        <script src="/js/chart/MacdChart.js"></script>
        <script src="/js/chart/SlowStcChart.js"></script>
        <script src="/js/chart/RsiChart.js"></script>
       	<%       	    	

       	
		Market market = (Market)session.getAttribute("Market");
		Company company = (Company)session.getAttribute("Company");
		QuoteList quoteList = (QuoteList)session.getAttribute("QuoteList");
		
		TechnicalAnalysis ta = new TechnicalAnalysis(quoteList.getList(), 0);
		ta.setTaPattern();

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
		ArrayList<Double> stcK = new ArrayList<Double>();
		ArrayList<Double> stcD = new ArrayList<Double>();

		ta.sma(5, e5);
		ta.sma(20, e20);
		ta.sma(60, e60);
		ta.sma(120, e120);
		ta.bollingerBend(20, bu, bm, bl);
		ta.parabolicSar(sa);
		ta.macd(12, 26, 9, macd, signal, oscillator);
		ta.slowStc(stcK, stcD);
		ta.rsi(rsi);
		
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
		session.invalidate();
		%>
		<script type="text/javascript">
			var s = <%= size %>
			var m = <%= money %>
			var w = <%= width %>
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
			var sd = <%= stcD %>	 
			var sk = <%= stcK %>	 
		</script>		
    </head>    
    <body onLoad="init(s, m, w, cal, o, c, l, h, v, p, e5, e20, e60, e120, bu, bm, bl, sa, mac, si, os, rs, sk, sd);">
    <%
    	if ( type == 3 )
    		out.println("<div id=\"scroll\"><img src=\"images\\arrow.png\"> </div>");    
    %>
    
    <!-- 차트 위에 버튼 -->
    <div style="height:20px"></div>
	<div id=subControlMenu>
		<form id="control">
			<div id="subContainer">
				MA<input type="checkbox" id="ema" class="check"> 
				Bollinger<input type="checkbox" id="bollinger" class="check">
				Parabolic<input type="checkbox" id="sar" class="check">			
			</div>
		</form>
	</div>	
	<!-- 차트 그림 시작. -->			
	<div id="chartArea">		
        <canvas id="candleChartCanvas">
            HTML5 X 지원
        </canvas>
        <div>             
        <canvas id="scrollCanvas">        	    	
        </canvas>
		</div>      
         <canvas id="volumeChartCanvas"></canvas>        
        <canvas id="macdChartCanvas" ></canvas>
        <canvas id="sstcChartCanvas" ></canvas>
        <canvas id="rsiChartCanvas" ></canvas>
    </div>
    </body>
</html>