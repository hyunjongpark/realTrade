package com.vvip.init;

import java.util.ArrayList;
import java.util.List;

import com.vvip.ga.CreateMacdGeneData;
import com.vvip.ga.GAcalculateProfit;

public class RunMacdGeneData implements Runnable {
	private Thread instance;

	@Override
	public void run() {
		List<String> symbolList = CommonUtil.checkPastMacdCount();
		if(symbolList.size() > 0 ){
			makeMacdGeneDataForPastEndDate(symbolList);	
		}else{
			addMacdGeneData();
		}
	}
	
	private void addMacdGeneData(){
		GAcalculateProfit.printDebug = false;
		for (int i = 0; i < CreateMacdGeneData.totalThreadColunt; i++) {
			Thread instance = new Thread(null, new CreateMacdGeneData(null), "InitGATable");
			instance.start();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void makeMacdGeneDataForPastEndDate(List<String> symbolList){
		GAcalculateProfit.printDebug = false;
		List<List<String>> tradeList = new ArrayList<List<String>>();
		for (int i = 0; i < symbolList.size(); i++) {
			tradeList.add(new ArrayList<String>());
		}
		for (int i = 0; i < symbolList.size(); i++) {
			int index = i % CreateMacdGeneData.totalThreadColunt;
			tradeList.get(index).add(symbolList.get(i));
		}
		for (int i = 0; i < CreateMacdGeneData.totalThreadColunt; i++) {
			if(i >= tradeList.size()){
				break;
			}
			Thread instance = new Thread(null, new CreateMacdGeneData(tradeList.get(i)), "InitGATable");
			instance.start();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void startVVIPManagerThread() {
		if (null == instance || !instance.isAlive()) {
			instance = new Thread(null, new RunMacdGeneData(), "MakeMacdGeneData");
			instance.start();
		}
	}

}
