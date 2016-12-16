<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" errorPage="ServerInternalError.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.vvip.quote.*"%>
<%@ page import="java.text.DecimalFormat"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="euc-kr">
<link rel="stylesheet" type="text/css" href="css/default.css">
<link rel="stylesheet" type="text/css" href="css/search.css">
<link rel="stylesheet" type="text/css" href="css/searchQuote.css">
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
	ArrayList<Company> companyList = (ArrayList<Company>)session.getAttribute("CompanyList");
	ArrayList<Quote> quotes = (ArrayList<Quote>)session.getAttribute("Quotes");
	
	session.invalidate();
	
	DecimalFormat dfNum = new DecimalFormat("###,###,###"); 
	DecimalFormat dfReal = new DecimalFormat("###,###,##0.00");
	
	int pageStart, pageEnd;
	int pageNum;
	int lastPage = companyList.size()/20 + 1;
	String pageStr = request.getParameter("page");
	String market = request.getParameter("market");		
	String search = request.getParameter("search");  
	
	if ( pageStr == null )
		pageNum = 1;
	else
		pageNum = Integer.valueOf(pageStr);
	
	if ( pageNum < 1)
		pageNum = 1;
	
	if ( pageNum > lastPage )
		pageNum = lastPage;
	
	
	pageStart = pageEnd = pageNum;
	
	while ( pageStart % 10 != 1 ) 
		pageStart--;
	
	while ( pageEnd % 10 != 0 )
		pageEnd++;
	if ( pageEnd > lastPage )
		pageEnd = lastPage;
%>

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
		
		<!-------Search Result ------->
		<article>
		<table id=result>		
			<tr><th>No</th><th>종목명</th><th>종가</th><th>전일비</th><th>거래량</th></tr>
			<%
				double diff;
				int j = 0;
				String sign;

	    		
				for ( int i = ((pageNum-1)*20); i < (pageNum*20) && i < companyList.size(); i++ ) {				
						diff = quotes.get(j).getClose() - quotes.get(j).getHigh();
						if ( diff > 0 ) {
							sign = "▲";	
						} else if ( diff == 0 ) {
							sign = "-";
						} else {
							diff = diff * -1;
							sign = "▼";
						}
					
					
					out.println("<tr><td class=index>" + (i+1) + "</td>");
					out.println("<td class=symbol><a href=\"/vvipserver_a/SearchQuote?type=1&symbol=" +companyList.get(i).getSymbol() + "\">");
					if (companyList.get(i).getNameInKor().contains("?"))
						out.println( companyList.get(i).getNameInEng() + "</a></td>");					
					else
						out.println(companyList.get(i).getNameInKor() + "</a></td>");
					if ( quotes.get(j).getLow() > 1)
						out.println("<td>" + dfReal.format(quotes.get(j).getClose()) + "</td>");
					else
						out.println("<td>" + dfNum.format(quotes.get(j).getClose()) + "</td>");
					
					if ( sign.equals("▼")  ) {
						out.print("<td id=minus>" + sign);
						if ( quotes.get(j).getLow() > 1 )
							out.println(dfReal.format(diff) + "</td>");
						else
							out.println(dfNum.format(diff) + "</td>");
					}
					else {
						out.print("<td id=plus>" + sign);
						if ( quotes.get(j).getLow() > 1 )
							out.println(dfReal.format(diff) + "</td>");
						else
							out.println(dfNum.format(diff) + "</td>");
					}
					
					out.println("<td>" + dfNum.format(quotes.get(j).getVolume()) + "</td></tr>");
					j++;
				}
			%>		
		</table>
		<div id=page>
				<%
				if ( lastPage != 1 ) {
					String address = "&market=" + market + "&search=" + search; 
					int temp = pageStart-1;
					if ( temp == 0 )
						temp = 1;					
					if ( temp != 1 ) {
						out.println("<span><a href=/vvipserver_a/SearchQuote?type=1&page=" + temp + address + "> ◀</a></span>");
					}
					for ( int i = pageStart; i <= pageEnd; i++) {
						if ( i == pageNum ) {
							out.println("<span id=current><a href=/vvipserver_a/Search?type=1&page=" + i + address +">" + i + "</a></span>");
						} else {
							out.println("<span ><a href=/vvipserver_a/Search?type=1&page=" + i +address + ">" + i + "</a></span>");
						}
					}
					if ( pageEnd < lastPage ) {
						out.println("<span><a href=/vvipserver_a/Search?type=1&page=" + (pageEnd+1) + address +"> ▶</a></span>");
					}
				}
			%>
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


