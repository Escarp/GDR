package control;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

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
import view.View;

public class Main {
	
	static String artifact	= null ;
	static String version	= null ;
	
	static File fileToConvert ;
	static File tileSet ;
	
	static int tile_w = 32 ;
	static int tile_h = 32 ;
	
	public static void startWindow(){
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
	
	public static void main( String[] args ) {
		final Properties properties = new Properties() ;
		try {
			properties.load( Main.class.getClassLoader().getResourceAsStream( 
					"project.properties" ) ) ;
			artifact = properties.getProperty( "artifactId" ) ;
			version = properties.getProperty( "version" ) ;
		}
		catch ( Exception e ) {
			View.printErr( "Main: main" , e ) ;
		}
		
		startWindow() ;
	}
}
