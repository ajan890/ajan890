/**
 * @author Aidan Jan
 * @version 2.0: Apr. 4, 2021
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.awt.event.ActionEvent;

public class Interface {

	private JFrame frame;
	private JTextField timeInput;
	private JTextField notes;
	private JRadioButton inRButton; 
	private JRadioButton atRButton;
	private JButton startButton;
	private JButton stopButton;
	
	private int count;
	private int maxCount;
	private Timer timer;
	
	public Interface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Timer paused.");
		frame.setBounds(100, 100, 332, 137);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		timeInput = new JTextField();
		timeInput.setText("HH:MM:SS");
		timeInput.setBounds(121, 11, 86, 20);
		panel.add(timeInput);
		timeInput.setColumns(10);
		
		ButtonGroup setType = new ButtonGroup();
		
		inRButton = new JRadioButton("Reminder in...");
		inRButton.setSelected(true);
		inRButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		inRButton.setBounds(6, 10, 109, 23);
		panel.add(inRButton);
		
		atRButton = new JRadioButton("Reminder at...");
		atRButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		atRButton.setBounds(6, 38, 109, 23);
		panel.add(atRButton);
		
		setType.add(inRButton);
		setType.add(atRButton);
		
		startButton = new JButton("Start");
		startButton.setBounds(217, 10, 89, 23);
		panel.add(startButton);
		
		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
		stopButton.setBounds(217, 38, 89, 23);
		panel.add(stopButton);
		
		notes = new JTextField();
		notes.setBounds(6, 68, 300, 20);
		panel.add(notes);
		notes.setColumns(10);
		
		//start button actionListener
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int time = parseTime();
				boolean timeCheck = false;
				if (inRButton.isSelected()) {
					if (time == 0) {
						JOptionPane.showMessageDialog(null, "Please enter a time longer than 0 seconds.");
					} else if (time > 0) {
						timeCheck = true;
					}
				} else {
					if (time != -1) {
						timeCheck = true;
					}
				}
				if (timeCheck) {
					stopButton.setEnabled(true);
					startButton.setEnabled(false);
					inRButton.setEnabled(false);
					atRButton.setEnabled(false);
					timeInput.setEnabled(false);
					//start timer
					if (inRButton.isSelected()) {
						startTimerIn(time);
					}/*end if inRButton.isSelected()*/ else {
						LocalDateTime now = LocalDateTime.now();
						int computerHours = now.getHour();
						int computerMinutes = now.getMinute();
						int computerSeconds = now.getSecond();
						int waitTime = 0;
						int currentTime = computerHours * 3600 + computerMinutes * 60 + computerSeconds;
						
						if (time > currentTime) {
							waitTime = time - currentTime;
						} else if (time < currentTime){
							waitTime = time + 86400 - currentTime;
						}
						startTimerAt(waitTime);
					}/*end if atRButton.isSelected()*/
				}//end time check
			}//end actionPerformed
		});
		
		//stop button actionListener
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		
	}//end initialize()
	
	private void stop() {
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		inRButton.setEnabled(true);
		atRButton.setEnabled(true);
		timeInput.setEnabled(true);
		timer.stop();
		frame.setTitle("Timer paused.");
		
	}

	private void startTimerIn(int time) {
		maxCount = time - 1;
		count = 0;
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setTitle(convertToTimeFormat(count / 3600, (count % 3600) / 60, count % 60) + " / " + timeInput.getText() + " elapsed.");
				if (count > maxCount) {
					((Timer)e.getSource()).stop();
					frame.requestFocus();
					frame.toFront();
					if (notes.getText().length() == 0) { //if there is no notes
						if (JOptionPane.showConfirmDialog(null, timeInput.getText() + " has elapsed.\nPress OK to restart the timer, CANCEL to quit.", "Timer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == 0) {
							startTimerIn(time);
						} else {
							stop();
						}
					} else { //if there is notes
						if (JOptionPane.showConfirmDialog(null, timeInput.getText() + " has elapsed.\n" + notes.getText() + "\nPress OK to restart the timer, CANCEL to quit.", "Timer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == 0) {
							startTimerIn(time);
						} else {
							stop();
						}
					}
				}
				count++;
			}//end actionPerformed
		});//end timer actionListener
		timer.start();
	}//end startTimerIn method

	private void startTimerAt(int time) {
		System.out.println("started" + time);
		timer = new Timer(time * 1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.requestFocus();
				frame.toFront();
				if (notes.getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "It is now " + timeInput.getText(), "Timer", JOptionPane.INFORMATION_MESSAGE);
					stop();
				} else {
					JOptionPane.showMessageDialog(null, "It is now " + timeInput.getText() + notes.getText(), "Timer", JOptionPane.INFORMATION_MESSAGE);
					stop();
				}
			}//end actionPerformed
		});//end ActionListener
		timer.start();
		frame.setTitle("Waiting for " + timeInput.getText());
	}//end method startTimerAt
	//returns the time in SECONDS user input into timeInput.  returns -1 if time is invalid or 0.
	private int parseTime() {
		String text = timeInput.getText();
		int time = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		boolean end = false;
		
		if  (text.length() != 8 || text.indexOf(":") != 2 || text.indexOf(":", 3) != 5) {
			JOptionPane.showMessageDialog(null, "Please use the time format \"HH:MM:SS\"");
			end = true;
		} else {
			try {
				hours = Integer.parseInt(text.substring(0, 2));
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter a valid number of hours.");
				end = true;
			}
			
			if (!end) {
				try {
					minutes = Integer.parseInt(text.substring(3, 5));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Please enter a valid number of minutes.");
					end = true;
				}
			}
			
			if (!end) {
				try {
					seconds = Integer.parseInt(text.substring(6));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Please enter a valid number of seconds");
				}
			}
			
			time = hours * 3600 + minutes * 60 + seconds;			
		}//end format check if-else
		
		if (end) {
			time = -1;
		} 
		
		return time;
	}
	
	private String convertToTimeFormat(int hours, int minutes, int seconds) {
		minutes += seconds / 60;
		seconds %= 60;
		hours += minutes / 60;
		minutes %= 60;
		
		String stringSeconds = String.valueOf(seconds);
		String stringMinutes = String.valueOf(minutes);
		String stringHours = String.valueOf(hours);
		
		if (seconds < 10) {
			stringSeconds = "0" + String.valueOf(seconds);
		}
		if (minutes < 10) {
			stringMinutes = "0" + String.valueOf(minutes);
		}
		if (hours < 10) {
			stringHours = "0" + String.valueOf(hours);
		}
		
		return stringHours + ":" + stringMinutes + ":" + stringSeconds;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}//end method main
}//end class
