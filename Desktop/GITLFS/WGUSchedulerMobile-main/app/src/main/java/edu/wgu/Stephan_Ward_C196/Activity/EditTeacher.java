package edu.wgu.Stephan_Ward_C196.Activity;

import android.widget.Toast;
import android.widget.EditText;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import edu.wgu.Stephan_Ward_C196.Entity.CourseTeacher;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.View;
import edu.wgu.Stephan_Ward_C196.R;
import android.view.MenuItem;

/**
 * Creates the the edit teacher model.
 * @author Stephan Ward
 * @since 07/12/2021
 */
public class EditTeacher extends AppCompatActivity {
    //The local database to be called
    LocalDB db;
    //Boolean to delete the teacher
    boolean teacherDEL;
    //Boolean to update the teacher
    boolean teacherUpdate;
    //Extended floating action button to update the teacher
    ExtendedFloatingActionButton updateTeacher_BTN;
    //Edit text for the editing for the teacher's Email
    EditText eTeacherEmail;
    //Edit text for the editing for the teacher Name
    EditText eTeacherName;
    //The edit text for the editing for the teacher's phone
    EditText eTeacherPhone;
    //The course ID for the teacher and student
    int courseID;
    //The intent for the the edit teacher
    Intent intent;
    //The integer for the teacher ID
    int teacherID;
    //The integer for the term ID
    int termID;

    /**
     * Creates the view layout for editing the teacher.
     * @param savedInstanceState The instance to be created to edit the teacher.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calls the edit teacher activity layout
        setContentView(R.layout.activity_edit_teacher);
        intent = getIntent();
        //Calls the local database instance to be populated.
        db = LocalDB.getInstance(getApplicationContext());
        //Intent to added the extra term ID, course ID, and teacher ID.
        termID = intent.getIntExtra("termID", -1);
        //Intent to add the course ID.
        courseID = intent.getIntExtra("courseID", -1);
        teacherID = intent.getIntExtra("teacherID", -1);
        //R method to edit the teacher's Name
        eTeacherName = findViewById(R.id.editTeacherName);
        eTeacherPhone = findViewById(R.id.editTeacherPhone);
        eTeacherEmail = findViewById(R.id.editTeacherEmailAddress);
        updateTeacher_BTN = findViewById(R.id.updateTeacherFAB);
        setValues();
        //On click listening even for older android phones.
        updateTeacher_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            //Updates the teacher's lists with data that is input with click
            public void onClick(View view) {
                //Updates the teacher's details class.
                updateTeacher();
                //Places the extra intents to be updates for the teacher
                if (teacherUpdate) {
                    Intent intent = new Intent(getApplicationContext(), TeacherDetails.class);
                    intent.putExtra("termID", termID);
                    intent.putExtra("courseID", courseID);
                    intent.putExtra("teacherID", teacherID);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Sets the teachers values to be added by a string
     */
    private void setValues() {
        CourseTeacher teacher = new CourseTeacher();
        teacher = db.courseTeacherDAO().getTeacher(courseID, teacherID);
        String name = teacher.getTeacher_name();
        String phone = teacher.getTeacher_phone();
        String email = teacher.getTeacher_email();
        //Sets the name, phone, and email for the teacher to be edited
        eTeacherName.setText(name);
        eTeacherPhone.setText(phone);
        eTeacherEmail.setText(email);
    }

    /**
     * Updates the teacher's Name, Phone, Email to the date base
     */
    private void updateTeacher() {
        //Creates teacher's string name
        String name = eTeacherName.getText().toString();
        //Creates teachers string phone.
        String phone = eTeacherPhone.getText().toString();
        //Creates teacher string email
        String email = eTeacherEmail.getText().toString();
        //Informs the student to enter date if missing.
        if (name.trim().isEmpty()) {
            //Toast is generated to inform student to enter teachers name
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.trim().isEmpty()) {
            //Toast is generated to inform student to enter teachers phone number
            Toast.makeText(this, "Please Enter Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.trim().isEmpty()) {
            //Toast is generated to inform student to enter teachers email.
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        //Creates a new teacher instance
        CourseTeacher teacher = new CourseTeacher();
        teacher.setCourse_id_fk(courseID);
        teacher.setTeacher_id(teacherID);
        teacher.setTeacher_name(name);
        teacher.setTeacher_phone(phone);
        teacher.setTeacher_email(email);
        db.courseTeacherDAO().updateTeacher(teacher);
        Toast.makeText(this, name + " was updated", Toast.LENGTH_SHORT).show();
        teacherUpdate = true;
    }

    /**
     * Deletes the teacher from the database
     */
    private void deleteTeacher() {
        CourseTeacher teacher = new CourseTeacher();
        teacher = db.courseTeacherDAO().getTeacher(courseID, teacherID);
        //Deletes the selected teacher.
        db.courseTeacherDAO().deleteTeacher(teacher);
        Toast.makeText(this, "Teacher was deleted", Toast.LENGTH_SHORT).show();
        teacherDEL = true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_teacher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteTeacherIC:
                deleteTeacher();
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