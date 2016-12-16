package com.vvip.test;

import java.util.ArrayList;

import com.vvip.data.ImportException;
import com.vvip.data.ImportKoreaMarketIndexFile;
import com.vvip.quote.Quote;

public class TestImportKoreaMarketIndex {
	public static void main(String[] args) {
		ArrayList<Quote> kospiList = null;
		ArrayList<Quote> kosdaqList = null;
		ArrayList<Quote> kospi200List = null;
		
		try {
			kospiList = ImportKoreaMarketIndexFile.importKospiIndexData();
			kosdaqList = ImportKoreaMarketIndexFile.importKosdaqIndexData();
			kospi200List = ImportKoreaMarketIndexFile.importKospi200IndexData();
			
		} catch (ImportException e) {
			System.err.println("Importing Data Fail : " + e);
		}
		for ( int i = 0; i < kospi200List.size(); i++ ) {
			System.out.println(kospi200List.get(i));
		}
	}
}