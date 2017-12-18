package zookeeperProject.main;

import org.apache.zookeeper.ZooKeeper;

import java.sql.Timestamp;
import java.util.ArrayList;


public class Client_test {
	private static ZooKeeper zk;

	public static LoginDialog log;

	public static void main(String[] args) {

		ArrayList<ZooContact> zooContacts = new ArrayList<ZooContact>();
		for(int i=0;i<100;i++){
			ZooContact conn = new ZooContact();
			try {
				zk = conn.connect("localhost");
			} catch (Exception e) {
				e.printStackTrace();
			}
			while(!zk.getState().isConnected()){

			}
			System.out.println(zk.getState());
			zooContacts.add(conn);
		}
		System.out.println("Beginning of the sending: " + (new Timestamp(System.currentTimeMillis())).toString());
		for(int j=0;j<100;j++){
			zooContacts.get(j).sendMessage(String.valueOf(j), "henri",String.valueOf(j));
		}
	
	}


}
