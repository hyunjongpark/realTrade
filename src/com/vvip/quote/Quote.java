package com.vvip.quote;

public class Quote implements Comparable<Quote> {	
	private TradeDate date;
	private String symbol = "";
	private double low;
	private double high;
	private double open;
	private double close;
	private long volume;
	private String pattern ="";
	
	/** Quotes before this year are considered suspect and raise warnings. */
    private static final int SUSPECT_YEAR = 1900;
    private static final TradeDate SUSPECT_DATE = new TradeDate(SUSPECT_YEAR, 0, 0);    
	
    public Quote(TradeDate date, double low, double high, double open, double close, long volume, String pattern) {
		this.date = date;
		this.low = low;
		this.high = high;
		this.open = open;
		this.close = close;
		this.volume = volume;
		this.pattern = pattern;
	}
    
    public Quote(String symbol, TradeDate date, double low, double high, double open, double close, long volume) {
    	this.setSymbol(symbol);
		this.date = date;
		this.low = low;
		this.high = high;
		this.open = open;
		this.close = close;
		this.volume = volume;
		this.pattern = "";
	}  
    
    public Quote(TradeDate date, double low, double high, double open, double close, long volume) {
		this.date = date;
		this.low = low;
		this.high = high;
		this.open = open;
		this.close = close;
		this.volume = volume;
		this.pattern = "";
	}    
    
	public Quote(int year, int month, int day, double low, double high, double open, double close, long volume) {
		this.date = new TradeDate(year, month, day);
		this.low = low;
		this.high = high;
		this.open = open;
		this.close = close;
		this.volume = volume;
		this.pattern = "";
	}
	
	public Quote(int year, int month, int day, double low, double high, double open, double close, long volume, String pattern) {
		this.date = new TradeDate(year, month, day);
		this.low = low;
		this.high = high;
		this.open = open;
		this.close = close;
		this.volume = volume;
		this.pattern = pattern;
	}

	public TradeDate getTradeDate() {
		return date;
	}
	
	public double getLow() {
		return low;
	}
	
	public double getHigh() {
		return high;
	}
	
	public double getOpen() {
		return open;
	}
	
	public double getClose() {
		return close;
	}
	
	public long getVolume() {
		return volume;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public void setLow(double low) {
		this.low = low;
	}
	
	public void setHigh(double high) {
		this.high = high;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
    public String verify() {

        String message = "";
        String data = date.toString() + " Open : " + open + " Close : " + close + " Low : " + low + " High : " + high + " Volume :" + volume + "\n";  

        if(date.before(SUSPECT_DATE))
        	message += "Suspect Date : " +  date.toString() + "\n";

        if(date.after(new TradeDate()))
        	message += "FUTURE_DATE : " +  date.toString() + "\n";

        if(date.isWeekend())
        	message += "WEEKEND_QUOTE : " +  date.toString() + "\n";

        if(low > open || low > close || low > high) {
        	message += "DAY_LOW_NOT_LOWEST : " + low + " -> ";
            low = Math.min(Math.min(open, close), high);
            message += low + "\n";
        }

        if(high < open || high < close || high < low) {        	
        	message += "DAY_HIGH_NOT_HIGHEST : " + high + " -> ";
            high = Math.max(Math.max(open, close), low);
            message += " high" + "\n";
        }

        if(low < 0) {
        	message += "DAY_LOW_LESS_THAN_ZERO : " + low + " -> 0\n";
            low = 0;
        }

        if(high < 0) {
        	message += "DAY_HIGH_LESS_THAN_ZERO : " + high + " -> 0\n";
            high = 0;
        }

        if(open < 0) {
        	message += "DAY_OPEN_LESS_THAN_ZERO : " + open + " -> 0\n";
            open = 0;
        }

        if(close < 0) {
        	message += "DAY_CLOSE_LESS_THAN_ZERO : " + close + " -> 0\n";
            close = 0;
        }

        if(volume < 0) {
        	message += "DAY_VOLUME_LESS_THAN_ZERO : " + volume + " -> 0\n";
            volume = 0;
        }
        
        if ( message.equals("") )
        	message = null;
        else
        	message = data + message;
        
        return message;
    }
    
	@Override
	public int compareTo(Quote quote) {
		return date.compareTo(quote.getTradeDate());		
	}	
	
	public String toString() {		
		return date.toString() + " Open : " + open + " Close : " + close + " Low : " + low + " High : " + high + " Volume : " + volume;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
