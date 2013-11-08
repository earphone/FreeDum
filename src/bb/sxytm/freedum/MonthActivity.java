package bb.sxytm.freedum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

public class MonthActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month);
		Parse.initialize(this, "GsPuRscxaR95D6ELavshA2X9zsGfIMnqHegQgeo5", "KRzPHVrLf0mCL8pQfkNFmQfhefdKuN9OBnkO5cP7"); 

	}

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
    		newEvent();
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
    	// Send user to create a new activity if button pressed
		Intent intent = new Intent(this, NewEventActivity.class);
		startActivity(intent);
    }
    
    public void listView() {
    	// Send user to list view if button pressed
    	Intent intent = new Intent(this, ListActivity.class);
    	startActivity(intent);
    	finish();
    }
}
