package com.vvip.init;

import java.util.ArrayList;
import java.util.Date;

import com.vvip.data.ImportDaumQuote;
import com.vvip.data.ImportException;
import com.vvip.ga.CreateMacdGeneData;
import com.vvip.quote.Company;
import com.vvip.quote.QuoteList;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

public class CreateDataBase {
	public static void main(String[] args) {
		System.out.println("CreateDataBase main 1");
		DatabaseManager.getConnection();
		createTable(); // Step_1
		insertQuote(); // Step_2
		CreateMacdGeneData.main(null); // Step_3
		UpdateKoreaQuote.main(null); // Step_4
		RunMacdGeneData macdgene = new RunMacdGeneData();
		macdgene.startVVIPManagerThread();
	}

	private static void createTable() {
		DatabaseManager.createGATable(0);
		DatabaseManager.createCompanyTable();
		insertCampanyInfo();

		ArrayList<Company> companyList = VVIPManager.getCompanyList();
		for (int i = 0; i < companyList.size(); i++) {
			String symbol = companyList.get(i).getSymbol();
			DatabaseManager.createSymbolTable(symbol);
		}
	}

	private static void insertCampanyInfo() {
		VVIPManager.setInsertKospiList();
		VVIPManager.setInsertkosdaqiList();
	}

	private static void insertQuote() {
		insertQuoteKorea(VVIPManager.getCompanyList());
	}

	private static void insertQuoteKorea(ArrayList<Company> companyList) {

		System.out.println("insertQuoteKorea");
		QuoteList quoteList = null;
		TechnicalAnalysis ta = null;
		for (int i = 0; i < companyList.size(); i++) {
			System.out.println(i + 1 + "/" + companyList.size());
			try {
				quoteList = ImportDaumQuote.importData(companyList.get(i).getSymbol(), null);
				if (null == quoteList) {
					continue;
				}
				ta = new TechnicalAnalysis(quoteList.getList(), 0);
				ta.setTaPattern();
				DatabaseManager.insertQuoteListToSymbol(quoteList, companyList.get(i).getSymbol());
			} catch (ImportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println((new Date()).toString());

	}

}
