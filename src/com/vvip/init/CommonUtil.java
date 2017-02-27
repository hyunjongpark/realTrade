package com.vvip.init;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.vvip.data.ImportDaumQuote;
import com.vvip.data.ImportException;
import com.vvip.ga.GeneResult;
import com.vvip.quote.Company;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

public class CommonUtil {
	private static String todayInit = null;
	public static ArrayList<String> removeSymbol = new ArrayList<>();

	public static void setTodayName() {
		Calendar cal = new GregorianCalendar();
		int mYear = cal.get(Calendar.YEAR);
		int mMonth = cal.get(Calendar.MONTH) + 1;
		int mDay = cal.get(Calendar.DAY_OF_MONTH);
		String strMonth;
		if (mMonth / 10 == 0) {
			strMonth = String.format("0%d", mMonth);
		} else {
			strMonth = Integer.toString(mMonth);
		}
		String strDay;
		if (mDay / 10 == 0) {
			strDay = String.format("0%d", mDay);
		} else {
			strDay = Integer.toString(mDay);
		}
		todayInit = mYear + "" + strMonth + "" + strDay;
	}

	public static String getTodayName() {
		setTodayName();
		return todayInit;
	}

	public static int getToday() {
		int today = 0;
		if (today == 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
			Date date = new Date();
			today = Integer.parseInt(format.format(date));
		}
		return today;
	}

	public static String getLastTradedayName() {
		String fileDate = null;
		QuoteList quoteList = DatabaseManager.selectLimitQuoteListBySymbol("005930", 1, 0, true);
		String yesterday = quoteList.getQuote(quoteList.getSize() - 1).getTradeDate().toString();
		fileDate = yesterday.replace(".", "");
		return fileDate;
	}

	public static int getLimitMacdEndTime() {
		int today = Integer.parseInt(getTodayName());
		return getDayIntByWeek(today, 3);
	}

	public static boolean isPastMacdEndTime(String symbol) {
		int today = Integer.parseInt(getTodayName());
		int preDay = getDayIntByMonth(today, 3);
		int lastMacdGeneTime = DatabaseManager.getMacdGeneEndTime(symbol);
		if (preDay >= lastMacdGeneTime) {
			return true;
		} else {
			return false;
		}
	}

	public static int getDayIntByMonth(int baseDay, int minusMonth) {
		if (baseDay == 99999999) {
			baseDay = getlastDayOfPrice();
		}
		int year = baseDay / 10000;
		int month = ((baseDay % 10000) / 100) - minusMonth;
		int date = baseDay % 100;

		if (month < 0) {
			year -= 1;
			month = 12 + month;
		}

		return year * 10000 + month * 100 + date;
	}

	public static int getDayIntByWeek(int baseDay, int minusWeek) {
		if (baseDay == 99999999) {
			baseDay = getlastDayOfPrice();
		}
		int year = baseDay / 10000;
		int month = (baseDay % 10000) / 100;
		int date = (baseDay % 100) - (minusWeek * 7);

		if (date < 0) {
			if (month == 1) {
				year -= 1;
				month = 12;
			} else {
				month -= 1;
			}

			date = 30 + date;
		}

		if (month < 0) {
			year -= 1;
			month = 12 + month;
		}

		return year * 10000 + month * 100 + date;
	}

	public static int getlastDayOfPrice() {
		QuoteList quoteList = DatabaseManager.selectLimitQuoteListBySymbol(VVIPManager.getCompanyList().get(0).getSymbol(), 0, 50, true);
		if (quoteList == null) {
			return 0;
		}
		int dbSize = quoteList.getSize();
		return quoteList.getQuote(dbSize - 1).getTradeDate().toInt();
	}

	public static List<String> checkPastMacdCount() {
		List<String> pastEndDateSymbol = new ArrayList<String>();
		try {
			int nCount = 0;
			int nVaildCount = 0;
			for (int i = 0; i < VVIPManager.getCompanyList().size(); i++) {
				String symbol = VVIPManager.getCompanyList().get(i).getSymbol();
				if (!isVaildSymbol(symbol)) {
					continue;
				}
				nVaildCount++;
				// int lastMacdGeneTime =
				// DatabaseManager.getMacdGeneEndTime(symbol);
				// if (lastMacdGeneTime == 0) {
				// System.out.println(i+" : symbol : "+symbol+" lastMacdGeneTime
				// : "+lastMacdGeneTime);
				// continue;
				// }
				if (isPastMacdEndTime(symbol)) {
					System.out.println("must make macd " + i + "/" + VVIPManager.getCompanyList().size() + " company : " + VVIPManager.getCompanyList().get(i).getNameInEng() + " symbol : "
							+ VVIPManager.getCompanyList().get(i).getSymbol());
					nCount++;
					pastEndDateSymbol.add(VVIPManager.getCompanyList().get(i).getSymbol());
				}
			}
			System.out.println("must make macdgene count : " + nCount + "/" + nVaildCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// for (int i = 0; i < removeSymbol.size(); i++) {
		// DatabaseManager.removeCompany(removeSymbol.get(i));
		// DatabaseManager.dropSymbolTable(removeSymbol.get(i));
		// }

		return pastEndDateSymbol;
	}

	public static boolean isVaildSymbol(String symbol) {
		QuoteList quoteList = DatabaseManager.selectQuoteListByDate(symbol, Integer.parseInt(getLastTradedayName()), Integer.parseInt(getLastTradedayName()));

		if (null == quoteList) {
			// System.out.println("delete symbol " + symbol);
			// removeSymbol.add(symbol);
			return true;
		}

		if (quoteList.getQuote(quoteList.getSize() - 1).getClose() < VVIPManager.rangeStartPrice || VVIPManager.rangeEndPrice < quoteList.getQuote(quoteList.getSize() - 1).getClose()) {
			return false;
		}
		return true;
	}

	public static int getYesterDayPrice(String symbol) {
		QuoteList quoteList = DatabaseManager.selectLimitQuoteListBySymbol(symbol, 0, 50, true);
		if (quoteList == null) {
			return 0;
		}
		int dbSize = quoteList.getSize();
		// System.out.println("getYesterDayPrice: " + quoteList.getQuote(dbSize
		// - 1).getTradeDate().toInt() + " symbol: " +symbol + " price: "
		// +quoteList.getQuote(dbSize - 1).getClose());
		return (int) quoteList.getQuote(dbSize - 1).getClose();
	}

	public static int getBuyCount(int stockPrice) {
		// int stockPrice = getYesterDayPrice(symbol);
		// boolean isKospy = DatabaseManager.isKospySymbol(symbol);
		int maxPrice = (int) VVIPManager.buyPrice;
		int stepUnit = 1;
		// if (isKospy == true && stockPrice > 50000) {
		// stepUnit = 10;
		// } else {
		// stepUnit = 1;
		// }
		int buyCount = 0;
		int buyPrice = 0;
		while (true) {
			buyCount++;
			buyPrice = stockPrice * stepUnit * buyCount;
			if (buyPrice > maxPrice) {
				buyCount--;
				break;
			}
		}

		if ((buyPrice - maxPrice) < (stockPrice / 2)) {
			buyCount++;
		}
		return stepUnit * buyCount;
	}

	public static void writeToFile(String filePath, String message) {
		if (message != null) {
			message += "\n";
			FileWriter writer;
			try {
				writer = new FileWriter(filePath + ".txt", true);
				BufferedWriter bwriter = new BufferedWriter(writer);
				bwriter.write(message);
				bwriter.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static int getPecentPrice(double currentPrice, double percent) {

		int stepPirce = 1;
		if (currentPrice < 1000) {
			stepPirce = 1;
		} else if (currentPrice < 5000) {
			stepPirce = 5;
		} else if (currentPrice < 10000) {
			stepPirce = 10;
		} else if (currentPrice < 50000) {
			stepPirce = 50;
		} else if (currentPrice < 100000) {
			stepPirce = 100;
		} else if (currentPrice < 500000) {
			stepPirce = 500;
		} else if (currentPrice > 1000000) {
			stepPirce = 10000;
		}

		double buyPrice = currentPrice + ((currentPrice * percent) / 100);

		buyPrice = (buyPrice - (buyPrice % stepPirce)) + stepPirce;

		return (int) buyPrice;

	}

	public static List<String> readFromFile(String path) throws ImportException {
		ArrayList<String> dataList = new ArrayList<String>();
		FileReader dataFile = null;
		BufferedReader bufferedReader = null;
		try {
			dataFile = new FileReader(path);
			bufferedReader = new BufferedReader(dataFile);
			String line = bufferedReader.readLine();
			dataList.add(line);
			while (line != null) {
				line = bufferedReader.readLine();
				dataList.add(line);
			}
			bufferedReader.close();
			dataFile.close();
		} catch (Exception e) {
			throw new ImportException(e.getMessage());
		}
		return dataList;
	}

	public static void insertQuoteKorea() {

		System.out.println("insertQuoteKorea");
		ArrayList<Company> companyList = VVIPManager.getCompanyList();
		QuoteList quoteList = null;
		TechnicalAnalysis ta = null;
		int count = 0;
		int totalCount = 0;
		for (int i = 0; i < companyList.size(); i++) {
			System.out.println(i + 1 + "/" + companyList.size());
			try {
				String updateSymbol = companyList.get(i).getSymbol();
				if (!CommonUtil.isVaildSymbol(updateSymbol)) {
					continue;
				}
				totalCount++;
				quoteList = DatabaseManager.selectLimitQuoteListBySymbol(updateSymbol, 0, 50, true);

				int dbSize = 0;
				if (quoteList != null) {
					dbSize = quoteList.getSize();
				}

				TradeDate filterDate = null;
				if (dbSize != 0) {
					filterDate = quoteList.getQuote(dbSize - 1).getTradeDate();
				}

				if (dbSize < 250) {
					filterDate = null;
				}

				quoteList = ImportDaumQuote.importData(companyList.get(i).getSymbol(), filterDate);

				if (null == quoteList) {
					continue;
				}
				count++;
				System.out.println("updateSymbol : " + updateSymbol + " size : " + quoteList.getSize());

				ta = new TechnicalAnalysis(quoteList.getList(), 0);
				ta.setTaPattern();
				DatabaseManager.insertQuoteListToSymbol(quoteList, companyList.get(i).getSymbol());
			} catch (ImportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println((new Date()).toString() + " count : " + count + "/" + totalCount);
	}

	public static int getVaildSymbolCount() {
		int returnCount = 0;
		ArrayList<Company> companyList = VVIPManager.getCompanyList();
		for (int i = 0; i < companyList.size(); i++) {
			String symbol = companyList.get(i).getSymbol();
			if (!CommonUtil.isVaildSymbol(symbol)) {
				continue;
			}
			List<GeneResult> dbGRList = DatabaseManager.getGeneBySymbol(symbol);
			if (dbGRList.size() == 0) {
				continue;
			}
			returnCount++;
			System.out.println("Check Trade Symbol  : " + symbol + " Count : " + returnCount + " profit : " + dbGRList.get(0).getGene().getProfit());

		}
		return returnCount;
	}

	static boolean isPEROK(String symbol) {
		String URLString = "http://finance.naver.com/item/main.nhn?code=" + symbol;
		double per = 0;
		double perAll = 0;
		// boolean isTradeInfo = false;
		try {
			URL url = new URL(URLString);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			InputStreamReader input = new InputStreamReader(connection.getInputStream());

			BufferedReader bufferedInput = new BufferedReader(input);

			String line = null;
			while ((line = bufferedInput.readLine()) != null) {
				if (line.indexOf("<span class=\"f_up\"><em>") != -1) {
					// System.out.println(line);
					// isTradeInfo = true;
				}
				if (line.indexOf("</em>πË") != -1) {
					if (line.indexOf("em id=\"_per") != -1) {
						String[] a = line.split(">");
						String[] b = a[1].split("<");
						per = Double.parseDouble(b[0]);
					} else if (line.indexOf("id") == -1) {
						String[] a = line.split(">");
						String[] b = a[1].split("<");
						perAll = Double.parseDouble(b[0]);
					}
					// System.out.println(line);
				}
			}
			bufferedInput.close();
			input.close();

		} catch (Exception e) {

		}
		// System.out.println(perAll / per);
		double aa = perAll / per;
		if (aa > 2 && perAll > 0 && per > 0) {
			return true;
		} else {
			return false;
		}
		// System.out.println(URLString + " per: " + per + " perAll: " +
		// perAll);
		// return perAll / per;
	}

	static void getKospi500() {

		HashSet<String> symbolList = new HashSet<String>();
		
		for (int i = 1; i < 2; i++) {

			String URLString = "http://finance.daum.net/quote/marketvalue.daum?stype=P&page=" + String.valueOf(i) + "&col=listprice&order=desc";
			System.out.println(URLString);

			try {
				URL url = new URL(URLString);
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);

				InputStreamReader input = new InputStreamReader(connection.getInputStream());

				BufferedReader bufferedInput = new BufferedReader(input);

				String line = null;
				while ((line = bufferedInput.readLine()) != null) {
					 System.out.println(line);
					 
//					 <a href="/item/main.daum?code=005930" title="?ñº6,000|-0.31%" target="_top">?Çº?Ñ±?†Ñ?ûê</a>
					 
					if (line.indexOf("class=\"txt\"") != -1) {
						continue;
					}
					if (line.indexOf("linkVals") != -1) {
						continue;
					}
					
					
					if (line.indexOf("code=") != -1) {
						
						String temp = line.trim();
//						if (line.indexOf("code=") != -1) {
//							String[] a = line.split("code=");
//							String[] b = a[1].split("\"");
////							System.out.println(b[0]);
//							if(!b[0].isEmpty()){
//								symbolList.add(b[0]);
//							}
//							
//						}
//						System.out.println(temp);
						System.out.println(temp.substring(30, 36));
						symbolList.add(temp.substring(30, 36));
					}
				}
				bufferedInput.close();
				input.close();

			} catch (Exception e) {

			}
		}
		
		System.out.println(symbolList.size() + " " + symbolList);
	}

	static public String getSource(URL url) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		connection.setDoOutput(true);
		connection.setDoInput(true);
		try {
			connection.getOutputStream().write(42);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] bytes = new byte[512];
		try (BufferedInputStream bis = new BufferedInputStream(connection.getInputStream())) {
			StringBuilder response = new StringBuilder(500);
			int in;
			while ((in = bis.read(bytes)) != -1) {
				response.append(new String(bytes, 0, in));
				System.out.println(in);
			}
			return response.toString().split("\r\n\r\n")[1];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		return null;
	}
}
