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
	<!-- ���ٴϴ� �޴� -->
	<div id="floatdiv">
		<ul>
			<li><a href="introduce.html">����Ʈ����</a></li>
			<li><a href="pattern.html">ĵ����Ʈ����</a></li>
			<li><a href="indicator.html">��ǥ����</a></li>
			<li><a href="ItemRecommend?type=1">������õ</a></li>
			<li><a href="aboutUS.html">About US</a></li>
			<li style="font-weight: bold"><a href="#">��������</a></li>
		</ul>
	</div>

	<div id="wrap">
		<header>
			<!-- �ΰ� �� �� -->
			<div id="logo">
				<a href="/"><img src="images/stock_vvip.png" alt="imsi_logo"
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
           

		<article>	
		<div style="font-size:12pt">	
		���� : <%= ( (company.getNameInEng() != null) ? ( company.getNameInEng() ) : ( company.getNameInEng() ) ) %> - <%= company.getSymbol() %><br>
		������ �� <%= parameter.get(2) %> �ִ���ȭ�� <%= parameter.get(3) %> 
		����Ȯ�� <%= parameter.get(4) %>% ����Ȯ�� <%= parameter.get(5) %>% <br>
		�н� �Ⱓ <%= parameter.get(0)  %> ~  <%= parameter.get(1) %><br>
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