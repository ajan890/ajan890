/**
	@version 1.0 - December 1, 2021
	@author Aidan Jan
	Log.java
*/
import javax.swing.JTextArea;

public class Log extends JTextArea {
	private static final long serialVersionUID = 6552239819073691756L;
	private int maxLines;
	private String text;
	private int numLines = 0;
	
	public Log(int maxLines) {
		super();
		this.maxLines = maxLines;
		this.text = "";
		setRows(20);
		setWrapStyleWord(true);
		setEditable(false);
	}
	
	public void addLine(String addText) {
		if (numLines == 0) {
			text += addText;
			numLines++;
		} else if (numLines < maxLines) {
			text += "\n" + addText;
			numLines++;
		} else {
			text = text.substring(text.indexOf("\n") + 1);
			text += "\n" + addText;
		}
		setText(text);
	}
	
}
