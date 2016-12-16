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
<link rel="stylesheet" type="text/css" href="css/searchQuote.css">

<%
	int size; 
	int pageNum;
	DecimalFormat df;
	DecimalFormat dfReal = new DecimalFormat("###,###,##0.00");
	DecimalFormat dfVolume = new DecimalFormat("###,###,###");
	

	int pageStart, pageEnd;
	String pageStr = request.getParameter("page");
	String type = 	request.getParameter("type");
	Market market = (Market)session.getAttribute("Market");
	
	if ( market.getMoneyPosition() == 0 )
		df = new DecimalFormat("###,###,##0");
	else
		df = new DecimalFormat("###,###,##0.00");
	
	Company company = (Company)session.getAttribute("Company");
	QuoteList quoteList = (QuoteList)session.getAttribute("QuoteList");
	GeneResult gr = (GeneResult)session.getAttribute("GeneResult");
	Quote newQuote = (Quote)session.getAttribute("NewQuote");
	Quote preQuote = (Quote)session.getAttribute("PreQuote");
	int lastPage = (Integer)session.getAttribute("LastPage");
	
	size = quoteList.getSize();
	session.invalidate();
	
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
	
	double today_diff = newQuote.getClose() - preQuote.getClose();
	String today_sign;
	//����
	if ( today_diff > 0 ) {
		today_sign = "��";	
	} else if ( today_diff == 0 ) {
		today_sign = "-";
	} else {
		today_diff = today_diff * -1;
		today_sign = "��";
	}
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
	<!-- ���ٴϴ� �޴� -->
	<div id="floatdiv">
		<ul>
			<li><a href="introduce.html">����Ʈ�Ұ�</a></li>
			<li><a href="ItemRecommend?type=1">������õ</a></li>
			<li><a href="indicator.html">��ǥ����</a></li>
			<li><a href="pattern.html">ĵ����Ʈ����</a></li>
			<li><a href="aboutUS.html">About US</a></li>
			<li style="font-weight: bold"><a href="#">��TOP</a></li>
		</ul>
	</div>

	<div id="wrap">
		<header>
			<!-- �ΰ� �� �� -->
			<div id="logo">
				<a href="./"><img src="images/stock_vvip.png" alt="imsi_logo"
					width="160" height="100" border="0"></a>
			</div>

            <!-- �޴� -->
            <nav class=box>
                <ul>
                	<li><a href="aboutUS.html">About US</a></li>
                    <li><a href="pattern.html">ĵ����Ʈ����</a></li>
                    <li><a href="indicator.html">��ǥ����</a></li>
                    <li><a href="ItemRecommend?type=1">������õ</a></li>
                    <li><a href="introduce.html">����Ʈ�Ұ�</a></li>
                </ul>
            </nav>
            <div class="clear"></div>

			<!--�˻�â�� ��ư-->
			<div id="search">
				<form action="Search">
					<input type="hidden" name="type" value="1"> 
					<select 	name="market" class="selector">
						<option value="ALL" selected>��ü����</option>
						<option value="KOSPI">KOSPI</option>
						<option value="KOSDAQ">KOSDAQ</option>
						<option value="NYSE">NYSE</option>
						<option value="NASDAQ">NASDAQ</option>
						<option value="AMEX">AMEX</option>
					</select> <span class="search_window"> <input type="text" size=50
						name=search id="searchInput" class="input_text">						
					</span> 
					<input type="submit" name="Ȯ�ι�ư" value="�˻�" class="submit_button">
				</form>
			</div>
		</header>

		<div class="clear"></div>


		<!-------Search Result ------->
		<article>
			<div id=chMenu>
				<ul>
					<li class=leftMenu><%= ( (company.getNameInEng() != null) ? ( company.getNameInEng() ) : ( company.getNameInEng() ) ) %>
						- <%= company.getSymbol() %> <%
						if ( gr!= null && gr.getGene().getProfit()>0 ) {
							if ( gr.getStatus() == GeneticAlgorithm.BUY )
								out.println(" / �ż� ��õ");
							else if ( gr.getStatus() == GeneticAlgorithm.SELL )
								out.println(" / �ŵ� ��õ");
						}
							
					%></li>
					<li class=right1Menu><a
						href="./SearchChart?type=2&symbol=<%=company.getSymbol()%>">&nbsp;��Ʈ&nbsp;</a></li>
					<li class=right2Menu><a href="#">&nbsp;�ְ�&nbsp;</a></li>
					<%
						if ( gr != null )
							out.println("<li class=right2Menu><a href=\"./RecommendHistory?type=1&symbol=" 
							+ company.getSymbol() + "\"> &nbsp;��õ History &nbsp;</a></li>");
					%>
				</ul>
			</div>
			<div>
				<table id=recent1>
					<tr>
						<td>���� :</td>
						<td><%= newQuote.getTradeDate().toString() %>&nbsp;&nbsp;</td>
					</tr>
				</table>
				<table id=recent2>
					<tr>
						<td class=nowQuote>���簡</td>
						<%
			   			if ( today_sign.equals("��") )
							out.println("<td id=minus class=nowQuote>" + df.format(newQuote.getClose()) + "</td>");
						else
							out.println("<td id=plus class=nowQuote>" + df.format(newQuote.getClose()) + "</td>");					
			   		%>

						<td>�ð�</td>
						<td><%= df.format(newQuote.getOpen()) %></td>
						<td>��</td>
						<td><%= df.format(newQuote.getHigh()) %></td>
						<td>�ŷ���</td>
						<td><%= dfVolume.format(newQuote.getVolume()) %></td>
					</tr>
					<tr>
						<td class=nowQuoteSub>���ϴ��</td>
						<%
						if ( today_sign.equals("��") )
							out.println("<td id=minus class=nowQuoteSub>" + today_sign + df.format(today_diff) + "&nbsp;&nbsp;" + "-" +							
									dfReal.format(today_diff/preQuote.getClose()*100) + "%</td>");
						else
							out.println("<td id=plus class=nowQuoteSub>" + today_sign + df.format(today_diff) + "&nbsp;&nbsp;" + 
									dfReal.format(today_diff/preQuote.getClose()*100) + "%</td>");							
					%>
						<td>����</td>
						<td><%= df.format(preQuote.getClose()) %></td>
						<td>����</td>
						<td><%=  df.format(newQuote.getLow())%></td>
						<td>&nbsp;</td>
					</tr>
				</table>
			</div>
			<table id=quote>
				<th>��¥</th>
				<th>�ð�</th>
				<th>����</th>
				<th>��</th>
				<th>����</th>
				<th>���Ϻ�</th>
				<th>�ŷ���</th>
				<%
				out.println("<tr><td colspan=\"7\" class=line></td></tr>");
				double diff;
				String sign;
				for ( int i = 0; i < size-1; i++ ) {					
					Quote q = quoteList.getQuote(i);
					diff = q.getClose() - quoteList.getQuote(i+1).getClose();
					if ( diff > 0 ) {
						sign = "��";	
					} else if ( diff == 0 ) {
						sign = "-";
					} else {
						diff = diff * -1;
						sign = "��";
					}
					if( ( i%5==0 ) && i!=0 )
					{
						out.println("<tr><td colspan=\"7\" class=line></td></tr>");
					}
					out.println("<tr><td>" + q.getTradeDate().toString() + "</td>" +
						"<td>" + df.format(q.getOpen()) + "</td>" +
						"<td>" + df.format(q.getLow()) + "</td>" +
						"<td>" + df.format(q.getHigh()) + "</td>" +
						"<td>" + df.format(q.getClose()) + "</td>");
					if ( sign.equals("��") )
						out.println("<td id=minus class=right>" + sign + df.format(diff) + "&nbsp;&nbsp;</td>");
					else
						out.println("<td id=plus class=right>" + sign + df.format(diff) + "&nbsp;&nbsp;</td>");
					
					out.println("<td class=right>" + dfVolume.format(q.getVolume()) + "&nbsp;&nbsp;</td>" + "</tr>");
				}
				out.println("<tr><td colspan=\"7\" class=line></td></tr>");
			%>
			</table>
			<div id=page>
				<%
				int temp = pageStart-1;
				if ( temp == 0 )
					temp = 1;
				if ( temp != 1 ) {
					out.println("<span><a href=./SearchQuote?type=" + type + "&symbol=" + 
							company.getSymbol() + "&page=" + temp + "> ��</a></span>");
				}
				for ( int i = pageStart; i <= pageEnd; i++) {
					if ( i == pageNum ) {
						out.println("<span id=current><a href=./SearchQuote?type=" + type + "&symbol=" + 
						company.getSymbol() + "&page=" + i + ">" + i + "</a></span>");
					} else {
						out.println("<span ><a href=./SearchQuote?type=" + type + "&symbol=" + 
								company.getSymbol() + "&page=" + i + ">" + i + "</a></span>");
					}
				}
				if ( pageEnd < lastPage ) {
					out.println("<span><a href=./SearchQuote?type=" + type + "&symbol=" + 
						company.getSymbol() + "&page=" + (pageEnd+1) + "> ��</a></span>");
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


