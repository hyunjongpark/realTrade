package com.vvip.ga;

import com.vvip.quote.TradeDate;

public class GeneResult {
	Gene gene;
	String symbol = "";
	int status = 0;
	String pattern ="";
	TradeDate start;
	TradeDate end;

	public GeneResult(String symbol, Gene gene, int status, TradeDate start, TradeDate end) {
		this.symbol = symbol;
		this.gene = gene;
		this.status = status;
		this.start = start;
		this.end = end;
	}

	public GeneResult(String symbol, Gene gene, TradeDate start, TradeDate end) {
		this.symbol = symbol;
		this.gene = gene;
		this.start = start;
		this.end = end;
	}
	
	public GeneResult(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public Gene getGene() {
		return gene;
	}

	public int getStatus() {
		return status;
	}

	public TradeDate getStart() {
		return start;
	}

	public TradeDate getEnd() {
		return end;
	}

	public String toString() {
		String str = gene.toString() + " Last Status : ";
		if (status == GeneticAlgorithm.BUY)
			str += "BUY";
		else if (status == GeneticAlgorithm.SELL)
			str += "SELL";
		else if (status == GeneticAlgorithm.HOLD)
			str += "HOLD";
		else
			str += "READY";

		return str;
	}
}
