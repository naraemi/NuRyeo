package com.example.snfu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Intro extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
		new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				try {					
					Thread.sleep(3000);
					} catch (Throwable ex) {
					ex.printStackTrace();
					}
				Intent intent_intro = new Intent(Intro.this, MainActivity.class);
				startActivity(intent_intro);
				finish();
			}
			}).start();
	}
}
