package com.vvip.init;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.vvip.quote.Quote;
import com.vvip.quote.TradeDate;

public class SocketType implements Runnable {
	private final int MAXBUFSIZE = 50240;
	// private final int MAXBUFSIZE = 9025;

	final static String COMMUNICATION_SPLITE = ";";//$NON-NLS-1$
	private Socket mainClient = null;

	private final static String COMMUNICATION_TYPE_1 = "BANK";//$NON-NLS-1$
	private final static String COMMUNICATION_TYPE_2 = "PRICE";//$NON-NLS-1$
	private final static String COMMUNICATION_TYPE_3 = "TRADE";//$NON-NLS-1$
	private final static String COMMUNICATION_TYPE_4 = "ACCOUNT";//$NON-NLS-1$
	private final static String COMMUNICATION_TYPE_5 = "QCLICK";//$NON-NLS-1$
	private final static String COMMUNICATION_TYPE_6 = "PER";//$NON-NLS-1$
	private final static String COMMUNICATION_TYPE_7 = "MINUTE";//$NON-NLS-1$
	private final static String COMMUNICATION_TYPE_8 = "MODIFY";//$NON-NLS-1$
	private final static String COMMUNICATION_TYPE_9 = "MULTY_QUOTA";//$NON-NLS-1$
	private final static String COMMUNICATION_TYPE_TRAFFIC = "TRAFFIC";//$NON-NLS-1$

	private BufferedWriter writer = null;
	private Thread instance;

	private SocketCommunicator tradeManager = null;

	Quote quote = null;
	String account = null;
	String bank = null;
	String bankStockInfo = null;
	String tradeNumber = "";
	String tradeModify = null;
	String qclick = null;
	String per = null;
	String requestResult = null;
	String minutePriceInfo = null;
	String trafficInfo = null;
	String multiQuotaList = null;

	Boolean notifyRecv = true;

	Boolean notifyAccept = false;

	public void run() {

		if (null != ServerManager.serveSocket) {
			int readSize = 0;

			InputStream in = null;
			while (null != mainClient) {
				try {
					in = mainClient.getInputStream();
					byte receiveBuffer[] = new byte[MAXBUFSIZE];
					readSize = in.read(receiveBuffer);
					receiveBuffer[readSize + 1] = '\0';
					if (readSize > 0) {
						parsingReceiveMessage(new String(receiveBuffer));
					} else {
						break;
					}
				} catch (IOException e) {
					ServerManager.disConnectClient(this.tradeManager);
					try {
						if (null != in) {
							in.close();
						}
						if (null != mainClient) {
							mainClient.close();
							mainClient = null;
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						break;
					}
					e.printStackTrace();
					break;
				}
			}

		}
		System.out.println("Disconnect client	");//$NON-NLS-1$
	}

	public void startIDECommunicatorThread(SocketCommunicator tradeManagerObj, Socket socket, SocketType obj) {
		if (null == instance || !instance.isAlive()) {
			this.tradeManager = tradeManagerObj;
			this.mainClient = socket;

			instance = new Thread(null, obj, "communicator");
			instance.start();
		}
	}

	public void stopIDEcommunicatorThread() {
		if (null != instance && instance.isAlive()) {
			instance.interrupt();
		}
	}

	public boolean isConnected() {
		if (mainClient == null) {
			return false;
		}
		return true;
	}

	public String requestBankAccount() {
		sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_4, "", "", "", ""));
		try {
			synchronized (notifyRecv) {
				notifyRecv.wait(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return account;
	}

	public String requestQClick(String search) {
		sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_5, search, "", "", ""));
		try {
			synchronized (notifyRecv) {
				notifyRecv.wait(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return qclick;
	}

	public String requestPER(String symbol) {
		sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_6, symbol, "", "", ""));
		try {
			synchronized (notifyRecv) {
				notifyRecv.wait(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return per;
	}

	public String requestBankInfo(String next) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_1, next, "", "", ""));
		try {
			synchronized (notifyRecv) {
				notifyRecv.wait(2000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return bankStockInfo;
	}

	public String requestMultyQuotaInfo(String count, String symbolList) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_9, count, symbolList, "", ""));
		try {
			synchronized (notifyRecv) {
				notifyRecv.wait(2000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return multiQuotaList;
	}

	public Quote requestPriceInfo(String symbol) {

		sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_2, symbol, "", "", ""));

		try {
			synchronized (notifyRecv) {
				// System.out.println("notifyRecv.wait() Start");
				notifyRecv.wait(1000);
				// System.out.println("notifyRecv.wait() END");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Quote returnQuote = quote;
		quote = null;
		return returnQuote;
	}

	public String requestTradeInfo(String symbol, String count, boolean isBuy, String price) { // price
																								// ==
																								// 0
																								// Ω√¿Â∞°
		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (isBuy) {
			sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_3, symbol, "BUY", count, price));
		} else {
			sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_3, symbol, "SELL", count, price));
		}
		try {
			synchronized (notifyRecv) {
				notifyRecv.wait(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return tradeNumber;
	}

	public String requestModifyTrade(String symbol, String count, String price, String orderNumber) {
		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_8, symbol, count, price, orderNumber));

		try {
			synchronized (notifyRecv) {
				notifyRecv.wait(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return tradeModify;
	}

	private String sendMessageString(String first, String second, String third, String four, String five) {
		StringBuffer sendMsg = new StringBuffer();
		sendMsg.append(first).append(COMMUNICATION_SPLITE).append(second).append(COMMUNICATION_SPLITE).append(third).append(COMMUNICATION_SPLITE).append(four).append(COMMUNICATION_SPLITE)
				.append(five);
		return sendMsg.toString();

	}

	public boolean sendMessageToIDE(final String message) {
		// TradeManager.writeToFile(TradeManager.getBuyFilePath(), message);
		// System.out.println("sendMessageToIDE : "+message);
		try {
			if (mainClient != null) {
				writer = new BufferedWriter(new OutputStreamWriter(mainClient.getOutputStream()));
				writer.write(message);
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Management Receive Message
	private void parsingReceiveMessage(String msg) {
//		System.out.println("parsingReceiveMessage : " + msg);
		if (msg.startsWith("MESSAGE_DATA")) {
			String[] result = msg.split("#");
			requestResult = "";
			requestResult = result[1];
			// System.out.println("requestResult : "+requestResult);
			synchronized (notifyRecv) {
				notifyRecv.notify();
			}
		} else {
			String[] strMsg = msg.split(COMMUNICATION_SPLITE);
			if (strMsg[0].equals(COMMUNICATION_TYPE_1)) {
				bank = "";
				bank = strMsg[1];
				bankStockInfo = msg;
				// System.out.println("bank : "+bank+" bankStockInfo :
				// "+bankStockInfo);
			} else if (strMsg[0].equals(COMMUNICATION_TYPE_2)) {
				Calendar cal = new GregorianCalendar();
				int mYear = cal.get(Calendar.YEAR);
				int mMonth = cal.get(Calendar.MONTH) + 1;
				int mDay = cal.get(Calendar.DAY_OF_MONTH);
				quote = new Quote(new TradeDate(mYear, mMonth, mDay), Double.parseDouble(strMsg[1]), Double.parseDouble(strMsg[2]), Double.parseDouble(strMsg[3]), Double.parseDouble(strMsg[4]),
						Long.parseLong(strMsg[5]));

			} else if (strMsg[0].equals(COMMUNICATION_TYPE_3)) {
//				System.out.println("parsingReceiveMessage : " + msg);
				if (msg.contains("TRADE")) {
					String[] strMsgTrade = msg.split("TRADE");
					String[] strMsgTrade1 = strMsgTrade[1].split(COMMUNICATION_SPLITE);
					tradeNumber = "";
					tradeNumber = strMsgTrade1[1];
				}
//				System.out.println("TRADE REQ:" + msg);
			} else if (strMsg[0].equals(COMMUNICATION_TYPE_4)) {
				account = "";
				account = strMsg[1];
			} else if (strMsg[0].equals(COMMUNICATION_TYPE_5)) {
				qclick = "";
				qclick = strMsg[1];
			} else if (strMsg[0].equals(COMMUNICATION_TYPE_6)) {
				per = "";
				per = strMsg[1];
			} else if (strMsg[0].equals(COMMUNICATION_TYPE_7)) {
				minutePriceInfo = "";
				minutePriceInfo = msg;
			} else if (strMsg[0].equals(COMMUNICATION_TYPE_TRAFFIC)) {
				trafficInfo = "";
				trafficInfo = msg;
			} else if (strMsg[0].equals(COMMUNICATION_TYPE_8)) {
				tradeModify = "";
			} else if (strMsg[0].equals(COMMUNICATION_TYPE_9)) {
				multiQuotaList = msg;
			}

			synchronized (notifyRecv) {
				notifyRecv.notify();
			}
		}

	}

	public String getResult() {
		return requestResult;
	}

	public String requestMinutePriceInfo(String symbol, String minuteUnite, String dataCount) {
		sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_7, symbol, minuteUnite, dataCount, ""));

		try {
			synchronized (notifyRecv) {
				notifyRecv.wait(10000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String returnString = minutePriceInfo;
		minutePriceInfo = "";
		return returnString;
	}

	public String requestTopTrafficSymbolInfo(String type) {
		sendMessageToIDE(sendMessageString(COMMUNICATION_TYPE_TRAFFIC, type, "", "", ""));
		try {
			synchronized (notifyRecv) {
				notifyRecv.wait(5000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return trafficInfo;
	}

}