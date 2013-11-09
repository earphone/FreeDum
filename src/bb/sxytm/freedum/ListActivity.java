package bb.sxytm.freedum;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

import com.parse.Parse;
import com.parse.ParseUser;

public class ListActivity extends Activity {

	int year, month, day;
	public final static String EXTRA_MESSAGE = "bb.sxytm.freedum.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
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
            }
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.listNewEvent:
        	newEvent();
        	return true;
        case R.id.listMonthView:
        	monthView();
        	return true;
    	case R.id.Logout:
    		// If logout clicked, then logout :p
    		logout();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
        }
	 }
        
	
    public void logout() {
    	// Logout user if this is selected
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
    
    public void newEvent() {
    	// Send user to create a new event if pressed
    	Intent intent = new Intent(this, NewEventActivity.class);
    	startActivity(intent);
    }
    
    public void monthView() {
    	// Send user to month view if pressed
    	Intent intent = new Intent(this, MonthActivity.class);
    	startActivity(intent);
    	finish();
    }
    
    // Called when the user clicks on a day
    public void sendMessage(int sMonth, int sDay, int sYear) {
    	// Do something in response to button
    	Intent intent = new Intent(this, NewEventActivity.class);
    	String message = sMonth + " " + sDay + " " + sYear;
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }
}
