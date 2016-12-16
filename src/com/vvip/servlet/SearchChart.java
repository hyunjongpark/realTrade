package com.vvip.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vvip.data.ImportDaumQuote;
import com.vvip.data.ImportException;
import com.vvip.data.ImportQuote;
import com.vvip.ga.GeneResult;
import com.vvip.quote.Company;
import com.vvip.quote.Market;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

/**
 * ������ ���� ���� �������� ���� JSP �������� ��������.
 */
public class SearchChart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("euc-kr");
			
		String typeStr = request.getParameter("type");
		String symbol = request.getParameter("symbol");
		int type;
		
		if ( symbol == null)
			throw new ServletException("������ ���� ������.");
		
		if ( typeStr == null )
			type = 1;
		else
			type = Integer.valueOf(typeStr);
		
		response.setContentType("text/html; charset=euc-kr");

//		try {
//			QuoteList quoteList = null;
//		
//			Company company = DatabaseManager.selectCompanyBySymbol(symbol);
//			if (company == null)
//				throw new ServletException("�������� ���� ���� ������.");
//
//			Market market = DatabaseManager.selectMarketByIndex(company.getMarketIndex());
//			
//			if (market == null)
//				throw new ServletException("�������� ���� ������ ������.");
//						
//			GeneResult gr = DatabaseManager.getGeneBySymbol(company.getSymbolIndex());		
//			if ( type >= 3) {
//				 quoteList = ImportQuote.getQuoteList(market, company, 0, 1250, true, true);				
//			}
//			else {
//				quoteList = ImportQuote.getQuoteList(market, company, 0, 2500, true, true);			
//				
//			}		
//			
//			HttpSession session = request.getSession();			
//	    	session.setAttribute("Company", company);
//        	session.setAttribute("Market", market);
//        	session.setAttribute("QuoteList", quoteList);
//        	session.setAttribute("GeneResult", gr);
//        	
//		  	if ( type >= 3 ) {
//        		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/mobile/MobileSearchChart.jsp");
//        		rd.forward(request, response);
//        	} else {
//        		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/SearchChart.jsp");
//        		rd.forward(request, response);
//        	}
//			
//		} catch (SQLException e) {
//			throw new ServletException(e.getMessage());
//		} 
	}

}
