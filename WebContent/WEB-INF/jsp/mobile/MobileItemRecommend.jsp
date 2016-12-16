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
<link rel="stylesheet" type="text/css" href="css/mobileRecommend.css">

<%
	ArrayList<GeneResult> grList = (ArrayList<GeneResult>)session.getAttribute("GeneList");
	ArrayList<Company> companyList = (ArrayList<Company>)session.getAttribute("CompanyList");	
	session.invalidate();
%>
<body>
<div id=recommend>		
		&nbsp;* &nbsp; Ư�� �Ⱓ ���� �м��� �˰����� ���� ������ �ż� / �ŵ� ������ �˷��ݴϴ�.
		</div>
		<div id=buy>
		�ż��� ��õ�ϴ� ����
		</div>
		<div>
		<table id = item>
		<th>����</th><th>������</th><th>�Ⱓ</th>
		<%
			for ( int i = 0; i < grList.size(); i++ ) {
				if ( grList.get(i).getStatus() == 1  && grList.get(i).getGene().getProfit() > 0 ) {
					String name = ( (companyList.get(i).getNameInKor() != null) ? ( companyList.get(i).getNameInKor() ) : ( companyList.get(i).getNameInEng() ) );
					out.println("<tr><td>");
					out.println(name + "</td>");
					out.println("<td>" + grList.get(i).getGene().getProfit() + "%</td>");
					out.println("<td>" + grList.get(i).getStart() + " ~ " + grList.get(i).getEnd() + "</td>");
					out.println("</td>");
				}
			}
		%>
		</table>
		</div>
		<br>
		<div id=sell>
		�ŵ��� ��õ�ϴ� ����
		</div>
		<div>
		<table id = item>
		<th>����</th><th>������</th><th>�Ⱓ</th>
			<%
			for ( int i = 0; i < grList.size(); i++ ) {
				if ( grList.get(i).getStatus() == 2  && grList.get(i).getGene().getProfit() > 0 ) {
					String name = ( (companyList.get(i).getNameInKor() != null) ? ( companyList.get(i).getNameInKor() ) : ( companyList.get(i).getNameInEng() ) );
					out.println("<tr><td>");
					out.println(name + "</td>");
					out.println("<td>" + grList.get(i).getGene().getProfit() + "%</td>");
					out.println("<td>" + grList.get(i).getStart() + " ~ " + grList.get(i).getEnd() + "</td>");
					out.println("</td>");
				}
			}
		%>
		</table>
		</div>
		<div class="clear"></div>
</body>
</head>