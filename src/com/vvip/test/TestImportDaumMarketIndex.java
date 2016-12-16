package com.vvip.test;

import java.util.ArrayList;

import com.vvip.data.ImportDaumMarketIndex;
import com.vvip.data.ImportException;

public class TestImportDaumMarketIndex {
	 public static void main(String[] args) {
		 ArrayList<String> indexList = null;
		 try {
			 indexList = ImportDaumMarketIndex.importData();
		 }
		 catch (ImportException e) {
			 System.out.println(e.getMessage());
		 }		 
		 for ( int i = 0; i < indexList.size(); i++) 
			 System.out.println(indexList.get(i));
	}	
}
