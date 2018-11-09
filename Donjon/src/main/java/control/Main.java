package control;

import java.io.File;
import java.util.Arrays;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
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

import model.MapParser;
import model.TileSet;
import view.View;

public class Main {
	
	static File fileToConvert ;
	static File tileSet ;
	
	public static void startWindow(){
		try{
			Terminal terminal = new DefaultTerminalFactory().createTerminal() ;
			Screen screen = new TerminalScreen( terminal ) ;
			
			screen.startScreen() ;
			
			final WindowBasedTextGUI textGUI = 
					new MultiWindowTextGUI( screen ) ;
			
			Panel panel = new Panel() ;
			
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
					View.printMsgln( input.toString() ) ;
					fileToConvert = input ;
				}
				
			}) ) ;
			
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
					View.printMsgln( input.toString() ) ;
					tileSet = input ;
				}
				
			}) ) ;
			
			panel.addComponent( new Button( "Convert!" , new Runnable(){

				@Override
				public void run() {
					if( fileToConvert == null || tileSet == null ){
						MessageDialog.showMessageDialog( 
								textGUI , 
								"Error!" , 
								"Choose a tileset and a file to convert!" , 
								MessageDialogButton.OK ) ;
						if( fileToConvert == null ){
							View.printMsgln( "File null!" ) ;
						}
						if( tileSet == null ){
							View.printMsgln( "TileSet null!" ) ;
						}
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
										"Map created \n"
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
									"Oops!" , 
									"Something went wrong" , 
									MessageDialogButton.OK ) ;
						}
					}
				}
				
			}) ) ;
			
			panel.addComponent( new Button( "Exit" , new Runnable(){

				@Override
				public void run() {
					System.exit( 1 ) ;
				}
				
			}) ) ;
			final Window window = new BasicWindow() ;
			window.setHints( Arrays.asList( 
					Window.Hint.FULL_SCREEN
					, Window.Hint.NO_DECORATIONS ) ) ;
			
			window.setComponent( panel ) ;
			
			textGUI.addWindowAndWait( window ) ;
		}
		catch( Exception e ) {
			View.printErr( "View: startWindow" , e ) ;
		}
	}
	
	public static void main( String[] args ) {
		startWindow() ;
	}
}
