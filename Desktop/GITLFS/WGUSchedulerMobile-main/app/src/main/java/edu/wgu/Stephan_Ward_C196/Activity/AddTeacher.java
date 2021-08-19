package edu.wgu.Stephan_Ward_C196.Activity;

import edu.wgu.Stephan_Ward_C196.R;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import edu.wgu.Stephan_Ward_C196.Entity.CourseTeacher;
import android.widget.EditText;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;

/**
 * Creates the the add teacher model.
 * @author Stephan Ward
 * @since 07/12/2021
 */
public class AddTeacher extends AppCompatActivity {
    LocalDB db;
    //Boolean to confirm if teacher is added
    boolean teacherAdded;
    //Edit Text boxed for the teacher's email address, name, and phone.
    EditText addTeacherEmailAddress;
    EditText addTeacherName;
    EditText addTeacherPhone;
    //The floating action button for the teacher.
    ExtendedFloatingActionButton addTeacherFAB;
    int courseID;
    Intent intent;
    int termID;

    /**
     * Created add teacher instance to be entered into the DB
     * @param savedInstanceState To create a new teacher
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calls the acitivty add teacher layout by the R method
        setContentView(R.layout.activity_add_teacher);
        intent = getIntent();
        //Call the local DB instance application contexts
        db = LocalDB.getInstance(getApplicationContext());
        termID = intent.getIntExtra("termID", -1);
        courseID = intent.getIntExtra("courseID", -1);
        //Adds the find View by ID R method for ID
        addTeacherName = findViewById(R.id.addTeacherName);
        addTeacherPhone = findViewById(R.id.addTeacherPhone);
        addTeacherEmailAddress = findViewById(R.id.addTeacherEmailAddress);
        addTeacherFAB = findViewById(R.id.addTeacherFAB);
        addTeacherFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTeacher();
                if (teacherAdded == true) {
                    Intent intent = new Intent(getApplicationContext(), CourseDetails.class);
                    intent.putExtra("termID", termID);
                    intent.putExtra("courseID", courseID);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * Adds a new teacher if validated
     */
    private void addTeacher() {
        String name = addTeacherName.getText().toString();
        String phone = addTeacherPhone.getText().toString();
        String email = addTeacherEmailAddress.getText().toString();
        //Toast to make sure teachers name is entered
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "A name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast to make sure teacher's phone number is entered.
        if (phone.trim().isEmpty()) {
            Toast.makeText(this, "A phone number is required", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast for teachers email.
        if (email.trim().isEmpty()) {
            Toast.makeText(this, "An email is required", Toast.LENGTH_SHORT).show();
            return;
        }
        //Applies the FK to the course teacher from the course ID
        CourseTeacher teacher = new CourseTeacher();
        teacher.setCourse_id_fk(courseID);
        //Sets the teacher's name, phone, and email.
        teacher.setTeacher_name(name);
        teacher.setTeacher_phone(phone);
        teacher.setTeacher_email(email);
        //Uploads to teachers data to the DB
        db.courseTeacherDAO().insertTeacher(teacher);
        Toast.makeText(this, name + " has been added", Toast.LENGTH_SHORT).show();
        teacherAdded = true;
    }
}