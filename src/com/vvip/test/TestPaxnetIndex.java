package com.vvip.test;

import com.vvip.data.ImportException;
import com.vvip.data.ImportPaxnetIndex;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

public class TestPaxnetIndex {
	public static void main(String[] args) {

		 //�ڽ��� kospi
		 //�ڽ��� kosdaq
		 //�ڽ���200 kospi200
		 //DB�� �����ϴ� ���� �ֱ� ��¥
	
			int symbolIndex;
			int marketIndex;
			int fail = 0;
			int dbSize;
			QuoteList newList = null;
			QuoteList quoteList = null;
			TechnicalAnalysis ta = null;
			TradeDate filterDate = null;		
//		 try {
////				���� �ε���
//				marketIndex = DatabaseManager.getMarketIndex("INDEX");
//
//				System.out.println(marketIndex);
//				//�ɺ� �ε���
//				symbolIndex = DatabaseManager.getDBSymbolIndex("kospi");				
//				if ( symbolIndex == -1 )
//					throw new ImportException("Symbol Index is not exist.");
//				
//				System.out.println(symbolIndex);
//				
//				//������ �ν��ϱ� ���� ���� 50���� �����´�. ���ź��� �ֽ� ������ �����´�.
//				quoteList = DatabaseManager.selectLimitQuoteListBySymbol(symbolIndex, marketIndex, 0, 0, 50, true);
//				
//				//������ ���̽��� �ɺ��� quote�� �������� ������
//				if ( quoteList == null )
//					throw new ImportException("Data is not Exist in Database");
//				
//				//������ ��ü ���� ����
//				dbSize = quoteList.getSize();
//				
//				//������ ���̽��� ù��° ��¥		
//				filterDate = quoteList.getQuote(dbSize-1).getTradeDate();
//				System.out.println(filterDate.toString());
//				//ù ��° ��¥���� ������ �����´�.
//				newList = ImportPaxnetIndex.importData("kospi", symbolIndex, marketIndex , filterDate);
//				
//				//���ο� �����Ͱ� �����ϸ�
//				if (newList != null) {
//					//���ο� �ְ� ������ �����ͺ��̽��� ����Ʈ�� �߰��Ѵ�.
//					for (int j = 0; j < newList.getSize(); j++) {
//						quoteList.addQuote(newList.getQuote(j));
//					}
//					//TA������ �ν��Ͽ� �����Ѵ�.
//					ta = new TechnicalAnalysis(quoteList.getList(), dbSize);
//					ta.setTaPattern();
//
//					//������ ���̽��� �߰��� �κи� �����Ѵ�.
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
