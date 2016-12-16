package com.vvip.test;

import java.util.ArrayList;

import com.vvip.quote.Company;
import com.vvip.quote.QuoteList;

public class TestDatabase {
	public static void main(String[] args) {
		ArrayList<Company> companyList = null;
		String search = "»ï¼º ÀüÀÚ";
		QuoteList quoteList;
		
		String[] splitSearch = search.split(" ");
		int marketIndex;

		try {
			String temp = null;
			for (int i = 0; i < 20; i++) {
				temp = String.valueOf((char)('a' + i));				
				System.out.println(temp);
			}
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		
	}
}
