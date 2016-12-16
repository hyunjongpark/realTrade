package com.vvip.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.vvip.init.VVIPManager;
import com.vvip.quote.Quote;
import com.vvip.quote.TradeDate;
import com.vvip.quote.TradeDateFormatException;

public class ImportKoreaMarketIndexFile {
	private static final String KospiPath = VVIPManager.getVVIP_PATH()+"/data/KOSPI_index.csv";
	private static final String KosdaqPath = VVIPManager.getVVIP_PATH()+"/data/KOSDAQ_index.csv";
	private static final String Kospi200Path = VVIPManager.getVVIP_PATH()+"/data/KOSPI200_index.csv";	

	private ImportKoreaMarketIndexFile() {
		assert false;
	}

	/**
	 * �������� �������� ���� ArrayList�� ��������.
	 * 
	 * @return
	 * @throws ImportException
	 */
	public static ArrayList<Quote> importKospiIndexData() throws ImportException {
		ArrayList<Quote> quoteList = new ArrayList<Quote>();
		FileReader dataFile = null;
		BufferedReader bufferedData = null;

		try {
			dataFile = new FileReader(KospiPath);
			bufferedData = new BufferedReader(dataFile);
			parseDataFile(quoteList, bufferedData);
			System.out.println("Parse OK");
			dataFile.close();
			return quoteList;
		} catch (Exception e) {
			throw new ImportException(e);
		}
	}

	/**
	 * �������� �������� ���� ArrayList�� ��������.
	 * 
	 * @return
	 * @throws ImportException
	 */
	public static ArrayList<Quote> importKosdaqIndexData() throws ImportException {
		ArrayList<Quote> quoteList = new ArrayList<Quote>();
		FileReader dataFile = null;
		BufferedReader bufferedData = null;

		try {
			dataFile = new FileReader(KosdaqPath);
			bufferedData = new BufferedReader(dataFile);
			parseDataFile(quoteList, bufferedData);
			dataFile.close();
			return quoteList;
		} catch (Exception e) {
			throw new ImportException(e);
		}
	}
	/**
	 * ������ 200�� �������� ���� ArrayList�� ��������.
	 * 
	 * @return
	 * @throws ImportException
	 */
	public static ArrayList<Quote> importKospi200IndexData() throws ImportException {
		ArrayList<Quote> quoteList = new ArrayList<Quote>();
		FileReader dataFile = null;
		BufferedReader bufferedData = null;

		try {
			dataFile = new FileReader(Kospi200Path);
			bufferedData = new BufferedReader(dataFile);
			parseDataFile(quoteList, bufferedData);
			dataFile.close();
			return quoteList;
		} catch (Exception e) {
			throw new ImportException(e);
		}

	}

	/**
	 * �������� ������ ���� ��������.
	 * 
	 * @param QuoteList
	 * @param bufferedReader
	 * @throws IOException
	 */
	private static void parseDataFile(ArrayList<Quote> quoteList,
			BufferedReader bufferedReader) throws IOException {
		String line = null;
		// ���� ����
		line = bufferedReader.readLine();
		while (line != null) {
			line = bufferedReader.readLine();
			if (line.length() > 7)
				quoteList.add(parseLineData(line));
			else
				break;
		}
		bufferedReader.close();
	}

	/**
	 * �� ������ �������� ��������.
	 * 
	 * @param line
	 * @return
	 * @throws TradeDateFormatException 
	 * @throws NumberFormatException 
	 */
	private static Quote parseLineData(String line) {
		Quote quote = null;
		String[] data = null;
		data = line.split("##");
		try {
		quote = new Quote(new TradeDate(data[0]), Double.valueOf(data[4]), 
							Double.valueOf(data[3]), Double.valueOf(data[2]), 
								Double.valueOf(data[1]), Long.valueOf(data[5]));
		quote.verify();
		} catch (TradeDateFormatException e) {
			System.out.println(e.getMessage());
		}
		return quote;
	}

}
