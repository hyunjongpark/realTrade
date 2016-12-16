package com.vvip.quote;

/**
 * TradeDate에서 데이터로 날짜를 생성할때 발생한 Exception을
 * 처리해주기위하여 만들어논 Exception이다. 메시지를 Throws해준다.
 * 
 * @author 구규림   해석smlee
 *
 */
public class TradeDateFormatException extends Throwable {
	public TradeDateFormatException(String message) {
	        super(message);	        
	    }
}
