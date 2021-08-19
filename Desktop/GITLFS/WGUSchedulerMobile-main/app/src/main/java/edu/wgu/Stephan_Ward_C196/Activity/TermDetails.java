package edu.wgu.Stephan_Ward_C196.Activity;

import android.os.Bundle;
import android.widget.TextView;
import java.util.List;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import edu.wgu.Stephan_Ward_C196.Entity.Course;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.MenuInflater;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Entity.Term;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Creates the the term details model.
 * @author Stephan Ward
 * @since 07/13/2021
 */
public class TermDetails extends AppCompatActivity {
    //Local database
    LocalDB db;
    //The float action add class button for the term details
    ExtendedFloatingActionButton addClass_BTN;
    //Term details add button
    Intent intent;
    //The term ID integer for the term details
    int termID;
    //All courses list for the term details
    List<Course> allCourses;
    //The list view class list for the term details
    ListView classList;
    //The term date text view for the term details
    TextView tdeDate;
    //The term name for the term details
    TextView name;
    //The term start date for the term details
    TextView startDate;
    //The term status for the term details
    TextView status;

    /**
     * Creates the term detailed view instance
     * @param savedInstanceState View instance for the term detail model.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calls the view layout for the term activity detail.
        setContentView(R.layout.activity_term_details);
        //The intent for the term details
        intent = getIntent();
        //The R method for class list view for the term details
        classList = findViewById(R.id.tdClassList);
        //Gets the local database instances
        db = LocalDB.getInstance(getApplicationContext());
        //Gets the term ID intent
        termID = intent.getIntExtra("termID", -1);
        //Gets the term name for the term details view
        name = findViewById(R.id.tdName);
        //Gets the term status for the term details view
        status = findViewById(R.id.tdStatus);
        //Gets the term date name for the term details view
        name = findViewById(R.id.tdName);
        //Gets the term start date for the term details view
        startDate = findViewById(R.id.tdSdate);
        //Gets the term end date for the term details view
        tdeDate = findViewById((R.id.tdEdate));
        //Gets the floating action button for the term details view
        addClass_BTN = findViewById(R.id.tdAddClassFAB);
        //Updates the term view class list
        updateClassList();
        //Sets the term view values for the term details view.
        setValues();
        //The Add class on click listener to add a course to the term details.
        addClass_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //The intent to add the add course
                Intent intent = new Intent(getApplicationContext(), AddCourse.class);
                //Extends the term ID to the intent
                intent.putExtra("termID", termID);
                //Starts the intent activity
                startActivity(intent);
            }
        });
        //The add course details click listener to add term ID and all courses to term details.
        classList.setOnItemClickListener((parent, view, position, id) -> {
            //New intent for the course detail's class for the term details.
            Intent intent = new Intent(getApplicationContext(), CourseDetails.class);
            //Extends the term ID to the intent
            intent.putExtra("termID", termID);
            //Extends the Course ID for all courses to the intent.
            intent.putExtra("courseID", allCourses.get(position).getCourse_id());
            //Starts the intent activity
            startActivity(intent);
            //System print out of the long ID
            System.out.println(id);
        });

    }

    /**
     * Sets the values for the term details
     */
    private void setValues() {
        //Sets up the term details to accept the new values
        Term term = new Term();
        //Sets up the term details instance of the database to get the term ID data
        term = db.termDao().getTerm(termID);
        //Assign string name to the term name for the term details
        String name = term.getTerm_name();
        //Assigns the string status to the term details
        String status = term.getTerm_status();
        //Assigns the string start date to the term details
        String sDate = DateFormat.format("MM/dd/yyyy", term.getTerm_start()).toString();
        //Assigns the string end date to the term details.
        String eDate = DateFormat.format("MM/dd/yyyy", term.getTerm_end()).toString();
        //Applies term details name
        this.name.setText(name);
        //Applies term status
        this.status.setText(status);
        //Applies the term start date
        startDate.setText(sDate);
        //Applies the term end date
        tdeDate.setText(eDate);
    }

    /**
     * Updates the class list for the term details
     */
    private void updateClassList() {
        //Applies the course list to the course DAO database
        List<Course> allCourses = db.courseDao().getCourseList(termID);
        //Applies the array adapter to the course R Layout method for all courses
        ArrayAdapter<Course> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allCourses);
        //Sets the Class list adapter
        classList.setAdapter(adapter);
        //All courses applied
        this.allCourses = allCourses;
        //Notifies that the attached objects have changed.
        adapter.notifyDataSetChanged();
    }

    /**
     * Option menu inflater to edit the term details
     * @param menu Menu inflater to edit the term details
     * @return Boolean true if the term details is edited
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        //New menu inflater instance
        MenuInflater menuInflater = getMenuInflater();
        //R layout menu to edit term details.
        menuInflater.inflate(R.menu.edit_term, menu);
        return true;
    }

    /**
     * Edits term details if option item is selected
     * @param item Term item to be edited
     * @return Boolean true if the term details items edited successfully
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Case to edit the term details by the float action button
            case R.id.tdEditTermFAB:
                //Applies intent to the course edit term class.
                Intent intent = new Intent(getApplicationContext(), EditTerm.class);
                //Intent extended to the Term ID
                intent.putExtra("termID", termID);
                //Intent extended to the all courses
                intent.putExtra("courseList", allCourses.size());
                //Starts the intent activity
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

