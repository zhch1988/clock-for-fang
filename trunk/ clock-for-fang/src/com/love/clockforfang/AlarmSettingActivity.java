package com.love.clockforfang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.love.ui.SettingAdapter;
import com.love.utils.AlarmInfo;
import com.love.utils.AlarmParser;

public class AlarmSettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarmsetting);
		AlarmInfo alarm = this.getIntent().getExtras().getParcelable("alarm");
		SettingAdapter adapter = new SettingAdapter(this, alarm);
		final ListView settings = (ListView) this.findViewById(R.id.settinglist);
		settings.setAdapter(adapter);
//		settings.setOnItemClickListener(new OnItemClickListener(){
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		Button positivebtn = (Button) this.findViewById(R.id.confirmbtn);
		Button negetivebtn = (Button) this.findViewById(R.id.cancelbtn);
		positivebtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data = new Intent();
				AlarmInfo newalarm = ((SettingAdapter) settings.getAdapter()).getAlarm();
				newalarm = AlarmParser.getSettedAlarm(newalarm);
				Bundle bundle = new Bundle();
				bundle.putParcelable("alarm", newalarm);
				data.putExtras(bundle);
				setResult(RESULT_OK, data);
				AlarmSettingActivity.this.finish();
			}
		});
		negetivebtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				AlarmSettingActivity.this.finish();
			}
		});
	}
	
}
