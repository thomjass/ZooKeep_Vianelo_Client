package zookeeperProject.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.*;

import org.apache.zookeeper.KeeperException;

public class ReceiveInterface {
	public static JTextField message;
	public static JComboBox combo1 = new JComboBox();
	public static JComboBox combo = new JComboBox();
	public static JPanel onglet1 = new JPanel();
	public static JTextArea message_receive = new JTextArea();
	public static JScrollPane scroller = new JScrollPane(message_receive, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private ZooContact connexion;

	public ReceiveInterface(final ZooContact connexion) {
	    this.connexion = connexion;
		
		JFrame f = new JFrame(LoginDialog.ID.getText()+" - Messagerie");
	    f.setSize(1000, 600);
	    JPanel pannel = new JPanel();
	    
	    JButton read = new JButton("Remove all");
	    read.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  List<String> listOfMessageToSuppress;
				try {
					listOfMessageToSuppress = connexion.zoo.getChildren("/queue/"+LoginDialog.ID.getText(), false);
					 for(String str:listOfMessageToSuppress) { 
				    	  connexion.zoo.delete("/queue/"+LoginDialog.ID.getText()+"/"+str, -1);
				   }
				   message_receive.setText(null);
				} catch (KeeperException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		      }
	      });
	    
	    JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);
	    JScrollPane scroller = new JScrollPane(message_receive);
	    scroller.setPreferredSize(new Dimension(800, 400));
	    onglet1.setPreferredSize(new Dimension(800, 400));
	    onglet1.add(read, BorderLayout.NORTH);
	    onglet1.add(scroller, BorderLayout.WEST);
	    onglets.addTab("Receive", onglet1);
	    
	    
	    
	    
	      JPanel onglet2 = new JPanel();
	     
	      JPanel pan_choice = new JPanel(new GridLayout(2,1));
	      JPanel pan_label = new JPanel(new GridLayout(2,1));
	      JPanel pan_textField = new JPanel(new GridLayout(2,1));
	      JPanel pan_button = new JPanel(new GridLayout(1,2));
	      onglet2.add(pan_choice, BorderLayout.NORTH);
	      onglet2.add(pan_label, BorderLayout.WEST);
	      onglet2.add(pan_textField, BorderLayout.CENTER);
	      onglet2.add(pan_button, BorderLayout.SOUTH);
	      pan_label.add(new JLabel(" Message: " ));
	   
	      //--------------------------------------------------------------//

	      //Choix de l'action
	      
	      
	      //Boutons ok et annuler
	      final JButton send = new JButton("Send" );
	      JButton annuler = new JButton("Annuler" );
	      send.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  connexion.sendMessage(LoginDialog.ID.getText(), String.valueOf(combo.getSelectedItem()), message.getText());
		      }
	      });
	 
	      annuler.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  try {
		    		  connexion.close();
		    		  System.exit(0);
				} catch (InterruptedException e1) {
					System.out.println(e1.getMessage());
				}
		      }
	      });
	     
	      //Champ de saisie du message
	      
	      message = new JTextField(20);
	      message.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
		      }
	      });
	    
	      // Ajout des elements aux panneaux
	      //-------------------------------------------------------//
	      
	      pan_choice.add(combo);
		  pan_textField.add(message);
		  pan_button.add(annuler);
		  pan_button.add(send);
		  
		  //--------------------------------------------------------//
	      
	      onglets.addTab("Send", onglet2);

	    onglets.setOpaque(true);
	    pannel.add(onglets);
	    f.getContentPane().add(pannel);
	    f.setVisible(true);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	  }
	
}
