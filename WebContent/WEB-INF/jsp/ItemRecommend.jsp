<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR" errorPage="ServerInternalError.jsp"%>
<%@ page import="java.util.*"%>	
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.vvip.quote.*"%>
<%@ page import="com.vvip.ga.*"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="euc-kr">
<link rel="stylesheet" type="text/css" href="css/default.css">
<link rel="stylesheet" type="text/css" href="css/search.css">
<link rel="stylesheet" type="text/css" href="css/recommend.css">

<%
	ArrayList<GeneResult> grList = (ArrayList<GeneResult>) session.getAttribute("GeneList");
	ArrayList<Company> companyList = (ArrayList<Company>) session.getAttribute("CompanyList");
	session.invalidate();
%>
 <script>
    function LockF5() {
    	if ( event.keyCode == 116 ) {
    		event.keyCode = 0;
    		return false;
    	}
    }
    
    document.onkeydown=LockF5;
</script>
</head>
<body>
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


		<!-------Recommend ------->
		<article>
			<div id=recommend>
				<div class="introduce">* &nbsp; 각각의 종목에 대해 유전알고리즘을 이용해 특정 기간 동안 가장 좋은 
				수익률을 얻는<br> Slow Stoch와 MACD를 구하여 오늘의 매수 / 매도 종목을 알려줍니다. 
				</div>
				<div class=buy>매수를 추천하는 종목</div>
				<table class=item>
					<tr>
						<td class="tag">종목</td>
						<td class="tag">수익율</td>
						<td class="tag">기간</td>
					</tr>
					<%
						for (int i = 0; i < grList.size(); i++) {
							if (grList.get(i).getStatus() == GeneticAlgorithm.BUY) {
								String name = (!companyList.get(i).getNameInKor().contains("?") ? (companyList
										.get(i).getNameInKor()) : (companyList.get(i)
										.getNameInEng()));
								out.println("<tr><td>");
								out.println("<a href=./SearchQuote?type=1&symbol="
										+ companyList.get(i).getSymbol() + ">" + name
										+ "</a></td>");
								out.println("<td>" + grList.get(i).getGene().getProfit()
										+ "%</td>");
								out.println("<td>" + grList.get(i).getStart() + " ~ "
										+ grList.get(i).getEnd() + "</td>");
								out.println("</td>");
							}
						}
					%>
				</table>
				<br>
				<div class=sell>매도를 추천하는 종목</div>
				<table class=item>
					<tr>
						<td class="tag">종목</td>
						<td class="tag">수익율</td>
						<td class="tag">기간</td>
					</tr>
					<%
						for (int i = 0; i < grList.size(); i++) {
							if (grList.get(i).getStatus() == GeneticAlgorithm.SELL) {
								String name = (!companyList.get(i).getNameInKor().contains("?") ? (companyList
										.get(i).getNameInKor()) : (companyList.get(i)
										.getNameInEng()));
								out.println("<tr><td>");
								out.println("<a href=./SearchQuote?type=1&symbol="
										+ companyList.get(i).getSymbol() + ">" + name
										+ "</a></td>");
								out.println("<td>" + grList.get(i).getGene().getProfit()
										+ "%</td>");
								out.println("<td>" + grList.get(i).getStart() + " ~ "
										+ grList.get(i).getEnd() + "</td>");
								out.println("</td>");
							}
						}
					%>
				</table>
			</div>
			<div class="clear"></div>
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


