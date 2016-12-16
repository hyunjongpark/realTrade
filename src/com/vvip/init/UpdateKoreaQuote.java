package com.vvip.init;

import java.util.ArrayList;

import com.vvip.data.ImportDaumQuote;
import com.vvip.data.ImportException;
import com.vvip.data.ImportPaxnetIndex;
import com.vvip.quote.Company;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

public class UpdateKoreaQuote {

	static ArrayList<String> logList = null;
	static int importZero = 0;

	public static void main(String[] args) {
		try {
			System.out.println("UpdateKoreaQuote");
			updateQuote(VVIPManager.getCompanyList());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	private static int updateQuote(ArrayList<Company> companyList) {
		int fail = 0;
		int dbSize = 1;
		QuoteList newList = null;
		QuoteList quoteList = null;
		TechnicalAnalysis ta = null;
		TradeDate filterDate = null;
		for (int i = 0; i < VVIPManager.getCompanyList().size(); i++) {

			String updateSymbol = companyList.get(i).getSymbol();
			System.out.println(i + 1 + "/" + companyList.size() + " : " + updateSymbol);
			
			if(!updateSymbol.equals("005300") && !CommonUtil.isVaildSymbol(updateSymbol)){
				continue;
			}
			quoteList = DatabaseManager.selectLimitQuoteListBySymbol(updateSymbol, 0, 50, true);

			if (quoteList != null) {
				dbSize = quoteList.getSize();
			}

			if (dbSize != 0) {
				filterDate = quoteList.getQuote(dbSize - 1).getTradeDate();
			}

			System.out.println("updateQuote : " + companyList.get(i).getNameInEng() + " : " + filterDate + " " + i + "/" + companyList.size() + " : " + companyList.get(i).getNameInEng() + " : "
					+ updateSymbol);

			try {
				newList = ImportDaumQuote.importData(updateSymbol, filterDate);
			} catch (ImportException e) {
				e.printStackTrace();
			}
			if (newList != null) {
				for (int j = 0; j < newList.getSize(); j++) {

					quoteList.addQuote(newList.getQuote(j));
				}

				ta = new TechnicalAnalysis(quoteList.getList(), dbSize);
				ta.setTaPattern();

				for (int k = dbSize; k < quoteList.getSize(); k++) {
					DatabaseManager.insertQuoteToSymbol(quoteList.getQuote(k), updateSymbol);
					System.out.println(quoteList.getQuote(k));
				}
			} else {
				importZero++;
			}
		}
		return fail;
	}

	public static int updateIndex(String marketName, ArrayList<Company> indexList) {
		int fail = 0;
		int dbSize;
		QuoteList newList = null;
		QuoteList quoteList = null;
		TechnicalAnalysis ta = null;
		TradeDate filterDate = null;

		for (int i = 0; i < indexList.size(); i++) {
			try {
				System.out.println(i + 1 + "/" + indexList.size());
				quoteList = DatabaseManager.selectLimitQuoteListBySymbol(indexList.get(i).getSymbol(), 0, 50, true);

				if (quoteList == null)
					throw new ImportException("Data is not Exist in Database");

				dbSize = quoteList.getSize();

				filterDate = quoteList.getQuote(dbSize - 1).getTradeDate();

				newList = ImportPaxnetIndex.importData(indexList.get(i).getSymbol(), filterDate);

				if (newList != null) {
					for (int j = 0; j < newList.getSize(); j++) {
						quoteList.addQuote(newList.getQuote(j));
					}
					ta = new TechnicalAnalysis(quoteList.getList(), dbSize);
					ta.setTaPattern();

					for (int k = dbSize; k < quoteList.getSize(); k++) {
						DatabaseManager.insertQuoteToSymbol(quoteList.getQuote(k), indexList.get(i).getSymbol());
						// System.out.println(quoteList.getQuote(k));
					}
				} else {
					importZero++;
				}

			} catch (ImportException e) {
				fail++;
				logList.add("Symbol : " + indexList.get(i).getSymbol());
				logList.add("Fail : " + indexList.get(i).getNameInKor() + "\n" + e.getMessage() + "\n");
			} catch (Exception e) {
				fail++;
				logList.add("Symbol : " + indexList.get(i).getSymbol());
				logList.add("Fail : " + indexList.get(i).getNameInKor() + "\n" + e.getMessage() + "\n");
			}
		}
		return fail;
	}
}
