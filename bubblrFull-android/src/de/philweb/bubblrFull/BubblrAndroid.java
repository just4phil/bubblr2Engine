package de.philweb.bubblrFull;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.swarmconnect.SwarmActiveUser.GotCloudDataCB;
import com.swarmconnect.SwarmLeaderboard;
import com.swarmconnect.SwarmLeaderboard.GotLeaderboardCB;
import com.swarmconnect.SwarmLeaderboard.GotScoreCB;
import com.swarmconnect.SwarmLeaderboardScore;
import com.swarmconnect.SwarmUser;
import com.swarmconnect.delegates.SwarmLoginListener;

import de.philweb.bubblr.Bubblr;
import de.philweb.bubblr.Welt;
import de.philweb.bubblr.screens.TextConstants;
import de.philweb.bubblr.tools.IActivityRequestHandler;

 

public class BubblrAndroid extends AndroidApplication implements IActivityRequestHandler {

	 
	// ================== THIS IS THE FULL VERSION ================================================================
	
	public final boolean isDemoVersion = false;
	final String PREFS_NAME = ".bubblrfull";

	
	// ================== THIS IS THE FULL VERSION ================================================================
	
	final String admobCode = 			"a14ff75b541e2a6";
	final String admobTestDevice = 		"C81149556B32D7A72718B6591F131ED5";
	final int swarmAppID = 				616;
	final String swarmAppKey = 			"47e13a2d6588a8e7c41ed4d848c0f52c";		 
	final String flurryCode_DEMO = 		"45215IY9G2QJCWKXRV9L";					// DEMO
	final String flurryCode_FULL = 		"XJZNVB9TDVQRFTTXRFQX";					// FULL Version
	String flurryCode;
	// ======================= Codes & Keys ========================================================================
	
	
	public Bubblr bubblr;
	
	Activity thisactivity;		// for interstitial ads
	
	public static Map<Integer, SwarmAchievement> achievements = new HashMap<Integer, SwarmAchievement>();	// für SWARM Achievements
	public static SwarmLeaderboard leaderboard;
    long score;
    int leaderboardID;
    SwarmLeaderboardScore highscore;
    public SwarmLeaderboardScore playerHighscore;
    SwarmUser loggedUser;
	
    
    //----- for Toasts -----
    public Context context = this; // = getApplicationContext();
    int duration;
	Toast toast;
    String toastText;
    
    //----- for sharing text to other apps
    String shareText;
    String shareTextSubject;
    String shareHeadline;
    Intent sendIntent;
	
    
    //----- for ads ---------------
    AdView adView;
    boolean adsEnabled = false;
//    public InterstitialAd interstitial;

    
    //---- for opening links to market and website
    String url;
    
    //--- for dialogs -------------------
    String dialogSubject;
    String dialogText;
    String dialogURL;
    AlertDialog messageBox;
    
    //------- showGameSolvedUploadDialog ---------------
    long points;
    
    
  //---- SWARM Cloud Callback
  public int swarm_PREF_PROGRESS_LEVEL;
  public int swarm_PREF_UNLOCKED_LEVEL;
  public long swarm_PREF_PROGRESS_POINTS;
  public int swarm_PREF_PROGRESS_LIFES;		
  public int swarm_PREF_PROGRESS_MONSTERKISSES;
  public int swarm_PREF_KissesForLifeCounter;
  public int swarm_PREF_PROGRESS_HATTRICK2;
  public int swarm_PREF_PROGRESS_HATTRICK3;
  public int swarm_PREF_PROGRESS_HATTRICK4;
  public int swarm_PREF_PROGRESS_HATTRICK5;
  public int swarm_PREF_laserCounter;
    
    
    //--- for Flurry 
//    Map<String, String> FPSlog_minFPS = new HashMap<String, String>();
//    Map<String, String> FPSlog_maxFPS = new HashMap<String, String>();
//    Map<String, String> FPSlog_minFPSLevel = new HashMap<String, String>();
//    Map<String, String> FPSlog_maxFPSLevel = new HashMap<String, String>();    
    Map<String, Number> FPSlogger = new HashMap<String, Number>();  
    Map<String, Number> AppRater = new HashMap<String, Number>();  
    
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
		    
		    
		    // Load our Leaderboard
		    if (isDemoVersion) {
		    	leaderboardID = Welt.leaderboardID_demo;
		    }
		    else {
		    	leaderboardID = Welt.leaderboardID_full;
		    }
		    
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
	
	
	GotCloudDataCB cloudCallbackPoints = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_PROGRESS_POINTS = Long.parseLong(data);
	    }
	}; 
	GotCloudDataCB cloudCallbackLevel = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_PROGRESS_LEVEL = Integer.parseInt(data);
	    }
	}; 
	GotCloudDataCB cloudCallbackUnlockedLevel = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_UNLOCKED_LEVEL = Integer.parseInt(data);
	    }
	}; 
	GotCloudDataCB cloudCallbackKisses = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_PROGRESS_MONSTERKISSES = Integer.parseInt(data);
	    }
	}; 
	GotCloudDataCB cloudCallbackKisses4Lifes = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_KissesForLifeCounter = Integer.parseInt(data);
	    }
	}; 
	GotCloudDataCB cloudCallbackLifes = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_PROGRESS_LIFES = Integer.parseInt(data);
	    }
	}; 
	GotCloudDataCB cloudCallbackH2 = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_PROGRESS_HATTRICK2 = Integer.parseInt(data);
	    }
	}; 
	GotCloudDataCB cloudCallbackH3 = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_PROGRESS_HATTRICK3 = Integer.parseInt(data);
	    }
	};  
	GotCloudDataCB cloudCallbackH4 = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_PROGRESS_HATTRICK4 = Integer.parseInt(data);
	    }
	};
	GotCloudDataCB cloudCallbackH5 = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_PROGRESS_HATTRICK5 = Integer.parseInt(data);
	    }
	};
	GotCloudDataCB cloudCallbackLaser = new GotCloudDataCB() {
	    public void gotData(String data) {
	        if (data == null) return;
	        if (data.length() == 0) data = "0";
	        swarm_PREF_laserCounter = Integer.parseInt(data);
	    }
	};
	

	//------------------------------------------------------------------
	
	GotLeaderboardCB callback = new GotLeaderboardCB() {
	    public void gotLeaderboard(SwarmLeaderboard leaderboard2) {

	    	if (leaderboard2 != null) {

	            // Save the leaderboard for later use
	            leaderboard = leaderboard2;
	        }
	    }
	};
	
	
	GotScoreCB scoreCallback = new GotScoreCB() {
	    
		@Override
		public void gotScore(SwarmLeaderboardScore playerHighscore2) {
			
			playerHighscore = playerHighscore2;
			
		}
	};
	
	
	//----- Handler for Ads --------------------
	private final int HIDE_ADS = 0;
    private final int SHOW_ADS = 1;
    private final int SUBMITSCORE = 2;
    private final int GETHIGHSCORE = 3;
    private final int GETPLAYERHIGHSCORE = 4;
    private final int SWARM_INIT = 5;
    private final int FPStoFLURRY = 6;
    private final int USERtoFLURRY = 7;
    private final int openMarket = 8;
    private final int AppRaterToFLURRY = 9;
    private final int openWebsite = 10;
    private final int showToast = 11;
    private final int share = 12;
    private final int email = 13;
    private final int openLink = 14;
    private final int showUrlDialog = 15;
    private final int showGameSolvedUploadDialog = 16;
    private final int showHintDialog = 17;
    private final int askSwarmLogin = 18;
    private final int showRateDialog = 19;
    private final int askRudeMode = 20;
    private final int getSwarmCloudDataPoints = 21;
    private final int getSwarmCloudDataLevel = 22;
    private final int getSwarmCloudDataLifes = 23;
    private final int getSwarmCloudDataKisses = 24;
    private final int getSwarmCloudDataH2 = 25;
    private final int getSwarmCloudDataH3 = 26;
    private final int getSwarmCloudDataH4 = 27;
    private final int getSwarmCloudDataH5 = 28;
    private final int getSwarmCloudDataLaser = 29;
    private final int getSwarmCloudDataKissesForLifes = 30;
    private final int showEULA = 31;
    private final int getSwarmCloudDataUnlocked = 32;
    private final int askExit = 33;
    private final int loadInterstitialAd = 34;
    private final int showInterstitialAd = 35;
    private final int askShare = 36;
    
    
    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                {
                    adView.setVisibility(View.VISIBLE);
                    break;
                }
                case HIDE_ADS:
                {
                    adView.setVisibility(View.GONE);
                    break;
                }
                case SUBMITSCORE:
                {
                    leaderboard.submitScore(score);
                    break;
                }
                case GETHIGHSCORE:
                {
//                    leaderboard.submitScore(score);
                    break;
                }
                case GETPLAYERHIGHSCORE:
                {
                	leaderboard.getScoreForUser(loggedUser, scoreCallback);
                    break;
                }
                case SWARM_INIT:
                {
                	Swarm.init(BubblrAndroid.this, swarmAppID, swarmAppKey, mySwarmLoginListener); 
                    break;
                }
                
                case FPStoFLURRY:
                {                	
//                	FlurryAgent.logEvent("FPSlog", FPSlog_maxFPS);
//                	FlurryAgent.logEvent("FPSlog", FPSlog_minFPS);
//                	FlurryAgent.logEvent("FPSlog", FPSlog_minFPSLevel);
//                	FlurryAgent.logEvent("FPSlog", FPSlog_maxFPSLevel);
                	FlurryAgent.logEvent("FPSlog", FPSlogger);
                    break;
                }
                case USERtoFLURRY:
                {                	
                	FlurryAgent.setUserId(user);
                    break;
                }
                
               

                case openLink:
                {                	
                	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); 
                    break;
                }
                
                case AppRaterToFLURRY:
                {                	
                	FlurryAgent.logEvent("AppRater", AppRater);
                    break;
                }
               
             
                
                case showToast:
                {                	
//                	context = getApplicationContext();
                	toast = Toast.makeText(context, toastText, duration);
                	toast.show();
                    break;
                }
                
                
                case share:
                {                	
                	sendIntent = new Intent();
                	sendIntent.setAction(Intent.ACTION_SEND);
                	sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                	sendIntent.putExtra(Intent.EXTRA_SUBJECT, shareTextSubject);
                	sendIntent.setType("text/plain");
                	
//                	startActivity(sendIntent);
                	startActivity(Intent.createChooser(sendIntent, shareHeadline));
                    break;
                }   

                case email:
                {                	
                	sendIntent = new Intent();
                	sendIntent.setAction(Intent.ACTION_SEND);
                	sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { TextConstants.mailaddress });
//                	sendIntent.putExtra(Intent.EXTRA_EMAIL, TextConstants.mailaddress);
                	sendIntent.putExtra(Intent.EXTRA_SUBJECT, "bubblr Contact");
//                	sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                	sendIntent.setType("plain/text");
                	
//                	startActivity(sendIntent);
                	startActivity(Intent.createChooser(sendIntent, "email us ..."));
                    break;
                }   
                
                case showUrlDialog:
                {   
                	messageBox = new AlertDialog.Builder(context).create();

                	messageBox.setTitle(dialogSubject);
                	messageBox.setMessage(dialogText);
                	messageBox.setButton(bubblr.lang.getString("YES"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {
                			showLink();
                			
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
                
                
                
           //---------------------------------------------------------------------------
//             a simple mesage box: for example: if map had special collectable that is still there => show warning for level progress 
                case showHintDialog:
                {   
                	messageBox = new AlertDialog.Builder(context).create();

                	messageBox.setTitle(dialogSubject);
                	messageBox.setMessage(dialogText);
                	messageBox.setButton(bubblr.lang.getString("OK"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {
                			cancelalert();
                		}
                	});                

                	messageBox.show();
                	break;
                }
                
                
           //---------------------------------------------------------------------------
//              - bei game solved und nicht in swarm eingeloggt sollte gefragt werden ob man sich einloggen und score uploaden will... Sonst ist score weg
//          	=> gamescreen: avoid people loosing their highscore after the last level when not logged in !!!
                
//                case showGameSolvedUploadDialog:
//                {   
//                	messageBox = new AlertDialog.Builder(context).create();
//
//                	messageBox.setTitle(dialogSubject);
//                	messageBox.setMessage(dialogText);
//                	messageBox.setButton("Yes", new OnClickListener() {
//
//                		@Override
//                		public void onClick(DialogInterface dialog, int which) {
//                			showSwarmLogin();
//                		}
//                	});
//                	messageBox.setButton2("No", new OnClickListener() {
//
//                		@Override
//                		public void onClick(DialogInterface dialog, int which) {	
//                			cancelalert();
//                		}
//                	});
//                	
//                	messageBox.show();
//                	break;
//                }
                
                
                
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
                			        
                			    	bubblr.geist_lassdassein_hasBeenPlayed = false;
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
                
                
                //-------------------------------------------------------------------
                case askShare:
                {   
                	messageBox = new AlertDialog.Builder(context).create();

                	messageBox.setTitle(dialogSubject);
                	messageBox.setMessage(dialogText);
                	messageBox.setButton(bubblr.lang.getString("YES"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {
                			
                			messageBox.dismiss();
                			share(shareHeadline, shareTextSubject, shareText);
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
                
                //---------------------------------------------
                
                case showRateDialog:
                {   
                	messageBox = new AlertDialog.Builder(context).create();

                	messageBox.setTitle(dialogSubject);
                	messageBox.setMessage(dialogText);
                	
                	messageBox.setButton(bubblr.lang.getString("later"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {
                			
                			messageBox.dismiss();
                		}
                	});
                	messageBox.setButton2(bubblr.lang.getString("Never"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {	
                			
                			messageBox.dismiss();
                			
                			Gdx.app.postRunnable(new Runnable(){
//                			    @Override
                			    public void run() {
                			    	bubblr.prefs.set_AppraterDontShowAgain(1); 		//--- disable apprater dialog
                			    }
                			});
                			
                			submitAppRatertoFlurry(0);
//                			showRateDialogNEVER();
                		}
                	});
                	messageBox.setButton3(bubblr.lang.getString("Rate bubblr!"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {	
                			
                	    	messageBox.dismiss();
                	    	
                			Gdx.app.postRunnable(new Runnable(){
//                			    @Override
                			    public void run() {
                			    	bubblr.prefs.set_AppraterDontShowAgain(1); 		//--- disable apprater dialog
                			    }
                			});
                			
                			submitAppRatertoFlurry(1);
                	    	openLink(dialogURL); 
//                			showRateDialogRATE();               	    	
                		}
                	});
                	
                	messageBox.show();
                	
					Button button1 = (Button) messageBox.findViewById(android.R.id.button1);
					button1.setFocusable(false);
					Button button2 = (Button) messageBox.findViewById(android.R.id.button2);
					button2.setFocusable(false);
					Button button3 = (Button) messageBox.findViewById(android.R.id.button3);
					button3.setFocusable(false);
					
                	break;
                }
                
                //-------------------------------------------------------------------
				case askRudeMode:
				{   
					messageBox = new AlertDialog.Builder(context).create();
					messageBox.setTitle(dialogSubject);
					messageBox.setMessage(dialogText);
					messageBox.setButton(bubblr.lang.getString("YES"), new OnClickListener() {
				
//						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							Gdx.app.postRunnable(new Runnable(){
				//    			The Gdx.app.postRunnable() makes the code written in the Runnable's run() method run on the libgdx render thread, 
				//    			just like the Handler runs code on the android ui thread. So in my example above the bubblr.prefs.putString(..) 
				//    			which was in the run() method would be posted on the libgdx render thread, before input processing. In my example 
				//    			I meant it to be inside the onClickListener(), and is important because the click listener stuff is inherently on 
				//    			the ui thread (you ensured it by putting it inside the Handler).
				//    			Actually to tell you the truth I think the Gdx preferences is thread safe and ok to call from either thread, so 
				//    			it's probably optional in this case.
//								@Override
							    public void run() {
							        bubblr.prefs.setRudeMode(false);	//----- disable rudemode -> save to prefs
							    	bubblr.prefs.setAskRudeMode();
							    }
							});
						}
					});
					messageBox.setButton2(bubblr.lang.getString("NO"), new OnClickListener() {
				
//						@Override
						public void onClick(DialogInterface dialog, int which) {	
							
							Gdx.app.postRunnable(new Runnable() {
//							    @Override
							    public void run() {
							        bubblr.prefs.setRudeMode(true);	//----- enable rudemode -> save to prefs
							    	bubblr.prefs.setAskRudeMode();
							    }
							});
						}
					});
					
				//                	messageBox.getCurrentFocus().clearFocus();												// führt zu crash der app
				//                	if (messageBox.getCurrentFocus() != null) messageBox.getCurrentFocus().clearFocus();	// funktioniert nicht :(
										
					messageBox.show();
					
					Button button1 = (Button) messageBox.findViewById(android.R.id.button1);
					button1.setFocusable(false);
					Button button2 = (Button) messageBox.findViewById(android.R.id.button2);
					button2.setFocusable(false);
					
					break;
				}
                
                //-------------------------------------------------------------------
                case showEULA:
                {   
                	messageBox = new AlertDialog.Builder(context).create();
                	messageBox.setTitle(dialogSubject);
                	messageBox.setMessage(dialogText);
                	messageBox.setCancelable(false);
                	messageBox.setButton(bubblr.lang.getString("OK"), new OnClickListener() {
                	
//                		@Override
                		public void onClick(DialogInterface dialog, int which) {
                			
                			
                			Gdx.app.postRunnable(new Runnable(){
//                			    @Override
                			    public void run() {
                			        
                			    	bubblr.prefs.set_EULA_hasBeenShown();
                			    }
                			});
                			messageBox.dismiss();
                		}
                	});
                	
                	messageBox.setButton2(bubblr.lang.getString("CANCEL"), new OnClickListener() {

//                		@Override
                		public void onClick(DialogInterface dialog, int which) {	
                			
                			messageBox.dismiss();
                			
                			Gdx.app.postRunnable(new Runnable() {
//                			    @Override
                			    public void run() {
                			        
                			    	Gdx.app.exit();
                			    }
                			});
                		}
                	});
                	
                	messageBox.setOnKeyListener(new OnKeyListener() { 
                    	
//                    	@Override 
                    	public boolean onKey(DialogInterface dialoginterface, int keyCode, KeyEvent event) {
                    		
                    		if ((keyCode == KeyEvent.KEYCODE_HOME)) {
                    			return false;  
                    		} 
                    		else {   
                    			return true;  
                    		} 
                    	}
                    });
                    
                	messageBox.show();
					
                 	break;
                }
                
                //---------------------------------------------
                
                // make asynchronous requests to Swarm for the Cloud Data
                
                case getSwarmCloudDataPoints:
                {   
    				Swarm.user.getCloudData("PREF_PROGRESS_POINTS", cloudCallbackPoints);
                	break;
                }
                case getSwarmCloudDataLevel:
                {   
    				Swarm.user.getCloudData("PREF_PROGRESS_LEVEL", cloudCallbackLevel);
                	break;
                } 
                case getSwarmCloudDataUnlocked:
                {   
    				Swarm.user.getCloudData("PREF_UNLOCKED_LEVEL", cloudCallbackUnlockedLevel);
                	break;
                } 
                case getSwarmCloudDataLifes:
                {   
    				Swarm.user.getCloudData("PREF_PROGRESS_LIFES", cloudCallbackLifes);
                	break;
                }
                case getSwarmCloudDataKisses:
                {   
    				Swarm.user.getCloudData("PREF_PROGRESS_MONSTERKISSES", cloudCallbackKisses);
                	break;
                }
                case getSwarmCloudDataH2:
                {   
    				Swarm.user.getCloudData("PREF_PROGRESS_HATTRICK2", cloudCallbackH2);
                	break;
                }
                case getSwarmCloudDataH3:
                {   
    				Swarm.user.getCloudData("PREF_PROGRESS_HATTRICK3", cloudCallbackH3);
                	break;
                }
                case getSwarmCloudDataH4:
                {   
    				Swarm.user.getCloudData("PREF_PROGRESS_HATTRICK4", cloudCallbackH4);
                	break;
                }
                case getSwarmCloudDataH5:
                {   
    				Swarm.user.getCloudData("PREF_PROGRESS_HATTRICK5", cloudCallbackH5);
                	break;
                }
                case getSwarmCloudDataLaser:
                {   
    				Swarm.user.getCloudData("PREF_laserCounter", cloudCallbackLaser);
                	break;
                }
                case getSwarmCloudDataKissesForLifes:
                {   
    				Swarm.user.getCloudData("PREF_KissesForLifeCounter", cloudCallbackKisses4Lifes);
                	break;
                }
                //---------------------------------------------         
  
                
                
//                case loadInterstitialAd:
//                {   
//                    // Create the interstitial
//	                interstitial = new InterstitialAd(thisactivity, admobCode);
//	
//	                // Create ad request
//	                AdRequest interstitialRequest = new AdRequest();
//	                interstitialRequest.addTestDevice(admobTestDevice);
//	                
//	                // and begin loading your interstitial
//	                interstitial.loadAd(interstitialRequest);
//                	break;
//                }
//                
//                case showInterstitialAd:
//                {
//                	
//                	interstitial.show();
//                }
                
            }
        }
    };
    
    
    //---------------------------------------------------------------------------
    
    
    public void getSwarmCloudDataPoints() {
    	handler.sendEmptyMessage(getSwarmCloudDataPoints);
    }
    public void getSwarmCloudDataLevel() {
    	handler.sendEmptyMessage(getSwarmCloudDataLevel);
    }
    public void getSwarmCloudDataUnlocked() {
    	handler.sendEmptyMessage(getSwarmCloudDataUnlocked);
    }
    public void getSwarmCloudDataLifes() {
    	handler.sendEmptyMessage(getSwarmCloudDataLifes);
    }
    public void getSwarmCloudDataKisses() {
    	handler.sendEmptyMessage(getSwarmCloudDataKisses);
    }
    public void getSwarmCloudDataH2() {
    	handler.sendEmptyMessage(getSwarmCloudDataH2);
    }
    public void getSwarmCloudDataH3() {
    	handler.sendEmptyMessage(getSwarmCloudDataH3);
    }
    public void getSwarmCloudDataH4() {
    	handler.sendEmptyMessage(getSwarmCloudDataH4);
    }
    public void getSwarmCloudDataH5() {
    	handler.sendEmptyMessage(getSwarmCloudDataH5);
    }
    public void getSwarmCloudDataLaser() {
    	handler.sendEmptyMessage(getSwarmCloudDataLaser);
    }
    public void getSwarmCloudDataKissesForLifes() {
    	handler.sendEmptyMessage(getSwarmCloudDataKissesForLifes);
    }
    
    
    
    public int getSwarmCloudDataEntry(String which) {
	   	
    	int returndata = 0;
	   	
    	if (which.equals("PREF_PROGRESS_LEVEL")) returndata =  swarm_PREF_PROGRESS_LEVEL;
    	if (which.equals("PREF_UNLOCKED_LEVEL")) returndata =  swarm_PREF_UNLOCKED_LEVEL;
    	if (which.equals("PREF_PROGRESS_LIFES")) returndata =  swarm_PREF_PROGRESS_LIFES;
    	if (which.equals("PREF_PROGRESS_MONSTERKISSES")) returndata =  swarm_PREF_PROGRESS_MONSTERKISSES;
    	if (which.equals("PREF_KissesForLifeCounter")) returndata =  swarm_PREF_KissesForLifeCounter;
    	if (which.equals("PREF_PROGRESS_HATTRICK2")) returndata =  swarm_PREF_PROGRESS_HATTRICK2;
    	if (which.equals("PREF_PROGRESS_HATTRICK3")) returndata =  swarm_PREF_PROGRESS_HATTRICK3;
    	if (which.equals("PREF_PROGRESS_HATTRICK4")) returndata =  swarm_PREF_PROGRESS_HATTRICK4;
    	if (which.equals("PREF_PROGRESS_HATTRICK5")) returndata =  swarm_PREF_PROGRESS_HATTRICK5;
    	if (which.equals("PREF_laserCounter")) returndata =  swarm_PREF_laserCounter;

    	return returndata;
    }
    
    public long getSwarmCloudDataScore() {

    	return swarm_PREF_PROGRESS_POINTS;
    }
    
    //----------------------------------------------------------------
    
    
    //----- to pass to simpleEula -----------
//    public Activity getContext() {
//
//    	return this;
//    }
    
   
    
    
    //    - bei game solved und nicht in swarm eingeloggt sollte gefragt werden ob man sich einloggen und score uploaden will... Sonst ist score weg
//	=> gamescreen: avoid people loosing their highscore after the last level when not logged in !!!
//    public void showGameSolvedUploadDialog(String dialogSubject, String dialogText, long points) {
//    	
//    	this.dialogSubject = dialogSubject;
//    	this.dialogText = dialogText;
//    	this.points = points;  	
//    	
//    	handler.sendEmptyMessage(showGameSolvedUploadDialog);
//    }
    
 
    public void askExit(String dialogSubject, String dialogText) {
    	
    	this.dialogSubject = dialogSubject;
    	this.dialogText = dialogText;	
    	
    	handler.sendEmptyMessage(askExit);
    }
    
    
    public void askRudeMode(String dialogSubject, String dialogText) {
    	
    	this.dialogSubject = dialogSubject;
    	this.dialogText = dialogText;	
    	
    	handler.sendEmptyMessage(askRudeMode);
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
    
    //---------------------------------------------------------------------------
   

    public void showRateDialog(String dialogSubject, String dialogText, String dialogURL) {
    	
    	this.dialogSubject = dialogSubject;
    	this.dialogText = dialogText;
    	this.dialogURL = dialogURL;  	
    	
    	handler.sendEmptyMessage(showRateDialog);
    }
        
//-----------------------------------------------------------
    
    
    public void showHintDialog(String dialogSubject, String dialogText) {
    	
    	this.dialogSubject = dialogSubject;
    	this.dialogText = dialogText;	
    	
    	handler.sendEmptyMessage(showHintDialog);
    }
    
    
    
    public void showUrlDialog(String dialogSubject, String dialogText, String dialogURL) {
    	
//    	context = getApplicationContext();
    	this.dialogSubject = dialogSubject;
    	this.dialogText = dialogText;
    	this.dialogURL = dialogURL;  	
    	
    	handler.sendEmptyMessage(showUrlDialog);
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
    
    
    //------ saves the text and shares it
    public void share(String shareHeadline, String shareTextSubject, String shareText) {
    	
    	this.shareText = shareText;
    	this.shareTextSubject = shareTextSubject;
    	this.shareHeadline = shareHeadline;
    	
    	handler.sendEmptyMessage(share);
    }
    
    
    
    //------ saves the text and shares it
    public void askShare(String dialogSubject, String dialogText, String shareHeadline, String shareTextSubject, String shareText) {
    	
    	this.dialogSubject = dialogSubject;
    	this.dialogText = dialogText;	
    	
    	this.shareText = shareText;
    	this.shareTextSubject = shareTextSubject;
    	this.shareHeadline = shareHeadline;
    	
    	handler.sendEmptyMessage(askShare);
    }
	
	
	
	
    //------ saves the text and shares it
    public void email() {
    	
//    	this.shareText = shareText;
//    	this.shareTextSubject = shareTextSubject;
    	
    	handler.sendEmptyMessage(email);
    }
    
    
    
    // This is the callback that posts a message for the handler
//    @Override
    public void showAds(boolean show) {
    	
    	this.adsEnabled = show;
       handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }
    
//    @Override
    public boolean idAdsEnabled() {
    	return adsEnabled;
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
    
  
    
   
//    @Override
    public void giveAchievement(int ID) {
    	
		if (Swarm.isLoggedIn()) {
	
	        // Check for nulls in case the user is offline
	        if (achievements != null && achievements.get(ID) != null) {
	
	            // Give the achievement for successfully landing!
	        	if (achievements.get(ID).unlocked == false) achievements.get(ID).unlock();		// only unlock if not already done
	        }
		}
    }

//    @Override
    public void getLeaderboard(int ID) {
    	
		if (Swarm.isLoggedIn()) {
	
			SwarmLeaderboard.getLeaderboardById(ID, callback);			/// asynchroner request für leaderboard (ergebnis geht an callback)
		}
    }
    
    
//    @Override
    public void submitScore(long score) {
    	
    	this.score = score;
    	
    	if (Swarm.isLoggedIn() && leaderboard != null) {
    		handler.sendEmptyMessage(SUBMITSCORE);
    	}
    }
    
    	
//    @Override
    public void getPlayerScore() {
    	
    	if (Swarm.isLoggedIn() && leaderboard != null) {
    		
    		this.loggedUser = Swarm.user;
    		handler.sendEmptyMessage(GETPLAYERHIGHSCORE);
    	}
    	
    }
    
        

        
    //--- for flurry ------------
    
//    @Override
	public void submitFPStoFlurry(int minFPS, int minFPSLevel, int maxFPS, int maxFPSLevel) {
	
//    FPSlog_minFPS.put("minFPS", "" + minFPS);
//    FPSlog_minFPSLevel.put("minFPSLevel", "" + minFPSLevel);
//    FPSlog_maxFPS.put("maxFPS", "" + maxFPS);
//    FPSlog_maxFPSLevel.put("maxFPSLevel", "" + maxFPSLevel);
    	
		FPSlogger.put("minFPS", minFPS);
		FPSlogger.put("minFPSLevel", minFPSLevel);
		FPSlogger.put("maxFPS", maxFPS);
		FPSlogger.put("maxFPSLevel", maxFPSLevel);
	
		handler.sendEmptyMessage(FPStoFLURRY);
	}
    
    
//    @Override
	public void submitAppRatertoFlurry(int didrate) {
    	
    	AppRater.put("didRateApp", didrate);

		handler.sendEmptyMessage(AppRaterToFLURRY);
	}
    
    
    
//    @Override
	public void submitUSERtoFlurry(String user) {
	
    	this.user = user;
    	handler.sendEmptyMessage(USERtoFLURRY);
	}
    
    
    
    
    //====================== EULA ==========================================================
    
    
    
	private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
             pi = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi; 
    }

	
    
     public void showEULA() {
         
    	PackageInfo versionInfo = getPackageInfo();

		final String eulaKey = "eula_" + versionInfo.versionCode;	// the eulaKey changes every time you increment the version number in the AndroidManifest.xml
		        	
    	// Show the Eula 
    	this.dialogSubject = bubblr.lang.getString("bubblr Enduser Licence Agreement") + " v" + versionInfo.versionName;
    	
        //Includes the updates as well so users know what changed. 
    	this.dialogText = 
    			bubblr.lang.getString("Latest Updates") +
    			"\n" +
    			bubblr.lang.getString("beta release") +
    			"\n\n" + 
    			bubblr.lang.getString("bubblr Enduser Licence Agreement") +
    			"\n" +
    			bubblr.lang.getString("bubblr is in beta-Stadium. No Warranty! Copyright by Heavily Loaded Games. Reverse Engineering, Decompilation and Disassembly is prohibited.");
        
        handler.sendEmptyMessage(showEULA);
    }
    
    //=========================================================================================
    
    
    
    
    //==============================================================
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate (Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
		
		
		//---- nicht wirklich nötig! -------------------
//      AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		config.useWakelock = false;
//		config.useGL20 = false;
//		
//		initialize(new Bubblr(this, isDemoVersion), config);
		//------------------------
		
//		initialize(new Bubblr(this, isDemoVersion), false);
		
		
		
		thisactivity = this;	// for interstitial ads
		
			
		bubblr = new Bubblr(this, isDemoVersion);
		
		
		
		//----- for ads -------------------
		
		// Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(bubblr, false);
 

        //------------for adwhirl ---------------------------------------
//        AdWhirlManager.setConfigExpireTimeout(1000 * 60 * 5);
//        AdWhirlTargeting.setAge(23);
//        AdWhirlTargeting.setGender(AdWhirlTargeting.Gender.MALE);
//        AdWhirlTargeting.setKeywords("online games gaming arcade");
//        AdWhirlTargeting.setPostalCode("94123");
//        AdWhirlTargeting.setTestMode(true);
//
////        AdWhirlLayout adWhirlLayout = (AdWhirlLayout)findViewById(R.id.adwhirl_layout);
//        AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "dbbd1e88d671413187c3508245de1964");
//        
//        TextView textView = new TextView(this);
//        RelativeLayout.LayoutParams layoutParams = new
//          RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                                      LayoutParams.WRAP_CONTENT);
//        int diWidth = 320;
//        int diHeight = 52;
//        int density = (int)getResources().getDisplayMetrics().density;
//
//        adWhirlLayout.setAdWhirlInterface(this);
//        adWhirlLayout.setMaxWidth((int)(diWidth * density));
//        adWhirlLayout.setMaxHeight((int)(diHeight * density));
//        
//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        textView.setText("Below AdWhirlLayout");
//
////        LinearLayout layout = (LinearLayout)findViewById(R.id.layout_main);
//
//        layout.setGravity(Gravity.CENTER_HORIZONTAL);
//        layout.addView(adWhirlLayout, layoutParams);
//        layout.addView(textView, layoutParams);
//        layout.invalidate();
        
        
        //------- for admob --------------------------
        
        // Create and setup the AdMob view
        adView = new AdView(this, AdSize.BANNER, admobCode); // Put in your secret key here
        
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
		
		
	

        
        
		
		
		
		//---- set flurryCode for demo/full version --------------
	    if (isDemoVersion) {
	    	flurryCode = flurryCode_DEMO;
	    }
	    else {
	    	flurryCode = flurryCode_FULL;
	    }
	    //-----------------------------------------------------------
		
		
	    Swarm.setActive(this);		
		FlurryAgent.onStartSession(this, flurryCode);

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

	

//	@Override
	public void loadInterstitial(InterstitialCallback callback) {
		
//		handler.sendEmptyMessage(loadInterstitialAd);
	}
//	@Override
	public void showInterstitial() {
		
//		if (interstitial.isReady()) handler.sendEmptyMessage(showInterstitialAd);
	}
	

}