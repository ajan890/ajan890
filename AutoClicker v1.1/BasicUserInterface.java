import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JRadioButton;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

import java.time.LocalDateTime;
import javax.swing.JFormattedTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class BasicUserInterface extends JFrame{

	//instance
	private JPanel contentPane; //contentPane
	private JTextField textFieldHours; //text field for Time interval - hours
	private JTextField textFieldMinutes; //text field for Time interval - minutes
	private JTextField textFieldSeconds; //text field for Time interval - seconds
	private JTextField textFieldMilliseconds; //text field for Time interval - milliseconds
	private String typeOfClick; //String output for type of click drop down menu
	private String clickModifier; //String output for click modifiers drop down menu
	private JTextField textXPosition; //text field for choose location - set location, X
	private JTextField textYPosition; //text field for choose location - set location, Y
	private JTextField textRepeatComputerTime; //text field for Repeat until - computer time
	private JTextField textTimeElapsed; //text field for Repeat until - time elapsed
	private JTextField textNumClicks; //text field for Repeat until -Number of clicks
	private JButton stopButton;
	
	//processed instance
	private int hours; //hours in time interval.
	private int minutes; //minutes in time interval.
	private int seconds; //seconds in time interval.
	private int milliseconds; //milliseconds in time interval.
	private int clickInterval; //total time between clicks, derived from hours, minutes, seconds, milliseconds.
	private int xPosition; //x position of location, if "Set location" is chosen.
	private int yPosition; //y position of location, if "Set location" is chosen.
	private int computerHours; //hours part of time when clicking should stop, if "Computer Time" is selected.
	private int computerMinutes; //minutes part of time when clicking should stop, if "Computer Time" is selected.
	private int computerSeconds; //seconds part of time when clicking should stop, if "Computer Time" is selected.
	private int timeElapsed; //Amount of time that should pass before clicking stops, if "Time Elapsed" is selected.  Measured in milliseconds.
	private int numClicks; //Number of clicks before clicking stops, if "Number of Clicks" is selected.
	
	private int button; //button number from typeOfClick;
	private boolean isRunning = false;
	private Clicker Clicker;
	
	//construct
	public BasicUserInterface(int width, int height) {
		//create frame
		setTitle("Autoclicker_Test");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((width / 3), (height / 3), 559, 399);
		
		//everything inside
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		
		JLabel typeOfClickLabel = new JLabel("Type of click");
		typeOfClickLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		typeOfClickLabel.setBounds(12, 77, 89, 16);
		panel_1.add(typeOfClickLabel);
		
		JComboBox<String> typeOfClickDropdown = new JComboBox<String>();
		typeOfClickDropdown.setModel(new DefaultComboBoxModel<String>(new String[] {"Left", "Middle", "Right"}));
		typeOfClickDropdown.setMaximumRowCount(3);
		typeOfClickDropdown.setBounds(12, 100, 89, 25);
		panel_1.add(typeOfClickDropdown);

		
		JLabel modifiersLabel = new JLabel("Modifiers");
		modifiersLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		modifiersLabel.setBounds(133, 77, 89, 16);
		panel_1.add(modifiersLabel);
		
		JComboBox<String> modifiersDropdown = new JComboBox<String>();
		modifiersDropdown.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "Double", "Hold"}));
		modifiersDropdown.setBounds(133, 100, 89, 25);
		panel_1.add(modifiersDropdown);
		
		JButton startButton = new JButton("Start");
		startButton.setMnemonic(81);
		startButton.setBounds(12, 298, 250, 51);
		panel_1.add(startButton);
		
		JLabel chooseLocationLabel = new JLabel("Choose Location");
		chooseLocationLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		chooseLocationLabel.setBounds(12, 150, 123, 16);
		panel_1.add(chooseLocationLabel);
		
		JLabel xPosLabel = new JLabel("X:");
		xPosLabel.setBounds(12, 247, 13, 16);
		panel_1.add(xPosLabel);
		
		textXPosition = new JTextField();
		textXPosition.setEnabled(false);
		textXPosition.setBounds(37, 244, 57, 22);
		panel_1.add(textXPosition);
		textXPosition.setColumns(10);
		
		JLabel yPosLabel = new JLabel("Y:");
		yPosLabel.setBounds(106, 247, 13, 16);
		panel_1.add(yPosLabel);
		
		textYPosition = new JTextField();
		textYPosition.setEnabled(false);
		textYPosition.setBounds(131, 244, 57, 22);
		panel_1.add(textYPosition);
		textYPosition.setColumns(10);
		
		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
		stopButton.setBounds(275, 298, 250, 51);
		panel_1.add(stopButton);
		
		JLabel lblNewLabel = new JLabel("Time Interval");
		lblNewLabel.setBounds(12, 13, 96, 16);
		panel_1.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		textFieldHours = new JTextField();
		textFieldHours.setText("0");
		textFieldHours.setBounds(12, 42, 61, 22);
		panel_1.add(textFieldHours);
		textFieldHours.setColumns(5);
		
		JLabel hoursLabel = new JLabel("Hours");
		hoursLabel.setBounds(75, 45, 36, 16);
		panel_1.add(hoursLabel);
		
		textFieldMinutes = new JTextField();
		textFieldMinutes.setText("0");
		textFieldMinutes.setBounds(123, 42, 61, 22);
		panel_1.add(textFieldMinutes);
		textFieldMinutes.setColumns(5);
		
		JLabel minutesLabel = new JLabel("Minutes");
		minutesLabel.setBounds(186, 44, 47, 19);
		panel_1.add(minutesLabel);
		
		textFieldSeconds = new JTextField();
		textFieldSeconds.setText("1");
		textFieldSeconds.setBounds(255, 42, 61, 22);
		panel_1.add(textFieldSeconds);
		textFieldSeconds.setColumns(5);
		
		JLabel secondsLabel = new JLabel("Seconds");
		secondsLabel.setBounds(318, 45, 51, 16);
		panel_1.add(secondsLabel);
		
		textFieldMilliseconds = new JTextField();
		textFieldMilliseconds.setText("0");
		textFieldMilliseconds.setBounds(391, 42, 61, 22);
		panel_1.add(textFieldMilliseconds);
		textFieldMilliseconds.setColumns(5);
		
		JLabel millisecondsLabel = new JLabel("Milliseconds");
		millisecondsLabel.setBounds(454, 45, 71, 16);
		panel_1.add(millisecondsLabel);
		
		JLabel repeatLabel = new JLabel("Repeat until...");
		repeatLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		repeatLabel.setBounds(291, 77, 115, 16);
		panel_1.add(repeatLabel);
		
		textRepeatComputerTime = new JTextField();
		textRepeatComputerTime.setEnabled(false);
		textRepeatComputerTime.setText("HH:MM:SS");
		textRepeatComputerTime.setBounds(409, 132, 116, 20);
		panel_1.add(textRepeatComputerTime);
		textRepeatComputerTime.setColumns(10);		
		
		textNumClicks = new JTextField();
		textNumClicks.setEnabled(false);
		textNumClicks.setText("clicks");
		textNumClicks.setBounds(409, 192, 71, 20);
		panel_1.add(textNumClicks);
		textNumClicks.setColumns(10);
		
		//action listener for pick location button
		JButton pickLocationButton = new JButton("Pick Location");
		pickLocationButton.setEnabled(false);
		pickLocationButton.setBounds(127, 205, 123, 25);
		panel_1.add(pickLocationButton);
		
		pickLocationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Overlay overlay = new Overlay(width, height);
				setVisible(false);
				overlay.addMouseListener(new MouseListener() {
		    		@Override
		    		public void mouseClicked(MouseEvent e) {
		    			textXPosition.setText(String.format("%d", (int)(MouseInfo.getPointerInfo().getLocation().getX())));
		    			textYPosition.setText(String.format("%d", (int)(MouseInfo.getPointerInfo().getLocation().getY())));
		    			setVisible(true);
		    			overlay.setVisible(false);
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent e) {
					}

					@Override
					public void mousePressed(MouseEvent e) {
					}

					@Override
					public void mouseReleased(MouseEvent e) {
					}
		    	});
		    	
			}//end action performed for set Location button
		}); //end action listener for set Location button
		
		//action listeners for setLocation radio buttons
		JRadioButton mousePosRButton = new JRadioButton("Use Mouse Position");
		mousePosRButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		mousePosRButton.setSelected(true);
		mousePosRButton.setBounds(12, 175, 139, 25);
		panel_1.add(mousePosRButton);
		
		JRadioButton setLocRButton = new JRadioButton("Set Location");
		setLocRButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		setLocRButton.setBounds(12, 205, 107, 25);
		panel_1.add(setLocRButton);
		
		ButtonGroup setLocation = new ButtonGroup();
				setLocation.add(setLocRButton);
				setLocation.add(mousePosRButton);
				
		setLocRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mousePosRButton.setSelected(false);
				pickLocationButton.setEnabled(true);
				textXPosition.setEnabled(true);
				textYPosition.setEnabled(true);
			}
		});
				
		mousePosRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			setLocRButton.setSelected(false);
			pickLocationButton.setEnabled(false);
			textXPosition.setEnabled(false);
			textYPosition.setEnabled(false);
			}
		});			
		
		//action listeners for repeatUntil Radio Buttons
		JRadioButton clickStopRButton = new JRadioButton("Stop button");
		clickStopRButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		clickStopRButton.setSelected(true);
		clickStopRButton.setBounds(279, 100, 127, 25);
		panel_1.add(clickStopRButton);
		
		JRadioButton computerTimeRButton = new JRadioButton("Computer Time");
		computerTimeRButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		computerTimeRButton.setBounds(279, 130, 127, 25);
		panel_1.add(computerTimeRButton);
		
		JRadioButton timeElapsedRButton = new JRadioButton("Time Elapsed");
		timeElapsedRButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		timeElapsedRButton.setBounds(279, 160, 109, 25);
		panel_1.add(timeElapsedRButton);
		
		JRadioButton numClicksRButton = new JRadioButton("Number of clicks");
		numClicksRButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		numClicksRButton.setBounds(279, 190, 127, 25);
		panel_1.add(numClicksRButton);

		ButtonGroup setRepeat = new ButtonGroup();
		setRepeat.add(clickStopRButton);
		setRepeat.add(computerTimeRButton);
		setRepeat.add(numClicksRButton);
		setRepeat.add(timeElapsedRButton);
		
		
		
		textTimeElapsed = new JTextField();
		textTimeElapsed.setEnabled(false);
		textTimeElapsed.setText("HH:MM:SS.xxx");
		textTimeElapsed.setBounds(409, 162, 116, 20);
		panel_1.add(textTimeElapsed);
		textTimeElapsed.setColumns(10);
		
		JButton setHotkeyButton = new JButton("Set Hotkey");
		setHotkeyButton.setBounds(279, 247, 117, 23);
		panel_1.add(setHotkeyButton);
		
		JButton openAdvClickerButton = new JButton("Advanced Clicker");
		openAdvClickerButton.setBounds(408, 247, 117, 23);
		panel_1.add(openAdvClickerButton);
		
		JSeparator separator1 = new JSeparator();
		separator1.setBounds(12, 72, 513, 2);
		panel_1.add(separator1);
		
		JSeparator separator2 = new JSeparator();
		separator2.setBounds(12, 285, 513, 2);
		panel_1.add(separator2);
		
		JSeparator separator3 = new JSeparator();
		separator3.setOrientation(SwingConstants.VERTICAL);
		separator3.setBounds(269, 78, 2, 201);
		panel_1.add(separator3);
		
		JSeparator separator4 = new JSeparator();
		separator4.setBounds(12, 138, 251, 2);
		panel_1.add(separator4);
		
		JSeparator separator5 = new JSeparator();
		separator5.setBounds(275, 230, 250, 2);
		panel_1.add(separator5);
		
		clickStopRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textRepeatComputerTime.setEnabled(false);
				textNumClicks.setEnabled(false);
				textTimeElapsed.setEnabled(false);
			}
		});
		
		computerTimeRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textRepeatComputerTime.setEnabled(true);
				textNumClicks.setEnabled(false);
				textTimeElapsed.setEnabled(false);
			}
		});
		
		timeElapsedRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textRepeatComputerTime.setEnabled(false);
				textNumClicks.setEnabled(false);
				textTimeElapsed.setEnabled(true);
			}
		});
		
		numClicksRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textRepeatComputerTime.setEnabled(false);
				textNumClicks.setEnabled(true);
				textTimeElapsed.setEnabled(false);
			}
		});
		
		//action listener for "start" and "stop" buttons
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean numCheck = true;
				boolean locationCheck = true;
				boolean repeatTimeCheck = true;

				
				//get type of click and click modifier
					typeOfClick = (String)typeOfClickDropdown.getSelectedItem();
					clickModifier = (String) modifiersDropdown.getSelectedItem();
				
				//check time
				try {
					hours = Integer.parseInt(textFieldHours.getText());
					minutes = Integer.parseInt(textFieldMinutes.getText());
					seconds = Integer.parseInt(textFieldSeconds.getText());
					milliseconds = Integer.parseInt(textFieldMilliseconds.getText());
				} catch (NumberFormatException f) {
					JOptionPane.showMessageDialog(null, "Please enter valid numbers for click interval.");
					numCheck = false;
				}
				
				if (numCheck && hours == 0 && minutes == 0 && seconds == 0 && milliseconds == 0) {
					JOptionPane.showMessageDialog(null, "Your click interval may not be 0 milliseconds!");
					numCheck = false;
				}//end check time
				
				//check location
				try {
					if (setLocRButton.isSelected()) {
						xPosition = Integer.parseInt(textXPosition.getText());
						yPosition = Integer.parseInt(textYPosition.getText());
					}
				} catch (NumberFormatException f){
					JOptionPane.showMessageDialog(null, "Please enter valid numbers for click location.");
					locationCheck = false;
				}
				
				if (locationCheck && setLocRButton.isSelected()) {
					if (xPosition > width || yPosition > height) {
						JOptionPane.showMessageDialog(null, "Please enter a location inside your screen." + "\n Your screen size is " + width + " x " + height);
						locationCheck = false;
					}
				}
				
				//end check location

				//check repeatTime
				if (computerTimeRButton.isSelected()) { //if computer time option is selected
					if (textRepeatComputerTime.getText().length() == 8 && textRepeatComputerTime.getText().substring(2, 3).equals(":") && textRepeatComputerTime.getText().substring(5, 6).equals(":")) {
						try {
							computerHours = Integer.parseInt(textRepeatComputerTime.getText().substring(0, 2));
							computerMinutes = Integer.parseInt(textRepeatComputerTime.getText().substring(3, 5));
							computerSeconds = Integer.parseInt(textRepeatComputerTime.getText().substring(6));
						} catch (NumberFormatException f) {
							JOptionPane.showMessageDialog(null, "Please enter a valid time for computer time when autoclicker stops.");
							repeatTimeCheck = false;
						}
					} else {
						JOptionPane.showMessageDialog(null, "Please enter a valid time for computer time when autoclicker stops.");
						repeatTimeCheck = false;
					}
					//check if numbers are an actual time
					if (computerHours > 23 || computerMinutes > 59 || computerSeconds > 59) {
						JOptionPane.showMessageDialog(null, "Please enter a valid time for computer time when autoclicker stops.");
						repeatTimeCheck = false;
					}
				} else if (timeElapsedRButton.isSelected()) { //if time elapsed option is selected
					if (textTimeElapsed.getText().length() == 12 && textTimeElapsed.getText().substring(2, 3).equals(":") && textTimeElapsed.getText().substring(5, 6).equals(":") && textTimeElapsed.getText().substring(8, 9).equals(".")) {
						try {
							timeElapsed = Integer.parseInt(textTimeElapsed.getText().substring(0, 2)) * 3600000 + Integer.parseInt(textTimeElapsed.getText().substring(3, 5)) * 60000 + Integer.parseInt(textTimeElapsed.getText().substring(6, 8)) * 1000 + Integer.parseInt(textTimeElapsed.getText().substring(9));
						} catch (NumberFormatException f) {
							JOptionPane.showMessageDialog(null, "Please enter a valid time for time elapsed before autoclicker stops.");
							repeatTimeCheck = false;
						}
					} else {
						JOptionPane.showMessageDialog(null, "Please enter a valid time for time elapsed before autoclicker stops.");
						repeatTimeCheck = false;
					}
					//check if numbers are an actual time
					try {
						if (repeatTimeCheck == true && (Integer.parseInt(textTimeElapsed.getText().substring(3, 5)) > 59 || Integer.parseInt(textTimeElapsed.getText().substring(6, 8)) > 59)) {
							JOptionPane.showMessageDialog(null, "Please enter a valid time for time elapsed before autoclicker stops.");
							repeatTimeCheck = false;
						}
					} catch (NumberFormatException f) {
						JOptionPane.showMessageDialog(null, "Please enter a valid time for time elapsed before autoclicker stops.");
						repeatTimeCheck = false;
					}
				} else if (numClicksRButton.isSelected()) {//if number of clicks option is selected
					try {
						numClicks = Integer.parseInt(textNumClicks.getText());
					} catch (NumberFormatException f) {
						JOptionPane.showMessageDialog(null, "Please enter a valid number of clicks before autoclicker stops.");
						repeatTimeCheck = false;
					}
					if (repeatTimeCheck && numClicks == 0) {
						JOptionPane.showMessageDialog(null, "You may not stop the autoclicker after 0 clicks!");
						repeatTimeCheck = false;
					}
				} else { //if click until stopped is selected
					numClicks = Integer.MAX_VALUE;
				}
				
				//start
				if (numCheck == true && locationCheck == true && repeatTimeCheck == true) {
					stopButton.setEnabled(true);
					startButton.setEnabled(false);
					//for testing
					
					//convert click interval into milliseconds
					clickInterval = milliseconds + seconds * 1000 + minutes * 60000 + hours * 3600000;
					
					if (setLocRButton.isSelected()) {
						System.out.println("Location: " + xPosition + " " + yPosition);
					}
					if (computerTimeRButton.isSelected()) {
						int secondStopOTD = computerHours * 3600 + computerMinutes * 60 + computerSeconds;
						LocalDateTime now = LocalDateTime.now();
						int secondNowOTD = now.getHour() * 3600 + now.getMinute()*60 + now.getSecond();
						if (secondStopOTD < secondNowOTD) {
							numClicks = ((secondStopOTD - secondNowOTD + 86400) * 1000) / clickInterval;
						} else if (secondStopOTD > secondNowOTD) {
							numClicks = ((secondStopOTD - secondNowOTD) * 1000) / clickInterval;
						} else {
							numClicks = 86400000 / clickInterval;
						}
						System.out.println("Stop at computer time: " + computerHours + ":" + computerMinutes + ":" + computerSeconds + " = " + numClicks + " clicks");
					}
					if (timeElapsedRButton.isSelected()) {
						//convert to number of clicks
						numClicks = timeElapsed / clickInterval;
						System.out.println("Stop after " + timeElapsed + " milliseconds" + " = " + numClicks);
					}
					if (numClicksRButton.isSelected()) {
						System.out.println("Stop after clicking " + numClicks + " times");
					}
					
					System.out.println("Type of click: " + typeOfClick);
					System.out.println("Click modifiers: " + clickModifier);
					System.out.println("Time: " + hours + " " + minutes + " " + seconds + " "+ milliseconds + "\n Total time: " + clickInterval);
					
					//disable all controls
					textFieldHours.setEnabled(false);
					textFieldMinutes.setEnabled(false);
					textFieldSeconds.setEnabled(false);
					textFieldMilliseconds.setEnabled(false);
					typeOfClickDropdown.setEnabled(false);
					modifiersDropdown.setEnabled(false);
					mousePosRButton.setEnabled(false);
					setLocRButton.setEnabled(false);
					pickLocationButton.setEnabled(false);
					textXPosition.setEnabled(false);
					textYPosition.setEnabled(false);
					clickStopRButton.setEnabled(false);
					computerTimeRButton.setEnabled(false);
					timeElapsedRButton.setEnabled(false);
					numClicksRButton.setEnabled(false);
					textRepeatComputerTime.setEnabled(false);
					textTimeElapsed.setEnabled(false);
					textNumClicks.setEnabled(false);
					
					isRunning = true;
				}//end start
				
				/**initialize and start clicker
					Instantiates a Clicker or DoubleClicker based on click type.
				*/
				//set button
				if (typeOfClick.equals("Left")) {
					button = 1;
				} else if (typeOfClick.equals("Middle")) {
					button = 2;
				} else {
					button = 3;
				}
				
				if (isRunning) {
					if (setLocRButton.isSelected() == false) { //if location not chosen
						Clicker = new Clicker(clickInterval, button, numClicks, clickModifier);
						Clicker.start();
					} else { //if location chosen
						Clicker = new Clicker(clickInterval, button, xPosition, yPosition, numClicks, clickModifier);
						Clicker.start();
					}
				} 
				
			} //end startButton action performed
		}); //end startButton action listener
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
				System.out.println("Program stopped");
				
				//enable all controls
				textFieldHours.setEnabled(true);
				textFieldMinutes.setEnabled(true);
				textFieldSeconds.setEnabled(true);
				textFieldMilliseconds.setEnabled(true);
				typeOfClickDropdown.setEnabled(true);
				modifiersDropdown.setEnabled(true);
				mousePosRButton.setEnabled(true);
				setLocRButton.setEnabled(true);
				
				if (setLocRButton.isSelected()) {
					textXPosition.setEnabled(true);
					textYPosition.setEnabled(true);
					pickLocationButton.setEnabled(true);
				}
				
				clickStopRButton.setEnabled(true);
				computerTimeRButton.setEnabled(true);
				timeElapsedRButton.setEnabled(true);
				numClicksRButton.setEnabled(true);
				
				if (computerTimeRButton.isSelected()) {
					textRepeatComputerTime.setEnabled(true);
				} else if (timeElapsedRButton.isSelected()) {
					textTimeElapsed.setEnabled(true);
				} else if (numClicksRButton.isSelected()) {
					textNumClicks.setEnabled(true);
				}
				
				try {
				Clicker.stop();
				isRunning = false;
				} catch (NullPointerException g){
				
				}
			} //end stop button action performed
		}); //end stop button action listener
	}//end constructor
	
	public String toString() {
		return "This works";
	}
	//getters
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public int getMilleseconds() {
		return milliseconds;
	}
	
	public String getTypeOfClick() {
		return typeOfClick;
	}
	
	public String getClickModifier() {
		return clickModifier;
	}
	
	public int getXPosition() {
		return xPosition;
	}
	
	public int getYPosition() {
		return yPosition;
	}
	
	public int getComputerHours() {
		return computerHours;
	}
	
	public int getComputerMinutes() {
		return computerMinutes;
	}
	
	public int getComputerSeconds() {
		return computerSeconds;
	}
	
	public int getTimeElapsed() {
		return timeElapsed;
	}
	
	public int getNumClicks() {
		return numClicks;
	}
	
	public JButton getStopButton() {
		return stopButton;
	}
}//end class
