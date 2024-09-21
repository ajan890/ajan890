package byow;
/**
 * 
 * A tile on the board.
 * Each tile is 16x16 pixels.
 *
 */
public class Tile {
	private int x;
	private int y;
	private int type; //0 = blank, 1 = wall, 2 = floor
	
	public Tile(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}//end constructor
	
	public int getX() {
		return x;
	}//end getX
	
	public int getY() {
		return y;
	}//end getY
	
	public int getType() {
		return type;
	}//end getType
	
	public void setType(int type) {
		this.type = type;
	}//end setType
}//end class