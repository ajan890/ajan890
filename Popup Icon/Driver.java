import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

class Driver {
    public static void main(String[] args) {
    	String imgPath = "";
        ImageWindow frame;
		try {
			frame = new ImageWindow(0, false, imgPath);
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        System.out.println(screenSize);
	        frame.setVisible(true);
	        
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}