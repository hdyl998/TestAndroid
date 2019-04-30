package com.callphone.game;

import android.os.Handler;
import android.os.Message;

import com.callphone.game.TerisView;

class RefreshHandler extends Handler {

	private static final long DELAY_MILLIS = 100;
	private TerisView terisView;
	private boolean isPause = false;

	public RefreshHandler(TerisView view) {
		terisView = view;
		isPause = false;
	}

	@Override
	public void handleMessage(Message msg) {
		terisView.logic();
		terisView.invalidate();
		sleep(DELAY_MILLIS);
	}

	public void sleep(long delayMillis) {
		if (isPause) {
			return;
		}
		removeMessages(0);
		sendMessageDelayed(obtainMessage(0), delayMillis);
	}

	public void start() {
		sleep(DELAY_MILLIS);
	}

	public void pause() {
		isPause = true;
	}

	public void resume() {
		isPause = false;
	}
}
