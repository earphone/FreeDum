package bb.sxytm.freedum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MonthActivity extends Activity {
	
	public static String EXTRA_MESSAGE = "bb.sxytm.freedum.MESSAGE";
	public static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
                                                    // marker.
    public ArrayList<String> items; // container to store calendar items which
                                                                    // needs showing the event marker
    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;
    
    Calendar c=Calendar.getInstance();
	int cYear = c.get(Calendar.YEAR);
	int cMonth = c.get(Calendar.MONTH);
	int cDay = c.get(Calendar.DAY_OF_MONTH);
	
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    		Parse.initialize(this, "GsPuRscxaR95D6ELavshA2X9zsGfIMnqHegQgeo5", "KRzPHVrLf0mCL8pQfkNFmQfhefdKuN9OBnkO5cP7"); 
            setContentView(R.layout.activity_month);
            Locale.setDefault(Locale.US);

    		final ParseUser currentUser = ParseUser.getCurrentUser();
            
            rLayout = (LinearLayout) findViewById(R.id.text);
            month = (GregorianCalendar) GregorianCalendar.getInstance();
            itemmonth = (GregorianCalendar) month.clone();

            items = new ArrayList<String>();

            adapter = new CalendarAdapter(this, month);

            GridView gridview = (GridView) findViewById(R.id.gridview);
            gridview.setAdapter(adapter);

            handler = new Handler();
            handler.post(calendarUpdater);

            TextView title = (TextView) findViewById(R.id.title);
            title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

            RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);
            
            previous.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                            setPreviousMonth();
                            refreshCalendar();
                    }
            });

            RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
            next.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                            setNextMonth();
                            refreshCalendar();

                    }
            });

            gridview.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                            // removing the previous view if added
                            if (((LinearLayout) rLayout).getChildCount() > 0) {
                                    ((LinearLayout) rLayout).removeAllViews();
                            }
                            desc = new ArrayList<String>();
                            date = new ArrayList<String>();
                            ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                            String selectedGridDate = CalendarAdapter.dayString
                                            .get(position);
                            Log.d("dayString", selectedGridDate);
                            String[] separatedTime = selectedGridDate.split("-");
                            String gridvalueString = separatedTime[2].replaceFirst("^0*",
                                            "");// taking last part of date. ie; 2 from 2012-12-02.
                            int gridvalue = Integer.parseInt(gridvalueString);
                            // navigate to next or previous month on clicking offdays.
                            if ((gridvalue > 10) && (position < 8)) {
                                    setPreviousMonth();
                                    refreshCalendar();
                            } else if ((gridvalue < 7) && (position > 28)) {
                                    setNextMonth();
                                    refreshCalendar();
                            }
                            ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                                                        
                            for (int i = 0; i < CalendarUtility.startDates.size(); i++) {
                                    if (CalendarUtility.startDates.get(i).equals(selectedGridDate)) {
                                            desc.add(CalendarUtility.nameOfEvent.get(i));
                                    }
                            }
                            
                            // Get data from Parse
                            String[] substring = selectedGridDate.split("-");
                            cMonth = Integer.parseInt(substring[1])-1;
                            cDay = Integer.parseInt(substring[2]);
                            cYear = Integer.parseInt(substring[0]);
                            final String currentDay = currentUser.getUsername() + "_" + months[cMonth] + "_" + cDay + "_" + cYear;
                            Log.d("currentDay",currentDay);
                            ParseQuery<ParseObject> query = ParseQuery.getQuery(currentDay);
                            query = query.orderByAscending("time");
                            query.findInBackground(new FindCallback<ParseObject>() {
                              public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                	desc = new ArrayList<String>();
                                	if(objects == null || objects.size() == 0) return;
                                	Log.d("objects.size", objects.size()+"");
                                	String event = "";
                                	String fTime = "";
                                	String tTime = "";
                                  for(int i = 0; i < objects.size(); i ++) {
                                	  Log.d("i", i+"");
                                	  try {
										event = objects.get(i).getJSONObject(currentDay).getString("name");
										fTime = objects.get(i).getJSONObject(currentDay).getString("fromTime");
										tTime = objects.get(i).getJSONObject(currentDay).getString("toTime");
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
                                	  Log.d("added", event);
                                	  //desc.add(events);
                                	
                                	  Log.d("desc.size", desc.size()+"");
                                	  Log.d("Setting rowText 1", i+"");
                                	  TextView eventTextView = new TextView(MonthActivity.this);

                                	  // set some properties of eventTextView
                                	  eventTextView.setText("Event: " + event);
                                	  eventTextView.setTextColor(Color.BLACK);
                                	  eventTextView.setBackgroundColor(0xFF00FF00);
                                	  eventTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                                	  // add the textview to the linearlayout	
                                	  rLayout.addView(eventTextView);
                                	  
                                	  // set some properties for timeTextView
                                	  TextView timeTextView = new TextView(MonthActivity.this);
                                	  timeTextView.setText("\t\t" + fTime + " - " + tTime);
                                	  timeTextView.setTextColor(Color.BLACK);
                                	  timeTextView.setBackgroundColor(Color.parseColor("#FF00DD"));
                                	  timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                	  rLayout.addView(timeTextView);
                                  }
                                } else {
                                  // Failed
                                }
                              }
                            });

                            Log.d("desc.size", desc.size()+"");
                            if (desc.size() > 0) {
                                    for (int i = 0; i < desc.size(); i++) {
                                    		Log.d("Setting rowText 2", i+"");
                                            TextView rowTextView = new TextView(MonthActivity.this);

                                            // set some properties of rowTextView or something
                                            rowTextView.setText("Event:" + desc.get(i));
                                            rowTextView.setTextColor(Color.BLACK);
                                            rowTextView.setBackgroundColor(Color.LTGRAY);

                                            // add the textview to the linearlayout
                                            rLayout.addView(rowTextView);

                                    }

                            }

                            desc = null;

                    }

            });
            
    }

    protected void setNextMonth() {
            if (month.get(GregorianCalendar.MONTH) == month
                            .getActualMaximum(GregorianCalendar.MONTH)) {
                    month.set((month.get(GregorianCalendar.YEAR) + 1),
                                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
            } else {
                    month.set(GregorianCalendar.MONTH,
                                    month.get(GregorianCalendar.MONTH) + 1);
            }

    }

    protected void setPreviousMonth() {
            if (month.get(GregorianCalendar.MONTH) == month
                            .getActualMinimum(GregorianCalendar.MONTH)) {
                    month.set((month.get(GregorianCalendar.YEAR) - 1),
                                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
            } else {
                    month.set(GregorianCalendar.MONTH,
                                    month.get(GregorianCalendar.MONTH) - 1);
            }

    }

    protected void showToast(String string) {
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

    }

    public void refreshCalendar() {
            TextView title = (TextView) findViewById(R.id.title);

            adapter.refreshDays();
            adapter.notifyDataSetChanged();
            handler.post(calendarUpdater); // generate some calendar items

            title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

            @Override
            public void run() {
                    items.clear();

                    // Print dates of the current week
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String itemvalue;
                    event = CalendarUtility.readCalendarEvent(MonthActivity.this);
                    Log.d("=====Event====", event.toString());
                    Log.d("=====Date ARRAY====", CalendarUtility.startDates.toString());

                    for (int i = 0; i < CalendarUtility.startDates.size(); i++) {
                            itemvalue = df.format(itemmonth.getTime());
                            itemmonth.add(GregorianCalendar.DATE, 1);
                            items.add(CalendarUtility.startDates.get(i).toString());
                    }
                    adapter.setItems(items);
                    adapter.notifyDataSetChanged();
            }
    };
	
/*	int year, month, day;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month);
		Parse.initialize(this, "GsPuRscxaR95D6ELavshA2X9zsGfIMnqHegQgeo5", "KRzPHVrLf0mCL8pQfkNFmQfhefdKuN9OBnkO5cP7"); 

		// Get current month
		Calendar c=Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);    
        
        // Sends user to new event activity if they select a day
        CalendarView calendarView=(CalendarView) findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                    int dayOfMonth) {
            	Toast.makeText(getApplicationContext(), dayOfMonth+" "+month+" "+year, 0).show();
            	sendMessage(dayOfMonth, month, year);
            }
        });
        
		// Get all days with events for current month and put into a List
        	// TO DO
*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.month, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
    	case R.id.monthNewEvent:
    		sendMessage(cMonth, cDay, cYear);
    		return true;
    	case R.id.monthListView:
    		listView();
    		return true;
    	case R.id.Logout:
    		// If logout clicked, then logout :p
    		logout();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
        }
	 }
        
	
    // Logout user if this is selected
    public void logout() {
    	Intent intent = new Intent(this, LoginActivity.class);
    	ParseUser.logOut();
    	ParseUser currentUser = ParseUser.getCurrentUser(); // this is now null
    	if(currentUser == null) {
    		startActivity(intent);
    	}
    	else {
    		Context context = getApplicationContext();
			CharSequence text = "SOMETHING MESSED UP! WE CAN'T LOG YOU OUT!! ";
			int duration = Toast.LENGTH_SHORT;
			Toast.makeText(context, text, duration).show();
    	}
    	finish();
    }

    // Send user to create a new activity if button pressed
    public void newEvent() {
		Intent intent = new Intent(this, NewEventActivity.class);
		startActivity(intent);
    }
    
    // Send user to list view if button pressed
    public void listView() {
    	Intent intent = new Intent(this, ListActivity.class);
    	startActivity(intent);
    	finish();
    }

/*
    // Go to next month when right arrow is clicked
    public void nextMonth(View v) {
    	if(month>11) {
    		month = 1;
    		year++;
    	}
    	else month++;
    	Log.d("month",month + "");
    	
    }
    
    // Go to previous month when left arrow is clicked
    public void previousMonth(View v) {
    	if(month <= 1) {
    		month = 12;
    		year--;
    	}
    	else month--;
    	Log.d("month",month + "");
    	
    }
*/
    
    // Called when the user clicks on a day
    public void sendMessage(int sMonth, int sDay, int sYear) {
    	// Do something in response to button
    	Intent intent = new Intent(this, NewEventActivity.class);
    	String message = sDay + " " + sMonth + " " + sYear;
    	Log.d("Message", message);
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }

}
