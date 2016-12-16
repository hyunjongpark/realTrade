package com.vvip.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vvip.quote.QuoteList;

public class GeneticAlgorithm extends Thread {

	private Thread instance;

	public final double DAY_OF_RISK_FREE_RATE = 0.035 / 365;
	public final double SELL_CHARGE = 0.00015;
	public final double BUY_CHARGE = 0.00015;
	public final double TAX_RATE = 0.003;
	final double SELECTION_PRESSURE = 4;
	final int GENE_SIZE = 24;
	public final static int BUY = 1;
	public final static int SELL = 2;
	public final static int HOLD = 3;
	public final static int READY = 4;

	int population;
	int maxGeneration;
	int generation;
	double crossoverRate;
	double mutationRate;
	ArrayList<Gene> returnStochBestGenes = new ArrayList<Gene>();
	ArrayList<Gene> returnMacdBestGenes = new ArrayList<Gene>();
	ArrayList<Gene> macdGenes = new ArrayList<Gene>();
	ArrayList<Gene> stochGenes = new ArrayList<Gene>();

	int geneX, geneY;
	int status;
	double sumFitness;
	double close[];
	double high[];
	double low[];
	int date[];
	long volume[];
	List<String> patternList = new ArrayList<String>();
	Set<String> pattenHashSet = new HashSet<String>();

	double startTradePrice = 0;

	int logStartDay = 0;

	boolean debugPrint = true;

	List<String> buyPatten = new ArrayList<String>();

	private String symbol = "";

	QuoteList quoteList = null;

	private boolean isEnd = false;

	public GeneticAlgorithm(String symbol, int population, int maxGeneration, double crossoverRate, double mutationRate, QuoteList quoteList ) {
		this.symbol = symbol;
		this.population = population;
		this.maxGeneration = maxGeneration;
		this.generation = 0;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		macdGenes = new ArrayList<Gene>();
		returnStochBestGenes = new ArrayList<Gene>();
		returnMacdBestGenes = new ArrayList<Gene>();
		this.quoteList = quoteList;
		setQuote(quoteList);
	}

	@Override
	public void run() {
		gaStart();
	}

	public void fitnessMeasure() {
		for (int i = 0; i < population; i++) {
			int startProfitDay = date[0];
			int endProfitDay = date[date.length - 1];
			
			GAcalculateProfit profitStochThread = new GAcalculateProfit(GAcalculateProfit.CAL_STOCH, startProfitDay, endProfitDay, close,  high, low, date, volume, macdGenes.get(i), false, quoteList, false);
			profitStochThread.setSymbol(symbol);
			profitStochThread.run();
			if (profitStochThread.getProfit() > 0) {
				Gene gene = new Gene(0, 0, 0, macdGenes.get(i).getFastK(), macdGenes.get(i).getSlowK(), macdGenes.get(i).getSlowD());
				gene.setProfit(profitStochThread.getProfit());
				macdGenes.get(i).setProfit(profitStochThread.getProfit());
				boolean isSameProfot = false;
				for (int j = 0; j < returnStochBestGenes.size(); j++) {
					if (returnStochBestGenes.get(j).getProfit() == profitStochThread.getProfit() &&
							returnStochBestGenes.get(j).getShortMA() == gene.getShortMA() &&
							returnStochBestGenes.get(j).getLongMA() == gene.getLongMA() &&
							returnStochBestGenes.get(j).getMacdMA() == gene.getMacdMA()) {
						isSameProfot = true;
						break;
					}
				}
				if (!isSameProfot) {
					returnStochBestGenes.add(gene);
				}
			}
			
			GAcalculateProfit profitMacdThread = new GAcalculateProfit(GAcalculateProfit.CAL_MACD, startProfitDay, endProfitDay, close,  high, low, date, volume, macdGenes.get(i), false, quoteList, false);
			profitMacdThread.setSymbol(symbol);
			profitMacdThread.run();
			if (profitMacdThread.getProfit() > 0) {
				Gene gene = new Gene(macdGenes.get(i).getShortMA(), macdGenes.get(i).getLongMA(), macdGenes.get(i).getMacdMA(), 0, 0, 0);
				gene.setProfit(profitMacdThread.getProfit());
				
				if (profitMacdThread.getProfit() > macdGenes.get(i).getProfit()){
					macdGenes.get(i).setProfit(profitMacdThread.getProfit());
				}
				
				boolean isSameProfot = false;
				for (int j = 0; j < returnMacdBestGenes.size(); j++) {
					if (returnMacdBestGenes.get(j).getProfit() == profitMacdThread.getProfit() &&
							returnMacdBestGenes.get(j).getSlowK() == gene.getSlowK() &&
									returnMacdBestGenes.get(j).getSlowD() == gene.getSlowD() &&
											returnMacdBestGenes.get(j).getFastK() == gene.getFastK()) {
						isSameProfot = true;
						break;
					}
				}
				if (!isSameProfot) {
					returnMacdBestGenes.add(gene);
				}
			}
			
		}
		
		Collections.sort(macdGenes);
		Collections.sort(returnStochBestGenes);
		Collections.sort(returnMacdBestGenes);
		double maxProfit = macdGenes.get(0).getProfit();
		double minProfit = macdGenes.get(macdGenes.size() - 1).getProfit();
		double pressure = (maxProfit - minProfit) / (SELECTION_PRESSURE - 1);

		sumFitness = 1;
		for (int i = 0; i < macdGenes.size(); i++) {
			double fitness = macdGenes.get(i).getProfit() - minProfit + pressure;
			fitness = Double.parseDouble(String.format("%.3f", fitness));
			sumFitness += fitness;
			macdGenes.get(i).setFitness(fitness);
		}

	}

	public void gaStart() {
		initializePopulation();
		fitnessMeasure();

		while (terminate() == false) {
			generation++;

			for (int i = 0; i < population * crossoverRate; i++) {
				selection();
				crossover();
			}
			mutation();
			fitnessMeasure();
		}

		isEnd = true;
	}

	public List<Gene> getResultGene() {
		while (true) {
			if (isEnd) {
				break;
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int nSize = returnMacdBestGenes.size() > returnStochBestGenes.size() ? returnStochBestGenes.size() : returnMacdBestGenes.size();
		ArrayList<Gene> returnBestGenes = new ArrayList<Gene>();
		for (int i = 0; i < nSize; i++){
			Gene gene = new Gene(returnMacdBestGenes.get(i).getShortMA(), returnMacdBestGenes.get(i).getLongMA(), returnMacdBestGenes.get(i).getMacdMA(), returnStochBestGenes.get(i).getFastK(), returnStochBestGenes.get(i).getSlowK(), returnStochBestGenes.get(i).getSlowD());
			double profit = returnStochBestGenes.get(i).getProfit() > returnMacdBestGenes.get(i).getProfit() ? returnStochBestGenes.get(i).getProfit(): returnMacdBestGenes.get(i).getProfit();
			gene.setProfit(profit);
			returnBestGenes.add(gene);
		}
		return returnBestGenes;
	}

	public void startVVIPManagerThread(GeneticAlgorithm obj, int indexNumber) {
		if (null == instance || !instance.isAlive()) {
			instance = new Thread(null, obj, "GeneticAlgorithm_" + indexNumber);
			instance.start();
		}
	}

	public void stopVVIPManagerThread() {
		if (null != instance && instance.isAlive()) {
			instance.interrupt();
		}
	}

	public void setLogStartDay(int logStartDay) {
		this.logStartDay = logStartDay;
	}

	public GeneticAlgorithm(QuoteList quoteList) {
		setQuote(quoteList);
	}

	private void setQuote(QuoteList quoteList) {
		if (null == quoteList) {
			return;
		}
		close = new double[quoteList.getSize()];
		high = new double[quoteList.getSize()];
		low = new double[quoteList.getSize()];
		date = new int[quoteList.getSize()];
		volume = new long[quoteList.getSize()];
		for (int i = 0; i < quoteList.getSize(); i++) {
			close[i] = quoteList.getQuote(i).getClose();
			high[i] = quoteList.getQuote(i).getHigh();
			low[i] = quoteList.getQuote(i).getLow();
			date[i] = quoteList.getQuote(i).getTradeDate().toInt();
			volume[i] = quoteList.getQuote(i).getVolume();
			patternList.add(quoteList.getQuote(i).getPattern());
			pattenHashSet.add(quoteList.getQuote(i).getPattern());
		}
	}

	/**
	 * 2 <= Short MA <= 127 3 <= Long MA <= 255 2 <= Macd MA <= 255
	 */
	public void initializePopulation() {
		macdGenes.clear();
		for (int i = 0; i < population; i++) {
			int shortMA = getRandomNumber(2, 127);
			int longMA = getRandomNumber(shortMA + 1, 255);
			int macdMA = getRandomNumber(2, 255);
			int fastD = getRandomNumber(2, 255);
			int slowK = getRandomNumber(2, 255);
			int slowD = getRandomNumber(2, 255);
			stochGenes.add(new Gene(shortMA, longMA, macdMA, fastD, slowK, slowD));
			macdGenes.add(new Gene(shortMA, longMA, macdMA, fastD, slowK, slowD));
		}
	}

	int getRandomNumber(int to, int from) {
		int returnValue = (int) (Math.random() * (from + 1 - to)) + to;
		return returnValue;
	}

	public ArrayList<Gene> getGene() {
		return macdGenes;
	}

	public void selection() {
		do {
			geneX = selectGene();
		} while (geneX == 0);

		do {
			geneY = selectGene();

		} while (geneY == geneX || geneY == 0);
	}

	public int selectGene() {
		if (sumFitness == 1) {
			sumFitness = 1000;
		}
		int point = (int) (Math.random() * sumFitness);
		int sum = 0;
		for (int i = 0; i < macdGenes.size(); i++) {
			sum += (macdGenes.get(i).getFitness() + i);
			if (point <= sum)
				return i;
		}
		return macdGenes.size() - 1;
	}

	public void crossover() {
		int to = getRandomNumber(0, GENE_SIZE - 2);
		int from = getRandomNumber(to, GENE_SIZE - 1);

		char[] xBinary = macdGenes.get(geneX).getBinaryGene();
		char[] yBinary = macdGenes.get(geneY).getBinaryGene();

		for (int i = to; i <= from; i++) {
			char temp = xBinary[i];
			xBinary[i] = yBinary[i];
			yBinary[i] = temp;
		}

		macdGenes.get(geneX).setGene(xBinary);
		macdGenes.get(geneY).setGene(yBinary);

	}

	public void mutation() {
		for (int i = 0; i < population; i++) {
			if (Math.random() < mutationRate) {
				char[] binary = macdGenes.get(i).getBinaryGene();
				reverseBinary(binary, 1, GENE_SIZE - 1);
				macdGenes.get(i).setGene(binary);
			}
		}
	}

	public boolean terminate() {
		return maxGeneration <= generation;
	}

	public int getLastStatus() {
		return status;
	}

	public void reverseBinary(char[] binary, int to, int from) {
		for (int i = to; i <= from; i++) {
			if (binary[i] == '0')
				binary[i] = '1';
			else
				binary[i] = '0';
		}
	}

	public double profitToPercentage(double profit) {
		if (profit == 0) {
			return 0.0;
		}
		return Double.parseDouble(String.format("%.3f", profit / startTradePrice * 100));
	}

	public static void main(String[] args) {
	}

}
