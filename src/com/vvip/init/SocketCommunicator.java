package com.vvip.init;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import com.vvip.ga.Gene;
import com.vvip.quote.Quote;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.trade.TradeType;
import com.vvip.util.ChartPattern;
import com.vvip.util.DatabaseManager;

public class SocketCommunicator implements Runnable {
    private Thread instance;
    SocketType trade = null;
    private Socket socket = null;

    class RealTimeTradeType {
        String symbol = "";
        QuoteList quoteList = null;
        Gene gene = null;

        RealTimeTradeType(String symbol, QuoteList quoteList, Gene gene) {
            this.symbol = symbol;
            this.quoteList = quoteList;
            this.gene = gene;
        }
    }

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
        double preProfit = -1;

        TodayRealTimeSymbolType(String symbol) {
            this.symbol = symbol;
        }
    }

    class SellOrder {
        String symbol = "";
        String number;
        int mHour;
        int mMinute;

        public SellOrder(String symbol, String number, int mHour, int mMinute) {
            this.symbol = symbol;
            this.number = number;
            this.mHour = mHour;
            this.mMinute = mMinute;
        }
    }

    public SocketType getTradeCommunicator() {
        if (null == trade) {
            trade = new SocketType();
        }
        return trade;
    }

    public void startVVIPManagerThread(Socket sock, SocketCommunicator obj, int indexNumber) {
        if (null == instance || !instance.isAlive()) {
            instance = new Thread(null, obj, "VVIPManager");
            this.socket = sock;
            instance.start();
        }
    }

    @Override
    public void run() {
        getTradeCommunicator().startIDECommunicatorThread(this, socket, getTradeCommunicator());
        realTimeTradeManagerByVolumn();
    }

    public double profitToPercentage(double baseProfit, double profit) {
        if (profit == 0) {
            return 0.0;
        }
        return Double.parseDouble(String.format("%.3f", (profit - baseProfit) / baseProfit * 100));
    }

    private List<TodayRealTimeSymbolType> getTodayTradeSymbolListByVolumn(int getListCount, double rangeStartPrice, double rangeEndPrice) {
        List<TodayRealTimeSymbolType> returnList = new ArrayList<TodayRealTimeSymbolType>();
        class QuoteComparator implements Comparable<QuoteComparator> {
            private Quote quote;
            private String symbol = "";

            public QuoteComparator(Quote quote, String symbol) {
                this.quote = quote;
                this.symbol = symbol;
            }

            @Override
            public int compareTo(QuoteComparator o) {
                return quote.getVolume() > o.quote.getVolume() ? -1 : 1;
            }
        }

        List<QuoteComparator> quoteComp = new ArrayList<QuoteComparator>();

        for (int i = 2; i < VVIPManager.getCompanyList().size(); i++) {
            String symbol = VVIPManager.getCompanyList().get(i).getSymbol();

            QuoteList quoteList = DatabaseManager.selectQuoteListByDate(symbol, CommonUtil.getDayIntByWeek(CommonUtil.getToday(), 2), CommonUtil.getToday());
            if (null == quoteList) {
                continue;
            }

            if (quoteList.getQuote(quoteList.getSize() - 1).getClose() < rangeStartPrice || rangeEndPrice < quoteList.getQuote(quoteList.getSize() - 1).getClose()) {
                continue;
            }

            if (quoteList.getList().size() < 5) {
                continue;
            }

            long avg20Volumn = 0;
            for (int j = 0; j < 5; j++) {
                avg20Volumn += quoteList.getQuote(quoteList.getSize() - 1 - j).getVolume();
            }
            avg20Volumn = avg20Volumn / 5;

            long volumn = quoteList.getQuote(quoteList.getSize() - 1).getVolume();

            // 5일 평균 보다 어제 2배 이상 상승
            if (avg20Volumn > (volumn / 2)) {
                // continue;
            }

            // 3일 연속 상승했는지
            double b3Price = quoteList.getQuote(quoteList.getSize() - 3).getClose();
            double b2Price = quoteList.getQuote(quoteList.getSize() - 2).getClose();
            double b1Price = quoteList.getQuote(quoteList.getSize() - 1).getClose();
            if (b3Price < b2Price && b2Price < b1Price) {
                continue;
            }
            if (b3Price > b2Price && b2Price > b1Price) {
                continue;
            }
            if ((b2Price - b1Price) / b1Price * 100 < -5) {
                continue;
            }

            double price = quoteList.getQuote(quoteList.getSize() - 1).getClose();

            // // 전날 거래양이 10억이싱인지
            if (quoteList.getQuote(quoteList.getSize() - 1).getVolume() * price / 100 < 10000000) {
                continue;
            }

            // // 2틀 연속 거래량이 커야한다.
            if (quoteList.getQuote(quoteList.getSize() - 1).getVolume() < quoteList.getQuote(quoteList.getSize() - 2).getVolume()) {
                continue;
            }
            
            quoteComp.add(new QuoteComparator(quoteList.getQuote(quoteList.getSize() - 1), symbol));
        }

        Collections.sort(quoteComp);
        int nSize = getListCount;
        if (getListCount > quoteComp.size()) {
            nSize = quoteComp.size();
        }
        ChartPattern chartPattern = ChartPattern.getInstance();
        for (int i = 0; i < nSize; i++) {
            System.out.println(
                    i + "/" + nSize + " " + quoteComp.get(i).symbol + " volume: " + quoteComp.get(i).quote.getVolume() + " " + chartPattern.convertPattern(quoteComp.get(i).quote.getPattern()));
            CommonUtil.writeToFile(TradeType.trade_path + "_" + quoteComp.get(i).symbol,
                    quoteComp.get(i).symbol + " volume: " + quoteComp.get(i).quote.getVolume() + " " + chartPattern.convertPattern(quoteComp.get(i).quote.getPattern()));
            TodayRealTimeSymbolType trade = new TodayRealTimeSymbolType(quoteComp.get(i).symbol);
            trade.yesterClosePrice = quoteComp.get(i).quote.getClose();
            trade.preRealTimePrice = trade.yesterClosePrice;
            
            String pebCheck = "PEB: True";
            if(!CommonUtil.isPEROK(quoteComp.get(i).symbol)){
            	pebCheck = "PEB: False";
//            	continue;
			}
            CommonUtil.writeToFile(TradeType.trade_path + "_" + quoteComp.get(i).symbol, quoteComp.get(i).symbol + ", " + pebCheck);
            
            returnList.add(trade);
        }
        return returnList;
    }

    public void realTimeTradeManagerByVolumn() {
        System.out.println("wait start");
        getBankStock();
        waiting8Time();

        System.out.println("make today trade list");
        List<TodayRealTimeSymbolType> todaySymbolList = getTodayTradeSymbolListByVolumn(80, VVIPManager.rangeStartPrice, VVIPManager.rangeEndPrice);

        // 들고 있는 좀목은 매수한 금액의 올려서 매도 주문
        List<TradeType> bankList1 = getBankStock();
        for (TradeType type : bankList1) {
            if (type.profit < 0 && !type.symbol.equals("097870")) {
                orderSellToClient(type.symbol, type.buyCount, (int) type.buyPrice);
            } else {
                orderSellToClient(type.symbol, type.buyCount, CommonUtil.getPecentPrice(type.buyPrice, VVIPManager.sellPercentByBuyPrice));
            }

            CommonUtil.writeToFile(TradeType.trade_path,
                    "case morning sell " + " buy price: " + type.buyPrice + " profit: " + type.profit + " sell:" + CommonUtil.getPecentPrice(type.buyPrice, VVIPManager.sellPercentByBuyPrice) + "\n");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<String> todayBuyList = new ArrayList<String>();

        List<SellOrder> sellOrderList = new ArrayList<SellOrder>();
        waiting9Time();

        System.out.println("Start today trade");
        boolean isContinue = true;

        ChartPattern chartPattern = ChartPattern.getInstance();

        while (isContinue) {
            Calendar cal = new GregorianCalendar();
            int mHour = cal.get(Calendar.HOUR_OF_DAY);
            int mMinute = cal.get(Calendar.MINUTE);
            int mSecond = cal.get(Calendar.SECOND);

            List<TradeType> bankList = getBankStock();
            try {
                Thread.sleep(1000 * 10 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 거래는 1분부터
            if (mHour < 9) {
                continue;
            }

            if (mHour >= 15 && mMinute >= 20) {
            	// 장 종료 전에 오늘 산 종목중 못 판 종목에 대해서는 매도한다.
    			if (todayBuyList.size() > 0) {
    				List<String> removeBuyList = new ArrayList<String>();
    				for (String ss : todayBuyList) {
    					for (TradeType type : bankList) {
    						if (type.symbol.equals(ss)) {
    							if (type.profit < 0) {
    								for (int t = 0; t < todaySymbolList.size(); t++) {
    									TodayRealTimeSymbolType tradeSymbol = todaySymbolList.get(t);
    									if (type.symbol.equals(tradeSymbol.symbol) && tradeSymbol.isSellOrder == false) {
    										tradeSymbol.isSellOrder = true;
    										removeBuyList.add(tradeSymbol.symbol);

    										String n = orderSellToClient(type.symbol, type.buyCount, (int)type.buyPrice);
    										CommonUtil.writeToFile(TradeType.trade_path, "last Sell order " + mHour + ":" + mMinute + " " + type.symbol + " price: " + type.currentPrice + " count: "
    												+ Integer.toString(type.buyCount) + " num : " + n + "\n");
    										sellOrderList.add(new SellOrder(type.symbol, n, mHour, mMinute));
    										try {
    											Thread.sleep(5000);
    										} catch (InterruptedException e) {
    											e.printStackTrace();
    										}
    									}
    								}
    							}
    						}
    					}
    				}
    				todayBuyList.removeAll(removeBuyList);
    			}
                System.out.println("END Trade");
                isContinue = false;
            }

            if (sellOrderList.size() > 0) {
                try {
                    List<SellOrder> removeSellOrderList = new ArrayList<SellOrder>();
                    for (SellOrder sell : sellOrderList) {
                        boolean isFinishSell = true;
                        for (TradeType type : bankList) {
                            if (sell.symbol.equals(type.symbol)) {
                                isFinishSell = false;
                            }
                        }
                        if (isFinishSell) {
                            removeSellOrderList.add(sell);
                        }
                    }
                    if (removeSellOrderList.size() > 0) {
                        sellOrderList.removeAll(removeSellOrderList);
                    }
                    removeSellOrderList.clear();

                    // 마이너스 profit 아래이면 현재 가격으로 매도 주문
                    for (SellOrder sell : sellOrderList) {
                        for (TradeType type : bankList) {
                            if (type.symbol.equals(sell.symbol)) {
                                for (int t = 0; t < todaySymbolList.size(); t++) {
                                    TodayRealTimeSymbolType tradeSymbol = todaySymbolList.get(t);
                                    if (!sell.symbol.equals(tradeSymbol.symbol)) {
                                        continue;
                                    }
//                                    if (sell.symbol.equals(type.symbol) && CommonUtil.getPecentPrice(tradeSymbol.yesterClosePrice, 0) > type.currentPrice) {
                                    if (sell.symbol.equals(type.symbol) && type.profit < VVIPManager.minusSellPercentByBuyPrice) {
                                        removeSellOrderList.add(sell);
                                        getTradeCommunicator().requestModifyTrade(type.symbol, Integer.toString(type.buyCount), Double.toString(type.currentPrice), sell.number);
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        CommonUtil.writeToFile(TradeType.trade_path, " Minus Sell " + mHour + ":" + mMinute + ":" + mSecond + " " + type.symbol + " price: " + type.currentPrice
                                                + " count: " + Integer.toString(type.buyCount) + " tradeSymbol.yesterClosePrice: " + tradeSymbol.yesterClosePrice + " num : " + sell.number + "\n");
                                    }
                                }
                            }
                        }
                    }
                    if (removeSellOrderList.size() > 0) {
                        sellOrderList.removeAll(removeSellOrderList);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            
            //

			// 매수 한 종목이 마이너스 profit일때 매도주문을 건다
			if (todayBuyList.size() > 0) {
				List<String> removeBuyList = new ArrayList<String>();
				for (String ss : todayBuyList) {
					for (TradeType type : bankList) {
						if (type.symbol.equals(ss)) {
//							if (type.profit < 0) {
								for (int t = 0; t < todaySymbolList.size(); t++) {
									TodayRealTimeSymbolType tradeSymbol = todaySymbolList.get(t);
									if (type.symbol.equals(tradeSymbol.symbol) && tradeSymbol.isSellOrder == false) {
										tradeSymbol.isSellOrder = true;
										removeBuyList.add(tradeSymbol.symbol);

										String n = orderSellToClient(type.symbol, type.buyCount, CommonUtil.getPecentPrice(type.buyPrice, VVIPManager.sellPercentByBuyPrice));
										CommonUtil.writeToFile(TradeType.trade_path, "Sell order " + mHour + ":" + mMinute + " " + type.symbol + " price: " + type.currentPrice + " count: "
												+ Integer.toString(type.buyCount) + " num : " + n + "\n");
										sellOrderList.add(new SellOrder(type.symbol, n, mHour, mMinute));
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
//									}
								}
							}
						}
					}
				}
				todayBuyList.removeAll(removeBuyList);
			}

            List<Quote> quoteList = requestPrice(todaySymbolList);
            for (int t = 0; t < todaySymbolList.size(); t++) {
                TodayRealTimeSymbolType tradeSymbol = todaySymbolList.get(t);

                Quote currentQuote = null;
                for (Quote quote : quoteList) {
                    if (quote.getSymbol().equals(tradeSymbol.symbol)) {
                        currentQuote = quote;
                        break;
                    }
                }

                if (null == currentQuote) {
                    continue;
                }

                double yesterdayPrice = tradeSymbol.yesterClosePrice;
                double currentPrice = currentQuote.getClose();
                long currentVolumn = currentQuote.getVolume();
                double currentProfit = profitToPercentage(yesterdayPrice, currentPrice);

                // update pattern
//                QuoteList quoteList11 = DatabaseManager.selectLimitQuoteListBySymbol(tradeSymbol.symbol, 0, 50, true);
//                quoteList11.addQuote(currentQuote);
//                TechnicalAnalysis ta = new TechnicalAnalysis(quoteList11.getList(), quoteList11.getSize() - 1);
//                ta.setTaPattern();

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
//    			checkArray.add(tradeSymbol.preMaxProfit < currentPrice);
//    			checkArray.add((Integer.parseInt(preVolume) *  currentPrice / 10000000) > 1);

                boolean isBuyStatus = true;
                for (int i = 0; i < checkArray.size(); i++) {
                    if (!checkArray.get(i)) {
                        isBuyStatus = false;
                        break;
                    }
                }
                if (isBuyStatus) {
                    tradeSymbol.isBuyOrder = true;
                    String buyN = orderBuyToClient(tradeSymbol.symbol, CommonUtil.getBuyCount((int) currentPrice), 0);
                    todayBuyList.add(tradeSymbol.symbol);

                    System.out.println("Buy " + mHour + ":" + mMinute + " " + tradeSymbol.symbol + " price: " + currentPrice + " volumn: " + currentVolumn + " diff: "
                            + (currentVolumn - tradeSymbol.preRealTimeVolumn) + " buyN: " + buyN + " order count: " + CommonUtil.getBuyCount((int) currentPrice));

                    CommonUtil.writeToFile(TradeType.trade_path,
                            "case Buy " + mHour + ":" + mMinute + ":" + mSecond + " " + tradeSymbol.symbol + " Num: " + buyN + " price: " + currentPrice + " profit: " + currentProfit + " volumn: "
                                    + currentVolumn + " diff: " + (currentVolumn - tradeSymbol.preRealTimeVolumn) + " traffic: " + ((currentPrice * diff) / 10000000) + " "
                                    + chartPattern.convertPattern(currentQuote.getPattern()) + "\n");

                    CommonUtil.writeToFile(TradeType.trade_path + "_" + tradeSymbol.symbol,
                            "case Buy " + mHour + ":" + mMinute + " " + tradeSymbol.symbol + " Num: " + buyN + " price: " + currentPrice + " profit: " + currentProfit + " volumn: " + currentVolumn
                                    + " diff: " + (currentVolumn - tradeSymbol.preRealTimeVolumn) + " traffic: " + ((currentPrice * diff) / 10000000) + " "
                                    + chartPattern.convertPattern(currentQuote.getPattern()) + "\n");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                tradeSymbol.preDiffVolumn = diff;
                tradeSymbol.preRealTimePrice = currentPrice;
                tradeSymbol.preRealTimeVolumn = currentVolumn;
                if (diff == 0) {
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

                boolean printBooleanValue[] = new boolean[checkArray.size()];
                for (int i = 0; i < checkArray.size(); i++) {
                    printBooleanValue[i] = checkArray.get(i);
                }

                CommonUtil.writeToFile(TradeType.trade_path + "_" + tradeSymbol.symbol,
                        "check " + mHour + ":" + mMinute + ":" + mSecond + " " + tradeSymbol.symbol + " price: " + currentPrice + " profit: " + currentProfit + " volumn: " + currentVolumn + " diff: "
                                + tradeSymbol.preDiffVolumn + " traffic: " + ((currentPrice * diff) / 10000000) + " up:" + tradeSymbol.up + " down: " + tradeSymbol.down + " "
                                + Arrays.toString(printBooleanValue) + "\n");

            }
        }
        waiting4Time();
        UpdateKoreaQuote.main(null);
        System.out.println("today end");
        return;

    }

    public List<Quote> requestPrice(List<TodayRealTimeSymbolType> todaySymbolList) {
        List<Quote> quoteList = new ArrayList<Quote>();
        int nTotalSize = todaySymbolList.size();
        int startIndex = 0;
        while (true) {
            List<String> getPriceSymbolList = new ArrayList<String>();
            for (int n = startIndex; n < nTotalSize; n++) {
                getPriceSymbolList.add(todaySymbolList.get(n).symbol);
                if (n != 0 && n % 45 == 0) {
                    break;
                }
            }

            List<Quote> getList = getMultySymbolInfo(getPriceSymbolList);
            for (Quote quote : getList) {
                quoteList.add(quote);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startIndex = quoteList.size();
            if (nTotalSize <= quoteList.size()) {
                break;
            }
        }
        return quoteList;
    }

    private void waiting4Time() {
        System.out.println("waiting4Time");
        while (true) {
            try {
                Calendar cal = new GregorianCalendar();
                int mHour = cal.get(Calendar.HOUR_OF_DAY);
                if (mHour >= 16) {
                    return;
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<TradeType> getBankStock() {
        List<TradeType> returnList = new ArrayList<TradeType>();
        String endSymbol = "FIRST";
        while (true) {
            endSymbol = getBankBuySymbol(returnList, endSymbol);
            if (endSymbol.equals("END")) {
                break;
            }
        }
        return returnList;
    }

    private String getBankBuySymbol(List<TradeType> setList, String next) {
        List<TradeType> returnList = new ArrayList<TradeType>();

        String bankStockInfos = getTradeCommunicator().requestBankInfo(next);
        if (null == bankStockInfos) {
            return "END";
        }
        if (bankStockInfos.contains(";")) {
            String[] bankStockInfosSplit = bankStockInfos.split(";");
            bankStockInfosSplit[0].trim();
            int stockCount = Integer.parseInt(bankStockInfosSplit[1]);
            int startIndex = 2;
            for (int i = 0; i < stockCount; i++) {
                String symbol = bankStockInfosSplit[startIndex++];
                symbol = symbol.replace("A", "");
                int count = Integer.parseInt(bankStockInfosSplit[startIndex++]);
                double buyPrice = Double.parseDouble(bankStockInfosSplit[startIndex++]);
                double profit = Double.parseDouble(bankStockInfosSplit[startIndex++]);
                double currentPrice = Double.parseDouble(bankStockInfosSplit[startIndex++]);
                TradeType stock = new TradeType(symbol, 0);
                stock.buyPrice = buyPrice;
                stock.currentPrice = currentPrice;
                stock.setBuyCount(count);
                stock.setProfit(profit);
                returnList.add(stock);
            }
        }

        setList.addAll(returnList);

        if (returnList.size() == 70) {
            return "NEXT";
        } else {
            return "END";
        }
    }

    private void waiting8Time() {
        while (true) {
            try {
                Calendar cal = new GregorianCalendar();
                int mHour = cal.get(Calendar.HOUR_OF_DAY);
                int mMinute = cal.get(Calendar.MINUTE);
                System.out.println(mHour + " : " + mMinute);
                if (mHour > 8) {
                    return;
                }
                Thread.sleep(5000);
                if (mHour == 8 && mMinute >= 0) {
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waiting9Time() {
        System.out.println("waiting9Time");
        while (true) {
            try {
                Calendar cal = new GregorianCalendar();
                int mHour = cal.get(Calendar.HOUR_OF_DAY);
                int mMinute = cal.get(Calendar.MINUTE);
                if (mHour > 8) {
                    return;
                }
                if (mHour == 8 && mMinute >= 58) {
                    return;
                }

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Quote> getMultySymbolInfo(List<String> symbolList) {
        List<Quote> returnList = new ArrayList<Quote>();

        String requestSsymbolList = "";
        for (String symbol : symbolList) {
            requestSsymbolList += symbol;
        }
        String bankStockInfos = getTradeCommunicator().requestMultyQuotaInfo(Integer.toString(symbolList.size()), requestSsymbolList);
        if (null == bankStockInfos) {
            return returnList;
        }
        if (bankStockInfos.contains(";")) {
            String[] bankStockInfosSplit = bankStockInfos.split(";");
            int stockCount = Integer.parseInt(bankStockInfosSplit[1]);
            int startIndex = 2;
            for (int i = 0; i < stockCount; i++) {

                String symbol = bankStockInfosSplit[startIndex++];
                symbol = symbol.replace("A", "");

                double low = Double.parseDouble(bankStockInfosSplit[startIndex++]);
                double hight = Double.parseDouble(bankStockInfosSplit[startIndex++]);
                double open = Double.parseDouble(bankStockInfosSplit[startIndex++]);
                double price = Double.parseDouble(bankStockInfosSplit[startIndex++]);
                Long volume = Long.parseLong(bankStockInfosSplit[startIndex++]);

                Calendar cal = new GregorianCalendar();
                int mYear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH) + 1;
                int mDay = cal.get(Calendar.DAY_OF_MONTH);
                Quote quote = new Quote(symbol, new TradeDate(mYear, mMonth, mDay), low, hight, open, price, volume);
                returnList.add(quote);
            }
        }
        return returnList;
    }

    private String orderBuyToClient(String symbol, int buyCount, int buyPrice) {
    	return "test";
//        if (buyPrice == 0) {
//            return getTradeCommunicator().requestTradeInfo(symbol, Integer.toString(buyCount), true, "0");
//        } else {
//            return getTradeCommunicator().requestTradeInfo(symbol, Integer.toString(buyCount), true, Integer.toString(buyPrice));
//        }
    }

    private String orderSellToClient(String symbol, int count, int price) {
        if (price == 0) {
            return getTradeCommunicator().requestTradeInfo(symbol, Integer.toString(count), false, "0");
        } else {
            return getTradeCommunicator().requestTradeInfo(symbol, Integer.toString(count), false, Integer.toString(price));
        }
    }

    public void stopVVIPManagerThread() {
        if (null != instance && instance.isAlive()) {
            instance.interrupt();
        }
    }
}
