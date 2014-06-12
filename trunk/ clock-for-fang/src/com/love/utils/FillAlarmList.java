package com.love.utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.love.ui.AlarmAdapter;

public class FillAlarmList extends AsyncTask<Void, Void, Void> {

	Activity context;
	AlarmAdapter adapter;
	public FillAlarmList(Activity context){
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
