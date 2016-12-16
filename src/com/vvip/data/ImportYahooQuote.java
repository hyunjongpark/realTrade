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

/**
 * Yahoo에서 해당하는 심볼에 대한 지정된 날짜 사이의 주가를 가져온다.
 * @author gugyulim
 *
 */
public class ImportYahooQuote{
	private final static String SYMBOL = "_SYM_";

	private final static String YAHOO_PATTERN = "?s=" + SYMBOL; 
	private final static String YAHOO_URL_PATTERN = ("http://ichart.finance.yahoo.com/table.csv" + YAHOO_PATTERN);
			
	public ImportYahooQuote() {
		assert false;
	}
	/**
	 * QuoteList의 Constructor 변경
	 * @return
	 * @throws ImportException
	 */
	static public QuoteList importData(String symbol, int symbolIndex, int marketIndex, TradeDate filterDate)  throws ImportException {
		QuoteList quoteList = null;
		String URLString = constructURL(symbol);		
		String message;
		
		if ( filterDate == null )
			filterDate = new TradeDate(1990, 1, 1);

		try {
			URL url = new URL(URLString);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			InputStreamReader input = new InputStreamReader(connection.getInputStream());
			
			BufferedReader bufferedInput = new BufferedReader(input);
			
			//첫 줄은 컴럼값이므로 무시한다.
			String line = bufferedInput.readLine();

			while ((line = bufferedInput.readLine()) != null) {
				Quote quote = parseLine(line, filterDate);
				if ( quote == null )
					break;
				
				message = quote.verify();	
				if ( message != null ) {
					FileWriter writer = new FileWriter(VVIPManager.getVVIP_PATH()+"/log/quote/" + symbol + ".txt", true);
					BufferedWriter bwriter = new BufferedWriter(writer);					
					bwriter.write(message);
					bwriter.close();
					writer.close();
				}
				
				if ( quoteList == null )
					quoteList = new QuoteList(symbol);
				quoteList.addQuote(quote);
			}
			bufferedInput.close();
		}
		catch (Exception e) {
			throw new ImportException("Import Error : " + e.getMessage());
		}
		//가져온 quote의 리스트를 정렬한다.
		if ( quoteList != null )
			quoteList.reverseList();
		return quoteList;
	}

	static public String constructURL(String symbol) {
		String URLString = YAHOO_URL_PATTERN;		
		URLString = URLString.replace(SYMBOL, symbol);
		
		return URLString;
	}


	static public Quote parseLine(String line, TradeDate filterDate) throws ImportException {
		Quote quote = null;

		if (line != null) {
			String[] quoteParts = line.split(",");// ',' 로 스트링을 나눈다. 
			int i = 0;

			if (quoteParts.length == 7) {
				TradeDate date = null;

				try {
					date = new TradeDate(quoteParts[i++]);	// 0번 날짜.
					if ( date.before(filterDate) || date.equals(filterDate) )
						return null;
				} catch (TradeDateFormatException e) {
					throw new ImportException(e.getMessage());
				}

				try {
					double open = Double.parseDouble(quoteParts[i++]);	 
					double high = Double.parseDouble(quoteParts[i++]);	
					double low = Double.parseDouble(quoteParts[i++]);
					double close = Double.parseDouble(quoteParts[i++]);
					long volume = Long.parseLong(quoteParts[i++]);
					
					// the remaining one is adjusted day close					
					quote = new Quote(date, low, high, open, close, volume);
					
				} catch (NumberFormatException e) {
					throw new ImportException("ERROR_PARSING_NUMBER" + quoteParts[i - 1]);
				}
			} else
				throw new ImportException("WRONG_FIELD_COUNT");
		}
		return quote;
	}

	

}

