import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

public class MovePanel {
	private ImageWindow frame;
	private Timer timer;
	private int[][] nodes;
	private int speed;
	
	//speed is how many pixels it moves per time interval (10ms).  
	//nodes is an array of coordinates (int [2]) that will determine points on the screen the window moves through. 
	public MovePanel(String imgPath, int speed, int[][] nodes) {
		
	    //TO-DO: Create folder in C:/ to store images, read file from dir
	    
	    
		try {
			frame = new ImageWindow(0, false, imgPath);
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        System.out.println(screenSize);
	        frame.setBounds((int)screenSize.getWidth() - 128, (int)screenSize.getHeight(), 128, 128);
	        frame.setVisible(true);
	        timer = new Timer(10, new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		
	        	}
	        });
	        
	        
	        frame.setBounds((int)screenSize.getWidth() - 128, (int)screenSize.getHeight() - 128, 128, 128);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
