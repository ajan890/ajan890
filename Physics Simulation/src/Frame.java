import javax.swing.JFrame;

public class Frame extends JFrame{
	private static final long serialVersionUID = -6540920119369591664L;
	private Canvas canvas;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean grapple;
	
	public Frame() {
		super();
		this.setBounds(1920, 100, 1920, 1080);
		canvas = new Canvas();
		this.getContentPane().add(canvas);
		
        
	}

	public Canvas getCanvas() {
		return canvas;
	}
}
