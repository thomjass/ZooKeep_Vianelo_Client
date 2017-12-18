package zookeeperProject.main;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


public class Client {
	private static ZooKeeper zk;
	public static ZooContact conn;
	public static LoginDialog log;

	public static void main(String[] args) {
		
		conn = new ZooContact();
	    try {
	    	//Here the IP adress of the master
			zk = conn.connect("localhost:2181");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		log = new LoginDialog(conn);
	    log.setVisible(true);
	    
	
	}

}
