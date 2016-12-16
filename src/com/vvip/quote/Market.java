package com.vvip.quote;

/** 
 * ���������� ������ ���� ������.
 * ������ �������� ���� ������ ������.
 * 
 * @author Brian 
 *
 */
public class Market {
	private int marketIndex;
	private String name;
	private String nation;	
	private int moneyPosition;
	private int hash;

	/**
	 * ������ MoneyPosition�� ���������� ������ ���������� 0���� ��������.
	 * @param name ��������
	 * @param nation ���� ��������
	 * 
	 * @author Brian 
	 */
	public Market(int marketIndex, String name, String nation, int pointPosition, int hash){
		this.marketIndex = marketIndex;
		this.name = name;
		this.nation = nation;
		this.moneyPosition = pointPosition;
		this.hash = hash;
	}	
	
	/**
	 * ���� ������
	 * @param name ��������
	 * @param nation ���� ��������
	 * @param pointPosition ���� ����.(������)
	 * 
	 * @author Brian 
	 */
	public Market(String name, String nation, int pointPosition, int hash) {
		this.name = name;
		this.nation = nation;	
		this.moneyPosition = pointPosition;
		this.hash = hash;
	}

	/**
	 *  @return String name. ���������� ����.
	 *  
	 *  @author Brian 
	 */			
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return String nation. �������� ����.
	 * 
	 * @author Brian 
	 */
	public String getNation() {
		return nation;
	}
	
	public int getMarketIndex() {
		return marketIndex;
	}

	/**
	 * 
	 * @return Int MoneyPosition. ���� ������ ����.
	 * 
	 * @author Brian 
	 */			
	public int getMoneyPosition() {
		return moneyPosition;
	}	
	
	/**
	 * �������������� ������ ������������ ������ ����������.
	 * 
	 * @return int hash ���������� ����
	 * 
	 * @author Brian    ����smlee
	 */
	public int getHash() {
		return hash;
	}
	
}


