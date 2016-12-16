package com.vvip.ga;

public class Gene implements Comparable<Gene> {
	int shortMA;
	int longMA;
	int macdMA;
	int fastK;
	int slowK;
	int slowD;
	double profit;
	double fitness;
	int status;
	int indexNumber;
	String pattern ="";

	 // 2 <= Short MA <= 127 3 <= Long MA <= 255 2 <= Macd MA <= 255
	public Gene(int shortMA, int longMA, int macdMA, int fastK, int slowK, int slowD) {
		this.shortMA = shortMA;
		this.longMA = longMA;
		this.macdMA = macdMA;
		this.fastK = fastK;
		this.slowK = slowK;
		this.slowD = slowD;
		this.profit = -100;
		this.fitness = 0;
	}

	public Gene(String binaryGene, double profit) {
		setGene(binaryGene.toCharArray());
		this.profit = profit;
		this.fitness = 0;
	}
	
	
	public String getPattern(){
		return pattern;
	}
	public void setPattern(String pattern){
		this.pattern = pattern;
	}

	public char[] getBinaryGene() {
		String gene;
		gene = IntegerToBinary(shortMA) + IntegerToBinary(longMA) + IntegerToBinary(macdMA) + IntegerToBinary(fastK) + IntegerToBinary(slowK) + IntegerToBinary(slowD);
		return gene.toCharArray();
	}
	
	public String getStringBinaryGene() {
		return IntegerToBinary(shortMA) + IntegerToBinary(longMA) + IntegerToBinary(macdMA) + IntegerToBinary(fastK) + IntegerToBinary(slowK) + IntegerToBinary(slowD);
	}

	public int getShortMA() {
		return shortMA;
	}

	public char[] getBinaryShortMA() {
		return IntegerToBinary(shortMA).toCharArray();
	}

	public int getLongMA() {
		return longMA;
	}

	public char[] getBinaryLongMA() {
		return IntegerToBinary(longMA).toCharArray();
	}

	public int getMacdMA() {
		return macdMA;
	}

	public char[] getBinaryMacdMA() {
		return IntegerToBinary(macdMA).toCharArray();
	}

	public int getFastK() {
		return fastK;
	}

	public char[] getBinaryFastK() {
		return IntegerToBinary(fastK).toCharArray();
	}

	public int getSlowK() {
		return slowK;
	}

	public char[] getBinarySlowK() {
		return IntegerToBinary(slowK).toCharArray();
	}

	public int getSlowD() {
		return slowD;
	}

	public char[] getBinarySlowD() {
		return IntegerToBinary(slowD).toCharArray();
	}

	public double getProfit() {
		return profit;
	}

	public double getFitness() {
		return fitness;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public void setGene(int shortMA, int longMA, int macdMA, int fastK, int slowK, int slowD) {
		this.shortMA = shortMA;
		this.longMA = longMA;
		this.macdMA = macdMA;
		this.fastK = fastK;
		this.slowK = slowK;
		this.slowD = slowD;
	}

	public void setGene(char[] geneChar) {
		String gene = String.valueOf(geneChar);
		String valueStr = gene.substring(0, 8);
		this.shortMA = Integer.parseInt(valueStr, 2);
		valueStr = gene.substring(8, 16);
		this.longMA = Integer.parseInt(valueStr, 2);
		valueStr = gene.substring(16, 24);
		this.macdMA = Integer.parseInt(valueStr, 2);
		valueStr = gene.substring(24, 32);
		this.fastK = Integer.parseInt(valueStr, 2);
		valueStr = gene.substring(32, 40);
		this.slowK = Integer.parseInt(valueStr, 2);
		valueStr = gene.substring(40);
		this.slowD = Integer.parseInt(valueStr, 2);
		checkGene();
	}

	public void setGene(char[] shortMA, char[] longMA, char[] macdMA) {
		this.shortMA = Integer.parseInt(String.valueOf(shortMA), 2);
		this.longMA = Integer.parseInt(String.valueOf(longMA), 2);
		this.macdMA = Integer.parseInt(String.valueOf(macdMA), 2);
		this.fastK = Integer.parseInt(String.valueOf(fastK), 2);
		this.slowK = Integer.parseInt(String.valueOf(slowK), 2);
		this.slowD = Integer.parseInt(String.valueOf(slowD), 2);

		checkGene();
	}

	public void checkGene() {
		if (this.shortMA > this.longMA) {
			int temp = this.shortMA;
			this.shortMA = this.longMA;
			this.longMA = temp;
		}

		if (shortMA > 127)
			shortMA -= 127;

		if (shortMA < 2)
			shortMA = 2;

		if (longMA < 2)
			longMA = 2;

		if (macdMA < 2)
			macdMA = 2;

		if (fastK < 2)
			fastK = 2;
		if (slowK < 2)
			slowK = 2;
		if (slowD < 2)
			slowD = 2;

		if (shortMA == longMA)
			longMA += longMA;
	}

	public String IntegerToBinary(int value) {
		String binaryStr = Integer.toBinaryString(value);
		while (binaryStr.length() < 8)
			binaryStr = "0" + binaryStr;
		return binaryStr;
	}

	@Override
	public int compareTo(Gene gene) {
		if (this.profit < gene.getProfit())
			return 1;
		else if (this.profit == gene.getProfit())
			return 0;
		else
			return -1;
	}

	public String toString() {
		String str = " Profit : " + profit + "%" + " shortMA : " + shortMA + " longMA : " + longMA + " macdMA : " + macdMA + " fastK : " + fastK + " slowK : " + slowK + " slowD : " + slowD+" pattern : "+pattern;
		return str;

	}

	public void setSymbol(String symbol) {
		// TODO Auto-generated method stub
		
	}
}
