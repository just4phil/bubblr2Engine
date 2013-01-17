package de.philweb.bubblr;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.flurry.android.FlurryAgent;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmAchievement;
import com.swarmconnect.SwarmAchievement.GotAchievementsMapCB;
import com.swarmconnect.SwarmActiveUser;
import com.swarmconnect.SwarmLeaderboard;
import com.swarmconnect.SwarmLeaderboard.GotLeaderboardCB;
import com.swarmconnect.SwarmLeaderboardScore;
import com.swarmconnect.SwarmUser;
import com.swarmconnect.delegates.SwarmLoginListener;

import de.philweb.bubblr.screens.CoolMainMenu;
import de.philweb.bubblr.tools.IActivityRequestHandler;



public class BubblrAndroid extends AndroidApplication implements IActivityRequestHandler {

	
	// ================== THIS IS THE DEMO VERSION ================================================================
	
	public final boolean isDemoVersion = false; // true;	temporarily changed to free version without ads!
	final String PREFS_NAME = ".bubblrdemo";
	
	
	public Bubblr bubblr;
	
	Activity thisactivity;		// for interstitial ads
	
	public static Map<Integer, SwarmAchievement> achievements = new HashMap<Integer, SwarmAchievement>();	// f�r SWARM Achievements
	public static SwarmLeaderboard leaderboard;
    long score;
    int leaderboardID;
    SwarmLeaderboardScore highscore;
    SwarmUser loggedUser;
	
    
    //----- for Toasts -----
    public Context context = this; // = getApplicationContext();
    int duration;
	Toast toast;
    String toastText;
    
    //----- for sharing text to other apps
//    String shareText;
//    String shareTextSubject;
//    String shareHeadline;
//    Intent sendIntent;
	
    
    //----- for ads ---------------
    AdView adView;
    boolean adsEnabled = false;

    
    //---- for opening links to market and website
    String url;
    
    //--- for dialogs -------------------
    String dialogSubject;
    String dialogText;
    String dialogURL;
    AlertDialog messageBox;
    
    //------- showGameSolvedUploadDialog ---------------
    long points;
    
    
    String user;
    


    
//----------------------------------------------------------------------------------------


	
	
	private SwarmLoginListener mySwarmLoginListener = new SwarmLoginListener() {

		// This method is called when the login process has started
		// (when a login dialog is displayed to the user).
		public void loginStarted() {
		}

		// This method is called if the user cancels the login process.
		public void loginCanceled() {
		}

		// This method is called when the user has successfully logged in.
		public void userLoggedIn(SwarmActiveUser user) {
			
		    // Load achievements so they are 
		    // available when we need them later.
		    SwarmAchievement.getAchievementsMap(new GotAchievementsMapCB() {
		        public void gotMap(Map<Integer, SwarmAchievement> achievementsMap) {

		            // Store the map of achievements to be used later.
		            achievements = achievementsMap;
		        }
		    });
		    
		    
		    
		    SwarmLeaderboard.getLeaderboardById(leaderboardID, new GotLeaderboardCB() {
		        public void gotLeaderboard(SwarmLeaderboard lb) {
		            leaderboard = lb;
		        }
		    });
		  
		}

		// This method is called when the user logs out.
		public void userLoggedOut() {
		}

	};
//	//---------------------------------------------------------------------------------------- 
	


	//------------------------------------------------------------------
	
	GotLeaderboardCB callback = new GotLeaderboardCB() {
	    public void gotLeaderboard(SwarmLeaderboard leaderboard2) {

	    	if (leaderboard2 != null) {

	            // Save the leaderboard for later use
	            leaderboard = leaderboard2;
	        }
	    }
	};
	

	
	//----- Handler for Ads --------------------
    private final int SWARM_INIT = 5;
    private final int showToast = 11;
    private final int openLink = 14;
    private final int askSwarmLogin = 18;
    private final int askExit = 33;
  
    
    
    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {

                case SWARM_INIT:
                {
//                	Swarm.init(BubblrAndroid.this, swarmAppID, swarmAppKey, mySwarmLoginListener); 
                    break;
                }
                

                case openLink:
                {                	
                	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); 
                    break;
                }

                
                case showToast:
                {                	
//                	context = getApplicationContext();
                	toast = Toast.makeText(context, toastText, duration);
                	toast.show();
                    break;
                }
                

              
                
                //-------------------------------------------------------------------
                case askExit:											// deaktiviert, da es beim backbutton crasht (quit icon funktioniert)
                {   
                	messageBox = new AlertDialog.Builder(context).create();

                	messageBox.setTitle(dialogSubject);
                	messageBox.setMessage(dialogText);
                	messageBox.setButton(bubblr.lang.getString("YES"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {
                			
                			messageBox.dismiss();
                			
                			Gdx.app.postRunnable(new Runnable() {
//                			    @Override
                			    public void run() {
                			        
                			    	bubblr.exit_quit();
                			    }
                			});
                		}
                	});
                	messageBox.setButton2(bubblr.lang.getString("NO"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {	
                			
                			messageBox.dismiss();
                			
                			Gdx.app.postRunnable(new Runnable() {
//                			    @Override
                			    public void run() {
                			        
                			    	bubblr.setScene2DScreen(new CoolMainMenu(bubblr, "MainMenu"));
                			    }
                			});	
                		}
                	});
                	
                	messageBox.show();
                	break;
                }
                
                
                //-------------------------------------------------------------------
                case askSwarmLogin:
                {   
                	messageBox = new AlertDialog.Builder(context).create();

                	messageBox.setTitle(dialogSubject);
                	messageBox.setMessage(dialogText);
                	messageBox.setButton(bubblr.lang.getString("YES"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {
                			messageBox.dismiss();
                			showSwarmLogin();
                		}
                	});
                	messageBox.setButton2(bubblr.lang.getString("NO"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {	
                			cancelalert();
                		}
                	});
                	
                	messageBox.show();
                	break;
                }
                

            }
        }
    };
    
    
    //---------------------------------------------------------------------------
    
    
 
    public void askExit(String dialogSubject, String dialogText) {
    	
    	this.dialogSubject = dialogSubject;
    	this.dialogText = dialogText;	
    	
    	handler.sendEmptyMessage(askExit);
    }
    
    
    
    public void askSwarmLogin(String dialogSubject, String dialogText) {
    	
    	this.dialogSubject = dialogSubject;
    	this.dialogText = dialogText;	
    	
    	handler.sendEmptyMessage(askSwarmLogin);
    }
    
    
    public void showSwarmLogin() {
    	
    	messageBox.dismiss();
    	if (Swarm.isInitialized() == false) swarmInit();
    	Swarm.showLogin();
    }
    
    
    public void showLink() {
    	messageBox.dismiss();
    	openLink(dialogURL); 
    }
    public void cancelalert() {
    	messageBox.dismiss();
    }
    
    
    
    //---- saves the toast text and displays it
    public void showToast(String toastText) {
    	
    	duration = Toast.LENGTH_SHORT;
    	
    	this.toastText = toastText;
    	handler.sendEmptyMessage(showToast);
    }
    
    //---- saves the toast text and displays it
    public void showToast(String toastText, int duration) {
    	
    	this.duration = duration;
    	this.toastText = toastText;
    	handler.sendEmptyMessage(showToast);
    }
    
    
    
    
    
    
//    @Override
    public void swarmInit() {
    	
    	handler.sendEmptyMessage(SWARM_INIT);
    }
	
    
//    @Override
    public void openLink(String url) {
    	
    	this.url = url;
    	handler.sendEmptyMessage(openLink);
    }
    
  
    
   

    
    
    
    //==============================================================
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate (Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
		
		
		
		bubblr = new Bubblr(this, isDemoVersion);
	
		
		
		thisactivity = this;	// for interstitial ads
		
		
		//----- for ads -------------------
		
		// Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(bubblr, false); 	//--- openGL 1.0
         
//        View gameView = initializeForView(bubblr, true); 		//--- openGL 2.0
        
        
        //------- for admob --------------------------
        
        // Create and setup the AdMob view
        adView = new AdView(this, AdSize.BANNER, "97659759"); // Put in your secret key here
        
        AdRequest adRequest = new AdRequest();
//        adRequest.addTestDevice(admobTestDevice);
        adView.loadAd(adRequest);

        
        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams = 
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        
        layout.addView(adView, adParams);

        // Hook it all up
        
        setContentView(layout);

				
		//----------------------------------
		
		
	
		
//	    Swarm.setActive(this);		
//		FlurryAgent.onStartSession(this, flurryCode);

	}
	
	//=======================================================================================================
	

	public void onResume() {
	    super.onResume();
	    Swarm.setActive(this);
	}

	public void onPause() {
	    super.onPause();
	    Swarm.setInactive(this);
	}
	
	public void onStop() {
	    super.onStop();
	    Swarm.setInactive(this);
	}
	
	public void onDestroy() {
	    super.onDestroy();
	    Swarm.setInactive(this);
	    FlurryAgent.onEndSession(this);
	}



//-----------------------------------------------------


	

}