package model;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import view.View ;

public class TileSet {
	private File imageFile ;
	private BufferedImage image ;
	private BufferedImage[] tiles ;
	private int tileWidth ;
	private int tileHeight ;
	
	//Getters
	public File getImageFile() {
		return imageFile;
	}
	public BufferedImage getImage() {
		return image;
	}
	public BufferedImage[] getTiles() {
		return tiles;
	}
	public int getTileWidth() {
		return tileWidth;
	}
	public int getTileHeight() {
		return tileHeight;
	}
	
	//Setters
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public void setTiles(BufferedImage[] tiles) {
		this.tiles = tiles;
	}
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
	
	//Constructors
	public TileSet() {
	}
	
	public TileSet( int tileWidth , int tileHeight , File imageFile ) {
		this.tileWidth	= tileWidth ;
		this.tileHeight	= tileHeight ;
		this.imageFile	= imageFile ;
		
		loadTiles() ;
	}
	
	//Methods
	public void loadTiles() {
		int index = 0 ;
		int y = 0 ;
		int x = 0 ;
		
		try {
			image = ImageIO.read( imageFile ) ;
			int imageWidth	= image.getWidth() ;
			int imageHeight	= image.getHeight()	;
			
			tiles = new BufferedImage[ ( imageWidth / tileWidth )
			                           * ( imageHeight / tileHeight ) ] ;
			
			for( y = 0 ; y < imageHeight / tileWidth ; y++ ) {
				for( x = 0 ; x < imageWidth / tileHeight ; x++ ) {
					tiles[ index++ ] = image.getSubimage(
							x * tileWidth , 
							y * tileHeight , 
							tileWidth , 
							tileHeight ) ;
				}
			}
			
		}
		catch( Exception e ) {
			View.printErr( "TileSet: loadTiles" , e ) ;
		}
	}
}
