package com.love.clockforfang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

import com.love.constant.Constant;

@SuppressLint("NewApi") 
public class SettingActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().add(android.R.id.content,  
                new SettingFragment()).commit();
	}

	public static class SettingFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{

		RingtonePreference pref_ringtone;
		ListPreference pref_interval;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.setting_preference);
			pref_ringtone = (RingtonePreference) this.findPreference(Constant.PREF_RINGTONE);
			pref_interval = (ListPreference) this.findPreference(Constant.PREF_INTERVAL);
			SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
//			int[] intevalarray = getResources().getIntArray(R.array.interval_value);
			
			Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), Uri.parse(preference.getString(Constant.PREF_RINGTONE, "")));
			pref_ringtone.setSummary(ringtone.getTitle(getActivity()));
			pref_interval.setSummary(preference.getString(Constant.PREF_INTERVAL, "")+"∑÷÷”");
			pref_ringtone.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference preference,
						Object newValue) {
					// TODO Auto-generated method stub
					Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), Uri.parse((String) newValue));
					pref_ringtone.setSummary(ringtone.getTitle(getActivity()));
					return true;
				}
			});
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			// TODO Auto-generated method stub
//			switch(key){
//			case Constant.PREF_RINGTONE:
//				String[] ringtonetext = sharedPreferences.getString(Constant.PREF_RINGTONE, "").split("/");
//				Log.w("Preference", sharedPreferences.getString(Constant.PREF_RINGTONE, ""));
//				pref_ringtone.setSummary(ringtonetext[ringtonetext.length - 1]);
//				break;
//			case Constant.PREF_INTERVAL:
//				pref_interval.setSummary(sharedPreferences.getString(key, "")+"∑÷÷”");
//				break;
//			}
			
//			if(key.equals(Constant.PREF_RINGTONE)){
//				String[] ringtonetext = sharedPreferences.getString(Constant.PREF_RINGTONE, "").split("/");
//				Log.w("Preference", sharedPreferences.getString(Constant.PREF_RINGTONE, ""));
//				pref_ringtone.setSummary(ringtonetext[ringtonetext.length - 1]);
//			}
			if(key.equals(Constant.PREF_INTERVAL)){
				pref_interval.setSummary(sharedPreferences.getString(key, "")+"∑÷÷”");
			}
			
		}
		
	}
}


