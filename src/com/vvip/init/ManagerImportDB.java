package com.vvip.init;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.vvip.ga.Gene;
import com.vvip.ga.GeneResult;
import com.vvip.quote.TradeDate;
import com.vvip.util.DatabaseManager;

public class ManagerImportDB {

	public static void saveMacdGeneToFile() {
		ArrayList<GeneResult> grList = DatabaseManager.selectGeneResultListByIndex();
		String path = VVIPManager.VVIP_PATH + File.separator + "macd" + File.separator + "macd_" + grList.size();
		String message;
		FileWriter writer;
		try {
			for (GeneResult gene : grList) {
				message = gene.getSymbol() + "!" + gene.getGene().getStringBinaryGene() + "!"
						+ gene.getGene().getProfit() + "!" + gene.getStatus() + "!" + gene.getStart().toInt() + "!"
						+ gene.getEnd().toInt() + "\n";
				writer = new FileWriter(path, true);
				BufferedWriter bwriter = new BufferedWriter(writer);
				bwriter.write(message);
				bwriter.close();
				writer.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insertMacdGeneFromFile(String fileName) {
		String path = VVIPManager.VVIP_PATH + File.separator + "macd" + File.separator + fileName;
		ArrayList<GeneResult> insertGrList = new ArrayList<GeneResult>();
		FileReader dataFile = null;
		BufferedReader bufferedReader = null;
		try {
			dataFile = new FileReader(path);
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "MS949"));
			String line;
			line = bufferedReader.readLine();
			while (line != null) {
				line = bufferedReader.readLine();
				if(null == line){
					break;
				}
				if (line.contains("!")) {
					String data[] = line.split("!");
					try {
						GeneResult gene = new GeneResult(data[0].trim(),
								new Gene(data[1], Double.parseDouble(data[2])), Integer.parseInt(data[3]),
								new TradeDate(Integer.parseInt(data[4])), new TradeDate(Integer.parseInt(data[4])));
						insertGrList.add(gene);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			bufferedReader.close();
			dataFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	DatabaseManager.deleteAllGATable();
		for (GeneResult gene : insertGrList) {
			DatabaseManager.insertGene(gene);
		}
	}
}
