package mw.facebook;

import javax.xml.ws.Endpoint;


public class MWFacebookServer {

	public static void main(String[] args) throws Exception {
		//String wsdlURL = args[0];
		String wsdlURL  = "http://0.0.0.0:18082/MWFacebookService?wsdl";
		System.out.println("WSDL: " + wsdlURL);

		Endpoint.publish(wsdlURL, new MWMyFacebookService());

		System.out.println("Facebook service ready at \"" + args[0] + "\".");
		while(true) {
			Thread.sleep(Long.MAX_VALUE);
		}
	}

}
