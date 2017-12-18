package zookeeperProject.main;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.zookeeper.ZooKeeper;

public class LoginDialog extends JDialog{
	public static JTextField ID;
	private JComboBox combo = new JComboBox();
	public static ZooContact zooContact;
	public LoginDialog(ZooContact zooConnection) {
		init();
		this.zooContact = zooConnection;
   }
   public LoginDialog(Frame owner, ZooContact zooConnection) {
      super (owner);
      init();
      this.zooContact = zooConnection;
   }
   public LoginDialog(Dialog owner, ZooContact zooConnection) {
      super (owner);
      init();
      this.zooContact = zooConnection;
   }

   private void init() {
      setTitle("Enrol / Quit / Login" );
      this.setSize(800, 400);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.addWindowListener(new WindowAdapter(){
          public void windowClosing(WindowEvent e){
             try {
            	 zooContact.close();
			} catch (InterruptedException e1) {
				System.out.println(e1.getMessage());
			}
        }
});
      // Disposition des éléments
      // --------------------------------------------------//

      JPanel pan_principal = new JPanel(new BorderLayout());
      JPanel pan_choice = new JPanel(new GridLayout(2,1));
      JPanel pan_label = new JPanel(new GridLayout(2,1));
      JPanel pan_textField = new JPanel(new GridLayout(2,1));
      JPanel pan_button = new JPanel(new GridLayout(1,2));
      
      pan_principal.add(pan_choice, BorderLayout.NORTH);
      pan_principal.add(pan_label, BorderLayout.WEST);
      pan_principal.add(pan_textField, BorderLayout.CENTER);
      pan_principal.add(pan_button, BorderLayout.SOUTH);
      pan_label.add(new JLabel(" ID  " ));
      
      // Construction des éléments
      //--------------------------------------------------------------//

      //Choix de l'action
      combo.addItem("Enrol");
      combo.addItem("Quit");
      combo.addItem("Login");    
      
      class ChoiceListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
		    	
		    }  
	 }    
     
      combo.addActionListener(new ChoiceListener());
      
      //Boutons ok et annuler
      final JButton ok = new JButton("OK" );
      JButton annuler = new JButton("Annuler" );
      ok.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  if (combo.getSelectedItem().toString().equals("Enrol")){
		    		zooContact.try_to_enrol(ID.getText());
		    	}
		    	else if(combo.getSelectedItem().toString().equals("Quit")){
		    		zooContact.quit(ID.getText());
		    	}
		    	else if(combo.getSelectedItem().toString().equals("Login")){
		    		zooContact.goOnline(ID.getText());
		    	}
		    	else{
		    		System.out.println(combo.getSelectedItem().toString());
		    		System.exit(0);
		    	}
	    	  
	    	  
	      }
      });
 
      annuler.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  try {
	    		  zooContact.close();
	    		  System.exit(0);
			} catch (InterruptedException e1) {
				System.out.println(e1.getMessage());
			}
	      }
      });
     
      //Champ de saisie de l'id
      
      ID = new JTextField(20);
      ID.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
	      }
      });
    
      // Ajout des éléments aux panneaux
      //-------------------------------------------------------//
      
      pan_choice.add(combo);
	  pan_textField.add(ID);
	  pan_button.add(annuler);
	  pan_button.add(ok);
	  
	  //--------------------------------------------------------//
	 
	  
      this.setContentPane(pan_principal);
      this.pack();
   } 

}
