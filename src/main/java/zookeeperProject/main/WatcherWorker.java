package zookeeperProject.main;

import java.awt.BorderLayout;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;


public class WatcherWorker implements Watcher {

	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		if (event.getType() == EventType.NodeCreated) {
			System.out.println("Client: " + event.getPath()+" NodeCreated triggered");
			
		}
		else if (event.getType() == EventType.NodeDeleted) {
			System.out.println("Client: " +event.getPath()+" NodeDeleted triggered");
		}
		else if (event.getType() == EventType.NodeChildrenChanged) {
			System.out.println("Client: " + event.getPath()+" NodeChildrenChanged triggered");
			if(event.getPath().contains("/queue/")){
				List<String> listOfMessage = null;
				try {
					listOfMessage = ZooContact.zoo.getChildren(event.getPath(), new WatcherWorker());
					Collections.sort(listOfMessage,new StringComparator());
				} catch (KeeperException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			     //for (String str:listOfMessage){
			    	  byte[] ByteContent = null;
					try {
						for(int i =0 ;i<listOfMessage.size();i++) {
							ByteContent = ZooContact.zoo.getData(event.getPath()+"/"+listOfMessage.get(i), false, null);
							String content = new String(ByteContent,"UTF-8");
							ReceiveInterface.message_receive.append("   ("+new Timestamp(System.currentTimeMillis()) +") " + content+"\n");
				    	
				    	//ZooContact.zoo.delete("/queue/"+LoginDialog.ID.getText()+"/"+listOfMessage.get(0), -1)
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 				
			}
			if(event.getPath().equals("/queue")){
				List<String> listOfUsersOnline = null;
				try {
					listOfUsersOnline = ZooContact.zoo.getChildren("/queue", new WatcherWorker());
				} catch (KeeperException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				ReceiveInterface.combo.removeAllItems();
			      for (String str:listOfUsersOnline){
			    	  if (!str.equals( LoginDialog.ID.getText())) {
			    		  ReceiveInterface.combo.addItem(str);
			    	  }
			      }
			}
		}
		else if (event.getType() == EventType.NodeDataChanged) {
			System.out.println("Client: " + event.getPath() +" NodeDataChanged triggered.");
			if(event.getPath().contains("/request/enroll/")) {
				
				try {
					byte[] bdata = ZooContact.zoo.getData(event.getPath(), new WatcherWorker(),null);
					String data = new String(bdata, "UTF-8");
					if(data.equals("1")) {
						ZooContact.zoo.delete(event.getPath(), -1);
						System.out.println("You've been registered successfully !");
						JOptionPane.showMessageDialog(null, "You've been registered successfully !", "Information", JOptionPane.INFORMATION_MESSAGE);
				
					}else if(data.equals("2")) {
						ZooContact.zoo.delete(event.getPath(), -1);
						JOptionPane.showMessageDialog(null, "You are already registered !", "Attention", JOptionPane.WARNING_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(null, "An error occured during regisration !", "Erreur", JOptionPane.ERROR_MESSAGE);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(event.getPath().contains("/request/quit")) {
				try {
					byte[] bdata = ZooContact.zoo.getData("/request/quit/"+LoginDialog.ID.getText(), new WatcherWorker(),null);
					String data = new String(bdata, "UTF-8");
					if(data.equals("1")) {
						ZooContact.zoo.delete("/request/quit/"+LoginDialog.ID.getText(), -1);
						System.out.println("You've been registered successfully !");
						JOptionPane.showMessageDialog(null, "Your account has been removed successfully !", "Information", JOptionPane.INFORMATION_MESSAGE);
					}else if(data.equals("2")) {
						ZooContact.zoo.delete("/request/quit/"+LoginDialog.ID.getText(), -1);
						JOptionPane.showMessageDialog(null, "You are not registered !", "Attention", JOptionPane.WARNING_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(null, "An error occured during quiting !", "Erreur", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			else if(event.getPath().contains("/online")) {
				try {
					byte[] bdata = ZooContact.zoo.getData(event.getPath(), new WatcherWorker(),null);
					String data = new String(bdata, "UTF-8");
					if(data.equals("1")) {
						//JOptionPane.showMessageDialog(null, "Your are online !", "Information", JOptionPane.INFORMATION_MESSAGE);
						try {
							List<String> listOfMessage = null;
							try {
								listOfMessage = ZooContact.zoo.getChildren("/queue/"+event.getPath().split("/")[2],false);
								Collections.sort(listOfMessage,new StringComparator());
							} catch (KeeperException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							} catch (InterruptedException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						     //for (String str:listOfMessage){
						    	  byte[] ByteContent = null;
								try {
									for(String str:listOfMessage) {
										ByteContent = ZooContact.zoo.getData("/queue/"+event.getPath().split("/")[2]+"/"+str, false, null);
										String content = new String(ByteContent,"UTF-8");
								    	System.out.println(content);
								    	ReceiveInterface.message_receive.append("   " + content+"\n");
									}
							    	//ZooContact.zoo.delete("/queue/"+LoginDialog.ID.getText()+"/"+listOfMessage.get(0), -1);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 				
							ZooContact.zoo.getChildren("/queue/"+event.getPath().split("/")[2], new WatcherWorker());
						} catch (KeeperException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Client.log.setVisible(false);
						ReceiveInterface messInt =new ReceiveInterface(LoginDialog.zooContact);
					}else if(data.equals("2")) {
						JOptionPane.showMessageDialog(null, "You are not registered !", "Attention", JOptionPane.WARNING_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(null, "An error occured during quiting !", "Erreur", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
	 
			
		}
		else {
			System.out.println(event.getPath());
		}
	}
}
class StringComparator implements Comparator<String> {
    public int compare(String a, String b) {
    	if(Integer.valueOf(a.substring(3,a.length()))<Integer.valueOf(b.substring(3,a.length()))) {
    		return -1;
    	}else if(Integer.valueOf(a.substring(3,a.length()))==Integer.valueOf(b.substring(3,a.length()))) {
    		return 0;
    	}else {
    		return 1;
    	}
        
    }
}
