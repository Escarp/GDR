package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import utils.MapConstants;

public class MapParser {
	private File file ;
	private List<String[]> map ;
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<String[]> getMap() {
		return map;
	}

	public void setMap(List<String[]> map) {
		this.map = map;
	}

	public MapParser(){
		
	}
	
	public MapParser( File file ) {
		this.file = file ;
		
		loadFile() ;
		
		toNormalChars() ;
		
		doubleUp() ;
		
		addWalls() ;
	}
	
	public boolean convert() {
		boolean success = false ;
		
		if( loadFile() ){
			
			toNormalChars() ;
			
			doubleUp() ;
			
			addWalls() ;
			
			success = true ;
		}
		return success ;
	}
	
	private boolean checkVoidTile( String pos ) {
		return !pos.equals(
						MapConstants.VOID)
				&& !pos.equals(
						MapConstants.WALL)
				&& !pos.equals(
						MapConstants.WALL_TOP_LEFT)
				&& !pos.equals(
						MapConstants.WALL_TOP)
				&& !pos.equals(
						MapConstants.WALL_TOP_RIGHT)
				&& !pos.equals(
						MapConstants.WALL_LEFT)
				&& !pos.equals(
						MapConstants.WALL_RIGHT)
				&& !pos.equals(
						MapConstants.WALL_BOTTOM_LEFT)
				&& !pos.equals(
						MapConstants.WALL_BOTTOM)
				&& !pos.equals(
						MapConstants.WALL_BOTTOM_RIGHT)
				&& !pos.equals(
						MapConstants.WALL_INNER_TOP_LEFT)
				&& !pos.equals(
						MapConstants.WALL_INNER_TOP_RIGHT)
				&& !pos.equals(
						MapConstants.WALL_INNER_BOTTOM_LEFT)
				&& !pos.equals(
						MapConstants.WALL_INNER_BOTTOM_RIGHT) ;
				
	}
	
	private void addWalls() {
		for( int y = 1 ; y < map.size() - 1 ; y++ ) {
			String[] temp = new String[map.get(y).length] ;
			
			String[] prevLine = map.get(y-1) ;
			String[] currentLine = map.get(y) ;
			String[] nextLine = map.get(y+1) ;
			
			for( int x = 1 ; x < map.get(y).length - 1 ; x++ ) {
				String current = currentLine[x] ;
				
				if( current.equals( MapConstants.VOID ) ){
					boolean topLeft 	= checkVoidTile( prevLine[ x - 1 ] ),
							top 		= checkVoidTile( prevLine[ x ] ),
							topRight 	= checkVoidTile( prevLine[ x + 1 ] ),
							left		= checkVoidTile( currentLine[ x - 1 ] ),
							right		= checkVoidTile( currentLine[ x + 1 ] ),
							bottomLeft	= checkVoidTile( nextLine[ x - 1 ] ),
							bottom		= checkVoidTile( nextLine[ x ] ),
							bottomRight	= checkVoidTile( nextLine[ x + 1 ] );
					
					if( top ) {
						if( left ) {
							current = MapConstants.WALL_INNER_TOP_LEFT ;
						}
						else if( right ) {
							current = MapConstants.WALL_INNER_TOP_RIGHT ;
						}
						else{
							current = MapConstants.WALL_BOTTOM ;
						}
					}
					else if( bottom ) {
						if( left ) {
							current = MapConstants.WALL_INNER_BOTTOM_LEFT ;
						}
						else if( right ) {
							current = MapConstants.WALL_INNER_BOTTOM_RIGHT ;
						}
						else{
							current = MapConstants.WALL_TOP ;
						}
					}
					else if( left ) {
						current =  MapConstants.WALL_RIGHT ;
					}
					else if( right ) {
						current =  MapConstants.WALL_LEFT ;
					}
					else if( topLeft ) {
						current =  MapConstants.WALL_BOTTOM_RIGHT ;
					}
					else if( topRight ) {
						current =  MapConstants.WALL_BOTTOM_LEFT ;
					}
					else if( bottomLeft ) {
						current =  MapConstants.WALL_TOP_RIGHT ;
					}
					else if( bottomRight ) {
						current =  MapConstants.WALL_TOP_LEFT ;
					}
					
				}
				temp[x] = current ;
			}
			temp[0] = " " ;
			temp[map.get(y).length-1] = " " ;
			map.set( y , temp ) ;
		}
	}
	
	private void doubleUp() {
		List<String[]> doubleMap = new ArrayList<String[]>() ;
		for( String[] line : map ){
			String[] doubled = new String[ line.length * 2 ] ;
			for( int i = 0 ; i < line.length ; i++ ){
				doubled[ i * 2 ] = line[i] ;
				doubled[ ( i * 2 ) + 1 ] = line[i] ;
			}
			doubleMap.add( doubled ) ;
			String[] temp = new String[ doubled.length ] ;
			for( int i = 0 ; i < doubled.length ; i++ ){
				temp[i] = doubled[i] ;
			}
			doubleMap.add( temp ) ;
		}
		
		map = doubleMap ;
	}
	
	private boolean loadFile() {
		File mapFile = file ;
		boolean success = false ;
		
		if( !mapFile.exists() ) {
			System.out.println( "file not found" );
		}
		else{
			TsvParserSettings settings = new TsvParserSettings() ;
			TsvParser parser = new TsvParser( settings ) ;
			map = parser.parseAll( mapFile ) ;
			success = true ;
		}
		
		return success ;
	}
	
	private void toNormalChars() {
		for( String[] line : map ) {
			for( int i = 0 ; i < line.length ; i++ ) {
				String temp = line[i] ;
				if( temp == MapConstants.DON_VOID ){
					temp = MapConstants.VOID ;
				}
				else if( temp.equals( MapConstants.DON_FLOOR ) ){
					temp = MapConstants.FLOOR ;
				}
				else if(
						temp.equals( MapConstants.DON_DOOR_TOP )
						|| temp.equals( MapConstants.DON_DOOR_BOTTOM )
						|| temp.equals( MapConstants.DON_DOOR_LEFT )
						|| temp.equals( MapConstants.DON_DOOR_RIGHT ) ){
					temp = MapConstants.DOOR ;
				}
				else if(
						temp.equals( MapConstants.DON_SECRET_DOOR_TOP )
						|| temp.equals( MapConstants.DON_SECRET_DOOR_BOTTOM )
						|| temp.equals( MapConstants.DON_SECRET_DOOR_LEFT )
						|| temp.equals( MapConstants.DON_SECRET_DOOR_RIGHT ) ){
					temp = MapConstants.SECRET ;
				}
				else if(
						temp.equals( MapConstants.DON_PORTCULLIS_TOP )
						|| temp.equals( MapConstants.DON_PORTCULLIS_BOTTOM )
						|| temp.equals( MapConstants.DON_PORTCULLIS_LEFT )
						|| temp.equals( MapConstants.DON_PORTCULLIS_RIGHT ) ){
					temp = MapConstants.PORTCULLIS ;
				}
				else if( 
						temp.equals( MapConstants.DON_STAIRS_UP )
						|| temp.equals( MapConstants.DON_STAIRS_UP_UP ) ){
					temp = MapConstants.STAIRS_UP ;
				}
				else if( 
						temp.equals( MapConstants.DON_STAIRS_DOWN )
						|| temp.equals( MapConstants.DON_STAIRS_DOWN_DOWN ) ){
					temp = MapConstants.STAIRS_DOWN ;
				}
				
				line[i] = temp ;
			}
		}
	}
}
