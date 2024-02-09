
public class Move {
	private int x;
	private int y;
	
	public Move(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	public Move() {
		this.setX(0);
		this.setY(0);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
