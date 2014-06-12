package com.love.utils;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ListView;

import com.love.clockforfang.AlarmService;
import com.love.clockforfang.MainActivity.PlaceholderFragment;
import com.love.ui.AlarmAdapter;

public class ShowAlarmList extends AsyncTask<Void, Void, Void> {
	Context context;
	ProgressDialog dialog;
	ListView alarmList;
	PlaceholderFragment fragment;
	Handler modifier;
	AlarmAdapter adapter;
	ArrayList<AlarmInfo> alarminfos;
	public ShowAlarmList(Context context, PlaceholderFragment fragment, ListView alarmlist, Handler modifier){
		this.context = context;
		this.fragment = fragment;
		this.alarmList = alarmlist;
		this.modifier = modifier;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = ProgressDialog.show(context, "提示", "载入中请稍后", false, true);
	}


	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		while(!fragment.isServiceGot()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		DBHelper dbhelper = new DBHelper(context);
//        ArrayList<AlarmInfo> alarminfos = dbhelper.getAlarmList();
		AlarmService service = fragment.getService();
		alarminfos = service.getAlarms();
        adapter = new AlarmAdapter(context, alarminfos, modifier);

		return null;
	}
	
	public void updateAlarmList(){
		ArrayList<AlarmInfo> newalarminfos = fragment.getService().getAlarms();
		alarminfos.clear();
		alarminfos.addAll(newalarminfos);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		alarmList.setAdapter(adapter);
		dialog.dismiss();
	}

	
	
}
