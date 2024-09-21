/**
	@version 1.0 - December 1, 2021
	@author Aidan Jan
	SettingsInterface.java
*/

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;

public class SettingsInterface extends JFrame {
	private static final long serialVersionUID = 3404835238815525488L;
	private JTextField minecraftPathField;
	private JTextField timerField;
	private JTextField watermarkField;
	private JTextField finalImageOpacityField;
	private JTextField watermarkSizeCornersField;
	private JTextField watermarkSizeCentersField;
	private FileWriter fileWriter;
	private int[] settings;
	private String path;
	
	public SettingsInterface(int[] settings1, String path1) {
		super();
		this.settings = settings1;
		this.path = path1;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e2) {
		}
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Watermarker Settings");
		setBounds(0, 0, 459, 308);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel minecraftPathTitleLabel = new JLabel("Minecraft Screenshots Folder:");
		minecraftPathTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		minecraftPathTitleLabel.setBounds(10, 11, 225, 14);
		panel.add(minecraftPathTitleLabel);
		
		minecraftPathField = new JTextField();
		minecraftPathField.setBounds(10, 30, 424, 20);
		panel.add(minecraftPathField);
		minecraftPathField.setText(path);
		minecraftPathField.setColumns(10);
		
		JButton autoLocateButton = new JButton("Auto-locate");
		autoLocateButton.setBounds(314, 7, 120, 23);
		panel.add(autoLocateButton);
		
		JButton applyButton = new JButton("Apply");
		applyButton.setBounds(345, 217, 89, 23);
		panel.add(applyButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(246, 217, 89, 23);
		panel.add(cancelButton);
		
		JButton resetButton = new JButton("Reset to defaults");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		resetButton.setBounds(10, 217, 124, 23);
		panel.add(resetButton);
		
		JLabel timerTitleLabel = new JLabel("File check timer interval:");
		timerTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		timerTitleLabel.setBounds(10, 192, 147, 14);
		panel.add(timerTitleLabel);
		
		timerField = new JTextField();
		timerField.setText(Integer.toString(settings[5]));
		timerField.setBounds(167, 189, 36, 20);
		panel.add(timerField);
		timerField.setColumns(10);
		
		JLabel secondsLabel = new JLabel("seconds");
		secondsLabel.setBounds(209, 192, 55, 14);
		panel.add(secondsLabel);
		
		JButton chooseFileButton = new JButton("Choose File");
		chooseFileButton.setBounds(345, 82, 89, 23);
		panel.add(chooseFileButton);
		
		JLabel setWatermarkTitleLabel = new JLabel("Set Watermark:");
		setWatermarkTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		setWatermarkTitleLabel.setBounds(10, 61, 97, 14);
		panel.add(setWatermarkTitleLabel);
		
		watermarkField = new JTextField();
		watermarkField.setBounds(10, 83, 330, 20);
		panel.add(watermarkField);
		watermarkField.setColumns(10);
		
		JLabel finalImageOpacityTitleLabel = new JLabel("Final Image Opacity:");
		finalImageOpacityTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		finalImageOpacityTitleLabel.setBounds(10, 117, 124, 14);
		panel.add(finalImageOpacityTitleLabel);
		
		finalImageOpacityField = new JTextField();
		finalImageOpacityField.setText(Integer.toString(settings[2]));
		finalImageOpacityField.setBounds(167, 114, 36, 20);
		panel.add(finalImageOpacityField);
		finalImageOpacityField.setColumns(10);
		
		JLabel percentageTitleLabel = new JLabel("%");
		percentageTitleLabel.setBounds(209, 117, 46, 14);
		panel.add(percentageTitleLabel);
		
		JLabel watermarkCornerSizeTitleLabel = new JLabel("Watermark size (corners):");
		watermarkCornerSizeTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		watermarkCornerSizeTitleLabel.setBounds(10, 142, 155, 14);
		panel.add(watermarkCornerSizeTitleLabel);
		
		watermarkSizeCornersField = new JTextField();
		watermarkSizeCornersField.setText(Integer.toString(settings[3]));
		watermarkSizeCornersField.setBounds(167, 139, 36, 20);
		panel.add(watermarkSizeCornersField);
		watermarkSizeCornersField.setColumns(10);
		
		JLabel percentageTitleLabel2 = new JLabel("%");
		percentageTitleLabel2.setBounds(209, 142, 46, 14);
		panel.add(percentageTitleLabel2);
		
		JLabel watermarkCenterSizeTitleLabel = new JLabel("Watermark size (center):");
		watermarkCenterSizeTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		watermarkCenterSizeTitleLabel.setBounds(10, 167, 155, 14);
		panel.add(watermarkCenterSizeTitleLabel);
		
		watermarkSizeCentersField = new JTextField();
		watermarkSizeCentersField.setText(Integer.toString(settings[4]));
		watermarkSizeCentersField.setBounds(167, 164, 36, 20);
		panel.add(watermarkSizeCentersField);
		watermarkSizeCentersField.setColumns(10);
		
		JLabel percentageTitleLabel3 = new JLabel("%");
		percentageTitleLabel3.setBounds(209, 167, 46, 14);
		panel.add(percentageTitleLabel3);
		
		JSlider finalOpacitySlider = new JSlider();
		finalOpacitySlider.setValue(settings[2]);
		finalOpacitySlider.setMinorTickSpacing(2);
		finalOpacitySlider.setMajorTickSpacing(10);
		finalOpacitySlider.setPaintTicks(true);
		finalOpacitySlider.setBounds(234, 114, 200, 20);
		panel.add(finalOpacitySlider);
		
		JSlider cornerSizeSlider = new JSlider();
		cornerSizeSlider.setValue(settings[3]);
		cornerSizeSlider.setMinorTickSpacing(2);
		cornerSizeSlider.setMajorTickSpacing(10);
		cornerSizeSlider.setPaintTicks(true);
		cornerSizeSlider.setBounds(234, 139, 200, 20);
		panel.add(cornerSizeSlider);
		
		JSlider centerSizeSlider = new JSlider();
		centerSizeSlider.setValue(settings[4]);
		centerSizeSlider.setMinorTickSpacing(2);
		centerSizeSlider.setMajorTickSpacing(10);
		centerSizeSlider.setPaintTicks(true);
		centerSizeSlider.setBounds(234, 164, 200, 20);
		panel.add(centerSizeSlider);
		
		JLabel lblNewLabel = new JLabel("Originally programmed for Ender SMP Technical Minecraft Server");
		lblNewLabel.setBounds(10, 251, 355, 14);
		panel.add(lblNewLabel);
		
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
		
		timerField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//settings[2] = Integer.parseInt(timerField.getText());
				} catch (NumberFormatException f) {
					timerField.setText("10");
				}
			}
		}); //end actionListener
		
		finalOpacitySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				finalImageOpacityField.setText(Integer.toString(finalOpacitySlider.getValue()));
			}
		}); //end actionListener
		
		finalImageOpacityField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int percentage = Integer.parseInt(finalImageOpacityField.getText());
					if (percentage < 0) {
						finalOpacitySlider.setValue(0);
						finalImageOpacityField.setText("0");
					} else if (percentage > 100) {
						finalOpacitySlider.setValue(100);
						finalImageOpacityField.setText("100");
					} else finalOpacitySlider.setValue(percentage);
				} catch (NumberFormatException f) {
					finalOpacitySlider.setValue(100);
					finalImageOpacityField.setText("100");
				}
			}
		}); //end actionListener
		
		cornerSizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				watermarkSizeCornersField.setText(Integer.toString(cornerSizeSlider.getValue()));
			}
		}); //end actionListener
		
		watermarkSizeCornersField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int percentage = Integer.parseInt(watermarkSizeCornersField.getText());
					if (percentage < 0) {
						cornerSizeSlider.setValue(0);
						watermarkSizeCornersField.setText("0");
					} else if (percentage > 100) {
						cornerSizeSlider.setValue(100);
						watermarkSizeCornersField.setText("100");
					} else cornerSizeSlider.setValue(percentage);
				} catch (NumberFormatException f) {
					cornerSizeSlider.setValue(100);
					watermarkSizeCornersField.setText("100");
				}
			}
		}); //end actionListener
		
		centerSizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				watermarkSizeCentersField.setText(Integer.toString(centerSizeSlider.getValue()));
			}
		}); //end actionListener
		
		watermarkSizeCentersField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int percentage = Integer.parseInt(watermarkSizeCentersField.getText());
					if (percentage < 0) {
						centerSizeSlider.setValue(0);
						watermarkSizeCentersField.setText("0");
					} else if (percentage > 100) {
						centerSizeSlider.setValue(100);
						watermarkSizeCentersField.setText("100");
					} else centerSizeSlider.setValue(percentage);
				} catch (NumberFormatException f) {
					centerSizeSlider.setValue(100);
					watermarkSizeCentersField.setText("100");
				}
			}
		}); //end actionListener
		
		timerField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int temp = Integer.parseInt(timerField.getText());
					if (temp < 1) {
						timerField.setText("1");
					}
				} catch (NumberFormatException e2) {
					timerField.setText("5");
				}
				
			}
			
		});
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		
		autoLocateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				autoLocate();
			}	
		});
		
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTitle("Watermarker Settings - Settings Applied");
				if (minecraftPathField.getText().length() != 0) {
					path = minecraftPathField.getText();
				}
				settings[2] = finalOpacitySlider.getValue();
				settings[3] = cornerSizeSlider.getValue();
				settings[4] = centerSizeSlider.getValue();
				settings[5] = Integer.parseInt(timerField.getText());
				try {
					fileWriter = new FileWriter("C:\\Watermarker\\settings.txt");
					String toWrite = "";
					toWrite += "WARNING: IT IS HIGHLY RECOMMENDED NOT TO EDIT THIS FILE DIRECTLY.\n" 
							+ "DOING SO MAY RESULT IN UNEXPECTED ERRORS.\n"
							+ "DELETE THIS FILE TO RESET THE PROGRAM.\n"
							+ "\n#SETTINGS\n"
							+ "Minecraft Path: <" + path + ">\n"
							+ "Watermark Opacity: <" + settings[0] + ">\n"
							+ "Location: <" + settings[1] + ">\n"
							+ "Final Opacity: <" + settings[2] + ">\n"
							+ "Watermark Corners Size <" + settings[3] + ">\n"
							+ "Watermark Center Size <" + settings[4] + ">\n"
							+ "Timer: <" + settings[5] + ">";
					fileWriter.write(toWrite);
					fileWriter.close();
				} catch (IOException e1) {
					System.out.println("Failed to write to Settings file.");
				}
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
							} catch (IOException e1) {
							}
						} else if (watermarkField.getText().substring(watermarkField.getText().lastIndexOf('.')).equals(".jpg")) {
							watermark.renameTo(new File("C:\\Watermarker\\watermark.jpg"));
						} else {
							JOptionPane.showMessageDialog(null, "Watermark file selected must be either .png or .jpg");
						}
	
					}
				}	
			} //end actionPerformed
		}); //end ActionListener
		
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//write to settings file
					fileWriter = new FileWriter(new File("C:\\Watermarker\\settings.txt"));
					fileWriter.write("WARNING: IT IS HIGHLY RECOMMENDED NOT TO EDIT THIS FILE DIRECTLY.\n" 
							+ "DOING SO MAY RESULT IN UNEXPECTED ERRORS.\n"
							+ "DELETE THIS FILE TO RESET THE PROGRAM.\n"
							+ "\n#SETTINGS\n"
							+ "Minecraft Path: <" + path + ">\n"
							+ "Watermark Opacity: <100>\n"
							+ "Location: <4>\n"
							+ "Final Opacity: <100>\n"
							+ "Watermark Corners Size <10>\n"
							+ "Watermark Center Size <100>\n"
							+ "Timer: <5>");
					fileWriter.close();
					//reset all settings
					autoLocate();
					watermarkField.setText("");
					finalImageOpacityField.setText("100");
					finalOpacitySlider.setValue(100);
					watermarkSizeCornersField.setText("10");
					cornerSizeSlider.setValue(10);
					watermarkSizeCentersField.setText("100");
					centerSizeSlider.setValue(100);
					timerField.setText("5");
					setTitle("Watermarker Settings - Settings Defaulted");		
				} catch (IOException e1) {
				}
			}
		});
		
	}//end constructor
	
	public void autoLocate() {
		String minecraftPath = new String("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\.minecraft\\screenshots");
		if (!new File(minecraftPath).exists()) {
			JOptionPane.showMessageDialog(null, "Minecraft folder cannot be located.");
		} else {
			minecraftPathField.setText(minecraftPath);
		}
	}
}//end class
