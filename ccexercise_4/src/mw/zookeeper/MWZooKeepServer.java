package mw.zookeeper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.zookeeper.zab.MultiZab;
import org.apache.zookeeper.zab.SingleZab;
import org.apache.zookeeper.zab.Zab;
import org.apache.zookeeper.zab.ZabCallback;
import org.apache.zookeeper.zab.ZabStatus;
import org.apache.zookeeper.zab.ZabTxnCookie;

public class MWZooKeepServer implements ZabCallback {
	static int port;
	private MWDataTree tree;

	public Map<String, MWObjectConnection> clients;

	public MWZooKeepServer(int port) {
		tree = new MWDataTree();
		this.port = port;
		clients = new HashMap<String, MWObjectConnection>();
		//this.zabNode = new MultiZab(this, zabProperties);
		/*
		try {
			this.zabNode = new SingleZab(zabProperties, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	@SuppressWarnings("resource")
	public void startServer(Zab zabNode) {
		System.out.println("server run");
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(this.port);
			//zabNode.startup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Socket clientSocket = null;
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				// String clientName =
				// clientSocket.getInetAddress().getCanonicalHostName();
				// System.out.println(clientName);
				MWObjectConnection connection = new MWObjectConnection(
						clientSocket);

				new MWRequestWorker(connection, zabNode, this.tree);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public class MWRequestWorker extends Thread {
		private MWObjectConnection connection;
		private Zab zabNode;
		private MWDataTree tree;

		public MWRequestWorker(MWObjectConnection connection, Zab zabNode,
				MWDataTree tree) {
			this.connection = connection;
			this.tree = tree;
			this.zabNode = zabNode;
			start();
		}

		public byte[] serialize(Serializable obj) throws Exception {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
			baos.close();
			return baos.toByteArray();
		}

		public void run() {

			while (true) {
				MWMessage message;
				Serializable retVal = null;
				try {
					// System.out.println("recive message");
					message = (MWMessage) connection.receiveObject();
				    if(message==null){
						this.stop();
					}
					if (message != null) {
						clients.put(message.getmsgOwner(), connection);

						if (message.getCommand().equalsIgnoreCase("create")) {

							try {
								zabNode.propose(serialize(message));
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else if (message.getCommand().equalsIgnoreCase(
								"delete")) {

							try {
								zabNode.propose(serialize(message));
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else if (message.getCommand().equalsIgnoreCase(
								"setData")) {

							try {
								zabNode.propose(serialize(message));
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else if (message.getCommand().equalsIgnoreCase(
								"getData")) {
							byte[] data = null;
							MWStat stat = new MWStat();

							try {
								data = tree.getData(message.getPath(), stat);

							} catch (MWZooKeeperException e) {
								// message.exception = e;
								System.err.println(e.getMessage());
							}

							MWMessage messageOut = new MWMessage(
									"getDataReturnMessage", null, data, stat,
									message.getmsgOwner());

							try {
								connection.sendObject(messageOut);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
					
					System.err.println("client is close");
				}
			}
		}

	}

	public Serializable deserialize(byte[] arr) throws Exception {

		ByteArrayInputStream bais = new ByteArrayInputStream(arr);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Serializable ret = (Serializable) ois.readObject();
		bais.close();
		ois.close();

		return ret;
	}

	@Override
	public void deliver(ZabTxnCookie arg0, byte[] arg1) {
		// TODO Auto-generated method stub

		MWMessage msg = null;
		MWMessage replyMessage = null;
		try {
			msg = (MWMessage) deserialize(arg1);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (msg.getCommand().equalsIgnoreCase("create")) {

			try {
				String path = tree.create(msg.getPath(), msg.getData(),
						System.currentTimeMillis());

				replyMessage = new MWMessage(null, path, null, null, null);

			} catch (MWZooKeeperException e) {
				System.err.println(e.getMessage());
			}
		} else if (msg.getCommand().equalsIgnoreCase("delete")) {

			try {
				tree.delete(msg.getPath(), msg.getState().version);
			} catch (MWZooKeeperException e) {
				System.err.println(e.getMessage());
			}
		} else if (msg.getCommand().equalsIgnoreCase("setData")) {

			MWStat stat = new MWStat();
			try {
				stat = tree
						.setData(msg.getPath(), msg.getData(), msg.getState()
								.getVersion(), msg.getState().getTimestamp());
				//System.err.println(stat.version);
			} catch (MWZooKeeperException e) {
				System.err.println(e.getMessage());
			}

			replyMessage = new MWMessage(null, null, null, stat, null);

		}
		/*
		 * else if (msg.getCommand().equalsIgnoreCase("kill")) {
		 * 
		 * tree.killSession(msg.outputStreamHash); if
		 * (outputstreams.containsKey(msg.outputStreamHash))
		 * outputstreams.remove(msg.outputStreamHash);
		 * 
		 * return;
		 * 
		 * } else { System.out.println("Sollte nicht vorkommen!"); }
		 */
		if (clients.containsKey(msg.getmsgOwner())) {
			try {
				clients.get(msg.getmsgOwner()).sendObject(replyMessage);
				//System.out.println(msg.getmsgOwner());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void deliverSync(ZabTxnCookie arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getState(OutputStream arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setState(InputStream arg0, ZabTxnCookie arg1)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void status(ZabStatus arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws Exception {
		Properties zabProperties1 = new Properties();
		zabProperties1.setProperty("myid",String.valueOf(1));
		zabProperties1.setProperty("peer1","192.168.0.108:7777");
		zabProperties1.setProperty("peer2","192.168.0.111:7778");
		zabProperties1.setProperty("peer3","192.168.0.111:7779");
		
		Properties zabProperties2 = new Properties();
		zabProperties2.setProperty("myid",String.valueOf(2));
		zabProperties2.setProperty("peer1","192.168.0.108:7777");
		zabProperties2.setProperty("peer2","192.168.0.111:7778");
		zabProperties2.setProperty("peer3","192.168.0.111:7779");
		
		Properties zabProperties3 = new Properties();
		zabProperties3.setProperty("myid",String.valueOf(3));
		zabProperties3.setProperty("peer1","192.168.0.108:7777");
		zabProperties3.setProperty("peer2","192.168.0.111:7778");
		zabProperties3.setProperty("peer3","192.168.0.111:7779");
		
		
		MWZooKeepServer server1 = new MWZooKeepServer(7782);
		Zab zabNode1 = new MultiZab(server1, zabProperties1);
		server1.startServer(zabNode1);


		
		
		
		/*
		MWZooKeepServer server2 = new MWZooKeepServer(7781);
		Zab zabNode2 = new MultiZab(server2, zabProperties2);
		try {
			zabNode2.startup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server2.startServer(zabNode2);
		*/
		/*
		MWZooKeepServer server3 = new MWZooKeepServer(7780);
		Zab zabNode3 = new MultiZab(server3, zabProperties3);
		try {
			zabNode3.startup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server3.startServer(zabNode3);
		*/
	}

}
