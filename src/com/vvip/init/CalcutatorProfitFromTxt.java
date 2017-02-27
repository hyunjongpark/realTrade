package com.vvip.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CalcutatorProfitFromTxt {
	double sumProfit = 0.0;
	double nS = 0;
	double nF = 0;

	class TodayRealTimeSymbolType {
		String symbol = "";
		double yesterClosePrice = 0;
		double preRealTimePrice = 0;
		double preRealTimeVolumn = 0;
		double preMaxProfit = 0;
		double preDiffVolumn = 0;
		boolean isMinus = false;
		boolean isDiffVolumnZero = false;
		boolean isDiffVolumn1Down = false;
		int up = 0;
		int down = 0;
		boolean isBuyOrder = false;
		boolean isSellOrder = false;
		int hValue = 0;
		int mValue = 0;
		int sValue = 0;
		double preProfit = 0;

		TodayRealTimeSymbolType(String symbol) {
			this.symbol = symbol;
		}
	}

	public void run() {
		String folderPath = "C:/Users/phj/vvip/realTrade/past_buy/data/201702/20170227";
		final File folder = new File(folderPath);
		ArrayList<String> paths = listFilesForFolder(folder);
		for (String path : paths) {
			ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
			try {
				if (path.contains("java")) {
					continue;
				}
				BufferedReader in = new BufferedReader(new FileReader(path));
				String s;
				String symbol = "";
				String volumen = "0";
				String peb = "";
				boolean isFirstLine = true;
				while ((s = in.readLine()) != null) {
					if (s.contains("PEB:") && isFirstLine) {
						isFirstLine = false;
						String d[] = s.split("PEB:");
						peb = d[1].trim();
					}
					if (s.contains("volume:")) {
						String d[] = s.split(" ");
						symbol = d[0];
						volumen = d[2];
					}
					if (!s.contains("check")) {
						continue;
					}
					if (!s.contains(":")) {
						continue;
					}
					ArrayList<String> data = new ArrayList<String>();
					String d[] = s.split(" ");
					for (int i = 0; i < d.length; i++) {
						if (i == 1 || i == 4 || i == 6 || i == 8) {
							data.add(d[i]);
						}
					}
					dataList.add(data);
				}
				in.close();
				if (dataList.size() > 0) {
					trading(symbol, volumen, path, peb, dataList);
				}
			} catch (IOException e) {
				System.err.println(e); // 占쎈퓠占쎌쑎揶쏉옙 占쎌뿳占쎈뼄筌롳옙 筌롫뗄�뻻筌욑옙 �빊�뮆�젾
				System.exit(1);
			}
		}
		System.out.println("success: " + nS + " fail: " + nF);
	}

	private void trading(String symbol, String preVolume, String readPath, String peb, ArrayList<ArrayList<String>> dataList) {
		if(!peb.equals("") && peb.equals("False")){
			return;
		}
		TodayRealTimeSymbolType tradeSymbol = new TodayRealTimeSymbolType(symbol);
		String buyData = "";
		double buyProfit = 0.0;
		double maxProfitAfterBuy = 0.0;
		double minProfitAfterBuy = 0.0;
		double buyPrice = 0;
		boolean isSucces11 = false;
		boolean isFaild = false;
		for (int t = 0; t < dataList.size(); t++) {
			ArrayList<String> data = dataList.get(t);
			int mHour = 0;
			int mMinute = 0;
			int mSecond = 0;
			String timeSplit[] = data.get(0).split(":");
			if (timeSplit.length == 3) {
				mHour = Integer.parseInt(timeSplit[0]);
				mMinute = Integer.parseInt(timeSplit[1]);
				mSecond = Integer.parseInt(timeSplit[2]);
			} else {
				return;
			}
			double currentPrice = Double.parseDouble(data.get(1));
			double currentProfit = Double.parseDouble(data.get(2));
			double currentVolumn = Double.parseDouble(data.get(3));
			if (tradeSymbol.isBuyOrder && isSucces11 == false) {
				double p = (currentPrice - buyPrice) / buyPrice * 100;
				minProfitAfterBuy = Math.min(minProfitAfterBuy, p);
//				System.out.println("buyPrice: " + buyPrice + " currentPrice: " + currentPrice  + " dd: " +p );
				
			}
			if (tradeSymbol.isBuyOrder) {
				double p = (currentPrice - buyPrice) / buyPrice * 100;
				maxProfitAfterBuy = Math.max(maxProfitAfterBuy, p);
//				System.out.println("buyPrice: " + buyPrice + " currentPrice: " + currentPrice  + " dd: " +p );
			}
			
			if (tradeSymbol.isBuyOrder && minProfitAfterBuy < VVIPManager.minusSellPercentByBuyPrice) {
				isFaild = true;
			}
			if (isSucces11 == false && isFaild == false && buyProfit > 0 && currentProfit >= buyProfit + VVIPManager.sellPercentByBuyPrice) {
				isSucces11 = true;
			}
			if (tradeSymbol.preRealTimePrice < currentPrice) {
				tradeSymbol.up++;
				tradeSymbol.down = 0;
			}
			if (tradeSymbol.preRealTimePrice > currentPrice) {
				tradeSymbol.up = 0;
				tradeSymbol.down++;
			}
			if (tradeSymbol.preRealTimePrice == currentPrice) {
				tradeSymbol.up = 0;
				tradeSymbol.down = 0;
			}
			double diff = currentVolumn - tradeSymbol.preRealTimeVolumn;
			double diffValue = -1;
			int diffS = -1;
			double diffProfit = -1;
			int preDiffS = (tradeSymbol.hValue * 60 * 60) + (tradeSymbol.mValue * 60) + tradeSymbol.sValue;
			int nowDiffS = (mHour * 60 * 60) + (mMinute * 60) + mSecond;
			diffS = nowDiffS - preDiffS;
			diffValue = diff / diffS;
			diffProfit = (currentProfit - tradeSymbol.preProfit) / diffS / diffValue * 100000;

			ArrayList<Boolean> checkArray = new ArrayList<Boolean>();
			checkArray.add(tradeSymbol.isBuyOrder == false);
			checkArray.add(tradeSymbol.preRealTimeVolumn != 0);
			checkArray.add(tradeSymbol.preDiffVolumn != 0);
			checkArray.add(tradeSymbol.preDiffVolumn < diff);
			checkArray.add(tradeSymbol.isDiffVolumnZero == false);
			checkArray.add(tradeSymbol.isDiffVolumn1Down == false);
			checkArray.add(tradeSymbol.isMinus == false);
			checkArray.add(2 <= tradeSymbol.up && tradeSymbol.up <= 5);
			checkArray.add(2 < currentProfit && currentProfit < 6);
			checkArray.add(100 < diffValue);
			checkArray.add(diffProfit >= 3);
			checkArray.add(mHour == 10 || mHour == 9 && mMinute >= 2);
			checkArray.add(((currentPrice * diff) / 10000000) > 2);
			
			
			
			
//			checkArray.add(((currentPrice * diff) / 100000000) > 1);
//			checkArray.add(tradeSymbol.preMaxProfit < currentPrice);
//			checkArray.add((Integer.parseInt(preVolume) *  currentPrice / 10000000) > 1);

			boolean isBuyStatus = true;
			for (int i = 0; i < checkArray.size(); i++) {
				if (!checkArray.get(i)) {
					isBuyStatus = false;
					break;
				}
			}
			if (isBuyStatus) {
				tradeSymbol.isBuyOrder = true;
				buyProfit = currentProfit;
				buyPrice = currentPrice;
//				buyData = "isDiffVolumnZero: " + (tradeSymbol.isDiffVolumnZero == false) + 
//						" isDiffVolumn1Down: " + (tradeSymbol.isDiffVolumn1Down == false) + 
//						" preMaxProfit: " + (tradeSymbol.preMaxProfit < currentPrice) +
//						" isMinus: " + (tradeSymbol.isMinus == false);
				buyData = data.get(0) + "\t price: " + currentPrice + "\t profit: " + currentProfit + "\t volumn: " + currentVolumn + "\t up: " + tradeSymbol.up + "\t currentProfit: "
						+ currentProfit + "\t diff: " + String.format("%.3f", (currentPrice * diff) / 10000000) +"\t diffValue:" + String.format("%.3f",diffValue)+ "\t diffProfit: " + diffProfit + "\t preVolumn: " + (Integer.parseInt(preVolume) *  buyPrice / 10000000);
			}
			tradeSymbol.preDiffVolumn = diff;
			tradeSymbol.preRealTimePrice = currentPrice;
			tradeSymbol.preRealTimeVolumn = currentVolumn;
			if (diff == 0 ) {
				tradeSymbol.isDiffVolumnZero = true;
			}
			if (((currentPrice * diff) / 10000000) < 1) {
				tradeSymbol.isDiffVolumn1Down = true;
			}
			tradeSymbol.preMaxProfit = Math.max(tradeSymbol.preMaxProfit, currentPrice);
			if (currentProfit < 0) {
				tradeSymbol.isMinus = true;
			}
			tradeSymbol.hValue = mHour;
			tradeSymbol.mValue = mMinute;
			tradeSymbol.sValue = mSecond;
			tradeSymbol.preProfit = currentProfit;
		}
		if (buyProfit != 0) {
//			if(!CommonUtil.isPEROK(symbol)){
//				return;
//			}
			System.out.println("peb:" + peb);
			if (isSucces11) {
				nS++;
				System.out.println(preVolume + "\t : " + (Integer.parseInt(preVolume) *  buyPrice / 10000000) + "\t success: " + readPath + " " + buyData);
				 System.out.println("-----------S min: " + minProfitAfterBuy + " max: " + maxProfitAfterBuy + " close: "+ (Double.parseDouble(dataList.get(dataList.size() -1).get(2)) - buyProfit));
			} else {
				nF++;
				System.out.println(preVolume + "\t : " + (Integer.parseInt(preVolume) *  buyPrice / 10000000) + "\t fail: " + readPath + " " + buyData);
				 System.out.println("----------F min: " + minProfitAfterBuy + " max: " + maxProfitAfterBuy + " close: " + (Double.parseDouble(dataList.get(dataList.size() - 1).get(2)) -buyProfit));
				// for (int i = 0; i < quoteList.getSize(); i++) {
				// if (quoteList.getQuote(i).getTradeDate().toInt() > date) {
				// if (getPriceOfPercentage(buyPrice, sellProfit) <
				// quoteList.getQuote(i).getHigh()) {
				// System.out.println(" fail: " +
				// quoteList.getQuote(i).getTradeDate().toInt() + " " +
				// quoteList.getQuote(i).getHigh());
				// }
				// }
				// }
			}
			sumProfit -= (Double.parseDouble(dataList.get(dataList.size() - 1).get(2)) - buyProfit);
		}
	}

	public double getPriceOfPercentage(double basePrice, double profit) {
		if (profit == 0) {
			return 0.0;
		}
		return Double.parseDouble(String.format("%.3f", basePrice + ((basePrice / 100) * profit)));
	}

	private ArrayList<String> listFilesForFolder(final File folder) {
		ArrayList<String> paths = new ArrayList<String>();
		if (folder.isFile()) {
			paths.add(folder.getPath());
			return paths;
		}
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				paths.addAll(listFilesForFolder(fileEntry));
			} else {
				paths.add(folder + "/" + fileEntry.getName());
			}
		}
		return paths;
	}
}
