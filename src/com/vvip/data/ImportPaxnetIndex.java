package com.vvip.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vvip.init.VVIPManager;
import com.vvip.quote.Quote;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.quote.TradeDateFormatException;

public class ImportPaxnetIndex {
	private final static String SYMBOL = "_SYM_";
	private final static String OFFSET = "_OFT_";
	private final static String PAXNET_PATTERN = "?chartVal=d&type=" + SYMBOL + "&page_m=&page_j=&sort=&page_d=" + OFFSET;
	private final static String PAXNET_URL_PATTERN = ("http://paxnet.moneta.co.kr/stock/stockPriceIndex/indexPtTrend.jsp" + PAXNET_PATTERN);
	
	private ImportPaxnetIndex() {
		assert false;
	}
	
	
	static public QuoteList importData(String symbol, TradeDate filterDate) throws ImportException {
		String line = null;
		String URLString = null;
		TradeDate td = null;
		QuoteList quoteList = null;
		boolean isContinue = true;
		int offset = 0;
		
		Pattern p = Pattern.compile("([0-9]{1,4})\\/([0-9]{1,2})\\/([0-9]{1,2})");
		
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
				int index = 0;

				while ( ( line = bufferedInput.readLine() ) != null ) {					
					Matcher m = p.matcher(line);
					if ( m.find() ) {						
						index++;
						
						td = checkDate(m.group(), filterDate);
			
						if ( td == null ) {
							isContinue = false;
							break;
						}						
						else {
							if ( quoteList == null )
								quoteList = new QuoteList(symbol);			
							
							quoteList.addQuote(parseQuote(td, bufferedInput, symbol));
						}				
					}					
					if ( index >= 10 )
						break;
				}
				bufferedInput.close();
				input.close();				
			} catch (Exception e) {				
				e.printStackTrace();
				throw new ImportException("Import Error : " + e.getMessage());
			}
		}while ( isContinue );
		
		if ( quoteList != null )
			quoteList.reverseList();
		
		return quoteList;
	}	
	
	/**
	 * Market�� ���� URL�� ������.
	 */
	static public String constructURL(int offset, String symbol) {
		String URLString = PAXNET_URL_PATTERN;
		URLString = URLString.replace(OFFSET, String.valueOf(offset));		
		URLString = URLString.replace(SYMBOL, symbol);		
		return URLString;
	}
	
	/**
	 * TradeDate ���������� ���� �� ���� ���� ���� ������ ���� ��������.
	 * @param line
	 * @return
	 * @throws ImportException
	 */
	static public TradeDate checkDate(String line, TradeDate filterDate) throws ImportException {
		try {
			TradeDate date = toTradeDate(line);
			
			//���� ���� ����
			if ( filterDate.equals(date) == true || filterDate.before(date) == true )
				return null;
			
			return date;
		} catch ( TradeDateFormatException e) {
			throw new ImportException(e.getMessage());
		}	
	}
	
	static public Quote parseQuote(TradeDate date, BufferedReader bufferedInput, String symbol) throws ImportException {
		Quote quote = null;		
		String line = null;
		double low, high, open, close;
		long volume;
		String message;
		try {		
			close = Double.valueOf(parseLine(bufferedInput.readLine()));
			open = Double.valueOf(parseLine(bufferedInput.readLine()));
			high = Double.valueOf(parseLine(bufferedInput.readLine()));
			low = Double.valueOf(parseLine(bufferedInput.readLine()));
			
			// skip
			line = bufferedInput.readLine();
			line = bufferedInput.readLine();
			
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
	
	
	/**
	 * �������� �������� ��������.
	 */
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
	
	/**
	 * ���� �������� TradeDate�� ��������.
	 * @param stringDate
	 * @return
	 * @throws TradeDateFormatException
	 */
	static public TradeDate toTradeDate(String stringDate) throws TradeDateFormatException {
		int year, month, day;
		
		year = Integer.valueOf(stringDate.substring(0, 4));
		month = Integer.valueOf(stringDate.substring(5, 7));
		day = Integer.valueOf(stringDate.substring(8));
		TradeDate tradeDate = new TradeDate(year, month, day);		
		return tradeDate;		
	}
}
