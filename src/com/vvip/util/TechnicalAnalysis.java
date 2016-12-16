/* TA-LIB Copyright (c) 1999-2007, Mario Fortier
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * - Neither name of author nor the names of its contributors
 *   may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/* List of contributors:
 *
 *  Initial  Name/description
 *  -------------------------------------------------------------------
 *  MF       Mario Fortier
 *  BT       Barry Tsung
 *
 * Change history:
 *
 *  MMDDYY BY     Description
 *  -------------------------------------------------------------------
 *  121005 MF     First Version
 *  022206 BT     1. initialization of candleSettings
 *                2. add SetCompatibility and GetCompatibility
 *                3. add SetUnstablePeriod, GetUnstablePeriod
 */

package com.vvip.util;

import java.util.ArrayList;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import com.vvip.quote.Quote;

public class TechnicalAnalysis {
	private Core ta_lib;
	private int size;	
	private ArrayList<Quote> quotes;
	private int startIndex;
	private int index;
	
	private double open[];
	private double close[];
	private double low[];
	private double high[];
	private double volume[];
	private char ta[][];

	private double optInPenetration = 3.000000e-1;
	
    private int outInteger[];    
    private MInteger outBegIdx;    
    private MInteger outNBElement;    
    private RetCode retCode;

    public static final int DEFAULT_DAY = 50;
	
	public TechnicalAnalysis(ArrayList<Quote> quotes, int index) {
		this.quotes = quotes;
		this.index = index;
		this.startIndex = (( index-DEFAULT_DAY < 0) ? 0 : index-DEFAULT_DAY);
		
		this.size = quotes.size() - startIndex; 
		
		ta_lib = new Core();		
		
		open = new double [size];
		close = new double [size];
		low = new double [size];
		high = new double [size];
		volume = new double [size];
		ta = new char [size][60];
		
		outInteger = new int   [size];
		outBegIdx = new MInteger();
		outNBElement = new MInteger();
		
		setQuoteData();
	}
	
	private void setQuoteData() {		
		for ( int i = 0; i < size; i++ ) {
			if(null == quotes.get(startIndex+i)){
				break;
			}
			open[i] = quotes.get(startIndex+i).getOpen();
			close[i] = quotes.get(startIndex+i).getClose();
			low[i] = quotes.get(startIndex+i).getLow();
			high[i] = quotes.get(startIndex+i).getHigh();
			volume[i] = quotes.get(startIndex+i).getVolume();
			ta[i][0] = '\0';
		}
	}
	
	public void macd(int shortDay, int longDay, int oscDay, ArrayList<Double> ret_macd, ArrayList<Double> ret_signal, ArrayList<Double> ret_hist) {
		double [] macd = new double[close.length];
		double [] signal = new double[close.length];
		double [] hist = new double[close.length];
		retCode = ta_lib.macd(0, close.length - 1, close, shortDay, longDay, oscDay, outBegIdx,outNBElement, macd, signal, hist);

		setReturnArray(macd, ret_macd);
		setReturnArray(signal, ret_signal);
		setReturnArray(hist, ret_hist);
	}
	
	public void sma(int day, ArrayList<Double> ret_sma) {
		double sma[] = new double[close.length];
		
		retCode = ta_lib.sma(0, close.length - 1, close, day, outBegIdx,outNBElement, sma);
		setReturnArray(sma, ret_sma);		
	}
	
	public void testma(int day, ArrayList<Double> ret_ema, double [] ret) {
		double ema[] = new double[close.length];		
		retCode = ta_lib.sma(0, close.length - 1, close, day, outBegIdx,outNBElement, ema);
		
		setReturnArray(ema, ret_ema);
		setReturnArray(ema, ret);		
	}
	
//	public void bollingerBend(int day, ArrayList<Double> ret_bollingerU, ArrayList<Double> ret_bollingerM, ArrayList<Double> ret_bollingerL) {
	public void bollingerBend(int day, double [] ret_bollingerU, double [] ret_bollingerM, double [] ret_bollingerL) {
		double bbU[] = new double[close.length];
		double bbM[] = new double[close.length];
		double bbL[] = new double[close.length];
		ta_lib.bbandsLookback(day, 2, 2, MAType.Sma);
		retCode = ta_lib.bbands(0, close.length-1, close, day, 2.0000, 2.0000, MAType.Sma, outBegIdx, outNBElement, bbU, bbM, bbL);		
		setReturnArray(bbU, ret_bollingerU);
		setReturnArray(bbM, ret_bollingerM);
		setReturnArray(bbL, ret_bollingerL);				
	}
	
	public void testbbands(int day, ArrayList<Double> ret_sma, double [] ret_up, double [] ret,  double [] ret_dn) {
		double bbU[] = new double[close.length];
		double bbM[] = new double[close.length];
		double bbL[] = new double[close.length];
			
		retCode = ta_lib.bbands(0, close.length-1, close, day, 2.0000, 2.0000, MAType.Sma, outBegIdx, outNBElement, bbU, bbM, bbL); 
		
		setReturnArray(bbM, ret);
		setReturnArray(bbU, ret_up);
		setReturnArray(bbL, ret_dn);
	}
	
	public void parabolicSar(ArrayList<Double> ret_sar) {
		double sar[] = new double[close.length];
		retCode = ta_lib.sar(0, close.length-1, high, low, 0.2, 0.2, outBegIdx, outNBElement, sar);
		setReturnArray(sar, ret_sar);
	}
	
	public void testsar(double [] ret_sar) {
		double sar[] = new double[close.length];
		retCode = ta_lib.sar(0, close.length-1, high, low, 0.02, 0.2, outBegIdx, outNBElement, sar);
		setReturnArray(sar, ret_sar); 
	}
	
	
	public void slowStc(ArrayList<Double> ret_stcK, ArrayList<Double> ret_stcD) {
		double stcK[] = new double[close.length];
		double stcD[] = new double[close.length];
		retCode = ta_lib.stoch(0, close.length-1, high, low, close, 15, 5, MAType.Sma, 3, MAType.Sma, outBegIdx, outNBElement, stcK, stcD);
		setReturnArray(stcK, ret_stcK);
		setReturnArray(stcD, ret_stcD);
	}
	
	public void slowStc(int fastK, int slowK, int slowD, ArrayList<Double> ret_stcK, ArrayList<Double> ret_stcD) {
		double stcK[] = new double[close.length];
		double stcD[] = new double[close.length];
		retCode = ta_lib.stoch(0, close.length-1, high, low, close, fastK, slowK, MAType.Sma, slowD, MAType.Sma, outBegIdx, outNBElement, stcK, stcD);
		setReturnArray(stcK, ret_stcK);
		setReturnArray(stcD, ret_stcD);
	}
	
	public void fastStc(ArrayList<Double> ret_stcK, ArrayList<Double> ret_stcD) {
		double stcK[] = new double[close.length];
		double stcD[] = new double[close.length];
		retCode = ta_lib.stochF(0, close.length-1, high, low, close, 15, 5, MAType.Sma, outBegIdx, outNBElement, stcK, stcD);
		setReturnArray(stcK, ret_stcK);
		setReturnArray(stcD, ret_stcD);
	}
	
	public void adx(ArrayList<Double> ret_adx, ArrayList<Double> ret_ma) {
		double adx[] = new double[close.length];
		double in_adx[] = new double[close.length];
		double ma[] = new double[close.length];
		retCode = ta_lib.adx(0, close.length-1, high, low, close, 14, outBegIdx, outNBElement, adx);
		setReturnArray(adx, ret_adx);
		setReturnArray(adx, in_adx);
		retCode = ta_lib.sma(0, close.length-1, in_adx, 9, outBegIdx, outNBElement, ma);		
		setReturnArray(ma, ret_ma);
	}
	
	public void willams(ArrayList<Double> ret_willams) {
		double willams[] = new double[close.length];
		retCode = ta_lib.willR(0, close.length-1, high, low, close, 14, outBegIdx, outNBElement, willams);
		
		setReturnArray(willams, ret_willams);
	}
	
	public void cci(ArrayList<Double> ret_cci) {
		double cci[] = new double[close.length];
		retCode = ta_lib.cci(0, close.length-1, high, low, close, 14, outBegIdx, outNBElement, cci);
		setReturnArray(cci, ret_cci);	
	}
	
	public void trix(ArrayList<Double> ret_trix) {
		double trix[] = new double[close.length];
		retCode = ta_lib.trix(0, close.length-1, close, 30, outBegIdx, outNBElement, trix);		
		setReturnArray(trix, ret_trix);	
	}
	
	public void roc(ArrayList<Double> ret_roc) {
		double roc[] = new double[close.length];
		retCode = ta_lib.roc(0, close.length-1, close, 30, outBegIdx, outNBElement, roc);		
		setReturnArray(roc, ret_roc);	
	}
	
	public void dma(ArrayList<Double> ret_pdmi, ArrayList<Double> ret_mdmi) {
		double pdmi[] = new double[close.length];
		double mdmi[] = new double[close.length];
		
		retCode = ta_lib.plusDI(0, close.length-1, high, low, close, 14, outBegIdx, outNBElement, pdmi);
		retCode = ta_lib.minusDI(0, close.length-1, high, low, close, 14, outBegIdx, outNBElement, mdmi);
		setReturnArray(pdmi, ret_pdmi);
		setReturnArray(mdmi, ret_mdmi);
	}
	
	
	public void rsi(ArrayList<Double> ret_rsi) {
		double rsi[] = new double[close.length];
		ta_lib.rsi(0, close.length-1, close, 10, outBegIdx, outNBElement, rsi);
				
		setReturnArray(rsi, ret_rsi);		
	}
	
	public void obv(ArrayList<Double> ret_obv) {
		double obv[] = new double[close.length];
		ta_lib.obv(0, close.length-1, close, volume, outBegIdx, outNBElement, obv);				
		setReturnArray(obv, ret_obv);		
	}
	
	public void setTaPattern() {
		char bullish = 'P';
		char found = 'F';
		int index = 0;
		ta_lib.cdl2Crows(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);		
		addResult(bullish, index++);
		ta_lib.cdl3BlackCrows(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);		
		addResult(bullish, index++);
		ta_lib.cdl3Inside(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);		
		addResult(bullish, index++);
		ta_lib.cdl3LineStrike(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);		
		addResult(bullish, index++);
		ta_lib.cdl3Outside(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);		
		addResult(bullish, index++);
		ta_lib.cdl3StarsInSouth(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);		
		addResult(bullish, index++);
		ta_lib.cdl3WhiteSoldiers(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);		
		addResult(bullish, index++);
		ta_lib.cdlAbandonedBaby(0, size-1, open, high, low, close, optInPenetration, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlAdvanceBlock(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlBeltHold(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlBreakaway(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlClosingMarubozu(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlConcealBabysWall(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlCounterAttack(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlDarkCloudCover(0, size-1, open, high, low, close, optInPenetration, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlDoji(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(found, index++);
		ta_lib.cdlDojiStar(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlDragonflyDoji(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(found, index++);
		ta_lib.cdlEngulfing(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlEveningDojiStar(0, size-1, open, high, low, close, optInPenetration, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlEveningStar(0, size-1, open, high, low, close, optInPenetration, outBegIdx, outNBElement, outInteger);		
		addResult(bullish, index++);
		ta_lib.cdlGapSideSideWhite(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlGravestoneDoji(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(found, index++);
		ta_lib.cdlHammer(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(found, index++);
		ta_lib.cdlHangingMan(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(found, index++);
		ta_lib.cdlHarami(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlHaramiCross(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlHignWave(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlHomingPigeon(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlIdentical3Crows(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlInNeck(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlInvertedHammer(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlKicking(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlLadderBottom(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlLongLeggedDoji(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(found, index++);
		ta_lib.cdlMarubozu(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlMatchingLow(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlMatHold(0, size-1, open, high, low, close, optInPenetration, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlMorningDojiStar(0, size-1, open, high, low, close, optInPenetration, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlMorningStar(0, size-1, open, high, low, close, optInPenetration, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlOnNeck(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlPiercing(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlRickshawMan(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(found, index++);
		ta_lib.cdlRiseFall3Methods(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlSeperatingLines(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlShootingStar(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlSpinningTop(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlStalledPattern(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlStickSandwhich(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlTakuri(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(found, index++);
		ta_lib.cdlTasukiGap(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlThrusting(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlTristar(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlUnique3River(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlUpsideGap2Crows(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
		ta_lib.cdlXSideGap3Methods(0, size-1, open, high, low, close, outBegIdx, outNBElement, outInteger);
		addResult(bullish, index++);
				
		setResult();
	}
	
	private void setReturnArray(double [] res, ArrayList<Double> ret) {
		for ( int i = 0; i < outBegIdx.value; i++ ) {
			ret.add(0.0);
		}			
		
		for ( int i = 0; i < outNBElement.value; i++ ) {			
			ret.add(Double.parseDouble(String.format("%.2f", res[i])) );
		}		
	}
	
	private void setReturnArray(double [] res, double [] ret) {
		for ( int i = 0; i < outBegIdx.value; i++ ) {
			ret[i]  = 0.0;
		}			
		
		for ( int i = 0; i < outNBElement.value; i++ ) {
			ret[i+outBegIdx.value] = Double.parseDouble(String.format("%.2f", res[i]));			
		}		
	}
	
	private void addResult(char result, int index) {
		for ( int i = 0; i < outBegIdx.value; i++ ) 
			ta[i][index] = 'Z';
		
		for ( int i = 0; i < outNBElement.value; i++ ) {
			if (outInteger[i] == -100) 
				ta[outBegIdx.value + i][index] = 'M';
			else if (outInteger[i] == 100) 
				ta[outBegIdx.value + i][index] = result;
			else
				ta[outBegIdx.value + i][index] = 'Z';
		}
	}
	
	private void setResult() {
		if ( index > DEFAULT_DAY ) {
			int j = index;
			for ( int i = DEFAULT_DAY; i < size; i++ ) { 
				quotes.get(j++).setPattern(String.valueOf(ta[i]));
			}
		}
		else {
			for ( int i = index; i < size; i++ ) { 
				quotes.get(i).setPattern(String.valueOf(ta[i]));
			}
		}
			
	}
}

