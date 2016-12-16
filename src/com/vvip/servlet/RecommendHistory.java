package com.vvip.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vvip.ga.GeneResult;
import com.vvip.ga.GeneticAlgorithm;
import com.vvip.quote.Company;
import com.vvip.quote.Market;
import com.vvip.quote.QuoteList;
import com.vvip.util.DatabaseManager;

/**
 * Servlet implementation class RecommendHistory
 */
public class RecommendHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("euc-kr");
		String typeStr = request.getParameter("type");
		String symbol = request.getParameter("symbol");
		int type;
					
		if ( typeStr == null )
			type = 2;
		else
			type = Integer.valueOf(typeStr);
		
//		try {
//			QuoteList quoteList = null;
//		
//			System.out.println("dddddd");
//			Company company = DatabaseManager.selectCompanyBySymbol(symbol);
//			if (company == null)
//				throw new ServletException("�������� ���� ���� ������.");
//
//			Market market = DatabaseManager.selectMarketByIndex(company.getMarketIndex());
//			
//			if ( market == null )				
//				throw new ServletException("�������� ��������.");
//			
//			int symbolIndex = company.getSymbolIndex();
//			int marketIndex = company.getMarketIndex();
//			int hashNum = symbolIndex % market.getHash();
//			GeneResult gr = DatabaseManager.getGeneBySymbol(symbolIndex);
//			
//			if ( gr == null  ) 
//				throw new ServletException("�������� ��������.");
//			
//			quoteList= DatabaseManager.selectQuoteListByDate(symbolIndex, marketIndex, hashNum, gr.getStart().previous(GeneticAlgorithm.START_INDEX).toInt(), 99999999);
//			
//			
//			HttpSession session = request.getSession();			
//	    	session.setAttribute("Company", company);
//        	session.setAttribute("Market", market);
//        	session.setAttribute("QuoteList", quoteList);
//        	session.setAttribute("GeneResult", gr);
//        	
//        	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/RecommendHistory.jsp");
//    		rd.forward(request, response);
//			
//		} catch ( Exception e) {
//			throw new ServletException(e.getMessage());
//		}
	}

}
