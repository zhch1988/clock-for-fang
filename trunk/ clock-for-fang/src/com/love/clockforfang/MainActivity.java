package com.love.clockforfang;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.love.ui.AlarmAdapter;
import com.love.utils.AlarmInfo;
import com.love.utils.DBHelper;
import com.love.utils.ShowAlarmList;


public class MainActivity extends ActionBarActivity {
	AlarmService mService = null;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        PreferenceManager.setDefaultValues(this, R.xml.setting_preference, false);
        Log.e("Activity", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());
        
//        new SettingThread().start();
        
        /**
         * 添加闹钟的测试区
         */
//        new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				DBHelper dbhelper = new DBHelper(MainActivity.this);
//				AlarmInfo alarm = new AlarmInfo("this is the test", System.currentTimeMillis()+10*1000, AlarmInfo.FREQUENCY_EVERYDAY, false, 0);
//				int _id = dbhelper.insert(alarm);
//				Log.e("insert", "id: "+_id);
//				AlarmInfo get = dbhelper.query(_id);
//				Log.e("after insert", "time: "+get.timeinmillis);
//				alarm.set_id(_id);
//				alarm.timeinmillis += 2000000;
//				Log.e("update", "return:"+dbhelper.update(alarm)+";");
//				get = dbhelper.query(_id);
//				Log.e("after update", "time:"+get.timeinmillis);
////				dbhelper.rebuildTable();
////				get = dbhelper.query(1);
////				Log.e("_id=1", get==null?"null":"time:"+get.timeinmillis);
//				ArrayList<AlarmInfo> alarmlist = dbhelper.getAlarmList();
//				
//				dbhelper.close();
//			}
//        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NewApi") @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
        case R.id.action_add:
//        	ListView alarmlist = (ListView) getFragmentManager().findFragmentById(R.id.mainfragment).getView().findViewById(R.id.alarmlist);
        	Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			AlarmInfo alarm = new AlarmInfo("", System.currentTimeMillis(), AlarmInfo.FREQUENCY_ONCE, true, 0);
			Intent intent = new Intent(this, AlarmSettingActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("alarm", alarm);
			intent.putExtras(bundle);
			getSupportFragmentManager().findFragmentById(R.id.container).startActivityForResult(intent, 0);
			break;
        case R.id.action_settings:
        	Intent settingintent = new Intent(this, SettingActivity.class);
        	startActivity(settingintent);
        	break;
//        case R.id.action_about:
//        	
//        	break;
        case R.id.action_exit:
        	this.finish();
        	break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

    	@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			getActivity().unbindService(serviceconnection);
			getActivity().unregisterReceiver(updateReceiver);
		}

		private ServiceConnection serviceconnection = new ServiceConnection() {
    		@Override
    		public void onServiceConnected(ComponentName name, IBinder service) {
    			// TODO Auto-generated method stub
    			if (name.getShortClassName().endsWith("AlarmService")) {
    				mService = ((AlarmService.ServiceBinder) service)
    						.getService();
    				Log.i("activity", "connected");
    			}
    		}
    		@Override
    		public void onServiceDisconnected(ComponentName name) {
    			// TODO Auto-generated method stub
    		}
    	};
    	AlarmService mService = null;
    	AlarmAdapter adapter;
    	ArrayList<AlarmInfo> alarminfos;
    	DBHelper dbhelper;
    	ListView alarmlist;
    	ShowAlarmList showlisttask;
        public PlaceholderFragment() {
        }
        public boolean isServiceGot(){
        	return mService == null ? false:true;
        }
        
        public AlarmService getService(){
			return mService;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            /**
             * start and bind service
             */
            Intent serviceintent = new Intent(getActivity(), AlarmService.class);
            getActivity().startService(serviceintent);
            getActivity().bindService(serviceintent, serviceconnection, BIND_AUTO_CREATE);
//            ArrayList<AlarmInfo> alarminfos = new ArrayList<AlarmInfo>();
//            AlarmInfo alarm1 = new AlarmInfo("1", System.currentTimeMillis()+10*1000, AlarmInfo.FREQUENCY_EVERYDAY, false, 0);
//            AlarmInfo alarm2 = new AlarmInfo("2", System.currentTimeMillis()+30*1000, AlarmInfo.FREQUENCY_ONCE, true, 0);
//            AlarmInfo alarm3 = new AlarmInfo("3", System.currentTimeMillis()+300*1000, AlarmInfo.FREQUENCY_INTERNAL, false, 0);
//            alarminfos.add(alarm1);
//            alarminfos.add(alarm2);
//            alarminfos.add(alarm3);
            
//            dbhelper = new DBHelper(getActivity());
//            alarminfos = dbhelper.getAlarmList();

            
            alarmlist = (ListView)rootView.findViewById(R.id.alarmlist);
            /**
             * this paragraph should be optimized
             * */
            Handler modifier = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					ArrayList<AlarmInfo> alarminfo = new ArrayList<AlarmInfo>();
					if(msg.what == 0){
						alarminfo = ((AlarmAdapter) (alarmlist.getAdapter())).getAlarmInfos();
						for(int i = 0; i < alarminfo.size(); i++){
							System.out.println(i+":"+alarminfo.get(i).enable);
						}
					}
				}
            	
            };
            
//            adapter = new AlarmAdapter(getActivity(), alarminfos, modifier);
//            alarmlist.setAdapter(adapter);
            showlisttask = new ShowAlarmList(getActivity(), this, alarmlist, modifier);
            showlisttask.execute();
            alarmlist.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					AlarmInfo item;
					if(parent.getAdapter().getItemViewType(position) == AlarmAdapter.VIEWTYPE_ADD){
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(System.currentTimeMillis());
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
						item = new AlarmInfo("", System.currentTimeMillis(), AlarmInfo.FREQUENCY_ONCE, true, 0);
					}
					else{
						item = (AlarmInfo) parent.getAdapter().getItem(position);
					}
					Log.e("test", ""+position);
					Log.e("item", "_id: " + item.get_id()+", name: " + item.description);
					Intent intent = new Intent(getActivity(), AlarmSettingActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable("alarm", item);
					intent.putExtras(bundle);
					startActivityForResult(intent, 0);
				}
            });
            alarmlist.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, final int position, long id) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(getActivity()).setItems(new String[]{"删除"}, new OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch(which){
							case 0:
								
								final AlarmInfo alarm = (AlarmInfo) alarmlist.getAdapter().getItem(position);
								new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("确认删除？").setPositiveButton("确定", new OnClickListener(){
									@Override
									public void onClick(DialogInterface confirmdialog,
											int which) {
										// TODO Auto-generated method stub
										mService.deleteAlarm(alarm);
										showlisttask.updateAlarmList();
									}
									
								}).setNegativeButton("取消", new OnClickListener(){
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										
									}
									
								}).show();
								break;
							}
						}
						
					}).show();
					return true;
				}
            	
            });
            
            /******************Broadcast Receiver******************/
    		IntentFilter filter = new IntentFilter();
    		filter.addAction("updatealarm");
    		getActivity().registerReceiver(updateReceiver, filter);

            return rootView;
        }
        BroadcastReceiver updateReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
				if(action.equals("updatealarm")){
					showlisttask.updateAlarmList();
				}
			}	
		};
		

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if(resultCode == RESULT_OK){
				AlarmInfo newalarm = data.getExtras().getParcelable("alarm");
				mService.updateAlarm(newalarm);
				showlisttask.updateAlarmList();
//				ArrayList<AlarmInfo> newalarminfos = mService.getAlarms();
//				alarminfos.clear();
//				alarminfos.addAll(newalarminfos);
//				adapter.notifyDataSetChanged();
			}
		}
        
    }
    
    

}
