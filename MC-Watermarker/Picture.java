/**
	@version 1.0 - December 1, 2021
	@author Aidan Jan
	Picture.java
*/

import java.awt.image.BufferedImage;

public class Picture {
	private Pixel[][] picture;
	private int height;
	private int width;
	
	public Picture(BufferedImage image) {
		//save original picture in pixels
		picture = new Pixel[image.getHeight()][image.getWidth()];
		height = image.getHeight();
		width = image.getWidth();
		
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				picture[j][i] = new Pixel(image, i, j);
			}
		}
	}

	public Pixel getPixel(int x, int y) {
		return picture[y][x];
	}
	
	public int getHeight() {
		return height;
	}
		
	public int getWidth() {
		return width;
	}
	
}
