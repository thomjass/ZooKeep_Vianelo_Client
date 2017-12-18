import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import zookeeperProject.main.ZooContact;

public class TestSpeed {
	ArrayList<ZooContact> connections = new ArrayList<ZooContact>();
	
	@Before
	public void setUp() throws Exception {
		
	    try {
	    	//Here the IP adress of the master
	    	for(int i=0;i<5;i++) {
	    		ZooContact conn = new ZooContact();
				conn.connect("localhost:2181");
				TimeUnit.SECONDS.sleep(3);
				conn.try_to_enrol(String.valueOf(i));
				conn.goOnline(String.valueOf(i));
				connections.add(conn);
			}
	    	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	    
	}

	@After
	public void tearDown() throws Exception {
		
		
		/*for (int count=0;count<100;count++) {
			ZooContact.zoo.delete("/online/"+count, -1);
			
		}
		conn.close();*/

	}
	
	
	@Test
	public void testDelay100message1user() {

		LocalTime now = LocalTime.now();
		System.out.println("The sending is beginning : "+new Timestamp(System.currentTimeMillis()));
		for (int k=0;k<5;k++) {
			
			connections.get(k).sendMessage(String.valueOf(k), "1", "test");			
		}
		
		while(true) {
			
		}
	}
	

}
