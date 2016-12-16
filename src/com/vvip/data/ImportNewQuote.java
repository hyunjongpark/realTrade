package com.vvip.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vvip.init.VVIPManager;
import com.vvip.quote.Company;
import com.vvip.quote.Quote;
import com.vvip.quote.TradeDate;
import com.vvip.quote.TradeDateFormatException;
import com.vvip.util.DatabaseManager;

/**
 * 占쏙옙占쏙옙 占쏙옙占�占쏙옙占쏙옙占쏙옙 占쌍쏙옙 占쌍곤옙 占쏙옙 占쏙옙占쏙옙占승댐옙.
 * 占�占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙퓔占�占쏙옙占쏙옙 占쌀쇽옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙홱占�
 * 
 * 占싱몌옙 占싱울옙占쏙옙 占쏙옙占싱몌옙 占쏙옙占싼댐옙.
 * 
 * @author gugyulim
 *
 */
public class ImportNewQuote {
	private final static String SYMBOL = "_SYM_";
	private final static String PAGE = "_PAGE_";	
	private final static String DAUM_PATTERN = "?page=1" + "&code=" + SYMBOL + "&modify=0";
	private final static String DAUM_URL_PATTERN = ("http://stock.daum.net/item/quote_yyyymmdd_sub.daum" + DAUM_PATTERN);	
	private ImportNewQuote() {
		assert false;
	}
	
	public static Quote importData(String symbol) throws ImportException {		
		try {
			DatabaseManager.getConnection();
			//Company company = DatabaseManager.selectCompanyBySymbol(symbol);
			Company company = VVIPManager.getCompany(symbol);
			
			//占쏙옙占쏙옙占싸듸옙占쏙옙占쏙옙 占싯삼옙占싹울옙 占싼깍옙 占쏙옙占쏙옙占쏙옙 占쏙옙占�占쏙옙占쏙옙占쏙옙占쏙옙 占실시곤옙 占쏙옙占쏙옙占싶몌옙 占쏙옙占쏙옙占승댐옙.
		//	if ( company.getMarketIndex() < 3 ) {
				//占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占시곤옙占쎈에占쏙옙 占싼댐옙.
				TradeDate today = new TradeDate();
				Calendar now = Calendar.getInstance();				
				if ( today.isWeekend() == true )
					return ImportDatabase(company);
				else if ( now.get(Calendar.HOUR_OF_DAY) > 19 && now.get(Calendar.HOUR_OF_DAY) < 8 )
					return ImportDatabase(company);
				else
					return ImportDaum(symbol);
	//		}
//			else if ( company.getMarketIndex() < 7  )
//				return ImportDatabase(company);
//			else
//				throw new ImportException("Symbol is not exist");
		} catch ( SQLException e ) {
			throw new ImportException(e.getMessage());
		} 
	}
	
	public static Quote ImportDatabase(Company company) throws SQLException {
		Quote quote = null;
//		Market market = DatabaseManager.selectMarketByIndex(company.getMarketIndex());
//		int symbolIndex = company.getSymbolIndex();
//		int marketIndex = company.getMarketIndex();
//		int hash = market.getHash();
//		//Hash 占쏙옙 0占싱몌옙 Index 占싱므뤄옙 hashNum占쏙옙 0 占쏙옙 占실깍옙 占쏙옙占쏙옙 symbolIndex占쏙옙 占쏙옙占쏙옙占싼댐옙.
//		if ( hash == 0)
//			hash = symbolIndex;		
		/*
		 * 占쏙옙占쏙옙占싶븝옙占싱쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쌍깍옙 占쌍곤옙占쏙옙 占쏙옙占쏙옙占승댐옙.
		 * 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占�占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙, 占쏙옙占쏙옙 占쌀쇽옙占쏙옙 占쏙옙치 占쏙옙占쏙옙 占쏙옙占쏙옙퓸占쏙옙獵占� 
		 */
		quote = DatabaseManager.getLastQuoteDifferenceBySymbol(company.getSymbol());		
		return quote;
	}
	public static Quote ImportDaum(String symbol) throws ImportException {
		Quote newQuote = null;
		
//		TradeDate td; 
//		//占쏙옙占쏙옙 占쌍근곤옙 占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쌍곤옙 占쏙옙占쏙옙占쏙옙 占식쏙옙占싹깍옙 占쏙옙占쏙옙 占쏙옙占�		int index = 2;
//		//占실뱄옙 占쏙옙占�占쏙옙占쏙옙占쏙옙, 占식띰옙占쏙옙拷占�占쏙옙占쌩깍옙 占쏙옙占쏙옙 占쎈도占쏙옙 占쏙옙占�		TradeDate filterDate =  new TradeDate(1900, 1, 1);
//		String URLString = DAUM_URL_PATTERN;
//		URLString = URLString.replace(SYMBOL, symbol);
//		try {
//			//占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싶몌옙 占싻는댐옙.
//			String line;
//			Quote preQuote = null;
//			URL url = new URL(URLString);			
//			URLConnection connection = url.openConnection();
//			connection.setConnectTimeout(5000);
//			connection.setReadTimeout(5000);
//			
//			//12.04.05占쏙옙 占쏙옙占쏙옙 占쏙옙占쌉쏙옙占쏙옙 찾占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙
//			Pattern p = Pattern.compile("([0-9]{1,2})\\.([0-9]{1,2})\\.([0-9]{1,2})");
//			InputStreamReader input = new InputStreamReader(connection.getInputStream());			
//			BufferedReader bufferedInput = new BufferedReader(input);
//			
//			//占쌍곤옙 占쏙옙占쏙옙占싶곤옙 占쌍댐옙 占쏙옙 占쏙옙占쏙옙 占쏙옙킵
//			do {
//				line = bufferedInput.readLine();
//			} while ( line.indexOf("<table") == -1 ); 
//			
//			/*
//			 * 占쏙옙占쏙옙 占쌍깍옙占쏙옙 占쌍곤옙占쏙옙 占식쏙옙占싹곤옙 占쌓댐옙占쏙옙 占쏙옙占쏙옙 占쌍곤옙 占쏙옙 占쏙옙占쏙옙占쏙옙 占쌍쏙옙占쏙옙 占� 占쏙옙占쏙옙占싼댐옙.
//			 * 占쌍쏙옙占쏙옙 占쏙옙占쏙옙 占쌀쇽옙占쏙옙 표占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙占싹울옙 占쏙옙占쏙옙占�占쏙옙 占쏙옙占쏙옙玖占�
//			 * 占쏙옙占�占�占쏙옙 占쏙옙占싱몌옙 占싱울옙占쏙옙 占쏙옙占�占쏙옙치 占쏙옙占쏙옙 占쏙옙占싼댐옙.
//			 */			
//			while ( ( line = bufferedInput.readLine() ) != null && index > 0) {					
//				Matcher m = p.matcher(line);
//				//占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙, 占쏙옙 占쏙옙짜 占쏙옙占쏙옙占싶곤옙 占쏙옙占쏙옙 占쏙옙
//				if ( m.find() ) {			
//					//占쏙옙짜占쏙옙 占식쏙옙占싹울옙 占쏙옙占쏙옙占승댐옙.
//					td = ImportDaumQuote.toTradeDate(ImportDaumQuote.parseLine(line), filterDate);
//					if ( index == 2 )
//						newQuote = ImportDaumQuote.parseQuote(td, bufferedInput, symbol);
//					else if ( index == 1 ) {
//						//占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쌍신놂옙占쏙옙 占쏙옙占쏙옙.
//						preQuote = ImportDaumQuote.parseQuote(td, bufferedInput, symbol);
//						newQuote.setLow(0);
//						newQuote.setHigh(preQuote.getClose());
//					}
//					index--;
//				}
//			}
//			
//			bufferedInput.close();
//			input.close();
//		}  catch (TradeDateFormatException e) {
//			throw new ImportException("Import Error : " + e.getMessage());
//		} catch (Exception e) {				
//			throw new ImportException("Import Error : " + e.getMessage());
//		}
		return newQuote;
	}
}
