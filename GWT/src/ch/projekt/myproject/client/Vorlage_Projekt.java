package src.ch.projekt.myproject.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Vorlage_Projekt implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Button ButtonSingleplayer = new Button("Singleplayer");
		Button ButtonMultiplayer = new Button("Multiplayer");
		Button ButtonOptions = new Button("Optionen");
		Button ButtonProfil = new Button("Profil");
		
		Label LabelGreeting = new Label("Hello *Username* we have missed your dearly");
		
		ButtonSingleplayer.setPixelSize(100, 50);
		ButtonMultiplayer.setPixelSize(100, 50);
		ButtonOptions.setPixelSize(100, 50);
		ButtonProfil.setPixelSize(100, 50);
		
		FlowPanel fp = new FlowPanel();
		
		ButtonSingleplayer.setFocus(true);
		
		ButtonSingleplayer.setStyleName("gwt-ButtonMyOwn");
		ButtonMultiplayer.setStyleName("gwt-ButtonMyOwn");
		ButtonOptions.setStyleName("gwt-ButtonMyOwn");
		ButtonProfil.setStyleName("gwt-ButtonMyOwn");
		
		fp.setStyleName("wrapper");
		
		RootPanel.get("LabelGreetingsContainer").add(LabelGreeting);
		RootPanel.get("ButtonContainerTop").add(ButtonProfil);
		RootPanel.get("ButtonContainerMiddle").add(ButtonMultiplayer);
		RootPanel.get("ButtonContainerMiddle").add(ButtonSingleplayer);
		RootPanel.get("ButtonContainerFoot").add(ButtonOptions);
		
		RootPanel.get("Wrapper").add(fp);
		
		
	}
	
}


