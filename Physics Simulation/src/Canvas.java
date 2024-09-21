import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 4842244635506023853L;
	private Graphics g;
	private Graphics2D g2d;
	private int count = 0;
	
	public Canvas() {
		super();
		g = this.getGraphics();
		g2d = (Graphics2D) g;
	}
	
	synchronized public void paint() {
		System.out.println("Painting!" + count);
		count++;
	}
	
	public void repaint() {
		paint();
	}
	
}
