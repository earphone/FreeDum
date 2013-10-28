package bb.sxytm.freedum;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

public class ToTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	
	static EditText ToTimeEdit;
	
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
  	  	LayoutInflater inflater = getActivity().getLayoutInflater();
  	  	View inflatorView = inflater.inflate(R.layout.activity_new_event, null);
  	    ToTimeEdit = (EditText) inflatorView.findViewById(R.id.newEventToTime);
		ToTimeEdit.setText(ToTimeEdit.getText() + " -" + hourOfDay + ":"    + minute);
    }
}
