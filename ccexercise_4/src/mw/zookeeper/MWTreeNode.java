package mw.zookeeper;

import java.util.HashMap;

public class MWTreeNode {
	private byte[] data;
	private MWStat state;
	private HashMap<String, MWTreeNode> children;
	public MWTreeNode(byte[] data,long time) {
		this.data = data;
		this.state = new MWStat(0,time);
		this.children = new HashMap<String, MWTreeNode>();
	}
       
}
