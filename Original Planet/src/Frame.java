import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;


public class Frame extends JFrame {
/**
	 * @author Owen Kelly
	 */
	private static final long serialVersionUID = 1L;
public Frame(){
	super("Cube");
	 Universe u = new Universe(); 
	    Container c = getContentPane();
	    c.setLayout( new BorderLayout() );    
	    c.add(u, BorderLayout.CENTER);

	    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    pack();
	    setResizable(true);    
	    setVisible(true);
	    System.out.println("Meg used="+(Runtime.getRuntime().totalMemory()-
	    		  Runtime.getRuntime().freeMemory())/(1000*1000)+"M");
	  } 
	  public static void main(String[] args)
	  { new Frame();
	  
	  }

}

