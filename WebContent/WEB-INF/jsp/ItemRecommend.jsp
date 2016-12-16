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


		<!-------Recommend ------->
		<article>
			<div id=recommend>
				<div class="introduce">* &nbsp; ������ ���� ���� �����˰����� �̿��� Ư�� �Ⱓ ���� ���� ���� 
				���ͷ��� ���<br> Slow Stoch�� MACD�� ���Ͽ� ������ �ż� / �ŵ� ������ �˷��ݴϴ�. 
				</div>
				<div class=buy>�ż��� ��õ�ϴ� ����</div>
				<table class=item>
					<tr>
						<td class="tag">����</td>
						<td class="tag">������</td>
						<td class="tag">�Ⱓ</td>
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
				<div class=sell>�ŵ��� ��õ�ϴ� ����</div>
				<table class=item>
					<tr>
						<td class="tag">����</td>
						<td class="tag">������</td>
						<td class="tag">�Ⱓ</td>
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


