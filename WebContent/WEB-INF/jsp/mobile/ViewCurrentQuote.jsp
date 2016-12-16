<?xml version="1.0" encoding="EUC-KR"?>
<%@ page language="java" contentType="text/xml; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="com.vvip.quote.*"%>    
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%
	DecimalFormat dfReal = new DecimalFormat("###,###,##0.00");
	DecimalFormat dfNum = new DecimalFormat("###,###,###");
	
	String [] symbolList = (String []) session.getAttribute("SymbolList");
	Integer [] pattern = (Integer [])session.getAttribute("Pattern");
	Quote [] quoteList = (Quote []) session.getAttribute("QuoteList");
	session.invalidate();	
%>
<vvip>
<%
	for ( int i = 0; i < quoteList.length; i++ ) {
		out.println("<subject symbol=\"" + symbolList[i] + "\">");
		out.println("<realPrice>");
		double diff = quoteList[i].getClose() - quoteList[i].getHigh();
		if ( quoteList[i].getLow() == 2 ) {
			out.println(dfReal.format(quoteList[i].getClose()));
			out.println("</realPrice>");
			if ( diff >= 0 ) {
				out.println("<fluctuatePrice isUp=\"yes\">");
			} else {
				out.println("<fluctuatePrice isUp=\"no\">");
				diff *= -1;
			}
			out.println(dfReal.format(diff));
			out.println("</fluctuatePrice>");
		}
		else {			
			out.println(dfNum.format(quoteList[i].getClose()));
			out.println("</realPrice>");
			if ( diff >= 0 ) {
				out.println("<fluctuatePrice isUp=\"yes\">");
			} else {
				out.println("<fluctuatePrice isUp=\"no\">");
				diff *= -1;
			}
			out.println(dfNum.format(diff));
			out.println("</fluctuatePrice>");
		}
		out.println("<fluctuatePercent>" + dfReal.format(diff/quoteList[i].getHigh()*100) + "</fluctuatePercent>");
		out.println("<volume>" + dfNum.format(quoteList[i].getVolume()) + "</volume>");
		for ( int j = 0; j < pattern.length; j++ ) {
			out.println("<pattern>");
			String pt = quoteList[i].getPattern();			
			if ( pt.charAt(pattern[j]) != 'Z' )
				out.println("1");
			else
				out.println("0");			
			out.println("</pattern>");
		}
		out.println("</subject>");
	}
%>	
</vvip>