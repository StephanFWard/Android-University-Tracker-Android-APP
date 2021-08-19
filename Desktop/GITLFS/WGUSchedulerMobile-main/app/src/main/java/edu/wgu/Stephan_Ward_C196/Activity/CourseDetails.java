package edu.wgu.Stephan_Ward_C196.Activity;

import android.widget.TextView;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import java.util.List;
import edu.wgu.Stephan_Ward_C196.Entity.Course;
import edu.wgu.Stephan_Ward_C196.Entity.Assessment;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Entity.CourseTeacher;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.view.View;

/**
 * Creates the the course details model.
 * @author Stephan Ward
 * @since 07/12/2021
 */
public class CourseDetails extends AppCompatActivity {
    //Creates the local database object
    LocalDB db;
    //Intent activity
    Intent intent;
    //integer course ID
    int courseID;
    //integer term ID
    int termID;
    //List View of the course details teacher's list
    ListView cdTeacherList;
    //List View of the course details AssessmentList
    ListView cdAssessmentList;
    //List View of the course details of all teachers
    List<CourseTeacher> allTeachers;
    //List View of the course details all assessments
    List<Assessment> allAssessments;
    //List View of the course details add teachers floating action button
    FloatingActionButton cdAddTeacherFAB;
    //List View of the course details add assessment floating action button
    FloatingActionButton cdAddAssessmentFAB;
    //Text View of the course details name
    TextView cdName;
    //Text View of the course details Status
    TextView cdStatus;
    //Text View of the course details Alert
    TextView cdAlert;
    //Text View of the course details start date
    TextView cdsDate;
    //Text View of the course details end date
    TextView cdeDate;
    //Text View of the course details notes
    TextView cdNotes;

    /**
     * Created course details instance to be entered into the DB
     * @param savedInstanceState Layout of course details.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creates the bundled saved instance for the course detailes
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        intent = getIntent();
        db = LocalDB.getInstance(getApplicationContext());
        termID = intent.getIntExtra("termID", -1);
        courseID = intent.getIntExtra("courseID", -1);
        cdTeacherList = findViewById(R.id.cdTeacherList);
        cdAssessmentList = findViewById(R.id.cdAssessmentList);
        cdAddTeacherFAB = findViewById(R.id.cdAddTeacherFAB);
        cdAddAssessmentFAB = findViewById(R.id.cdAddAssessmentFAB);
        cdName = findViewById(R.id.cdName);
        cdStatus = findViewById(R.id.cdStatus);
        cdAlert = findViewById(R.id.cdAlert);
        cdsDate = findViewById(R.id.cdSdate);
        cdeDate = findViewById(R.id.cdEdate);
        cdNotes = findViewById(R.id.cdNotes);
        setValues();
        updateLists();

        //Teachers
        cdAddTeacherFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTeacher.class);
                intent.putExtra("termID", termID);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
            }
        });
        //Course details teachers list.
        cdTeacherList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), TeacherDetails.class);
            intent.putExtra("termID", termID);
            intent.putExtra("courseID", courseID);
            intent.putExtra("teacherID", allTeachers.get(position).getTeacher_id());
            startActivity(intent);
            System.out.println(id);
        });

        //Assessments floating action button
        cdAddAssessmentFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAssessment.class);
                intent.putExtra("termID", termID);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
            }
        });
        //Course details assessment list
        cdAssessmentList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), AssessmentDetails.class);
            intent.putExtra("termID", termID);
            intent.putExtra("courseID", courseID);
            intent.putExtra("assessmentID", allAssessments.get(position).getAssessment_id());
            startActivity(intent);
            System.out.println(id);
        });

    }
    //Sets the values of the course details
    private void setValues() {
        Course course = new Course();
        course = db.courseDao().getCourse(termID, courseID);
        String name = course.getCourse_name();
        String status = course.getCourse_status();
        boolean alert1 = course.getCourse_alert();
        String sDate = DateFormat.format("MM/dd/yyyy", course.getCourse_start()).toString();
        String eDate = DateFormat.format("MM/dd/yyyy", course.getCourse_end()).toString();
        String notes = course.getCourse_notes();
        String alert = "Off";
        if (alert1) {
            alert = "On";
        }
        cdName.setText(name);
        cdStatus.setText(status);
        cdAlert.setText(alert);
        cdsDate.setText(sDate);
        cdeDate.setText(eDate);
        cdNotes.setText(notes);
    }

    private void updateLists() {
        List<CourseTeacher> allTeachers = db.courseTeacherDAO().getTeacherList(courseID);
        ArrayAdapter<CourseTeacher> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allTeachers);
        cdTeacherList.setAdapter(adapter);
        this.allTeachers = allTeachers;
        adapter.notifyDataSetChanged();

        List<Assessment> allAssessments = db.assessmentDao().getAssessmentList(courseID);
        ArrayAdapter<Assessment> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allAssessments);
        cdAssessmentList.setAdapter(adapter2);
        this.allAssessments = allAssessments;
        adapter2.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tdEditCourseIC:
                Intent intent = new Intent(getApplicationContext(), EditCourse.class);
                intent.putExtra("termID", termID);
                intent.putExtra("courseID", courseID);
                intent.putExtra("teacherList", allTeachers.size());
                intent.putExtra("assessmentList", allAssessments.size());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}