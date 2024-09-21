package byow;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.util.Random;

public class Frame extends JFrame {

	private static final long serialVersionUID = 7544429512914045589L;

	private Random generator;

	public Frame(long seed) {
		generator = new Random(seed);
		initialize();
	}//end constructor

	private void initialize() {
		setBounds(0, 0, 900, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Board board = new Board(50, 50, generator);
		
		getContentPane().add(board, BorderLayout.CENTER);
	}//end initialize

}//end class