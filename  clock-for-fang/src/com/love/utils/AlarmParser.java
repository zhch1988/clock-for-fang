package com.love.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.util.Log;

public class AlarmParser {
	public static AlarmInfo getSettedAlarm(AlarmInfo alarm){
		int frequency = alarm.frequency;
		switch(frequency){
		case AlarmInfo.FREQUENCY_EVERYDAY:
		case AlarmInfo.FREQUENCY_ONCE:
			while(alarm.timeinmillis > System.currentTimeMillis())
				alarm.timeinmillis -= 24*3600*1000;
			while(alarm.timeinmillis < System.currentTimeMillis()){
				alarm.timeinmillis += 24*3600*1000;
			}
			break;
		case AlarmInfo.FREQUENCY_INTERNAL:
			while(alarm.timeinmillis < System.currentTimeMillis()){
				alarm.timeinmillis -= 24*3600*1000;
				alarm.daysofweek --;
			}
			int mod = alarm.daysofweek%3>0 ? alarm.daysofweek%3:(alarm.daysofweek%3+3);
			while(alarm.timeinmillis < System.currentTimeMillis() || mod == 2){
				alarm.timeinmillis += 24*3600*1000;
				alarm.daysofweek ++;
				mod = alarm.daysofweek%3>=0 ? alarm.daysofweek%3:(alarm.daysofweek%3+3);
			}
			break;
		case AlarmInfo.FREQUENCY_SPECIFIED:
			long origintime = alarm.timeinmillis;
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(origintime);
			int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			while(alarm.timeinmillis < System.currentTimeMillis() || ((0x1 << dayofweek & alarm.daysofweek) == 0)){
				origintime += 24*3600*1000;
				calendar.setTimeInMillis(origintime);
				dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			}
			break;
		}
		
		return alarm;
	}
	
	public static AlarmInfo getNextAlarm(AlarmInfo alarm){
		int frequency = alarm.frequency;
		switch(frequency){
		case AlarmInfo.FREQUENCY_EVERYDAY:
			while(alarm.timeinmillis < System.currentTimeMillis()){
				alarm.timeinmillis += 24*3600*1000;
			}
			break;
		case AlarmInfo.FREQUENCY_ONCE:
			if(alarm.timeinmillis < System.currentTimeMillis())
				alarm.enable = false;
			break;
		case AlarmInfo.FREQUENCY_INTERNAL:
			while(alarm.timeinmillis < System.currentTimeMillis() || (alarm.daysofweek%3) == 2){
				alarm.timeinmillis += 24*3600*1000;
				alarm.daysofweek ++;
			}
			break;
		case AlarmInfo.FREQUENCY_SPECIFIED:
			long origintime = alarm.timeinmillis;
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(origintime);
			int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			while(origintime < System.currentTimeMillis() || (((0x1 << dayofweek) & alarm.daysofweek) == 0)){
				origintime += 24*3600*1000;
				calendar.setTimeInMillis(origintime);
				dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			}
			alarm.timeinmillis = origintime;
			break;
		}
		
		return alarm;
	}
	
	public static String getFrequencyText(AlarmInfo alarm){
		int frequency = alarm.frequency;
		String frequencytext = "";
		switch(frequency){
		case AlarmInfo.FREQUENCY_EVERYDAY:
			frequencytext = "每天";
			break;
		case AlarmInfo.FREQUENCY_ONCE:
			frequencytext = "从不重复";
			break;
		case AlarmInfo.FREQUENCY_INTERNAL:
			AlarmInfo nextalarm = getNextAlarm(alarm);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(nextalarm.timeinmillis);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日",Locale.CHINA);
			String nextday = simpleDateFormat.format(calendar.getTime());
			frequencytext = "银行班,下次为：" + nextday;
			break;
		case AlarmInfo.FREQUENCY_SPECIFIED:
			int daysofweek = alarm.daysofweek;
			ArrayList<String> daysofweeklist = new ArrayList<String>();
			if((daysofweek & AlarmInfo.SUNDAY) == AlarmInfo.SUNDAY){
				daysofweeklist.add("周日");
			}
			if((daysofweek & AlarmInfo.MONDAY) == AlarmInfo.MONDAY){
				daysofweeklist.add("周一");
			}
			if((daysofweek & AlarmInfo.TUESDAY) == AlarmInfo.TUESDAY){
				daysofweeklist.add("周二");
			}
			if((daysofweek & AlarmInfo.WEDNESDAY) == AlarmInfo.WEDNESDAY){
				daysofweeklist.add("周三");
			}
			if((daysofweek & AlarmInfo.THURSDAY) == AlarmInfo.THURSDAY){
				daysofweeklist.add("周四");
			}
			if((daysofweek & AlarmInfo.FRIDAY) == AlarmInfo.FRIDAY){
				daysofweeklist.add("周五");
			}
			if((daysofweek & AlarmInfo.SATURDAY) == AlarmInfo.SATURDAY){
				daysofweeklist.add("周六");
			}
			for(int i = 0; i < daysofweeklist.size(); i++){
				if(!frequencytext.equals(""))
					frequencytext += "，";
				frequencytext += daysofweeklist.get(i);
			}
		}
		return frequencytext;
	}
	
	public static boolean[] getSelectedDaysofweek(AlarmInfo alarm){
		boolean[] selected = new boolean[]{false, false, false, false, false, false, false};
		if(alarm.frequency != AlarmInfo.FREQUENCY_SPECIFIED)
			return selected;
		int daysofweek = alarm.daysofweek;
		if((daysofweek & AlarmInfo.SUNDAY) == AlarmInfo.SUNDAY){
			selected[0] = true;
		}else{
			selected[0] = false;
		}
		if((daysofweek & AlarmInfo.MONDAY) == AlarmInfo.MONDAY){
			selected[1] = true;
		}else{
			selected[1] = false;
		}
		if((daysofweek & AlarmInfo.TUESDAY) == AlarmInfo.TUESDAY){
			selected[2] = true;
		}else{
			selected[2] = false;
		}
		if((daysofweek & AlarmInfo.WEDNESDAY) == AlarmInfo.WEDNESDAY){
			selected[3] = true;
		}else{
			selected[3] = false;
		}
		if((daysofweek & AlarmInfo.THURSDAY) == AlarmInfo.THURSDAY){
			selected[4] = true;
		}else{
			selected[4] = false;
		}
		if((daysofweek & AlarmInfo.FRIDAY) == AlarmInfo.FRIDAY){
			selected[5] = true;
		}else{
			selected[5] = false;
		}
		if((daysofweek & AlarmInfo.SATURDAY) == AlarmInfo.SATURDAY){
			selected[6] = true;
		}else{
			selected[6] = false;
		}
		
		return selected;
	}
	
	
	public static int daysofweek_ArraytoInt(boolean[] selected){
		int daysofweek = 0;
		for(int i = 0; i < selected.length; i++){
			if(selected[i] == true){
				//this place i didn't use
				daysofweek = daysofweek|(0x1<<i);
			}
		}
		return daysofweek;
	}
}
