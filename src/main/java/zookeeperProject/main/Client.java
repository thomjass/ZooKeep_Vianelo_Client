package zookeeperProject.main;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


public class Client {
	private static ZooKeeper zk;
	public static ZooContact conn = new ZooContact();
	public static LoginDialog log;

	public static void main(String[] args) {

		try {
			zk = conn.connect("localhost");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			WatcherConn.connectionLatch.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(zk.getState());
		
		log = new LoginDialog(conn);
	    log.setVisible(true);


	    
	
	}


}
