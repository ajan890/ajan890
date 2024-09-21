package byow;
import javax.swing.JOptionPane;

public class Driver {
	public static void main(String[] args) {
		
		String seed = JOptionPane.showInputDialog("Seed");
		long hash = 0;
		
		boolean parse = false;
		try {
			hash = Long.parseLong(seed);
			if (String.valueOf(hash).length() != 16) {
				parse = true;
			}
		} catch (NumberFormatException e) {
			parse = true;
		}
		
		if (parse) {
			if (seed.length() > 64) {
				seed = seed.substring(seed.length() - 64);
			}
			
			int index = seed.substring(0, 1).hashCode();
			for (Character c : seed.toCharArray()) {
				hash += 27512614111L * c.hashCode() + c * (index - 1);
				index++;
			}
			while (String.valueOf(hash).length() < 18) {
				hash *= 2;
			}
			hash %= 10000000000000000L;
		}
		
		
		System.out.println(hash);
		Frame frame = new Frame(hash);
		frame.setTitle("Seed = " + hash);
		frame.setVisible(true);
	}//end main
}//end class (Total 1263 lines)