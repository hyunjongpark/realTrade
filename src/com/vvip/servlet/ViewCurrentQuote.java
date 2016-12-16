package com.vvip.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vvip.data.ImportQuote;
import com.vvip.quote.Quote;

/**
 * Servlet implementation class ViewCurrentQuote
 */
public class ViewCurrentQuote extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int count;
		int pCount = 7;

		String sNum = request.getParameter("snum");

//		if (sNum == null || sNum.equals("null")) {
//			RequestDispatcher rd = request
//					.getRequestDispatcher("/WEB-INF/jsp/mobile/MobileErrorPage.jsp");
//			rd.forward(request, response);
//		} else {
//			count = Integer.valueOf(sNum);
//			String symbolList[] = new String[count];
//			String patternList[] = new String[pCount];
//			String temp = null;
//
//			for (int i = 0; i < count; i++) {
//				temp = String.valueOf((char) ('a' + i));
//				symbolList[i] = request.getParameter(temp);
//			}
//
//			patternList[0] = request.getParameter("p1");
//			patternList[1] = request.getParameter("p2");
//			patternList[2] = request.getParameter("p3");
//			patternList[3] = request.getParameter("p4");
//			patternList[4] = request.getParameter("p5");
//			patternList[5] = request.getParameter("p6");
//			patternList[6] = request.getParameter("p7");
//
//			Integer pattern[] = new Integer[pCount];
//
//			for (int i = 0; i < pCount; i++)
//				pattern[i] = Integer.valueOf(patternList[i]);
//
//			Quote quoteList[] = new Quote[count];
//			for (int i = 0; i < count; i++)
//				quoteList[i] = ImportQuote.getNewQuoteDiff(symbolList[i], true);
//
//			String nameList[] = new String[count];
//			for (int i = 0; i < count; i++) {
//				nameList[i] = ImportQuote.getQuote(symbolList[i]);
//
//				HttpSession session = request.getSession();
//				session.setAttribute("Count", Integer.valueOf(count));
//				session.setAttribute("SymbolList", symbolList);
//				session.setAttribute("Pattern", pattern);
//				session.setAttribute("QuoteList", quoteList);
//				session.setAttribute("QuoteName", nameList);
//
//				RequestDispatcher rd = request
//						.getRequestDispatcher("/WEB-INF/jsp/mobile/ViewCurrentQuote.jsp");
//				rd.forward(request, response);
//			}
//		}
	}
}
