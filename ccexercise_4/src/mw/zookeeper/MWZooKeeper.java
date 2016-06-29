package mw.zookeeper;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.Socket;

public class MWZooKeeper {

	private Socket socket;
	MWObjectConnection connection;
	String clientName = ManagementFactory.getRuntimeMXBean().getName();

	public MWZooKeeper() {

		try {
			socket = new Socket("192.168.0.108", 7782);
			connection = new MWObjectConnection(socket);
			System.out.println(clientName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String create(String path, byte[] data) throws MWZooKeeperException {

		MWStat state = new MWStat(0, System.currentTimeMillis());
		MWMessage msg = new MWMessage("create", path, data, state, clientName);

		MWMessage reply = null;
		try {
			connection.sendObject(msg);
			//Thread.sleep(200);
			reply = (MWMessage) connection.receiveObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reply.getPath();
	}

	public void delete(String path, int version) throws MWZooKeeperException {
		MWStat state = new MWStat(version);
		MWMessage msg = new MWMessage("delete", path, null, state, clientName);
		MWMessage reply = null;
		try {
			connection.sendObject(msg);
			reply = (MWMessage) connection.receiveObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MWStat setData(String path, byte[] data, int version)
			throws MWZooKeeperException {

		MWStat state = new MWStat(version, System.currentTimeMillis());
		MWMessage msg = new MWMessage("setData", path, data, state, clientName);
		MWMessage reply = null;
		try {
			connection.sendObject(msg);
			reply = (MWMessage) connection.receiveObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.err.println(reply.getState().version);
		return reply.getState();
	}

	public byte[] getData(String path, MWStat stat) throws MWZooKeeperException {

		MWMessage msg = new MWMessage("getData", path, null, stat, clientName);
		MWMessage reply = null;

		try {
			connection.sendObject(msg);
			reply = (MWMessage) connection.receiveObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		 stat.version = reply.getState().version;
		 stat.timestamp = reply.getState().timestamp;
		 
		return reply.getData();
	}
	
	public void closeSocket(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		MWZooKeeper zookeeper = new MWZooKeeper();
		
		 //=======================create node======================================
		  String path = "/huang";
          String teststr = "Cloud Computing is good";
		//String answ = zookeeper.create(path, teststr.getBytes());
		//System.out.println(answ);
		/*
		//=======================get data of node======================================
		MWStat answStat = new MWStat();
		byte[] answByte = zookeeper.getData(path, answStat);
		System.out.println("Antwort: " + new String(answByte) + " - Version: " + answStat.version);
	
		//=======================set data of node======================================
		String replaceStr="Cloud Computing ist sehr sehccr sehr gut";
		MWStat newAnswStat = zookeeper.setData(path, replaceStr.getBytes(),answStat.version );
		System.out.println("Neue Version: " + newAnswStat.version);
		
		//=======================get new data of node======================================
		byte[] answByte2 = zookeeper.getData(path, newAnswStat);
		System.out.println("Antwort: " + new String(answByte2) + " - Version: " + newAnswStat.version);
		
		//=======================create new node======================================
		String path1 = "/huang/home/";
		String teststr1 = "Cloud Computing is very good";
		String answ1 = zookeeper.create(path1, teststr1.getBytes());
		System.out.println(answ1);
		
		//=======================get data of second node======================================
		MWStat answStat1 = new MWStat();
		byte[] answByte1 = zookeeper.getData(path1, answStat);
		System.out.println("Antwort: " + new String(answByte1) + " - Version: " + answStat1.version);
		
		//=======================delete first node======================================
		//zookeeper.delete(path,  newAnswStat.version);

		
		//=======================get data of second node again=======result is null=====================
        //answByte1 = zookeeper.getData(path1, answStat);
		//System.out.println("Antwort: " + new String(answByte1) + " - Version: " + answStat1.version);
		
		//=======================get data of first node again==========result  is null=======================
       //answByte = zookeeper.getData(path, answStat);
		//System.out.println("Antwort: " + new String(answByte) + " - Version: " + answStat.version);
*/
		/*
		//=======================read test                      =================================
		int n = 100;
		MWStat answStat = new MWStat();
		long start = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			zookeeper.getData("/huang", answStat);
		}
		long end = System.currentTimeMillis();
		
		System.out.println("Read Average time: " + (end-start)/n + "ms");

		//=======================write test                      =================================
		int n1 = 100;
		MWStat answStat1 = new MWStat();
		zookeeper.getData("/huang", answStat1);
		//System.out.println("first         "+answStat1.version);
		long start1 = System.currentTimeMillis();
		for (int i = 0; i < n1; i++) {
			//zookeeper.getData("/huang", answStat1);
			//System.out.println("second1  "+answStat1.version);
			answStat1 = zookeeper.setData("/huang", new byte[] {1},answStat1.version);
			
			//System.out.println("second2  "+answStat1.version);
		}
		long end1 = System.currentTimeMillis();
		
		System.out.println("Write Average time: " + (end1-start1)/n + "ms");
		zookeeper.closeSocket();
		*/
		
		for (int i = 0; i < 100; i++) {
			try {
			MWStat answStat2 = new MWStat();
			zookeeper.getData("/huang", answStat2);
			answStat2 = zookeeper.setData("/huang", new byte[] {1}, answStat2.version);
			System.out.println("New Version: " + answStat2.version);
			} catch (MWZooKeeperException e) {
				System.err.println(e.getMessage());
			}
		}
		
	}
}