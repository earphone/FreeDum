package bb.sxytm.freedum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CalendarAdapter extends BaseAdapter{
    private Context mContext;

    private java.util.Calendar month;
    public GregorianCalendar pmonth; // calendar instance for previous month
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int lastWeekDay;
    int leftDays;
    int mnthlength;
    private boolean comparing = false;
    private String compare;
    String itemvalue, curentDateString;
	public static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    DateFormat df;
    List<ParseObject> daysInMonth = new ArrayList<ParseObject>();
    List<ParseObject> compareDaysInMonth = new ArrayList<ParseObject>();
    List<String> namesOfDays = new ArrayList<String>();
    List<String> compareNamesOfDays = new ArrayList<String>();
    
    private ArrayList<String> items;
    public static List<String> dayString;
    private View previousView;

    ParseUser currentUser = ParseUser.getCurrentUser();

    public CalendarAdapter(Context c, GregorianCalendar monthCalendar, String compareFriend) {
            Log.d("CalendarAdapter", "compareFriend: " + compareFriend);
            if(compareFriend != null) {
            	comparing = true;
            	compare = compareFriend;
            	Log.d("CalendarAdapter", "COMPARING: " + comparing);
            }
    		CalendarAdapter.dayString = new ArrayList<String>();
            Locale.setDefault(Locale.US);
            month = monthCalendar;
            selectedDate = (GregorianCalendar) monthCalendar.clone();
            mContext = c;
            month.set(GregorianCalendar.DAY_OF_MONTH, 1);
            this.items = new ArrayList<String>();
            df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            curentDateString = df.format(selectedDate.getTime());
 
            refreshDays();
    }

    public void setItems(ArrayList<String> items) {
            for (int i = 0; i != items.size(); i++) {
                    if (items.get(i).length() == 1) {
                            items.set(i, "0" + items.get(i));
                    }
            }
            this.items = items;
        	// Get days that have events from Parse
        	String[] substring = curentDateString.split("-");
        	final String currentMonth = currentUser.getUsername() + "_" + months[Integer.parseInt(substring[1])-1] + "_" + substring[0];
        	Log.d("currentMonth",currentMonth);
        	ParseQuery<ParseObject> query = ParseQuery.getQuery(currentMonth);
        	try {
        		daysInMonth = query.find();
        	} catch (ParseException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        
        	String listString = "";
        	String current = "";
        	
        	for (ParseObject s : daysInMonth)
        	{
        		if(s.get(currentMonth)!=null) {
        			current = s.get(currentMonth).toString();
        			listString += current + "\t";
        			namesOfDays.add(current);
        		}
        	}
            Log.d("daysInMonth", listString);
        
            // Get days that have events for comparing
            if(comparing) {
            	final String compareCurrentMonth = compare + "_" + months[Integer.parseInt(substring[1])-1] + "_" + substring[0];
            	Log.d("compareCurrentMonth", compareCurrentMonth);
            	ParseQuery compareQuery = ParseQuery.getQuery(compareCurrentMonth);
                try {
    				compareDaysInMonth = compareQuery.find();
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                
                listString = "";
                current = "";

                for (ParseObject s : compareDaysInMonth)
                {
                	if(s.get(compareCurrentMonth)!=null) {
                		current = s.get(compareCurrentMonth).toString();
                		listString += current + "\t";
                		compareNamesOfDays.add(current);
                	}
                }
            }
            Log.d("compareDaysInMonth", listString);
    }

    public int getCount() {
            return dayString.size();
    }

    public Object getItem(int position) {
            return dayString.get(position);
    }

    public long getItemId(int position) {
            return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            TextView dayView;
            
            if (convertView == null) { // if it's not recycled, initialize some
                                                                    // attributes
                    LayoutInflater vi = (LayoutInflater) mContext
                                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.calendar_item, null);

            }
            dayView = (TextView) v.findViewById(R.id.date);
            // separates daystring into parts.
            String[] separatedTime = dayString.get(position).split("-");
            // taking last part of date. ie; 2 from 2012-12-02
            String gridvalue = separatedTime[2].replaceFirst("^0*", "");
            // checking whether the day is in current month or not.
            if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
                    // setting offdays to white color.
                    dayView.setTextColor(Color.WHITE);
                    dayView.setClickable(false);
                    dayView.setFocusable(false);
            } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
                    dayView.setTextColor(Color.WHITE);
                    dayView.setClickable(false);
                    dayView.setFocusable(false);
            } else {
                    // setting current month's days in blue color.
                    dayView.setTextColor(Color.BLUE);
            }

            if (dayString.get(position).equals(curentDateString)) {
                    setSelected(v);
                    previousView = v;
            } else {
                    v.setBackgroundResource(R.drawable.list_item_background);
            }
            dayView.setText(gridvalue);

            // create date string for comparison
            String date = dayString.get(position);

            if (date.length() == 1) {
                    date = "0" + date;
            }
            String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
            if (monthStr.length() == 1) {
                    monthStr = "0" + monthStr;
            }

            // Check for event from parse
            String[] substring = date.split("-");
            boolean found = false;
            boolean compareFound = false;
            final String currentDay = currentUser.getUsername() + "_" + months[Integer.parseInt(substring[1])-1] + "_" + substring[2] + "_" + substring[0];
//            Log.d("currentDay : contained",currentDay + " : " + namesOfDays.contains(currentDay));
            if(namesOfDays != null && namesOfDays.contains(currentDay)) found = true;
            
            if(comparing) {
            	final String compareCurrentDay = compare + "_" + months[Integer.parseInt(substring[1])-1] + "_" + substring[2] + "_" + substring[0];
                if(compareNamesOfDays != null && compareNamesOfDays.contains(compareCurrentDay)) compareFound = true;
            }
            Log.d("CalendarAdapter ", position + " found: " + found + "\tcomparing: " + comparing + "\tcompareFound: " + compareFound);
            
            // show icon if date is not empty and it exists in the items array
           ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
           ImageView ix = (ImageView) v.findViewById(R.id.date_iconB);
           ImageView iy = (ImageView) v.findViewById(R.id.date_icons);
            if (date.length() > 0 && items != null && items.contains(date)) {
                iw.setVisibility(View.VISIBLE);
                Log.d("CalendarAdapter", "Visible: date");
            }
            else if(found && compareFound) {
            	iy.setVisibility(View.VISIBLE);
                Log.d("CalendarAdapter", "Visible: iy");
            }
            else if(found) {
            	iw.setVisibility(View.VISIBLE);
                Log.d("CalendarAdapter", "Visible: iw");
            }
            else if(compareFound) {
            	ix.setVisibility(View.VISIBLE);
                Log.d("CalendarAdapter", "Visible: ix");
            }
            else {
                Log.d("CalendarAdapter", "Visible: none");
                iw.setVisibility(View.INVISIBLE);
                ix.setVisibility(View.INVISIBLE);
                iy.setVisibility(View.INVISIBLE);
            }
            found = false;
            return v;
    }

    public View setSelected(View view) {
            if (previousView != null) {
                    previousView.setBackgroundResource(R.drawable.list_item_background);
            }
            previousView = view;
            view.setBackgroundResource(R.drawable.calendar_cel_selectl);
            return view;
    }

    public void refreshDays() {
            // clear items
    		namesOfDays.clear();
    		daysInMonth.clear();
    		compareNamesOfDays.clear();
    		compareDaysInMonth.clear();
            items.clear();
            dayString.clear();
            Locale.setDefault(Locale.US);
            pmonth = (GregorianCalendar) month.clone();
            // month start day. ie; sun, mon, etc
            firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
            // finding number of weeks in current month.
            maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
            // allocating maximum row number for the gridview.
            mnthlength = maxWeeknumber * 7;
            maxP = getMaxP(); // previous month maximum day 31,30....
            calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
            /**
             * Calendar instance for getting a complete gridview including the three
             * month's (previous,current,next) dates.
             */
            pmonthmaxset = (GregorianCalendar) pmonth.clone();
            /**
             * setting the start date as previous month's required date.
             */
            pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

            /**
             * filling calendar gridview.
             */
            for (int n = 0; n < mnthlength; n++) {

                    itemvalue = df.format(pmonthmaxset.getTime());
                    pmonthmaxset.add(GregorianCalendar.DATE, 1);
                    dayString.add(itemvalue);

            }
    }

    private int getMaxP() {
            int maxP;
            if (month.get(GregorianCalendar.MONTH) == month
                            .getActualMinimum(GregorianCalendar.MONTH)) {
                    pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
            } else {
                    pmonth.set(GregorianCalendar.MONTH,
                                    month.get(GregorianCalendar.MONTH) - 1);
            }
            maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

            return maxP;
    }
}
