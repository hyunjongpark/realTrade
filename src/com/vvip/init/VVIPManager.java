package com.vvip.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vvip.data.ImportDaumQuote;
import com.vvip.data.ImportException;
import com.vvip.data.ImportSymbolList;
import com.vvip.ga.CreateMacdGeneData;
import com.vvip.quote.Company;
import com.vvip.quote.Quote;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.util.ChartPattern;
import com.vvip.util.DatabaseManager;
import com.vvip.util.TechnicalAnalysis;

public class VVIPManager {

	static List<SocketCommunicator> tradeList = new ArrayList<SocketCommunicator>();
	private static ArrayList<Company> kospiList = null;
	private static ArrayList<Company> kosdaqList = null;
	private static HashMap<String, Company> companyHashMap = new HashMap<String, Company>();

	// java -classpath C:\vvip\vvip_0615\hsqldb.jar
	// org.hsqldb.util.DatabaseManager -user sa -url
	// jdbc:hsqldb:C:\vvip\vvip_0615\db\vvip

	// java -classpath /home/phj/vvip/vvip_0615/hsqldb.jar
	// org.hsqldb.util.DatabaseManager -user sa -url
	// jdbc:hsqldb:/home/phj/vvip/vvip_0615/db/vvip

	public final static String VVIP_PATH = "C:/vvip/vvip_0615";
	// public final static String VVIP_PATH = "/home/phj/vvip/vvip_0615";

	public final static double checkMinusSellProfit = -10000;
	public final static double checkPlusSellProfit = 10000;
	public final static double checkProfit = 0;

	public static double rangeStartPrice = 2000;
	public static double rangeEndPrice = 200000;
	public static double buyPrice = 200000;
	public static double sellPercentByBuyPrice = 1.3;

	public static int startTradeDay = 20150101;
	public static int endTradeDay = 99999999;

	private final static int CASE_RunMode = 1;
	private final static int CASE_CREATE = 2;
	private final static int CASE_MACD_GENE = 3;
	private final static int CASE_TODAY_TRADE = 4;
	private final static int CASE_DELETE = 5;
	private final static int CASE_UPDATE_SYMBOL = 6;
	private final static int CASE_TEST_MAKE_GENE = 7;
	private final static int CASE_TEST_REAL_TIME_TRADE = 8;
	private final static int CASE_50_TRADE = 9;
	private final static int CASE_CHART = 10;

	private final static int selectedMode = CASE_RunMode;

	public static void main(String[] args) {
		switch (selectedMode) {
		case CASE_CREATE: {
			CreateDataBase.main(null);
			break;
		}
		case CASE_CHART: {
			ChartPattern chartPattern = ChartPattern.getInstance();
			for (int i = 0; i < VVIPManager.getCompanyList().size(); i++) {
				String symbol = VVIPManager.getCompanyList().get(i).getSymbol();
				QuoteList quoteList = DatabaseManager.selectLimitQuoteListBySymbol(symbol, 0, 50, true);
				Quote q = quoteList.getQuote(quoteList.getSize() - 1);

				String pattern = chartPattern.convertPattern(quoteList.getQuote(quoteList.getSize() - 2).getPattern());
				if (pattern.contains("P@Engulfing Pattern##")) {
					System.out.println("symbol: " + symbol + " date: " + q.getTradeDate().toInt() + " " + chartPattern.convertPattern(q.getPattern()) + " 2: "
							+ quoteList.getQuote(quoteList.getSize() - 2).getClose() + " 1: " + q.getClose());
				}

			}
			break;
		}
		case CASE_RunMode: {
//			 UpdateKoreaQuote.main(null);
			ServerManager.startVVIPManagerThread();
			break;
		}
		case CASE_MACD_GENE: {
			CommonUtil.getVaildSymbolCount();
			RunMacdGeneData macdgene = new RunMacdGeneData();
			macdgene.startVVIPManagerThread();
			break;
		}
		case CASE_TEST_MAKE_GENE: {
			int startDate = 20150101;
			int endDate = 99999999;
			int limitQuoteCount = 250;
			for (int i = 0; i < VVIPManager.getCompanyList().size(); i++) {
				String symbol = VVIPManager.getCompanyList().get(i).getSymbol();
				CreateMacdGeneData macdGene = new CreateMacdGeneData(new ArrayList<String>());
				macdGene.setGene(symbol, startDate, endDate, limitQuoteCount);
			}

			// List<String> testList = new ArrayList<String>();
			// testList.add("122630");// kodex R
			// testList.add("114800");// kodex In
			// testList.add("069500");// kodex 200
			//
			// // testList.add("229200");//kosdaq 150
			// // testList.add("233740");//kosdaq 150 R
			// // testList.add("226490");//kodex kospi
			// for (String symbol : testList) {
			// CreateMacdGeneData macdGene = new CreateMacdGeneData(new
			// ArrayList<String>());
			// macdGene.setGene(symbol, startDate, endDate, limitQuoteCount);
			// }

			break;
		}

		case CASE_TODAY_TRADE: {
			int nCount = 0;
			TradeTodayMacdGene.totalProfit = 0;

			boolean isTest = false;

			if (isTest) {
				List<String> testList = new ArrayList<String>();
				testList.add("028040");
				for (String symbol : testList) {
					TradeTodayMacdGene.tradeSymbol(symbol, VVIPManager.startTradeDay, 99999999, VVIPManager.checkProfit, rangeStartPrice, rangeEndPrice, 1);
				}
			} else {
				for (int i = 0; i < VVIPManager.getCompanyList().size(); i++) {
					String symbol = VVIPManager.getCompanyList().get(i).getSymbol();
					// ArrayList<GeneResult> grList =
					// DatabaseManager.selectGeneResultListByIndex();
					// for (GeneResult gene : grList) {
					// String symbol = gene.getSymbol();
					TradeTodayMacdGene.tradeSymbol(symbol, VVIPManager.startTradeDay, 99999999, VVIPManager.checkProfit, rangeStartPrice, rangeEndPrice, 1);
					nCount++;
				}
			}
			System.out.println("total count : " + nCount + " vaildCount : " + TradeTodayMacdGene.symbolCount + " total profit : " + TradeTodayMacdGene.totalProfit + " averge : "
					+ TradeTodayMacdGene.totalProfit / TradeTodayMacdGene.symbolCount);
			break;
		}
		case CASE_DELETE: {
			int today = CommonUtil.getToday();
			for (int i = 0; i < getCompanyList().size(); i++) {
				String symbol = getCompanyList().get(i).getSymbol();
				DatabaseManager.deleteQuote(symbol, CommonUtil.getDayIntByWeek(today, 1), 99999999);
				System.out.println(i + "/" + getCompanyList().get(i).getSymbol() + " symbol: " + symbol + " delete");
				// DatabaseManager.dropSymbolTable(symbol);
			}
			// SocketCommunicator trade = new SocketCommunicator();
			// trade.settingNextDayTradeCandidate();
			break;
		}
		case CASE_UPDATE_SYMBOL: {
			// http://marketdata.krx/m1/m1_1/m1_1_5/JHPKOR01001_05.jsp
			// kospi 200:
			// http://marketdata.krx.co.kr/contents/MKD/02/0203/02030101/MKD02030101.jsp
			// CommonUtil.insertQuoteKorea();

			List<String> testList = new ArrayList<String>();
			testList.add("122630");// kodex R
			testList.add("069500");// kodex 200
			testList.add("114800");// kodex In
			testList.add("229200");// kosdaq 150
			testList.add("233740");// kosdaq 150 R
			testList.add("226490");// kodex kospi
			for (String symbol : testList) {
				// DatabaseManager.createSymbolTable(symbol);
				QuoteList quoteList;
				try {
					quoteList = DatabaseManager.selectLimitQuoteListBySymbol(symbol, 0, 50, true);

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

					quoteList = ImportDaumQuote.importData(symbol, filterDate);
					System.out.println("updateSymbol : " + symbol + " size : " + quoteList.getSize());

					TechnicalAnalysis ta = new TechnicalAnalysis(quoteList.getList(), 0);
					ta.setTaPattern();
					DatabaseManager.insertQuoteListToSymbol(quoteList, symbol);

				} catch (ImportException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		}
		case CASE_TEST_REAL_TIME_TRADE: {
			SocketCommunicator trade = new SocketCommunicator();
			trade.realTimeTradeManagerByVolumn();
			break;
		}
		case CASE_50_TRADE: {
			UpdateKoreaQuote.main(null);
			break;
		}
		}

	}

	public static String getVVIP_PATH() {
		return VVIP_PATH;
	}

	public static void initCompayInfo() {
		for (int i = 0; i < getCompanyList().size(); i++) {
			companyHashMap.put(getCompanyList().get(i).getSymbol(), getCompanyList().get(i));
		}
	}

	public static Company getCompany(String symbol) {
		if (companyHashMap.size() < 1) {
			initCompayInfo();
		}
		return companyHashMap.get(symbol);
	}

	public static void setInsertKospiList() {
		try {
			kospiList = ImportSymbolList.importKospiList();
			if (null != kospiList) {
				DatabaseManager.insertCompanyTable(kospiList, 1);
			}
		} catch (ImportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setInsertkosdaqiList() {
		try {
			kosdaqList = ImportSymbolList.importKosdaqList();
			if (null != kosdaqList) {
				DatabaseManager.insertCompanyTable(kosdaqList, 2);
			}
		} catch (ImportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<Company> getCompanyList() {
		return DatabaseManager.selectCompanyTable();
	}
}
