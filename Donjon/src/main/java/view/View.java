package view;

import java.awt.Graphics ;
import java.awt.image.BufferedImage ;
import java.io.File ;
import java.util.List;

import javax.imageio.ImageIO ;

import utils.MapConstants ;
import utils.TileConstants ;

public class View {
	public static void printMsgln( String message ) {
		System.out.println( message ) ;
	}
	
	public static void printMsgln() {
		System.out.println() ;
	}
	
	public static void printMsg( String message ) {
		System.out.print( message ) ;
	}
	
	public void printMap( List<String[]> map ) {
		for( String[] line : map ) {
			for( int i = 0 ; i < line.length ; i++ ) {
				printMsg( line[ i ] ) ;
			}
			printMsgln( "" ) ;
		}
	}
	
	public static String createMap( List<String[]> map , BufferedImage[] tiles ) {
		if( tiles == null || tiles.length <= 0 ) {
			printMsgln( "SaveFilesToImg: ERROR: No tiles loaded" ) ;
			return null ;
		}
		
		int type 		= tiles[0].getType() ;
		int tileWidth 	= tiles[0].getWidth() ;
		int tileHeight 	= tiles[0].getHeight() ;
		
		try {
			if( type == 0 ) {
				type = 5 ;
			}
			
			BufferedImage target = new BufferedImage( 
					tileWidth * map.get( 0 ).length ,
					tileHeight * map.size() ,
					type ) ;
			Graphics g = target.getGraphics() ;
			
			for( int y = 0 ; y < map.size() ; y++ ) {
				for( int x = 0 ; x < map.get(y).length ; x++ ) {
					String current = map.get( y )[x] ;
					
					switch( current ) {
						/*
						case() : {
							break ;
						}
						*/
						case( MapConstants.VOID ) : {
							break ;
						}
						case( MapConstants.WALL_TOP_LEFT ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_TOP_LEFT ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_TOP ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_TOP ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_TOP_RIGHT ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_TOP_RIGHT ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_LEFT ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_LEFT ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.FLOOR ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_CENTER ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_RIGHT ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_RIGHT ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_BOTTOM_LEFT ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_BOTTOM_LEFT ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_BOTTOM ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_BOTTOM ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_BOTTOM_RIGHT ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_BOTTOM_RIGHT ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_INNER_TOP_LEFT ) : {
							g.drawImage( 
									tiles[ TileConstants.TILE_INNER_TOP_LEFT ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_INNER_TOP_RIGHT ) : {
							g.drawImage( 
									tiles[ 
									       TileConstants.TILE_INNER_TOP_RIGHT 
									       ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_INNER_BOTTOM_LEFT ) : {
							g.drawImage( 
									tiles[ 
									       TileConstants.TILE_INNER_BOTTOM_LEFT 
									       ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						case( MapConstants.WALL_INNER_BOTTOM_RIGHT ) : {
							g.drawImage( 
									tiles[ 
									      TileConstants.TILE_INNER_BOTTOM_RIGHT 
									      ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
						default : {
							g.drawImage( 
									tiles[ TileConstants.TILE_CENTER ] , 
									x * tileWidth , 
									y * tileHeight , 
									null ) ;
							break ;
						}
					}
				}
			}
			File output = new File( "output.png" ) ;
			ImageIO.write( target , "png" , output ) ;
			
			return output.getAbsolutePath() ;
		} 
		catch( Exception e ) {
			printErr( "View: createMap" , e ) ;
		}
		return null ;
	}
	
	public static void printErr( String methodNameAndPath , Exception e ){
		printMsgln( methodNameAndPath + ": Error: " + e.getMessage() ) ;
		printMsg( "Stack: " ) ;
		for( int i = 0 ; i < e.getStackTrace().length ; i++ ) {
			printMsgln( "\t" + e.getStackTrace()[i] ) ;
		}
	}
}
