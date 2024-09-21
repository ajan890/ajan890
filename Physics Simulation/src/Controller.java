import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Controller {
	private Frame frame;
	private Canvas canvas;
	private Clock clock;
	private Player player;
	
    public Controller(Frame frame){
        this.frame = frame;
        this.canvas = frame.getCanvas();
        this.clock = new Clock(1000, canvas);
        clock.start();
        this.player = new Player();
        this.frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					//jump
					break;
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					//move left
					break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					//??
					break;
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					//move right
					break;
				case KeyEvent.VK_SPACE:
					//grapple
					break;
				default: break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
    }
        
	
}
