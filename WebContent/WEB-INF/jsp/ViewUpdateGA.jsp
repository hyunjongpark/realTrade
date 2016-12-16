<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" errorPage="ServerInternalError.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.vvip.quote.*"%>
<%@ page import="com.vvip.ga.*"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="euc-kr">
<link rel="stylesheet" type="text/css" href="css/default.css">
<link rel="stylesheet" type="text/css" href="css/search.css">

<%
	ArrayList<String> parameter = (ArrayList<String>)session.getAttribute("Parameter");
	GeneResult gr = (GeneResult)session.getAttribute("GeneResult");
	Company company = (Company)session.getAttribute("Company");
	session.invalidate();
%>

</head>
<body>
	<!-- 떠다니는 메뉴 -->
	<div id="floatdiv">
		<ul>
			<li><a href="introduce.html">사이트설명</a></li>
			<li><a href="pattern.html">캔들차트설명</a></li>
			<li><a href="indicator.html">지표설명</a></li>
			<li><a href="ItemRecommend?type=1">종목추천</a></li>
			<li><a href="aboutUS.html">About US</a></li>
			<li style="font-weight: bold"><a href="#">제일위로</a></li>
		</ul>
	</div>

	<div id="wrap">
		<header>
			<!-- 로고 들어갈 곳 -->
			<div id="logo">
				<a href="/"><img src="images/stock_vvip.png" alt="imsi_logo"
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
           

		<article>	
		<div style="font-size:12pt">	
		종목 : <%= ( (company.getNameInEng() != null) ? ( company.getNameInEng() ) : ( company.getNameInEng() ) ) %> - <%= company.getSymbol() %><br>
		해집단 수 <%= parameter.get(2) %> 최대진화수 <%= parameter.get(3) %> 
		교배확률 <%= parameter.get(4) %>% 변이확률 <%= parameter.get(5) %>% <br>
		학습 기간 <%= parameter.get(0)  %> ~  <%= parameter.get(1) %><br>
 		<%= gr.toString() %>
 		</div>
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