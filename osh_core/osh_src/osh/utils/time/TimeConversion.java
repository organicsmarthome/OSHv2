/*
 ************************************************************************************************* 
 * OrganicSmartHome [Version 2.0] is a framework for energy management in intelligent buildings
 * Copyright (C) 2014  Florian Allerding (florian.allerding@kit.edu) and Kaibin Bao and
 *                     Ingo Mauser and Till Schuberth
 * 
 * 
 * This file is part of the OrganicSmartHome.
 * 
 * OrganicSmartHome is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * 
 * OrganicSmartHome is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OrganicSmartHome.  
 * 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 *************************************************************************************************
 */

package osh.utils.time;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class TimeConversion {
	
	private final static TimeZone tzUTC = TimeZone.getTimeZone("UTC");


	public static int convertUnixTime2Year(long unixTime) {
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeZone(tzUTC);
		_calendar.setTimeInMillis(unixTime * 1000);
		int year = _calendar.get(Calendar.YEAR);
		return year;
	}
	
	public static int convertUnixTime2DayOfYear(long unixTime) {
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeZone(tzUTC);
		_calendar.setTimeInMillis(unixTime * 1000);
		int day = _calendar.get(Calendar.DAY_OF_YEAR);
		return day;
	}
	
	/**
	 * 
	 * @param unixTime
	 * @return sun=1, mon=2...
	 */
	public static int convertUnixTime2WeekdayInt(long unixTime) {
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeZone(tzUTC);
		_calendar.setTimeInMillis(unixTime * 1000);
		int day = _calendar.get(Calendar.DAY_OF_WEEK);
		return day;
	}
	
	/**
	 * Mo=0, Tu,We,Th=1, Fr=2, Sa=3, Su=4
	 * @param weekday
	 * @return
	 */
	public static int convertWeekdayIntTo5Weekdays(int weekday) {
		int i;
		if (weekday == 1) {
			i = 4;
		}
		else if (weekday == 2) {
			i = 0;
		}
		else if (weekday >= 3 && weekday <= 5) {
			i = 1;
		}
		else if (weekday == 6) {
			i = 2;
		}
		else {
			i = 3;
		}
		return i;
	}
	
	/**
	 * 
	 * @param unixTime
	 * @return
	 */
	public static Months convertUnixTime2Month(long unixTime) {
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeZone(tzUTC);
		_calendar.setTimeInMillis(unixTime * 1000);
		int month = _calendar.get(Calendar.MONTH);
		return Months.values()[month];
	}
	
	/**
	 * 
	 * @param unixTime
	 * @return jan=0, ... , dec=11
	 */
	public static int convertUnixTime2MonthInt(long unixTime) {
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeZone(tzUTC);
		_calendar.setTimeInMillis(unixTime * 1000);
		int month = _calendar.get(Calendar.MONTH);
		return month;
	}
	
	/**
	 * 
	 * @param unixTime
	 * @return seconds since midnight (1am=3600)
	 */
	public static int convertUnixTime2SecondsSinceMidnight(long unixTime) {
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeZone(tzUTC);
		_calendar.setTimeInMillis(unixTime * 1000);
		int hour = _calendar.get(Calendar.HOUR_OF_DAY);
		int minute = _calendar.get(Calendar.MINUTE);
		int second = _calendar.get(Calendar.SECOND);
		return hour * 60 * 60 + minute * 60 + second;
	}

	public static long calculateTimeSpanFromMidnight(long unixTime) {
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeZone(tzUTC);
		_calendar.setTimeInMillis(unixTime * 1000);
		long hours = _calendar.get(Calendar.HOUR_OF_DAY);
		long minutes = _calendar.get(Calendar.MINUTE);
		long seconds = _calendar.get(Calendar.SECOND);
		return seconds + minutes * 60 + hours * 60 * 60;
	}
	
	public static long getUnixTimeStampCurrentDayMidnight(long currentUnixTime) {
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeZone(tzUTC);
		_calendar.setTimeInMillis(currentUnixTime * 1000);
		_calendar.set(Calendar.HOUR_OF_DAY, 0);
		_calendar.set(Calendar.MINUTE, 0);
		_calendar.set(Calendar.SECOND, 0);
		return _calendar.getTimeInMillis() /1000L;
	}
	
	public static long componentTimeToTimestamp(int year, int month, int day, int hour) {
	    Calendar c = Calendar.getInstance();
	    c.setTimeZone(tzUTC);
	    c.set(Calendar.YEAR, year);
	    c.set(Calendar.MONTH, month);
	    c.set(Calendar.DAY_OF_MONTH, day);
	    c.set(Calendar.HOUR_OF_DAY, hour);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);
	    return (long) (c.getTimeInMillis() / 1000L);
	}

}
