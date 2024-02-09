import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Timer;

public class MovePanel {
	private ImageWindow frame;
	private Timer timer;
	private int[][] nodes;
	private int speed;
	private MoveList moveList;
	
	
	//speed is how many pixels it moves per time interval (10ms).  
	//nodes is an array of coordinates (int [2]) that will determine points on the screen the window moves through. 
	public MovePanel(BufferedImage image, int speed, ArrayList<int[]> nodes) {
		
	    //TO-DO: Create folder in C:/ to store images, read file from dir
	    
	    moveList = new MoveList();
	    
		try {
			frame = new ImageWindow(0, false, image);
	        frame.setVisible(true);
	        	        			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setMoves(String moves) {
		
	}
	
	public void move(int x, int y) {
		frame.setBounds(frame.getX() + x, frame.getY() + y, frame.getWidth(), frame.getHeight());
		return;
	}
	
}
