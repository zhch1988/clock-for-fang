package com.love.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

public class AlarmInfo implements Parcelable {
	public String description;
	public long timeinmillis;
	public int frequency;
	public boolean enable;
	public int daysofweek;
	private int _id;// this field will be generated automatically in database

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public static final String ID = "_id";
	public static final String DESCRIPTION = "description";
	public static final String TIME = "time";
	public static final String FREQUENCY = "frequency";
	public static final String ENABLE = "enable";
	public static final String DAYS_OF_WEEK = "days_of_week";
	public static final int SUNDAY = 0x1;
	public static final int MONDAY = 0x1 << 1;
	public static final int TUESDAY = 0x1 << 2;
	public static final int WEDNESDAY = 0x1 << 3;
	public static final int THURSDAY = 0x1 << 4;
	public static final int FRIDAY = 0x1 << 5;
	public static final int SATURDAY = 0x1 << 6;

	public static final int FREQUENCY_ONCE = 0;
	public static final int FREQUENCY_EVERYDAY = 1;
	public static final int FREQUENCY_SPECIFIED = 2;
	public static final int FREQUENCY_INTERNAL = 3;

	public AlarmInfo(Parcel source) {
		description = source.readString();
		_id = source.readInt();
		timeinmillis = source.readLong();
		frequency = source.readInt();
		enable = (source.readByte() == 1);
		daysofweek = source.readInt();
	}

	public AlarmInfo(String name, long time, int frequency, boolean enable,
			int daysofweek) {
		this(-1, name, time, frequency, enable, daysofweek);
	}

	public AlarmInfo(int _id, String name, long time, int frequency,
			boolean enable, int daysofweek) {
		this._id = _id;
		this.description = name;
		this.timeinmillis = time;
		this.frequency = frequency;
		this.enable = enable;
		this.daysofweek = daysofweek;
	}

	public boolean equals(AlarmInfo other) {
		if (this.get_id() == other.get_id()
				&& this.description == other.description
				&& this.timeinmillis == other.timeinmillis
				&& this.frequency == other.frequency
				&& this.enable == other.enable
				&& this.daysofweek == other.daysofweek) {
			return true;
		}else{
			return false;
		}
	}

	public String getFormatedTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeinmillis);
//		int hour = calendar.get(Calendar.HOUR_OF_DAY);
//		int minute = calendar.get(Calendar.MINUTE);
//		return calendar.get(Calendar.HOUR_OF_DAY) + ":"
//				+ calendar.get(Calendar.MINUTE);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm",Locale.CHINA);
		return simpleDateFormat.format(calendar.getTime());
	}

	public int getHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeinmillis);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeinmillis);
		return calendar.get(Calendar.MINUTE);
	}

	public int getYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeinmillis);
		return calendar.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeinmillis);
		return calendar.get(Calendar.MONTH);
	}

	public int getDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeinmillis);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static class Time {
		public int hour;
		public int minute;

		public Time(int hour, int minute) {
			this.hour = hour;
			this.minute = minute;
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(description);
		dest.writeInt(_id);
		dest.writeLong(timeinmillis);
		dest.writeInt(frequency);
		dest.writeByte((byte) (enable ? 1 : 0));
		dest.writeInt(daysofweek);
	}

	public static final Parcelable.Creator<AlarmInfo> CREATOR = new Creator<AlarmInfo>() {

		@Override
		public AlarmInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new AlarmInfo(source);
		}

		@Override
		public AlarmInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AlarmInfo[size];
		}

	};

}
