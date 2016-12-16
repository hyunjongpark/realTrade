package com.vvip.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vvip.init.VVIPManager;

/**
 * Servlet implementation class ViewLogFile
 */
public class ViewLogFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewLogFile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		throw new ServletException("������ ���� ������.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String kor_file = request.getParameter("KOREA");
		String usa_file = request.getParameter("USA");		
		String quote_file = request.getParameter("quote");
		String path = null;
		if ( kor_file != null ) {
			path = VVIPManager.getVVIP_PATH()+"/log/KOREA/" + kor_file;
		} else if ( usa_file != null ) {
			path = VVIPManager.getVVIP_PATH()+"/log/USA/" + usa_file;
		} else if ( quote_file != null ) {
			path = VVIPManager.getVVIP_PATH()+"/log/quote/" + quote_file;
		} else {
			throw new ServletException("������ ���� ������.");
		}
		
		//������ ���� JSP������ ������ ����
		HttpSession session = request.getSession();
    	session.setAttribute("Path", path);
    	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/ViewFile.jsp");
    	rd.forward(request, response);   	
		
	}
}
