package com.vvip.improveGA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vvip.quote.QuoteList;

public class ImproveGeneticAlgorithm extends Thread {

	private Thread instance;

	public final double DAY_OF_RISK_FREE_RATE = 0.035 / 365;
	public final double SELL_CHARGE = 0.00015;
	public final double BUY_CHARGE = 0.00015;
	public final double TAX_RATE = 0.003;
	final double SELECTION_PRESSURE = 4;
	final int GENE_SIZE = 48;
	public final static int BUY = 1;
	public final static int SELL = 2;
	public final static int HOLD = 3;
	public final static int READY = 4;

	int population;
	int maxGeneration;
	int generation;
	double crossoverRate;
	double mutationRate;
	ArrayList<ImproveGene> macdGenes;
	ArrayList<ImproveGene> stochGenes;

	int geneX, geneY;
	int status;
	double sumFitness;
	double close[];
	double high[];
	double low[];
	int date[];
	List<String> patternList = new ArrayList<String>();
	Set<String> pattenHashSet = new HashSet<String>();

	double startTradePrice = 0;

	int logStartDay = 0;

	boolean debugPrint = true;

	List<String> buyPatten = new ArrayList<String>();

	private String symbol = "";


	private boolean isEnd = false;

	public ImproveGeneticAlgorithm(String symbol, int population, int maxGeneration, double crossoverRate, double mutationRate, QuoteList quoteList) {
		this.symbol = symbol;
		this.population = population;
		this.maxGeneration = maxGeneration;
		this.generation = 0;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		macdGenes = new ArrayList<ImproveGene>();
		stochGenes = new ArrayList<ImproveGene>();

		setQuote(quoteList);
	}

	@Override
	public void run() {
		gaStart();
	}

	public void fitnessMeasure() {
		for (int i = 0; i < population; i++) {
			int startProfitDay = date[0];
			int endProfitDay = date[date.length-1];
			ImporveGAcalculateProfit profitThread = new ImporveGAcalculateProfit(startProfitDay, endProfitDay, close, date, macdGenes.get(i).getShortMA(), macdGenes.get(i).getLongMA(), macdGenes.get(i).getMacdMA(), true);
			profitThread.setSymbol(symbol);
			profitThread.run();
			
			System.out.println("profit : " + profitThread.getProfit() + " ShortMA: " + macdGenes.get(i).getShortMA()+" LongMA : " + macdGenes.get(i).getLongMA() + " MacdMA: " + macdGenes.get(i).getMacdMA());
			
			
			if(profitThread.getProfit() > 0){
				boolean isSameProfot = false;
				for(int j = 0 ;j < macdGenes.size() ; j++){
					if(macdGenes.get(j).getProfit() == profitThread.getProfit()){
						isSameProfot = true;
						break;
					}
				}
				if(!isSameProfot){
					macdGenes.get(i).setProfit(profitThread.getProfit());	
				}
			}
		}
		Collections.sort(macdGenes);
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
	
	public ImproveGene gaStart() {
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
		return macdGenes.get(0);
	}

	public List<List<ImproveGene>> getResultGene() {
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
		List<List<ImproveGene>> returnGene = new ArrayList<List<ImproveGene>>();
		returnGene.add(macdGenes);
		returnGene.add(stochGenes);
		return returnGene;
	}

	public void startVVIPManagerThread(ImproveGeneticAlgorithm obj, int indexNumber) {
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

	public ImproveGeneticAlgorithm(QuoteList quoteList) {
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

		for (int i = 0; i < quoteList.getSize(); i++) {
			close[i] = quoteList.getQuote(i).getClose();
			high[i] = quoteList.getQuote(i).getHigh();
			low[i] = quoteList.getQuote(i).getLow();
			date[i] = quoteList.getQuote(i).getTradeDate().toInt();
			patternList.add(quoteList.getQuote(i).getPattern());
			pattenHashSet.add(quoteList.getQuote(i).getPattern());
		}
	}

	/**
	 * 2 <= Short MA <= 127 3 <= Long MA <= 255 2 <= Macd MA <= 255
	 */
	public void initializePopulation() {
		macdGenes.clear();
		stochGenes.clear();
		for (int i = 0; i < population; i++) {
			int shortMA = getRandomNumber(2, 127);
			int longMA = getRandomNumber(shortMA + 1, 255);
			int macdMA = getRandomNumber(2, 255);
			int fastD = getRandomNumber(2, 255);
			int slowK = getRandomNumber(2, 255);
			int slowD = getRandomNumber(2, 255);
			macdGenes.add(new ImproveGene(shortMA, longMA, macdMA, fastD, slowK, slowD));
			stochGenes.add(new ImproveGene(shortMA, longMA, macdMA, fastD, slowK, slowD));
		}
	}

	int getRandomNumber(int to, int from) {
		int returnValue = (int) (Math.random() * (from + 1 - to)) + to;
		return returnValue;
	}

	

	public ArrayList<ImproveGene> getGene() {
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
