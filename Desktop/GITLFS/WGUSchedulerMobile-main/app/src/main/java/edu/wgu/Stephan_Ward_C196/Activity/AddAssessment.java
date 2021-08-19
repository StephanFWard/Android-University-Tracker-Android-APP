package edu.wgu.Stephan_Ward_C196.Activity;

import android.content.Intent;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import edu.wgu.Stephan_Ward_C196.Entity.Assessment;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import edu.wgu.Stephan_Ward_C196.Utilities.Converters;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Utilities.Notifications;
import edu.wgu.Stephan_Ward_C196.Utilities.DatePickerFragment;

/**
 * Creates the the add assessment model.
 * @author Stephan Ward
 * @since 07/12/2021
 */
public class AddAssessment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //Log Tag string when Assessment is added
    public static String LOG_TAG = "Assessment added";
    LocalDB db;
    boolean assessmentAdded;
    Date courseDate;
    Date due;
    EditText addAssessmentName;
    int assessmentID;
    int courseID;
    Intent intent;
    int termID;
    SimpleDateFormat formatter;
    Spinner addAssessmentStatus;
    Spinner addAssessmentType;
    String name;
    String status;
    String type;
    Switch aAlert;
    TextView addAssessmentDueDate;
    private TextView datePickerView;

    /**
     * Creates the assessment saved instances
     * @param savedInstanceState Saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        intent = getIntent();
        db = LocalDB.getInstance(getApplicationContext());
        termID = intent.getIntExtra("termID", -1);
        courseID = intent.getIntExtra("courseID", -1);
        addAssessmentName = findViewById(R.id.addAssessmentName);
        addAssessmentType = findViewById(R.id.addAssessmentType);
        addAssessmentStatus = findViewById(R.id.addAssessmentStatus);
        addAssessmentDueDate = findViewById(R.id.addAssessmentDueDate);
        aAlert = findViewById(R.id.aAlert);
        setupDatePicker();
        setupSpinner();
    }

    /**
     * Spinner sets up listening event for the type and
     * status drop down list from the R.Layout for the Assessment
     */
    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addAssessmentType.setAdapter(adapter);
        addAssessmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = addAssessmentType.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //Assessment status spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.assessment_status_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addAssessmentStatus.setAdapter(adapter2);
        addAssessmentStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = addAssessmentStatus.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Adds assessment while using simple date format.
     * @throws ParseException Validates required fields
     */
    private void addAssessment() throws ParseException {
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        name = addAssessmentName.getText().toString();
        String dDate = addAssessmentDueDate.getText().toString();
        String cDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        boolean alert = aAlert.isChecked();
        due = formatter.parse(dDate);
        this.courseDate = formatter.parse(cDate);
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dDate.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Due Date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Creates a new Assessment to be added to the DB
        Assessment assessment = new Assessment();
        assessment.setCourse_id_fk(courseID);
        assessment.setAssessment_name(name);
        assessment.setAssessment_type(type);
        assessment.setAssessment_status(status);
        assessment.setAssessment_due_date(due);
        assessment.setAssessment_alert(alert);
        db.assessmentDao().insertAssessment(assessment);
        Toast.makeText(this, name + " was added.", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, name + " was added.");
        assessmentAdded = true;
        if (alert) {
            AddAssessmentAlert();
        }
    }

    /**
     * Adds assessment alert
     */
    public void AddAssessmentAlert() {
        Assessment assessment = new Assessment();
        assessment = db.assessmentDao().getCurrentAssessment(courseID);
        assessmentID = assessment.getAssessment_id();
        String sText = name + " is due today!";
        setAlert(assessmentID, due, name, sText);
    }

    /**
     * Sets the alert parameters
     * @param ID The ID of the alert
     * @param date The dates of the alert
     * @param title The title of the alert
     * @param text The text of the alert
     */
    private void setAlert(int ID, Date date, String title, String text) {
        long alertTime = Converters.dateToTimeStamp(date);
        if (due.compareTo(courseDate) < 0) {
            return;
        }
        Notifications.setAssessmentAlert(getApplicationContext(), ID, alertTime, title, text);
        Toast.makeText(this, name + " due date alarm is enabled", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets up the on click listener for the date picker.
     */
    private void setupDatePicker() {
        addAssessmentDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerView = findViewById(R.id.addAssessmentDueDate);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    /**
     * Sets the assessment date
     * @param view View of Date Picker
     * @param year Year of Date Picker
     * @param month Month of Date Picker
     * @param dayOfMonth Day of Date Picker
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month = month + 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = month + "/" + dayOfMonth + "/" + year;
        datePickerView.setText(currentDateString);
    }

    /**
     * Boolean that returns true if assessment menu is added
     * @param menu Menu of the assessment
     * @return True if option menu inflater is added.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_assessment, menu);
        return true;
    }

    /**
     * A boolean that confirms if the add assessment menu items of Term and Course is added
     * @param item Assessment FAB
     * @return A boolean to return true if Term and Course added.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addAssessmentFAB:
                try {
                    addAssessment();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (assessmentAdded == true) {
                    Intent intent = new Intent(getApplicationContext(), CourseDetails.class);
                    intent.putExtra("termID", termID);
                    intent.putExtra("courseID", courseID);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}