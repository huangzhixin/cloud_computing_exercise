package mw.path;

import java.util.List;

import javax.xml.bind.JAXBException;

import mw.cache.MWNoSuchKeyException;


public interface MWPathServiceInterface {
	
	public String[] calculatePath(String startID, String endID) throws MWNoPathException, JAXBException, MWNoSuchKeyException;

}
