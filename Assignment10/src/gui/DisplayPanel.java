package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class DisplayPanel extends JComponent {

	private int x,y;
	
	private BufferedImage displayedImage;
	
	public DisplayPanel() {
		displayedImage = null; 
	}
	
	public void set(BufferedImage image) {
		
		
		if(image != null) {
				x = image.getWidth();
				y = image.getHeight();

			displayedImage = image;
			repaint();
		}
	}
	

	public void clearImage() {
		
		Graphics g = displayedImage.getGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, x, y);
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Dimension size = getSize();
		g.clearRect(0, 0, size.width, size.height);
		if(displayedImage != null) {
			g.drawImage(displayedImage, 0, 0, null);
		}
	}
}
