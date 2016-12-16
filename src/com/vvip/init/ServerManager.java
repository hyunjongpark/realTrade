//package com.vvip.init;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.GregorianCalendar;
package com.vvip.init;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerManager implements Runnable {
	static List<SocketCommunicator> tradeList = new ArrayList<SocketCommunicator>();
	static ServerSocket serveSocket = null;
	private static Thread instance;

	@Override
	public void run() {
		try {
			serveSocket = new ServerSocket(9637);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				System.out.println("Waiting Client : " + tradeList.size());
				Socket socket = serveSocket.accept();
				SocketCommunicator trade = new SocketCommunicator();
				trade.startVVIPManagerThread(socket, trade, tradeList.size());
				tradeList.add(trade);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public synchronized static void disConnectClient(SocketCommunicator obj) {
		tradeList.remove(obj);
	}

	public static void startVVIPManagerThread() {
		if (null == instance || !instance.isAlive()) {
			instance = new Thread(null, new ServerManager(), "VVIPManager");
			instance.start();
			
		}
	}

	public static void stopVVIPManagerThread() {
		if (null != instance && instance.isAlive()) {
			instance.interrupt();
		}
	}

}
