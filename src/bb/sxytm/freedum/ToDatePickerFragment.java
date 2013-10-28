package bb.sxytm.freedum;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

public class ToDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	static EditText ToDateEdit;

	
	
	@Override
      public Dialog onCreateDialog(Bundle savedInstanceState) {
          // Use the current date as the default date in the picker
          final Calendar c = Calendar.getInstance();
          int year = c.get(Calendar.YEAR);
          int month = c.get(Calendar.MONTH);
          int day = c.get(Calendar.DAY_OF_MONTH);

          // Create a new instance of DatePickerDialog and return it
          return new DatePickerDialog(getActivity(), this, year, month, day);
      }

      public void onDateSet(DatePicker view, int year, int month, int day) {
          // Do something with the date chosen by the user
    	  LayoutInflater inflater = getActivity().getLayoutInflater();
    	  View inflatorView = inflater.inflate(R.layout.activity_new_event, null);
    	  ToDateEdit = (EditText) inflatorView.findViewById(R.id.newEventToDate);
          ToDateEdit.setText(day + "/" + (month + 1) + "/" + year);
      }
}
