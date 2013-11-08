package bb.sxytm.freedum;

import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseUser;

public class NewEventActivity extends Activity {
		int hour,min,day,month,year;
		static final int TIME_DIALOG_ID = 0;
		static final int DATE_DIALOG_ID = 1;
		static boolean TO_FROM_TIME = true;		// true if toTime
		static boolean TO_FROM_DATE = true;		// true if toDate
		String startDay = "";
		String startMonth = "";
		public static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

		
		// OnCreate sets the dates and times to current
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_new_event);
	        Calendar c=Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH);
	        int day = c.get(Calendar.DAY_OF_MONTH);
	        EditText FromDateEdit = (EditText) findViewById(R.id.newEventFromDate);
	  	  	String text = (month + 1) + "/" + day + "/" + year;
	  	  	FromDateEdit.setText(text);
	  	    EditText ToDateEdit = (EditText) findViewById(R.id.newEventToDate);
		  	ToDateEdit.setText(text);
		  	EditText FromTimeEdit = (EditText) findViewById(R.id.newEventFromTime);
	  	  	int hour = c.get(Calendar.HOUR_OF_DAY);
	  	  	min = c.get(Calendar.MINUTE);
		  	text = hour + ":" + min;
	  	  	FromTimeEdit.setText(text);
	  	    EditText ToTimeEdit = (EditText) findViewById(R.id.newEventToTime);
		  	ToTimeEdit.setText(text);
		  	startMonth = months[month] + "_" + String.valueOf(year);
			startDay = months[month] + "_" +String.valueOf(day) + "_" +  String.valueOf(year);

	    }
	    
	    // Seperate methods called depending on which argument is being set
	    public void showFromTimeDialog(View v) {
	    	showDialog(TIME_DIALOG_ID);
	    	TO_FROM_TIME = true;
	    }
	    
	    public void showToTimeDialog(View v) {
	    	showDialog(TIME_DIALOG_ID);
	    	TO_FROM_TIME = false;
	    }
	    
	    public void showFromDateDialog(View v) {
	    	showDialog(DATE_DIALOG_ID);
	    	TO_FROM_DATE = true;
	    }
	    
	    public void showToDateDialog(View v) {
	    	showDialog(DATE_DIALOG_ID);
	    	TO_FROM_DATE = false;
	    }
	    
	    // Creates a dialog depending on whether it is date or time
	    protected Dialog onCreateDialog(int id) {
	    	final Calendar c = Calendar.getInstance();
	    	switch(id)
	    	{
	    	case TIME_DIALOG_ID:
	    		int hour = c.get(Calendar.HOUR_OF_DAY);
	    		int min = c.get(Calendar.MINUTE);
	    		return new TimePickerDialog(this, timeSetListener, hour, min, true);
	    	
	    	case DATE_DIALOG_ID:
	            int year = c.get(Calendar.YEAR);
	            int month = c.get(Calendar.MONTH);
	            int day = c.get(Calendar.DAY_OF_MONTH);
	    		return new DatePickerDialog(this, dateSetListener, year, month, day);
	    	}
	    	return null;
	    }
	    
	    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int mYear, int monthOfYear,
					int dayOfMonth) {
				EditText et;
				year = mYear;
				month = monthOfYear;
				day = dayOfMonth;
				if(TO_FROM_DATE) {
					et = (EditText)findViewById(R.id.newEventFromDate);
					startMonth = months[month] + "_" + String.valueOf(year);
					startDay = months[month] + "_" +String.valueOf(day) + "_" +  String.valueOf(year);
					Log.d("startMonth", startMonth);
					Log.d("startDay", startDay);
				}
				else et = (EditText)findViewById(R.id.newEventToDate);
				et.setText((month+1) + "/" + day + "/" + year);
			}
		};
	  
		// Both to and from times use the same dialog
	    private TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				EditText et;
				hour=hourOfDay;
				min=minute;
				if(TO_FROM_TIME) et=(EditText)findViewById(R.id.newEventFromTime);
				else et=(EditText)findViewById(R.id.newEventToTime);
				et.setText(hour+":"+min);

			}
		};
		
		// Cancel finishes the activity and does not save anything
		public void cancel(View v) {
			finish();
		}
		
		// Saves the current event to Parse
		public void saveEvent(View v) throws JSONException {
		//	Context context = getApplicationContext();
		//	TextView etext = (TextView)findViewById(R.id.newEventFromDate);
		//	CharSequence text = etext.getText();
		//	int duration = Toast.LENGTH_LONG;
		//	Toast.makeText(context, text, duration).show();
			
			// Get current user and save to day_month_year data in month_year
			ParseUser currentUser = ParseUser.getCurrentUser();
			Context errorContext = getApplicationContext();
			CharSequence doneText = "Saving . . .";
			int duration = Toast.LENGTH_SHORT;
			Toast.makeText(errorContext, doneText, duration).show();
			if(currentUser == null) {
				CharSequence errorText = "You are not signed in!! Nothing saved and sending to Login!!";
				int errorDuration = Toast.LENGTH_LONG;
				Toast.makeText(errorContext, errorText, errorDuration).show();
				Intent intent = new Intent(this, LoginActivity.class);
	        	startActivity(intent);
	        	finish();
			}
			else {
					// Save all event information in a JSON array
					JSONObject event = new JSONObject();
					TextView etext = (TextView)findViewById(R.id.newEventName);
					CharSequence text = etext.getText();
					Log.d("NAME", text.toString());
					event.put("name",text.toString());
					etext = (TextView)findViewById(R.id.newEventFromDate);
					text = etext.getText();
					Log.d("FROM DATE", text.toString());
					event.put("fromDate",text.toString());
					etext = (TextView)findViewById(R.id.newEventToDate);
					text = etext.getText();
					Log.d("TO DATE", text.toString());
					event.put("toDate",text.toString());
					etext = (TextView)findViewById(R.id.newEventFromTime);
					text = etext.getText();
					Log.d("FROM TIME", text.toString());
					event.put("fromTime",text.toString());
					etext = (TextView)findViewById(R.id.newEventToTime);
					text = etext.getText();
					Log.d("TO TIME", text.toString());
					event.put("toTime",text.toString());
					currentUser.put(startMonth, startDay);
					currentUser.put(startDay, event);
					currentUser.saveEventually();
					doneText = ("Congratulations!! Saved: " + startDay);
					duration = Toast.LENGTH_LONG;
					Toast.makeText(errorContext, doneText, duration).show();
					finish();
			}
			
		}
		
        @Override
        protected void onResume() {
                super.onResume();
                
        }
        
        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            // Read values from the "savedInstanceState"-object and put them in your textview
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            // Save the values you need from your textview into "outState"-object
            super.onSaveInstanceState(outState);
        }
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.new_event, menu);
                return true;
        }
	}