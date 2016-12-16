package com.vvip.ga;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.vvip.init.CommonUtil;
import com.vvip.init.VVIPManager;
import com.vvip.quote.QuoteList;
import com.vvip.trade.TradeType;

public class GAcalculateProfit {

	public static boolean printDebug = false;

	static public final double DAY_OF_RISK_FREE_RATE = 0.035 / 365;
	static public final double SELL_CHARGE = 0.00015;
	static public final double BUY_CHARGE = 0.00015;
	static public final double TAX_RATE = 0.003;
	static final double SELECTION_PRESSURE = 4;
	static final int GENE_SIZE = 48;
	static public final int BUY = 1;
	static public final int SELL = 2;
	static public final int HOLD = 3;
	static public final int READY = 4;

	double high[];
	double low[];
	double close[];
	long volume[];
	int date[];
	int status;
	double startTradePrice = 0;
	String symbol = "";

	int shortMA;
	int longMA;
	int macdMA;
	int fastK;
	int slowK;
	int slowD;

	double profit = 0;

	boolean isRealTimeTrade;

	int startTradeDay = 0;
	int endTradeDay = 0;
	int startQuotaDay = 0;
	int endQuotaDay = 0;

	boolean isAreadyBuy = false;

	QuoteList quoteList = null;

	static public final int CAL_ALL = 1;
	static public final int CAL_STOCH = 2;
	static public final int CAL_MACD = 3;
	int calculateType = 0; // 1: calculateStochProfit, 2:calculateMacdProfit, 3:
							// all

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public GAcalculateProfit(int calculateType, int profitStartDay, int endStartDay, double close[], double high[], double low[], int date[], long volume[], Gene gene, boolean isAreadyBuy,
			QuoteList quoteList, boolean isRealTimeTrade) {
		this.startQuotaDay = profitStartDay;
		this.endQuotaDay = endStartDay;
		this.startTradeDay = 0;
		this.endTradeDay = 0;
		this.close = close;
		this.date = date;
		this.high = high;
		this.low = low;
		this.shortMA = gene.getShortMA();
		this.longMA = gene.getLongMA();
		this.macdMA = gene.getMacdMA();
		this.fastK = gene.getFastK();
		this.slowK = gene.getSlowK();
		this.slowD = gene.getSlowD();

		this.volume = volume;
		this.isAreadyBuy = isAreadyBuy;
		this.quoteList = quoteList;
		this.calculateType = calculateType;
		this.isRealTimeTrade = isRealTimeTrade;
	}

	public void run() {
		double macdProfit = 0.0;
		if (calculateType == CAL_ALL) {
			ArrayList<Double> stochKArray = new ArrayList<Double>();
			ArrayList<Double> stochDArray = new ArrayList<Double>();
			getStochData(fastK, slowK, slowD, stochKArray, stochDArray);

			ArrayList<Double> macdArray = new ArrayList<Double>();
			ArrayList<Double> signalArray = new ArrayList<Double>();
			getMacdData(shortMA, longMA, macdMA, macdArray, signalArray);

			macdProfit = calculateProfit(stochKArray, stochDArray, macdArray, signalArray, isRealTimeTrade);

		} else if (calculateType == CAL_STOCH) {
			ArrayList<Double> stochKArray = new ArrayList<Double>();
			ArrayList<Double> stochDArray = new ArrayList<Double>();
			getStochData(fastK, slowK, slowD, stochKArray, stochDArray);
			macdProfit = calculateStochProfit(stochKArray, stochDArray);
		} else if (calculateType == CAL_MACD) {
			ArrayList<Double> macdArray = new ArrayList<Double>();
			ArrayList<Double> signalArray = new ArrayList<Double>();
			getMacdData(shortMA, longMA, macdMA, macdArray, signalArray);
			macdProfit = calculateMacdProfit(macdArray, signalArray);
		}
		profit = macdProfit;
	}

	public int getStatus() {
		return status;
	}

	private void getStochData(int fastK, int slowK, int slowD, ArrayList<Double> stochKArray, ArrayList<Double> stochDArray) {
		Core ta = new Core();
		MInteger stcIdx = new MInteger();
		MInteger stcElement = new MInteger();
		double[] stochK = new double[close.length];
		double[] stochD = new double[close.length];
		ta.stoch(0, close.length - 1, high, low, close, fastK, slowK, MAType.Sma, slowD, MAType.Sma, stcIdx, stcElement, stochK, stochD);

		for (int i = 0; i < stcIdx.value; i++) {
			stochKArray.add(0.0);
			stochDArray.add(0.0);
		}

		for (int i = 0; i < stcElement.value; i++) {
			stochKArray.add(Double.parseDouble(String.format("%.2f", stochK[i])));
			stochDArray.add(Double.parseDouble(String.format("%.2f", stochD[i])));
		}
	}

	public double calculateStochProfit(ArrayList<Double> stochKArray, ArrayList<Double> stochDArray) {
		StringBuffer profitPrint = new StringBuffer();
		profitPrint.append("STOCH:" + date[0] + " ~ " + shortMA + " longMA : " + longMA + " macdMA : " + macdMA);

		double preBuyPrice = 0;
		double mscdProfit = 0;
		int stochStatus = READY;

		if (close.length != stochKArray.size()) {
			return 0.0;
		}

		for (int t = 1; t <= close.length - 1; t++) {
			int tradeDay = date[t];
			double closePrice = close[t];
			// System.out.println("stoch tradeDay: " + tradeDay + " stochK: " +
			// stochK[stochIndex] +" stochIndex: " +stochIndex);
			if (stochKArray.get(t - 1) < stochDArray.get(t - 1) && stochKArray.get(t) > stochDArray.get(t)

			// //stochK[stochIndex] < 10.0
			// && stochD[stochIndex] < 10.0
			// && stochK[stochIndex -1] < stochD[stochIndex -1]
			// && stochK[stochIndex] > stochD[stochIndex]
					&& stochStatus == READY) {
				stochStatus = BUY;

				preBuyPrice = (closePrice + closePrice * BUY_CHARGE);
				startTradePrice = closePrice;

				profitPrint.append("\n	STOCH : " + tradeDay + " BUY : " + closePrice + " profit : " + profitToPercentage(mscdProfit) + " preBuyPrice : " + preBuyPrice);

			} else if (stochKArray.get(t - 1) > stochDArray.get(t - 1) && stochKArray.get(t) < stochDArray.get(t)
			// stochK[stochIndex] > 80.0 && stochD[stochIndex] > 80.0 &&
					&& (stochStatus == HOLD || stochStatus == BUY)) {

				double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
				double currentProfit = (sellPrice - preBuyPrice);
				mscdProfit += currentProfit;
				preBuyPrice = 0;

				stochStatus = SELL;

				profitPrint
						.append("\n	STOCH : " + tradeDay + " SELL : " + closePrice + " profit : " + profitToPercentage(currentProfit) + " sellPrice : " + sellPrice + " preBuyPrice : " + preBuyPrice);

			} else if (stochStatus == BUY) {
				stochStatus = HOLD;
			} else if (stochStatus == SELL) {
				stochStatus = READY;
			}

			if (stochStatus == HOLD) {
				double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
				double currentProfit = (sellPrice - preBuyPrice);
				profitPrint.append("\n	           day : " + tradeDay + " profit : " + profitToPercentage(currentProfit));
				if (profitToPercentage(currentProfit) < VVIPManager.checkMinusSellProfit || profitToPercentage(currentProfit) > VVIPManager.checkPlusSellProfit) {
					profitPrint.append("\n	SELL \n");
					mscdProfit += currentProfit;
					stochStatus = SELL;
				}
			}
		}

		if (preBuyPrice > 0 && (stochStatus == HOLD || stochStatus == BUY)) {
			double sellPrice = (close[close.length - 1] - close[close.length - 1] * SELL_CHARGE - close[close.length - 1] * TAX_RATE);
			double currentProfit = (sellPrice - preBuyPrice);
			mscdProfit += currentProfit;

			profitPrint.append("\nLast : " + "	STOCH : " + date[close.length - 1] + " SELL : " + close[close.length - 1] + " profit : " + profitToPercentage(mscdProfit) + " sellPrice : " + sellPrice
					+ " preBuyPrice : " + preBuyPrice);
		}

		if (mscdProfit != 0) {
			if (printDebug) {
				System.out.println(symbol + " --------------- calculator report : " + profitPrint.toString());
			}
			return profitToPercentage(mscdProfit);
		} else {
			return 0;
		}
	}

	private void getMacdData(int fastK, int slowK, int slowD, ArrayList<Double> macdArray, ArrayList<Double> signalArray) {
		Core ta = new Core();

		MInteger macdIdx = new MInteger();
		MInteger macdElement = new MInteger();
		double[] macd = new double[close.length];
		double[] signal = new double[close.length];
		ArrayList<Double> oscArray = new ArrayList<Double>();
		double[] osc = new double[close.length];
		ta.macd(0, close.length - 1, close, shortMA, longMA, macdMA, macdIdx, macdElement, macd, signal, osc);

		for (int i = 0; i < macdIdx.value; i++) {
			macdArray.add(0.0);
			signalArray.add(0.0);
			oscArray.add(0.0);
		}

		for (int i = 0; i < macdElement.value; i++) {
			macdArray.add(Double.parseDouble(String.format("%.2f", macd[i])));
			signalArray.add(Double.parseDouble(String.format("%.2f", signal[i])));
			oscArray.add(Double.parseDouble(String.format("%.2f", osc[i])));
		}
	}

	public double calculateMacdProfit(ArrayList<Double> macdArray, ArrayList<Double> signalArray) {
		StringBuffer profitPrint = new StringBuffer();
		profitPrint.append("MACD:" + date[0] + " ~ " + date[date.length - 1] + " Start shortMA : " + shortMA + " longMA : " + longMA + " macdMA : " + macdMA);

		double preBuyPrice = 0;
		double mscdProfit = 0;
		status = READY;

		if (close.length != signalArray.size()) {
			return 0.0;
		}

		for (int t = 1; t <= close.length - 1; t++) {
			int tradeDay = date[t];
			double closePrice = close[t];
			// System.out.println("macd tradeDay: " + tradeDay + " signal: " +
			// signal[macdIndex] +" oscIndex: " +macdIndex);

			if (signalArray.get(t - 1) > macdArray.get(t - 1) && signalArray.get(t) < macdArray.get(t)
			// && macd[macdIndex - 1] < macd[macdIndex] && signal[macdIndex - 1]
			// < signal[macdIndex]

			// if (signal[macdIndex] < macd[macdIndex] && volume[t - 1] <
			// volume[t] && macd[macdIndex - 1] < macd[macdIndex] &&
			// signal[macdIndex - 1] < signal[macdIndex] && macd[macdIndex - 1]
			// <= 0 && macd[macdIndex] >= 0
			// && macd[oscIndex] > 0
			// && signal[oscIndex] > 0
					&& status == READY) {
				status = BUY;

				preBuyPrice = (closePrice + closePrice * BUY_CHARGE);
				startTradePrice = closePrice;

				profitPrint.append("\n	MACD : " + tradeDay + " BUY : " + closePrice + " profit : " + profitToPercentage(mscdProfit) + " preBuyPrice : " + preBuyPrice);

			} else if (macdArray.get(t - 1) >= 0 && macdArray.get(t) <= 0 && (status == HOLD || status == BUY)) {
				status = SELL;

				double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
				double currentProfit = (sellPrice - preBuyPrice);
				mscdProfit += currentProfit;
				preBuyPrice = 0;
				status = SELL;
				profitPrint
						.append("\n	MACD : " + tradeDay + " SELL : " + closePrice + " profit : " + profitToPercentage(currentProfit) + " sellPrice : " + sellPrice + " preBuyPrice : " + preBuyPrice);
			} else if (status == BUY) {
				status = HOLD;
			} else if (status == SELL) {
				status = READY;
			}

			if (status == HOLD) {
				double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
				double currentProfit = (sellPrice - preBuyPrice);
				profitPrint.append("\n	           day : " + tradeDay + " profit : " + profitToPercentage(currentProfit));
				if (profitToPercentage(currentProfit) < VVIPManager.checkMinusSellProfit || profitToPercentage(currentProfit) > VVIPManager.checkPlusSellProfit) {
					profitPrint.append("\n	SELL \n");
					mscdProfit += currentProfit;
					status = SELL;
				}
			}
		}

		if (preBuyPrice > 0 && (status == HOLD || status == BUY)) {
			double sellPrice = (close[close.length - 1] - close[close.length - 1] * SELL_CHARGE - close[close.length - 1] * TAX_RATE);
			double currentProfit = (sellPrice - preBuyPrice);
			mscdProfit += currentProfit;

			profitPrint.append("\nLast : " + "	MACD : " + date[close.length - 1] + " SELL : " + close[close.length - 1] + " profit : " + profitToPercentage(mscdProfit) + " sellPrice : " + sellPrice
					+ " preBuyPrice : " + preBuyPrice);
		}

		if (mscdProfit != 0) {
			if (printDebug) {
				System.out.println(symbol + " --------------- calculator report : " + profitPrint.toString());
			}
			return profitToPercentage(mscdProfit);
		} else {
			return 0;
		}
	}

	public double calculateProfit(ArrayList<Double> stochKArray, ArrayList<Double> stochDArray, ArrayList<Double> macdArray, ArrayList<Double> signalArray, boolean isRealyTimeTrade) {
		StringBuffer profitPrint = new StringBuffer();
		profitPrint.append(date[0] + " ~ " + date[date.length - 1] + " Start shortMA : " + shortMA + " longMA : " + longMA + " macdMA : " + macdMA);

		// System.out.println("system: " +symbol +" day: " +
		// date[date.length-1]);
		double preBuyPrice = 0;
		double mscdProfit = 0;

		status = READY;

		Core ta = new Core();
		MInteger outBegIdx = new MInteger();
		MInteger outNBElement = new MInteger();
		double rsi[] = new double[close.length];
		ta.rsi(0, close.length - 1, close, 14, outBegIdx, outNBElement, rsi);

		ArrayList<Double> rsiArray = new ArrayList<Double>();
		for (int i = 0; i < outBegIdx.value; i++) {
			rsiArray.add(0.0);
		}
		for (int i = 0; i < outNBElement.value; i++) {
			rsiArray.add(Double.parseDouble(String.format("%.2f", rsi[i])));
		}

		double emaShort[] = new double[close.length];
		ta.sma(0, close.length - 1, close, 5, outBegIdx, outNBElement, emaShort);
		ArrayList<Double> emaShortArray = new ArrayList<Double>();
		for (int i = 0; i < outBegIdx.value; i++) {
			emaShortArray.add(0.0);
		}
		for (int i = 0; i < outNBElement.value; i++) {
			emaShortArray.add(Double.parseDouble(String.format("%.2f", emaShort[i])));
		}

		double emaLong[] = new double[close.length];
		ta.sma(0, close.length - 1, close, 60, outBegIdx, outNBElement, emaLong);
		ArrayList<Double> emaLongArray = new ArrayList<Double>();
		for (int i = 0; i < outBegIdx.value; i++) {
			emaLongArray.add(0.0);
		}
		for (int i = 0; i < outNBElement.value; i++) {
			emaLongArray.add(Double.parseDouble(String.format("%.2f", emaLong[i])));
		}

		if (close.length != signalArray.size() || close.length != stochKArray.size()) {
			System.out.println("close.length: " + close.length + " signalArray.size(): " + signalArray.size() + " stochKArray.size(): " + stochKArray.size());
			return 0.0;
		}

		List<Double> preBuyConditionPrice = new ArrayList<Double>();

		// int today = CommonUtil.getToday();
		// int yestdayPrice = CommonUtil.getYesterDayPrice(symbol);
		// System.out.println("symbol: " + symbol+ " yestdayPrice: " +
		// yestdayPrice);

		int buyCount = 0;
		for (int t = 3; t <= close.length - 1; t++) {

			int tradeDay = date[t];

			double closePrice = close[t];

			// String dataString = " day: " + tradeDay + " closePrice: " +
			// closePrice + " rsi: " + rsiArray.get(t) + " emaShort: " +
			// emaShortArray.get(t) + " emaLong: " + emaLongArray.get(t) + "
			// signal: " + signalArray.get(t) + " stochK: " + stochKArray.get(t)
			// + " macd: " +macdArray.get(t) + " stochD: " + stochDArray.get(t)
			// + " valume: " +volume[t];
			// System.out.println(dataString);

			if (emaShortArray.get(t) < 1 || emaLongArray.get(t) < 1) {
				continue;
			}

			if (startTradeDay == 0) {
				startTradeDay = tradeDay;
				endTradeDay = 0;
			}
			if (t == close.length - 1) {
				endTradeDay = tradeDay;
			}

			if (isAreadyBuy && t == close.length - 2) {
				status = BUY;
			}

			if (tradeDay < 20170101 && isRealyTimeTrade) {
				continue;
			}

			// System.out.println(closePrice + ":" + emaShortArray.get(t) + " :
			// " + emaLongArray.get(t));

			// String up = " up";
			// if (close[t] > close[t+1]){
			// up = " down";
			// }

			/*
			 * if (macdArray.get(t -2) < macdArray.get(t -1) && macdArray.get(t
			 * - 1) < macdArray.get(t)){ System.out.println(up +
			 * " macdArray.get(t -2) < macdArray.get(t -1) && macdArray.get(t - 1) < macdArray.get(t)"
			 * ); } if (macdArray.get(t -1) < signalArray.get(t -1) &&
			 * macdArray.get(t) > signalArray.get(t)){ System.out.println(up +
			 * " signalArray.get(t) < macdArray.get(t)"); } if
			 * (stochKArray.get(t -1) < stochDArray.get(t -1) &&
			 * stochKArray.get(t) > stochDArray.get(t)){ System.out.println(up +
			 * " stochKArray.get(t -1) < stochDArray.get(t -1) && stochKArray.get(t) > stochDArray.get(t)"
			 * ); } if (emaShortArray.get(t-1) < emaLongArray.get(t-1) &&
			 * emaShortArray.get(t) > emaLongArray.get(t)){
			 * System.out.println(up +
			 * " emaShortArray.get(t) > emaLongArray.get(t)"); } if (volume[t -
			 * 1] < volume[t]){ System.out.println(up +
			 * " volume[t - 1] < volume[t]"); } if (rsiArray.get(t -1) < 50 &&
			 * rsiArray.get(t) > 50){ System.out.println(up +
			 * " rsiArray.get(t) > 50"); }
			 */

			if (
					
					emaShortArray.get(t - 1) < emaLongArray.get(t - 1) && emaShortArray.get(t) > emaLongArray.get(t) && status == READY
//					&& stochKArray.get(t) > stochDArray.get(t)
//					&& emaShortArray.get(t) > emaLongArray.get(t)
//					&& volume[t - 1] < volume[t]
//					emaShortArray.get(t - 1) < emaLongArray.get(t - 1) && emaShortArray.get(t) > emaLongArray.get(t) 
//					&& stochKArray.get(t) > stochDArray.get(t)
					
					&& status == READY
					) {

				status = BUY;
				buyCount++;

				preBuyConditionPrice.add(closePrice);

				preBuyPrice = (closePrice + closePrice * BUY_CHARGE * 0.4);
				startTradePrice = closePrice;

				profitPrint.append("\n	MACD : " + tradeDay + " BUY : " + closePrice + " profit : " + profitToPercentage(mscdProfit) + " preBuyPrice : " + preBuyPrice + " rsi: " + rsiArray.get(t));

				profitPrint.append("\n	           day : " + date[t - 1] + " profit : " + profitToPercentage(0) + " rsi: " + rsiArray.get(t - 1) + " signal: " + signalArray.get(t - 1) + " macd: "
						+ macdArray.get(t - 1) + " stochK: " + stochKArray.get(t - 1) + " stochD: " + stochDArray.get(t - 1) + " closePrice: " + closePrice + " emaShortArray: "
						+ emaShortArray.get(t - 1) + " emaLongArray: " + emaLongArray.get(t - 1));

				profitPrint.append("\n	           day : " + tradeDay + " profit : " + profitToPercentage(0) + " rsi: " + rsiArray.get(t) + " signal: " + signalArray.get(t) + " macd: "
						+ macdArray.get(t) + " stochK: " + stochKArray.get(t) + " stochD: " + stochDArray.get(t) + " closePrice: " + closePrice + " emaShortArray: " + emaShortArray.get(t)
						+ " emaLongArray: " + emaLongArray.get(t));
			} else if (
					
					(
//							emaShortArray.get(t - 1) > emaLongArray.get(t - 1) && emaShortArray.get(t) < emaLongArray.get(t) 
//							|| emaShortArray.get(t) > close[t]
							emaShortArray.get(t - 1) > emaLongArray.get(t - 1) && emaShortArray.get(t) < emaLongArray.get(t) 
//					&& stochKArray.get(t) < stochDArray.get(t)
					)
//					(close[t] < close[t - 1] 
//							&& close[t - 1] < close[t - 2] 
//									&& close[t - 2] < close[t - 3]) 
//					&& volume[t - 1] < volume[t]
//					&& emaShortArray.get(t - 1) > emaLongArray.get(t - 1)
//					&& emaShortArray.get(t) < emaLongArray.get(t) 
					
					&& (status == HOLD || status == BUY)

			) {
				status = SELL; 

				double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
				double currentProfit = (sellPrice - preBuyPrice);
				mscdProfit += currentProfit;
				preBuyPrice = 0;
				status = SELL;
				profitPrint
						.append("\n	MACD : " + tradeDay + " SELL : " + closePrice + " profit : " + profitToPercentage(currentProfit) + " sellPrice : " + sellPrice + " preBuyPrice : " + preBuyPrice);
			} else if (status == BUY) {
				status = HOLD;
			} else if (status == SELL) {
				status = READY;
			}

			if (status == HOLD) {
				double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
				double currentProfit = (sellPrice - preBuyPrice);
				profitPrint.append("\n	           day : " + tradeDay + " profit : " + profitToPercentage(currentProfit) + " rsi: " + rsiArray.get(t) + " signal: " + signalArray.get(t) + " macd: "
						+ macdArray.get(t) + " stochK: " + stochKArray.get(t) + " stochD: " + stochDArray.get(t) + " closePrice: " + closePrice + " emaShortArray: " + emaShortArray.get(t)
						+ " emaLongArray: " + emaLongArray.get(t));
				if (profitToPercentage(currentProfit) < VVIPManager.checkMinusSellProfit || profitToPercentage(currentProfit) > VVIPManager.checkPlusSellProfit) {
					profitPrint.append("\n	SELL \n");
					mscdProfit += currentProfit;
					status = SELL;
					break;
				}
			}
		}

		if (preBuyPrice > 0 && (status == HOLD || status == BUY)) {
			double sellPrice = (close[close.length - 1] - close[close.length - 1] * SELL_CHARGE - close[close.length - 1] * TAX_RATE);
			double currentProfit = (sellPrice - preBuyPrice);
			mscdProfit += currentProfit;

			profitPrint.append("\nLast : " + "	MACD : " + date[close.length - 1] + " SELL : " + close[close.length - 1] + " profit : " + profitToPercentage(mscdProfit) + " sellPrice : " + sellPrice
					+ " preBuyPrice : " + preBuyPrice);
		}

		// status = READY;
		// if (preBuyConditionPrice.size() >= 1){
		// for (int i = 1 ; i < preBuyConditionPrice.size() ; i++){
		// if (preBuyConditionPrice.get(i) > preBuyConditionPrice.get(i -1)){
		// status = BUY;
		// } else {
		// status = READY;
		// }
		// }
		// }

		
		if (mscdProfit != 0) {
			System.out.println("buyCount: " + buyCount);
			if (printDebug) {
				System.out.println(symbol + " --------------- calculator report : " + profitPrint.toString());
			}
			return profitToPercentage(mscdProfit);
		} else {
			return 0;
		}
	}

	public double getProfit() {
		return profit;
	}

	public int getStartDay() {
		return startTradeDay;
	}

	public int getEndDay() {
		return endTradeDay;
	}

	public double profitToPercentage(double profit) {
		if (profit == 0) {
			return 0.0;
		}
		return Double.parseDouble(String.format("%.3f", profit / startTradePrice * 100));
	}
}