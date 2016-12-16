<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR" errorPage="MobileErrorPage.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.vvip.quote.*"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<link rel="stylesheet" type="text/css" href="css/mobileSearchQuote.css">
<title>Mobile Quote</title>
<%
	int size;
	int pageNum;
	DecimalFormat df;
	DecimalFormat dfReal = new DecimalFormat("###,###,##0.00");
	DecimalFormat dfVolume = new DecimalFormat("###,###,###");

	int pageStart, pageEnd;
	String pageStr = request.getParameter("page");
	String type = request.getParameter("type");
	Market market = (Market) session.getAttribute("Market");

	if (market.getMoneyPosition() == 0)
		df = new DecimalFormat("###,###,##0");
	else
		df = new DecimalFormat("###,###,##0.00");

	Company company = (Company) session.getAttribute("Company");
	QuoteList quoteList = (QuoteList) session.getAttribute("QuoteList");
	Quote newQuote = (Quote) session.getAttribute("NewQuote");
	Quote preQuote = (Quote) session.getAttribute("PreQuote");
	int lastPage = (Integer) session.getAttribute("LastPage");

	size = quoteList.getSize();
	session.invalidate();

	if (pageStr == null)
		pageNum = 1;
	else
		pageNum = Integer.valueOf(pageStr);

	if (pageNum < 1)
		pageNum = 1;

	if (pageNum > lastPage)
		pageNum = lastPage;

	pageStart = pageEnd = pageNum;

	while (pageStart % 10 != 1)
		pageStart--;

	while (pageEnd % 10 != 0)
		pageEnd++;
	if (pageEnd > lastPage)
		pageEnd = lastPage;

	double today_diff = newQuote.getClose() - preQuote.getClose();
	String today_sign;
	//음수
	if (today_diff > 0) {
		today_sign = "▲";
	} else if (today_diff == 0) {
		today_sign = "-";
	} else {
		today_diff = today_diff * -1;
		today_sign = "▼";
	}
%>
</head>
<body id = "wrap">
	<div>		
		<table id=recent2>
			<tr>
				<td class=nowQuote>현재가</td>
				<%
					if (today_sign.equals("▼"))
						out.println("<td id=minus class=nowQuote>"
								+ df.format(newQuote.getClose()) + "</td>");
					else
						out.println("<td id=plus class=nowQuote>"
								+ df.format(newQuote.getClose()) + "</td>");
				%>
				<td>전일대비</td>
				<%
					if (today_sign.equals("▼"))
						out.println("<td id=minus class=nowQuoteSub>" + today_sign
								+ df.format(today_diff) + "&nbsp;&nbsp;" + "-"
								+ dfReal.format(today_diff / preQuote.getClose() * 100)
								+ "%</td>");
					else
						out.println("<td id=plus class=nowQuoteSub>" + today_sign
								+ df.format(today_diff) + "&nbsp;&nbsp;"
								+ dfReal.format(today_diff / preQuote.getClose() * 100)
								+ "%</td>");
				%>
			</tr><tr>
				<td>전일</td>
				<td><%=df.format(preQuote.getClose())%></td>
				<td>시가</td>
				<td><%=df.format(newQuote.getOpen())%></td>
				
			</tr><tr>
				<td>고가</td>
				<td><%=df.format(newQuote.getHigh())%></td>
				<td>저가</td>
				<td><%=df.format(newQuote.getLow())%></td>
			</tr>
			<tr>
				<td>거래량</td>
				<td><%=dfVolume.format(newQuote.getVolume())%></td>
				<td class=date>기준날짜 </td>
				<td class=date><%=newQuote.getTradeDate().toString()%></td>
			</tr>
		</table>
	</div>
	<table id=quote>
<th>날짜</th><th>시가</th><th>저가</th><th>고가</th><th>종가</th><th>전일비</th><th>거래량</th>	
<%
		out.println("<tr><td colspan=\"7\" class=line></td></tr>");
		double diff;
		String sign;
		for (int i = 0; i < size - 1; i++) {
			Quote q = quoteList.getQuote(i);
			diff = q.getClose() - quoteList.getQuote(i + 1).getClose();
			if (diff > 0) {
				sign = "▲";
			} else if (diff == 0) {
				sign = "-";
			} else {
				diff = diff * -1;
				sign = "▼";
			}
			if ((i % 5 == 0) && i != 0) {
				out.println("<tr><td colspan=\"7\" class=line></td></tr>");
			}
			out.println("<tr><td>" + q.getTradeDate().toString() + "</td>"
					+ "<td>" + df.format(q.getOpen()) + "</td>" + "<td>"
					+ df.format(q.getLow()) + "</td>" + "<td>"
					+ df.format(q.getHigh()) + "</td>" + "<td>"
					+ df.format(q.getClose()) + "</td>");
			if (sign.equals("▼"))
				out.println("<td id=minus class=right>" + sign
						+ df.format(diff) + "&nbsp;&nbsp;</td>");
			else
				out.println("<td id=plus class=right>" + sign
						+ df.format(diff) + "&nbsp;&nbsp;</td>");

			out.println("<td class=right>" + dfVolume.format(q.getVolume())
					+ "&nbsp;&nbsp;</td>" + "</tr>");
		}
		out.println("<tr><td colspan=\"7\" class=line></td></tr>");
	%>		
</table>
<div id=page>
<%
	int temp = pageStart - 1;
	if (temp == 0)
		temp = 1;
	if (temp != 1) {
		out.println("<span><a href=/SearchQuote?type=" + type
				+ "&symbol=" + company.getSymbol() + "&page=" + temp
				+ "> ◀</a></span>");
	}
	for (int i = pageStart; i <= pageEnd; i++) {
		if (i == pageNum) {
			out.println("<span id=current><a href=/SearchQuote?type="
					+ type + "&symbol=" + company.getSymbol()
					+ "&page=" + i + ">" + i + "</a></span>");
		} else {
			out.println("<span ><a href=/SearchQuote?type=" + type
					+ "&symbol=" + company.getSymbol() + "&page=" + i
					+ ">" + i + "</a></span>");
		}
	}
	if (pageEnd < lastPage) {
		out.println("<span><a href=/SearchQuote?type=" + type
				+ "&symbol=" + company.getSymbol() + "&page="
				+ (pageEnd + 1) + "> ▶</a></span>");
	}
%>
</div>
</body>
</html>