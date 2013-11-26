package bb.sxytm.freedum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// Parse: Initialize
		Parse.initialize(this, "GsPuRscxaR95D6ELavshA2X9zsGfIMnqHegQgeo5", "KRzPHVrLf0mCL8pQfkNFmQfhefdKuN9OBnkO5cP7"); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Parse: Used to track how many times app is opened
		ParseAnalytics.trackAppOpened(getIntent());
		// Parse: Check if current user is signed in
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser != null) {
			// Parse: User logged in so send to MonthActivity
			Intent intent = new Intent(this,MonthActivity.class);
			startActivity(intent);
			finish();
		}
		else {
			// Parse: User not logged in so stay in this activity
		}
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void loginLogin(View view) {
		// Use text from username and password fields to login user
		// If username and password match then go to Main
		// If username and password don't match the return to this activity
		EditText usernameText = (EditText) findViewById(R.id.usernameLogin);
		EditText passwordText = (EditText) findViewById(R.id.passwordLogin);
		final String username = usernameText.getText().toString();
		final String password = passwordText.getText().toString();

		ParseUser.logInInBackground(username, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if(e == null) {
					// Yes the user is valid and logged in
					Context context = getApplicationContext();
					CharSequence text = "WELCOME BACK ";
					int duration = Toast.LENGTH_SHORT;
					Toast.makeText(context, text + username, duration).show();
					Intent intent = new Intent(context, MonthActivity.class);
					startActivity(intent);
					finish();
				} else {
					// Signin failed
					// Return back to this activity
					Context context = getApplicationContext();
					CharSequence text = "OH NO! SOMETHING WENT WRONG!! TRY AGAIN!!!";
					int duration = Toast.LENGTH_LONG;
					Toast.makeText(context, text, duration).show();
					Intent intent = getIntent();
					startActivity(intent);
				}
			}
		});
	} // end of loginLogin

	public void loginSignup(View view) {
		// Go from login page to sign up page
		Intent intent = new Intent(this, SignUpActivity.class);
		startActivity(intent);
		finish();
	} // end of loginSignup
	
}
