import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class ArchiverFrame {
	private JFrame frame;
	private JTextField sourceField;
	private JTextField destField;
	private String src;
	private String dest;
	
	public ArchiverFrame() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 448, 206);
		frame.setTitle("Auto-Archiver");
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel sourceLabel = new JLabel("Source Path:");
		sourceLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		sourceLabel.setBounds(12, 13, 95, 16);
		panel.add(sourceLabel);
		
		sourceField = new JTextField();
		sourceField.setBounds(12, 34, 408, 22);
		panel.add(sourceField);
		sourceField.setColumns(10);
		
		JLabel destLabel = new JLabel("Destination Path:");
		destLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		destLabel.setBounds(12, 69, 140, 16);
		panel.add(destLabel);
		
		destField = new JTextField();
		destField.setBounds(12, 90, 408, 22);
		panel.add(destField);
		destField.setColumns(10);
		
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "Setup complete.  Refresh to move files.");
			src = new String();
			dest = new String();
			
			src = sourceField.getText();
			dest = destField.getText();
			
			try {
			FileWriter writer = new FileWriter("C:\\Auto-Archiver\\data.txt");
			writer.write("SOURCE: <" + src + ">\n");
			writer.write("DESTINATION: <" + dest + ">");
			writer.close();
			} catch (IOException e) {
				System.out.println("Error writing to file.");
			}
			
			
			frame.dispose();
			}
		});
		confirmButton.setBounds(323, 125, 97, 25);
		panel.add(confirmButton);

	}
	
	public void setVisible() {
		frame.setVisible(true);
	}
}


