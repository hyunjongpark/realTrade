<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" errorPage="ServerInternalError.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.vvip.quote.*"%>



<!DOCTYPE HTML>
<html>
<head>
<meta charset="euc-kr">
<link rel="stylesheet" type="text/css" href="css/default.css">
<link rel="stylesheet" type="text/css" href="css/search.css">

<%
	
	int i;
	String confirm = (String)session.getAttribute("Confirm");
	if ( confirm.equals("loginadminok") == false ) {		
		session.invalidate();
	//	throw new ServletException("잘못된 접근입니다.");
	}
	ArrayList <String> kor_list = (ArrayList<String>)session.getAttribute("KORLIST");
	ArrayList <String> usa_list = (ArrayList<String>)session.getAttribute("USALIST");
	ArrayList <String> quote_list = (ArrayList<String>)session.getAttribute("QUOTELIST");
	TradeDate end = new TradeDate();
	TradeDate start = end.previous(1100);
	session.invalidate();	
	
%>
<script type="text/javascript">
function isNumberKey()
{
	var code = window.event.keyCode; 

	 if ((code > 34 && code < 41) || (code > 47 && code < 58) || (code > 95 && code < 106) || code==110 || code == 8 || code == 9 || code == 13 || code == 46) 
	 { 
	  window.event.returnValue = true; 
	  return; 
	 } 
	 window.event.returnValue = false; 
}
</script>
</head>
<body>
	
			<form method="post"  action="UpdateQuote">
				종목 심볼 코드 : <input type="text" size=10 name=symbol> 
				<input type="submit" name="view" value="업데이트">
			</form>		
			
			
		
</body>
</html>