package byow;
import java.util.ArrayList;
/**
 * A room is rectangular, always has four corners.  Corner positions are generated from coordinates, width, and height.
 */
public class Room {

	private ArrayList<Corner> corners;
	private int id;
	private int width;
	private int height;
	private ArrayList<Integer> connections;
	
	public Room(int id, int x, int y, int width, int height) {

		this.id = id;
		this.width = width;
		this.height = height;
		corners = new ArrayList<Corner>();
		corners.add(new Corner(id, 0, x, y));
		corners.add(new Corner(id, 1, x + width, y));
		corners.add(new Corner(id, 2, x + width, y + height));
		corners.add(new Corner(id, 3, x, y + height));
		connections = new ArrayList<Integer>();
	}//end constructor
	
	public Room(Room a, Room b) {
		corners = new ArrayList<Corner>();
		corners.addAll(a.getCorners());
		corners.addAll(b.getCorners());		
	}//end constructor

	public int getId() {
		return id;
	}//end getId
	
	public int getWidth() {
		return width;
	}//end getWidth
	
	public int getHeight() {
		return height;
	}//end getHeight
	
	public Corner getCorner(int i) {
		return corners.get(i);
	}//end getCorner
	
	public ArrayList<Corner> getCorners() {
		return corners;
	}//end getCorners
	
	public int numCorners() {
		return corners.size();
	}//end numCorners
	
	public void addConnection(Integer connection) {
		connections.add(connection);
	}//end addConnection
	
	public ArrayList<Integer> getConnections() {
		return connections;
	}//end getConnections
}//end class