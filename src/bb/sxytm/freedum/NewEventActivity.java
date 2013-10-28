package bb.sxytm.freedum;

import java.util.Calendar;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewEventActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_event);
		final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        TextView FromDateEdit = (TextView) findViewById(R.id.newEventFromDate);
  	  	String text = (month + 1) + "/" + day + "/" + year;
  	  	FromDateEdit.setText(text);
  	    TextView ToDateEdit = (TextView) findViewById(R.id.newEventToDate);
	  	ToDateEdit.setText(text);
	  	TextView FromTimeEdit = (TextView) findViewById(R.id.newEventFromTime);
  	  	int hour = c.get(Calendar.HOUR);
  	  	if(hour == 0) hour = 12;
  	  	int apm = c.get(Calendar.AM_PM);
  	  	String pam = "AM";
  	  	if(apm == 0) pam = "AM";
  	  	if(apm == 1) pam = "PM";
  	  	int min = c.get(Calendar.MINUTE);
	  	text = hour + ":" + min + pam;
  	  	FromTimeEdit.setText(text);
  	    TextView ToTimeEdit = (TextView) findViewById(R.id.newEventToTime);
	  	ToTimeEdit.setText(text);
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
	
	public void showFromTimePicker(View v) {
		DialogFragment newFragment = new FromTimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void showFromDatePicker(View v) {
		DialogFragment newFragment = new FromDatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}
	public void showToTimePicker(View v) {
		DialogFragment newFragment = new FromTimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void showToDatePicker(View v) {
		DialogFragment newFragment = new FromDatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public void cancel(View v) {
		finish();
	}
	
	public void save(View v) {
		Context context = getApplicationContext();
		TextView etext = (TextView)findViewById(R.id.newEventFromDate);
		CharSequence text = etext.getText();
		int duration = Toast.LENGTH_LONG;
		Toast.makeText(context, text, duration).show();
	}
}
