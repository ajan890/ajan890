import java.util.LinkedList;
import java.util.NoSuchElementException;

public class MoveList {
	private LinkedList<Move> moves;
	
	public MoveList() {
		moves = new LinkedList<Move>();
	}
	
	public void add(Move move) {
		moves.add(move);
	}
	
	public Move remove() throws NoSuchElementException {
		Move move = moves.remove();
		return move;
	}
	


}
