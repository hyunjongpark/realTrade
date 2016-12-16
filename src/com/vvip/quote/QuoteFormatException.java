package com.vvip.quote;


public class QuoteFormatException extends Throwable {
	/**
	 * ������ �����ϱ����� ������ ������� ���� format������ ���������� �����ִ�.
	 * 
	 * @author ���Ը�  �ؼ�smlee
	 */
	private static final long serialVersionUID = -3342758622588772742L;

	/**
	 * Quote���Ŀ� ���� �����޽����� ����ϴµ� ������ü�� �Ѱ��ش�.
	 * 
	 * @param message
	 * 
	 * @author ���Ը�  �ؼ�smlee
	 */
	public QuoteFormatException(String message) {
		super(message);	        
	}
}
