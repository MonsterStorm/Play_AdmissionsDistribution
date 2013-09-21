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
	
	/**
	 * format datatime
	 * @param dateTime
	 * @return
	 */
	public static String formatDateTime(Long dateTime){
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME_DEFAULT);
		return sdf.format(new Date(dateTime));
	}
	
	/**
	 * format date
	 * @param dateTime
	 * @return
	 */
	public static String formatDate(Long dateTime){
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_DEFAULT);
		return sdf.format(new Date(dateTime));
	}
}
