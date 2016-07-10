package mw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Tools {
	public String CURL(String Addresss) throws UnsupportedEncodingException, IOException{
		//URL url = new URL("http://169.254.169.254/latest/meta-data/public-ipv4");
		URL url = new URL(Addresss);
		String externalip="";
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
		    for (String line; (line = reader.readLine()) != null;) {
		        System.out.println("external ip address is "+line);
		        externalip+=line;
		    }
		}
		return externalip;
	}
	public String[] decode(String key){
		List<String> list = new ArrayList<String>();
		for(int i=0;i<key.length();i++){
			String currentStr = key.substring(i);
			int indexofnumber = currentStr.indexOf(":");
			int number = new Integer(currentStr.substring(0, indexofnumber));
			String tureStr=currentStr.substring(indexofnumber+1, indexofnumber+1+number);
			list.add(tureStr);
			i+=indexofnumber+1+number;
		}
		String[] keys = new String[list.size()];
		for(int i=0;i<list.size();i++){
			keys[i]=list.get(i);
		}
		return keys;
	}
}
