package com.vvip.util;

import java.util.ArrayList;

public class ChartPattern {
	private static ChartPattern instance = null;
	private static ArrayList<String> patternList = new ArrayList<String > ();
	
	private ChartPattern() {
		patternList = new ArrayList<String> ();
		patternList.add("2 Crows");
		patternList.add("3 Black Crows");
		patternList.add("3 Inside Up/Down");
		patternList.add("3 Line Strike");
		patternList.add("3 Outside Up/Down");
		patternList.add("3 Stars in the South");
		patternList.add("3 Advancing White Soldiers");
		patternList.add("Abandoned Baby");
		patternList.add("Advance Block");
		patternList.add("Belt-hold");
		patternList.add("Breakaway");
		patternList.add("Closing Marubozu");
		patternList.add("Concealing Baby Swallow");
		patternList.add("Counterattack");
		patternList.add("Dark Cloud Cover");
		patternList.add("Doji");
		patternList.add("Doji Star");
		patternList.add("Dragonfly Doji");
		patternList.add("Engulfing Pattern");
		patternList.add("Evening Doji Star");
		patternList.add("Evening Star");
		patternList.add("Up/Down-gap White Lines");
		patternList.add("Gravestone Doji");
		patternList.add("Hammer");
		patternList.add("Hanging Man");
		patternList.add("Harami Pattern");
		patternList.add("Harami Cross Pattern");
		patternList.add("High Wave");
		patternList.add("Homing Pigeon");
		patternList.add("Indentical Three Crows");
		patternList.add("In Neck PAttern");
		patternList.add("Inverted Hammer");
		patternList.add("Kicking");
		patternList.add("Ladder Bottom");
		patternList.add("Long Legged Doji");
		patternList.add("Marubozu");
		patternList.add("Matching Low");
		patternList.add("Mat Hold");
		patternList.add("Morning Doji Star");
		patternList.add("Morning Star");
		patternList.add("On Neck Pattern");
		patternList.add("Piercing Pattern");
		patternList.add("Rickshaw Man");
		patternList.add("Rising/Falling 3 Methods");
		patternList.add("Seperating Lines");
		patternList.add("Shooting Star");
		patternList.add("Spinning Top");
		patternList.add("Stalled Pattern");
		patternList.add("Stick Sandwich");
		patternList.add("Takuri");
		patternList.add("Tasuki Gap");
		patternList.add("Thrusting Pattern");
		patternList.add("Tristar Pattern");
		patternList.add("Unique 3 River");
		patternList.add("Upside Gap Two Crows");
		patternList.add("Upside/Downside Gap 3 Methods");
	}
	
	public static ChartPattern getInstance() {
		if ( instance == null ) 
			instance = new ChartPattern();
		return instance;
	}			
	
	public void addPattern(String pattern) {
		patternList.add(pattern);
	}
	
	public static String getPattern(int index) {
		return patternList.get(index);
	}
	
	public int getPatternCount() {
		return patternList.size();
	}
	public String convertPattern(String patternData) {
		if (null == patternData){
			return "";
		}
		String pattern = "\"";
		int size = patternData.length();
		for (int i = 0; i < size; i++) {
			if (patternData.charAt(i) == 'M')
				pattern += "M@" + getPattern(i) + "##";
			else if (patternData.charAt(i) == 'P')
				pattern += "P@" + getPattern(i) + "##";
			else if (patternData.charAt(i) == 'F')
				pattern += "F@" + getPattern(i) + "##";
		}
		pattern += "\"";
		return pattern;
	}	
}
