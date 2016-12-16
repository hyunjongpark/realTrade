package com.vvip.servlet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vvip.data.ImportDaumQuote;
import com.vvip.data.ImportException;
import com.vvip.data.ImportPaxnetIndex;
import com.vvip.data.ImportYahooQuote;
import com.vvip.init.ServerManager;
import com.vvip.quote.Company;
import com.vvip.quote.Market;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

/**
 * Servlet implementation class UpdateQuote
 */
public class UpdateQuote extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateQuote() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		throw new ServletException("������ ���� ������.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doPost : UpdateQuote");
		String symbol = request.getParameter("symbol");
		
		
		ArrayList<String> logList = new ArrayList<String> ();
		logList.add((new Date()).toString());
		
		DatabaseManager.getConnection();
//		try {
//			int fail = 0, dbSize;
//			int symbolIndex, marketIndex, hash, hashNum;
//			Calendar cal = Calendar.getInstance();
//			TradeDate today = new TradeDate();
//			String dateString = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
//			
//			//�������� ���� ���� ����
//			logList.add("Update : " + symbol);
//			
//			
//			System.out.print("Update : " + symbol+"\n");
//			
//			QuoteList newList = null;
//			QuoteList quoteList = null;
//			TechnicalAnalysis ta = null;
//			TradeDate filterDate = null;
//			
//			Company company = DatabaseManager.selectCompanyBySymbol(symbol);	
//			
//			System.out.print(company);
//			System.out.print("ddddddd  "+company.getMarketIndex()+"  ddddddd\n" );
//			
//			// ������ �������� ������ ���� ����
//			if ( company.getMarketIndex() < 3 && today.isWeekend() == false
//					&& 8 < cal.get(Calendar.HOUR_OF_DAY)
//					&& cal.get(Calendar.HOUR_OF_DAY) < 19) { 
//				throw new ServletException("���� ���������� �������� ����������.");			
//			}
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			// ������ ������ ��������.
//			Market market = DatabaseManager.selectMarketByIndex(company.getMarketIndex());
//			
//			
//			
//			// ������ ������ ������ ���� ����
//			if (market == null)
//				throw new ServletException("�������� ���� ���� ������ ������.");
//			
//
//			
//			symbolIndex = company.getSymbolIndex();
//			//���� ������
//			if ( symbolIndex == -1 )
//				throw new ServletException("Symbol Index is not exist.");
//			
//			marketIndex = company.getMarketIndex();
//			
//			if ( marketIndex == -1 )
//				throw new ServletException("Market Index is not exist.");
//			
//			hash = market.getHash();
//			if ( hash == 0 )
//				hashNum = 0;
//			else
//				hashNum = symbolIndex % hash;			
//			
//		
//
//			
//			System.out.print(symbolIndex+" "+marketIndex+" "+hashNum+"\n");
//			
//			//������ �������� ���� ���� 50���� ��������. �������� ���� ������ ��������.
//			quoteList = DatabaseManager.selectLimitQuoteListBySymbol(symbolIndex, marketIndex, hashNum, 0, 50, true);
//			
//			
//			if (true)
//				throw new ServletException("-------- 3\n");
//			
//			//������ ���� ���� ����
//			dbSize = quoteList.getSize();
//			
//			if (true)
//				throw new ServletException("-------- 4\n");
//			
//			//������ �������� ������ ����		
//			filterDate = quoteList.getQuote(dbSize-1).getTradeDate();
//			
//			//�� ���� �������� ������ ��������.
//			if ( company.getMarketIndex() < 3 )
//				newList = ImportDaumQuote.importData(company.getSymbol(), symbolIndex, marketIndex , filterDate);
//			else if ( company.getMarketIndex() < 7) {
//				if ( company.getSymbol().toLowerCase().contains("kos") == true )
//					newList = ImportPaxnetIndex.importData(company.getSymbol(), symbolIndex, marketIndex , filterDate);
//				else
//					newList = ImportYahooQuote.importData(company.getSymbol(), symbolIndex, marketIndex , filterDate);
//			}
//			
//			//������ �������� ��������
//			if (newList != null) {
//				//������ ���� ������ �������������� �������� ��������.
//				for (int j = 0; j < newList.getSize(); j++) {
//					quoteList.addQuote(newList.getQuote(j));
//				}
//				
//				//TA������ �������� ��������.
//				ta = new TechnicalAnalysis(quoteList.getList(), dbSize);
//				ta.setTaPattern();
//
//				//������ �������� ������ ������ ��������.
//				for (int k = dbSize; k < quoteList.getSize(); k++) {
//					DatabaseManager.insertQuoteToSymbol(quoteList.getQuote(k), symbolIndex, marketIndex, hashNum);	
//					logList.add(quoteList.getQuote(k).toString());
//				}
//			} else {
//				logList.add(symbol + " is already new");
//			}
//			
//			
//			
//			
//			
//			FileWriter writer = null;
//			//���� ������ ����
//			if ( company.getMarketIndex() < 3 ) {
//				writer = new FileWriter(VVIPManager.getVVIP_PATH()+"/log/KOREA/KOR" + dateString + ".txt", true);
//			} else if ( company.getMarketIndex() < 7) {
//				if ( company.getSymbol().toLowerCase().contains("kos") == true )
//					writer = new FileWriter(VVIPManager.getVVIP_PATH()+"/log/KOREA/KOR" + dateString + ".txt", true);
//				else
//					writer = new FileWriter(VVIPManager.getVVIP_PATH()+"/log/USA/USA" + dateString + ".txt", true);
//			} else {
//				throw new ServletException("�������� ���� ����������.");
//			}
//			logList.add((new Date()).toString());
//			BufferedWriter bwriter = new BufferedWriter(writer);			
//			for ( int i = 0; i < logList.size(); i++ ) {
//				bwriter.write(logList.get(i));
//				bwriter.newLine();
//			}
//			bwriter.close();
//			writer.close();
//			
//			//���� ������
//			HttpSession session = request.getSession();
//	    	session.setAttribute("LogList", logList);
//	     	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/ViewLog.jsp");
//	    	rd.forward(request, response);   	
//
//		} catch ( SQLException e ) {
//			throw new ServletException(e.getMessage());
//		} catch ( ImportException e ) {
//			throw new ServletException(e.getMessage());
//		} 
	}

}
