package mw.path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.xml.ws.Endpoint;

import mw.MWRegistryAccess;
import mw.Tools;

public class MWPathPubishSrv {
	
	public static void main(String[] args) throws Exception {

		
		
     
		// access metadata service --> find external IP of VM
		//   => ask Mattias
		//TODO
		// "http://<external-IP>:12345/MWPathSrv?wsdl";
		
		// remove stale entries from registry
		// remove everything from group8
		//TODO
		
		// register service IP at registry
		//TODO
		
		Tools curl = new  Tools();
		String externalip = InetAddress.getLocalHost().getHostAddress();
		//String externalip = curl.CURL("http://169.254.169.254/latest/meta-data/public-ipv4");	
		//String registryURL = "http://134.169.47.184:4222/juddi";
		String registryURL = curl.CURL("https://www.ibr.cs.tu-bs.de/courses/ss16/cc/pub/exercise1/ws-registry.adr");
		
		Locale.setDefault(Locale.US);
		String queryManagerURL = registryURL + "/inquiry";
		String lifeCycleManagerURL = registryURL + "/publish";
		MWRegistryAccess MWRA = new MWRegistryAccess();
		MWRA.openConnection(queryManagerURL, lifeCycleManagerURL);
		
		
		MWRA.authenticate("gruppe8", "");
		MWRA.registerService("gruppe8", "MWPathService", "http://" +externalip + "/MWPathSrv?wsdl");
		MWRA.listWSDLs("gruppe8","MWPathService");
		
		
		
		String wsdl = "http://0.0.0.0:12345/MWPathSrv?wsdl";
		MWPathServiceInterface adder = new MWMyPathService();
		Endpoint e = Endpoint.publish(wsdl, adder);
		System.out.println("Adder ready: " + e.isPublished());

	
		while(true) {
			
			Thread.sleep(Long.MAX_VALUE);

		}

	}

}
