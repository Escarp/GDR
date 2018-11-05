package control;

import model.MapParser;
import model.TileSet ;
import view.View;

public class Main {
	public static void main( String[] args ) {
		MapParser mp = new MapParser( "/map.txt" ) ;
		View v = new View() ;
		TileSet t = new TileSet( 32 , 32 , "/TestImg.png" ) ;
		
		v.createMap( mp.getMap() , t.getTiles() ) ;
		
		/* XXX DEBUG XXX
		v.printMap( mp.getMap() ) ;
		v.saveTilesetToImg( t.getTiles() ) ;*/
		
	}
}
