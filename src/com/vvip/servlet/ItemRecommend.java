package com.vvip.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vvip.ga.GeneResult;
import com.vvip.quote.Company;
import com.vvip.util.DatabaseManager;

/**
 * Servlet implementation class ItemRecommend
 */
public class ItemRecommend extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		try {
//			request.setCharacterEncoding("euc-kr");
//			String typeStr = request.getParameter("type");
//			int type;
//						
//			if ( typeStr == null )
//				type = 2;
//			else
//				type = Integer.valueOf(typeStr);
//			
//			ArrayList<GeneResult> grList = DatabaseManager.selectGeneResultListALLByIndex();
//			ArrayList<Company> companyList = new ArrayList<Company> ();
//			
//			for ( int i = 0; i < grList.size(); i++ ) {
//				
//				Company c = DatabaseManager.selectCompanyByIndex(grList.get(i).getSymbolIndex());
//	//			System.out.println("i : "+i+" getNameInEng() : "+c.getNameInEng()+" getStatus() : "+grList.get(i).getStatus());
//				companyList.add(c);
//			}
//			
//			HttpSession session = request.getSession();			
//	    	session.setAttribute("GeneList", grList);
//	    	session.setAttribute("CompanyList", companyList);
//	    	RequestDispatcher rd;
//	    	
//        	if ( type == 1 ) 
//        		rd = request.getRequestDispatcher("/WEB-INF/jsp/ItemRecommend.jsp");
//        	else
//        		rd = request.getRequestDispatcher("/WEB-INF/jsp/mobile/MobileItemRecommend.jsp");
//        	
//			rd.forward(request, response);
//		} catch ( Exception e) {
//			throw new ServletException(e.getMessage());
//		}
	}
}
