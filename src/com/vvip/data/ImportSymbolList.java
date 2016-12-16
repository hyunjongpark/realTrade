package com.vvip.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.vvip.init.VVIPManager;
import com.vvip.quote.Company;

public class ImportSymbolList {	
	public static ArrayList<Company> importNasdaqList() throws ImportException {
		ArrayList<Company> companyList = null;
		String path = VVIPManager.getVVIP_PATH()+"/data/NASDAQ.csv";
			
		companyList = importUSAData(path);
		
		return companyList;
	}
	public static ArrayList<Company> importNyseList() throws ImportException  {
		ArrayList<Company> companyList = null;
		String path = VVIPManager.getVVIP_PATH()+"/data/NYSE.csv";
			
		companyList = importUSAData(path);
				
		return companyList;
	}
	public static ArrayList<Company> importAmexList() throws ImportException {
		ArrayList<Company> companyList = null;
		String path = VVIPManager.getVVIP_PATH()+"/data/AMEX.csv";
			
		companyList = importUSAData(path);
			
		return companyList;
	}
	
	public static ArrayList<Company> importKospiList() throws ImportException {
		ArrayList<Company> companyList = null;
		String path = VVIPManager.getVVIP_PATH()+"/data/kospi.csv";
		companyList = importKoreaData(path);
		return companyList;
	}
	
	public static ArrayList<Company> importKosdaqList() throws ImportException{
		ArrayList<Company> companyList = null;
		String path = VVIPManager.getVVIP_PATH()+"/data/kosdaq.csv";
			
		companyList = importKoreaData(path);		
		return companyList;
	}
	
	private static ArrayList<Company> importUSAData(String path) throws ImportException {
		ArrayList<Company> companyList = new ArrayList<Company>();
		FileReader dataFile = null;
		BufferedReader bufferedReader = null;

		try {
			dataFile = new FileReader(path);
			bufferedReader = new BufferedReader(dataFile);

			String line = bufferedReader.readLine();

			while (line != null) {
				line = bufferedReader.readLine();
				if (line.length() > 7) {
					String[] data = null;
					data = line.split("##");
					companyList.add(new Company(data[0], data[1]));
				} else
					break;
			}
			bufferedReader.close();
			dataFile.close();
		} catch (Exception e) {
			throw new ImportException(e.getMessage());
		}

		return companyList;
	}
	
	private static ArrayList<Company> importKoreaData(String path) throws ImportException {
		ArrayList<Company> companyList = new ArrayList<Company>();
		FileReader dataFile = null;
		BufferedReader bufferedReader = null;
		
		System.out.print("importKoreaData\n"+ path +"\n");
		try {
			dataFile = new FileReader(path);
			
			bufferedReader = new BufferedReader( new InputStreamReader(new FileInputStream(path), "MS949"));

			String line = bufferedReader.readLine();

			while (line != null) {
				line = bufferedReader.readLine();
				
				if (line != null && line.length() > 7) {
					String[] data = null;
					
					data = line.split(",");
					
					companyList.add(new Company(data[0], data[1], Double.parseDouble(data[2])));
					System.out.println(data[0]+"/"+data[1]+"/"+Double.parseDouble(data[2]));
				} else
					break;
			}
			bufferedReader.close();
			dataFile.close();
		} catch (Exception e) {
			throw new ImportException(e.getMessage());
		}
		return companyList;
	}
}
