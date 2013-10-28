package bb.sxytm.freedum;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class FromDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	//static EditText FromDateEdit;
	
	@Override
      public Dialog onCreateDialog(Bundle savedInstanceState) {
          // Use the current date as the default date in the picker
          final Calendar c = Calendar.getInstance();
          int year = c.get(Calendar.YEAR);
          int month = c.get(Calendar.MONTH);
          int day = c.get(Calendar.DAY_OF_MONTH);
          
          LayoutInflater inflater = getActivity().getLayoutInflater();
    	  View inflatorView = inflater.inflate(R.layout.activity_new_event, null);
    	  TextView FromDateEdit = (TextView) inflatorView.findViewById(R.id.newEventFromDate);
    	  String text = day + "/" + (month + 1) + "/" + year;
          FromDateEdit.setText(text);
          // Create a new instance of DatePickerDialog and return it
          return new DatePickerDialog(getActivity(), this, year, month, day);
      }

      public void onDateSet(DatePicker view, int year, int month, int day) {
          // Do something with the date chosen by the user
    	  LayoutInflater inflater = getActivity().getLayoutInflater();
    	  View inflatorView = inflater.inflate(R.layout.activity_new_event, null);
    	  EditText FromDateEdit = (EditText) inflatorView.findViewById(R.id.newEventFromDate);
    	  String text = day + "/" + (month + 1) + "/" + year;
          FromDateEdit.setText(text);
      }
}
