package com.vvip.test;

import com.vvip.quote.QuoteList;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

public class TestTechnicalAnalysis {
	public static void main(String[] args) {
		
		
		DatabaseManager.getConnection();
		QuoteList quoteList = DatabaseManager.selectQuoteListByDate("090370", 20140101, 20150501);
		
		
		TechnicalAnalysis ta = new TechnicalAnalysis(quoteList.getList(), 0);		
	
		double [] ret = new double [quoteList.getSize()];
		double [] ret_bbands = new double [quoteList.getSize()];
		double [] ret_bbands_up = new double [quoteList.getSize()];
		double [] ret_bbands_dn = new double [quoteList.getSize()];
		double [] ret_sar = new double [quoteList.getSize()];
		ta.bollingerBend(20, ret_bbands_up, ret_bbands, ret_bbands_dn);
//		for ( int i = quoteList.getSize()-40; i < quoteList.getSize(); i++)  {
		for ( int i = 0; i < quoteList.getSize(); i++)  {
			System.out.print(quoteList.getQuote(i).getTradeDate());
			System.out.print("\t"+quoteList.getQuote(i).getClose());
			System.out.print("\t"+ret[i]);
			System.out.print("\t"+ret_bbands_up[i]);
			System.out.print("\t"+ret_bbands[i]);
			System.out.print("\t"+ret_bbands_dn[i]);
			System.out.println("\t"+ret_sar[i]);
		}
		
		
		
	}
}
