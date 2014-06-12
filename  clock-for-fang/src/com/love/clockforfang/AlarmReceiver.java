package com.love.clockforfang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		Toast.makeText(context, "alarm!", Toast.LENGTH_LONG).show();
		// Intent serviceintent = new Intent(context,AlarmService.class);
		// context.startService(serviceintent);

		Intent alertintent = new Intent(context, AlertActivity.class);
		alertintent.putExtras(intent.getExtras());
		alertintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alertintent);

	}

}
