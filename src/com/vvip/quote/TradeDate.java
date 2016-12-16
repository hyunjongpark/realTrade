package com.vvip.quote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



public class TradeDate implements Comparable<TradeDate>{
	/**
	 * @uml.property  name="year"
	 */
	private int year;
	/**
	 * @uml.property  name="month"
	 */
	private int month;
	/**
	 * @uml.property  name="date"
	 */
	private int date;
	/**
	 * date format to yyyymmdd;
	 */
	private int yyyymmdd;
	
	
	public int getYyyymmdd(){
		return yyyymmdd;
	}
	
	public TradeDate() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		this.year = gc.get(Calendar.YEAR);
		this.month = gc.get(Calendar.MONTH) + 1;
		this.date = gc.get(Calendar.DATE);
	}
	
	public TradeDate(int year, int month, int date) {
		this.year = year;
		this.month = month;
		this.date = date;
	}
	
	public TradeDate(int data) {
		this.year = data / 10000;
		this.month =  (data % 10000) /100;
		this.date = data % 100;
	}
	
	public void setTrade(int data) {
		this.year = data / 10000;
		this.month =  (data % 10000) /100;
		this.date = data % 100;
	}
	
	public TradeDate(String date) throws TradeDateFormatException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = new GregorianCalendar();
		try {
			gc.setTime( (Date)formatter.parse(date) );
			this.year = gc.get(Calendar.YEAR);
			this.month = gc.get(Calendar.MONTH) + 1;
			this.date = gc.get(Calendar.DATE);
		} catch ( Exception e) {
			throw new TradeDateFormatException("Date Format is wrong");
		}
	}
	
	public TradeDate(Calendar cd) {
		year = cd.get(Calendar.YEAR);
		month = cd.get(Calendar.MONTH) + 1; 
		date = cd.get(Calendar.DAY_OF_MONTH);
	}
	
	public TradeDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		this.year = gc.get(Calendar.YEAR);
		this.month = gc.get(Calendar.MONTH) + 1;
		this.date = gc.get(Calendar.DATE);
	}
	
	/**
	 * @return
	 * @uml.property  name="year"
	 */
	public int getYear() {
		return this.year;
	}
	
	/**
	 * @return
	 * @uml.property  name="month"
	 */
	public int getMonth() {
		return this.month;
	}
	
	/**
	 * @return
	 * @uml.property  name="day"
	 */
	public int getDate() {
		return this.date;
	}

	
	/**
	 * 
	 * @return
	 */
	public int getDayOfWeek() {
		Calendar cal = toCalendar();
		return cal.get(Calendar.DAY_OF_WEEK);		
	}
	

	public boolean isWeekend() {
		Calendar cal =  toCalendar();
		return (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}
	
	public boolean equals(TradeDate td) {
		return (compareTo(td) == 0);
	}
	
	public boolean before(TradeDate td) {
		return (compareTo(td) > 0);
	}

	public boolean after(TradeDate td) {
		return (compareTo(td) < 0);
	}
	
	public TradeDate previous(int days) {

		Calendar cd = toCalendar();

		for (int i = 0; i < days; i++) {

			// Take 1 day or more to skip weekends as necessary
			do {
				cd.add(Calendar.DAY_OF_WEEK, -1);
			} while (cd.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| cd.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
		}

		// Create new date
		return new TradeDate(cd);
	}

	/**
	 * Create a new date which is the specified number of trading days after
	 * this date.
	 * 
	 * @param days
	 *            the number of days to move
	 * @return date which is <code>days</code> after the current one
	 * 
	 */
	public TradeDate next(int days) {

		Calendar cd = this.toCalendar();

		for (int i = 0; i < days; i++) {

			// Add 1 day or more to skip weekends as necessary
			do {
				cd.add(Calendar.DAY_OF_WEEK, 1);
			} while (cd.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| cd.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
		}

		// Create new date
		return new TradeDate(cd);
	}
	
	public Calendar toCalendar() {
		// Convert from our month of 1-12 to theirs of 0-11
		return new GregorianCalendar(getYear(), getMonth() - 1, getDate());
	}
	
	@Override
	public int compareTo(TradeDate td) {
		if ( year < td.year )
			return 1;
		if ( year > td.year )
			return -1;
		if ( month < td.month )
			return 1;
		if ( month > td.month )
			return -1;
		if ( date < td.date )
			return 1;
		if ( date > td.date )
			return -1;
		return 0;
	}	
	
	/**
	 * date format to yyyymmdd
	 * @return int yyyymmdd
	 */
	public int toInt() {
		int intDate = year*10000 + month*100 + date;

		return intDate;
	}	
	
	public String toString() {
		String m, d;
		if ( month < 10) 
			m = "0" + month;
		else
			m = String.valueOf(month);
		
		if ( date < 10 ) 
			d = "0" + date;
		else
			d = String.valueOf(date);
			
		return year + "." + m + "." + d;
	}
}
