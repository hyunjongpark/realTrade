package com.vvip.trade;

import java.util.ArrayList;
import java.util.List;

import com.vvip.util.DatabaseManager;

public class TradeTypeManager {
	public static List<TradeType> tradeSymbolManager = new ArrayList<TradeType>();

	public static void setTodaySymbol() {
//		String lastDay = DatabaseManager.getStockLastDay();
//		if(null != lastDay){
//			tradeSymbolManager = DatabaseManager.selectBuyTrade(lastDay);
//		//	TradeType.setTradeTypeFromFile();
//			System.out.println(tradeSymbolManager.get(0).buyCount);	
//		}
	}
}
