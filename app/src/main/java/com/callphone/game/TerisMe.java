package com.callphone.game;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.callphone.myapplication.R;

public class TerisMe extends Activity implements OnClickListener {

	private TerisView terisView;
	public RefreshHandler refreshHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设为无标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		terisView = (TerisView) findViewById(R.id.terisme);
		refreshHandler = new RefreshHandler(terisView);

		findViewById(R.id.buttonLeft).setOnClickListener(this);
		findViewById(R.id.buttonRight).setOnClickListener(this);
		findViewById(R.id.buttonDown).setOnClickListener(this);
		findViewById(R.id.buttonChange).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.buttonLeft:
				terisView.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT);
				break;

			case R.id.buttonRight:
				terisView.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT);
				break;

			case R.id.buttonDown:
				terisView.fastDrop();
				break;
			case R.id.buttonChange:
				terisView.onKeyDown(KeyEvent.KEYCODE_DPAD_UP);
				break;

		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (refreshHandler != null) {
			refreshHandler.pause();
			Log.v("teris", "pause");
		}
		// 游戏暂停
		// terisView.setMode(TerisView.STATE_MENU);
	}

	public void onResume() {
		super.onResume();
		if (refreshHandler != null) {
			refreshHandler.resume();
			refreshHandler.start();
			Log.v("teris", "resume");
		}
	}

}