import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;

public class Clicker {
	//instance
	private Robot robot;
	private int count = 0;
	private Timer timer;
	private static BasicUserInterface basicUserInterface;
	
	//construct
	public Clicker(int delay, int button, int maxCount, String modifier) {
		timer = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					robot = new Robot();
				} catch (AWTException f) {
					System.out.println("Error creating Robot in Clicker");
				}
				//do clicking
				if (modifier.equals("Hold")) {//hold click
					robot.mousePress(InputEvent.getMaskForButton(button));
				} else {//single click
				robot.mousePress(InputEvent.getMaskForButton(button));
				robot.mouseRelease(InputEvent.getMaskForButton(button));
					if (modifier.equals("Double")) {//double click
						robot.mousePress(InputEvent.getMaskForButton(button));
						robot.mouseRelease(InputEvent.getMaskForButton(button));
					}
				}
				//check count
				if (count >= maxCount) {
					((Timer)e.getSource()).stop();
					basicUserInterface.getStopButton().doClick();
				}
				count++;
			}//end actionPerformed
		}); //end ActionListener

	}//end constructor w/o location
	
	public Clicker(int delay, int button, int locX, int locY, int maxCount, String modifier) {
		timer = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					robot = new Robot();
				} catch (AWTException f) {
					System.out.println("Error creating Robot in Clicker");
				}
				robot.mouseMove(locX, locY);
				//do clicking
				if (modifier.equals("Hold")) {//hold click
					robot.mousePress(InputEvent.getMaskForButton(button));
				} else {//single click
				robot.mousePress(InputEvent.getMaskForButton(button));
				robot.mouseRelease(InputEvent.getMaskForButton(button));
					if (modifier.equals("Double")) {//double click
						robot.mousePress(InputEvent.getMaskForButton(button));
						robot.mouseRelease(InputEvent.getMaskForButton(button));
					}
				}
				//check count
				if (count >= maxCount) {
					((Timer)e.getSource()).stop();
					basicUserInterface.getStopButton().doClick();
				}
				count++;
			}
		});
	}//end constructor with location
	
	//methods to get screen dimensions
	public static int getScreenWidth() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return (int)(screen.getWidth());
	}
	
	public static int getScreenHeight() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return (int)(screen.getHeight());
	}
	
	/**start program here.
	 * Initializes BasicUserInterface; acts as driver, as well as clicker.
	 * 
	 */
	public static void main(String[] args) {
		basicUserInterface = new BasicUserInterface(getScreenWidth(), getScreenHeight());
		basicUserInterface.setVisible(true);	
	}
	
	public void start() {//starts the clicker
		timer.start();
	}
	public void stop() {//stops the clicker immediately, when called
		timer.stop();
	}
}//end class
	