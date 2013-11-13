package bb.sxytm.freedum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class CalendarUtility {
    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();
    public static ArrayList<String> startFromTimes = new ArrayList<String>();
    public static ArrayList<String> startToTimes = new ArrayList<String>();
    public static ArrayList<String> endFromTimes = new ArrayList<String>();
    public static ArrayList<String> endToTimes = new ArrayList<String>();

    public static ArrayList<String> readCalendarEvent(Context context) {
            Cursor cursor = context.getContentResolver()
                            .query(Uri.parse("content://com.android.calendar/events"),
                                            new String[] { "calendar_id", "title", "description",
                                                            "dtstart", "dtend", "eventLocation" }, null,
                                            null, null);
            cursor.moveToFirst();
            // fetching calendars name
            String CNames[] = new String[cursor.getCount()];

            // fetching calendars id
            nameOfEvent.clear();
            startDates.clear();
            endDates.clear();
            descriptions.clear();
            
            
            try {
            for (int i = 0; i < CNames.length; i++) {

                    nameOfEvent.add(cursor.getString(1));
                    startDates.add(getDate(Long.parseLong(cursor.getString(3).trim())));
                    endDates.add(getDate(Long.parseLong(cursor.getString(4).trim())));
                    descriptions.add(cursor.getString(2));
                    CNames[i] = cursor.getString(1);
                    cursor.moveToNext();

            }
            } catch (java.lang.NullPointerException e) {
            	nameOfEvent.add("No Event");
            	Log.d("Error", "Null Pointer Adding Date");
            }
            return nameOfEvent;
    }

    @SuppressLint("SimpleDateFormat")
	public static String getDate(long milliSeconds) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
    }
}
