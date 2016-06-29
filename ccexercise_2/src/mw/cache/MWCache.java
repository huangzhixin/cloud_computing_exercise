package mw.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import mw.cache.generated.MWMessage;
import mw.cache.generated.ObjectFactory;

@WebServiceProvider
@ServiceMode(value=Service.Mode.PAYLOAD)

public class MWCache implements Provider<Source> {
	@javax.annotation.Resource(type=WebServiceContext.class)
	protected WebServiceContext wsContext;
	private final JAXBContext jaxbContext;
	private static final String contextPath = "mw.cache.generated";
	private final Unmarshaller unmarshaller;
	private final ObjectFactory factory;
	protected final Map<String,String> cache;
	
	public MWCache(){
		try{
			jaxbContext = JAXBContext.newInstance(MWCache.contextPath);
			unmarshaller = jaxbContext.createUnmarshaller();
		}catch(Exception exc){
			throw new RuntimeException(exc);
		}
		factory = new ObjectFactory();
		cache = new HashMap<String,String>(1000);
	}
	private Source processAddRequest(String key, Source requestSource) throws JAXBException{
		try{
			JAXBElement request = (JAXBElement) unmarshaller.unmarshal(requestSource);
			MWMessage input = (MWMessage) request.getValue();
			String Value = cache.put(key, input.getMessage());
			MWMessage responeMessageBody = factory.createMWMessage();
			
			if(Value == null){
				responeMessageBody.setMessage("null");
				responeMessageBody.setStatus("404");
			}else{
				responeMessageBody.setMessage("successful:"+Value);
				responeMessageBody.setStatus("200");
			}
			return new JAXBSource(jaxbContext, factory.createMWCacheReply(responeMessageBody));
		}catch(JAXBException e){
			
			MWMessage responeMessageBody = factory.createMWMessage();
			responeMessageBody.setMessage("failed");
			responeMessageBody.setStatus("201");
			return new JAXBSource(jaxbContext, factory.createMWCacheReply(responeMessageBody));
		}
	}
	
	private Source processGetRequest(String key) throws JAXBException{
		System.out.println("get key:"+key);
		try{
			MWMessage responeMessageBody = factory.createMWMessage();
			
			if(!cache.containsKey(key)){
				responeMessageBody.setMessage("not found failed");
				responeMessageBody.setStatus("404");
			}else{
				responeMessageBody.setMessage(cache.get(key));
			}
			
			return new JAXBSource(jaxbContext, factory.createMWCacheReply(responeMessageBody));
		}catch (JAXBException e) {
			MWMessage responeMessageBody = factory.createMWMessage();
			responeMessageBody.setMessage("failed");
			responeMessageBody.setStatus("201");
			return new JAXBSource(jaxbContext, factory.createMWCacheReply(responeMessageBody));
		}
	}
			
	public Source invoke(Source source){
		MessageContext mc = wsContext.getMessageContext();
		String httpMethod = (String) mc.get(MessageContext.HTTP_REQUEST_METHOD);
		String path = (String) mc.get(MessageContext.PATH_INFO);
		System.out.println(httpMethod +" request, cache:" + path);
		switch(httpMethod)
		{
		case "POST":
			try {
				return processAddRequest(path,source);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		case "GET":
			try {
				return processGetRequest(path);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		default:
			System.out.println("default");
		}
		return source; 
		
	}
	
	
	public static void main(String[] args){
	MWCache mwcache= new MWCache();
	
	Endpoint endpoint=Endpoint.create(HTTPBinding.HTTP_BINDING,mwcache);
	endpoint.publish("http://0.0.0.0:12346/cache-service/");
	}
}

