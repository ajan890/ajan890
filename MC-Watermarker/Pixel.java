/**
	@version 1.0 - December 1, 2021
	@author Aidan Jan
	Pixel.java
*/

import java.awt.Color;
import java.awt.image.BufferedImage;


public class Pixel {
	private int red;
	private int green;
	private int blue;
	private int alpha;

	public Pixel(BufferedImage image, int x, int y) {
		Color color = new Color(image.getRGB(x, y), true);
		this.red = color.getRed();
		this.green = color.getGreen();
		this.blue = color.getBlue();
		this.alpha = color.getAlpha();
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	
	public int getAlpha() {
		return alpha;
	}
	
	public double getOpacity() {
		return alpha / 255.0;
	}
	
	public void setColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public void setColor(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	
	public void addColor(int red, int green, int blue, int alpha, double opacity) {
		double finalOpacity = opacity * (alpha / 255.0); 
		this.red = (int)((double) this.red * (1 - finalOpacity) + (double) red * finalOpacity);
		this.green = (int)((double) this.green * (1 - finalOpacity) + (double) green * finalOpacity);
		this.blue = (int)((double) this.blue * (1 - finalOpacity)+ (double) blue * finalOpacity);
	}
	
	public void addColor(Pixel pixel, double opacity) {
		double finalOpacity = opacity * pixel.getOpacity(); 
		this.red = (int)((double) this.red * (1 - finalOpacity) + (double) pixel.getRed() * finalOpacity);
		this.green = (int)((double) this.green * (1 - finalOpacity) + (double) pixel.getGreen() * finalOpacity);
		this.blue = (int)((double) this.blue * (1 - finalOpacity)+ (double) pixel.getBlue() * finalOpacity);
	}
	
	public int getRGB() {
		return new Color(red, green, blue).getRGB();
	}
}