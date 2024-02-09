import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

class Driver {
	
    public static void main(String[] args) {
//    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        System.out.println(screenSize);
//        //frame.setBounds((int)screenSize.getWidth() - 128, (int)screenSize.getHeight(), 128, 128);
//    	String imgPath = "C:\\Users\\adnja\\Pictures\\Screenshots\\128-128img.png";
//    	
//    	BufferedImage image;
//		try {
//			image = ImageIO.read(new File(imgPath));
//			int width = image.getWidth();
//			int height = image.getHeight();
//	    	
//			ArrayList<int[]> nodes = new ArrayList<int[]>();
//	    	
//	    	nodes.add(new int[] {(int) screenSize.getWidth() - width, (int) screenSize.getHeight()});
//	    	nodes.add(new int[] {(int) screenSize.getWidth() - width, (int) screenSize.getHeight() - height});
//	    	nodes.add(new int[] {(int) screenSize.getWidth() - width, (int) screenSize.getHeight()});
//	    	
//	    	MovePanel movePanel = new MovePanel(image, 10, nodes);
//		} catch (IOException e) {
//			System.err.println("Image does not exist at path.");
//		}

    	JFrame imageWindow = new JFrame();
    	imageWindow.setVisible(true);
    	MovementController mv1 = new MovementController(imageWindow, PointGenerator.Bezier(new int[]{0,0}, new int[]{200,200}, new int[]{200,400}, new int[]{400,400}, 100));
    	MovementController mv2 = new MovementController(imageWindow, PointGenerator.Lerp(new int[]{0,0}, new int[]{200,0}, 2));
        mv1.run();
        mv2.run();
    }
}