package mw.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBException;


import mw.Tools;
import mw.cache.MWCacheClient;
import mw.cache.MWNoSuchKeyException;
import mw.facebookclient.MWFacebookService;
import mw.facebookclient.MWMyFacebookService;
import mw.facebookclient.MWUnknownIDException_Exception;
import mw.facebookclient.StringArray;


@WebService(name = "MWPathService", serviceName = "MWPathService")
@SOAPBinding(style = SOAPBinding.Style.RPC)



public class MWMyPathService implements MWPathServiceInterface{
	

	private MWMyFacebookService MyFacebookService;
	
	 //bekommen MWMyFacebookService fb
	public MWMyPathService(){
		MyFacebookService  = new MWFacebookService().getMWMyFacebookServicePort();
	}
	
	@WebMethod
	public String[] remoteCalculatePath(String startID, String endID) throws MWNoPathException {
		
		List<String> sAllFriends = null,eAllFriends = null;
		
		//sAF und eAF speichern die  Freundeskreis von  friendsStart  und friendsEnd
		Set sAF = null,eAF = null;
		
		//  sAFtemp und eAFtemp speichern die neue Freunden von letztem Loop
        // Am End von jedem Loop musst diese Zwei Liste clear werden, weil sie temporäre Variable sind
		Set sAFtemp = new HashSet(), eAFtemp = new HashSet();
		
		try {
			StringArray newStartFriends = MyFacebookService.getFriends(startID);
			StringArray newEndFriends = MyFacebookService.getFriends(endID);
			sAllFriends = newStartFriends.getItem();
			eAllFriends = newEndFriends.getItem();
			
			sAF = new HashSet(sAllFriends);
			eAF = new HashSet(eAllFriends);
			sAF.add(startID);
			eAF.add(endID);
						
		} catch (MWUnknownIDException_Exception e) {
			
			e.printStackTrace();
		}
		
		
		/*Iterator<String> sit = sAF.iterator();
		while(sit.hasNext())
		{
				System.out.println(sit.next());	
		}
		System.out.println("++++++++++++");
		Iterator<String> eit = eAF.iterator();
		while(eit.hasNext())
		{
				System.out.println(eit.next());
		}
		*/
		
		//Hauptloop
		while(true)
		{
			
			//Wenn die zwei Freundeskreis von  friendsStart  und friendsEnd schneiden
			//，break und MWDijkstra.getShortestPath
			if(!Collections.disjoint(sAF, eAF))
			{
				break;
			}
			
			
			//Finden alle Frienden von menschen, wem in sAF(Freundeskreis von  friendsStart) sind;
			// und solche neue Freunden werden in sAFtemp speichert.
			Iterator<String> sit = sAF.iterator();
			while(sit.hasNext())
			{
				try {
					
					sAFtemp.addAll(MyFacebookService.getFriends(sit.next()).getItem());
					
				} catch (MWUnknownIDException_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			int sSize1 = sAF.size();
			sAF.addAll(sAFtemp);    //neue Freunden werden in sAF speichert.
			int sSize2 = sAF.size();		
			sAFtemp.clear();             //clear sAFtemp
					
			
			//System.out.println("+++++++++");
			
			
			//Finden alle Frienden von menschen, wem in eAF(Freundeskreis von  friendsEnd) sind;
			// und solche neue Freunden werden in eAFtemp speichert.
			Iterator<String> eit = eAF.iterator();
			while(eit.hasNext())
			{
				try {
					eAFtemp.addAll(MyFacebookService.getFriends(eit.next()).getItem());
				} catch (MWUnknownIDException_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			int eSize1 = eAF.size();
			eAF.addAll(eAFtemp);   //neue Freunden werden in eAF speichert.
			int eSize2 = eAF.size();
			eAFtemp.clear();            //clear eAFtemp
			
			//Nach Hinzufügen neuer Freunde， wenn die Zahle von diesen zwei Freundeskreis nicht steigen,
			//d.h diese zwei Freundeskreis haben ewig kein Schneiden---->throw MWNoPathException
			if(sSize1 == sSize2 && eSize1 == eSize2)
			{
				throw new MWNoPathException("noPath");
			}
			
		}
			
		//System.out.println("********");
		//wenn diese zwei Freundeskreis ein Schneiden haben,
		//löschen die doppelten Daten
		//und speichern alle Freunden in ein Set für  MWDijkstra
		Set aF = new HashSet();
		aF.addAll(sAF);
		aF.addAll(eAF);
		
		/*Iterator<String> it = aF.iterator(); 
		while(it.hasNext())
		{
			String value = it.next();
			System.out.println(value);
		}*/
		
		return MWDijkstra.getShortestPath(MyFacebookService, aF, startID, endID);
	}
	
	
	
	public String[] calculatePath(String startID, String endID) throws MWNoPathException, JAXBException, MWNoSuchKeyException{
		MWCacheClient client = new MWCacheClient();
		int lenOfstartID = startID.length();
		int lenOfiendID = endID.length();
		String result = client.getObject(lenOfstartID+":"+startID+":"+lenOfiendID+":"+endID);
		//=================================================================
		//result = "2:as:5:asdfg:3:qwe"
		Tools decode = new Tools();
		String[] paths = decode.decode(result);
		for (String str : paths) {
			   System.out.println(str);
		}
		System.out.println("number of node is : " + paths.length);
		
		return paths;
	}
	
	
	public static void main(String[] args) throws MWNoSuchKeyException, JAXBException {
        MWMyPathService s = new MWMyPathService();
        //String[] path = s.calculatePath("1694452301", "100000859170147");
        String[] path;
		try {
			//path = s.calculatePath("1694452301", "100000859170147");
			//System.out.println("number of node is : " + path.length);
			//path = s.calculatePath("1832770518", "100000690315984");
			//for (String str : path) {
			 //   System.out.println(str);
			//}
			//path = s.calculatePath("1832770518", "100000690315984");
			path = s.remoteCalculatePath("1832770518", "100000690315984");
			//path = s.judgement("1694452301", "100000859170147");
		} catch (MWNoPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	
}
