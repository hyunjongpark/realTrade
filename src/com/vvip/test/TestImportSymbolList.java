package com.vvip.test;

import java.util.ArrayList;

import com.vvip.data.ImportException;
import com.vvip.data.ImportSymbolList;
import com.vvip.quote.Company;


public class TestImportSymbolList {
	public static void main(String[] args) {
		ArrayList<Company> kospiList = null;
		ArrayList<Company> kosdaqList = null;
		ArrayList<Company> nyseList = null;
		ArrayList<Company> nasdaqList = null;
		ArrayList<Company> amexList = null;
		try {
			kospiList = ImportSymbolList.importKospiList();
			kosdaqList = ImportSymbolList.importKosdaqList();
			nyseList = ImportSymbolList.importNyseList();
			nasdaqList = ImportSymbolList.importNasdaqList();
			amexList = ImportSymbolList.importAmexList();
			
			for ( int i = 0; i < kospiList.size(); i++ ) {
				System.out.println(kospiList.get(i));
			}
			for ( int i = 0; i < kosdaqList.size(); i++ ) {
				System.out.println(kosdaqList.get(i));
			}
			for ( int i = 0; i < nyseList.size(); i++ ) {
				System.out.println(nyseList.get(i));
			}
			for ( int i = 0; i < nasdaqList.size(); i++ ) {
				System.out.println(nasdaqList.get(i));
			}
			for ( int i = 0; i < amexList.size(); i++ ) {
				System.out.println(amexList.get(i));
			}
			
		} catch (ImportException e) {
			System.err.println("Importing Data Fail : " + e);
		}
		
	}
}