package control;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
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
	
	static String artifact = null ;
	static String version = null ;
	
	static File fileToConvert ;
	static File tileSet ;
	
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
			
			//TITLE COMPONENT TODO:ADD VERSION CONTROL
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
					//View.printMsgln( input.toString() ) ;
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
							.setDescription( "Choose one of the files below" )
							.setActionLabel( "Open" )
							.build()
							.showDialog( textGUI ) ;
					//View.printMsgln( input.toString() ) ;
					tileSet = input ;
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
//						if( fileToConvert == null ){
//							View.printMsgln( "File null!" ) ;
//						}
//						if( tileSet == null ){
//							View.printMsgln( "TileSet null!" ) ;
//						}
					}
					else{
						TileSet ts = new TileSet( 32 , 32 , tileSet ) ;
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
		} catch (IOException e) {
			View.printErr( "Main: main" , e ) ;
		}
		
		startWindow() ;
	}
}
