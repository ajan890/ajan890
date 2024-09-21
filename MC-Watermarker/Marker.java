/**
	@version 1.0 - December 1, 2021
	@author Aidan Jan
	Marker.java
*/

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Marker {
	private Picture image;
	private Picture watermark;
	private double opacity;
	private int location;
	private int startX;
	private int startY;
	private float finalOpacity;
	private double cornersSize;
	private double centerSize;
	
	
	public Marker(BufferedImage inputImage, BufferedImage inputWatermark, int[] settings) {
		System.out.println("Initialized!");
		this.opacity = settings[0] / 100.0;
		this.location = settings[1];
		this.finalOpacity = (float) (settings[2] / 100.0);
		System.out.println("FinalOpacity:" + finalOpacity);
		this.cornersSize = settings[3] / 100.0;
		System.out.println("Corners Size: " + cornersSize);
		this.centerSize = settings[4] / 100.0;
		System.out.println("Centers Size: " + centerSize);
		BufferedImage resized;
		int targetWidth;
		int targetHeight;
		double imageRatio = inputImage.getWidth() / inputImage.getHeight();
		double watermarkRatio = inputWatermark.getWidth() / inputWatermark.getHeight();		
		
		//TODO: rewrite to force image to always fit.

		
		
		//resize watermark
		if (imageRatio < watermarkRatio) {
			//resize so widths are equal
			targetWidth = inputImage.getWidth();
			targetHeight = (int)(((double) inputImage.getWidth() / inputWatermark.getWidth()) * inputWatermark.getHeight());
			if (location != 5) {
				targetWidth *= cornersSize;
				targetHeight *= cornersSize;
			} else {
				targetWidth *= centerSize;
				targetHeight *= centerSize;
			}
		} else {
			//resize so heights are equal
			targetHeight = inputImage.getHeight();
			targetWidth = (int)(((double) inputImage.getHeight() / inputWatermark.getHeight()) * inputWatermark.getWidth());
			if (location != 5) {
				targetWidth *= cornersSize;
				targetHeight *= cornersSize;
			} else {
				targetWidth *= centerSize;
				targetHeight *= centerSize;
			}
		}
		resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(inputWatermark, 0, 0, targetWidth, targetHeight, null);
		g2d.dispose();
	
		image = new Picture(inputImage);
		watermark = new Picture(resized);

	}
	
	public BufferedImage mark() throws IOException {		
		BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		System.out.println("Image dimensions: " + image.getWidth() + " x " + image.getHeight());
		System.out.println("Watermark dimension: " + watermark.getWidth() + " x " + watermark.getHeight());
		//define where the array should start filling in the watermark
		switch (location) {
		case 1: startX = 0; startY = 0; break; //TL
		case 2: startX = image.getWidth() - watermark.getWidth(); startY = 0; break; //TR
		case 3: startX = 0; startY = image.getHeight() - watermark.getHeight(); break; //BL
		case 4: startX = image.getWidth() - watermark.getWidth(); startY = image.getHeight() - watermark.getHeight(); break; //BR
		case 5: startX = image.getWidth() / 2 - watermark.getWidth() / 2; startY = image.getHeight() / 2 - watermark.getHeight() / 2; break;//Center
		} //end of switch
		for (int i = 0; i < watermark.getHeight(); i++) {
			for (int j = 0; j < watermark.getWidth(); j++) {
				image.getPixel(startX + j, startY + i).addColor(watermark.getPixel(j, i), opacity);
			}
		}
		//convert each pixel into RGB
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				output.setRGB(j, i, image.getPixel(j, i).getRGB());
			}
		}

		//opacity
		BufferedImage translucentImage = new BufferedImage(output.getWidth(), output.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = translucentImage.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, finalOpacity));
		g2d.drawImage(output, 0, 0, output.getWidth(), output.getHeight(), null);
		g2d.dispose();
		return translucentImage;
	}
}
