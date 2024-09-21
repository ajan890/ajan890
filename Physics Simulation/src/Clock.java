
public class Clock extends Thread{
	private int interval;
	private Canvas canvas;
	private boolean running;
	
	public Clock(int interval, Canvas canvas) {
		this.interval = interval;
		this.canvas = canvas;
		this.running = false;
	}
	
	public void run() {
		running = true;
		while(running) {
			canvas.repaint();
			System.out.println("repaint called");
			try {
				Clock.sleep(interval);
			} catch (InterruptedException e) {
				System.err.println("Interrupted Exception: " + e);
			}
		}
	}
	
	
	
}
