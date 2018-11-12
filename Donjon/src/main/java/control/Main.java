package control;

import java.util.Properties;
import view.View;

public class Main {
	public static Properties properties = null ;
	
	public static void initProperties(){
		properties = new Properties() ;
		
		try {
			properties.load( Main.class.getClassLoader().getResourceAsStream( 
					"project.properties" ) ) ;
		}
		catch ( Exception e ) {
			View.printErr( "Main: initProperties" , e ) ;
		}
	}
	
	public static void main( String[] args ) {
		View v = new View() ;
		
		initProperties() ;
		
		v.setArtifact(	properties.getProperty( "artifactId" ) ) ;
		v.setVersion(	properties.getProperty( "version" ) ) ;
		
		v.startWindow() ;
	}
}
