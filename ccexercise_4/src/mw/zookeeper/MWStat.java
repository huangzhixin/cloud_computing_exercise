package mw.zookeeper;

import java.io.Serializable;

public class MWStat implements Serializable{
	
	private static final long serialVersionUID = -6181292557718958696L;
	
	  long timestamp= System.currentTimeMillis();
	  int version = -1;
	  public MWStat(){
		  this.version=0;
      }
      public MWStat(int version){
    	  this.version=version;
      }
      public MWStat(int version, long timestamp){
    	  this.version=version;
    	  this.timestamp=timestamp;
      }
      
      public void increaseVersion()
		{
			this.version += 1;
		}
      
      public void updateTimestamp()
		{
			this.timestamp= System.currentTimeMillis();
		}
      
      public int getVersion(){
    	  return this.version;
      }
      
      public long getTimestamp(){
    	  return this.timestamp;
      }
      
      
      
}
