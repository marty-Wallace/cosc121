package image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import fileManagement.FileManager;
import gui.MosaicGUI;

public class Mosaic {
	
	private static final String TILE_PATH = "res/jpg";
	private static final float RED_WEIGHT = .6f;
	private static final float GREEN_WEIGHT = 1.0f;
	private static final float BLUE_WEIGHT = 1.0f;
	
	private BufferedImage image;
	private BufferedImage newImage;
	private Tile[][] mosaic;
	private List<Tile> tiles;
	MosaicGUI gui;

	public Mosaic(MosaicGUI gui){
		this.gui = gui;
		try {
			 this.tiles = FileManager.getImagesFromTiles(new File(TILE_PATH));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(gui.getFrame(), "Could not load tiles from the path " + TILE_PATH, "ERROR!" , JOptionPane.ERROR_MESSAGE);
			gui.enableButton(false);
		}
	}
	
	
	public BufferedImage createMosaic(BufferedImage image) {
		this.image = image;

		this.mosaic = new Tile[image.getWidth()/tiles.get(0).image.getWidth()][image.getHeight()/tiles.get(0).image.getHeight()];
		
		return createImage(tiles);
	}
	
	private BufferedImage createImage(List<Tile> tiles) {
		
		int tileHeight = tiles.get(0).image.getHeight();
		int tileWidth = tiles.get(0).image.getWidth();
		
		for(int i = 0; i < mosaic.length; i++) {
			for(int j = 0; j < mosaic[i].length; j ++) {
				Tile imagePiece = new Tile(image.getSubimage(i * tileWidth, j * tileHeight,  tileWidth, tileHeight));
				mosaic[i][j] = matchImagePiece(imagePiece, tiles);
			}
		}
		
		//int width = mosaic[0].length * mosaic[0][0].image.getWidth();
		//int height = mosaic.length * mosaic[0][0].image.getHeight();
		int width = mosaic.length * mosaic[0][0].image.getWidth();
		int height = mosaic[0].length * mosaic[0][0].image.getHeight();
		newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
        Color oldColor = g.getColor();//
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(oldColor);

		for(int i = 0; i < mosaic.length; i++){
			for(int j = 0; j < mosaic[0].length; j++) {
				g.drawImage(mosaic[i][j].image,null, i * mosaic[i][j].image.getWidth(), j * mosaic[i][j].image.getHeight());
			}
		}
        g.dispose();
        
        return newImage;
	}
	
	private Tile matchImagePiece(Tile imagePiece, List<Tile> tiles) {
		
		int targetBlue = imagePiece.averageBlue;
		int targetRed = imagePiece.averageRed;
		int targetGreen = imagePiece.averageGreen;
		
		for(Tile t : tiles) {
			scoreTileByAverage(t, targetBlue, targetRed, targetGreen);
		}
		Collections.sort(tiles);

		Tile ret = tiles.get(0);
		for(Tile t : tiles) {
			t.score = 0;
		}
		return ret;
	}

	
	private  void scoreTileByAverage(Tile tile, int targetBlue, int targetRed, int targetGreen) {
		
		int score = 0;
		score += (int) (Math.abs(tile.averageBlue - targetBlue) * BLUE_WEIGHT);
		score += (int)(Math.abs(tile.averageRed - targetRed) * RED_WEIGHT);
		score += (int)(Math.abs(tile.averageGreen - targetGreen) * GREEN_WEIGHT);
		
		
		tile.score = score;
	}

}