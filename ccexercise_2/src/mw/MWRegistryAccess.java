package mw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;


import javax.xml.registry.*;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;




public class MWRegistryAccess {
		
		// Aufbau der Verbindung
		private Connection connection;
		private RegistryService regSvc;
		
		public MWRegistryAccess() {
	        Locale.setDefault(Locale.US);
	    }

		public void openConnection(String queryMangerURL, String lifeCycleManagerURL) {
				// Zusammenstellung der Verbindungsdaten
				Properties props = new Properties();
				
				props.setProperty("javax.xml.registry.queryManagerURL", queryMangerURL);
				props.setProperty("javax.xml.registry.lifeCycleManagerURL",lifeCycleManagerURL);
				
				try {
					
					ConnectionFactory fact = ConnectionFactory.newInstance();
					//==============================
					fact.setProperties(props);
					//=============================
					connection = fact.createConnection();
					//connection.setCredentials(authenticate("group8", ""));
					//Rein lesende Operationen ! keine Authentifizierung notwendig
					//System.out.println(connection.isClosed());
					
					regSvc = connection.getRegistryService();
					} catch(Exception e) {
						System.out.println("connection faild!");
					}
		}
		
		
		public void closeConnection(){
				try {
					connection.close();
					//System.out.println(connection.isClosed());
				} catch (JAXRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} 
		
		public Collection<Organization> getOrganizations(String organzationName) throws JAXRException{
			// Erzeugung der Suchkriterien
			Collection<String> findQualifiers = new ArrayList<String>();
			findQualifiers.add(FindQualifier.SORT_BY_NAME_ASC);
			// Erzeugung der Namenskriterien			
			Collection<String> orgNamePatterns = new ArrayList<String>();
			orgNamePatterns.add("%"+organzationName+"%");
			
			BusinessQueryManager m = regSvc.getBusinessQueryManager();
			
			//Wenn man möchtet Organizations suchen, kann man schreiben:
			BulkResponse br = m.findOrganizations(findQualifiers,orgNamePatterns, null, null, null, null);
			Collection<Organization> orgs = br.getCollection();
			return orgs;
		}
		
		public void removeOrganizations(String organzationName) throws JAXRException{
		
			Collection<Organization> orgs = getOrganizations(organzationName);
			BusinessLifeCycleManager lcm;
			lcm = regSvc.getBusinessLifeCycleManager();
			//InternationalString os = lcm.createInternationalString(organzationName);
			//lcm.deleteOrganizations(orgs);
			Collection<Key> orgKeys = new ArrayList<Key>();
			for(Organization org : orgs)
			{
				orgKeys.add(org.getKey());
			}
			lcm.deleteOrganizations(orgKeys);
			//lcm.saveOrganizations(orgs);
		}
		
		public void listOrganizations(String organzationName) throws JAXRException{
			
			Collection<Organization> orgs = getOrganizations(organzationName);
			
			for(Organization org : orgs)
			{
               System.out.println(org.getName());
			}	
			
		}
		
		public void removeservice(String organzationName, String serviceName) throws JAXRException{
				Collection<Service> services = this.getServices(organzationName, serviceName);
				Collection<Organization> orgs = getOrganizations(organzationName);
				for(Organization org : orgs){
					org.removeServices(services);
				}
				BusinessLifeCycleManager lcm;
				try {
					lcm = regSvc.getBusinessLifeCycleManager();
					InternationalString os = lcm.createInternationalString(organzationName);
					BulkResponse response = lcm.saveOrganizations(orgs);
					
				} catch (JAXRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		public Collection<Service> getServices(String organzationName, String serviceName) throws JAXRException{

			Collection<String> namePatterns = new ArrayList<String>();
			namePatterns.add("%"+serviceName+"%");
			// Ausfuehrung der Suche
			
			BusinessQueryManager m = regSvc.getBusinessQueryManager();
			//Wenn man möchtet Organizations suchen, kann man schreiben:
			Collection<Organization> orgs = this.getOrganizations(organzationName);
			if(orgs.size()>0){
				Key orgKey = null;
				for(Organization org : orgs)
				{
				   orgKey = org.getKey();
				}		
				BulkResponse br = m.findServices(orgKey, null, namePatterns, null, null);
			Collection<Service> services = br.getCollection();
			return services;
			}
			else{
				return new ArrayList<Service>();
			}
				
		}
		public String listWSDLs(String organzationName,String serviceName){
			
			  try {
					Collection<Service> services = this.getServices(organzationName, serviceName);
					//System.out.println(services.size());
			
					for(Service s: services){
						Collection<ServiceBinding> sb  = s.getServiceBindings();
						for(ServiceBinding address: sb){
							System.out.println(address.getAccessURI());
							return address.getAccessURI();
						}
					}
				} catch(Exception e) { 
					System.out.println("list WSDL faild!");
				}
			return null;
		}
		
		
		//Einloggen mit Benutzername und Passwort
		public void authenticate(String userName, String password){
			
			PasswordAuthentication pa = new PasswordAuthentication(userName,password.toCharArray());
			Set<PasswordAuthentication> credentials =new HashSet<PasswordAuthentication>();
			credentials.add(pa);
			try {
				connection.setCredentials(credentials);
			} catch (JAXRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//Registration von  Service
		public void registerService(String orgName, String serviceName, String wsdlURL) throws JAXRException
		{
			BusinessLifeCycleManager lcm;
			Collection<Organization> orgs = this.getOrganizations(orgName);
			try {
				lcm = regSvc.getBusinessLifeCycleManager();
				if(orgs.size()==0){
				  System.out.println("organization not found,create new organization");
				  //1. createOrganization
				  InternationalString os = lcm.createInternationalString(orgName);
				  Organization organization = lcm.createOrganization(os);
				  //2.createService
				  InternationalString ss = lcm.createInternationalString(serviceName);
				  Service service = lcm.createService(ss);
				  organization.addService(service);
				  //organization.removeService(service);
				  //3.createServiceBinding
				  ServiceBinding binding = lcm.createServiceBinding();
				  binding.setAccessURI(wsdlURL);
				  service.addServiceBinding(binding);
				  orgs = new ArrayList<Organization>(1);
				  orgs.add(organization);
				  BulkResponse response = lcm.saveOrganizations(orgs);
				}
				else if(this.getOrganizations(orgName).size()>1){
					System.out.println("too many organization with same name,clean all organization");
					this.removeOrganizations(orgName);
				}
				else{ 
				  System.out.println("organization hat existed,remove the old address for service and add new address!");
				  
				  this.removeservice(orgName, serviceName);
				  //2.createService
				  InternationalString ss = lcm.createInternationalString(serviceName);
				  Service service = lcm.createService(ss);
				  
				  for(Organization org : orgs){
					  org.addService(service);
				  }
				  //3.createServiceBinding
				  ServiceBinding binding = lcm.createServiceBinding();
				  binding.setAccessURI(wsdlURL);
				  service.addServiceBinding(binding);
				  BulkResponse response = lcm.saveOrganizations(orgs);
				  
				}
				
				
				
			} catch (JAXRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		public static void main(String[] args) throws JAXRException, UnsupportedEncodingException, IOException {
			//
			Locale.setDefault(Locale.US);
			//System.out.println(Locale.getDefault()); 
			// TODO Auto-generated method stub
			Tools curl = new  Tools();
			String registryURL = curl.CURL("https://www.ibr.cs.tu-bs.de/courses/ss16/cc/pub/exercise1/ws-registry.adr");
			//String registryURL = 	"http://134.169.47.184:4222/juddi";
			String queryManagerURL = registryURL + "/inquiry";
			String lifeCycleManagerURL = registryURL + "/publish";
			MWRegistryAccess MWRA = new MWRegistryAccess();
			MWRA.openConnection(queryManagerURL, lifeCycleManagerURL);
			String uri = "http://er1.169.47.28:12346/MWMyPathSrv";
			MWRA.authenticate("gruppe8", "");
			//MWRA.removeservice("gruppe8","MWPathService");
			//MWRA.listOrganizations("gruppe8");
			//MWRA.removeOrganizations("gruppe8");
			//MWRA.registerService("gruppe8", "MWPathService", uri + "?wsdl");
			
			MWRA.listOrganizations("gruppe8");
			 
			MWRA.listWSDLs("gruppe8","MWPathService");
			
			
			
			
			if(args.length == 2 && args[0].equals("LIST")){
				MWRA.listWSDLs(args[1],args[2]);
				//MWRA.listWSDLs("MWFacebookService");
				//MWRA.listWSDLs("MWPathService");
			}
			else if(args.length == 2 && args[0].equals("PUBLISH")){
				MWRA.authenticate("gruppe8", "");
				MWRA.registerService("gruppe8", "MWPathService", uri + "?wsdl");
			}
			else{
				System.out.println("error");
			}
			
			MWRA.closeConnection();
			
		}
		
}
