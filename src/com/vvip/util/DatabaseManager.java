package com.vvip.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vvip.ga.Gene;
import com.vvip.ga.GeneResult;
import com.vvip.init.CommonUtil;
import com.vvip.init.VVIPManager;
import com.vvip.quote.Company;
import com.vvip.quote.Quote;
import com.vvip.quote.QuoteList;
import com.vvip.quote.TradeDate;
import com.vvip.trade.TradeType;

public class DatabaseManager {

	private static Connection connection;

	// private final static int PERMIT = 1;
	// private static Semaphore semaphore = new Semaphore(PERMIT);
	// private static String url;
	// private static String user;
	// private static String pwd;
	// private static String schema;

	public static void semaphoreAcquire() {
	}

	public static void semaphoreRelease() {
	}

	public static synchronized Connection getConnection() {
		try {
			if (null != connection) {
				return connection;
			}
			Class.forName("org.hsqldb.jdbcDriver");
		//	String dbUrl = "jdbc:hsqldb:" + VVIPManager.getVVIP_PATH() + File.separator + "db" + File.separator + "vvip" + ";shutdown=true;hsqldb.default_table_type=cached;hsqldb.large_data=true;hdqldb.log_data=false"; //$NON-NLS-1$
			String dbUrl = "jdbc:hsqldb:" + VVIPManager.getVVIP_PATH() + "/db" +  "/vvip" + ";shutdown=true;hsqldb.default_table_type=cached;hsqldb.large_data=true;hdqldb.log_data=false"; //$NON-NLS-1$
			connection = DriverManager.getConnection(dbUrl, "SA", "");
			SQLWarning warning = connection.getWarnings();
			while (warning != null) {
				System.out.println("Message: " + warning.getMessage());
				System.out.println("SQL state: " + warning.getSQLState());
				System.out.println("Vendor code: " + warning.getErrorCode());
				warning = warning.getNextWarning();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	// public static synchronized void close(Connection dbConn) {
	// // if (connection != null) {
	// // try {
	// // connection.close();
	// // } catch (Exception e) {
	// // System.out.println(e.toString());
	// // }
	// // }
	// // connection = null;
	// }

	public static synchronized void close1(Connection dbConn) {
		if (connection == null) {
			return;
		} else {
			try {
				Statement st = connection.createStatement();
				st.execute("SHUTDOWN");
				if (null != st) {
					st.close();
				}
				connection.close();
			} catch (SQLException se) {
				if (!se.getSQLState().equals("XJ015")) {
					System.out.println("DB did not shutdown normally");
				}
			}
			connection = null;
		}
	}
	
	public static synchronized void close(Connection dbConn) {
//		if (connection == null) {
//			return;
//		} else {
//			try {
//				Statement st = connection.createStatement();
//				st.execute("SHUTDOWN");
//				if (null != st) {
//					st.close();
//				}
//				connection.close();
//			} catch (SQLException se) {
//				if (!se.getSQLState().equals("XJ015")) {
//					System.out.println("DB did not shutdown normally");
//				}
//			}
//			connection = null;
//		}
	}

	// /////////////////////////////////
	// company
	// /////////////////////////////////

	public static synchronized void createCompanyTable() {
		try {
			Connection dbConn = getConnection();
			if (null != dbConn) {
				semaphoreAcquire();
			}
			Statement stmt = dbConn.createStatement();


			String query = "CREATE TABLE company(" + "Symbol VARCHAR(512) NOT NULL PRIMARY KEY,  "
					+ "EngName VARCHAR(512) NOT NULL ,  " + "Total double, " + "PER double, " + "faceValue double, "
					+ "preSales double, " + "preIncome double, " + "preTaxIncome double, " + "preNetProfit double, "
					+ "preEPS double, " + "prePreSales double, " + "prePreIncome double, " + "prePreTaxIncome double, "
					+ "prePreNetProfit double, " + "prePreEPS double, " + "Type INTEGER NOT NULL " + ")";

			stmt.executeUpdate(query);
			System.out.println("Success : companyTable");
			if (null != dbConn) {
				semaphoreRelease();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void insertCompanyTable(ArrayList<Company> list, int type) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			for (int i = 0; i < list.size(); i++) {
				Company cp = list.get(i);
				// String query = "INSERT INTO company " +
				// "(Symbol, engName, type) " + "VALUES ('" + cp.getSymbol() +
				// "', '" + cp.getNameInEng() + "', " + type + ")";
				String query = "INSERT INTO company "
						+ "(Symbol, engName, total, per, facevalue, presales, preincome, pretaxincome, prenetprofit, preeps, prepresales, prepreincome, prepretaxincome, preprenetprofit, prepreeps,type) "
						+ "VALUES ('"
						+ cp.getSymbol()
						+ "', '"
						+ cp.getNameInEng()
						+ "', '"
						+ cp.getTotal()
						+ "', '"
						+ 0
						+ "', '"
						+ 0

						+ "', '"
						+ 0
						+ "', '"
						+ 0
						+ "', '"
						+ 0
						+ "', '"
						+ 0
						+ "', '"
						+ 0
						+ "', '"
						+ 0
						+ "', '"
						+ 0
						+ "', '" + 0 + "', '" + 0 + "', '" + 0

						+ "', " + type

						+ ")";
				System.out.println("query : " + query);
				stmt.executeUpdate(query);
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized void updateCompany(String[] info) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "Update company set" + " total = " + info[1].trim() + ", per = " + info[2].trim()
					+ ", facevalue = " + info[3].trim() + ", presales = " + info[4].trim() + ", preincome = "
					+ info[5].trim() + ", pretaxincome = " + info[6].trim() + ", prenetprofit = " + info[7].trim()
					+ ", preeps = " + info[8].trim()

					+ ", prepresales = " + info[9].trim() + ", prepreincome = " + info[10].trim()
					+ ", prepretaxincome = " + info[11].trim() + ", preprenetprofit = " + info[12].trim()
					+ ", prepreeps = " + info[13].trim() + " where symbol = '" + info[0].trim() + "'";

			System.out.println("query : " + query);
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized void removeCompany(String symbol) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "delete from company where symbol ='"+symbol+"'";

			System.out.println("query : " + query);
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized void dropSymbolTable(String symbol) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String tableName = "symbol_"+symbol;
			String query = "drop table "+tableName;
			System.out.println("query : " + query);
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<Company> selectCompanyTable() {
		ArrayList<Company> companyList = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			// String query = "select * from company where " + " per > 0 and" +
			// " presales >0 and" + " preincome >0 and" + " pretaxincome >0 and"
			// + " prenetprofit >0 and" + " preeps >0 and" +
			// " prepresales >0 and" + " prepreincome >0 and" +
			// " prepretaxincome >0 and" + " preprenetprofit >0 and" +
			// " prepreeps >0 " + " order by total desc";
			String query = "select * from company order by total desc";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				companyList = new ArrayList<Company>();
				do {
					companyList.add(new Company(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs
							.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs
							.getDouble(10), rs.getDouble(11), rs.getDouble(12), rs.getDouble(13), rs.getDouble(14), rs
							.getDouble(15), rs.getInt(16)));
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return companyList;
	}

	public static boolean isKospySymbol(String symbol) {
		int type = 0;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select type from company where symbol ='"+symbol+"'";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				type = rs.getInt(1);
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type == 1 ? true : false;
	}

	// /////////////////////////////////
	// All_pattern
	// /////////////////////////////////
	public static synchronized void createAllPattern() {
		try {
			Connection dbConn = getConnection();
			if (null != dbConn) {
				semaphoreAcquire();
			}
			Statement stmt = dbConn.createStatement();
			String query = "CREATE TABLE allPATTERN ( symbol VARCHAR(512) NOT NULL, pattern VARCHAR(512) NOT NULL, plus double not null, minus double not null)";
			stmt.executeUpdate(query);
			System.out.println("Success : allPATTERN");
			if (null != dbConn) {
				semaphoreRelease();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<String> isAllPattern(String option) {
		List<String> returnList = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select * from allpattern where " + option;
			// System.out.println("query : "+query);
			rs = stmt.executeQuery(query);
			if (null != rs) {
				ResultSetMetaData rsMetaData = rs.getMetaData();
				int nRsMetaDataCount = rsMetaData.getColumnCount();
				while (rs.next()) {
					returnList = new ArrayList<String>();
					for (int i = 1; i < nRsMetaDataCount + 1; i++) {
						if (rsMetaData.getColumnTypeName(i).contains("INTEGER")) {
							returnList.add(Integer.toString(rs.getInt(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("BIGINT")) {
							returnList.add(Long.toString(rs.getLong(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("VARCHAR")) {//$NON-NLS-1$
							returnList.add(rs.getString(rsMetaData.getColumnName(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("DOUBLE")) {//$NON-NLS-1$
							returnList.add(Double.toString(rs.getDouble(rsMetaData.getColumnName(i))));
						} else {
							System.out.println("Error undefine Type : " + rsMetaData.getColumnTypeName(i));
						}
					}
				}
			}
			// System.out.println("selectQuoteListByDate : "+query);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != dbConn) {
			semaphoreRelease();
		}
//		close(dbConn);
		return returnList;
	}

	public static synchronized void insertAllPattern(String pattern, String info) {

		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;

		try {
			stmt = dbConn.createStatement();
			String data[] = info.split("#");
			String query = "INSERT INTO allpattern (symbol, pattern, plus, minus) " + "VALUES ('" + data[0].trim()
					+ "','" + data[1].trim() + "', " + data[2].trim() + ", " + data[3].trim() + ")";
			System.out.println("query : " + query);

			stmt.executeUpdate(query);

			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void updateAllPattern(String info, String option) {

		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;

		try {
			stmt = dbConn.createStatement();
			String data[] = info.split("#");
			String query = "Update pattern set" + " plus = " + data[0].trim() + ", minus = " + data[1].trim()
					+ " where " + option;
			System.out.println("query : " + query);
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<List<String>> selectAllPattern() {
		List<List<String>> returnList = new ArrayList<List<String>>();
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "SELECT * FROM allpattern";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				do {
					List<String> lowData = new ArrayList<String>();
					lowData.add(rs.getString(1));
					lowData.add(Double.toString(rs.getDouble(2)));
					lowData.add(Double.toString(rs.getDouble(3)));
					returnList.add(lowData);
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnList;
	}

	// /////////////////////////////////
	// pattern_symbol
	// /////////////////////////////////
	public static synchronized void createPatternSymbol(String symbol) {
		try {
			Connection dbConn = getConnection();
			if (null != dbConn) {
				semaphoreAcquire();
			}
			Statement stmt = dbConn.createStatement();
			String query = "CREATE TABLE PATTERN_"
					+ symbol
					+ " (today Integer not null, todayPattern VARCHAR(1024) NOT NULL, yesterdayPattern VARCHAR(1024) NOT NULL, tomorrowPattern VARCHAR(1024) NOT NULL, next1dayProfit double not null, next2dayProfit double not null,next5dayProfit double not null,next10dayProfit double not null)";

			stmt.executeUpdate(query);
			System.out.println("Success : PATTERN_" + symbol);
			if (null != dbConn) {
				semaphoreRelease();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void dropPatternSymbol(String symbol) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "drop table PATTERN_" + symbol;
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized void insertPatternSymbol(String symbol, QuoteList quoteList) {

		int size = quoteList.getSize();
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			for (int i = 1; i < size - 10; i++) {
				Quote todayQt = quoteList.getQuote(i);
				Quote pre1DayQt = quoteList.getQuote(i - 1);
				Quote next1DayQt = quoteList.getQuote(i + 1);
				Quote next2DayQt = quoteList.getQuote(i + 2);
				Quote next5DayQt = quoteList.getQuote(i + 5);
				Quote next10DayQt = quoteList.getQuote(i + 2);

				String query = "INSERT INTO PATTERN_"
						+ symbol
						+ "(today, todaypattern, yesterdayPattern, tomorrowPattern, next1dayProfit, next2dayProfit, next5dayProfit, next10dayProfit) "
						+ "VALUES (" + todayQt.getTradeDate().toInt() + ",'" + todayQt.getPattern().trim() + "', '"
						+ pre1DayQt.getPattern().trim() + "', '" + next1DayQt.getPattern().trim() + "', "
						+ profitToPercentage(todayQt.getClose(), next1DayQt.getClose()) + ", "
						+ profitToPercentage(next1DayQt.getClose(), next2DayQt.getClose()) + ", "
						+ profitToPercentage(next2DayQt.getClose(), next5DayQt.getClose()) + ", "
						+ profitToPercentage(next5DayQt.getClose(), next10DayQt.getClose()) + ")";
				// System.out.println("query : " + query);

				stmt.executeUpdate(query);
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static double profitToPercentage(double startDay, double endDay) {
		double profit = startDay - endDay;
		if (profit == 0) {
			return 0.0;
		}
		return Double.parseDouble(String.format("%.3f", profit / startDay * 100));
	}

	public static List<List<String>> selectPatternSymbol(String symbol) {
		List<List<String>> returnList = new ArrayList<List<String>>();
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "SELECT * FROM GATable WHERE symbol = '" + symbol + "' order by profit desc";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				do {
					List<String> lowData = new ArrayList<String>();
					lowData.add(rs.getString(2));
					lowData.add(rs.getString(3));
					lowData.add(rs.getString(4));
					lowData.add(Double.toString(rs.getDouble(5)));
					lowData.add(Double.toString(rs.getDouble(6)));
					lowData.add(Double.toString(rs.getDouble(7)));
					lowData.add(Double.toString(rs.getDouble(8)));
					returnList.add(lowData);
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnList;
	}

	// /////////////////////////////////
	// GATable
	// /////////////////////////////////

	public static synchronized void createGATable(int type) {
		try {
			Connection dbConn = getConnection();
			if (null != dbConn) {
				semaphoreAcquire();
			}
			String tableName = "";
			if (type == 0) {
				tableName = "macdGene";
			} else {
				tableName = "stochGene";
			}
			Statement stmt = dbConn.createStatement();
			String query = "CREATE TABLE " + tableName + " ( Symbol VARCHAR(512) NOT NULL ,  "
					+ "Gene VARCHAR(512) NOT NULL ,  " + "PROFIT DOUBLE NOT NULL ,  " + "STATUS INTEGER NOT NULL ,  "
					+ "StartTime INTEGER NOT NULL ,  " + "EndTime INTEGER NOT NULL   " + ")";

			stmt.executeUpdate(query);
			System.out.println("Success : createGATable");
			if (null != dbConn) {
				semaphoreRelease();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void insertGene(GeneResult gr) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			double profit = Double.parseDouble(String.format("%.3f", gr.getGene().getProfit()));
			String query = "INSERT INTO macdGene (symbol, Gene, PROFIT, STATUS, StartTime, EndTime) "
					+ "VALUES ('" + gr.getSymbol() + "', '" + String.valueOf(gr.getGene().getBinaryGene()) + "', "
					+ profit + ", " + gr.getStatus() + ", " + gr.getStart().toInt() + ", " + gr.getEnd().toInt() + ");";
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close1(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void deleteGATable(String symbol) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "delete from macdgene where symbol = '" + symbol + "'";
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized void updateGATable(String symbol, int status, int makeMacddate) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "update macdgene set status = '"+status +"' where symbol = '" + symbol + "' and endtime = '"+makeMacddate+"'";
	//		System.out.println(query);
			stmt.execute(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized void deleteGATable(String symbol, int endTime) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "delete from macdgene where symbol = '" + symbol + "' and endTime = '"+endTime+"'";
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized void updateGeneBySymbol(String symbol, GeneResult gr, int type) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String tableName = "";
			if (type == 0) {
				tableName = "macdGene";
			} else {
				tableName = "stochGene";
			}
			String queyr = "Update " + tableName + " set" + " Gene = '" + String.valueOf(gr.getGene().getBinaryGene())
					+ "'" + ", PROFIT = " + gr.getGene().getProfit() + ", STATUS = " + gr.getStatus()
					+ ", StartTime = " + gr.getStart().toInt() + ", EndTime = " + gr.getEnd().toInt()
					+ " where symbol = '" + symbol + "'";
			stmt.executeUpdate(queyr);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<GeneResult> selectGeneResultListByIndex() {
		ArrayList<GeneResult> grList = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select * from macdgene order by symbol desc";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				grList = new ArrayList<GeneResult>();
				do {
					grList.add(new GeneResult(rs.getString(1), new Gene(rs.getString(2), rs.getDouble(3)),
							rs.getInt(4), new TradeDate(rs.getInt(5)), new TradeDate(rs.getInt(6))));
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch bl ock
			e.printStackTrace();
		}
		return grList;
	}
	
	public static ArrayList<GeneResult> selectBuyGeneData() {
		ArrayList<GeneResult> grList = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			int limitEndDate = CommonUtil.getDayIntByMonth(CommonUtil.getLimitMacdEndTime(), 3);
			stmt = dbConn.createStatement();
			String query = "SELECT * FROM macdGene where status = 1 and endtime > "+limitEndDate;
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				grList = new ArrayList<GeneResult>();
				do {
					grList.add(new GeneResult(rs.getString(1), new Gene(rs.getString(2), rs.getDouble(3)),
							rs.getInt(4), new TradeDate(rs.getInt(5)), new TradeDate(rs.getInt(6))));
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grList;
	}
	
	public static ArrayList<GeneResult> selectSellGeneData() {
		ArrayList<GeneResult> grList = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "SELECT * FROM macdGene where status = 2 or status = 4";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				grList = new ArrayList<GeneResult>();
				do {
					grList.add(new GeneResult(rs.getString(1), new Gene(rs.getString(2), rs.getDouble(3)),
							rs.getInt(4), new TradeDate(rs.getInt(5)), new TradeDate(rs.getInt(6))));
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grList;
	}
	

	public static List<String> selectFinishGASymbol(int type) {
		List<String> symbolList = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String tableName = "";
			if (type == 0) {
				tableName = "macdGene";
			} else {
				tableName = "stochGene";
			}
			String query = "select distinct symbol from " + tableName;
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				symbolList = new ArrayList<String>();
				do {
					symbolList.add(rs.getString(1));
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return symbolList;
	}

	public static ArrayList<GeneResult> selectGeneResultListCount(String symbol, int type) {
		ArrayList<GeneResult> grList = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String tableName = "";
			if (type == 0) {
				tableName = "macdGene";
			} else {
				tableName = "stochGene";
			}
			String query = "SELECT * FROM " + tableName + " WHERE symbol = '" + symbol + "' order by profit desc";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				grList = new ArrayList<GeneResult>();
				do {
					grList.add(new GeneResult(rs.getString(1), new Gene(rs.getString(2), rs.getDouble(3)),
							rs.getInt(4), new TradeDate(rs.getInt(5)), new TradeDate(rs.getInt(6))));
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return grList;
	}

	public static List<GeneResult> getGeneBySymbol(String symbol) {
		ResultSet rs;
		List<GeneResult> grList = new ArrayList<GeneResult>();
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
		//	String query = "SELECT * FROM macdGene WHERE symbol = '" + symbol + "' and profit > " + VVIPManager.checkProfit +" order by endtime desc";
			String query = "SELECT * FROM macdGene WHERE symbol = '" + symbol  +"' order by endtime desc";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				grList = new ArrayList<GeneResult>();
				do {
					grList.add(new GeneResult(rs.getString(1), new Gene(rs.getString(2), rs.getDouble(3)),
							rs.getInt(4), new TradeDate(rs.getInt(5)), new TradeDate(rs.getInt(6))));
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return grList;
	}
	
	public static List<String> getTotalGene() {
		ResultSet rs;
		List<String> symbolList = new ArrayList<String>();
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select DISTINCT symbol from macdgene";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				do {
					symbolList.add(rs.getString(1));
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return symbolList;
	}

	public static int getMacdGeneEndTime(String symbol) {
		int endTime = -1;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "SELECT max(endtime) from macdgene where symbol ='" + symbol + "'";
			rs = stmt.executeQuery(query);
			if (rs != null && rs.next()) {
				endTime = rs.getInt(1);
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return endTime;
	}



	public static synchronized int getMACDDateFromTrade(String symbol) {
		int macdDate = 0;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select MACD_DATE from trade where SELL_DATE = 0  and symbol = '" + symbol + "'";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				do {
					return rs.getInt(1);
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return macdDate;
	}
	
	public static synchronized boolean isBuyedSymbol(String symbol) {
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
//			String query = "select MACD_DATE from trade where symbol = '" + symbol + "'";
			String query = "select MACD_DATE from trade where symbol = '000140'";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				do {
					return true;
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	

	public static synchronized List<TradeType> selectTradeNotSell() {
		List<TradeType> symbolList = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select * from trade where sell_date =0";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				symbolList = new ArrayList<TradeType>();
				do {
					symbolList.add(new TradeType(rs.getString(1), rs.getInt(3)));
				} while (rs.next());
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return symbolList;
	}

	// ///////////////////////////////////////
	// pattern Table
	// /////////////////////////////////////

	public static synchronized void createPatternTable() {
		try {
			Connection dbConn = getConnection();
			if (null != dbConn) {
				semaphoreAcquire();
			}
			Statement stmt = dbConn.createStatement();
			String query = "CREATE TABLE pattern (" + "Symbol VARCHAR(512) NOT NULL,  "
					+ "pattern VARCHAR(1024) NOT NULL ,  " + "gene VARCHAR(1024) NOT NULL ,  "
					+ "profit double NOT NULL ,  " + "plus double NOT NULL ,  " + "minus double NOT NULL,   "
					+ "otherPlus double NOT NULL ,  " + "otherMinus double NOT NULL   " + ")";
			stmt.executeUpdate(query);
			System.out.println("Success : createPatternTable");
			if (null != dbConn) {
				semaphoreRelease();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void updatePattern(String info, String option) {

		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;

		try {
			stmt = dbConn.createStatement();
			String data[] = info.split("#");
			String query = "Update pattern set" + " profit = " + data[0].trim() + ", plus = " + data[1].trim()
					+ ", minus = " + data[2].trim() + " where " + option;
			// System.out.println("query : " + query);
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void updateOtherPattern(String info, String option) {

		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;

		try {
			stmt = dbConn.createStatement();
			String data[] = info.split("#");
			String query = "Update pattern set" + " otherPlus = " + data[0].trim() + ", otherMinus = " + data[1].trim()
					+ " where " + option;
			// System.out.println("query : " + query);
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void insertPattern(String symbol, String info) {

		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;

		try {
			stmt = dbConn.createStatement();
			String data[] = info.split("#");
			String query = "INSERT INTO pattern (Symbol, pattern, gene, profit, plus, minus, otherPlus, otherMinus"
					+ ") " + "VALUES ('" + data[0].trim() + "', '" + data[1].trim() + "', '" + data[2].trim() + "', "
					+ data[3].trim() + ", " + data[4].trim() + ", " + data[5].trim() + ",0,0)";
			// System.out.println("query : " + query);

			stmt.executeUpdate(query);

			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<String> isPattern(String option) {
		List<String> returnList = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select * from pattern where " + option;
			// System.out.println("query : "+query);
			rs = stmt.executeQuery(query);
			if (null != rs) {
				ResultSetMetaData rsMetaData = rs.getMetaData();
				int nRsMetaDataCount = rsMetaData.getColumnCount();
				while (rs.next()) {
					returnList = new ArrayList<String>();
					for (int i = 1; i < nRsMetaDataCount + 1; i++) {
						if (rsMetaData.getColumnTypeName(i).contains("INTEGER")) {
							returnList.add(Integer.toString(rs.getInt(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("BIGINT")) {
							returnList.add(Long.toString(rs.getLong(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("VARCHAR")) {//$NON-NLS-1$
							returnList.add(rs.getString(rsMetaData.getColumnName(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("DOUBLE")) {//$NON-NLS-1$
							returnList.add(Double.toString(rs.getDouble(rsMetaData.getColumnName(i))));
						} else {
							System.out.println("Error undefine Type : " + rsMetaData.getColumnTypeName(i));
						}
					}
				}
			}
			// System.out.println("selectQuoteListByDate : "+query);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != dbConn) {
			semaphoreRelease();
		}
//		close(dbConn);
		return returnList;
	}

	public static List<List<String>> isOtherPattern(String option) {
		List<List<String>> returnList = new ArrayList<List<String>>();
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select * from pattern where " + option;
			rs = stmt.executeQuery(query);
			if (null != rs) {
				ResultSetMetaData rsMetaData = rs.getMetaData();
				int nRsMetaDataCount = rsMetaData.getColumnCount();
				while (rs.next()) {
					List<String> rowData = new ArrayList<String>();
					for (int i = 1; i < nRsMetaDataCount + 1; i++) {
						if (rsMetaData.getColumnTypeName(i).contains("INTEGER")) {
							rowData.add(Integer.toString(rs.getInt(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("BIGINT")) {
							rowData.add(Long.toString(rs.getLong(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("VARCHAR")) {//$NON-NLS-1$
							rowData.add(rs.getString(rsMetaData.getColumnName(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("DOUBLE")) {//$NON-NLS-1$
							rowData.add(Double.toString(rs.getDouble(rsMetaData.getColumnName(i))));
						} else {
							System.out.println("Error undefine Type : " + rsMetaData.getColumnTypeName(i));
						}
					}
					returnList.add(rowData);
				}
			}

			// System.out.println("selectQuoteListByDate : "+query);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != dbConn) {
			semaphoreRelease();
		}
//		close(dbConn);
		return returnList;
	}

	public static List<List<String>> selectPattern(String symbol) {
		List<List<String>> returnList = new ArrayList<List<String>>();
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select * from pattern where symbol = '" + symbol + "' order by profit desc";
			rs = stmt.executeQuery(query);
			if (null != rs) {
				ResultSetMetaData rsMetaData = rs.getMetaData();
				int nRsMetaDataCount = rsMetaData.getColumnCount();
				while (rs.next()) {
					List<String> rowData = new ArrayList<String>();
					for (int i = 1; i < nRsMetaDataCount + 1; i++) {
						if (rsMetaData.getColumnTypeName(i).contains("INTEGER")) {
							rowData.add(Integer.toString(rs.getInt(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("BIGINT")) {
							rowData.add(Long.toString(rs.getLong(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("VARCHAR")) {//$NON-NLS-1$
							rowData.add(rs.getString(rsMetaData.getColumnName(i)));
						} else if (rsMetaData.getColumnTypeName(i).contains("DOUBLE")) {//$NON-NLS-1$
							rowData.add(Double.toString(rs.getDouble(rsMetaData.getColumnName(i))));
						} else {
							System.out.println("Error undefine Type : " + rsMetaData.getColumnTypeName(i));
						}
					}
					returnList.add(rowData);
				}
			}

			// System.out.println("selectQuoteListByDate : "+query);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != dbConn) {
			semaphoreRelease();
		}
//		close(dbConn);
		return returnList;
	}

	// ///////////////////////////////////////
	// SymbolTable
	// /////////////////////////////////////

	public static synchronized void createSymbolTable(String symbol) {
		try {
			Connection dbConn = getConnection();
			if (null != dbConn) {
				semaphoreAcquire();
			}
			Statement stmt = dbConn.createStatement();
			String query = "create table SYMBOL_"
					+ symbol
					+ " ("
					// +"Index int GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1)  , "
					// + "Symbol INT NOT NULL ,  "
					+ "Date INT NOT NULL PRIMARY KEY,  " + "Low DOUBLE NULL ,  " + "High DOUBLE NULL ,  "
					+ "Open DOUBLE NULL ,  " + "Close DOUBLE NULL ,  " + "Volume DOUBLE NULL ,  "
					+ "Pattern VARCHAR(60) NULL   " + ")";

			stmt.executeUpdate(query);
			System.out.println("Success : createTable : " + symbol);
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static QuoteList selectQuoteListByDate(String symbol, int start, int end) {
		QuoteList quotes = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "SELECT * FROM symbol_" + symbol + " WHERE " + start + " <= " + " Date and Date <=" + end
					+ " order by Date asc ;";

			rs = stmt.executeQuery(query);

			if (rs.next()) {
				quotes = new QuoteList(symbol);
				do {
					quotes.addQuote(new Quote((new TradeDate(rs.getInt(1))), rs.getDouble(2), rs.getDouble(3), rs
							.getDouble(4), rs.getDouble(5), rs.getLong(6), rs.getString(7)));
				} while (rs.next());
			}

			// System.out.println("selectQuoteListByDate : "+query);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			CommonUtil.removeSymbol.add(symbol);
			e.printStackTrace();
		}

		if (null != dbConn) {
			semaphoreRelease();
		}
//		close(dbConn);
		return quotes;
	}

	public static synchronized void insertQuoteListToSymbol(QuoteList quoteList, String symbol) {

		int size = quoteList.getSize();
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;

		int yesterday = 0;
		try {
			stmt = dbConn.createStatement();
			for (int i = 0; i < size; i++) {
				Quote qt = quoteList.getQuote(i);
				String query = "INSERT INTO SYMBOL_" + symbol + "(Date, Open, Close, Volume, High, Low, Pattern) "
						+ "VALUES (" + qt.getTradeDate().toInt() + ", " + qt.getOpen() + ", " + qt.getClose() + ", "
						+ qt.getVolume() + ", " + qt.getHigh() + ", " + qt.getLow() + ", '" + qt.getPattern().trim()
						+ "')";
				System.out.println("query : " + query);

				if (yesterday != qt.getTradeDate().toInt()) {
					stmt.executeUpdate(query);
				}

				yesterday = qt.getTradeDate().toInt();
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void insertQuoteToSymbol(Quote qt, String symbol) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "INSERT INTO SYMBOL_" + symbol + "(Date, Open, Close, Volume, High, Low, Pattern) "
					+ "VALUES (" + qt.getTradeDate().toInt() + ", " + qt.getOpen() + ", " + qt.getClose() + ", "
					+ qt.getVolume() + ", " + qt.getHigh() + ", " + qt.getLow() + ", '" + qt.getPattern().trim() + "')";
			System.out.println("query : " + query);
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteQuoteTable(String symbol) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			stmt.executeUpdate("Delete FROM symbol_" + symbol);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != dbConn) {
			semaphoreRelease();
		}
		close(dbConn);
	}

	public static void deleteSymbolQuoteTable(String symbol, int date) throws SQLException {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt = dbConn.createStatement();

		// String query = "Delete FROM `" + schema + "`.QuoteTable_" +
		// marketIndex + "_"+ hashNum + " WHERE symbol = "
		// + symbol + " and Date =" + date + ";";
		// stmt.execute(query);

		stmt.close();
		if (null != dbConn) {
			semaphoreRelease();
		}
		close(dbConn);
	}

	public static QuoteList selectLimitQuoteListBySymbol(String symbol, int start, int count, boolean sort) {
		QuoteList quotes = null;
		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "select * from symbol_" + symbol + " order by Date desc";
			rs = stmt.executeQuery(query);
			quotes = new QuoteList(symbol);
			if (rs.next()) {
				
				do {
					quotes.addQuote(new Quote((new TradeDate(rs.getInt(1))), rs.getDouble(2), rs.getDouble(3), rs
							.getDouble(4), rs.getDouble(5), rs.getLong(6), rs.getString(7)));
				} while (rs.next());

				if (sort) {
					quotes.reverseList();
				}
			}
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
//			close(dbConn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return quotes;
	}


	public static Quote getLastQuoteDifferenceBySymbol(String symbol) throws SQLException {
		Quote quote = null;
//		ResultSet rs;
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}

		if (null != dbConn) {
			semaphoreRelease();
		}
		close(dbConn);
		return quote;
	}

	

	public static synchronized void deleteAllGATable() {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "delete from macdGene";
			stmt.executeUpdate(query);
			stmt.close();
			if (null != dbConn) {
				semaphoreRelease();
			}
			close1(dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void deleteQuote(String symbol, int start, int end) {
		Connection dbConn = getConnection();
		if (null != dbConn) {
			semaphoreAcquire();
		}
		Statement stmt;
		try {
			stmt = dbConn.createStatement();
			String query = "delete from symbol_" + symbol + " where date >= " + start + " and date <= " + end;
			stmt.execute(query);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != dbConn) {
			semaphoreRelease();
		}
		close(dbConn);
	}

}
