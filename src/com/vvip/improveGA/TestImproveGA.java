package com.vvip.improveGA;

import java.util.ArrayList;
import java.util.List;

import com.vvip.quote.QuoteList;
import com.vvip.util.DatabaseManager;

public class TestImproveGA {
	public static void main(String[] args) {
		String symbol = "012860";
		DatabaseManager.getConnection();
		QuoteList quoteList = DatabaseManager.selectQuoteListByDate(symbol, 20140101, 20150501);
		
		List<ImproveGeneticAlgorithm> gaList = new ArrayList<ImproveGeneticAlgorithm>();
		for (int i = 0; i < 1; i++) {
			ImproveGeneticAlgorithm gaC = new ImproveGeneticAlgorithm(symbol, quoteList.getSize() * 1, 30, 0.9, 0.005, quoteList);
			gaC.startVVIPManagerThread(gaC, i);
			gaList.add(gaC);
		}

		for (int i = 0; i < 1; i++) {
			List<List<ImproveGene>> resultGene = gaList.get(i).getResultGene();
			List<ImproveGene> macdGeneList = resultGene.get(0);
			List<ImproveGene> stockGeneList = resultGene.get(1);

			for (ImproveGene data : macdGeneList) {
				data.toString();
			//	bestMacdGene.add(data);
			}
			for (ImproveGene data : stockGeneList) {
				data.toString();
			//	bestStochGene.add(data);
			}
		}
	
	}
}
