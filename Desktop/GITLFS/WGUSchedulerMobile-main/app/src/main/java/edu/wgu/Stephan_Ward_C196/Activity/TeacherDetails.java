package edu.wgu.Stephan_Ward_C196.Activity;

import android.view.MenuItem;
import edu.wgu.Stephan_Ward_C196.R;
import android.widget.Toast;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import edu.wgu.Stephan_Ward_C196.Entity.CourseTeacher;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.View;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;

/**
 * Creates the the teachers details model.
 * @author Stephan Ward
 * @since 07/13/2021
 */
public class TeacherDetails extends AppCompatActivity {
    //Local database
    LocalDB db;
    //The term ID integer for the teachers detail
    int termID;
    //The course ID integer for the teachers detail
    int courseID;
    //The teacher ID integer for the teachers detail
    int teacherID;
    //The intent for the teachers detail
    Intent intent;
    //The text view for the teacher's name.
    TextView teacherName;
    //The text view for the teacher's phone.
    TextView teacherPhone;
    //The text view for the teacher's email.
    TextView teacherEmail;
    //The extended floating action button to edit the teachers detail.
    ExtendedFloatingActionButton edit_BTN;
    //Boolean to delete teacher.
    boolean teacherDEL;

    /**
     * Creates the teachers detailed view instance
     * @param savedInstanceState View instance for the teacher's detail model.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calls the view layout for the teacher's activity detail.
        setContentView(R.layout.activity_teacher_details);
        intent = getIntent();
        //Gets the local database instances
        db = LocalDB.getInstance(getApplicationContext());
        //Gets the term ID intent
        termID = intent.getIntExtra("termID", -1);
        //Gets the course ID intent
        courseID = intent.getIntExtra("courseID", -1);
        //Gets the teachers ID intent
        teacherID = intent.getIntExtra("teacherID", -1);
        //Gets the R layout of the teacher name.
        teacherName = findViewById(R.id.mdName);
        //Gets the R layout of the teacher's phone.
        teacherPhone = findViewById(R.id.mdPhone);
        //Gets the R layout of the teacher's email
        teacherEmail = findViewById(R.id.mdEmail);
        //Gets the Find view ID button to edit the teacher's detail.
        edit_BTN = findViewById(R.id.mdEditFAB);
        //Sets the values for the view
        setValues();
        //A on click listening event to edit the teacher's class
        edit_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to edit the edit teacher's class
                Intent intent = new Intent(getApplicationContext(), EditTeacher.class);
                //Intent puts the term ID in the teacher's detail
                intent.putExtra("termID", termID);
                //Intent puts the course ID in the teacher's detail
                intent.putExtra("courseID", courseID);
                //Intent puts the course ID in the teacher's detail
                intent.putExtra("teacherID", teacherID);
                //Intent activity is started.
                startActivity(intent);
            }
        });

    }

    /**
     * Method created to delete the teacher.
     */
    private void deleteTeacher() {
        CourseTeacher teacher = new CourseTeacher();
        teacher = db.courseTeacherDAO().getTeacher(courseID, teacherID);
        //Updates the course teacher Dao to delete the
        db.courseTeacherDAO().deleteTeacher(teacher);
        //Toast made for when teacher is successfully deleted
        Toast.makeText(this, "Teacher was deleted", Toast.LENGTH_SHORT).show();
        teacherDEL = true;
    }

    /**
     * Sets the teacher's data information to the course Teacher's DAO
     */
    private void setValues() {
        //Teacher to be added
        CourseTeacher teacher = new CourseTeacher();
        teacher = db.courseTeacherDAO().getTeacher(courseID, teacherID);
        //Teacher's Name to be set
        String name = teacher.getTeacher_name();
        //Teacher's phone to be set
        String phone = teacher.getTeacher_phone();
        //Teachers email to be set.
        String email = teacher.getTeacher_email();
        //Sets teacher name string to teacher's name
        teacherName.setText(name);
        //Sets teacher's phone string to teacher's phone
        teacherPhone.setText(phone);
        //Sets teacher's email string to teacher's emails
        teacherEmail.setText(email);
    }

    /**
     * Option menu inflater to delete the teacher.
     * @param menu Menu inflater to delete the teacher.
     * @return Boolean true if the teacher is deleted
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        //New menu inflater instance
        MenuInflater menuInflater = getMenuInflater();
        //R layout menu to delete teacher.
        menuInflater.inflate(R.menu.delete_teacher, menu);
        return true;
    }

    /**
     * Deletes teacher if option item is selected
     * @param item Teacher item to be deleted.
     * @return Boolean true if the teacher items deleted successfully
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Case to delete the teacher
            case R.id.deleteTeacherIC:
                //Method called to delete teacher.
                deleteTeacher();
                //Applies intent to the course details class.
                Intent intent = new Intent(getApplicationContext(), CourseDetails.class);
                //Intent extended to the Term ID
                intent.putExtra("termID", termID);
                //Intent extended to the course ID
                intent.putExtra("courseID", courseID);
                //Intent Activity is started.
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}