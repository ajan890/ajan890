import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ImageWindow extends JFrame {

	private static final long serialVersionUID = 2604044952471670358L;
	private int rotation; 	//rotation: 0 = Bottom, 1 = Top, 2 = Left, 3 = Right
	private boolean flip;
	private int width;
	private int height;
	private int x = 0;
	private int y = 0;
	
	
	//rotation: 0 = Bottom, 1 = Top, 2 = Left, 3 = Right
	public ImageWindow(int rotation, boolean flip, BufferedImage image) throws IOException {
		super();		
		
		this.setRotation(rotation);
		this.setFlip(flip);
		
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		
		
		
		if (flip) {
			AffineTransform f = AffineTransform.getScaleInstance(-1,  1);
			f.translate(-image.getWidth(), 0);
			AffineTransformOp op = new AffineTransformOp(f, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			image = op.filter(image,  null);
		}		
		
		switch(rotation) {
		case 0: break;			
		case 1: 
		case 2:
		case 3:
		}
		
		
		ImagePanel panel = new ImagePanel(image);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		panel.setBounds(0, 0, width, height);
		panel.repaint();
		panel.setVisible(true);
		

		
		//x and y are some random coordinates
		setBounds(x, y, width, height);			
	}

	public void move(int moveX, int moveY) {
		setBounds(x + moveX, y + moveY, width, height);
	}
	
	
	
	
	
	
//getters and setters
	public int getRotation() { return rotation;	}
	public void setRotation(int rotation) { this.rotation = rotation; }
	public boolean isFlip() { return flip; }
	public void setFlip(boolean flip) { this.flip = flip; }
	
	
}
