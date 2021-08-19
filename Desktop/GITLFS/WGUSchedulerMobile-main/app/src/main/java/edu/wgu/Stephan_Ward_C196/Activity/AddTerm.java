package edu.wgu.Stephan_Ward_C196.Activity;

import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Utilities.DatePickerFragment;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import edu.wgu.Stephan_Ward_C196.Entity.Term;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

/**
 * Creates the the add term model.
 * @author Stephan Ward
 * @since 07/12/2021
 */
public class AddTerm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //Boolean for term to be added
    boolean termAdded;
    EditText termName;
    //Floating action button to save the Term
    ExtendedFloatingActionButton saveTermFAB;
    //Local Database Object
    LocalDB db;
    //Simple date Format
    SimpleDateFormat formatter;
    //Spinner status
    Spinner status;
    //Validate string status
    String statusV;
    //Text View end date
    TextView endDate;
    //Text View Start date
    TextView startDate;
    private TextView datePickerView;

    /**
     * Created add term instance to be entered into the DB
     * @param savedInstanceState To create a new term
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets the R layout to add term activity
        setContentView(R.layout.activity_add_term);
        //Calls database instance
        db = LocalDB.getInstance(getApplicationContext());
        //Calls the save term floating action button view
        saveTermFAB = findViewById(R.id.saveTermButton);
        //Gets the Term Status ID by R method
        status = findViewById(R.id.addTermStatus);
        //Gets the Term Name by R method
        termName = findViewById(R.id.addTermName);
        //Boolean for the term to add.
        termAdded = false;
        //Gets the ID for term start date by R method.
        startDate = findViewById(R.id.addSDateTerm);
        //Gets the ID for term end date by R method
        endDate = findViewById(R.id.addEDateTerm);
        //Setup date picker method.
        setupDatePicker();
        //Creates the R method layout for the drop down spinner Array for selected term
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.term_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Calls the adapter for the term Array
        status.setAdapter(adapter);
        //Listener for the selected term.
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Selected term item status is updated to the the adapter view
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusV = status.getItemAtPosition(i).toString();
            }
            @Override
            //No selected term item status is updated to the adapter view
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //Calls the on click listener to the Saved Term floating action button
        saveTermFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Adds term to the stack
                try {
                    addTerm();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Gets term context and boolean true to add the term to the TermList class
                if (termAdded == true) {
                    Intent intent = new Intent(getApplicationContext(), TermList.class);
                    startActivity(intent);
                }
            }
        });
    }
    //Adds the new term
    private void addTerm() throws ParseException {
        //Applies the simple date format for the time
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        //Term string Name
        String name = termName.getText().toString();
        //Term start date string
        String sDate = startDate.getText().toString();
        //Term end date string
        String eDate = endDate.getText().toString();
        //Formats the term start date
        Date startDate = formatter.parse(sDate);
        //Formats the term end date.
        Date endDate = formatter.parse(eDate);
        //Toast is called for empty name box.
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast is called for date error if start date is not before the end date.
        if (startDate.after(endDate)) {
            Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast is called to enter term start date
        if (sDate.trim().isEmpty()) {
            Toast.makeText(this, "Please enter start date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast is called to enter term end date
        if (eDate.trim().isEmpty()) {
            Toast.makeText(this, "Please enter end date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Adds the new term to the stack
        Term term = new Term();
        //Sets the term name
        term.setTerm_name(name);
        //Sets the term status
        term.setTerm_status(statusV);
        //Sets the term start date
        term.setTerm_start(startDate);
        //Sets the term end date
        term.setTerm_end(endDate);
        //Inserts the term in the termDAO database
        db.termDao().insertTerm(term);
        //Toast message if the term was successfully added to the toast termDao database.
        Toast.makeText(this, name + " was added", Toast.LENGTH_SHORT).show();
        termAdded = true;
    }

    /**
     * Sets up the date Picker
     */
    private void setupDatePicker() {
        //startdate findViewByID(R.id.addSDateTerm; not needs as it could be replaced
        //endDate.........addEDateTerm; same thing
//        startDate = findViewById(R.id.addSDateTerm);
//        endDate = findViewById(R.id.addEDateTerm);
        //Click listening event for the start date
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Adds view for the start date term date picker view
                datePickerView = findViewById(R.id.addSDateTerm);
                //Creates the new date picker dialog fragment used for older android versions
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        //Click listening event for the end date.
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Adds view for the end date term date picker view
                datePickerView = findViewById(R.id.addEDateTerm);
                //Creates the new date picker dialog fragment used for older android versions
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    /**
     * Adds the date picker calender for the term to be added.
     * @param view Date picker view to be added for the term
     * @param year Integer year for the the date for the term
     * @param month Integer month for the date for the term
     * @param dayOfMonth Integer day for the date for the term
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Calls and gets the calendar instance
        Calendar calendar = Calendar.getInstance();
        //Sets the calender year
        calendar.set(Calendar.YEAR, year);
        //Sets the calender month +1
        calendar.set(Calendar.MONTH, month = month + 1);
        //Sets the day of month
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //Creates the current date string
        String currentDateString = month + "/" + dayOfMonth + "/" + year;
        //Sets the date picker view string
        datePickerView.setText(currentDateString);
    }
}