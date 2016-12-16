package com.vvip.data;


public class ImportException extends Throwable {
	   /**
	 * 
	 */
	private static final long serialVersionUID = 789302901020823120L;
	public ImportException(Exception e) {
		super(e);
	}
	
	public ImportException(String message) {
	        super(message);	        
	    }
}
