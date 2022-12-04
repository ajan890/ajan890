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

	//rotation: 0 = Bottom, 1 = Top, 2 = Left, 3 = Right
	public ImageWindow(int rotation, boolean flip, String imgPath) throws IOException {
		super();		
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		
		BufferedImage image = ImageIO.read(new File(imgPath));
		int width = image.getWidth();
		int height = image.getHeight();
		
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
		int x = 0, y = 0;
		
		
		setBounds(x, y, width, height);	
		
	}
	
	
}
