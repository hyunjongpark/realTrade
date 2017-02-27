package com.vvip.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.vvip.quote.Quote;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.quote.TradeDateFormatException;

public class ImportDaumQuote {
	private final static String SYMBOL = "_SYM_";
	private final static String PAGE = "_PAGE_";	
	private final static String DAUM_PATTERN = "?page=" + PAGE + "&code=" + SYMBOL + "&modify=0";
	private final static String DAUM_URL_PATTERN = ("http://finance.daum.net/item/quote_yyyymmdd_sub.daum" + DAUM_PATTERN);
//	                                                 http://finance.daum.net/item/marketvalue.daum?stype=P&page=2&col=listprice&order=desc
		
	public ImportDaumQuote() {
		assert false;
	}

	static public QuoteList importData(String symbol,TradeDate filterDate) throws ImportException {
		
		String line = null;
		String URLString;		
		String filterString = "<td class=\"datetime2\">";
		int page = 0;
		
		if ( filterDate == null ) {
			filterDate = new TradeDate(2013, 1, 1);
		}
		
		QuoteList quoteList = null;
		boolean isContinue = true;
		
		int count;
		
		do {
			page++;		
			count = 0;
			URLString = constructURL(page, symbol);
			System.out.println("daum url : " + URLString);
						
			try {
				URL url = new URL(URLString);
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				
				InputStreamReader input = new InputStreamReader(connection.getInputStream());
				
				BufferedReader bufferedInput = new BufferedReader(input);

				while ( ( line = bufferedInput.readLine() ) != null && count != 30 ) {
					
					if ( line.indexOf(filterString) != -1 ) {
						TradeDate date = toTradeDate(parseLine(line), filterDate);
						
						if ( date == null ) {
							isContinue = false;
							break;
						}
						
						if ( quoteList == null )
							quoteList = new QuoteList(symbol);
						
						quoteList.addQuote((parseQuote(date, bufferedInput, symbol)));
						count++;
					}
				}
				bufferedInput.close();
				input.close();
				
				if ( count != 30 )
					isContinue = false;
				
			} catch (TradeDateFormatException e) {
				throw new ImportException("Import Error : " + e.getMessage());
			} catch (Exception e) {				
				throw new ImportException("Import Error : " + e.getMessage());
			}
		} while ( isContinue );
		
		if ( quoteList != null )
			quoteList.reverseList();
		return quoteList;
	}

	static public String constructURL(int page, String symbol) {
		String URLString = DAUM_URL_PATTERN;
		URLString = URLString.replace(PAGE, String.valueOf(page));
		URLString = URLString.replace(SYMBOL, symbol);		
		return URLString;
	}

	static public String parseLine(String line) throws ImportException{
		try {
		int start = line.indexOf(">");
		int end = line.lastIndexOf("</td>");
		String number = line.substring(start+1, end);	
		return number.replaceAll(",", "");
		} catch (Exception e) {
			throw new ImportException(e.getMessage());
		}
		
	}
	
	static public Quote parseQuote(TradeDate date, BufferedReader bufferedInput, String symbol) {
		Quote quote = null;
		String line = null;
		double low, high, open, close;
		long volume;
		String message;

		try {
			open = Double.valueOf(parseLine(bufferedInput.readLine()));
			high = Double.valueOf(parseLine(bufferedInput.readLine()));
			low = Double.valueOf(parseLine(bufferedInput.readLine()));
			close = Double.valueOf(parseLine(bufferedInput.readLine()));
			line = bufferedInput.readLine();
			line = bufferedInput.readLine();
			volume = Long.valueOf(parseLine(bufferedInput.readLine()));
			quote = new Quote(date, low, high, open, close, volume);
			message = quote.verify();

//			if (message != null) {
//				FileWriter writer = new FileWriter(VVIPManager.getVVIP_PATH() + "/log/quote/" + symbol + ".txt", true);
//				BufferedWriter bwriter = new BufferedWriter(writer);
//				bwriter.write(message);
//				bwriter.close();
//				writer.close();
//			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return quote;
	}
	
	static public TradeDate toTradeDate(String stringDate, TradeDate filterDate) throws TradeDateFormatException {
		int year, month, day;
		if ( Integer.valueOf(stringDate.substring(0, 2)) > 50 )
			stringDate = "19" + stringDate;
		else
			stringDate = "20" + stringDate;
		//��, ��, �� ������ ����
		year = Integer.valueOf(stringDate.substring(0, 4));
		month = Integer.valueOf(stringDate.substring(5, 7));
		day = Integer.valueOf(stringDate.substring(8));
		TradeDate tradeDate = new TradeDate(year, month, day);		
		//���� ���� ������ ������ ��������.
		if ( tradeDate.before(filterDate) ||tradeDate.equals(filterDate))
			return null;
		return tradeDate;		
	}

}
