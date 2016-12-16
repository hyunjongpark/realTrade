package com.vvip.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vvip.ga.Gene;
import com.vvip.ga.GeneResult;
import com.vvip.ga.GeneticAlgorithm;
import com.vvip.quote.Company;
import com.vvip.quote.Market;
import com.vvip.quote.QuoteList;
import com.vvip.util.DatabaseManager;

/**
 * Servlet implementation class UpdateGA
 */
public class UpdateGA extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateGA() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doPost : UpdateGA");
		String symbol = request.getParameter("symbol2");
		String startStr = request.getParameter("start");
		String endStr = request.getParameter("end");
		String popStr = request.getParameter("population");
		String geStr = request.getParameter("generation");
		String croStr = request.getParameter("crossover");
		String muStr = request.getParameter("mutate");
		
		if ( startStr == null || endStr == null || popStr == null || geStr == null || croStr == null || muStr == null ) 
			throw new ServletException("������ ������ ������.");
		
		int start = Integer.valueOf(startStr);
		int end = Integer.valueOf(endStr);
		int population = Integer.valueOf(popStr);
		int generation = Integer.valueOf(geStr);
		double crossover = Double.valueOf((double)Integer.valueOf(croStr)/100);
		double mutation = Double.valueOf((double)Integer.valueOf(muStr)/100);
		
		
		if ( start < 19000000 )
			throw new ServletException("������ ���� ����������.");
		if ( end < 19000000 )
			throw new ServletException("������ ���� ����������.");
		if ( population > 2000 || population < 1 ) 
			throw new ServletException("�������� ���� ������ ��������.");
		if ( generation > 2000 || generation < 1 ) 
			throw new ServletException("�������� ���� ������ ��������.");
		if ( crossover > 1 || crossover < 0 ) 
			throw new ServletException("���������� ���� ������ ��������.");
		if ( mutation > 1 || mutation < 0 ) 
			throw new ServletException("���������� ���� ������ ��������.");
		
//		try {
//			// ������ ���� ������ ��������.
//			Company company = DatabaseManager.selectCompanyBySymbol(symbol);
//			
//			if (company == null)
//				throw new ServletException("�������� ���� �������� ������.");
//			
//			// ������ ������ ��������.
//			Market market = DatabaseManager.selectMarketByIndex(company
//					.getMarketIndex());
//			// ������ ������ ������ ���� ����
//			if (market == null)
//				throw new ServletException("�������� ���� ���� ������ ������.");
//			
//			// ���� ������ �������� ���� ��������
//			int symbolIndex = company.getSymbolIndex();
//			int marketIndex = company.getMarketIndex();
//			int hash = market.getHash();
//			int hashNum;
//			if ( hash == 0 )
//				hashNum = 0;
//			else
//				hashNum = symbolIndex % market.getHash();
//			int indexNumber;
//			// ���� ���� ������
//			Gene bestGA = new Gene(1, 1, 1, 1, 1, 1);
//			
//			GeneResult gene = DatabaseManager.getGeneBySymbol(symbolIndex);
//			if ( gene == null )
//				throw new ServletException("�������� �� GA ������ ��������.");
//
//			QuoteList quoteList = DatabaseManager.selectQuoteListByDate(symbolIndex, marketIndex, hashNum, start, end);
//			
//			if ( quoteList.getSize() < GeneticAlgorithm.START_INDEX )
//				throw new ServletException("������ ���� ��������.");
//			
//			GeneticAlgorithm ga = new GeneticAlgorithm(population, generation, crossover, mutation, quoteList);
//			//20�� �������� ���� ���� �������� ������.
//			for ( int i = 0; i < 20; i++ ) {
//				Gene best = ga.run();
//				if ( bestGA.getProfit() < best.getProfit() )
//					bestGA = best;
//			}
//			double profit = ga.calculateProfit(bestGA.getShortMA(), bestGA.getLongMA(), bestGA.getMacdMA(), bestGA.getFastK(), bestGA.getSlowK(), bestGA.getSlowD());
//			profit = ga.profitToPercentage(profit);
//			bestGA.setProfit(profit);
//			if ( symbolIndex < 1800 )
//				indexNumber = 1;
//			else 
//				indexNumber = 2;
//			GeneResult gr = new GeneResult(symbolIndex, bestGA, ga.getLastStatus(), indexNumber, 
//					quoteList.getQuote(GeneticAlgorithm.START_INDEX).getTradeDate(), quoteList.getLastDate());
//			
//			//DatabaseManager.updateGeneBySymbol(symbolIndex, gr);
//			
//			ArrayList<String> parameter = new ArrayList<String> ();
//			parameter.add(quoteList.getQuote(GeneticAlgorithm.START_INDEX).getTradeDate().toString());
//			parameter.add(quoteList.getLastDate().toString());
//			parameter.add(popStr);
//			parameter.add(geStr);
//			parameter.add(String.valueOf(crossover));
//			parameter.add(String.valueOf(mutation));
//			HttpSession session = request.getSession();
//			session.setAttribute("Parameter", parameter);
//    		session.setAttribute("GeneResult", gr);
//        	session.setAttribute("Company", company);
//        	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/ViewUpdateGA.jsp");
//    		rd.forward(request, response);
//        	
//		} catch ( Exception e ) {
//			throw new ServletException(e.getMessage()); 
//		}
//		finally {
//		}
		
	}

}
