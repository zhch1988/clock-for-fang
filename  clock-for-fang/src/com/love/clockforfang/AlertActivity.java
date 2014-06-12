package com.love.clockforfang;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.love.constant.Constant;
import com.love.utils.AlarmInfo;

public class AlertActivity extends Activity {

	private AlarmService mService = null;
	private ServiceConnection serviceconnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			if (name.getShortClassName().endsWith("AlarmService")) {
				mService = ((AlarmService.ServiceBinder) service).getService();
				Log.i("alertactivity", "connected");
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}
	};

	AlarmInfo alarminfo;
	Dialog alertDialog;
	MediaPlayer player;
	KeyguardManager km;
	// 得到键盘锁管理器对象
	KeyguardLock kl;
	PowerManager pm;
	PowerManager.WakeLock wl;

	Timer timer = new Timer(true);
	int prestate = -1;

	TelephonyManager telephonyManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// final Window win = getWindow();
		// win.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		// | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		//
		//
		// bind service here
		Intent serviceintent = new Intent(this, AlarmService.class);
		startService(serviceintent);
		bindService(serviceintent, serviceconnection, BIND_AUTO_CREATE);
		// get sharedpreference here
		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(this);
		String ringtoneuri = preference.getString(Constant.PREF_RINGTONE, "");
		final int interval = Integer.parseInt(preference.getString(
				Constant.PREF_INTERVAL, ""));

		// set alarm ringtone here
		// Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

		final Uri uri = Uri.parse(ringtoneuri);
		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_ALARM);
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// TODO Auto-generated method stub
				super.onCallStateChanged(state, incomingNumber);
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:
					if (prestate == -1) {
						try {
							player.setDataSource(AlertActivity.this, uri);
							player.setLooping(true);
							player.prepare();
							player.start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (prestate == TelephonyManager.CALL_STATE_OFFHOOK
							|| prestate == TelephonyManager.CALL_STATE_RINGING) {

					}
					prestate = TelephonyManager.CALL_STATE_IDLE;
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					if (prestate == -1) {
						try {
							player.setDataSource(AlertActivity.this, uri);
							player.setLooping(true);
							player.prepare();
							player.start();
							player.pause();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (prestate == TelephonyManager.CALL_STATE_IDLE){
						try{
							player.start();
							player.pause();
						}catch(IllegalStateException e){
							
						}
					}
					prestate = TelephonyManager.CALL_STATE_OFFHOOK;
					break;
				case TelephonyManager.CALL_STATE_RINGING:
					if (prestate == -1) {
						try {
							player.setDataSource(AlertActivity.this, uri);
							player.setLooping(true);
							player.prepare();
							player.start();
							player.pause();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (prestate == TelephonyManager.CALL_STATE_IDLE){
						try{
							player.start(); 
							player.pause();
						}catch (IllegalStateException e){
							
						}
					}
					prestate = TelephonyManager.CALL_STATE_RINGING;
					break;
				}
			}

		}, PhoneStateListener.LISTEN_CALL_STATE);

		alarminfo = this.getIntent().getExtras().getParcelable("AlarmInfo");
		String title = "闹钟时间到";
		if (!alarminfo.description.equals("")) {
			title = alarminfo.description;
		}

		km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		kl = km.newKeyguardLock("unLock");
		pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "reciever");

		kl.disableKeyguard(); // 解锁
		wl.acquire();// 唤醒屏幕
		alertDialog = new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage("您的选择？")
				.setCancelable(false)
				.setPositiveButton("关闭闹钟",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								timer.cancel();
								mService.setNextAlarm(alarminfo);
								wl.release();
								kl.reenableKeyguard();
								sendUpdateBroadcast();
								try {
									if (player.isPlaying()) {
										player.stop();
										player.release();
									}
								} catch (IllegalStateException e) {

								}
								AlertActivity.this.finish();
							}
						})
				.setNegativeButton(interval + "分钟后再响",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// int _id = alarminfo.get_id();
								// Bundle bundle = new Bundle();
								// bundle.putParcelable("AlarmInfo", alarminfo);
								// Intent intent = new
								// Intent(AlertActivity.this,
								// AlarmReceiver.class);
								// intent.putExtras(bundle);
								// PendingIntent pendingIntent = PendingIntent
								// .getBroadcast(AlertActivity.this, _id,
								// intent,
								// PendingIntent.FLAG_ONE_SHOT);
								// AlarmManager alarmManager = (AlarmManager)
								// getSystemService(ALARM_SERVICE);
								// alarmManager.set(AlarmManager.RTC_WAKEUP,
								// System.currentTimeMillis() + interval
								// * 60 * 1000, pendingIntent);
								// wl.release();
								// kl.reenableKeyguard();
								// player.stop();
								// player.release();
								// AlertActivity.this.finish();
								timer.cancel();
								mService.setNextAlarm(alarminfo);
								sendUpdateBroadcast();
								setDelayedAlarm(interval * 60 * 1000);
							}
						}).create();
		alertDialog.setCancelable(false);
		// alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertDialog.show();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (alertDialog.isShowing()) {
					alertDialog.dismiss();
				}
				setDelayedAlarm(5 * 60 * 1000);
			}
		}, 1000 * 60 * 2);
		Log.e("AlertActivity", "Alarm!");
	}

	private void setDelayedAlarm(int delay) {
		int _id = alarminfo.get_id();
		Bundle bundle = new Bundle();
		bundle.putParcelable("AlarmInfo", alarminfo);
		Intent intent = new Intent(AlertActivity.this, AlarmReceiver.class);
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				AlertActivity.this, _id, intent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ delay, pendingIntent);
		if (wl.isHeld())
			wl.release();
		kl.reenableKeyguard();
		try {
			if (player.isPlaying()) {
				player.stop();
				player.release();
			}
		} catch (IllegalStateException e) {

		}
		AlertActivity.this.finish();
	}

	private void sendUpdateBroadcast() {
		/************************** Broadcast Sender *************************/
		Intent intent = new Intent();
		intent.setAction("updatealarm");
		sendBroadcast(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		telephonyManager.listen(new PhoneStateListener(), PhoneStateListener.LISTEN_NONE);
		unbindService(serviceconnection);
	}

}
