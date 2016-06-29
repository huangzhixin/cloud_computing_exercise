package mw.zookeeper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import com.sun.org.glassfish.external.statistics.annotations.Reset;

public class MWObjectConnection {

	ObjectOutputStream out;
	ObjectInputStream in;
 	public MWObjectConnection(Socket socket) {
 		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	public void sendObject(Serializable object) throws IOException {
             out.writeObject(object);
             //out.flush();
             out.reset();
	}
	public Serializable  receiveObject() throws ClassNotFoundException{
		  Serializable objekt=null;
            try {
            	 objekt=(Serializable) in.readObject();
            	 in.reset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//System.err.println("object connection client is close");
			}
            return objekt;
	}

}
