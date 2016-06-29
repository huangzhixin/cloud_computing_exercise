package mw.zookeeper;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MWDataTree {
	protected class MWTreeNode {
		public MWStat stat;
		public byte[] data;
		public Map<String, MWTreeNode> children;

		public MWTreeNode(byte[] data, long time) {
			this.data = data;
			this.stat = new MWStat(0, time);
			this.children = new HashMap<String, MWTreeNode>();
		}
	}

	protected MWTreeNode root;

	public MWDataTree() {
		root = new MWTreeNode(new byte[0], 0);
	}

	public String create(String path, byte[] data, long time)
			throws MWZooKeeperException {
		
		StringTokenizer pathArray = new StringTokenizer(path, "/");
		MWTreeNode pointer = root;
		String str = null;
		while (pathArray.hasMoreTokens()) {
			str = pathArray.nextToken();
			if (pointer.children.containsKey(str)) {
				if (!pathArray.hasMoreTokens()) {
					throw new MWZooKeeperException("Ordner " + str
							+ " schon vorhanden (" + path + ")!");
				} else {
					pointer = pointer.children.get(str);
				}
			} else if (pathArray.hasMoreTokens()) {
				throw new MWZooKeeperException("Ueberordner " + str
						+ " existiert noch nicht (" + path + ")!");
			}
		}
		MWTreeNode node = new MWTreeNode(data, time);
		System.out.println("create new path : " +path +"  the data is"+ new String(data));
		pointer.children.put(str, node);
		return path;
	}

	public void delete(String path, int version) throws MWZooKeeperException {
		StringTokenizer pathArray = new StringTokenizer(path, "/");
		MWTreeNode pointer = root;
		String str = null;
		while (pathArray.hasMoreTokens()) {
			str = pathArray.nextToken();
			if (!pointer.children.containsKey(str)) {
				throw new MWZooKeeperException("Fehler bei delete(" + path
						+ ") bei Ordner " + str + "!");
			}
			if (pathArray.hasMoreTokens()) {
				pointer = pointer.children.get(str);
			}
		}
		System.out.println("delete version is :" +pointer.children.get(str).stat.version);
		if (pointer.children.get(str).stat.version == version) {
			pointer.children.remove(str);
		} else {
			throw new MWZooKeeperException(
					"Versionsnummern stimmen nicht ueberein!");
		}
	}

	public MWStat setData(String path, byte[] data, int version, long time)
			throws MWZooKeeperException {
		StringTokenizer pathArray = new StringTokenizer(path, "/");
		MWTreeNode pointer = root;
		String str = null;
		while (pathArray.hasMoreTokens()) {
			str = pathArray.nextToken();
			if (!pointer.children.containsKey(str)) {
				throw new MWZooKeeperException("Pfad " + path
						+ " existiert nicht (" + str + ")!");
			}
			pointer = pointer.children.get(str);
		}
		if (pointer.stat.version == version) {
			pointer.data = data;
			pointer.stat.version++;
			pointer.stat.timestamp = time;
		} else {
			throw new MWZooKeeperException(
					"Versionsnummern stimmen nicht ueberein!");
		}
		return pointer.stat;
	}

	public byte[] getData(String path, MWStat stat) throws MWZooKeeperException {
		//System.out.println("getdata!!!!!!!!!!! ");
		StringTokenizer pathArray = new StringTokenizer(path, "/");
		MWTreeNode pointer = root;
		String str = null;
		while (pathArray.hasMoreTokens()) {
			str = pathArray.nextToken();
			if (!pointer.children.containsKey(str)) {
				throw new MWZooKeeperException("Pfad " + path
						+ " existiert nicht (" + str + ")!");
			}
			pointer = pointer.children.get(str);
		}
		stat.version = pointer.stat.version;
		stat.timestamp = pointer.stat.timestamp;
		
		return pointer.data;
	}
}