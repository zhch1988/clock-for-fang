package com.love.ui;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.love.clockforfang.R;
import com.love.utils.AlarmInfo;
import com.love.utils.AlarmParser;

public class SettingAdapter extends BaseAdapter {

	private static final int TYPE_TIME = 0;
	private static final int TYPE_DESCRIPTION = 1;
	private static final int TYPE_ENABLE = 2;
	private static final int TYPE_FREQUENCY = 3;
	// private static final int TYPE_DAYSOFWEEK = 4;
	private static final int TYPE_NULL = 4;
	private static final int COUNT = 5;

	private Context context;
	private AlarmInfo alarm;
	private AlarmInfo newalarm;

	public SettingAdapter(Context context, AlarmInfo alarm) {
		this.context = context;
		this.newalarm = this.alarm = alarm;
	}

	public AlarmInfo getAlarm() {
		return newalarm;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return COUNT - 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int type;
		switch (position) {
		case 0:
			type = TYPE_TIME;
			break;
		case 1:
			type = TYPE_DESCRIPTION;
			break;
		case 2:
			type = TYPE_ENABLE;
			break;
		case 3:
			type = TYPE_FREQUENCY;
			break;
		default:
			type = TYPE_NULL;
			break;
		}
		return type;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int type = this.getItemViewType(position);
		switch (type) {
		case TYPE_TIME:
			convertView = LayoutInflater.from(context).inflate(
					R.layout.time_item, null);
			TimePicker timepicker = (TimePicker) convertView
					.findViewById(R.id.timePicker);
			timepicker.setIs24HourView(true);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(newalarm.timeinmillis);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			timepicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			timepicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
			timepicker.setOnTimeChangedListener(new OnTimeChangedListener() {
				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay,
						int minute) {
					// TODO Auto-generated method stub
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(newalarm.timeinmillis);
					calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					calendar.set(Calendar.MINUTE, minute);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
//					if (calendar.getTimeInMillis() <= System
//							.currentTimeMillis()) {
//						newalarm.timeinmillis = calendar.getTimeInMillis() + 24 * 3600 * 1000;
//					} else {
						newalarm.timeinmillis = calendar.getTimeInMillis();
//					}
				}
			});
			break;
		case TYPE_DESCRIPTION:
			convertView = LayoutInflater.from(context).inflate(
					R.layout.name_item, null);
			EditText edittext = (EditText) convertView
					.findViewById(R.id.name_text);
			edittext.setText(newalarm.description);
			edittext.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					newalarm.description = s.toString();
				}

			});
			break;
		case TYPE_ENABLE:
			convertView = LayoutInflater.from(context).inflate(
					R.layout.enable_item, null);
			final CheckBox checkbox = (CheckBox) convertView
					.findViewById(R.id.enable_checkbox);
			checkbox.setChecked(newalarm.enable);
			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					Log.e("setting", "checked:" + isChecked);
					newalarm.enable = isChecked;
				}

			});
			convertView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					boolean isChecked = checkbox.isChecked();
					newalarm.enable = !isChecked;
					checkbox.setChecked(!isChecked);
				}
			});
			break;
		case TYPE_FREQUENCY:
			convertView = LayoutInflater.from(context).inflate(
					R.layout.frequency_item, null);
			final TextView textview = (TextView) convertView
					.findViewById(R.id.frequency_text);
			textview.setText(AlarmParser.getFrequencyText(newalarm));
			final boolean[] selecteddays = AlarmParser
					.getSelectedDaysofweek(newalarm);
			LinearLayout frequencylayout = (LinearLayout) convertView
					.findViewById(R.id.frequencyLayout);
			frequencylayout.setOnClickListener(new OnClickListener() {
				int selected = newalarm.frequency;

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("setting", "frequency clicked");
					Dialog alertDialog = new AlertDialog.Builder(context)
							.setTitle("重复模式")
							.setSingleChoiceItems(R.array.frequencytype,
									newalarm.frequency,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											selected = which;
										}
									})
							.setPositiveButton(R.string.confirm,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											switch (selected) {
											case 0:
												newalarm.frequency = AlarmInfo.FREQUENCY_ONCE;
												newalarm.daysofweek = 0;
												break;
											case 1:
												newalarm.frequency = AlarmInfo.FREQUENCY_EVERYDAY;
												newalarm.daysofweek = 0;
												break;
											case 2:
												Dialog alertDialog = new AlertDialog.Builder(
														context)
														.setTitle("重复")
														.setMultiChoiceItems(
																R.array.daysofweek,
																selecteddays,
																new OnMultiChoiceClickListener() {
																	@Override
																	public void onClick(
																			DialogInterface dialog,
																			int which,
																			boolean isChecked) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		selecteddays[which] = isChecked;
																	}

																})
														.setPositiveButton(
																"确定",
																new DialogInterface.OnClickListener() {
																	@Override
																	public void onClick(
																			DialogInterface dialog,
																			int which) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		newalarm.frequency = AlarmInfo.FREQUENCY_SPECIFIED;
																		newalarm.daysofweek = AlarmParser
																				.daysofweek_ArraytoInt(selecteddays);
																		if(newalarm.daysofweek == 0)
																			newalarm.frequency = AlarmInfo.FREQUENCY_ONCE;
																		textview.setText(AlarmParser.getFrequencyText(newalarm));
																	}
																})
														.setNegativeButton(
																"取消",
																new DialogInterface.OnClickListener() {
																	@Override
																	public void onClick(
																			DialogInterface dialog,
																			int which) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub

																	}
																}).create();
												alertDialog.show();
												break;
											case 3:
												Dialog datedialog = new CustomDatePickerDialog(
														context,
														new OnDateSetListener() {
															@Override
															public void onDateSet(
																	DatePicker view,
																	int year,
																	int monthOfYear,
																	int dayOfMonth) {
																// TODO
																// Auto-generated
																// method stub
																Calendar calendar = Calendar
																		.getInstance();
																calendar.setTimeInMillis(newalarm.timeinmillis);
																calendar.set(
																		year,
																		monthOfYear,
																		dayOfMonth);
																newalarm.timeinmillis = calendar
																		.getTimeInMillis();
																newalarm.frequency = AlarmInfo.FREQUENCY_INTERNAL;
																newalarm.daysofweek = 0;
																textview.setText(AlarmParser.getFrequencyText(newalarm));
															}
														},
														newalarm.getYear(),
														newalarm.getMonth(),
														newalarm.getDayOfMonth(),
														"请选择首日上班时间");
												datedialog.show();
												break;
											default:
												break;
											}
											textview.setText(AlarmParser.getFrequencyText(newalarm));
										}
									})
							.setNegativeButton(R.string.cancel,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

										}
									}).create();
					alertDialog.show();

					// Dialog alertDialog = new
					// AlertDialog.Builder(context).setTitle("重复").setMultiChoiceItems(R.array.daysofweek,
					// selecteddays, new OnMultiChoiceClickListener(){
					// @Override
					// public void onClick(DialogInterface dialog, int which,
					// boolean isChecked) {
					// // TODO Auto-generated method stub
					// selecteddays[which] = isChecked;
					// }
					//
					// }).setPositiveButton("确定", new
					// DialogInterface.OnClickListener(){
					// @Override
					// public void onClick(DialogInterface dialog, int which) {
					// // TODO Auto-generated method stub
					// newalarm.daysofweek =
					// AlarmParser.daysofweek_ArraytoInt(selecteddays);
					// }
					// }).setNegativeButton("取消", new
					// DialogInterface.OnClickListener(){
					// @Override
					// public void onClick(DialogInterface dialog, int which) {
					// // TODO Auto-generated method stub
					//
					// }
					// }).create();
					// alertDialog.show();
				}
			});
			break;
		case TYPE_NULL:
			convertView = null;
			break;
		default:
			convertView = null;
			break;
		}

		return convertView;
	}

	class CustomDatePickerDialog extends DatePickerDialog {
		private String titletext;

		public CustomDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth, String title) {  
	        super(context, callBack, year, monthOfYear, dayOfMonth);
	        titletext = title;
	        setTitle(titletext);
	    }

		@Override
		public void onDateChanged(DatePicker view, int year, int month, int day) {
			// TODO Auto-generated method stub
			super.onDateChanged(view, year, month, day);
			setTitle(titletext);
		}

	}

}
