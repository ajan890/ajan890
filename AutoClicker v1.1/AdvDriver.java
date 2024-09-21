import java.awt.Dimension;
import java.awt.Toolkit;

public class AdvDriver {
	public static int getScreenWidth() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return (int)(screen.getWidth());
	}
	
	public static int getScreenHeight() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return (int)(screen.getHeight());
	}
	
	public static void main(String[] args) {
		
	}
}
