package byow;
/**
 * Represents a corner of a room
 * Contains the location of the corner on the grid, the room the corner belongs to, and the number of the corner on the room
 */
public class Corner {
	private int x;
	private int y;
	private int id;
	private int roomId;
	
	public Corner(int roomId, int id, int x, int y) {
		this.roomId = roomId;
		this.id = id;
		this.x = x;
		this.y = y;
	}//end constructor
	
	public int getRoomId() {
		return roomId;
	}//end getRoomId
	
	public int getId() {
		return id;
	}//end getId
	
	public int getX() {
		return x;
	}//end getX
	
	public int getY() {
		return y;
	}//end getY
	
	public String toString() {
		return "Corner " + id + " of room " + roomId + "(" + x + ", " + y + ")";
	}//end toString
}//end class