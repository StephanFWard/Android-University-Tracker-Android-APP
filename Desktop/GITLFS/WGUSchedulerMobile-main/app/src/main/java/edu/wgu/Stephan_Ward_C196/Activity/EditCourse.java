package edu.wgu.Stephan_Ward_C196.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.fragment.app.DialogFragment;
import java.text.SimpleDateFormat;
import edu.wgu.Stephan_Ward_C196.Utilities.Notifications;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import java.util.Locale;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Entity.Course;
import edu.wgu.Stephan_Ward_C196.Utilities.DatePickerFragment;
import edu.wgu.Stephan_Ward_C196.Utilities.Converters;
import android.app.DatePickerDialog;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.telephony.SmsManager;
import android.os.Bundle;
import android.view.Menu;
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

/**
 * Creates the the edit course model.
 * @author Stephan Ward
 * @since 07/12/2021
 */
public class EditCourse extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //Private final static int to set SMS permission request
    private static final int PERMISSIONS_REQUEST_SMS = 0;
    //Boolean to delete the course
    boolean courseDEL;
    //Boolean to update course
    boolean courseUpdate;
    //Extended floating action button to send SMS message notes
    ExtendedFloatingActionButton eSend_BTN;
    //Extended floating action button to update course
    ExtendedFloatingActionButton updateCourse_BTN;
    //The course date
    Date courseDate;
    //The course end date
    Date endDate;
    //The course start date
    Date startDate;
    //The edit SMS text for the phone number
    EditText eSMS_Number;
    //The edit course name text
    EditText eCourseNameTxt;
    //The edit course notes text
    EditText eNotes;
    //The integer for the assessment list
    int assessmentList;
    //The integer for the courseID to edit course
    int courseID;
    //The intent for the edit course
    Intent intent;
    //The teacher's list for the edit course
    int teacherList;
    //The integer for the term ID
    int termID;
    //Calls the local database for the edit course
    LocalDB db;
    //The simple date formatter for the edit course
    SimpleDateFormat formatter;
    //The dropdown spinner for the edit course status
    Spinner eCourseStatus;
    //The message string for the edit course
    String message;
    //The teacher's name for the edit course
    String name;
    //The teacher's phone for the edit course
    String phone;
    //The student's status for the edit course
    String status;
    //The edit course alert switch
    Switch editCourseAlert;
    //The edit text for the course start date
    TextView eStartCourse;
    //The edit text for the course end date
    TextView eEndCourse;
    //The date picker view for the edit course
    private TextView datePickerView;

    /**
     * The on create view instance to edit course activity
     * @param savedInstanceState The instance activity to edit the course
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calls the R method to set the content view for the edit activity of the course.
        setContentView(R.layout.activity_edit_course);
        intent = getIntent();
        //Calls the local database instance
        db = LocalDB.getInstance(getApplicationContext());
        //Gets the term ID intent
        termID = intent.getIntExtra("termID", -1);
        //Gets the course ID intent
        courseID = intent.getIntExtra("courseID", -1);
        //Gets the teachers list intent
        teacherList = intent.getIntExtra("teacherList", -1);
        assessmentList = intent.getIntExtra("assessmentList", -1);
        //Utilizes the R method to edit course name text
        eCourseNameTxt = findViewById(R.id.editCourseNameText);
        //Utilizes the R method to edit course status
        eCourseStatus = findViewById(R.id.editCourseStatus);
        //Utilizes the R method to edit start date of course
        eStartCourse = findViewById(R.id.editSCourseTerm);
        eEndCourse = findViewById(R.id.editECourseTerm);
        editCourseAlert = findViewById(R.id.editCourseAlert);
        eNotes = findViewById(R.id.editCourseNotes);
        //R method for the update course floating update course button
        updateCourse_BTN = findViewById(R.id.updateCourseFAB);
        //R method for the Send message floating button
        eSend_BTN = findViewById(R.id.ecSendFAB);
        //R method view for the Send number to the SMS handler
        eSMS_Number = findViewById(R.id.ecSendNumber);
        //Sets the date picker to edit the course
        setupDatePicker();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.course_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Calls the course status adapter for the listener
        eCourseStatus.setAdapter(adapter);
        eCourseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Updates for the adapter view when item is selected.
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = eCourseStatus.getItemAtPosition(i).toString();
            }
            //Does nothing when no item is not selected for the adapter view
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //Sets the course values.
        setValues();
        //Update course button via on click listener
        updateCourse_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Updates the course when button is clicked and prints it to the stack
            public void onClick(View view) {
                try {
                    updateCourse();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //puts the the term ID and course ID on click.
                if (courseUpdate == true) {
                    Intent intent = new Intent(getApplicationContext(), CourseDetails.class);
                    //Adds extended termID to the event
                    intent.putExtra("termID", termID);
                    //Adds extended courseID to the event
                    intent.putExtra("courseID", courseID);
                    startActivity(intent);
                }
            }
        });
        //Creates the edit course send button on click listener
        eSend_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Updates the view
            public void onClick(View view) {
                //Checks for permission to send
                messagePermission();
            }
        });
    }

    /**
     * Sets the values for the edit course
     */
    private void setValues() {
        Course course = new Course();
        //Calls the database course DAO data to set the data
        course = db.courseDao().getCourse(termID, courseID);
        //String to get course name
        String name = course.getCourse_name();
        //String to get course status
        String status = course.getCourse_status();
        //String to get the formatted course start date utilizing to string method
        String sDate = DateFormat.format("MM/dd/yyyy", course.getCourse_start()).toString();
        ////String to get the formatted course end date utilizing to string method
        String eDate = DateFormat.format("MM/dd/yyyy", course.getCourse_end()).toString();
        //Creates the course alert boolean
        boolean alert1 = course.getCourse_alert();
        //Gets the course notes for the edit course
        String notes = course.getCourse_notes();
        //Gets the course name to be edited
        eCourseNameTxt.setText(name);
        //Gets the course status to be edited
        eCourseStatus.setSelection(getIndex(eCourseStatus, status));
        //Gets the course start date to be edited
        eStartCourse.setText(sDate);
        //Gets the course end date to be edited
        eEndCourse.setText(eDate);
        //Sets the check alert button to be edited
        editCourseAlert.setChecked(alert1);
        //Gets the course notes to be edited.
        eNotes.setText(notes);
    }

    /**
     * Gets the spinner count to iterates for item position
     * @param spinner Spinner to be iterated
     * @param myString String to be compared against
     * @return Spinner to be returned
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
     * Sets up the date picker for the start course term
     */
    private void setupDatePicker() {
        //Listener for the edit course start date.
        eStartCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Date picker to edit start course term
                datePickerView = findViewById(R.id.editSCourseTerm);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        //Listener for the edit course end date
        eEndCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Date picker to edit end course term
                datePickerView = findViewById(R.id.editECourseTerm);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    /**
     * Deletes course actions
     */
    private void deleteCourse() {
        if (teacherList > 0) {
            //Toast is created to inform user that they must delete the teacher first before the course maybe deleted.
            Toast.makeText(this, "Teachers must be deleted to delete course.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (assessmentList > 0) {
            //Toast is created to inform user that they must delete the assessments first before the course maybe deleted.
            Toast.makeText(this, "Assessments must be deleted to delete course.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Confirms if course was deleted
        Course course = new Course();
        course = db.courseDao().getCourse(termID, courseID);
        //Deletes the course from the database
        db.courseDao().deleteCourse(course);
        Toast.makeText(this, "Course was deleted", Toast.LENGTH_SHORT).show();
        courseDEL = true;
    }

    /**
     * Updates the course in the database
     * @throws ParseException Throws exception if course can not be deleted.
     */
    private void updateCourse() throws ParseException {
        //Gets the Simple date formatter for the update course string to edit the course
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        name = eCourseNameTxt.getText().toString();
        //Start date to be updated when edited
        String sDate = eStartCourse.getText().toString();
        //End date to be updated when edited
        String eDate = eEndCourse.getText().toString();
        //Notes to be updated when edited
        String notes = eNotes.getText().toString();
        //The course date to be updated when edited
        String cDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        boolean alert = editCourseAlert.isChecked();
        //Formats the string parse start date when updated
        startDate = formatter.parse(sDate);
        //Formats the string end date of the course to be edited
        endDate = formatter.parse(eDate);
        //Formats the course state of the course to be edited
        this.courseDate = formatter.parse(cDate);

        //Toast to inform student to enter name
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast to inform student that the start date must be before the end date.
        if (startDate.after(endDate)) {
            Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast to inform student that the start date can not be empty
        if (sDate.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast to inform student that the end date can not be empty
        if (eDate.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter End Date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Notes must not empty
        if (notes.trim().isEmpty()) {
            notes = " ";
        }
        //Sets the course data to be updated.
        Course course = new Course();
        //Term ID to be set
        course.setTerm_id_fk(termID);
        //Course ID to be set
        course.setCourse_id(courseID);
        //Course name to be set
        course.setCourse_name(name);
        //Course start date to be set
        course.setCourse_start(startDate);
        //Course end date to be set
        course.setCourse_end(endDate);
        //Course status to be set
        course.setCourse_status(status);
        //Course notes to be set
        course.setCourse_notes(notes);
        //Course alert to be set
        course.setCourse_alert(alert);
        //updates the course Dao to the database
        db.courseDao().updateCourse(course);
        //Toast message to confirm that the course was updated.
        Toast.makeText(this, name + " was updated", Toast.LENGTH_SHORT).show();
        courseUpdate = true;
        //Alert if true
        if (alert) {
            AddCourseAlert();
        }
    }

    /**
     * Adds the court alert if course begins or ends today.
     */
    public void AddCourseAlert() {
        //string text for the course to begin today
        String sText = name + " begins today!";
        //String text for the course to end today
        String eText = name + " ends today!";
        //Sets the alert start date and end date to the string
        setAlert(courseID, startDate, name, sText);
        //The end date text string.
        setAlert(courseID, endDate, name, eText);
    }

    /**
     * Sets the course date alarm string when course date is received
     * @param ID ID of the course
     * @param date Date of the course
     * @param title Title of the course
     * @param text The text description of the course
     */
    private void setAlert(int ID, Date date, String title, String text) {
        //Receives the long alert timestamp
        long alertTime = Converters.dateToTimeStamp(date);
        //Compares the course date to the time stamp
        if (date.compareTo(courseDate) < 0) {
            return;
        }
        //Applies the alarm course alarm
        Notifications.setCourseAlert(getApplicationContext(), ID, alertTime, title, text);
        //Toast message to student if alarm is enabled
        Toast.makeText(this, "Course alarm is enabled", Toast.LENGTH_SHORT).show();
    }

    /**
     * Applies the date picker for the edit course.
     * @param view View of the edit course
     * @param year Year of the edit course
     * @param month Month of the edit course
     * @param dayOfMonth Day of the edit course
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Applies the calender instance
        Calendar calendar = Calendar.getInstance();
        //Sets the calendar year
        calendar.set(Calendar.YEAR, year);
        //Applies the calender month
        calendar.set(Calendar.MONTH, month = month + 1);
        //Applies the calender day
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //Applies the calender string to be displayed
        String currentDateString = month + "/" + dayOfMonth + "/" + year;
        //Sets the text to the date picker view
        datePickerView.setText(currentDateString);
    }

    /**
     * Sets the message permission to send text message
     */
    protected void messagePermission() {
        //Applies phone number to the SMS string
        phone = eSMS_Number.getText().toString();
        //Applies the notes to the SMS string
        String notes = eNotes.getText().toString();
        //Applies the course name to the SMS string
        String cName = eCourseNameTxt.getText().toString();
        //Creates the SMS message string
        message = "Course: " + cName + "  Notes: " + notes;
        //Validates checks for proper SMS string
        if (phone.trim().isEmpty()) {
            //Toast to inform student to enter phone number
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cName.trim().isEmpty()) {
            //Toast to inform student to enter course name
            Toast.makeText(this, "Please Enter Course Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (notes.trim().isEmpty()) {
            //Toast to inform student to enter notes
            Toast.makeText(this, "Please Enter Notes", Toast.LENGTH_SHORT).show();
            return;
        }
        //Checks the manifest file to see if proper permission was granted to send SMS message and sends
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SMS);
        }
        //Sends the packages request if all checks are okay
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendSMSMessage();
        }
    }

    //Sends the SMS message
    protected void sendSMSMessage() {
        //SMS manager operation is called to ensure message deliver
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, message, null, null);
        //Toast generated to inform student that the SMS note message was sent
        Toast.makeText(getApplicationContext(), "SMS note message sent successfully", Toast.LENGTH_LONG).show();
    }

    /**
     * Calls the permission request to send the SMS message if request code is not granted.
     * @param requestCode Request Code to send SMS message
     * @param permissions Permission to send  SMS message
     * @param grantResult Results to be returned if granted.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_SMS: {
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, message, null, null);
                    //Toast generated if SMS notes were successfully sent after operation
                    Toast.makeText(getApplicationContext(), "SMS note message sent successfully", Toast.LENGTH_LONG).show();
                } else {
                    //Toast generated if SMS notes were not successfully sent.
                    Toast.makeText(getApplicationContext(), "SMS note message failed. Try Again", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    /**
     * Creates the inflater for the delete course options menu
     * @param menu Menu to delete course when editing
     * @return Boolean to confirm inflater
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_course, menu);
        return true;
    }

    /**
     * Selects the menu item to be deleted from the term list
     * @param item The course item to be deleted in the term list
     * @return The select course item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //R method to delete course resource Term ID of the course
            case R.id.deleteCourseIC:
                //Deletes the course
                deleteCourse();
                //Create new intent for the term list class
                Intent intent = new Intent(getApplicationContext(), TermList.class);
                intent.putExtra("termID", termID);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}