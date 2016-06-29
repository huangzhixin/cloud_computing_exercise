package mw.zookeeper;

import java.io.Serializable;

public class MWMessage implements Serializable {
	
	private static final long serialVersionUID = -6181292557718958696L;
	
	private String command;
    private String path;
    private byte[] data;
    private MWStat state;
    private String msgOwner;
    public MWMessage( String command,String path,byte[] data, MWStat state,String msgOwner){
 	     this.command=command; 
    	 this.path = path;
 	     this.data =data;
 	     this.state=state;
 	     this.msgOwner= msgOwner;
    }
    public String  getPath(){
 	   return path;
    }
	public String getCommand(){
	    return command;
	 }
	 public byte[] getData(){
	    return data;
	}
	public MWStat getState(){
		return state;
	}	
	public String  getmsgOwner(){
		return msgOwner;
	}	
}
