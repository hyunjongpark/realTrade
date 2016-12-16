package com.vvip.init;

import java.util.ArrayList;
import java.util.List;

import com.vvip.ga.GAcalculateProfit;
import com.vvip.ga.GeneResult;
import com.vvip.quote.QuoteList;
import com.vvip.trade.TradeType;
import com.vvip.util.DatabaseManager;

public class TradeTodayMacdGene {
	static double totalProfit = 0;
	private static int TRADE_SYMBOL_COUNT = 500;
	public static int symbolCount = 0;

	class HOLD {
		String symbol = "";
		int endTime = 0;

		HOLD(String symbol, int endTime) {
			this.symbol = symbol;
			this.endTime = endTime;
		}
	}

	private static int findMacdGeneEndDate = 0;

	private SocketCommunicator socket = null;

	public TradeTodayMacdGene(SocketCommunicator socketCommunicator) {
		this.socket = socketCommunicator;
	}

	public static int getEndTradeDay() {
		return CommonUtil.getToday();
	}

	public void main(String[] args) {
		CommonUtil.checkPastMacdCount();
		checkHodingSymbol();
		findBuySymbol();
		findSellSymbol();
	}

	private List<TradeType> getCurrentHoldSymbol() {
		List<TradeType> returnList = new ArrayList<TradeType>();
		returnList = this.socket.getBankStock();
		return returnList;
	}

	private void checkHodingSymbol() {
		symbolCount = 0;
		totalProfit = 0;
		List<TradeType> tradeSymbolManager = getCurrentHoldSymbol();
		if (null == tradeSymbolManager) {
			return;
		}
		for (TradeType trade : tradeSymbolManager) {
			Integer isSuccess = tradeSymbol(trade.symbol, 20100101, CommonUtil.getlastDayOfPrice(), VVIPManager.checkProfit, VVIPManager.rangeStartPrice, VVIPManager.rangeEndPrice, GAcalculateProfit.HOLD);
			String statusName = "";
			if (isSuccess == GAcalculateProfit.BUY) {
				statusName = " BUY";
			} else if (isSuccess == GAcalculateProfit.SELL) {
				statusName = " SELL";
			} else if (isSuccess == GAcalculateProfit.HOLD) {
				statusName = " HOLD";
			} else if (isSuccess == GAcalculateProfit.READY) {
				statusName = " READY";
			} else {
				statusName = " FAILED";
			}
			CommonUtil.writeToFile(TradeType.trade_path, "HOLDING : " + trade.symbol + " status : " + statusName);
		}
	}

	private void findBuySymbol() {
		symbolCount = 0;
		totalProfit = 0;
		int size = VVIPManager.getCompanyList().size();
		int stockCount = 0;
		List<TradeType> tradeSymbolManager = getCurrentHoldSymbol();
		if (null == tradeSymbolManager) {
			return;
		}
		for (int i = 0; i < size; i++) {
			String symbol = VVIPManager.getCompanyList().get(i).getSymbol();
			boolean existBuySymbol = false;
			for (TradeType trade : tradeSymbolManager) {
				if (symbol.equals(trade.symbol)) {
					existBuySymbol = true;
					break;
				}
			}
			if (existBuySymbol) {
				continue;
			}
			Integer isSuccess = tradeSymbol(symbol, 20100101, CommonUtil.getlastDayOfPrice(), VVIPManager.checkProfit, VVIPManager.rangeStartPrice, VVIPManager.rangeEndPrice, GAcalculateProfit.BUY);
			if (isSuccess != -1 && isSuccess == GAcalculateProfit.BUY) {
				stockCount++;
			}
			if (stockCount > TRADE_SYMBOL_COUNT) {
				return;
			}
		}
	}

	private void findSellSymbol() {
		symbolCount = 0;
		totalProfit = 0;
		List<TradeType> tradeSymbolManager = getCurrentHoldSymbol();
		if (null == tradeSymbolManager) {
			return;
		}

		for (TradeType data : tradeSymbolManager) {
			tradeSymbol(data.symbol, 20100101, CommonUtil.getlastDayOfPrice(), VVIPManager.checkProfit, VVIPManager.rangeStartPrice, VVIPManager.rangeEndPrice, GAcalculateProfit.SELL);
		}
	}

	static public GeneResult findMatchingGene(String symbol, int tradeType) {
		List<GeneResult> dbGRList = DatabaseManager.getGeneBySymbol(symbol);
		if (dbGRList.size() == 0) {
			if (GAcalculateProfit.SELL == tradeType) {
				System.out.println("----ERROR not macdgne of hold symbol : " + symbol + " size == 0");
			}
			return null;
		}
		for (GeneResult gene : dbGRList) {
			if (gene.getStatus() == GAcalculateProfit.HOLD || gene.getStatus() == GAcalculateProfit.BUY || gene.getStatus() == GAcalculateProfit.SELL) {
				findMacdGeneEndDate = gene.getEnd().toInt();
				return gene;
			}

		}
		for (GeneResult gene : dbGRList) {
			if (gene.getStatus() == GAcalculateProfit.READY) {
				findMacdGeneEndDate = gene.getEnd().toInt();
				return gene;
			}

		}
		if (GAcalculateProfit.SELL == tradeType) {
			return null;
		}
		findMacdGeneEndDate = dbGRList.get(0).getEnd().toInt();
		return dbGRList.get(0);
	}

	public static Integer tradeSymbol(String symbol, int startDate, int endDate, double profit, double startPrice, double endPrice, int tradeType) {
		GeneResult dbGR = findMatchingGene("001820", tradeType);
		if (null == dbGR) {
//			System.out.println("	return : null == dbGR");
			return -1;
		}

		if (dbGR.getGene().getProfit() < profit) {
			return -1;
		}
		QuoteList quoteList = DatabaseManager.selectQuoteListByDate(symbol, startDate, endDate);
		if (null == quoteList) {
			return -1;
		}
		if (quoteList.getQuote(quoteList.getSize() - 1).getClose() < startPrice || endPrice < quoteList.getQuote(quoteList.getSize() - 1).getClose()) {
			return -1;
		}

		double close[] = new double[quoteList.getSize()];
		double high[] = new double[quoteList.getSize()];
		double low[] = new double[quoteList.getSize()];
		long volume[] = new long[quoteList.getSize()];
		int date[] = new int[quoteList.getSize()];
		for (int j = 0; j < quoteList.getSize(); j++) {
			close[j] = quoteList.getQuote(j).getClose();
			high[j] = quoteList.getQuote(j).getHigh();
			low[j] = quoteList.getQuote(j).getLow();
			date[j] = quoteList.getQuote(j).getTradeDate().toInt();
			volume[j] = quoteList.getQuote(j).getVolume();
		}

		GAcalculateProfit profitThread = new GAcalculateProfit(GAcalculateProfit.CAL_ALL, startDate, endDate, close,  high, low, date, volume, dbGR.getGene(), false, quoteList, false);

		profitThread.setSymbol(symbol);
		profitThread.run();

		double lastPrice = close[close.length - 1];

		if (profitThread.getStatus() == GAcalculateProfit.BUY) {
		}
		if (profitThread.getStatus() == GAcalculateProfit.SELL) {
		}

		totalProfit += profitThread.getProfit();
		if (profitThread.getProfit() != 0) {
			System.out.println("symbol : " + symbol + " profit : " + profitThread.getProfit() +" Total-profit : " + totalProfit + " lastPrice: " + lastPrice + " , " + profitThread.getStartDay() + " ~ " + profitThread.getEndDay());
			symbolCount++;
		}

//		DatabaseManager.updateGATable(symbol, profitThread.getStatus(), findMacdGeneEndDate);

		return profitThread.getStatus();
	}

	static int getBuyCount(double buyPrice) {
		return (int) VVIPManager.buyPrice / (int) buyPrice;
	}
}
