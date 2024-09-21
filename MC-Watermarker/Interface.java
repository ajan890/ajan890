/**
	@version 1.0 - December 1, 2021
	@author Aidan Jan
	Interface.java
*/

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JRadioButton;
import java.awt.Color;
import javax.swing.JSlider;
import javax.swing.JSeparator;
import javax.swing.Timer;
import static java.nio.file.StandardWatchEventKinds.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Scanner;

public class Interface extends JFrame {
	private static final long serialVersionUID = -5293921287514809059L;
	private JTextField opacityField;
	private int[] settings; //settings[0]: 0 <= opacity <= 100
							//settings[1]: location 1 = TL, 2 = TR, 3 = BL, 4 = BR, 5 = C
							//settings[2]: 0 <= finalImageOpacity <= 100
							//settings[3]: 0 <= waterMarkScaleCorners <= 100
							//settings[4]: 0 <= waterMarkScaleCenter <= 100
							//settings[5]: timer (seconds)
	private FileWriter fileWriter;
	private Scanner scanner;
	private String path;
	private int state = 1;
	private Log logLabel;
	private boolean settingsOpened = false;
	private SettingsInterface settingsUI;
	private Timer timer;
	
	private boolean running = false;
	private WatchService fileWatcher;

	
	public Interface() {
		super();
		setResizable(false);
		setTitle("Watermarker");
		setBounds(0, 0, 514, 380);
		
		//create frame
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		logLabel = new Log(16);
		logLabel.setBackground(Color.WHITE);
		logLabel.setBounds(10, 32, 253, 267);
		panel.add(logLabel);
		
		JLabel logTitleLabel = new JLabel("Log:");
		logTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		logTitleLabel.setBounds(10, 11, 253, 14);
		panel.add(logTitleLabel);
		
		JButton toggleButton = new JButton("Start Tracking");
		toggleButton.setBounds(304, 289, 184, 41);
		panel.add(toggleButton);
		
		JLabel statusTitleLabel = new JLabel("Status:");
		statusTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		statusTitleLabel.setBounds(10, 316, 55, 14);
		panel.add(statusTitleLabel);
		
		JLabel statusLabel = new JLabel("Inactive");
		statusLabel.setForeground(Color.RED);
		statusLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		statusLabel.setBounds(70, 316, 66, 14);
		panel.add(statusLabel);
		
		JLabel locationTitleLabel = new JLabel("Location:");
		locationTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		locationTitleLabel.setBounds(307, 11, 86, 14);
		panel.add(locationTitleLabel);
		
		JRadioButton topLeftButton = new JRadioButton("Top Left of Image");
		topLeftButton.setBackground(Color.WHITE);
		topLeftButton.setBounds(304, 32, 184, 23);
		panel.add(topLeftButton);
		
		JRadioButton topRightButton = new JRadioButton("Top Right of Image");
		topRightButton.setBackground(Color.WHITE);
		topRightButton.setBounds(304, 58, 184, 23);
		panel.add(topRightButton);
		
		JRadioButton bottomLeftButton = new JRadioButton("Bottom Left of Image");
		bottomLeftButton.setBackground(Color.WHITE);
		bottomLeftButton.setBounds(304, 84, 184, 23);
		panel.add(bottomLeftButton);
		
		JRadioButton bottomRightButton = new JRadioButton("Bottom Right of Image");
		bottomRightButton.setBackground(Color.WHITE);
		bottomRightButton.setBounds(304, 110, 184, 23);
		panel.add(bottomRightButton);
		
		JRadioButton centerButton = new JRadioButton("Center of Image");
		centerButton.setBackground(Color.WHITE);
		centerButton.setBounds(304, 136, 184, 23);
		panel.add(centerButton);
		
		JLabel opacityTitleLabel = new JLabel("Watermark Opacity:");
		opacityTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		opacityTitleLabel.setBounds(304, 166, 131, 14);
		panel.add(opacityTitleLabel);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(topLeftButton);
		buttonGroup.add(topRightButton);
		buttonGroup.add(bottomLeftButton);
		buttonGroup.add(bottomRightButton);
		buttonGroup.add(centerButton);
		
		JSlider opacitySlider = new JSlider();
		opacitySlider.setValue(100);
		opacitySlider.setBackground(Color.WHITE);
		opacitySlider.setMinorTickSpacing(2);
		opacitySlider.setMajorTickSpacing(10);
		opacitySlider.setPaintTicks(true);
		opacitySlider.setBounds(304, 191, 184, 26);
		panel.add(opacitySlider);
		
		opacityField = new JTextField();
		opacityField.setText("100");
		opacityField.setBackground(Color.WHITE);
		opacityField.setBounds(424, 163, 36, 20);
		panel.add(opacityField);
		opacityField.setColumns(10);
		
		JLabel percentageLabel = new JLabel("%");
		percentageLabel.setBounds(465, 166, 23, 14);
		panel.add(percentageLabel);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(293, 11, 1, 319);
		panel.add(separator);
		
		JButton minimizeButton = new JButton("Minimize Window");
		minimizeButton.setBounds(304, 260, 184, 23);
		panel.add(minimizeButton);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 305, 277, 1);
		panel.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(304, 255, 181, 1);
		panel.add(separator_2);
		
		JButton moreSettingsButton = new JButton("More Settings");
		moreSettingsButton.setBounds(304, 226, 181, 23);
		panel.add(moreSettingsButton);
		
		
		
		logLabel.addLine("Program started.");
		
		//load settings
		
		settings = new int[6];
		settings = readSettings();
		opacitySlider.setValue(settings[0]);
		opacityField.setText(Integer.toString(settings[0]));

		switch(settings[1]) {
		case 1: topLeftButton.setSelected(true); break;
		case 2: topRightButton.setSelected(true); break;
		case 3: bottomLeftButton.setSelected(true); break;
		case 4: bottomRightButton.setSelected(true); break;
		case 5: centerButton.setSelected(true); break;
		}
		
		//action listeners
		opacitySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				opacityField.setText(Integer.toString(opacitySlider.getValue()));
			}
		}); //end actionListener
		
		opacityField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int percentage = Integer.parseInt(opacityField.getText());
					if (percentage < 0) {
						opacityField.setText("0");
						opacitySlider.setValue(0);
					} else if (percentage > 100) {
						opacityField.setText("100");
						opacitySlider.setValue(100);
					} else opacitySlider.setValue(percentage);
				} catch (NumberFormatException f) {
					opacitySlider.setValue(100);
					opacityField.setText("100");
				}
			}
		}); //end actionListener
		
		moreSettingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings[0] = opacitySlider.getValue();
				if (topLeftButton.isSelected()) settings[1] = 1;
				else if (topRightButton.isSelected()) settings[1] = 2;
				else if (bottomLeftButton.isSelected()) settings[1] = 3;
				else if (centerButton.isSelected()) settings[1] = 5;
				else settings[1] = 4;
				settingsUI = new SettingsInterface(settings, path);
				if (!settingsOpened) {
					settingsOpened = true;
					settingsUI.setVisible(true);
					settingsUI.addWindowListener(new WindowAdapter() {
					    public void windowClosing(WindowEvent e) {
						    settingsOpened = false;
						    readSettings();
						    opacitySlider.setValue(settings[0]);
						    opacityField.setText(Integer.toString(settings[0]));
						    switch(settings[1]) {
							case 1: topLeftButton.setSelected(true); break;
							case 2: topRightButton.setSelected(true); break;
							case 3: bottomLeftButton.setSelected(true); break;
							case 4: bottomRightButton.setSelected(true); break;
							case 5: centerButton.setSelected(true); break;
							}
						}
					    public void windowClosed(WindowEvent e) {
					    	settingsOpened = false;
					    	readSettings();
					    	opacitySlider.setValue(settings[0]);
						    opacityField.setText(Integer.toString(settings[0]));
						    switch(settings[1]) {
							case 1: topLeftButton.setSelected(true); break;
							case 2: topRightButton.setSelected(true); break;
							case 3: bottomLeftButton.setSelected(true); break;
							case 4: bottomRightButton.setSelected(true); break;
							case 5: centerButton.setSelected(true); break;
							}
					    }
					});
				} else {
					settingsUI.setVisible(true);
					settingsUI.requestFocus();
				}
			}
			
		});
		
		toggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!running) {
					//check presence of watermark image
					File png = new File("C:\\Watermarker\\watermark.png");
					if (!png.exists()) {
						File jpg = new File("C:\\Watermarker\\watermark.jpg");
						if (!jpg.exists()) {
							logLabel.addLine("Watermark not found!");
							logLabel.addLine("Images will not be watermarked.");
							JOptionPane.showMessageDialog(null, "No watermark image found!\nImages will not be watermarked.");
						}
					}
					
					//write to settings
					settings = readSettings();
					settings[0] = opacitySlider.getValue();
					if (topLeftButton.isSelected()) settings[1] = 1;
					else if (topRightButton.isSelected()) settings[1] = 2;
					else if (bottomLeftButton.isSelected()) settings[1] = 3;
					else if (centerButton.isSelected()) settings[1] = 5;
					else settings[1] = 4;
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
					
					//start running
					running = true;
					logLabel.addLine("Tracking started.");
					toggleButton.setText("Stop Tracking");
					//lock settings
					topLeftButton.setEnabled(false);
					topRightButton.setEnabled(false);
					bottomLeftButton.setEnabled(false);
					bottomRightButton.setEnabled(false);
					centerButton.setEnabled(false);
					opacityField.setEnabled(false);
					opacitySlider.setEnabled(false);
					//set status to active
					statusLabel.setForeground(Color.GREEN);
					statusLabel.setText("Active");
					setTitle("Watermarker - ACTIVE");
					//start file watcher
					Path directory = Paths.get(path);
					try {
						fileWatcher = FileSystems.getDefault().newWatchService();
						directory.register(fileWatcher, ENTRY_CREATE);
					} catch (IOException e2) {
						System.out.println("Error registering fileWatcher");
					}
					
					timer = new Timer(settings[5] * 1000, new ActionListener() {
						@SuppressWarnings("unchecked")
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								if (running) {
									WatchKey key = fileWatcher.poll();
									for (WatchEvent<?> event: key.pollEvents()) {
										System.out.println("Detected!");
										Path fileName = ((WatchEvent<Path>)event).context();
										//check new file is an image
										String filePath = fileName.toString();
										if(filePath.substring(filePath.length() - 4).equals(".png") || filePath.substring(filePath.length() - 4).equals(".jpg")) {
											if (running) {
												System.out.println("Running?:" + running);
												//watermark image
												BufferedImage screenshot = ImageIO.read(new File(path + "\\" + filePath));
												BufferedImage watermark = ImageIO.read(new File("C:\\Watermarker\\watermark.png"));
												Marker marker = new Marker(screenshot, watermark, settings);
												screenshot = marker.mark();
												
												//make file
												File watermarkedFolder = new File(path + "\\watermarked");
												watermarkedFolder.mkdir();
												logLabel.addLine("Marked image " + fileName);
												filePath = filePath.substring(0, filePath.lastIndexOf('.'));
												ImageIO.write(screenshot, "png", new File(path + "\\watermarked\\" + filePath + "_marked.png"));
											}
										}
										key.reset();
									}
								}
							} catch (NullPointerException e1) {
								System.out.println("None Detected.");
							} catch (IOException e1) {
								System.out.println("Error with Marker");
							}
						}
						
					});
					timer.start();
					
				} else {
					running = false;
					logLabel.addLine("Tracking stopped.");
					toggleButton.setText("Start Tracking");
					//unlock settings
					topLeftButton.setEnabled(true);
					topRightButton.setEnabled(true);
					bottomLeftButton.setEnabled(true);
					bottomRightButton.setEnabled(true);
					centerButton.setEnabled(true);
					opacityField.setEnabled(true);
					opacitySlider.setEnabled(true);
					//set status to inactive
					statusLabel.setForeground(Color.RED);
					statusLabel.setText("Inactive");
					setTitle("Watermarker - INACTIVE");
					//stop timer
					try {
						fileWatcher.close();
					} catch (IOException e1) {
					}
					timer.stop();
				}	
			}
			
		}); //end action listener
		
		minimizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (state == 1) {
					setSize(170, 146);
					statusTitleLabel.setBounds(10, 8, 50, 17);
					statusLabel.setBounds(70, 8, 56, 17);
					minimizeButton.setBounds(10, 33, 133, 23);
					toggleButton.setBounds(10, 61, 133, 34);
					logTitleLabel.setVisible(false);
					logLabel.setVisible(false);
					minimizeButton.setText("Expand Window");
					state = 0;
				} else {
					setSize(514, 380);
					statusTitleLabel.setBounds(10, 316, 55, 14);
					statusLabel.setBounds(70, 316, 66, 14);
					minimizeButton.setBounds(304, 260, 184, 23);
					toggleButton.setBounds(304, 289, 184, 41);	
					logTitleLabel.setVisible(true);
					logLabel.setVisible(true);
					minimizeButton.setText("Minimize Window");
					state = 1;
				}
			}
		});
	} //end constructor
	
	public int[] readSettings() {
		//load settings
				int[] tempSettings = new int[6];
				tempSettings[0] = 100; tempSettings[1] = 4; tempSettings[2] = 100; tempSettings[3] = 10; tempSettings[4] = 100; tempSettings[5] = 5;
				try {
					scanner = new Scanner(new File("C:\\Watermarker\\settings.txt"));
					String temp = scanner.nextLine();
					while(scanner.hasNextLine()) {
						if (temp.indexOf("#") != -1) {
							break;
						}
						temp = scanner.nextLine();
					}
					temp = scanner.nextLine();
					path = temp.substring(temp.indexOf("<") + 1, temp.indexOf(">"));
					temp = "";
					int index = 0;
					while(scanner.hasNextLine()) {
						temp = scanner.nextLine();
						if (temp.indexOf("<") != -1) {
							try {
								tempSettings[index] = Integer.parseInt(temp.substring(temp.indexOf("<") + 1, temp.indexOf(">")));
								index++;
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Error reading settings file.  Resetting to defaults.");
								tempSettings = getDefaultSettings();
								FileWriter fileWriter;
								try {
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
								} catch (IOException e1) {
								}
								
								break;
							}
						}
					}
					
					System.out.println("Path: " + path);
					System.out.println("Opacity: " + settings[0]);
					System.out.println("Location: " + settings[1]);
					System.out.println("Final Opacity: " + settings[2]);
					System.out.println("Watermark Corners Size: " + settings[3]);
					System.out.println("Watermark Center Size: " + settings[4]);
					System.out.println("Timer: " + settings[5]);
				} catch (FileNotFoundException e2) {
					System.out.println("File not found!");
					logLabel.addLine("Error loading settings! Values restored to defaults.");
				}
		
				
		return tempSettings;
	}
	
	public static int[] getDefaultSettings() {
		int[] temp = new int[6];
		temp[0] = 100;
		temp[1] = 4;
		temp[2] = 100;
		temp[3] = 10;
		temp[4] = 100;
		temp[5] = 5;
		return temp;
	}

} //end class
