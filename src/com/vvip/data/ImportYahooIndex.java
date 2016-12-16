package com.vvip.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.vvip.init.VVIPManager;
import com.vvip.quote.Quote;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.quote.TradeDateFormatException;

public class ImportYahooIndex {
	private final static String SYMBOL = "_SYM_";
	private final static String OFFSET = "_OFT_";
	private final static String YAHOO_PATTERN = "?offset=" + OFFSET + "&symbol=" +SYMBOL + "&type=5&historytype=D0#stHistory";
	private final static String YAHOO_URL_PATTERN = ("http://kr.finance.yahoo.com/stock/compositeindex.html" + YAHOO_PATTERN);
		
	private ImportYahooIndex() {
		assert false;
	}
	
	
	static public QuoteList importData(String symbol, int symbolIndex, int marketIndex, TradeDate filterDate) throws ImportException {
		String line = null;
		String URLString = null;		
		String filterString1 = "<tr class=\"fst\">";
		String filterString2 = "<td class=\"a\">";
		QuoteList quoteList = null;
		boolean isContinue = true;
		int offset = 0;
		
		do {
			offset++;			
			URLString = constructURL(offset, symbol);		
									
			try {
				URL url = new URL(URLString);
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				
				InputStreamReader input = new InputStreamReader(connection.getInputStream());
				
				BufferedReader bufferedInput = new BufferedReader(input);

				while ( ( line = bufferedInput.readLine() ) != null ) {
					
					line = bufferedInput.readLine();
					if ( line.indexOf(filterString1) != -1) {
						line = bufferedInput.readLine();
						
						if ( line.indexOf(filterString2) != -1 ) 
							break;
					}
				}

				if ( line != null) {					
					TradeDate date = null;
					for (int i = 0; i < 10; i++) {
						date = checkDate(line, filterDate);
						if ( date == null ) {	
							isContinue = false;
							break;
						}
						
						if ( quoteList == null )
							quoteList = new QuoteList(symbol);
						
						quoteList.addQuote(parseQuote(date, bufferedInput, symbol));
						
						do {							
							line = bufferedInput.readLine();							
						} while ( i != 9 && line.indexOf(filterString2) == -1 );
					}
					filterDate = date;
					bufferedInput.close();
					input.close();
				}
			} catch (Exception e) {				
				throw new ImportException("Import Error : " + e.getMessage());
			}
		}while ( isContinue );
		
		return quoteList;
	}	
	
	static public String constructURL(int offset, String symbol) {
		String URLString = YAHOO_URL_PATTERN;
		URLString = URLString.replace(OFFSET, String.valueOf(offset));		
		URLString = URLString.replace(SYMBOL, symbol);		
		return URLString;
	}

	static public String constructFilterString(TradeDate filterDate) {
		String dateString = ">" + filterDate.getYear() + ".";
		
		if  ( filterDate.getMonth() < 10 )
			dateString += "0" + filterDate.getMonth() + ".";
		else 
			dateString += filterDate.getMonth() + ".";
		
		if ( filterDate.getDate() < 10 ) 
			dateString += "0" + filterDate.getDate() + "<";
		else
			dateString += filterDate.getDate() + "<";
		
		return dateString;
	}
	
	static private TradeDate checkDate(String line, TradeDate filterDate) throws ImportException {
		try {
			TradeDate date = toTradeDate(parseLine(line));
			
			//���� ���� ����
			if ( filterDate.equals(date) == true || filterDate.after(date) == true )
				return null;
			
			return date;
		} catch ( TradeDateFormatException e) {
			throw new ImportException(e.getMessage());
		}	
	}
	
	static private Quote parseQuote(TradeDate date, BufferedReader bufferedInput, String symbol) throws ImportException {
		Quote quote = null;		
		String line = null;
		double low, high, open, close;
		long volume;
		String message;
		try {		
			close = Double.valueOf(parseLine(bufferedInput.readLine()));
			
			// skip
			line = bufferedInput.readLine();
			line = bufferedInput.readLine();

			open = Double.valueOf(parseLine(bufferedInput.readLine()));
			high = Double.valueOf(parseLine(bufferedInput.readLine()));
			low = Double.valueOf(parseLine(bufferedInput.readLine()));
			volume = Long.valueOf(parseLine(bufferedInput.readLine()));
						
			quote = new Quote(date, low, high, open, close, volume);
			message = quote.verify();
			
			if ( message != null ) {
				FileWriter writer = new FileWriter(VVIPManager.getVVIP_PATH()+"/log/quote/" + symbol + ".txt", true);
				BufferedWriter bwriter = new BufferedWriter(writer);				
				bwriter.write(message);
				bwriter.close();
				writer.close();
			}
		} catch ( Exception e ) {
			throw new ImportException(e.getMessage());
		}		
		return quote;
	}
	
	
	static public String parseLine(String line) throws ImportException{	
		try {
			int start = line.lastIndexOf("\">");
			int end = line.indexOf("</");
			String number = line.substring(start+2, end);
			return number.replaceAll(",", "");
		} catch (Exception e) {
			throw new ImportException(e.getMessage());
		}
	}
	
	static public TradeDate toTradeDate(String stringDate) throws TradeDateFormatException {
		int year, month, day;
		
		year = Integer.valueOf(stringDate.substring(0, 4));
		month = Integer.valueOf(stringDate.substring(5, 7));
		day = Integer.valueOf(stringDate.substring(8));
		TradeDate tradeDate = new TradeDate(year, month, day);		
		return tradeDate;		
	}
}