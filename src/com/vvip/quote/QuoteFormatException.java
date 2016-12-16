package com.vvip.quote;


public class QuoteFormatException extends Throwable {
	/**
	 * 에러를 방지하기위해 생성된 상수변수 현재 format에대한 버전정보를 갖고있다.
	 * 
	 * @author 구규림  해석smlee
	 */
	private static final long serialVersionUID = -3342758622588772742L;

	/**
	 * Quote형식에 대한 에러메시지를 출력하는데 상위객체에 넘겨준다.
	 * 
	 * @param message
	 * 
	 * @author 구규림  해석smlee
	 */
	public QuoteFormatException(String message) {
		super(message);	        
	}
}
