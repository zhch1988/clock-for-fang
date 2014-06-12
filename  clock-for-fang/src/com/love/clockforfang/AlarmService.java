package com.love.clockforfang;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import com.love.constant.Constant;
import com.love.utils.AlarmInfo;
import com.love.utils.AlarmParser;
import com.love.utils.DBHelper;

public class AlarmService extends Service {
	
	private DBHelper dbhelper;
	private ArrayList<AlarmInfo> allalarm = null;
	
	class AlarmSettingThread extends Thread{
		ArrayList<AlarmInfo> alarmlist;
		AlarmSettingThread(ArrayList<AlarmInfo> alarmlist){
			this.alarmlist = alarmlist;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
            //获取闹钟管理器  
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); 
            //设置闹钟  
//            DBHelper dbhelper = new DBHelper(AlarmService.this);
//            alarmlist = dbhelper.getAlarmList();
            for(int i = 0; i < alarmlist.size(); i++){
            	AlarmInfo curalarm = alarmlist.get(i);
            	//建立Intent和PendingIntent来调用闹钟管理器  
            	int _id = curalarm.get_id();
            	Bundle bundle = new Bundle();
            	bundle.putParcelable("AlarmInfo", curalarm);
                Intent intent = new Intent(AlarmService.this,AlarmReceiver.class);
//                intent.setAction("com.love.clockforfang.ALARM");
                intent.putExtras(bundle);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmService.this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if(curalarm.enable)
                	alarmManager.set(AlarmManager.RTC_WAKEUP, curalarm.timeinmillis, pendingIntent);
                else
                	alarmManager.cancel(pendingIntent);
            }
            
		}		
	}

	/**
	 * to delete an alarm, first you should cancel the alarm on it, then you delete it in database
	 * @param alarm the alarm to be deleted
	 */
	public void deleteAlarm(AlarmInfo alarm){
		//1.cancel the alarm on it
		int _id = alarm.get_id();
    	Bundle bundle = new Bundle();
    	bundle.putParcelable("AlarmInfo", alarm);
        Intent intent = new Intent(AlarmService.this,AlarmReceiver.class);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmService.this, _id, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        //2.delete it in database
        dbhelper.delete(alarm);
        updateAlarmList();
	}
	
//	Handler handler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			if(msg.what == 0){//setting alarm
//				new AlarmSettingThread(allalarm).start();
//			}
//		}
//		
//	};
	@SuppressLint("NewApi") @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Set preference here
//		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
//		Editor editor= preference.edit();
//		String ringtone = preference.getString(Constant.PREF_RINGTONE, "");
//		if(ringtone.equals("")){
//			editor.putString(Constant.PREF_RINGTONE, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());
//			Log.i("Service", "edit PREF_RINGTONE");
//		}
//		int interval = preference.getInt(Constant.PREF_INTERVAL, -1);
//		int[] intevalarray = getResources().getIntArray(R.array.interval_value);
//		if(interval == -1){
//			editor.putInt(Constant.PREF_INTERVAL, intevalarray[1]);
//			Log.i("Service", "edit PREF_INTERVAL");
//		}
//		editor.apply();
		
		dbhelper = new DBHelper(AlarmService.this);
		Log.i("Service", "OnCreate");
		updateAlarmList();
		refreshAlarms();
		updateAlarmList();
		setAlarms();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("Service", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
		
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
//	class AlarmGettingThread extends Thread{
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			super.run();
//			
//			allalarm = dbhelper.getAlarmList();
//			
//			if(allalarm.isEmpty())
//				return;
//			Message msg = new Message();
//			msg.what = 0;
//			handler.sendMessage(msg);
//		}
//	}
	public void updateAlarmList(){
		allalarm = dbhelper.getAlarmList();
	}
	
	public void refreshAlarms(){
		for(int i = 0; i < allalarm.size(); i++){
			AlarmInfo curalarm = allalarm.get(i);
			AlarmInfo newalarm = AlarmParser.getNextAlarm(curalarm);
			if(!curalarm.equals(newalarm)){
				dbhelper.update(newalarm);
			}
		}
	}
	
	public ArrayList<AlarmInfo> getAlarms(){
		if(allalarm == null)
			updateAlarmList();
		return allalarm;
	}
	

	private void setAlarms(){
		new AlarmSettingThread(getAlarms()).start();
	}
	
	public void setNextAlarm(AlarmInfo alarm){
		AlarmInfo nextalarm = AlarmParser.getNextAlarm(alarm);
		updateAlarm(nextalarm);
	}
	
	public void updateAlarm(AlarmInfo alarm){
		alarm = dbhelper.update(alarm);
		updateAlarmList();
		ArrayList<AlarmInfo> list = new ArrayList<AlarmInfo>();
		list.add(alarm);
		new AlarmSettingThread(list).start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("Service", "onBind");
		return new ServiceBinder();
	}

	
	public class ServiceBinder extends Binder{
		public AlarmService getService(){
			return AlarmService.this;
		}
	}
}
