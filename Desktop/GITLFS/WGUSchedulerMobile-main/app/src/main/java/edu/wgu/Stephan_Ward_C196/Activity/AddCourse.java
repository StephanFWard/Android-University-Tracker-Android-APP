package edu.wgu.Stephan_Ward_C196.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import edu.wgu.Stephan_Ward_C196.Entity.Course;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Utilities.Converters;
import edu.wgu.Stephan_Ward_C196.Utilities.DatePickerFragment;
import edu.wgu.Stephan_Ward_C196.Utilities.Notifications;

/**
 * Creates the add course model.
 * @author Stephan Ward
 * @since 07/12/2021
 */
public class AddCourse extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //Requests the phone state and SMS permissions
    private static final int PERMISSIONS_REQUEST_SMS = 0;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    LocalDB db;
    boolean courseAdded;
    Date courseDate;
    Date endDate;
    Date startDate;
    EditText acSendNumber;
    EditText addCourseNameText;
    EditText addCourseNotes;
    ExtendedFloatingActionButton acSendFAB;
    int courseID;
    Intent intent;
    int termID;
    SimpleDateFormat formatter;
    Spinner addCourseStatus;
    String message;
    String name;
    String phone;
    String statusV;
    Switch addCourseAlert;
    TextView addECourseTerm;
    TextView addSCourseTerm;
    private TextView datePickerView;

    /**
     * Creates the the add course model.
     * @author Stephan Ward
     * @since 07/12/2021
     * @param savedInstanceState Saved Instance State of the local DB
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        intent = getIntent();
        db = LocalDB.getInstance(getApplicationContext());
        //Adds term ID
        termID = intent.getIntExtra("termID", -1);
        //Utilizes R method to receive view by ID
        addCourseNameText = findViewById(R.id.addCourseNameText);
        addCourseStatus = findViewById(R.id.addCourseStatus);
        addSCourseTerm = findViewById(R.id.addSCourseTerm);
        addECourseTerm = findViewById(R.id.addECourseTerm);
        addCourseAlert = findViewById(R.id.addCourseAlert);
        addCourseNotes = findViewById(R.id.addCourseNotes);
        //Add course Send Button
        acSendFAB = findViewById(R.id.acSendbutton);
        //Add course Send Number
        acSendNumber = findViewById(R.id.acSendNumber);
        setupDatePicker();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.course_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addCourseStatus.setAdapter(adapter);
        //A item select listener for the add course status
        addCourseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusV = addCourseStatus.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //Sends the add course message when button is clicked.
        acSendFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagePermission();
            }
        });
    }

    /**
     * Collects and Validates the add course data
     * @throws ParseException Error is thrown if add course data is not valid.
     */
    private void addCourse() throws ParseException {
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        name = addCourseNameText.getText().toString();
        //Start Date to string
        String sDate = addSCourseTerm.getText().toString();
        //End Date to string
        String eDate = addECourseTerm.getText().toString();
        //Notes to string
        String notes = addCourseNotes.getText().toString();
        //Calender date locale simple date formatter
        String cDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        boolean alert = addCourseAlert.isChecked();
        //Parses and formats the start date.
        startDate = formatter.parse(sDate);
        //Parses and formats the end date.
        endDate = formatter.parse(eDate);
        this.courseDate = formatter.parse(cDate);
        //Creates toast to enter name
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        //Creates toast to start date end date error
        if (startDate.after(endDate)) {
            Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Creates toast if start date is not entered
        if (sDate.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Creates toast if end date is not entered
        if (eDate.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter End Date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Empty space
        if (notes.trim().isEmpty()) {
            notes = " ";
        }
        //Creates new course primary key in relationship to Term ID foreign key
        Course course = new Course();
        //Sets the foreign key term ID for the course
        course.setTerm_id_fk(termID);
        //Sets the course name
        course.setCourse_name(name);
        //Sets the course start date
        course.setCourse_start(startDate);
        //Sets the course end date
        course.setCourse_end(endDate);
        //Sets the course status
        course.setCourse_status(statusV);
        //Sets the course notes
        course.setCourse_notes(notes);
        //Sets the course alert
        course.setCourse_alert(alert);
        //Inserts the course into the course Dao DB
        db.courseDao().insertCourse(course);
        //Creates toast if course was added to the Dao DB.
        Toast.makeText(this, name + " was added", Toast.LENGTH_SHORT).show();
        courseAdded = true;
        if (alert) {
            AddCourseAlert();
        }
    }

    /**
     * Course alert created for notification of when the course date begins or ends
     */
    public void AddCourseAlert() {
        Course course = new Course();
        course = db.courseDao().getCurrentCourse(termID);
        courseID = course.getCourse_id();
        String sText = name + "begins today!";
        String eText = name + "ends today!";
        //Calls the start date alert
        setAlert(courseID, startDate, name, sText);
        //Calls the end date alert
        setAlert(courseID, endDate, name, eText);
    }

    /**
     * Sets the alert created for notification of when the course date begins or ends.
     * @param ID ID of the course
     * @param date Date of the course
     * @param title Title of the course
     * @param text Text description of the course
     */
    private void setAlert(int ID, Date date, String title, String text) {
        //Time Stamp converter to compare course date.
        long alertTime = Converters.dateToTimeStamp(date);
        if (date.compareTo(courseDate) < 0) {
            return;
        }
        Notifications.setCourseAlert(getApplicationContext(), ID, alertTime, title, text);
        Toast.makeText(this, "Alarm for " + title + "added", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets up the add course date picker
     */
    private void setupDatePicker() {
        //Adds date picker for course start date to add.
        addSCourseTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerView = findViewById(R.id.addSCourseTerm);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        //Adds date picker for course end date to add.
        addECourseTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerView = findViewById(R.id.addECourseTerm);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    /**
     * Sets the add course date with the date picker information
     * @param view Date picker view
     * @param year Year of course to be added
     * @param month Month of course to be added
     * @param dayOfMonth Day of course to be added
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
     * Manages add course send number permissions for SMS
     */
    protected void messagePermission() {
        phone = acSendNumber.getText().toString();
        String notes = addCourseNotes.getText().toString();
        String cName = addCourseNameText.getText().toString();
        message = "Course: " + cName + "  Notes: " + notes;
        //Creates toast to make sure student enters phone number
        if (phone.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        //Creates toast to enter course name.
        if (cName.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Course Name", Toast.LENGTH_SHORT).show();
            return;
        }
        //Creates toast to add notes.
        if (notes.trim().isEmpty()) {
            Toast.makeText(this, "Please Add Notes", Toast.LENGTH_SHORT).show();
            return;
        }
        //Checks the phone state to see if permission was granted and sends the SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SMS);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                sendSMSMessage();
            }
        }
    }

    /**
     * Creates the SMS manager operation for the sending of data text
     */
    protected void sendSMSMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS notes message successfully sent", Toast.LENGTH_LONG).show();
    }

    /**
     * SMS data message is granted permission to send text or else denied
     * @param requestCode Request of the SMS text data
     * @param permissions Permission string of the SMS text data
     * @param grantResult Grant privilege to send the SMS text data
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_SMS: {
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS notes message successfully sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "SMS notes message failed, try again", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    /**
     * Adds the add course inflater create options menu
     * @param menu Menu of the course that is to be added
     * @return True if the inflater for the add course menu is called
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_course, menu);
        return true;
    }

    /**
     * Selects the add course item to be added
     * @param item Course item to be added.
     * @return True boolean for the course item to be added.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCourseFAB:
                try {
                    addCourse();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (courseAdded == true) {
                    Intent intent = new Intent(getApplicationContext(), TermDetails.class);
                    intent.putExtra("termID", termID);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}