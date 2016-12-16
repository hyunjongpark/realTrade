package com.vvip.jni;

public class LoadJni {

    public native void print();
    
    public static void main(String[] args)
    {
    	try{
    		new LoadJni().print();
    		
    	} catch ( Exception e){
    		e.printStackTrace();
    		
    	}
    }

    static {
        System.loadLibrary("LoadJniStock");
    }
}
