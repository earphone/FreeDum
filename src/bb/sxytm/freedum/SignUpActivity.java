package bb.sxytm.freedum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		setTitle("Sign Up");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}
	
	public void finishSignup(View view) {
		// Take all fields and create a new user in Parse with this data
		// After that go to MainActivity
		Intent intent = new Intent(this, MonthActivity.class);
		EditText usernameText = (EditText) findViewById(R.id.signupUsername);
		EditText nameText = (EditText) findViewById(R.id.signupName);
		EditText emailText = (EditText) findViewById(R.id.signupEmail);
		EditText passwordText = (EditText) findViewById(R.id.signupPassword);
		String username = usernameText.getText().toString();
		String name = nameText.getText().toString();
		String email = emailText.getText().toString();
		String password = passwordText.getText().toString();
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.put("name", name);		// Added in this field
		user.setEmail(email);
		user.setPassword(password);
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if(e == null) {
					// Yay! They have signed up correctly
					// Show a toast that says they are signed up
					Context context = getApplicationContext();
					CharSequence text = "YOU HAVE SIGNED UP SUCCESSFULLY!!!";
					int duration = Toast.LENGTH_SHORT;
					Toast.makeText(context, text, duration).show();
				}
				else {
					// Sign up didn't succeed.  Look at the 
					// ParseException to figure out what went wrong
					// Show a toast that says signup failed
					Context context = getApplicationContext();
					CharSequence text = "SOMETHING WENT WRONG :(\n\t\t\t\tTRY AGAIN";
					int duration = Toast.LENGTH_SHORT;
					Toast.makeText(context, text, duration).show();
				}
			}
		});
		
		// Open up the next activity
		startActivity(intent);
	} // end of finishSignup

}
