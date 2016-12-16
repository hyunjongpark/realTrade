package com.vvip.test;

import java.util.ArrayList;

import com.vvip.quote.Company;
import com.vvip.util.DatabaseManager;

public class TestSearch {
	public static void main(String[] args) {
		String search = "ai";
		ArrayList<Company> companyList = null;
		String[] splitSearch = search.split(" ");
		int index = 0;
		int size = splitSearch.length;

		DatabaseManager.getConnection();
//		try {
//			while (companyList == null && index < size ) {
//				companyList = DatabaseManager.searchCompanyByInput(splitSearch[index++], 0);
//			}
//			for ( int i = index; i < size; i++ ) {
//    			splitSearch[i] = splitSearch[i].toUpperCase();
//    			for ( int j = companyList.size()-1; j >= 0; j-- ) {
//    				Company c = companyList.get(j);    				
//    				if ( c.getNameInKor() != null ) {
//    					if ( c.getSymbol().toUpperCase().indexOf(splitSearch[i]) == -1  
//    							&& c.getNameInEng().toUpperCase().indexOf(splitSearch[i]) == -1 
//    							&& c.getNameInKor().indexOf(splitSearch[i]) == -1 ) {
//    						companyList.remove(j);
//    					}
//    				} else {
//    					if ( c.getSymbol().toUpperCase().indexOf(splitSearch[i]) == -1  
//    							&& c.getNameInEng().toUpperCase().indexOf(splitSearch[i]) == -1 ) {    					
//    						companyList.remove(j);
//    					}
//    				}
//    			}	
//    		}           		      			
//			for ( int i = 0; i < companyList.size(); i++ )
//				System.out.println(companyList.get(i));
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
