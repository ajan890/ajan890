package byow;
import java.util.ArrayList;
/**
 * Basically a group of rooms.
 * Complex() creates an empty group of rooms
 * add(room) adds a room to the complex
 * Corners of every room are automatically tracked, including their original room number
 * connectRooms() creates connections between room corners that connect rooms.
 */
public class Complex {
	
	private ArrayList<Room> rooms;
	private ArrayList<ArrayList<Corner>> corners;
	private ArrayList<Corner[]> connections;
	
	public Complex() {
		rooms = new ArrayList<Room>();
		corners = new ArrayList<ArrayList<Corner>>();
		connections = new ArrayList<Corner[]>();
	}//end constructor
	
	public void connectRooms() {
		ArrayList<Room> tempRooms = new ArrayList<Room>();
		for (int i = 0; i < rooms.size(); i++) {
			tempRooms.add(rooms.get(i));
		}
		
		//for every 2 rooms...
		for (int i = 0; i < tempRooms.size() - 1; i++) { //for each room
			double minDistance = Integer.MAX_VALUE;
			int[] minIndices = new int[3]; //pos1 stores first room corner number, pos2 second room number, pos3 second room corner number
			for (int j = 0; j < tempRooms.get(i).numCorners(); j++) { //for each corner on said room
				for (int k = i + 1; k < tempRooms.size(); k++) { //for each other room
					for (int l = 0; l < tempRooms.get(k).numCorners(); l++) { //for each corner on other tempRooms
						double tempDist = findDistance(tempRooms.get(i).getCorner(j), tempRooms.get(k).getCorner(l));
						if (tempDist < minDistance) {
							minDistance = tempDist;
							minIndices[0] = j;
							minIndices[1] = k;
							minIndices[2] = l;
						} else if (tempDist == minDistance) { //prioritize straight corridors over bent ones, if distances are equal
							if (!((j == 0 && l == 2) || (j == 2 && l == 0) || (j == 1 && l == 3) || (j == 3 && l == 1)) && ((minIndices[0] == 0 && minIndices[2] == 2) || (minIndices[0] == 2 && minIndices[2] == 0) || (minIndices[0] == 1 && minIndices[2] == 3) || (minIndices[0] == 3 && minIndices[2] == 1))) {
								minIndices[0] = j;
								minIndices[1] = k;
								minIndices[2] = l;
							}
						}
					}
				}
			}
			//at this point, I should have the closest corner between room 1 and any other corner
			
			Corner corner1 = tempRooms.get(i).getCorner(minIndices[0]);
			Corner corner2 = tempRooms.get(minIndices[1]).getCorner(minIndices[2]);
			Corner[] tempCornerList = {corner1, corner2};
			connections.add(tempCornerList);			
			
			Room tempRoom = new Room(tempRooms.get(i), tempRooms.get(minIndices[1]));			
			tempRooms.add(0, tempRoom);
			tempRooms.remove(tempRooms.get(i + 1));
			tempRooms.remove(tempRooms.get(minIndices[1]));

			i--;
		}
	}//end connectRooms
	
	public double findDistance(Corner a, Corner b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}//end findDistance
	
	
	public void add(Room r) {
		rooms.add(r);
		corners.add(r.getCorners());
	}//end add
	
	public Room getRoom(int i) {
		return rooms.get(i);
	}//end getRoom
	
	public int numCorners() {
		int total = 0;
		for (int i = 0; i < corners.size(); i++) {
			total += corners.get(i).size();
		}
		return total;
	}//end numCorners
	
	public String toString() {
		String s = "Complex:\n";
		for (int i = 0; i < rooms.size(); i++) {
			s += "Room " + i + ": ";
			for (int j = 0; j < rooms.get(i).numCorners(); j++) {
				s += rooms.get(i).getCorner(j) + " ";
			}
			s += "\n";
		}
		return s;
				
	}//end toString
	
	public ArrayList<Integer> findAllConnectedRooms(int origRoom, int room) {
		ArrayList<Integer> allConnections = new ArrayList<Integer>();
		if (rooms.get(room).getConnections().size() == 1 && rooms.get(room).getConnections().get(0) == origRoom) {
			ArrayList<Integer> ints = new ArrayList<Integer>();
			ints.add(room);
			return ints;
		} else {
			ArrayList<Integer> connections = new ArrayList<Integer>();
			connections = rooms.get(room).getConnections();
			for (Integer connection : connections) {
				if (connection != origRoom) allConnections.addAll(findAllConnectedRooms(room, connection));	
			}
			allConnections.add(room);
			return allConnections;
		}
		
	}//end findAllConnectedRooms
	
	public void printConnections() {
		System.out.println("Connections: ");
		for (int i = 0; i < connections.size(); i++) {
			System.out.println("Connection " + i + ": " + connections.get(i)[0] + " to " + connections.get(i)[1]);
		}
	}//end printConnections
	
	public ArrayList<Corner[]> getConnections() {
		return connections;
	}//end getConnections
}//end class