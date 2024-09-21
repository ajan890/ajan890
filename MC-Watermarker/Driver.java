/**
	@version 1.0 - December 1, 2021
	@author Aidan Jan
	Driver.java
*/


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import java.io.FileWriter;

public class Driver {
	private static Scanner scanner;
	private static String minecraftPath;
	
	public static void main(String[] args) {
		File dataFolder = new File("C:\\Watermarker");
		dataFolder.mkdir();
		try {
			File settings = new File("C:\\Watermarker\\settings.txt");
			if (settings.createNewFile()) {
				System.out.println("File created");
				minecraftPath = new String("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\.minecraft\\screenshots");
				if (!new File(minecraftPath).exists()) {
					JOptionPane.showMessageDialog(null, "Minecraft folder cannot be located.");
				}
				FileWriter fileWriter = new FileWriter(settings);
				fileWriter.write("WARNING: IT IS HIGHLY RECOMMENDED NOT TO EDIT THIS FILE DIRECTLY.\n" 
						+ "DOING SO MAY RESULT IN UNEXPECTED ERRORS.\n"
						+ "DELETE THIS FILE TO RESET THE PROGRAM.\n"
						+ "\n#SETTINGS\n"
						+ "Minecraft Path: <" + minecraftPath + ">\n"
						+ "Watermark Opacity: <100>\n"
						+ "Location: <4>\n"
						+ "Final Opacity: <100>\n"
						+ "Watermark Corners Size <10>\n"
						+ "Watermark Center Size <100>\n"
						+ "Timer: <5>");
				fileWriter.close();
				//show setup frame
				System.out.println("Welcome!");
				WelcomeInterface welcomeInterface = new WelcomeInterface();
				welcomeInterface.setVisible(true);
				welcomeInterface.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						System.exit(0);	
					}
				});
			} else {
				scanner = new Scanner(settings);
				String temp = scanner.nextLine();
				while(scanner.hasNextLine()) {
					if (temp.indexOf("#") != -1) {
						break;
					}
					temp = scanner.nextLine();
				}
				temp = scanner.nextLine();
			
				minecraftPath = new String(temp.substring(temp.indexOf("<") + 1, temp.indexOf(">")));
				Interface ui = new Interface();
				ui.setVisible(true);
				ui.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						System.exit(0);	
					}
				});
			}
		} catch (IOException e) {
			System.out.println("File failed to create");
		}

	}
	
	//rename this one to "main" to test the marker class by itself.
	public static void testMain(String[] args) throws IOException {
		File watermarkFile = new File("C:\\Watermarker\\watermark.png");
		File screenshotFile = new File("C:\\Watermarker\\screenshot.png");
		if (!watermarkFile.exists() || !screenshotFile.exists()) {
			JOptionPane.showMessageDialog(null, "No watermark/screenshot image found!\nImages will not be watermarked.");
			return;
		}
		BufferedImage watermark = ImageIO.read(watermarkFile);
		BufferedImage screenshot = ImageIO.read(screenshotFile);				
		
		int[] settings = new int[6];
		settings[0] = 100;//watermark opacity
		settings[1] = 4;//location
		settings[2] = 100;//final opacity
		settings[3] = 10;//corners
		settings[4] = 100;//center
		settings[5] = 5; //timer
		Marker marker = new Marker(screenshot, watermark, settings);
		BufferedImage marked = marker.mark();
		ImageIO.write(marked, "png", new File("C:\\Watermarker\\processed.png"));
	}
	
}
