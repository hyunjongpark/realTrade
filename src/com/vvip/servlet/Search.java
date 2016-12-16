package com.vvip.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vvip.data.ImportException;
import com.vvip.data.ImportQuote;
import com.vvip.quote.Company;
import com.vvip.quote.Quote;
import com.vvip.util.DatabaseManager;

public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("euc-kr");
		ArrayList<Company> companyList = null;
		
		String pageStr = request.getParameter("page");
		String marketName = request.getParameter("market");		
    	String search = request.getParameter("search");  
    	int page;
    	response.setContentType("text/html; charset=euc-kr");
    	if (search == null || search.length()< 2)
			throw new ServletException("search == null || search.length()< 2");
    	
    	search = search.replaceAll("`", "");
    	search = search.replaceAll("\"", "");
    	search = search.replaceAll("\'", "");
    	
    	if ( pageStr == null )
    		page = 1;
    	else
    		page = Integer.valueOf(pageStr);
    			
    	if ( page < 1)
    			page = 1;
    	
    	String [] splitSearch = search.split(" ");

    	int index = 0;
		int size = splitSearch.length;
		
    	int marketIndex;
    	    	
//    	try {
//    		if ( marketName.equals("ALL") )
//    			marketIndex = 0;
//    		else
//    			marketIndex = DatabaseManager.getMarketIndex(marketName);
//    		
//    		while ( companyList == null && index < size ) {
//    			companyList = DatabaseManager.searchCompanyByInput(splitSearch[index++], marketIndex);
//    		}
//    		    		
//    		for ( int i = index; i < size; i++ ) {
//    			splitSearch[i] = splitSearch[i].toUpperCase();
//    			for ( int j = companyList.size()-1; j >= 0; j-- ) {
//    				Company c = companyList.get(j);    				
//    				if ( c.getNameInKor() != null ) {
//    					if ( c.getSymbol().toUpperCase().indexOf(splitSearch[i]) == -1  
//    							&& c.getNameInEng().toUpperCase().indexOf(splitSearch[i]) == -1 
//    							&& c.getNameInKor().indexOf(splitSearch[i]) == -1 ) {
//    						companyList.remove(j);
//    					}
//    				} else {
//    					if ( c.getSymbol().toUpperCase().indexOf(splitSearch[i]) == -1  
//    							&& c.getNameInEng().toUpperCase().indexOf(splitSearch[i]) == -1 ) {    					
//    						companyList.remove(j);
//    					}
//    				}
//    			}	
//    		}       			
//    		if ( companyList == null || companyList.size() == 0 )
//    			throw new ServletException("companyList == null || companyList.size() == 0");
//    	} catch ( SQLException e) {
//    		throw new ServletException(e.getMessage());
//    	}
    	if ( companyList.size() == 1) {    	    		
        	RequestDispatcher rd = request.getRequestDispatcher("/SearchQuote?type=1&symbol=" + companyList.get(0).getSymbol());
        	rd.forward(request, response);   	
    	}
    	else {
    		ArrayList<Quote> quotes = new ArrayList<Quote> ();
    		Collections.sort(companyList);    		
    		
    		if ( page > companyList.size()/20 + 1) {
    			page = companyList.size()/20+1;
    		}
    		for ( int i = ((page-1)*20); i < (page*20) && i < companyList.size(); i++ ) {
    			Quote q = ImportQuote.getNewQuoteDiff(companyList.get(i).getSymbol(),false);
    			if(null != q ){
    				quotes.add(q);
    			}
    		}
    		HttpSession session = request.getSession();
        	session.setAttribute("CompanyList", companyList);
        	session.setAttribute("Quotes", quotes);
        	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/SearchResult.jsp");
        	rd.forward(request, response);   	
    	}
	}
}
