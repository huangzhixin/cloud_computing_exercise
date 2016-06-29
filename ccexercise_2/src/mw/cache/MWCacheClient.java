package mw.cache;

import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import mw.Tools;
import mw.cache.generated.MWMessage;
import mw.cache.generated.ObjectFactory;
import mw.path.MWMyPathService;
import mw.path.MWNoPathException;

public class MWCacheClient {
	public void addObject(String key, String value) throws JAXBException{
		// Zusammenstellung der Ressourcen-Adresse
		String path = "http://0.0.0.0:12346/cache-service/" + key;
		// Konfiguration der Service-Verbindung
		QName qName = new QName("", "");
		// Service-Endpunkt-Name
		Service service = Service.create(qName);
		service.addPort(qName, HTTPBinding.HTTP_BINDING, path);
		// Erzeugung des Binding-Context
		String contextPath = "mw.cache.generated";
		JAXBContext jc = JAXBContext.newInstance(contextPath);
		// Erzeugung des Dispatch
		Dispatch<Object> disp = service.createDispatch(qName, jc, Service.Mode.PAYLOAD);
		// Festlegung der HTTP-Methode
		Map<String, Object> rc = disp.getRequestContext();
		rc.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
		// Erzeugung der Objekt-Factory
		ObjectFactory f = new ObjectFactory();
		// Erzeugung des Aufrufparameters
		MWMessage input = f.createMWMessage();
		input.setMessage(value);
		// text: zu druckende Zeichenkette
		// Erzeugung der Anfrage
		JAXBElement<MWMessage> request = f.createMWCacheRequest(input);
		disp.invokeOneWay(request);
	}
	public String getObject(String key) throws JAXBException, MWNoSuchKeyException, MWNoPathException{
		// Zusammenstellung der Ressourcen-Adresse
		String path = "http://0.0.0.0:12346/cache-service/" + key;
		// Konfiguration der Service-Verbindung
		QName qName = new QName("", "");
		// Service-Endpunkt-Name
		Service service = Service.create(qName);
		service.addPort(qName, HTTPBinding.HTTP_BINDING, path);
		// Erzeugung des Binding-Context
		String contextPath = "mw.cache.generated";
		JAXBContext jc = JAXBContext.newInstance(contextPath);
		// Erzeugung des Dispatch
		Dispatch<Object> disp = service.createDispatch(qName, jc, Service.Mode.PAYLOAD);
		// Festlegung der HTTP-Methode
		Map<String, Object> rc = disp.getRequestContext();
		rc.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
		// Erzeugung der Objekt-Factory
		ObjectFactory f = new ObjectFactory();
		JAXBElement<MWMessage> request = f.createMWCacheRequest(null);
		// Senden der Anfrage und Empfang der Antwort
		JAXBElement reply = (JAXBElement) disp.invoke(request);
		// Auswertung der Antwort
		
		MWMessage value = (MWMessage) reply.getValue();
		//System.out.println(value.getMessage());
		if(value.getMessage().equals("not found failed")){
			
			Tools decode = new Tools();
			//key ="10:asdfghjklö:5:asdfg";
			String[] id = decode.decode(key);
			
			//id = key.split("/");
			return MWNoSuchKeyException(id[0],id[1]);
		}
		else{
		   System.out.println("path is: " + value.getMessage());
		}
		return value.getMessage();
	}
	/**
	 * @param args
	 * @throws JAXBException 
	 * @throws MWNoPathException 
	 */
	public String MWNoSuchKeyException(String startID, String endID) throws JAXBException, MWNoPathException {
		System.out.println("NO Such Key !!!!!!! call MWFacebookService to calculatePath!!! ");
		MWMyPathService s = new MWMyPathService();
        //String[] path = s.calculatePath("1694452301", "100000859170147");
		String[] path;
        String value= "";
		path = s.remoteCalculatePath(startID, endID);
		for (String str : path) {
			   int lenOfstr = str.length();
			   value+=lenOfstr+":"+str+":";
			  // System.out.println(str);
		}
		MWCacheClient client = new MWCacheClient();
		int lenOfstartID = startID.length();
		int lenOfiendID = endID.length();
		client.addObject(lenOfstartID+":"+startID+":"+lenOfiendID+":"+endID, value);
		return value;
   }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       
	}

}
