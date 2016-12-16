<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" errorPage="ServerInternalError.jsp"%>    
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.vvip.quote.*"%>
<%@ page import="com.vvip.util.*"%>
<%@ page import="com.vvip.ga.*"%>
<!DOCTYPE HTML>
<html>
	    <head>
        <meta charset="euc-kr">       
        <link rel="stylesheet" type="text/css" href="css/default.css" >
        <link rel="stylesheet" type="text/css" href="css/search.css">
        <link rel="stylesheet" type="text/css" href="css/chartMenu.css ">
            
		<title>차트</title>		
		<script src="./js/gaHistory/Font.js"></script>
		<script src="./js/gaHistory/Color.js"></script>
        <script src="./js/gaHistory/InitChart.js"></script>
        <script src="./js/gaHistory/ChartScroll.js"></script>
        <script src="./js/gaHistory/DrawChart.js"></script>
        <script src="./js/gaHistory/ControlChart.js"></script>
        <script src="./js/gaHistory/PaperTradeChart.js"></script>
        <script src="./js/gaHistory/SlowStcChart.js"></script>
        <script src="./js/gaHistory/MacdChart.js"></script>
        <script src="./js/gaHistory/OscillatorChart.js"></script>
        <script src="./js/gaHistory/EventListener.js"></script>
        <script>
    	function LockF5() {
	    	if ( event.keyCode == 116 ) {
    			event.keyCode = 0;
    			return false;
	    	}
    	}
		    
    	document.onkeydown=LockF5;
		</script>
		<%
		
	
		%>
		<script type="text/javascript">
			var s = <%= size %>;
			var minIndex = <%= startIndex %>;
			var m = <%= money %>;
			var sta = <%= statusStr %>;
			var cal = <%= calendar %>;
			var c = <%= close %>;
			var stcK = <%= stcK %>;
			var stcD = <%= stcD %>;
			var mac = <%= macd %>;	 
			var si = <%= signal %>;
			var os = <%= oscillator %>;
			var pro = <%= profit %>;
			</SCRIPT>
		</script>
	</head>
<body onLoad="init(s, m, minIndex, cal, c, pro, sta, stcK, stcD, mac, si, os);"
	contextmenu="return false" onselectstart="return false" ondragstart="return false" onMouseDrag="false;">
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
					<li class=leftMenu><%= ( (company.getNameInEng() != null) ? ( company.getNameInEng() ) : ( company.getNameInEng() ) ) %></li>									
					<li class=right1Menu><a href="./SearchChart?type=2&symbol=<%=company.getSymbol()%>">차트</a></li>					
					<li class=right2Menu><a href="./SearchQuote?type=2&symbol=<%=company.getSymbol()%>">주가</a></li>
					<li class=right2Menu><a href="#">추천 History</a></li>
				</ul>
			</div>
			<div class="clear"> </div> 
			
		<div id=result>
		<table border="1" cellspacing="0" cellpadding="2" width="700" bordercolor="#dcdcdc">
		<tr><td colspan="6"  align="center" bgcolor="#E1F6FA"><b>모의투자 기간</b> &nbsp;<%=gr.getStart().toString() %> ~ <%= gr.getEnd().toString() %></td></tr>
		<tr><td align="center" bgcolor="#F9FFFF">원금</td><td align="right"> <%= df.format(close.get(startIndex)) %>&nbsp; </td><td align="center" bgcolor="#F9FFFF"> 최종 수입</td><td align="right"> <%= df.format(savedProfit) %>&nbsp;</td><td align="center" bgcolor="#F9FFFF"> 수익률</td><td align="right"><%= gr.getGene().getProfit() %>% &nbsp;</td></tr>
		<tr><td align="center" bgcolor="#F9FFFF" bgcolor="#F9FFFF"> Fast K% </td><td align="right"> <%= gr.getGene().getFastK() %>&nbsp;</td><td align="center" bgcolor="#F9FFFF"> Slow K% </td><td align="right"> <%= gr.getGene().getSlowK() %>&nbsp;</td><td align="center" bgcolor="#F9FFFF"> Slow D%</td><td align="right"><%= gr.getGene().getSlowD() %>&nbsp;</td></tr>
		<tr><td align="center" bgcolor="#F9FFFF" bgcolor="#F9FFFF"> MACD 단기</td><td align="right"> <%= gr.getGene().getShortMA() %>&nbsp;</td><td align="center" bgcolor="#F9FFFF"> 장기 </td><td align="right"> <%= gr.getGene().getLongMA() %>&nbsp;</td><td align="center" bgcolor="#F9FFFF"> 지수이동평균</td><td align="right"><%= gr.getGene().getMacdMA() %>&nbsp;</td></tr>
		</table>
		</div>
		<div id ="controlMenu"> 
			<div class="btnContainer" onclick="changeIndex(-100)"> << </div>
			<div class="btnContainer" onclick="changeIndex(-1)"> < </div>
			<div class="btnContainer" onclick="changeDayCount(-30)"> + </div>
			<div class="btnContainer" onclick="changeDayCount(30)"> - </div>
			<div class="btnContainer" onclick="changeIndex(1)"> > </div>
			<div class="btnContainer" onclick="changeIndex(100)"> >> </div>		
		</div>
		<canvas id="candleChartCanvas">
            HTML5 X 지원
        </canvas>
		<canvas id="scrollCanvas">        	
        </canvas>
        <canvas id="sstcChartCanvas"> </canvas>
        <canvas id="oscillatorChartCanvas">            
        </canvas>
		<canvas id="macdChartCanvas">            
        </canvas>
        
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
