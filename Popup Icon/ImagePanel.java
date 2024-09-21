import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	private static final long serialVersionUID = 2730631373587824211L;
	private BufferedImage image;
	
	public ImagePanel(BufferedImage img) {
		super();
		image = img;
		setOpaque(false);
	}
		
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	
	}
}
