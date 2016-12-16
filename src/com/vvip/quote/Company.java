package com.vvip.quote;

public class Company  implements Comparable<Company> {
	private String symbol;
	private int searchCount;
	private String nameInEng;
	private String nameInKor;
	private int type;
	
	private double Total;
	private double PER;
	private double faceValue;
	private double preSales; 
	private double preIncome;
	private double preTaxIncome;
	private double preNetProfit;
	private double preEPS;
	private double prePreSales;
	private double prePreIncome;
	private double prePreTaxIncome;
	private double prePreNetProfit;
	private double prePreEPS;
	
	public Company(String symbol, String nameEng) {
		this.symbol = symbol;
		this.nameInEng = nameEng;
		this.nameInKor = null;
		this.searchCount = 0;
	}
	public Company(String symbol, String nameEng, String nameKor) {
		this.symbol = symbol;
		this.nameInEng = nameEng;
		this.nameInKor = nameKor;
		this.searchCount = 0;
	}
	
	public Company(String symbol, String nameEng, int type) {
		this.symbol = symbol;
		this.nameInEng = nameEng;
		this.nameInKor = nameEng;
		this.type = type;
	}	
	public Company( String symbol, String nameEng, String nameKor, int searchCount, int marketIndex) {
		this.symbol = symbol;
		this.nameInEng = nameEng;
		if ( nameKor.equals("null") )
			nameKor = null;
		this.nameInKor = nameKor;
		this.searchCount = searchCount;
	}
	
	
	public Company(String symbol, String nameEng, double Total, double PER, double faceValue, double preSales, 
			double preIncome, double preTaxIncome, double preNetProfit, double preEPS, double prePreSales
			, double prePreIncome, double prePreTaxIncome, double prePreNetProfit, double prePreEPS, int type) {
		this.symbol = symbol;
		this.nameInEng = nameEng;
		this.Total = Total;
		this.PER= PER;
		this.faceValue= faceValue;
		this.preSales =preSales; 
		this.preIncome =preIncome;
		this.preTaxIncome = preTaxIncome;
		this.preNetProfit = preNetProfit;
		this.preEPS =preEPS;
		this.prePreSales=prePreSales;
		this.prePreIncome=prePreIncome;
		this.prePreTaxIncome=prePreTaxIncome;
		this.prePreNetProfit=prePreNetProfit;
		this.prePreEPS=prePreEPS;
		this.type = type;
	}
	public Company(String symbol, String nameEng, double Total) {
		this.symbol = symbol;
		this.nameInEng = nameEng;
		this.Total = Total;
		this.PER= PER;
		this.faceValue= faceValue;
		this.preSales =preSales; 
		this.preIncome =preIncome;
		this.preTaxIncome = preTaxIncome;
		this.preNetProfit = preNetProfit;
		this.preEPS =preEPS;
		this.prePreSales=prePreSales;
		this.prePreIncome=prePreIncome;
		this.prePreTaxIncome=prePreTaxIncome;
		this.prePreNetProfit=prePreNetProfit;
		this.prePreEPS=prePreEPS;
		this.type = type;
	}
	public double getPrice(){
		return preSales;
	}
	
	public boolean isOverProfit() {
		if (PER > 0 && preSales > 0 && preIncome > 0 && preTaxIncome > 0 && preNetProfit > 0 && preEPS > 0 && prePreSales > 0 && prePreIncome > 0 && prePreTaxIncome > 0 && prePreNetProfit > 0 && prePreEPS > 0 && preSales > prePreSales && preIncome > prePreIncome && preTaxIncome > prePreTaxIncome && preNetProfit > prePreNetProfit && preEPS > prePreEPS) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getSymbol() {
		return symbol;
	}

	public int getSearchCount() {
		return searchCount;
	}	
	
	public String getNameInEng() {
		return nameInEng;
	}
	
	public double getTotal() {
		return Total;
	}

	public String getNameInKor() {
		return nameInKor;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public String toString() {
		String str;
		str = symbol + " - " + nameInEng;		
		if(nameInKor != null)
			str = str + " (" + nameInKor + ")";
		return str;
	}
	
	@Override
	public int compareTo(Company company) {
		return symbol.compareTo(company.getSymbol());
	}
	
}
