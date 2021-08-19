package edu.wgu.Stephan_Ward_C196.Utilities;

import android.app.Dialog;
import android.app.DatePickerDialog;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.annotation.Nullable;
import java.util.Calendar;

/**
 * A date picker fragment that is extended throughout the application.
 * @author Stephan Ward
 * @since 07/15/2021
 */
public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    //Calls the calender bundled saved instance state
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Gets instance of a Calender
        Calendar calendar = Calendar.getInstance();
        //Assigns the year integer
        int year = calendar.get(Calendar.YEAR);
        //Assigns the month integer
        int month = calendar.get(Calendar.MONTH);
        //Assigns the day integer
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //Returns the new date picker activity
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }
}