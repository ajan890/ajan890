import java.nio.file.*;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; 
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.util.Scanner;


public class Archiver {
		
	private static String srcPath;
	private static String destPath;
	private static Scanner scanner;
	
	public static String getDate() {
	DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		return date.format(now);
	} //end getDate
	
	public static void createFile(String path) {
		File filesArchived = new File(path + "\\" + getDate() + " archives");
		filesArchived.mkdir();
	}//end createFile
	
	public static boolean moveFiles(String name) {
		boolean isSuccess = true;
		try {
			Path temp = Files.move(Paths.get(srcPath + "\\" + name), Paths.get(destPath + "\\" + getDate() + " archives\\" + name));
			if (temp == null) {
			isSuccess = false;
			}
		} catch (IOException e) {
		}//end try-catch
	return isSuccess;
	}//end moveFiles
	

	public static File[] getFileNames() {
		File folder = new File(srcPath);
		File[] fileList = folder.listFiles();
	return fileList;
	}//end getFileNames
	
	public static void main (String[] args) {
		File dataFolder = new File("C:\\Auto-Archiver");
		dataFolder.mkdir();
		try {
			File data = new File("C:\\Auto-Archiver\\data.txt");
			if (data.createNewFile()) {
				System.out.println("File created");
				ArchiverFrame frame = new ArchiverFrame();
				frame.setVisible();
			} else {
				scanner = new Scanner(data);
				String temp = scanner.nextLine();
				srcPath = new String(temp.substring(temp.indexOf("<") + 1, temp.indexOf(">")));
				String temp2 = scanner.nextLine();
				destPath = new String(temp2.substring(temp2.indexOf("<") + 1, temp2.indexOf(">")));
				
				System.out.println(srcPath);
				System.out.println(destPath);
			}
		} catch (IOException e) {
			System.out.println("File failed to create");
		}
		
		//create destination folder (if it doesn't exist)
		createFile(destPath);
		File[] fileNames = getFileNames();
		int successCounter = 0;
		
		//move files
		for (int i = 0; i < fileNames.length; i++) {
			if (moveFiles(fileNames[i].getName())) {
				successCounter++;
			}
		} //end for
		
		//check of operation worked
		if (successCounter == fileNames.length) {
			JOptionPane.showMessageDialog(null, "Successfully archived " + successCounter + " files.");
		} else {
			JOptionPane.showMessageDialog(null, "Failed to transfer " + (fileNames.length - successCounter) + " files.");
		}
	
	}//end main 
}//end class
