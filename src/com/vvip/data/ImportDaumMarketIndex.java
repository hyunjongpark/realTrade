package com.vvip.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * UTF-8�� ���ڵ� �Ǿ�� ������ ã�� �� �ִ�.
 * Daum ����Ʈ�� �ش��ϴ�  URL�������� �����Ͽ� 
 * ���������� �Ľ��Ͽ� �����´�.
 * @author GyuL
 *
 */
public class ImportDaumMarketIndex {
	private final static String DAUM_URL_STRING = ("http://stock.daum.net/?t__nil_bestservice=stock");
	private final static String [] market = {"�ڽ���", "�ڽ���", "�ڽ���200", "��/�޷�", "�ٿ�����", "������", "S&P500"};	
	
	public ImportDaumMarketIndex() {
		assert false;
	}
	
	static public ArrayList<String> importData() throws ImportException {
		int index = 0;
		String line = null;
		String URLString = DAUM_URL_STRING;		
		String filterString = constructFilterString(index++);
		ArrayList<String> indexList = new ArrayList<String> ();
		
		try {
			URL url = new URL(URLString);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			
			InputStreamReader input = new InputStreamReader(connection.getInputStream(), "UTF-8");
			
			
			BufferedReader bufferedInput = new BufferedReader(input);		
			
			while ( ( line = bufferedInput.readLine() ) != null ) {				
				if ( line.indexOf(filterString) != -1 ) {
					indexList.add(parseLine(line));
					if ( index >= 7 )
						break;
					else {
						filterString = constructFilterString(index++);
					}	
				}
			}
			
			if ( line == null ) {
				bufferedInput.close();
				throw new ImportException("Import Error : Data is not exist");
			}		
		}
		catch (Exception e) {
			throw new ImportException("Import Error : " + e.getMessage());
		}
		return indexList;
	}

	static public String parseLine(String line) throws ImportException {
		try {
			int start = line.indexOf("value\">");
			int end = line.indexOf("</span>");
			String close = line.substring(start + 7, end);
			line = line.substring(end);

			start = line.indexOf("<b>");
			end = line.indexOf("</b>");
			String diff = line.substring(start + 3, end);
			line = line.substring(end);

			start = line.indexOf("changePercentage\">");
			end = line.lastIndexOf("</span>");
			String percent = line.substring(start + 18, end);

			if (percent.charAt(0) == '-')
				diff = "��" + diff;
			else
				diff = "��" + diff;

			return close + "##" + diff + "##" + percent;
		} catch (Exception e) {
			throw new ImportException(e.getMessage());
		}
	}

	static public String constructFilterString(int index) {
		return ">" + market[index] + "</a>";
	}
}
