package common;

import java.text.*;
import java.util.*;
/**
 * date helper for data & time
 * @author MonsterStorm
 *
 */
public class DateHelper {
	public static final String FORMAT_DATE_DEFAULT = "yyyy-MM-dd";
	public static final String FORMAT_DATETIME_DEFAULT = "yyyy-MM-dd hh:mm:ss";
	public static final String FORMAT_CURRENT_DEFAULT = "yyyyMMddhhmmss";
	
	/**
	 * format datatime
	 * @param dateTime
	 * @return
	 */
	public static String formatDateTime(Long dateTime){
		return format(dateTime, FORMAT_DATETIME_DEFAULT);
	}
	
	/**
	 * format date
	 * @param dateTime
	 * @return
	 */
	public static String formatDate(Long dateTime){
		return format(dateTime, FORMAT_DATE_DEFAULT);
	}
	
	/**
	 * format date with given format
	 * @param dateTime
	 * @param format
	 * @return
	 */
	public static String format(Long dateTime, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(dateTime));
	}
	
	/**
	 * get current time
	 * @return
	 */
	public static String getCurrent(){
		return format(System.currentTimeMillis(), FORMAT_CURRENT_DEFAULT);
	}
}
