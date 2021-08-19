package edu.wgu.Stephan_Ward_C196.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.fragment.app.DialogFragment;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import edu.wgu.Stephan_Ward_C196.Utilities.Notifications;
import java.util.Calendar;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import java.util.Locale;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Entity.Assessment;
import edu.wgu.Stephan_Ward_C196.Utilities.DatePickerFragment;
import edu.wgu.Stephan_Ward_C196.Utilities.Converters;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.AdapterView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Creates the the edit assessment model.
 * @author Stephan Ward
 * @since 07/12/2021
 */
public class EditAssessment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //Local sql lite database
    LocalDB db;
    //Boolean to delete assessment from the course details
    boolean assessmentDeleted;
    //Boolean to update assessment from the course details
    boolean assessmentUpdated;
    //Course details date
    Date cDate;
    //Course details due date
    Date dueDate;
    //Course details edit assessment name text
    EditText editAssessmentNameText;
    //Update assessment details floating action button
    ExtendedFloatingActionButton updateAssessmentFAB;
    //Assessment ID integer for the course details
    int assessmentID;
    //Course ID integer for the course details
    int courseID;
    //Intent for the course details
    Intent intent;
    //Term ID for the course details
    int termID;
    //The Simple date formatter for the course details
    SimpleDateFormat formatter;
    //The edit assessment spinner status for the course details
    Spinner editAssessmentStatus;
    //The edit assessment spinner type for the course details
    Spinner editAssessmentType;
    //The course details string name
    String name;
    //The course details status string
    String status;
    //The course details type string
    String type;
    //The course details edit alert string
    Switch editaAlert;
    //The course details edit assessment due date text view
    TextView editAssessmentDueDate;
    //The course details date picker view
    private TextView datePickerView;

    //The on create view instance to edit course details activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Uses R method to find the edit assessment activity set content view
        setContentView(R.layout.activity_edit_assessment);
        intent = getIntent();
        db = LocalDB.getInstance(getApplicationContext());
        termID = intent.getIntExtra("termID", -1);
        courseID = intent.getIntExtra("courseID", -1);
        assessmentID = intent.getIntExtra("assessmentID", -1);
        editAssessmentNameText = findViewById(R.id.editAssessmentNameText);
        editAssessmentType = findViewById(R.id.editAssessmentType);
        editAssessmentStatus = findViewById(R.id.editAssessmentStatus);
        editAssessmentDueDate = findViewById(R.id.editAssessmentDueDate);
        editaAlert = findViewById(R.id.editaAlert);
        updateAssessmentFAB = findViewById(R.id.updateAssessmentButton);
        setupDatePicker();
        setupSpinner();
        //The floating action button on view click listener.
        updateAssessmentFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateAssessment();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (assessmentUpdated) {
                    Intent intent = new Intent(getApplicationContext(), AssessmentDetails.class);
                    intent.putExtra("termID", termID);
                    intent.putExtra("courseID", courseID);
                    intent.putExtra("assessmentID", assessmentID);
                    startActivity(intent);
                }
            }
        });
        setValues();
    }
    //Drop down spinner for the edit assessment view type for selected item for course details.
    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editAssessmentType.setAdapter(adapter);
        editAssessmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Gets edit assessment item position for the course details listening event.
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = editAssessmentType.getItemAtPosition(i).toString();
            }
            //For when not item is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //Second adapter for the edit assessment view status for the selected item for course details.
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.assessment_status_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editAssessmentStatus.setAdapter(adapter2);
        editAssessmentStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Utilizes a on click listener for the selected status item
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = editAssessmentStatus.getItemAtPosition(i).toString();
            }
            @Override
            //For when no item is selected.
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setValues() {
        Assessment assessment = new Assessment();
        assessment = db.assessmentDao().getAssessment(courseID, assessmentID);
        String name = assessment.getAssessment_name();
        String type = assessment.getAssessment_type();
        String status = assessment.getAssessment_status();
        //Applies the date formatter for the course details.
        String dDate = DateFormat.format("MM/dd/yyyy", assessment.getAssessment_due_date()).toString();
        boolean alert1 = assessment.getAssessment_alert();
        editAssessmentNameText.setText(name);
        //Allows the edit of assessment type
        editAssessmentType.setSelection(getIndex(editAssessmentType, type));
        //Allows the edit of the assessment status
        editAssessmentStatus.setSelection(getIndex(editAssessmentStatus, status));
        editAssessmentDueDate.setText(dDate);
        editaAlert.setChecked(alert1);
    }
    //Get index spinner for position
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void updateAssessment() throws ParseException {
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        name = editAssessmentNameText.getText().toString();
        String dDate = editAssessmentDueDate.getText().toString();
        String cDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        boolean alert = editaAlert.isChecked();
        dueDate = formatter.parse(dDate);
        this.cDate = formatter.parse(cDate);
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dDate.trim().isEmpty()) {
            Toast.makeText(this, "Due date is required", Toast.LENGTH_SHORT).show();
            return;
        }
        Assessment assessment = new Assessment();
        assessment.setCourse_id_fk(courseID);
        //Sets the assessment ID for the course details.
        assessment.setAssessment_id(assessmentID);
        assessment.setAssessment_name(name);
        assessment.setAssessment_type(type);
        assessment.setAssessment_status(status);
        //Sets the due date for the the course details.
        assessment.setAssessment_due_date(dueDate);
        assessment.setAssessment_alert(alert);
        db.assessmentDao().updateAssessment(assessment);
        Toast.makeText(this, name + " was updated", Toast.LENGTH_SHORT).show();
        assessmentUpdated = true;
        if (alert) {
            AddAssessmentAlert();
        }
    }

    /**
     * Adds the Assessment Alert
     */
    public void AddAssessmentAlert() {
        //Sets the assessment aleart to be due
        String sText = name + " is due today!";
        setAlert(assessmentID, dueDate, name, sText);
    }

    /**
     * Sets the Assessment Alert
     * @param ID ID of the course
     * @param date Date of the course
     * @param title Title of the Course
     * @param text Text details of the course
     */
    private void setAlert(int ID, Date date, String title, String text) {
        long alertTime = Converters.dateToTimeStamp(date);
        if (dueDate.compareTo(cDate) < 0) {
            return;
        }
        Notifications.setAssessmentAlert(getApplicationContext(), ID, alertTime, title, text);
        Toast.makeText(this, name + " due date alarm enabled", Toast.LENGTH_SHORT).show();
    }

    /**
     * Deletes the assessment
     */
    private void deleteAssessment() {
        Assessment assessment = new Assessment();
        assessment = db.assessmentDao().getAssessment(courseID, assessmentID);
        db.assessmentDao().deleteAssessment(assessment);
        Toast.makeText(this, "Assessment was deleted", Toast.LENGTH_SHORT).show();
        assessmentDeleted = true;
    }

    /**
     * Creates the date picker for the assessment
     */
    private void setupDatePicker() {
        editAssessmentDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerView = findViewById(R.id.editAssessmentDueDate);
                DialogFragment datePicker = new DatePickerFragment();
                //Fragment used for older versions of Android
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        //Sets the calender year
        calendar.set(Calendar.YEAR, year);
        //Sets the calender month
        calendar.set(Calendar.MONTH, month = month + 1);
        //Sets the calender day of month
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = month + "/" + dayOfMonth + "/" + year;
        datePickerView.setText(currentDateString);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_assessment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAssessmentIC:
                deleteAssessment();
                Intent intent = new Intent(getApplicationContext(), CourseDetails.class);
                intent.putExtra("termID", termID);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
