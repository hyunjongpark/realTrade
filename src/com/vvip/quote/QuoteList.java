package com.vvip.quote;

import java.util.ArrayList;
import java.util.Collections;


public class QuoteList {
	private String symbol;
	
	
	/**
	 * @uml.property  name="quotes"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.vvip.quote.Quote"
	 */
	private ArrayList<Quote> quotes;
	
	/**
	 * @uml.property  name="count"
	 */
	private int size;
	
	/**
	 * @param symbolIndex
	 * @param MarketIndex
	 */
	public QuoteList(String symbol)
	{
		this.symbol = symbol;
		quotes = new ArrayList<Quote> ();
		size = 0;
	}
	
	
	public void addQuote(int year, int month, int day, double low, double high, double open, double close, long volumn)
	{
		quotes.add(new Quote(year, month, day, low, high, open, close, volumn, null));
		size++;
	}
	
	public void addQuote(int year, int month, int day, double low, double high, double open, double close, long volumn, String pattern)
	{
		quotes.add(new Quote(year, month, day, low, high, open, close, volumn, pattern));
		size++;
	}
	
	public void addQuote(Quote quote)
	{
		quotes.add(quote);
		size++;
	}
	
	public String getSymbolIndex() {
		return symbol;
	}
	
	public ArrayList<Quote> getList() {
		return quotes;
	}
	
	public int getSize() {
		return size;
	}
	
	public Quote getQuote(int i)
	{
		return quotes.get(i);
	}
	
	public TradeDate getLastDate() {
		return quotes.get(size-1).getTradeDate();
	}
	
	
	public void sortList()
	{
		Collections.sort(quotes);
	}
	
	public void reverseList() {
		Collections.reverse(quotes);
	}
	
}
