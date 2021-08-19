package edu.wgu.Stephan_Ward_C196.Activity;

import android.widget.Toast;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.fragment.app.DialogFragment;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import edu.wgu.Stephan_Ward_C196.Entity.Term;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Utilities.DatePickerFragment;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

/**
 * Creates the the edit term model.
 * @author Stephan Ward
 * @since 07/13/2021
 */
public class EditTerm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //The local database
    LocalDB db;
    //Boolean to delete term when editing term
    boolean termDEL;
    //Boolean to update term when editing term
    boolean termUpdate;
    //Edit text for term name when editing term
    EditText eTermName;
    //Floating Action button to update term when editing term
    ExtendedFloatingActionButton updateTerm_BTN;
    //Course list integer for term when editing
    int courseList;
    //Term edit intent
    Intent intent;
    //Integer for term ID to be edited
    int termID;
    //Simple formatter for date
    SimpleDateFormat formatter;
    //Spinner to edit term status
    Spinner eTermStatus;
    //String status form term status
    String status;
    //Text view for term start date to be edited
    TextView eTermStart;
    //Text view for term end date to be edited
    TextView eTermEnd;
    //Date picker view for the term to be edited.
    private TextView datePickerView;

    /**
     * Creates the view layout for editing the term.
     * @param savedInstanceState The instance to be created to edit the term.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calls the edit term activity layout
        setContentView(R.layout.activity_edit_term);
        intent = getIntent();
        //Calls the local database instance to be populated.
        db = LocalDB.getInstance(getApplicationContext());
        //Intent to added the extra term ID and course list
        termID = intent.getIntExtra("termID", -1);
        courseList = intent.getIntExtra("courseList", -1);
        //R method to edit term Name
        eTermName = findViewById(R.id.editTermName);
        //R method to edit term status
        eTermStatus = findViewById(R.id.editTermStatus);
        //R method to edit term start date
        eTermStart = findViewById(R.id.editSDateTerm);
        //R method to edit term end date
        eTermEnd = findViewById(R.id.editEDateTerm);
        //Float button to update term
        updateTerm_BTN = findViewById(R.id.updateTermFAB);
        //Sets up the edit term date picker
        setupDatePicker();
        //Listener for the edit term status adapter for the spinner drop down item of the R method term array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.term_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eTermStatus.setAdapter(adapter);
        //The term status listener when iterm item is selected
        eTermStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Selects the adapter view of the item selected for the edit term status.
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Sets the term status
                status = eTermStatus.getItemAtPosition(i).toString();
            }
            //Does nothing to the adapter view when no item is selected.
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //Sets the values when the term button is clicked to be updated
        setValues();
        updateTerm_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Will update the stack data
                    updateTerm();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (termUpdate) {
                    //Updates the term details for the term details class.
                    Intent intent = new Intent(getApplicationContext(), TermDetails.class);
                    //Adds extra term ID
                    intent.putExtra("termID", termID);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Sets the term values to be added to the term DAO of the database
     */
    private void setValues() {
        Term term = new Term();
        //Calls the term Dao for the database
        term = db.termDao().getTerm(termID);
        //Gets term string name
        String name = term.getTerm_name();
        //Gets term string status
        String status = term.getTerm_status();
        //Gets term start date that is formatted.
        String sDate = DateFormat.format("MM/dd/yyyy", term.getTerm_start()).toString();
        //Gets the term end date that is formatted.
        String eDate = DateFormat.format("MM/dd/yyyy", term.getTerm_end()).toString();
        //Sets the term Name
        eTermName.setText(name);
        //Sets the edit term status
        eTermStatus.setSelection(getIndex(eTermStatus, status));
        //Sets the edit term start date
        eTermStart.setText(sDate);
        //Sets the edit term end date.
        eTermEnd.setText(eDate);
    }

    /**
     * Gets the edit term index
     * @param spinner Gets edit term item spinner
     * @param myString Applies the string to be compared with
     * @return Edit term item position
     */
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Deletes term from the course list
     */
    private void deleteTerm() {
        if (courseList > 0) {
            //Toast is generated to inform the student to delete course first before deleting the term
            Toast.makeText(this, "Please delete courses first.", Toast.LENGTH_SHORT).show();
            return;
        }
        //new term instance to be made when deleted.
        Term term = new Term();
        //Applies term to the term DAO database when deleted.
        term = db.termDao().getTerm(termID);
        //Method to delete term
        db.termDao().deleteTerm(term);
        //Toast to inform student if term was successfully deleted or not
        Toast.makeText(this, "Term successfully deleted", Toast.LENGTH_SHORT).show();
        //Returns term deleted boolean
        termDEL = true;
    }

    /**
     * Updates term if validated
     * @throws ParseException Error if missing information
     */
    private void updateTerm() throws ParseException {
        //Simple date formatter applied to edit term
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        //String name to edit term
        String name = eTermName.getText().toString();
        //Start date of term to be edited
        String sDate = eTermStart.getText().toString();
        //End date of term to be edited
        String eDate = eTermEnd.getText().toString();
        //Start date formatter for term start date to be edited
        Date stDate = formatter.parse(sDate);
        //End date formatter for term end date to be edited
        Date enDate = formatter.parse(eDate);
        //Validators for whether or not term maybe updated.
        if (name.trim().isEmpty()) {
            //Toast is generated to inform student to enter term name.
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        //st is start en is end
        if (stDate.after(enDate)) {
            //Toast is generated to check whether the end date is after start date
            Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
            return;
        }
        //sdate is start date
        if (sDate.trim().isEmpty()) {
            //Toast to check is the start date is empty
            Toast.makeText(this, "Please Enter Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        //eDate is end date
        if (eDate.trim().isEmpty()) {
            //Toast to check if the end date is empty
            Toast.makeText(this, "Please Enter End Date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Sets the term items to be updated in the Term Dao
        Term term = new Term();
        //Sets the term ID
        term.setTerm_id(termID);
        //Sets the term name
        term.setTerm_name(name);
        //Sets the term status
        term.setTerm_status(status);
        //Sets the term start date
        term.setTerm_start(stDate);
        //Sets the term end date
        term.setTerm_end(enDate);
        //Applies updatable items to the term Dao database
        db.termDao().updateTerm(term);
        //Toast is created if term is successfully updated
        Toast.makeText(this, "Term successfully updated.", Toast.LENGTH_SHORT).show();
        //Returns term updated boolean
        termUpdate = true;
    }

    /**
     * Sets the date picker for the term to be edited
     */
    private void setupDatePicker() {
        //Term on click listening event for the term date date
        eTermStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Date Picker View for the R method edit term start date.
                datePickerView = findViewById(R.id.editSDateTerm);
                //Calls the date picker fragment for older android phones.
                DialogFragment datePicker = new DatePickerFragment();
                //Date picker support fragment manager is called.
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        //Term on click listening event for the term end date
        eTermEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Date Picker View for the R method edit term end date
                datePickerView = findViewById(R.id.editEDateTerm);
                //Calls the date picker fragment for older android phones.
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    /**
     * Sets the edit term dates.
     * @param view View for the edit term dates
     * @param year Year for the edit term date.
     * @param month Month for the edit term date.
     * @param dayOfMonth Day for the edit term date
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Gets the calender instance
        Calendar calendar = Calendar.getInstance();
        //Calender year to be set
        calendar.set(Calendar.YEAR, year);
        //Calender month to be set
        calendar.set(Calendar.MONTH, month = month + 1);
        //Calender day to be set
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //String for the current date
        String currentDateString = month + "/" + dayOfMonth + "/" + year;
        //Date picker view string that sets the string text
        datePickerView.setText(currentDateString);
    }

    /**
     * Creates the edit term options inflated
     * @param menu Menu view of term to be deleted
     * @return Term to be deleted
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        //Term to be deleted
        menuInflater.inflate(R.menu.delete_term, menu);
        return true;
    }
    //Selected term to be deleted from the view
    @Override
    //Term item from menu to be selected
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Case of selected term to be deleted
            case R.id.deleteTermIC:
                //Method to delete term.
                deleteTerm();
                //Extends the term list class, the selected term to be deleted.
                Intent intent = new Intent(getApplicationContext(), TermList.class);
                //Extends the term ID
                intent.putExtra("termID", termID);
                startActivity(intent);
                return true;
            default:
                //The term ID selected to be deleted.
                return super.onOptionsItemSelected(item);
        }
    }
}