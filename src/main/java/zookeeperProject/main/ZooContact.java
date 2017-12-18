package zookeeperProject.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZooContact {
	public static ZooKeeper zoo;
	   final CountDownLatch connectedSignal = new CountDownLatch(1);

	   // Method to connect zookeeper ensemble.
	   public ZooKeeper connect(String host) throws IOException,InterruptedException {	
	      zoo = new ZooKeeper(host,5000,new WatcherConn());
	      return zoo;
	   }
		
	   public static void create(String path, byte[] data) throws 
	      KeeperException,InterruptedException {
	      zoo.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
	      CreateMode.PERSISTENT);
		}
	

	   // Method to disconnect from zookeeper server
	   public void close() throws InterruptedException {
	      zoo.close();
	   }
	   
	   Watcher watcher = new WatcherWorker();
	   
	   public void try_to_enrol(String newID) {
		   try {
			   Stat exist = ZooContact.zoo.exists("/request/enroll/"+newID, false);
			   if (exist == null) {
				   zoo.create("/request/enroll/"+newID, "-1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				   zoo.getData("/request/enroll/"+newID, new WatcherWorker(), null);
			   }
			   else{
				   JOptionPane.showMessageDialog(null, "Server is not answering, please a few moments and try again", "Attention", JOptionPane.WARNING_MESSAGE);
			   }
		} catch (Exception e) {
			e.printStackTrace();
		}
	   }
	   
	   public void quit(String ID) {
		   try {
			   Stat exist2 = ZooContact.zoo.exists("/request/quit/"+LoginDialog.ID.getText(), false);
			   if (exist2 == null) {
				   zoo.create("/request/quit/"+ID, "-1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				   zoo.getData("/request/quit/"+ID, new WatcherWorker(),null);
			   }
			   else{
				   JOptionPane.showMessageDialog(null, "Server is not answering, please a few moments and try again", "Attention", JOptionPane.WARNING_MESSAGE);
			   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	   }
	   
	   public void goOnline(String ID) {
	        // TODO Auto-generated method stub
	        try {	
	        		System.out.println("Client: ZooContact: Cr�ation de "+"/online/"+ID);
	                zoo.create("/online/"+ID, "-1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	        		System.out.println("Client: ZooContact: creation watcher qui peut trigger NodeDeleted or NodeDataChanged sur "+"/online/"+ID);
	                zoo.getData("/online/"+ID, new WatcherWorker(), null);
	                System.out.println("Client: ZooContact: creation watcher qui peut trigger NodeDeleted or NodeChildrenChanged sur "+"/queue/");
	                zoo.getChildren("/queue", new WatcherWorker());
	               
	        }catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	   
	    public void goOffline(String ID) {
	        // TODO Auto-generated method stub
	        try {
	                zoo.delete("/online/"+ID, -1);
	                zoo.close();
	        }catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	   
	    public void sendMessage(String IDSender, String IDReceiver, String text) {
	        try {
	            zoo.create("/queue/"+IDReceiver+"/msg", (IDSender+" : "+ text).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
	        } catch (KeeperException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	   
	    public List<String> receiveMessages(String ID) {
	        try {
	            return zoo.getChildren("/queue/"+ID, false);
	        } catch (KeeperException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return null;
	    }
	   
	    public String readMessage(String ID, String MsgID) throws UnsupportedEncodingException {
	        byte[] cryptmsg = null;
	        try {
	            cryptmsg = zoo.getData("/queue/"+ID+"/"+MsgID, new WatcherWorker(), null);
	            zoo.delete("/queue/"+ID+MsgID, -1);
	        } catch (KeeperException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return new String(cryptmsg, "UTF-8");
	    }
}
