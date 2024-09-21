/**
	@version 1.0 - December 1, 2021
	@author Aidan Jan
	WelcomeInterface.java
*/

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WelcomeInterface extends JFrame {

	private static final long serialVersionUID = -4508965999377745446L;
	private JTextField watermarkField;
	
	public WelcomeInterface() {
		super();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e2) {
		}
		setTitle("Watermarker");
		setBounds(100, 100, 450, 235);
		setResizable(false);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel welcomeLabel = new JLabel("Welcome!");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 32));
		welcomeLabel.setBounds(0, 11, 433, 53);
		panel.add(welcomeLabel);
		
		JLabel lblNewLabel = new JLabel("Please select an image to use as a watermark.");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 56, 433, 14);
		panel.add(lblNewLabel);
		
		watermarkField = new JTextField();
		watermarkField.setBounds(9, 125, 413, 20);
		panel.add(watermarkField);
		watermarkField.setColumns(10);
		
		JButton chooseFileButton = new JButton("Choose File");
		chooseFileButton.setBounds(149, 75, 135, 45);
		panel.add(chooseFileButton);
		
		JButton selectButton = new JButton("Select");
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		selectButton.setBounds(171, 156, 89, 23);
		panel.add(selectButton);
		
		chooseFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png");
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("C:\\Users\\" + System.getProperty("user.name")));
				
                if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
                    // do something
                	File selectedFile = chooser.getSelectedFile();
                	watermarkField.setText(selectedFile.getPath());
                }
			}
		});
		
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//copy watermark image
				File prevWatermark;
				if (watermarkField.getText().length() != 0) {
				File watermark = new File(watermarkField.getText());
					if (!watermark.exists()) { //if path provided DOES NOT exist
						JOptionPane.showMessageDialog(null, "Watermark specified by the path provided does not exist!");
					} else { //if path provided DOES exist, copy watermark into folder
						File toDelete1 = new File("C:\\Watermarker\\prevwatermark.png");
						File toDelete2 = new File("C:\\Watermarker\\prevwatermark.jpg");
						if (toDelete1.exists()) toDelete1.delete();
						if (toDelete2.exists()) toDelete1.delete();
						prevWatermark = new File("C:\\Watermarker\\watermark.png");
						if (!prevWatermark.exists()) {
							prevWatermark = new File("C:\\Watermarker\\watermark.jpg");
							prevWatermark.renameTo(new File("C:\\Watermarker\\prevwatermark.jpg"));
						} else {
							prevWatermark.renameTo(new File("C:\\Watermarker\\prevwatermark.png"));
						}
						if (watermarkField.getText().substring(watermarkField.getText().lastIndexOf('.')).equals(".png")) {
							try {
								Files.copy(watermark.toPath(), new File("C:\\Watermarker\\watermark.png").toPath());
								//tell user setup is complete
								JOptionPane.showMessageDialog(null, "Watermark selected!\nFirst boot setup is now complete.\nPlease restart the program.");
								dispose();
								

							} catch (IOException e1) {
							}
						} else if (watermarkField.getText().substring(watermarkField.getText().lastIndexOf('.')).equals(".jpg")) {
							watermark.renameTo(new File("C:\\Watermarker\\watermark.jpg"));
						} else {
							JOptionPane.showMessageDialog(null, "Watermark file selected must be either .png or .jpg");
						}
	
					}
				}
			}
			
		});
		
		
	}
}
