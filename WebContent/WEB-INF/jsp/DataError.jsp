<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"  isErrorPage="true" %>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="euc-kr">
<link rel="stylesheet" type="text/css" href="css/default.css">
<link rel="stylesheet" type="text/css" href="css/search.css">

<%
	response.setStatus(200);
%>

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
           
		<!-------Error  ------->
		<article>
			<div id="errorMessage">
				�߸��� �����Ͱ� �Է� �Ǿ����ϴ�.
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