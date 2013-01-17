package de.philweb.bubblrFull;


import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

// This is for ACRA (via bugsense)
@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=a3cb0150",
formKey = "",
mode = ReportingInteractionMode.TOAST,
  resToastText = R.string.crash_toast_text)
 
//@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=a3cb0150", formKey = "")

public class BubblrApp extends Application {

@Override
public void onCreate() {
  super.onCreate();
  ACRA.init(this);
}
}