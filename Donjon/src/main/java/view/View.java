package view;

import java.awt.Graphics ;
import java.awt.image.BufferedImage ;
import java.io.File ;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO ;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.FileDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorColorConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorPalette;

import model.MapParser;
import model.TileSet;
import utils.MapConstants ;
import utils.TileConstants ;

public class View {
	
	static File fileToConvert ;
	static File tileSet ;
	
	static int tile_w = 32 ;
	static int tile_h = 32 ;
	
	private String artifact	= null ;
	private String version	= null ;
	
	public void setArtifact( String artifact ) {
		this.artifact = artifact ;
	}
	
	public void setVersion( String version ) {
		this.version = version ;
	}
	
	public void startWindow(){
		try{
			//TERMINAL CONFIG
			Terminal terminal = new DefaultTerminalFactory()
					.setTerminalEmulatorTitle( artifact + " " + version )
					.setTerminalEmulatorColorConfiguration( 
							TerminalEmulatorColorConfiguration.newInstance( 
									TerminalEmulatorPalette.GNOME_TERMINAL ) )
					.createTerminal() ;
			
			//SCREEN CONFIG
			Screen screen = new TerminalScreen( terminal ) ;
			screen.startScreen() ;
			final WindowBasedTextGUI textGUI = 
					new MultiWindowTextGUI( screen ) ;
			
			//PANEL CONFIG
			Panel panel = new Panel() ;
			
			//TITLE COMPONENT
			panel.addComponent( new Label( "Welcome to " + artifact + " " 
					+ version + " ! " ).setLayoutData( 
							LinearLayout.createLayoutData( 
									LinearLayout.Alignment.Center ) )
					.addStyle( SGR.BLINK ) ) ;
			
			//DESCRIPTION COMPONENT
			panel.addComponent( new Label( "Start by selecting a file and a "
					+ "tileset, then press \"Convert!\"" ).setLayoutData( 
							LinearLayout.createLayoutData( 
									LinearLayout.Alignment.Center ) )
					.addStyle( SGR.BOLD ) ) ;
			
			//EMPTY SPACE COMPONENT
			panel.addComponent( new EmptySpace() ) ;
			
			//CHOOSE MAP COMPONENT
			panel.addComponent( new Button( "Choose file to convert" , 
					new Runnable(){
				@Override
				public void run() {
					File input = new FileDialogBuilder()
							.setTitle( "Open file" )
							.setDescription( "Choose one of the files below" )
							.setActionLabel( "Open" )
							.build()
							.showDialog( textGUI ) ;
					fileToConvert = input ;
				}
			}).setLayoutData( LinearLayout.createLayoutData( 
					LinearLayout.Alignment.Center ) ) ) ;
			
			//CHOOSE TILESET COMPONENT
			panel.addComponent( new Button( "Choose tileset" , 
					new Runnable(){
				@Override
				public void run() {
					File input = new FileDialogBuilder()
							.setTitle( "Open file" )
							.setDescription( "Choose one of the files below"
									+ "pixels tilesets ;)" )
							.setActionLabel( "Open" )
							.build()
							.showDialog( textGUI ) ;
					tileSet = input ;
				}
			}).setLayoutData( LinearLayout.createLayoutData( 
					LinearLayout.Alignment.Center ) ) ) ;
			
			//SET TILE DIMENSIONS
			panel.addComponent( new Button( "Set tile dimensions" , 
					new Runnable() {
				@Override
				public void run() {
					BasicWindow window = new BasicWindow() ;
					Panel panel = new Panel() ;
					
					window.setTitle( "Set tile dimensions" ) ;
					
					window.setHints( Arrays.asList( 
							Window.Hint.CENTERED ) ) ;
					
					panel.addComponent( new Label( 
							"Leave blank for default 32x32" ) ) ;
					
					panel.addComponent( new Label( "Tile width" )
							.setLayoutData( LinearLayout.createLayoutData( 
							LinearLayout.Alignment.Center ) ) ) ;
					
					final TextBox t_width = new TextBox()
							.setValidationPattern( Pattern.compile( "[0-9]*" ) )
							.setLayoutData( LinearLayout.createLayoutData( 
									LinearLayout.Alignment.Center ) )
							.addTo( panel ) ;
					
					panel.addComponent( new Label( "Tile height" )
							.setLayoutData( LinearLayout.createLayoutData( 
							LinearLayout.Alignment.Center ) ) ) ;
					
					final TextBox t_height = new TextBox()
							.setValidationPattern( Pattern.compile( "[0-9]*" ) )
							.setLayoutData( LinearLayout.createLayoutData( 
									LinearLayout.Alignment.Center ) )
							.addTo( panel ) ;
					panel.addComponent( new Button( "Done" , 
							new Runnable() {
						@Override
						public void run() {
							if( t_width.getText() != null 
									&& !t_width.getText().equals( "" ) ){
								tile_w = Integer.parseInt( 
										t_width.getText() ) ;
							}
							else{
								tile_w = 32 ;
							}
							
							if ( t_height.getText() != null 
									&& !t_height.getText().equals( "" ) ) {
								tile_h = Integer.parseInt( 
										t_height.getText() ) ;
							}
							else{
								tile_h = 32 ;
							}
							
							window.close() ;
						}
					}).setLayoutData( LinearLayout.createLayoutData( 
							LinearLayout.Alignment.End ) ) ) ;
					window.setComponent( panel ) ;
					textGUI.addWindowAndWait( window ) ;
				}
			}).setLayoutData( LinearLayout.createLayoutData( 
					LinearLayout.Alignment.Center ) ) ) ;
			
			//CONVERT BUTTON COMPONENT
			panel.addComponent( new Button( "Convert!" , new Runnable(){

				@Override
				public void run() {
					if( fileToConvert == null || tileSet == null ){
						MessageDialog.showMessageDialog( 
								textGUI , 
								"Error!" , 
								"Choose a tileset and a file to convert!" , 
								MessageDialogButton.OK ) ;
					}
					else{
							//Change
						TileSet ts = new TileSet( tile_w , tile_h , tileSet ) ;
						MapParser mp = new MapParser() ;
						mp.setFile( fileToConvert ) ;
						if( mp.convert() ) {
							String path = View.createMap( 
									mp.getMap() , 
									ts.getTiles() ) ;
							if( path != null ){
								MessageDialog.showMessageDialog( 
										textGUI , 
										"Success!" , 
										"Map created in: \n"
										+ path , 
										MessageDialogButton.OK ) ;
							}
							else{
								MessageDialog.showMessageDialog( 
										textGUI , 
										"Error" , 
										"File was not created!" , 
										MessageDialogButton.OK ) ;
							}
						}
							
						else{
							MessageDialog.showMessageDialog( 
									textGUI , 
									"Error!" , 
									"Conversion Failed!" , 
									MessageDialogButton.OK ) ;
						}
					}
				}
			}).setLayoutData( LinearLayout.createLayoutData( 
					LinearLayout.Alignment.Center ) ) ) ;
			
			//EXIT BUTTON COMPONENT
			panel.addComponent( new Button( "Exit" , new Runnable(){
				@Override
				public void run() {
					System.exit( 1 ) ;
				}
			}).setLayoutData( LinearLayout.createLayoutData( 
					LinearLayout.Alignment.Center ) ) ) ;
			
			//WINDOW CONFIG
			final Window window = new BasicWindow() ;
			window.setHints( Arrays.asList( 
					Window.Hint.FULL_SCREEN ) ) ;
			window.setComponent( panel ) ;
			textGUI.addWindowAndWait( window ) ;
		}
		catch( Exception e ) {
			View.printErr( "View: startWindow" , e ) ;
		}
	}
	
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
