package edu.wgu.Stephan_Ward_C196.Utilities;

import java.util.Date;
import androidx.room.TypeConverter;

/**
 * Converter for the time stamps that is used
 * @author Stephan Ward
 * @since 07/15/2021
 */
public class Converters {
    //Date to time stamp converter to get time
    @TypeConverter
    public static Long dateToTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }
    @TypeConverter
    //TimeStamp to Date to converter to get date.
    public static Date timeStampToDate(Long value) {
        return value == null ? null : new Date(value);
    }
}
