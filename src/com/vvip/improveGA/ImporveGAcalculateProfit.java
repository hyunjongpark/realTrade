package com.vvip.improveGA;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.vvip.init.VVIPManager;

public class ImporveGAcalculateProfit {

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
	double close[];
	// double high[];
	// double low[];
	int date[];
	int status;
	double startTradePrice = 0;
	String symbol = "";

	int shortMA;
	int longMA;
	int macdMA;
	// int fastK;
	// int slowK;
	// int slowD;
	// String binaryGene ="";
	boolean test = false;

	double profit;

	int profitStartDay = 0;
	int endStartDay = 0;

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public ImporveGAcalculateProfit(int profitStartDay, int endStartDay, double close[], int date[], int shortMA, int longMA, int macdMA, boolean test) {
		this.profitStartDay = profitStartDay;
		this.endStartDay = endStartDay;
		this.close = close;
		this.date = date;
		this.shortMA = shortMA;
		this.longMA = longMA;
		this.macdMA = macdMA;
		this.test = test;
	}

	public void run() {
		double macdProfit = 0.0;
		if (!test) {
			macdProfit = calculateMACDProfit(shortMA, longMA, macdMA);
		} else {
			macdProfit = testCalculateMACDProfit(shortMA, longMA, macdMA);
		}
		profit = macdProfit;
	}

	public int getStatus() {
		return status;
	}

	public double testCalculateMACDProfit(int shortMA, int longMA, int macdMA) {

		StringBuffer profitPrint = new StringBuffer();

		Core ta = new Core();

		double mscdProfit = 0;

		MInteger macdIdx;
		MInteger macdElement;

		macdIdx = new MInteger();
		macdElement = new MInteger();

		double[] macd;
		double[] signal;
		double[] osc;

		macd = new double[close.length];
		signal = new double[close.length];
		osc = new double[close.length];

		double preBuyPrice = 0;
		ta.macd(0, close.length - 1, close, shortMA, longMA, macdMA, macdIdx, macdElement, macd, signal, osc);

		// double[] dBbU = new double[close.length];
		// double[] dBbM = new double[close.length];
		// double[] dBbL = new double[close.length];
		double bbU[] = new double[close.length];
		double bbM[] = new double[close.length];
		double bbL[] = new double[close.length];
		MInteger outBegIdx = new MInteger();
		MInteger outNBElement = new MInteger();
		ta.bbandsLookback(9, 2, 2, MAType.Sma);
		ta.bbands(0, close.length - 1, close, 9, 2.0000, 2.0000, MAType.Sma, outBegIdx, outNBElement, bbU, bbM, bbL);

		if (macdElement.value > 0) {
			preBuyPrice = 0;
			for (int i = 0; i < macdElement.value; i++) {
				macd[i] = Double.parseDouble(String.format("%.2f", macd[i]));
				// System.out.println(macd[i]);
			}
			int startIndex = macdIdx.value + 1;
			int oscIndex = 2;
			status = READY;

			for (int t = startIndex; t < close.length - 1; t++) {
				int tradeDay = date[t + 1];
				double closePrice = close[t + 1];
				if (tradeDay <= profitStartDay || endStartDay < tradeDay) {
					oscIndex++;
					continue;
				}

				// System.out.println(macd[oscIndex] + " / " + tradeDay
				// + " closePrice : " + closePrice);
				//
				// System.out.println("dBbU : " + bbU[oscIndex] + " dBbM : "
				// + bbM[oscIndex] + " dBbL : " + bbL[oscIndex]);

				if (macd[oscIndex - 1] < macd[oscIndex] && status == READY) {
					// if (macd[oscIndex - 1] < macd[oscIndex] && status ==
					// READY && closePrice < bbM[oscIndex]) {
					// if (macd[oscIndex - 1] <= 0 && macd[oscIndex] >= 0 &&
					// status == READY) {
					preBuyPrice = (closePrice + closePrice * BUY_CHARGE);
					profitPrint.append("\n	BUY MACD : " + tradeDay + " macd : " + macd[oscIndex] + " total profit : " + profitToPercentage(mscdProfit) + " BUY : " + closePrice + " preBuyPrice : " + preBuyPrice);
					profitPrint.append("\n  BUY dBbU : " + bbU[oscIndex] + "dBbM : " + bbM[oscIndex] + "dBbL : " + bbL[oscIndex]);
					startTradePrice = closePrice;
					status = BUY;
					// System.out.println("BUY");
				} else if (macd[oscIndex - 1] > macd[oscIndex] && (status == HOLD || status == BUY)) {
					// } else if (macd[oscIndex - 1] >= 0 && macd[oscIndex] <= 0
					// && (status == HOLD || status == BUY)) {
					double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
					double currentProfit = (sellPrice - preBuyPrice);
					mscdProfit += currentProfit;
					profitPrint.append("\n	SELL MACD : " + tradeDay + " macd : " + macd[oscIndex] + " total profit : " + profitToPercentage(mscdProfit) + " SELL : " + closePrice + " profit : " + profitToPercentage(currentProfit) + " sellPrice : " + sellPrice + " preBuyPrice : " + preBuyPrice);
					profitPrint.append("\n");
					preBuyPrice = 0;
					status = SELL;
					// System.out.println("SELL");
				} else if (status == BUY) {
					status = HOLD;
				} else if (status == SELL) {
					status = READY;
				}
				if (status == HOLD) {
					double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
					double currentProfit = (sellPrice - preBuyPrice);
					profitPrint.append("\n	day  : " + tradeDay + " macd : " + macd[oscIndex] + " total profit : " + profitToPercentage(currentProfit) + " profit : " + profitToPercentage(currentProfit));
					if (profitToPercentage(currentProfit) < VVIPManager.checkMinusSellProfit || profitToPercentage(currentProfit) > VVIPManager.checkPlusSellProfit) {
						profitPrint.append("\n	SELL \n");
						mscdProfit += currentProfit;
						status = SELL;
					}
				}
				oscIndex++;
			}

			if (preBuyPrice > 0 && (status == HOLD || status == BUY)) {
				double sellPrice = (close[close.length - 1] - close[close.length - 1] * SELL_CHARGE - close[close.length - 1] * TAX_RATE);
				double currentProfit = (sellPrice - preBuyPrice);
				mscdProfit += currentProfit;
				if (currentProfit > sellPrice) {
					profitPrint.append("\nLast : " + "	MACD : " + date[close.length - 1] + " SELL : " + close[close.length - 1] + " profit : " + profitToPercentage(mscdProfit) + " sellPrice : " + sellPrice + " preBuyPrice : " + preBuyPrice);
				} else {
					profitPrint.append("\nLast : " + "	MACD : " + date[close.length - 1] + " SELL : " + close[close.length - 1] + " profit : " + profitToPercentage(mscdProfit) + " sellPrice : " + sellPrice + " preBuyPrice : " + preBuyPrice);
					profitPrint.append("\n");
				}
			}
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

	public double calculateMACDProfit(int shortMA, int longMA, int macdMA) {

		StringBuffer profitPrint = new StringBuffer();
		// profitPrint.append("Start shortMA : " + shortMA + " longMA : " +
		// longMA + " macdMA : " + macdMA + " fastK : " + fastK + " slowK : " +
		// slowK + " slowD : " + slowD);
		// profitPrint.append("\n");

		Core ta = new Core();

		double mscdProfit = 0;

		MInteger macdIdx;
		MInteger macdElement;

		macdIdx = new MInteger();
		macdElement = new MInteger();

		double[] macd;
		double[] signal;
		double[] osc;

		macd = new double[close.length];
		signal = new double[close.length];
		osc = new double[close.length];

		double preBuyPrice = 0;
		ta.macd(0, close.length - 1, close, shortMA, longMA, macdMA, macdIdx, macdElement, macd, signal, osc);

		if (macdElement.value > 0) {
			preBuyPrice = 0;
			for (int i = 0; i < macdElement.value; i++) {
				macd[i] = Double.parseDouble(String.format("%.2f", macd[i]));
			}
			int startIndex = macdIdx.value + 1;
			int oscIndex = 2;
			status = READY;

			for (int t = startIndex; t < close.length - 1; t++) {
				int tradeDay = date[t + 1];
				double closePrice = close[t + 1];
				if (tradeDay <= profitStartDay || endStartDay < tradeDay) {
					oscIndex++;
					continue;
				}

				if (macd[oscIndex - 1] <= 0 && macd[oscIndex] >= 0 && status == READY) {
					preBuyPrice = (closePrice + closePrice * BUY_CHARGE);
					profitPrint.append("\n	MACD : " + tradeDay + " BUY : " + closePrice + " profit : " + profitToPercentage(mscdProfit) + " preBuyPrice : " + preBuyPrice);
					startTradePrice = closePrice;
					status = BUY;
				} else if (macd[oscIndex - 1] >= 0 && macd[oscIndex] <= 0 && (status == HOLD || status == BUY)) {
					double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
					double currentProfit = (sellPrice - preBuyPrice);
					mscdProfit += currentProfit;
					profitPrint.append("\n	MACD : " + tradeDay + " SELL : " + closePrice + " profit : " + profitToPercentage(currentProfit) + " sellPrice : " + sellPrice + " preBuyPrice : " + preBuyPrice);
					profitPrint.append("\n");
					preBuyPrice = 0;
					status = SELL;
				} else if (status == BUY) {
					status = HOLD;
				} else if (status == SELL) {
					status = READY;
				}
				if (status == HOLD) {
					double sellPrice = (closePrice - closePrice * SELL_CHARGE - closePrice * TAX_RATE);
					double currentProfit = (sellPrice - preBuyPrice);
					profitPrint.append("\n	day : " + tradeDay + " macd : " + macd[oscIndex] + " profit : " + profitToPercentage(currentProfit));
					if (profitToPercentage(currentProfit) < VVIPManager.checkMinusSellProfit || profitToPercentage(currentProfit) > VVIPManager.checkPlusSellProfit) {
						profitPrint.append("\n	SELL \n");
						mscdProfit += currentProfit;
						status = SELL;
					}
				}
				oscIndex++;
			}

			if (preBuyPrice > 0 && (status == HOLD || status == BUY)) {
				double sellPrice = (close[close.length - 1] - close[close.length - 1] * SELL_CHARGE - close[close.length - 1] * TAX_RATE);
				double currentProfit = (sellPrice - preBuyPrice);
				mscdProfit += currentProfit;
				if (currentProfit > sellPrice) {
					profitPrint.append("\nLast : " + "	MACD : " + date[close.length - 1] + " SELL : " + close[close.length - 1] + " profit : " + profitToPercentage(mscdProfit) + " sellPrice : " + sellPrice + " preBuyPrice : " + preBuyPrice);
				} else {
					profitPrint.append("\nLast : " + "	MACD : " + date[close.length - 1] + " SELL : " + close[close.length - 1] + " profit : " + profitToPercentage(mscdProfit) + " sellPrice : " + sellPrice + " preBuyPrice : " + preBuyPrice);
					profitPrint.append("\n");
				}
			}
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

	public double getProfit() {
		return profit;
	}

	public double profitToPercentage(double profit) {
		if (profit == 0) {
			return 0.0;
		}
		return Double.parseDouble(String.format("%.3f", profit / startTradePrice * 100));
	}
}