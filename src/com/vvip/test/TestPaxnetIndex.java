package com.vvip.test;

import com.vvip.data.ImportException;
import com.vvip.data.ImportPaxnetIndex;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

public class TestPaxnetIndex {
	public static void main(String[] args) {

		 //코스피 kospi
		 //코스닥 kosdaq
		 //코스피200 kospi200
		 //DB에 존재하는 가장 최근 날짜
	
			int symbolIndex;
			int marketIndex;
			int fail = 0;
			int dbSize;
			QuoteList newList = null;
			QuoteList quoteList = null;
			TechnicalAnalysis ta = null;
			TradeDate filterDate = null;		
//		 try {
////				마켓 인덱스
//				marketIndex = DatabaseManager.getMarketIndex("INDEX");
//
//				System.out.println(marketIndex);
//				//심볼 인덱스
//				symbolIndex = DatabaseManager.getDBSymbolIndex("kospi");				
//				if ( symbolIndex == -1 )
//					throw new ImportException("Symbol Index is not exist.");
//				
//				System.out.println(symbolIndex);
//				
//				//패턴을 인식하기 위해 앞의 50개를 가져온다. 과거부터 최신 순으로 가져온다.
//				quoteList = DatabaseManager.selectLimitQuoteListBySymbol(symbolIndex, marketIndex, 0, 0, 50, true);
//				
//				//데이터 베이스에 심볼의 quote가 존재하지 않을때
//				if ( quoteList == null )
//					throw new ImportException("Data is not Exist in Database");
//				
//				//종목의 전체 수를 저장
//				dbSize = quoteList.getSize();
//				
//				//데이터 베이스의 첫번째 날짜		
//				filterDate = quoteList.getQuote(dbSize-1).getTradeDate();
//				System.out.println(filterDate.toString());
//				//첫 번째 날짜까지 웹에서 가져온다.
//				newList = ImportPaxnetIndex.importData("kospi", symbolIndex, marketIndex , filterDate);
//				
//				//새로운 데이터가 존재하면
//				if (newList != null) {
//					//새로운 주가 정보를 데이터베이스의 리스트에 추가한다.
//					for (int j = 0; j < newList.getSize(); j++) {
//						quoteList.addQuote(newList.getQuote(j));
//					}
//					//TA패턴을 인식하여 저장한다.
//					ta = new TechnicalAnalysis(quoteList.getList(), dbSize);
//					ta.setTaPattern();
//
//					//데이터 베이스에 추가된 부분만 삽입한다.
//					for (int k = dbSize; k < quoteList.getSize(); k++) {
//						//DatabaseManager.insertQuoteToSymbol(quoteList.getQuote(k), symbolIndex, marketIndex, 0);
//						System.out.println(quoteList.getQuote(k));
//					}
//				}
//		 }
//		 catch (ImportException e) {
//			 System.out.println(e.getMessage());
//		 } catch (Exception e) {
//			 System.out.println(e.getMessage());
//		 }
//		 finally{
//		 }
	}	
}
