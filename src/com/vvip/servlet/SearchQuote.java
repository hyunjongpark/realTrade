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
import com.vvip.quote.Quote;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.util.DatabaseManager;

/**
 * ������ ���� ���� �������� ���� JSP �������� ��������.
 */
public class SearchQuote extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("euc-kr");
		String pageStr = request.getParameter("page");
		String typeStr = request.getParameter("type");
		String symbol = request.getParameter("symbol");
		int page, type;
		
		//������ ���� ��
		if ( symbol == null)
			throw new ServletException("������ ���� ������.");
		
		//������ ���� �� 2�� ����
		if ( typeStr == null )
			type = 2;
		else
			type = Integer.valueOf(typeStr);
				
		if ( pageStr == null )
			page = 1;
		else
			page = Integer.valueOf(pageStr);
		
		if ( page < 1)
			page = 1;
		
    	response.setContentType("text/html; charset=euc-kr");
    	
//    	try {    		
//    		QuoteList quoteList = null;
//    		QuoteList newList = null;
//    		Quote newQuote;
//    		Quote preQuote;
//    		
//    		Company company = DatabaseManager.selectCompanyBySymbol(symbol);
//    		System.out.println("company : "+company.getNameInEng()+" symbol : "+symbol);
//    		if ( company == null )
//    			throw new ServletException("�������� ���� �������� ������.");
//    		Market market = DatabaseManager.selectMarketByIndex(company.getMarketIndex());
//    		if ( market == null )
//    			throw new ServletException("�������� ���� ���� ������ ������.");
//    		
//    		int lastPage;
//    		int count;
//    		int symbolIndex = company.getSymbolIndex();
//    		
//    		GeneResult gr = DatabaseManager.getGeneBySymbol(symbolIndex);	
//    		
//    		int marketIndex = company.getMarketIndex();
//    		int hash = market.getHash();
//    		if ( hash == 0)
//    			hash = symbolIndex;
//    
//    		System.out.println("symbolIndex : "+symbolIndex+" marketIndex : "+marketIndex+" hash : "+hash);
//    		
//    		count = DatabaseManager.getQuoteCount(symbolIndex, marketIndex, symbolIndex%hash);
//			lastPage = count/20;
//    		if (page > lastPage )
//    			page = lastPage;
//    		
//    		if ( type == 1 )
//    			DatabaseManager.updateCompanySearchBySymbol(company.getSymbol());
//    		    		
//    		quoteList = ImportQuote.getQuoteList(market, company, (page-1)*20, 21, false, false);
//    		
//			newList = ImportQuote.getQuoteList(market, company, 0, 2, true, true);
//			newQuote = newList.getQuote(newList.getSize()-1);
//			preQuote = newList.getQuote(newList.getSize()-2);
//    		
//    		if ( quoteList == null || newQuote == null || preQuote == null ) {
//    			throw new ServletException("�������� ���� ������ ������.");
//    		}        	
//    		
//    		HttpSession session = request.getSession();
//    		session.setAttribute("LastPage", lastPage);
//        	session.setAttribute("Company", company);
//        	session.setAttribute("Market", market);      
//        	session.setAttribute("GeneResult", gr);
//        	session.setAttribute("QuoteList", quoteList);
//        	session.setAttribute("NewQuote", newQuote);
//        	session.setAttribute("PreQuote", preQuote);
//        	 
//        	if ( type == 3 ) {
//        		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/mobile/MobileSearchQuote.jsp");
//        		rd.forward(request, response);
//        	} else {
//        		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/SearchQuote.jsp");
//        		rd.forward(request, response);
//        	}
//    		
//    	} catch ( SQLException e) {
//    		throw new ServletException(e.getMessage());
//    	} 
	}
}
