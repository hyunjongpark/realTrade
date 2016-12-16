package com.vvip.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vvip.data.ImportDaumMarketIndex;
import com.vvip.data.ImportException;

public class MobileMarketIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("euc-kr");    	
    	response.setContentType("text/html; charset=euc-kr");
    	ArrayList<String> indexList = null;
		try {
			//Index 데이터를 파싱해 온다.
			indexList = ImportDaumMarketIndex.importData();
			
			//세션을 통해 JSP페이지 데이터 전송
			HttpSession session = request.getSession();
			session.setAttribute("Kospi", indexList.get(0).split("##"));
			session.setAttribute("Kosdaq", indexList.get(1).split("##"));
			session.setAttribute("Kospi200", indexList.get(2).split("##"));
			session.setAttribute("ExchangeRate", indexList.get(3).split("##"));
			session.setAttribute("Dow", indexList.get(4).split("##"));
			session.setAttribute("Nasdaq", indexList.get(5).split("##"));
			session.setAttribute("Snp", indexList.get(6).split("##"));
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/mobile/MobileMarketIndex.jsp");
		    rd.forward(request, response);
		    
		} catch (ImportException e) {
			
		}	
	}
	
}