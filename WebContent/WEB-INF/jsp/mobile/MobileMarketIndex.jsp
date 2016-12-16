<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="com.vvip.quote.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
		String [] kospi = (String [])session.getAttribute("Kospi");
		String [] kosdaq = (String [])session.getAttribute("Kosdaq");
		String [] kospi200 = (String[] )session.getAttribute("Kospi200");
		String [] exchangeRate = (String [])session.getAttribute("ExchangeRate");
		String [] dow = (String [])session.getAttribute("Dow");
		String [] nasdaq = (String [])session.getAttribute("Nasdaq");
		String [] snp = (String [])session.getAttribute("Snp");
		
		session.invalidate();
		
		TradeDate date = new TradeDate(); 
				
		%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<script>
function init() {
    var ksp = document.getElementById("ksp");
    var ksd = document.getElementById("ksd");
    var ksp200 = document.getElementById("ksp200");
    var dow = document.getElementById("dow");
    var nsd = document.getElementById("nsd");
    var snp = document.getElementById("snp");
    var rate = document.getElementById("rate");
    
    if ( '<%= kospi[2].charAt(0) %>' == '-' )
        ksp.style.color="dodgerblue";    
    if ( '<%= kosdaq[2].charAt(0) %>' == '-' )
        ksd.style.color="dodgerblue";        
   if ( '<%= kospi200[2].charAt(0) %>' == '-' )
        ksp200.style.color="dodgerblue";     
    if ( '<%= dow[2].charAt(0) %>' == '-' )
        dow.style.color="dodgerblue"; 
    if ( '<%= nasdaq[2].charAt(0) %>' == '-' )
        nsd.style.color="dodgerblue";    
    if ( '<%= snp[2].charAt(0) %>' == '-' )
        snp.style.color="dodgerblue";
    if ( '<%= exchangeRate[2].charAt(0) %>' == '-' )
        rate.style.color="dodgerblue";       
}

</script>
</head>
<body onLoad="init();" bgcolor="white" >
 <font id="font">
     
<table width="100%">   
    <tr id="ksp" style="color:orangered" >
        <td><b>　코스피</b></td>        
        <td align="right"><b>　<%= kospi[0] %></b></td>        
        <td align="left"><b>　<%= kospi[1] %></b></td> 
    </tr>
    <tr id="ksd" style="color:orangered">
        <td><b>　코스닥</b></td>
        <td align="right"><b>　<%= kosdaq[0] %></b></td>
        <td align="left"> <b>　<%= kosdaq[1] %></b></td> 
    </tr>
    <tr id="ksp200" style="color:orangered">
        <td><b>　코스피200</b></td>
        <td align="right"> <b>　<%= kospi200[0] %></b></td> 
        <td align="left"> <b>　<%= kospi200[1] %></b></td>
    </tr>
    <tr  id="dow" style="color:orangered">
        <td><b>　다우지수</b> </td> 
        <td align="right"><b>　<%= dow[0] %></b></td> 
        <td align="left"><b>　<%= dow[1] %></b></td>
    </tr>
    <tr  id="nsd" style="color:orangered">
        <td><b>　나스닥</b></td> 
        <td align="right"><b>　<%= nasdaq[0] %></b></td> 
        <td align="left"><b>　<%= nasdaq[1] %></b></td> 
    </tr>
    <tr  id="snp" style="color:orangered">
        <td><b>　S&P500</b></td> 
        <td align="right"> <b>　<%= snp[0] %></b></td> 
        <td align="left"><b>　<%= snp[1] %></b></td>
        </tr>
    <tr  id="rate" style="color:orangered">
        <td><b>　원/달러</b></td> 
        <td align="right"><b>　<%= exchangeRate[0] %></b></td> 
        <td align="left"><b>　<%= exchangeRate[1] %></b></td> 
    </tr>
    <tr style="color:black">
        <td colspan=3 align="right"> <%= date.toString() %>　</td>
       </tr>    
</table>
</font>
</body>
</html>