package byow;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;


public class Board extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3920597802671133093L;
	private	Tile[][] board;
	private Random generator;
	private Complex complex;
	
	public Board(int rows, int cols, Random generator) {
		board = new Tile[rows][cols];
		this.generator = generator;
		complex = new Complex();
		generate();
		
		//PRINT OUT, TESTING
		System.out.println("Completed");
		System.out.println(complex);
		
		System.out.println();
		complex.printConnections();
		
		ArrayList<Corner[]> remaining = new ArrayList<Corner[]>();
		for (Corner[] connection : complex.getConnections()) {
			if (drawCorridors(connection) != null) {
				remaining.add(connection);
			}
		}
		
		//fill in all remaining corridors
				for (int i = 0; i < remaining.size(); i++) {
					System.out.println("Remaining: " + remaining.get(i));
					
					connectNeighbors(remaining.get(i));
				}
		fillBlackTiles();
	}//end constructor (MAIN)
	
	public void generate() {
		//fill every tile with wall
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new Tile(i, j, 0);
			}
		}
		int roomIds = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				int temp = (int)(generator.nextDouble() * 5); //CHANGE FREQUENCY OF ROOMS HERE (Lower numbers draw more rooms)
				if (temp == 1) {
					int tempWidth = (int)(generator.nextDouble() * 8) + 2;
					int tempHeight = (int)(generator.nextDouble() * 8) + 2;
					if (createRoom(i, j, tempWidth, tempHeight)) {
						Room room = new Room(roomIds, i, j, tempWidth, tempHeight);
						complex.add(room);
						roomIds++;
						break;
					}
				}
			}
		}
		
		//connect rooms
		complex.connectRooms();
		
	}//end generate
	
	public void paint(Graphics g) {
		drawBoard(g);
		
		/**
		 * Uncomment below to draw grid.
		 */
		//drawGrid(g);
	}//end paint (overwritten method)
	
	public boolean createRoom(int x, int y, int width, int height) {
		//check if room fits on map
		if (x + width > board[0].length - 1 || y + height > board.length - 1 || x == 0 || y == 0) {
			return false;
		} 
		
		//check if room overlaps other rooms
		for (int i = -1; i < width + 1; i++) {
			for (int j = -1; j < height + 1; j++) {
				if (board[x + i][y + j].getType() != 0) {
					return false;
				}
			}
		}
		
		//if not any of the above, draw room
		for (int i = -1; i < height + 1; i++) {
			for (int j = -1; j < width + 1; j++) {
				if (i == -1 || j == -1 || j == width || i == height) {
					board[x + j][y + i].setType(1);
				} else {
					board[x + j][y + i].setType(2);
				}
			}
		}
		return true;
	}//end drawRoom
	
	/**
	 * @param connection always has two elements, corners are always different rooms.
	 */
	public Corner[] drawCorridors(Corner[] connection) {
		Room a = complex.getRoom(connection[0].getRoomId());
		Room b = complex.getRoom(connection[1].getRoomId());
		int aId = connection[0].getId();
		int bId = connection[1].getId();
		
		Corner[] remaining = null;
		
		if (aId == 0 && bId == 1 || aId == 1 && bId == 0) { //STRAIGHT CORRIDORS
			//compare L and R from top
			ArrayList<Integer> overlap = new ArrayList<Integer>();
			for (int i = connection[0].getY(); i < connection[0].getY() + a.getHeight(); i++) {
				if (i >= connection[1].getY() && i < connection[1].getY() + b.getHeight()) {
					overlap.add(i);
				}
			}
			int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
			//draw corridor from y=connectionPoint;
			drawFromY(connection[0], connection[1], connectionPoint);
			
			
		} else if (aId == 2 && bId == 3 || aId == 3 && bId == 2) {
			//compare L and R from bottom
			ArrayList<Integer> overlap = new ArrayList<Integer>();
			for (int i = connection[0].getY() - 1; i >= connection[0].getY() - a.getHeight(); i--) {
				if (i < connection[1].getY() && i >= connection[1].getY() - b.getHeight()) {
					overlap.add(i);
				}
			}
			int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
			//draw corridor from y=connectionPoint;
			drawFromY(connection[0], connection[1], connectionPoint);
			
		} else if (aId == 0 && bId == 3 || aId == 3 && bId == 0) {
			//compare T and B from left
			ArrayList<Integer> overlap = new ArrayList<Integer>();
			for (int i = connection[0].getX(); i < connection[0].getX() + a.getWidth(); i++) {
				if (i >= connection[1].getX() && i < connection[1].getX() + b.getWidth()) {
					overlap.add(i);
				}
			}
			int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
			//draw corridor from x = connectionPoint
			drawFromX(connection[0], connection[1], connectionPoint);
			
			
		} else if (aId == 1 && bId == 2 || aId == 2 && bId == 1) {
			//compare T and B from right
			ArrayList<Integer> overlap = new ArrayList<Integer>();
			for (int i = connection[0].getX() - 1; i >= connection[0].getX() - a.getWidth(); i--) {
				if (i < connection[1].getX() && i >= connection[1].getX() - b.getWidth()) {
					overlap.add(i);
				}
			}
			int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
			//draw corridor from x = connectionPoint;
			drawFromX(connection[0], connection[1], connectionPoint);
			
		} else if (aId == 0 && bId == 2) { //3 cases, vertical straight corridors, horizontal straight corridors, bent corridors
			if (connection[0].getX() > connection[1].getX() && connection[0].getY() < connection[1].getY()) {
				//compare L and R edges
				ArrayList<Integer> overlap = new ArrayList<Integer>();
				for (int i = connection[0].getY(); i < connection[0].getY() + a.getHeight(); i++) {
					if (i >= connection[1].getY() - b.getHeight() && i < connection[1].getY()) {
						overlap.add(i);
					}
				}
				int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
				//draw corridor from y = connectionPoint;
				drawFromY(connection[0], connection[1], connectionPoint);
				
			} else if (connection[0].getX() < connection[1].getX() && connection[0].getY() > connection[1].getY()) {
				//compare T and B edges
				ArrayList<Integer> overlap = new ArrayList<Integer>();
				for (int i = connection[0].getX(); i < connection[0].getX() + a.getWidth(); i++) {
					if (i >= connection[1].getX() - b.getWidth() && i < connection[1].getX()) {
						overlap.add(i);
					}
				}
				int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
				//draw corridor from x = connectionPoint;
				drawFromX(connection[0], connection[1], connectionPoint);
				
			} else { //if one is greater / less than in both coordinates, or equal in 1 coordinate
				/**
				 * bent corridor
				 */
				if (!drawBentCorridor(connection)) remaining = connection;
			}
		} else if (aId == 2 && bId == 0) {
			if (connection[0].getX() < connection[1].getX() && connection[0].getY() > connection[1].getY()) {
				//compare L and R edges
				ArrayList<Integer> overlap = new ArrayList<Integer>();
				for (int i = connection[0].getY() - 1; i >= connection[0].getY() - a.getHeight(); i--) {
					if (i < connection[1].getY() + b.getHeight() && i >= connection[1].getY()) {
						overlap.add(i);
					}
				}
				int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
				//draw corridor from y = connectionPoint;
				drawFromY(connection[0], connection[1], connectionPoint);
				
			} else if (connection[0].getX() > connection[1].getX() && connection[0].getY() < connection[1].getY()) {
				//compare T and B edges
				ArrayList<Integer> overlap = new ArrayList<Integer>();
				for (int i = connection[0].getX() - 1; i >= connection[0].getX() - a.getWidth(); i--) {
					if (i < connection[1].getX() + b.getWidth() && i >= connection[1].getX()) {
						overlap.add(i);
					}
				}
				int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
				//draw corridor from x = connectionPoint;
				drawFromX(connection[0], connection[1], connectionPoint);
				
			} else {
				/**
				 * bent corridor
				 */
				if (!drawBentCorridor(connection)) remaining = connection;
			}
		} else if (aId == 1 && bId == 3) {
			if (connection[0].getX() < connection[1].getX() && connection[0].getY() < connection[1].getY()) {
				//compare L and R edges
				ArrayList<Integer> overlap = new ArrayList<Integer>();
				for (int i = connection[0].getY(); i < connection[0].getY() + a.getHeight(); i++) {
					if (i >= connection[1].getY() - b.getHeight() && i < connection[1].getY()) {
						overlap.add(i);
					}
				}
				int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
				//draw corridor from y = connectionPoint;
				drawFromY(connection[0], connection[1], connectionPoint);
				
			} else if (connection[0].getX() > connection[1].getX() && connection[0].getY() > connection[1].getY()) {
				//compare T and B edges
				ArrayList<Integer> overlap = new ArrayList<Integer>();
				for (int i = connection[0].getX() - 1; i >= connection[0].getX() - a.getWidth(); i--) {
					if (i < connection[1].getX() + b.getWidth() && i >= connection[1].getX()) {
						overlap.add(i);
					}
				}
				int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
				//draw corridor from x = connectionPoint;
				drawFromX(connection[0], connection[1], connectionPoint);
				
			} else {
				/**
				 * bent corridor
				 */
				if (!drawBentCorridor(connection)) remaining = connection;
			}
		} else if (aId == 3 && bId == 1) {
			if (connection[0].getX() > connection[1].getX() && connection[0].getY() > connection[1].getY()) {
				//compare L and R edges
				ArrayList<Integer> overlap = new ArrayList<Integer>();
				for (int i = connection[0].getY() - 1; i >= connection[0].getY() - a.getHeight(); i--) {
					if (i < connection[1].getY() + b.getHeight() && i >= connection[1].getY()) {
						overlap.add(i);
					}
				}
				int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
				//draw corridor from y = connectionPoint;
				drawFromY(connection[0], connection[1], connectionPoint);
				
			} else if (connection[0].getX() < connection[1].getX() && connection[0].getY() < connection[1].getY()) {
				//compare T and B edges
				ArrayList<Integer> overlap = new ArrayList<Integer>();
				for (int i = connection[0].getX(); i < connection[0].getX() + a.getWidth(); i++) {
					if (i >= connection[1].getX() - b.getWidth() && i < connection[1].getX()) {
						overlap.add(i);
					}
				}
				int connectionPoint = overlap.get((int)(generator.nextDouble() * overlap.size()));
				//draw corridor from x = connectionPoint;
				drawFromX(connection[0], connection[1], connectionPoint);
				
			} else {
				/**
				 * bent corridor
				 */
				if (!drawBentCorridor(connection)) remaining = connection;
			}
		}
		return remaining;
	}//end drawCorridors
	
	public boolean drawBentCorridor(Corner[] connection) { //returns true if drawn, false if not
		//find exposed edges
		System.out.println(connection[0] + ", " + connection[1]);
		Room a = complex.getRoom(connection[0].getRoomId());
		Room b = complex.getRoom(connection[1].getRoomId());
		int aId = connection[0].getId();
		int bId = connection[1].getId();
		
		if (aId == 0 && bId == 2 || aId == 3 && bId == 1 ) {
			a = complex.getRoom(connection[1].getRoomId());
			b = complex.getRoom(connection[0].getRoomId());
			aId = connection[1].getId();
			bId = connection[0].getId();
		}
		/**
		 * aId == 2 && bId == 0	
		 */
		if (aId == 2 && bId == 0) {
			//check for exposed blocks
			ArrayList<Integer> exposedA = new ArrayList<Integer>();
			ArrayList<Integer> exposedB = new ArrayList<Integer>();
			ArrayList<Integer> exposedC = new ArrayList<Integer>();
			ArrayList<Integer> exposedD = new ArrayList<Integer>();
			
			System.out.println("Exposed A");
			for (int i = a.getCorner(3).getX(); i < a.getCorner(2).getX(); i++) {
				try {	
					if (bordersNoTile(board[i][a.getCorner(3).getY() + 2], 1) && board[i][a.getCorner(3).getY() + 2].getType() == 0) {
						exposedA.add(i);
						System.out.print(i + " ");
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
			System.out.println("\nExposed B");
			for (int i = b.getCorner(0).getY(); i < b.getCorner(3).getY(); i++) { 
				try {
					if (bordersNoTile(board[b.getCorner(0).getX() - 3][i], 1) && board[b.getCorner(0).getX() - 3][i].getType() == 0) {
						exposedB.add(i);
						System.out.print(i + " ");
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
			System.out.println("\nExposed C");
			for (int i = a.getCorner(1).getY(); i < a.getCorner(2).getY(); i++) {
				try {
					if (bordersNoTile(board[a.getCorner(1).getX() + 2][i], 1) && board[a.getCorner(1).getX() + 2][i].getType() == 0) {
						exposedC.add(i);
						System.out.print(i + " ");
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
			System.out.println("\nExposed D");
			for (int i = b.getCorner(0).getX(); i < b.getCorner(1).getX(); i++) {
				try {
					if (bordersNoTile(board[i][b.getCorner(0).getY() - 3], 1) && board[i][b.getCorner(0).getY() - 3].getType() == 0) {
						exposedD.add(i);
						System.out.print(i + " ");
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
			if ((exposedA.size() == 0 && exposedC.size() == 0) || (exposedB.size() == 0 && exposedD.size() == 0) || (exposedA.size() == 0 && exposedD.size() == 0) || (exposedB.size() == 0 && exposedC.size() == 0)) return false;
			
			//check possible path area for invalid tiles IF there are possible paths
			ArrayList<Tile> invalid = new ArrayList<Tile>();
			if (exposedA.size() != 0 && exposedB.size() != 0) {
				for (int i = exposedA.get(0); i < b.getCorner(3).getX() - 2; i++) {
					for (int j = a.getCorner(3).getY() + 2; j < exposedB.get(exposedB.size() - 1); j++) {
						if (!bordersNoTile(board[j][i], 1)) {
							invalid.add(board[j][i]);
						}
					}
				} 
			}
			if (exposedC.size() != 0 && exposedD.size() != 0) {
				for (int i = a.getCorner(1).getX() + 2; i < exposedD.get(exposedD.size() - 1); i++) {
					for (int j = exposedC.get(0); j < b.getCorner(1).getY() - 2; j++) {
						if (!bordersNoTile(board[j][i], 1)) {
							invalid.add(board[j][i]);
						}
					}
				}
			}

			//Generate possible paths and pick ONE
			ArrayList<int[]> paths = new ArrayList<int[]>();
			//for exposedA and exposedB
			for (int i = 0; i < exposedA.size(); i++) {
				for (int j = 0; j < exposedB.size(); j++) {
					//for each Tile in each path
					boolean valid = true;
					//vertical part of path
					for (int k = a.getCorner(2).getY() + 2; k < exposedB.get(j); k++) {
						for (int l = 0; l < invalid.size(); l++) {
							if (board[exposedA.get(i)][k] == invalid.get(l)) {
								valid = false;
								break;
							}
							if (!valid) break;
						}
						if (!valid) break;
					}
					if (!valid) break;
					//horizontal part of path
					for (int k = exposedA.get(i); k < b.getCorner(0).getX() - 2; k++) {
						for (int l = 0; l < invalid.size(); l++) {
							if (board[k][exposedB.get(j)] == invalid.get(l)) {
								valid = false;
								break;
							}
							if (!valid) break;
						}
						if (!valid) break;
					}
					if (valid) {
						int[] temp = {exposedA.get(i), exposedB.get(j), 1};
						paths.add(temp);
					}
				}
			}
			//for exposedC and exposedD
			for (int i = 0; i < exposedC.size(); i++) {
				for (int j = 0; j < exposedD.size(); j++) {
					//for each Tile in each path
					boolean valid = true;
					//vertical part of path
					for (int k = exposedC.get(i); k < b.getCorner(0).getY() - 2; k++) {
						for (int l = 0; l < invalid.size(); l++) {
							if (board[exposedD.get(j)][k] == invalid.get(l)) {
								valid = false;
								break;
							}
							if (!valid) break;
						}
						if (!valid) break;
					}
					if (!valid) break;
					//horizontal part of path
					for (int k = a.getCorner(2).getX() + 2; k < exposedD.get(j); k++) {
						for (int l = 0; l < invalid.size(); l++) {
							if (board[k][exposedC.get(i)] == invalid.get(l)) {
								valid = false;
								break;
							}
							if (!valid) break;
						}
						if (!valid) break;
					}
					if (valid) {
						int[] temp = {exposedC.get(i), exposedD.get(j), 2};
						paths.add(temp);
					}
				}
			}
			if (paths.size() == 0) return false;
			
			int[] chosenPath = paths.get((int)(generator.nextDouble() * paths.size()));
			
			for (int i = 0; i < 3; i++) {
				System.out.println("Chosen path: " + chosenPath[i]);
			}
		
			//draw path
			if (chosenPath[2] == 1) {
				//exposed A and B
				for (int i = a.getCorner(2).getY(); i < chosenPath[1]; i++) {
					board[chosenPath[0]][i].setType(2);
				}
				for (int i = chosenPath[0]; i < b.getCorner(0).getX(); i++) {
					board[i][chosenPath[1]].setType(2);
				}
			} else {
				//exposed C and D
				for (int i = a.getCorner(2).getX(); i < chosenPath[1]; i++) {
					board[i][chosenPath[0]].setType(2);
				}
				for (int i = chosenPath[0]; i < b.getCorner(0).getY(); i++) {
					board[chosenPath[1]][i].setType(2);
				}
			}
			
			//connect the rooms within Room objects
			a.addConnection(b.getId());
			b.addConnection(a.getId());
			return true;
			
			/**
			 * aId == 1 && bId == 3
			 */
		} else if (aId == 1 && bId == 3) {
			//check for exposed blocks
			ArrayList<Integer> exposedA = new ArrayList<Integer>();
			ArrayList<Integer> exposedB = new ArrayList<Integer>();
			ArrayList<Integer> exposedC = new ArrayList<Integer>();
			ArrayList<Integer> exposedD = new ArrayList<Integer>();
			
			System.out.println("Exposed A");
			for (int i = a.getCorner(0).getX(); i < a.getCorner(1).getX(); i++) {
				try {	
					if (bordersNoTile(board[i][a.getCorner(1).getY() - 3], 1) && board[i][a.getCorner(1).getY() - 3].getType() == 0) {
						exposedA.add(i);
						System.out.print(i + " ");
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
			System.out.println("\nExposed B");
			for (int i = b.getCorner(0).getY(); i < b.getCorner(3).getY(); i++) { 
				try {
					if (bordersNoTile(board[b.getCorner(3).getX() - 3][i], 1) && board[b.getCorner(3).getX() - 3][i].getType() == 0) {
						exposedB.add(i);
						System.out.print(i + " ");
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
			System.out.println("\nExposed C");
			for (int i = a.getCorner(1).getY(); i < a.getCorner(2).getY(); i++) {
				try {
					if (bordersNoTile(board[a.getCorner(1).getX() + 2][i], 1) && board[a.getCorner(1).getX() + 2][i].getType() == 0) {
						exposedC.add(i);
						System.out.print(i + " ");
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
			System.out.println("\nExposed D");
			for (int i = b.getCorner(3).getX(); i < b.getCorner(2).getX(); i++) {
				try {
					if (bordersNoTile(board[i][b.getCorner(3).getY() + 2], 1) && board[i][b.getCorner(3).getY() + 2].getType() == 0) {
						exposedD.add(i);
						System.out.print(i + " ");
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
			if ((exposedA.size() == 0 && exposedC.size() == 0) || (exposedB.size() == 0 && exposedD.size() == 0) || (exposedA.size() == 0 && exposedD.size() == 0) || (exposedB.size() == 0 && exposedC.size() == 0)) return false;
			
			//check possible path area for invalid tiles IF there are possible paths
			ArrayList<Tile> invalid = new ArrayList<Tile>();
			if (exposedA.size() != 0 && exposedB.size() != 0) {
				for (int i = exposedA.get(0); i < b.getCorner(3).getX() - 2; i++) {
					for (int j = exposedB.get(0); j < a.getCorner(1).getY() - 2; j++) {
						if (!bordersNoTile(board[j][i], 1)) {
							invalid.add(board[j][i]);
						}
					}
				} 
			}
			if (exposedC.size() != 0 && exposedD.size() != 0) {
				for (int i = a.getCorner(1).getX() + 2; i < exposedD.get(exposedD.size() - 1); i++) {
					for (int j = b.getCorner(3).getY() + 2; j < exposedC.get(exposedC.size() - 1); j++) {
						if (!bordersNoTile(board[j][i], 1)) {
							invalid.add(board[j][i]);
						}
					}
				}
			}
			
			//Generate possible paths and pick ONE
			ArrayList<int[]> paths = new ArrayList<int[]>();
			//for exposedA and exposedB
			for (int i = 0; i < exposedA.size(); i++) {
				for (int j = 0; j < exposedB.size(); j++) {
					//for each Tile in each path
					boolean valid = true;
					//vertical part of path
					for (int k = exposedB.get(j); k < a.getCorner(1).getY() - 2; k++) {
						for (int l = 0; l < invalid.size(); l++) {
							if (board[exposedA.get(i)][k] == invalid.get(l)) {
								valid = false;
								break;
							}
							if (!valid) break;
						}
						if (!valid) break;
					}
					if (!valid) break;
					//horizontal part of path
					for (int k = exposedA.get(i); k < b.getCorner(3).getX() - 2; k++) {
						for (int l = 0; l < invalid.size(); l++) {
							if (board[k][exposedB.get(j)] == invalid.get(l)) {
								valid = false;
								break;
							}
							if (!valid) break;
						}
						if (!valid) break;
					}
					if (valid) {
						int[] temp = {exposedA.get(i), exposedB.get(j), 1};
						paths.add(temp);
					}
				}
			}
			//for exposedC and exposedD
			for (int i = 0; i < exposedC.size(); i++) {
				for (int j = 0; j < exposedD.size(); j++) {
					//for each Tile in each path
					boolean valid = true;
					//vertical part of path
					for (int k = b.getCorner(3).getY() - 2; k < exposedC.get(i); k++) {
						for (int l = 0; l < invalid.size(); l++) {
							if (board[exposedD.get(j)][k] == invalid.get(l)) {
								valid = false;
								break;
							}
							if (!valid) break;
						}
						if (!valid) break;
					}
					if (!valid) break;
					//horizontal part of path
					for (int k = a.getCorner(1).getX() + 2; k < exposedD.get(j); k++) {
						for (int l = 0; l < invalid.size(); l++) {
							if (board[k][exposedC.get(i)] == invalid.get(l)) {
								valid = false;
								break;
							}
							if (!valid) break;
						}
						if (!valid) break;
					}
					if (valid) {
						int[] temp = {exposedC.get(i), exposedD.get(j), 2};
						paths.add(temp);
					}
				}
			}
			if (paths.size() == 0) return false;
			
			int[] chosenPath = paths.get((int)(generator.nextDouble() * paths.size()));
			
			for (int i = 0; i < 3; i++) {
				System.out.println("Chosen path: " + chosenPath[i]);
			}
			
			//draw path
			if (chosenPath[2] == 1) {
				//exposed A and B
				for (int i = chosenPath[1]; i < a.getCorner(1).getY(); i++) {
					board[chosenPath[0]][i].setType(2);
				}
				for (int i = chosenPath[0]; i < b.getCorner(3).getX(); i++) {
					board[i][chosenPath[1]].setType(2);
				}
			} else {
				//exposed C and D
				for (int i = a.getCorner(1).getX(); i <= chosenPath[1]; i++) {
					board[i][chosenPath[0]].setType(2);
				}
				for (int i = b.getCorner(3).getY(); i < chosenPath[0]; i++) {
					board[chosenPath[1]][i].setType(2);
				}
			}
			
			//connect the rooms within Room objects
			a.addConnection(b.getId());
			b.addConnection(a.getId());
			return true;
		} else { //when weird things occur and aId and bId are not diagonals
			System.out.println("ERROR!  aId and bId are not diagonals!");
			return false;
		}
	}//end drawBentCorridor
	
	public void connectNeighbors(Corner[] connection) {
		System.out.println("Executed connectNeighbors");
		Room a = complex.getRoom(connection[0].getRoomId());
		Room b = complex.getRoom(connection[1].getRoomId());
		ArrayList<Room> allConnectionsA = new ArrayList<Room>();
		ArrayList<Room> allConnectionsB = new ArrayList<Room>();
		for (Integer i : complex.findAllConnectedRooms(-1, a.getId())) {
			allConnectionsA.add(complex.getRoom(i));
		}
		for (Integer i : complex.findAllConnectedRooms(-1, b.getId())) {
			allConnectionsB.add(complex.getRoom(i));
		}
		
		//Print out systems for testing
		System.out.println("Connections A");
		for (int i = 0; i < allConnectionsA.size(); i++) {
			System.out.println(allConnectionsA.get(i));
		}
		
		System.out.println("Connections B");
		for (int i = 0; i < allConnectionsB.size(); i++) {
			System.out.println(allConnectionsB.get(i));
		}
		
		//connect the two systems.
		double prev = -1;
		boolean first = true;
		boolean isConnected = false;
		while (!isConnected) {
			double min = Integer.MAX_VALUE;
			Corner[] toConnect = new Corner[2];
			for (int i = 0; i < allConnectionsA.size(); i++) { //for each room in systems 1 and 2
				for (int j = 0; j < allConnectionsB.size(); j++) {
					//for each corner in systems 1 and 2
					for (int k = 0; k < 4; k++) {
						for (int l = 0; l < 4; l++) {
							double dist = complex.findDistance(allConnectionsA.get(i).getCorner(k), allConnectionsB.get(j).getCorner(l));
							if (dist < min && dist > prev) {
								min = dist;
								Corner[] temp = {allConnectionsA.get(i).getCorner(k), allConnectionsB.get(j).getCorner(l)};
								toConnect = temp;
							}
						}
					}
				}
			}
			System.out.println(toConnect[0].getRoomId() + " " + toConnect[0].getId() + ", " + toConnect[1].getRoomId() + " " + toConnect[1].getId());
			
			if (first || (Math.abs(toConnect[0].getId() - toConnect[1].getId()) == 2)) {
				prev = min;
				first = false;
			} else {
				//attempt to connect corners
				System.out.println("Attempting X");
				ArrayList<Integer> xOverlap = new ArrayList<Integer>();
				ArrayList<Integer> yOverlap = new ArrayList<Integer>();
				if (toConnect[0].getId() == 0 || toConnect[0].getId() == 3) {
					for (int i = toConnect[0].getX(); i < toConnect[0].getX() + complex.getRoom(toConnect[0].getRoomId()).getWidth(); i++) {
						if (i >= complex.getRoom(toConnect[1].getRoomId()).getCorner(3).getX() && i < complex.getRoom(toConnect[1].getRoomId()).getCorner(2).getX()) {
							xOverlap.add(i);
						}
					}
				} else {
					for (int i = toConnect[0].getX() - complex.getRoom(toConnect[0].getRoomId()).getWidth(); i < toConnect[0].getX(); i++) {
						if (i >= complex.getRoom(toConnect[1].getRoomId()).getCorner(3).getX() && i < complex.getRoom(toConnect[1].getRoomId()).getCorner(2).getX()) {
							xOverlap.add(i);
						}
					}
				}
				
				if (xOverlap.size() == 0) {
					//check y
					System.out.println("Attempting Y");
					if (toConnect[0].getId() == 0 || toConnect[0].getId() == 1) {
						for (int i = toConnect[0].getY(); i < toConnect[0].getY() + complex.getRoom(toConnect[0].getRoomId()).getHeight(); i++) {
							if (i >= complex.getRoom(toConnect[1].getRoomId()).getCorner(0).getY() && i < complex.getRoom(toConnect[1].getRoomId()).getCorner(3).getY()) {
								yOverlap.add(i);
							}
						}
					} else {
						for (int i = toConnect[0].getY() - complex.getRoom(toConnect[0].getRoomId()).getHeight(); i < toConnect[0].getY(); i++) {
							if (i >= complex.getRoom(toConnect[1].getRoomId()).getCorner(0).getY() && i < complex.getRoom(toConnect[1].getRoomId()).getCorner(3).getY()) {
								yOverlap.add(i);
							}
						}
					}
					
					//choose one from yOverlap
					if (yOverlap.size() != 0) {
						int draw = yOverlap.get((int)(generator.nextDouble() * yOverlap.size()));
						
						drawFromY(toConnect[0], toConnect[1], draw);
						isConnected = true;
					} else {
						prev = min;
						System.out.println(min);
					}
					
					
				} else {
					//draw corridor at random x-value in xOverlap
					int draw = xOverlap.get((int)(generator.nextDouble() * xOverlap.size()));
					
					drawFromX(toConnect[0], toConnect[1], draw);
					isConnected = true;
				}
			}	
		}//end while
	}//end connectNeighbors

	
	public boolean bordersNoTile(Tile tile, int type) {
		if (board[tile.getX() - 1][tile.getY() - 1].getType() == type ||
			board[tile.getX() - 1][tile.getY()].getType() == type ||
			board[tile.getX() - 1][tile.getY() + 1].getType() == type ||
			board[tile.getX()][tile.getY() - 1].getType() == type ||
			board[tile.getX()][tile.getY() + 1].getType() == type ||
			board[tile.getX() + 1][tile.getY() - 1].getType() == type ||
			board[tile.getX() + 1][tile.getY()].getType() == type ||
			board[tile.getX() + 1][tile.getY() + 1].getType() == type) {
			return false;
		} else {
			return true;
		}
	}//end bordersNoTile
	
	public void drawFromX(Corner a, Corner b, int connectionPoint) {
		//draw corridor from x = connectionPoint;
		if (a.getY() < b.getY()) {
			for (int i = a.getY(); i < b.getY(); i++) {
				board[connectionPoint][i].setType(2);
			}
		} else {
			for (int i = b.getY(); i < a.getY(); i++) {
				board[connectionPoint][i].setType(2);
			}
		}
		complex.getRoom(a.getRoomId()).addConnection(b.getRoomId());
		complex.getRoom(b.getRoomId()).addConnection(a.getRoomId());
	}//end drawFromX
	
	public void drawFromY(Corner a, Corner b, int connectionPoint) {
		if (a.getX() < b.getX()) {
			for (int i = a.getX(); i < b.getX(); i++) {
				board[i][connectionPoint].setType(2);
			}
		} else {
			for (int i = b.getX(); i < a.getX(); i++) {
				board[i][connectionPoint].setType(2);
			}
		}
		complex.getRoom(a.getRoomId()).addConnection(b.getRoomId());
		complex.getRoom(b.getRoomId()).addConnection(a.getRoomId());
	}//end drawFromY
	
	public void drawBoard(Graphics g) {
		System.out.println("Drawing board");
		Graphics2D graphic2d = (Graphics2D) g;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j].getType() == 0) {
					graphic2d.setColor(Color.GRAY);
					graphic2d.fillRect(i * 16, j * 16, 16, 16);
				} else if (board[i][j].getType() == 1) {
					graphic2d.setColor(Color.BLACK);
					graphic2d.fillRect(i * 16, j * 16, 16, 16);
				} else {
					graphic2d.setColor(Color.WHITE);
					graphic2d.fillRect(i * 16, j * 16, 16, 16);
				}
			}
		}
	}//end drawBoard
	
	public void drawGrid(Graphics g) {
		System.out.println("Drawing grid");
		Graphics2D graphic2d = (Graphics2D) g;
		graphic2d.setColor(Color.YELLOW);
		for (int i = 0; i < board.length; i++) {
			if (i % 5 == 0) graphic2d.setColor(Color.RED);
			graphic2d.drawLine(i * 16, 0, i * 16, board[0].length * 16);
			graphic2d.setColor(Color.YELLOW);
		}
		for (int i = 0; i < board[0].length; i++) {
			if (i % 5 == 0) graphic2d.setColor(Color.RED);
			graphic2d.drawLine(0, i * 16, board.length * 16, i * 16);
			graphic2d.setColor(Color.YELLOW);
		}
	}//end drawGrid
	
	public void fillBlackTiles() {
		//top left corner
		if (board[1][0].getType() == 2 || board[1][1].getType() == 2 || board[0][1].getType() == 2) {
			board[0][0].setType(1);
		}
		
		//top row
		for (int i = 1; i < board[0].length - 1; i++) {
			if (board[1][i - 1].getType() == 2 || board[1][i].getType() == 2 || board[1][i + 1].getType() == 2) {
				board[0][i].setType(1);
			}
		}
		
		//top right corner
		if (board[0][board.length - 2].getType() == 2 || board[1][board.length - 2].getType() == 2 || board[1][board.length - 1].getType() == 2) {
			board[0][board.length - 1].setType(1);
		}
		
		//left column
		for (int i = 1; i < board.length - 1; i++ ) {
			if (board[i - 1][1].getType() == 2 || board[i][1].getType() == 2 || board[i + 1][1].getType() == 2) {
				board[i][0].setType(1);
			}
		}
		
		//middle tiles
		for (int i = 1; i < board[0].length - 1; i++) {
			for (int j = 1; j < board.length - 1; j++) {
				if (board[i][j].getType() == 0) {
					if (board[i - 1][j - 1].getType() == 2 ||
						board[i][j - 1].getType() == 2 ||
						board[i + 1][j - 1].getType() == 2 ||
						board[i - 1][j].getType() == 2 ||
						board[i + 1][j].getType() == 2 ||
						board[i - 1][j + 1].getType() == 2 ||
						board[i][j + 1].getType() == 2 ||
						board[i + 1][j + 1].getType() == 2) {
							board[i][j].setType(1);
						}
				}
			}
		}
		
		//right column
		for (int i = 1; i < board.length - 1; i++) {
			if (board[board[0].length - 2][i - 1].getType() == 2 || board[board[0].length - 2][i].getType() == 2 || board[board[0].length - 2][i + 1].getType() == 2) {
				board[board[0].length - 1][i].setType(1);
			}
		}
		
		//bottom left corner
		if (board[board.length - 2][0].getType() == 2 || board[board.length - 2][1].getType() == 2 || board[board.length - 1][1].getType() == 2) {
			board[board.length - 1][0].setType(1); 
		}
		
		//bottom row
		for (int i = 1; i < board[0].length - 1; i++) {
			if (board[board.length - 2][i - 1].getType() == 2 || board[board.length - 2][i].getType() == 2 || board[board.length - 2][i + 1].getType() == 2) {
				board[board.length - 1][i].setType(1);
			}
		}
		
		//bottom right corner
		if (board[board.length - 2][board[0].length - 1].getType() == 2 || board[board.length - 2][board[0].length - 2].getType() == 2 || board[board.length - 1][board[0].length - 2].getType() == 2) {
			board[board.length - 1][board[0].length - 1].setType(1);
		}
	}//end fillBlackTiles
}//end class