package com.vvip.data;

import java.util.Calendar;

import com.vvip.init.VVIPManager;
import com.vvip.quote.Company;
import com.vvip.quote.Market;
import com.vvip.quote.Quote;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

public class ImportQuote {
	public static String getQuote(String symbol){
		Company c = null;
		String name = null;
		try{
			c = VVIPManager.getCompany(symbol);
			name =( c.getNameInKor() != null) ? ( c.getNameInKor() ) : ( c.getNameInEng() );
			
		}catch(Exception e){
		}
		return name;
	}

	public static Quote getNewQuoteDiff(String symbol, boolean pattern) {
		Quote q = null;
		Company company = VVIPManager.getCompany(symbol);
		q = getNewQuoteDiff(company, pattern);
		return q;
	}
	
	public static Quote getNewQuoteDiff(Company c, boolean pattern) {
		Quote quote = null;
		QuoteList quoteList = null;
		if ( pattern == true ) 
			quoteList = getQuoteList(null, c, 0, 62, true, true);
		else
			quoteList = getQuoteList(null, c, 0, 2, true, true);
		if(null != quoteList){
		
		quote = quoteList.getQuote(quoteList.getSize()-1);
		
			quote.setLow(0);
		
		quote.setHigh(quoteList.getQuote(quoteList.getSize()-2).getClose());		
		}
		return quote;						
	}
	
	public static QuoteList getQuoteList(Market m, Company c, int start, int count, boolean sort, boolean update) {		
		QuoteList quoteList = null;		
		
		
		try {
		//	if ( m == null )
		//		m = DatabaseManager.selectMarketByIndex(c.getMarketIndex());
			
			//int sIndex = c.getSymbolIndex();
			//int mIndex = m.getMarketIndex();
			//int hash = m.getHash();
			//int hashNum = ((hash == 0) ? 0 : (sIndex%hash));
			quoteList = DatabaseManager.selectLimitQuoteListBySymbol(c.getSymbol(), start, count, sort);
	
			if(null == quoteList){
				return null;
			}
//			System.out.println("quoteList : "+quoteList+" m : "+m+" c : "+c+" start :"+start+" count :"+count+" sort : "+sort+" update : "+update);
//			System.out.println("sIndex : "+sIndex+" sIndex : "+m+" mIndex : "+mIndex+" hashNum :"+hashNum+" count :"+count+" sort : "+sort);
			
				
			if ( update ) {				
					TradeDate today = new TradeDate();
					Calendar now = Calendar.getInstance();				
					if (today.isWeekend() == false && 8 < now.get(Calendar.HOUR_OF_DAY) 	&& now.get(Calendar.HOUR_OF_DAY) < 19) {
						QuoteList newList = null;
						
						int preSize = quoteList.getSize();
						newList = ImportDaumQuote.importData(c.getSymbol(), quoteList.getLastDate());
						if (newList != null) {
							for (int j = 0; j < newList.getSize(); j++) {
								quoteList.addQuote(newList.getQuote(j));
							}
							if ( quoteList.getSize() > TechnicalAnalysis.DEFAULT_DAY ) {
								TechnicalAnalysis ta = new TechnicalAnalysis(quoteList.getList(), preSize);
								ta.setTaPattern();
							}
							
						}
					}
				}
		}  catch ( ImportException e ) {
		}		
		return quoteList;
	}
}
