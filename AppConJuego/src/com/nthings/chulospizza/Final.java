package com.nthings.chulospizza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Final extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ImageButton facebook=(ImageButton)findViewById(R.id.facebook);
		facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	String facebookUrl = "https://www.facebook.com/pages/Chulos-Pizza/1628837510678597";
            	try {
            	    int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            	    if (versionCode >= 3002850) {
            	        Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
            	        startActivity(new Intent(Intent.ACTION_VIEW, uri));;
            	    } else {
            	        // open the Facebook app using the old method (fb://profile/id or fb://page/id)
            	        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1628837510678597")));
            	    }
            	} catch (PackageManager.NameNotFoundException e) {
            	    // Facebook is not installed. Open the browser
            	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
            	}
            }
        });
		
		ImageButton jugar=(ImageButton)findViewById(R.id.jugar);
		jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.chulospizza.flappymeni");
            	startActivity(launchIntent);
            }
        });
	}
}
