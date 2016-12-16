package com.vvip.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class AdminServer
 */
public class AdminServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
      

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		throw new ServletException("AdminServer - doGet");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String password = request.getParameter("password");
//		if ( password == null ) {
//			throw new ServletException("worng password");
//		}
//		else if ( password.equals("jni") == true ) {
//			JniStock.main(null);
//		}
//		else if ( password.equals("st") == true ) {
//			VVIPManager.startVVIPManagerThread();
//		}	
//		else if ( password.equals("GaUpdateDBfromFile") == true ) {
//			System.out.println("GaUpdateDBfromFile");
//				FileReader dataFile = null;
//				BufferedReader bufferedReader = null;
//				try {
//					dataFile = new FileReader(VVIPManager.getVVIP_PATH()+"/data/ga.csv");
//					bufferedReader = new BufferedReader(dataFile);
//					String line = bufferedReader.readLine();
//					while (line != null) {
//						line = bufferedReader.readLine();
//						if (line.length() > 7) {
//							String[] data = null;
//							data = line.split("##");
//							
//									System.out.print(+Integer.valueOf(data[0])+" "+String.valueOf(data[1])+
//											" "+Double.valueOf(data[2])+" "+Integer.valueOf(data[3])+" "+Integer.valueOf(data[4])+
//											" "+String.valueOf(data[5])+" "+String.valueOf(data[6])+"\n");
//									DatabaseManager.insertGeneFromFile(line);
//						
//						} else
//							break;
//					}
//					bufferedReader.close();
//					dataFile.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//		}
//		else if ( password.equals("uq") == true ) {
//			System.out.println("UpdateKoreaQuote main");
//			UpdateKoreaQuote.main(null);
//		}
//		else if ( password.equals("phj1629") == true ) {
//			System.out.println("phj1629");
//			int i;
//			File kor_dir = new File(VVIPManager.getVVIP_PATH()+"/log/KOREA");
//			File[] kor_files = kor_dir.listFiles();
//			ArrayList<String> kor_list = new ArrayList<String> ();
//			for ( i = 0; i < kor_files.length; i++ ) 
//				if ( kor_files[i].isFile()  )
//					kor_list.add(kor_files[i].getName());
//			
//			File usa_dir = new File(VVIPManager.getVVIP_PATH()+"/log/USA");
//			File[] usa_files = usa_dir.listFiles();
//			ArrayList<String> usa_list = new ArrayList<String> ();
//			for ( i = 0; i < usa_files.length; i++ ) 
//				if ( usa_files[i].isFile()  )
//					usa_list.add(usa_files[i].getName());
//			
//			File quote_dir = new File(VVIPManager.getVVIP_PATH()+"/log/quote");
//			File[] quote_files = quote_dir.listFiles();
//			ArrayList<String> quote_list = new ArrayList<String> ();
//			for ( i = 0; i < quote_files.length; i++ ) 
//				if ( quote_files[i].isFile()  )
//					quote_list.add(quote_files[i].getName());
//			
//			Collections.sort(kor_list);
//			Collections.sort(usa_list);
//			Collections.sort(quote_list);
//			
//			HttpSession session = request.getSession();
//	    	session.setAttribute("Confirm", "loginadminok");
//	    	session.setAttribute("KORLIST", kor_list);
//	    	session.setAttribute("USALIST", usa_list);
//	    	session.setAttribute("QUOTELIST", quote_list);
//	    	
//	    	
//	    	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/Admin.jsp");
//        	rd.forward(request, response);   	
//        	
//		}		
//		else {
//			throw new ServletException("AdminServer - doPost");
//		}	
	}
}
