package de.philweb.bubblr.tools;



public interface IActivityRequestHandler {
	   
	public void swarmInit();
	public void openLink(String url);
	public void showToast(String string);
	public void showToast(String toastText, int duration);
	public void askSwarmLogin(String string, String string2);
	public void askExit(String dialogSubject, String dialogText);

	
}
