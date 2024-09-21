import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;

public class Overlay extends JFrame {
    private JPanel contentPane;
    
    //construct
	public Overlay(int width, int height) {
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		setContentPane(contentPane);
		setUndecorated(true);
    	setSize(width, height);
    	
    	JPanel panel = new JPanel();
    	contentPane.add(panel);
    	
    	JLabel label = new JLabel("Click to set location.");
    	label.setFont(new Font("Tahoma", Font.BOLD, 32));
    	panel.add(label);
    	
    	setOpacity(.25f);
    	setVisible(true);
    }//end construct
}//end class
