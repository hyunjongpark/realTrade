package com.vvip.ga;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import com.vvip.init.CommonUtil;
import com.vvip.init.VVIPManager;
import com.vvip.quote.QuoteList;
import com.vvip.trade.TradeType;
import com.vvip.util.DatabaseManager;

public class CreateMacdGeneData implements Runnable {

	public static int totalThreadColunt = 5;
	public static int threadCount = 0;
	private int currentThreadIndex = 0;
	public static int intervalDay = 15;
	int insertProfit = 6;
	private List<String> tradeSymbolList = null;

	private String currentTradeSymbol = "";
	private int sameSymbolTradeCount = 1;
	
	private int tryMakeMacdGeneDataCount = 0;
	
	private int limitQuoteCount = 250;

	public CreateMacdGeneData(List<String> tradeSymbolList) {
		this.tradeSymbolList = tradeSymbolList;
	}

	public void addMacdGene(int thrthreadCount) {
		System.out.println("thrthreadCount : " + thrthreadCount);
		try {
			int lastSize = VVIPManager.getCompanyList().size();
			int stepCount = lastSize / totalThreadColunt;
			int startIndex = thrthreadCount * stepCount;
			int endIndex = startIndex + stepCount - 1;
			int yesterDay = Integer.parseInt(CommonUtil.getLastTradedayName());
			for (int i = startIndex; i < endIndex; i++) {
				insertProfit = 6;
				String symbol = VVIPManager.getCompanyList().get(i).getSymbol();
				QuoteList quoteList = DatabaseManager.selectQuoteListByDate(symbol, yesterDay, yesterDay);
				if (null == quoteList) {
					continue;
				}
				if (quoteList.getQuote(quoteList.getSize() - 1).getClose() < VVIPManager.rangeStartPrice || VVIPManager.rangeEndPrice < quoteList.getQuote(quoteList.getSize() - 1).getClose()) {
					continue;
				}
				System.out.println(i + "/" + VVIPManager.getCompanyList().size() + " company : " + VVIPManager.getCompanyList().get(i).getNameInEng() + " symbol : " + VVIPManager.getCompanyList().get(i).getSymbol());
				if (!CommonUtil.isPastMacdEndTime(symbol)) {
					if (currentTradeSymbol.equals(symbol)) {
						sameSymbolTradeCount++;
					} else {
						sameSymbolTradeCount = 1;
					}
					currentTradeSymbol = symbol;
					setGene(symbol, VVIPManager.startTradeDay, VVIPManager.endTradeDay, limitQuoteCount);
				}
			}
			System.out.println("end startIndex : " + startIndex + " endIndex : " + endIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}

	public void setGene(String symbol, int startDate, int endDate, int limitQuoteCount) {
		Calendar cal = new GregorianCalendar();
		int mHour = cal.get(Calendar.HOUR_OF_DAY);
		int mMinute = cal.get(Calendar.MINUTE);
		System.out.println("index : " + currentThreadIndex + " Symbol : " + symbol + " tryCount : " + sameSymbolTradeCount + " mHour : " + mHour + " mMinute : " + mMinute);
		try {
			QuoteList quoteList = DatabaseManager.selectQuoteListByDate(symbol, startDate, endDate);
			if (null != quoteList) {
				QuoteList limiteQuoteList = new QuoteList(symbol);
				int startIndex = 0;
				if (quoteList.getSize() > limitQuoteCount) {
					startIndex = quoteList.getSize() - limitQuoteCount;
				}

				for (int i = startIndex; i < quoteList.getSize(); i++) {
					limiteQuoteList.addQuote(quoteList.getQuote(i));
				}
				List<Gene> macdGene = getBestGene(symbol, limiteQuoteList, limitQuoteCount);
				if (macdGene != null) {
					List<Gene> bestMacdGene = checkProfitOfLatMonth(macdGene, symbol, limiteQuoteList);
//					if (bestMacdGene != null) {
//						insertGA(bestMacdGene, symbol, quoteList, 0);
//					}
					insertGA(macdGene, symbol, quoteList, 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Gene> getBestGene(String symbol, QuoteList quoteList, int limitQuoteCount) {
		List<Gene> bestMacdGene = new ArrayList<Gene>();
		try {
			if ((null == quoteList) || (quoteList.getSize() < limitQuoteCount)) {
				System.out.println("Failed symbol : " + symbol + " quoteList.getSize() < " + limitQuoteCount + ": ");
				return null;
			}
			int size = 1;

			List<GeneticAlgorithm> gaList = new ArrayList<GeneticAlgorithm>();
			for (int i = 0; i < size; i++) {
				GeneticAlgorithm gaC = new GeneticAlgorithm(symbol, quoteList.getSize() * 20, 30, 0.9, 0.005, quoteList );
				gaC.startVVIPManagerThread(gaC, i);
				gaList.add(gaC);
			}

			int isEndAllThreadCount = 0;

			while (true) {
				for (int i = 0; i < size; i++) {
					List<Gene> resultGene = gaList.get(i).getResultGene();
					isEndAllThreadCount++;
					bestMacdGene.addAll(resultGene);
				}
				if (isEndAllThreadCount >= size) {
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(bestMacdGene);
		if (bestMacdGene.size() > 0) {
			System.out.println("0 Company : " + symbol + " MACD : " + bestMacdGene.get(0).toString() + " profit : " + bestMacdGene.get(0).getProfit() + " startDate : " + quoteList.getQuote(0).getTradeDate().toInt() + " endDate : " + quoteList.getQuote(quoteList.getSize() - 1).getTradeDate().toInt());
		}
		return bestMacdGene;
	}

	private List<Gene> checkProfitOfLatMonth(List<Gene> macdGenes, String symbol, QuoteList quoteList) {
		List<Gene> returnGene = new ArrayList<Gene>();
		if (null == quoteList) {
			return  null;
		}

		double close[];
		double high[];
		double low[];
		int date[];

		close = new double[quoteList.getSize()];
		high = new double[quoteList.getSize()];
		low = new double[quoteList.getSize()];
		date = new int[quoteList.getSize()];
		long volume[] = new long[quoteList.getSize()];
		for (int i = 0; i < quoteList.getSize(); i++) {
			close[i] = quoteList.getQuote(i).getClose();
			high[i] = quoteList.getQuote(i).getHigh();
			low[i] = quoteList.getQuote(i).getLow();
			date[i] = quoteList.getQuote(i).getTradeDate().toInt();
			volume[i] = quoteList.getQuote(i).getVolume();
		}

		int startDayForProfitCal = quoteList.getQuote(quoteList.getSize() - 80).getTradeDate().toInt();
		int endDayForProfitCal = quoteList.getQuote(quoteList.getSize() - 1).getTradeDate().toInt();

		for (int i = 0; i < macdGenes.size(); i++) {
			GAcalculateProfit profitThread = new GAcalculateProfit(GAcalculateProfit.CAL_ALL, startDayForProfitCal, endDayForProfitCal, close, high, low, date, volume, macdGenes.get(i), false, quoteList, false);
			profitThread.setSymbol(symbol);
			profitThread.run();
			if (profitThread.getProfit() > 0) {
				macdGenes.get(i).setProfit(profitThread.getProfit());
	//			System.out.println(profitThread.getProfit() + " " + startDayForProfitCal + " ~ " + endDayForProfitCal);
				returnGene.add(macdGenes.get(i));
			}
		}

		Collections.sort(returnGene);

		if (returnGene.size() == 0) {
			if (tryMakeMacdGeneDataCount > 5) {
				GeneResult gr = new GeneResult(symbol, new Gene(0, 0, 0, 0, 0, 0), 0, quoteList.getQuote(0).getTradeDate(), quoteList.getQuote(quoteList.getSize() - 1).getTradeDate());
				gr.getGene().setProfit(-100);
				DatabaseManager.insertGene(gr);
			}
			tryMakeMacdGeneDataCount++;
			if (currentTradeSymbol.equals(symbol)) {
				sameSymbolTradeCount++;
			} else {
				sameSymbolTradeCount = 1;
			}
			currentTradeSymbol = symbol;
			setGene(symbol, quoteList.getQuote(0).getTradeDate().toInt(), endDayForProfitCal, limitQuoteCount);
		} else if (returnGene.get(0).getProfit() < insertProfit) {
			insertProfit -= 2;
			if (currentTradeSymbol.equals(symbol)) {
				sameSymbolTradeCount++;
			} else {
				sameSymbolTradeCount = 1;
			}
			currentTradeSymbol = symbol;
			setGene(symbol, quoteList.getQuote(0).getTradeDate().toInt(), endDayForProfitCal, limitQuoteCount);
		}

		if (returnGene.size() > 0){
			System.out.println("1 Company : " + symbol + " MACD : " + returnGene.get(0).toString() + " profit : " + returnGene.get(0).getProfit() + " startDate : " + startDayForProfitCal + " endDate : " + endDayForProfitCal);
		}
		return returnGene;
	}

	public void insertGA(List<Gene> resultGene, String symbol, QuoteList quoteList, int type) {
		deleteMacd(symbol);
		int maxInsertCount = 1;
		if (resultGene.size() < maxInsertCount) {
			maxInsertCount = resultGene.size();
		}
		for (int i = 0; i < maxInsertCount; i++) {
			Gene data = resultGene.get(i);
			if (data != null) {
				GeneResult gr = new GeneResult(symbol, data, 0, quoteList.getQuote(0).getTradeDate(), quoteList.getQuote(quoteList.getSize() - 1).getTradeDate());
				if (gr.getGene().getProfit() > 0) {
					DatabaseManager.deleteGATable(symbol);
					DatabaseManager.insertGene(gr);
				//	CommonUtil.writeToFile(TradeType.trade_path, "[insert macdgene] Symbol : " + gr.getSymbol() + " profit : " + gr.getGene().getProfit() + "\n");
				}
			}

		}
	}

	private void deleteMacd(String symbol) {
		List<GeneResult> dbGRList = DatabaseManager.getGeneBySymbol(symbol);
		if (dbGRList.size() > 10) {
			for (int i = 10; i < dbGRList.size(); i++) {
				GeneResult gene = dbGRList.get(i);
				DatabaseManager.deleteGATable(symbol, gene.getEnd().toInt());
			}
		}

	}

	@Override
	public void run() {
		currentThreadIndex = threadCount;
		if (null == this.tradeSymbolList) {
			addMacdGene(threadCount++);
		} else {
			for (int i = 0 ; i < 200 ; i++){
//			for (String symbol : this.tradeSymbolList) {
				String symbol = this.tradeSymbolList.get(i);
				setGene(symbol, VVIPManager.startTradeDay, VVIPManager.endTradeDay, limitQuoteCount);
			}
		}
	}

}