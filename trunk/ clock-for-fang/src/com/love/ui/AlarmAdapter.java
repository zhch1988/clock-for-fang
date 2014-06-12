package com.love.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.love.clockforfang.R;
import com.love.utils.AlarmInfo;
import com.love.utils.AlarmParser;

public class AlarmAdapter extends BaseAdapter {

	Context context;
	ArrayList<AlarmInfo> alarminfos;
	Handler modifier;

	public AlarmAdapter(Context context, ArrayList<AlarmInfo> alarminfos,
			Handler modifier) {
		this.context = context;
		this.alarminfos = alarminfos;
		this.modifier = modifier;
//		AlarmInfo newalarmholder = new AlarmInfo("",
//				System.currentTimeMillis(), AlarmInfo.FREQUENCY_ONCE, true, 0);
//		alarminfos.add(newalarmholder);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alarminfos.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return alarminfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
//		if (position == alarminfos.size() - 1)
//			return -1;
//		else
//			return position;
		return position;
	}

	public static final int VIEWTYPE_COUNT = 2;
	public static final int VIEWTYPE_ALARM = 0;
	public static final int VIEWTYPE_ADD = 1;

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == alarminfos.size()) {
			return VIEWTYPE_ADD;
		} else {
			return VIEWTYPE_ALARM;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return VIEWTYPE_COUNT;
	}

	private class AlarmViewHolder {
		TextView alarmTime;
		TextView description;
		TextView frequency;
		CheckBox enable;
	}


//	@Override
//	public void notifyDataSetChanged() {
//		// TODO Auto-generated method stub
//		if(alarminfos.get(alarminfos.size() - 1).get_id() != -1){
//			alarminfos.add(new AlarmInfo("", System.currentTimeMillis(), AlarmInfo.FREQUENCY_ONCE, true, 0));
//		}
//		super.notifyDataSetChanged();
//	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int itemtype = getItemViewType(position);
		// new alarm item
		if (itemtype == VIEWTYPE_ADD) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.add_item, null);
			}
			return convertView;
		} else {

			// alarm items
			AlarmViewHolder holder = new AlarmViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item, null);
				holder.alarmTime = (TextView) convertView
						.findViewById(R.id.alarmTime);
				holder.frequency = (TextView) convertView
						.findViewById(R.id.frequency);
				holder.enable = (CheckBox) convertView
						.findViewById(R.id.enable);
				holder.description = (TextView) convertView
						.findViewById(R.id.description);
				convertView.setTag(holder);
			} else {
				holder = (AlarmViewHolder) convertView.getTag();
			}
			holder.alarmTime
					.setText(alarminfos.get(position).getFormatedTime());
			String frequency_string = "";
//			switch (alarminfos.get(position).frequency) {
//			case AlarmInfo.FREQUENCY_ONCE:
//				frequency_string = "从不";
//				break;
//			case AlarmInfo.FREQUENCY_EVERYDAY:
//				frequency_string = "每天";
//				break;
//			case AlarmInfo.FREQUENCY_INTERNAL:
//				frequency_string = "银行班";
//				break;
//			case AlarmInfo.FREQUENCY_SPECIFIED:
//				frequency_string = "每周";
//				break;
//			default:
//				frequency_string = "未知";
//			}
			frequency_string = AlarmParser.getFrequencyText(alarminfos.get(position));
			holder.frequency.setText(frequency_string);
			if(alarminfos.get(position).description.equals(""))
				holder.description.setText("闹钟描述");
			else
				holder.description.setText(alarminfos.get(position).description);
			holder.enable.setOnCheckedChangeListener(null);
			holder.enable.setChecked(alarminfos.get(position).enable);
			holder.enable
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							alarminfos.get(position).enable = isChecked;
							modifier.sendEmptyMessage(0);
						}

					});
			return convertView;
		}
	}

	public ArrayList<AlarmInfo> getAlarmInfos() {
//		ArrayList<AlarmInfo> alarms = alarminfos;
//		alarms.remove(alarms.size() - 1);
//		return alarms;
		return alarminfos;
	}

}
