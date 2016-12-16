package com.vvip.trade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.vvip.init.CommonUtil;
import com.vvip.init.VVIPManager;

public class TradeType {
	public static String tomorrowTrade_path = VVIPManager.getVVIP_PATH() + "/tomorrowTrade/tomorrowTrade.txt";
	public static String tomorrowTrade_copy_path = VVIPManager.getVVIP_PATH() + "/tomorrowTrade/tomorrowTrade_"+CommonUtil.getTodayName()+".txt";
	public static String trade_path = VVIPManager.getVVIP_PATH() + "/buy/" + "buy_" + CommonUtil.getTodayName() + "_trade.txt";
	public static String buy_path = VVIPManager.getVVIP_PATH() + "/buy/" + "buy_" + CommonUtil.getTodayName() + ".txt";
	public static String BUY_TRUE = "BUY_TRUE";
	public static String FIRST_DOWN_PRICE = "FIRST_DOWN_PRICE";
	public static String SELL_TRUE = "SELL_TRUE";

	public String symbol = "";
	public double tradeStartPrice = 0;
	public double openPrice = 0;
	public double preDayClosePrice = 0;
	public double currentPrice = 1;
	public double buyPrice = 0;
	public int buyHour = 0;
	public int buyMinute = 0;

	public boolean isFirstDownPrice = false;

	public boolean isBuy = false;
	public boolean isSell = false;
	public boolean isMinusBuy = false;
	public final static double quotePrice = 30000;
	public double minPrice = 10000000;

	public int buyStatusCount = 0;
	public int sellPlusProfitStatusCount = 0;
	public int sellMinusProfitStatusCount = 0;

	private double maxPriceAfterBuy = 0;
	private double minPriceAfterBuy = 10000000;

	public int buyCount = 0;
	public double profit = 0;

	private int againCheckTimeForTrade = 5;

	public TradeType(String symbol, int preDayClosePrice) {
		this.symbol = symbol;
		this.preDayClosePrice = preDayClosePrice;
	}

	
	public boolean isBuyStatue() {
		tradeStartPrice = preDayClosePrice >= openPrice ? openPrice : preDayClosePrice;

		Calendar cal = new GregorianCalendar();
		int mHour = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);

		double firstChangePercentPrice = Math.round(tradeStartPrice * 0.017);
		double firstCheckConditionPrice = tradeStartPrice - firstChangePercentPrice;

		double sencondChangePercentPrice = Math.round(minPrice * 0.003);
		double sencondCheckConditionPrice = minPrice + sencondChangePercentPrice;

		if (isBuy == false && isFirstDownPrice == false && firstCheckConditionPrice > currentPrice) {
			CommonUtil.writeToFile(buy_path, symbol + ";" + FIRST_DOWN_PRICE + ";" + mHour + ";" + mm + ";" + firstCheckConditionPrice);
			isFirstDownPrice = true;
		}
		if (minPrice > currentPrice) {
			buyStatusCount = 0;
		}

		// System.out.println("isBuy : "+isBuy+" sencondCheckConditionPrice : "+sencondCheckConditionPrice+" currentPrice : "+currentPrice+" firstCheckConditionPrice : "+firstCheckConditionPrice);
		if (isBuy == false && sencondCheckConditionPrice <= currentPrice && firstCheckConditionPrice >= currentPrice) {
			System.out.println("symbol : " + symbol + "mHour : " + mHour + " mm : " + mm + " buyStatusCount : " + buyStatusCount + " againCheckTimeForTrade : " + againCheckTimeForTrade);
			System.out.println("sencondCheckConditionPrice : " + sencondCheckConditionPrice + " firstCheckConditionPrice : " + firstCheckConditionPrice + " currentPrice : " + currentPrice);
			CommonUtil.writeToFile(trade_path, "symbol : " + symbol + "mHour : " + mHour + " mm : " + mm + " buyStatusCount : " + buyStatusCount + " againCheckTimeForTrade : "
					+ againCheckTimeForTrade);
			CommonUtil.writeToFile(trade_path, "sencondCheckConditionPrice : " + sencondCheckConditionPrice + " firstCheckConditionPrice : " + firstCheckConditionPrice + " currentPrice : "
					+ currentPrice);
			buyStatusCount++;
			if (buyStatusCount < againCheckTimeForTrade) {
				return false;
			}

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
			buyCount = (int) (quotePrice / currentPrice);
			int minusCount = (int) (buyCount % stepPirce);

			if (buyCount > minusCount) {
				buyCount = buyCount - minusCount;
			}

			CommonUtil.writeToFile(buy_path, symbol + ";" + BUY_TRUE + ";" + currentPrice + ";" + buyCount + ";" + mHour + ";" + mm);
			System.out.println("BUY symbol : " + symbol + " price : " + currentPrice + " buyCount : " + buyCount + " time : " + mHour + ":" + mm);
			buyHour = mHour;
			buyMinute = mm;
			buyPrice = currentPrice;
			isBuy = true;
			maxPriceAfterBuy = buyPrice;
			return true;
		}

		if (minPrice > currentPrice) {
			minPrice = currentPrice;

		}

		return false;
	}

	public boolean isBuyMinusStatue() {
		
		if (buyPrice == 0) {
			return false;
		}

		if (isBuy == false || isSell == true || isMinusBuy == true) {
			return false;
		}

		if (minPriceAfterBuy > currentPrice) {
			minPriceAfterBuy = currentPrice;
		}

		if (currentPrice >= buyPrice - Math.round(buyPrice * 0.015)) {
			return false;
		}

		if (currentPrice <= (minPriceAfterBuy + Math.round(minPriceAfterBuy * 0.003))) {
			return false;
		}

		sellMinusProfitStatusCount++;
		if (sellMinusProfitStatusCount > againCheckTimeForTrade) {
			isMinusBuy = true;
			return true;
		}
		return false;

	}

	public boolean isSellPlusStatue() {
		if (buyPrice == 0) {
			return false;
		}

		if (isBuy == false || isSell == true) {
			return false;
		}

		if (maxPriceAfterBuy < currentPrice) {
			maxPriceAfterBuy = currentPrice;
		}

		if (currentPrice <= buyPrice + Math.round(buyPrice * 0.02)) {
			return false;
		}

		if (currentPrice >= (maxPriceAfterBuy - Math.round(maxPriceAfterBuy * 0.003))) {
			return false;
		}

		sellPlusProfitStatusCount++;
		if (sellPlusProfitStatusCount > againCheckTimeForTrade) {
			isSell = true;
			return true;
		}

		return false;

	}

	public static void setTradeTypeFromFile() {
		File checkExistFile = new File(buy_path);
		if (!checkExistFile.exists()) {
			return;
		}
		FileReader dataFile = null;
		BufferedReader bufferedReader = null;
		try {
			dataFile = new FileReader(buy_path);
			bufferedReader = new BufferedReader(dataFile);
			while (true) {
				String line = bufferedReader.readLine();
				if (null == line) {
					break;
				}
				if (line.contains(";")) {
					String[] data = line.split(";");

					TradeType tradeType = null;
					for (int i = 0; i < TradeTypeManager.tradeSymbolManager.size(); i++) {
						if (TradeTypeManager.tradeSymbolManager.get(i).symbol.equals(data[0])) {
							tradeType = TradeTypeManager.tradeSymbolManager.get(i);
							break;
						}
					}
					if (line.contains(TradeType.BUY_TRUE)) {
						tradeType.isBuy = true;
						tradeType.buyPrice = Double.parseDouble(data[2]);
						tradeType.buyCount = Integer.parseInt(data[3]);
						tradeType.buyHour = Integer.parseInt(data[4]);
						tradeType.buyMinute = Integer.parseInt(data[5]);

					} else if (line.contains(TradeType.FIRST_DOWN_PRICE)) {
						tradeType.isFirstDownPrice = true;
					} else if (line.contains(TradeType.SELL_TRUE)) {
						tradeType.isSell = true;
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setBuyCount(int count){
		buyCount =count;
	}
	
	public double getProfit(){
		return profit;
	}
	public void setProfit(double profit){
		this.profit =  profit;
	}

}
