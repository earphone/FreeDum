package bb.sxytm.freedum;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewEventActivity extends Activity {
		int hour,min,day,month,year;
		static final int TIME_DIALOG_ID = 0;
		static final int DATE_DIALOG_ID = 1;
		static boolean TO_FROM_TIME = true;
		static boolean TO_FROM_DATE = true;
		
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
				if(TO_FROM_DATE) et = (EditText)findViewById(R.id.newEventFromDate);
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
		public void save(View v) {
			Context context = getApplicationContext();
			TextView etext = (TextView)findViewById(R.id.newEventFromDate);
			CharSequence text = etext.getText();
			int duration = Toast.LENGTH_LONG;
			Toast.makeText(context, text, duration).show();
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