package mw.cache;

import javax.xml.bind.JAXBException;

import mw.path.MWMyPathService;
import mw.path.MWNoPathException;

public class MWNoSuchKeyException extends Exception {
	 
	public MWNoSuchKeyException()  {
		System.out.println("NO Such Key !!!!!!! call MWFacebookService to calculatePath!!! ");
   }
}
