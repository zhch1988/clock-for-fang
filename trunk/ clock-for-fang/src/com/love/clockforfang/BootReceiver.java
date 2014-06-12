package com.love.clockforfang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
			 Log.e("bootreceiver", "boot broadcast received");
			 Intent serviceintent = new Intent(context, AlarmService.class);
			 context.startService(serviceintent);
		 }
	}

}
