package edu.wgu.Stephan_Ward_C196.Activity;

import android.view.MenuInflater;
import android.widget.TextView;
import android.view.View;
import edu.wgu.Stephan_Ward_C196.Utilities.AddSampleData;
import androidx.annotation.RequiresApi;
import android.os.Build;
import android.content.Intent;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;
import java.util.List;
import edu.wgu.Stephan_Ward_C196.Entity.Course;
import edu.wgu.Stephan_Ward_C196.Entity.Assessment;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Entity.Term;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;

/**
 * Creates the the home pager model.
 * @author Stephan Ward
 * @since 07/13/2021
 */
public class HomePage extends AppCompatActivity {
    //The local database to be called
    LocalDB db;
    //The text View of the term Data
    TextView termData;
    //The course pending text view text
    TextView cPendingTxt;
    //The course completed text view text
    TextView cCompletedTxt;
    //The course dropped text view text
    TextView cDroppedTxt;
    //The activity pending text view text
    TextView aPendingTxt;
    //The activity passing text view text
    TextView aPassedTxt;
    //The activity failed text view text
    TextView aFailedTxt;
    //The extended floating action button for th term list.
    ExtendedFloatingActionButton termList_BTN;

    //Sets the required API version build for lollipop API 21
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creates the Home Page instances.
        super.onCreate(savedInstanceState);
        //R layout sets the main activity view
        setContentView(R.layout.activity_main);
        //Instance of the local database is called
        db = LocalDB.getInstance(getApplicationContext());
        //The termed data is applied by the R layout view method
        termData = findViewById(R.id.termData);
        //The course pending date is applied by the R layout view method.
        cPendingTxt = findViewById(R.id.coursesPendingTextView);
        //The course completed is applied by the R layout view method.
        cCompletedTxt = findViewById(R.id.coursesCompletedTextView);
        //The course dropped text is applied by the R layout view method.
        cDroppedTxt = findViewById(R.id.coursesDroppedTextView);
        //The course dropped text is applied by the R layout view method.
        aPendingTxt = findViewById(R.id.assessmentsPendingTextView);
        //The activity passed text is applied by the R layout view method
        aPassedTxt = findViewById(R.id.assessmentsPassedTextView);
        //The activity failed is applied by the R layout view method.
        aFailedTxt = findViewById(R.id.assessmentsFailedTextView);
        //The term listed floating button is applied by the R layout view method.
        termList_BTN = findViewById(R.id.hTermListFAB);
        // Update view is called on term list button.
        updateViews();
        //Listener is applied for the on view click listening event.
        termList_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Applies the new intent for the term list class.
                Intent intent = new Intent(getApplicationContext(), TermList.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Updates the home page view with information university tracker information
     */
    private void updateViews() {
        //Term integer that are completed
        int term = 0;
        //Terms that are completed
        int termComplete = 0;
        //Terms that are pending
        int termPending = 0;
        //Courses that are completed
        int course = 0;
        //Assessments that are completed
        int assessment = 0;
        //Courses that are pending
        int coursesPending = 0;
        //Courses that are completed
        int coursesCompleted = 0;
        //Courses that are dropped
        int coursesDropped = 0;
        //Assessments that are pending
        int assessmentsPending = 0;
        //Assessments that have been passed
        int assessmentsPassed = 0;
        //Assessments that are failed
        int assessmentsFailed = 0;
        try {
            //Gets all term list from the database term DAO
            List<Term> termList = db.termDao().getAllTerms();
            //Gets all course list from the database course DAO
            List<Course> courseList = db.courseDao().getAllCourses();
            //Gets all assessment list from the database assessment DAO
            List<Assessment> assessmentList = db.assessmentDao().getAllAssessments();
            try {
                //Iterates for the term List status count
                for (int i = 0; i < termList.size(); i++) {
                    term = termList.size();
                    //Gets term list items with the status completed
                    if (termList.get(i).getTerm_status().contains("Completed")) termComplete++;
                    //Gets term list items with the status in progress
                    if (termList.get(i).getTerm_status().contains("In-Progress")) termPending++;
                }
                //Throws execution error to the stack if operation is unsuccessful
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //Iterates the course list status count
                for (int i = 0; i < courseList.size(); i++) {
                    course = courseList.size();
                    //Gets course list items with the status pending
                    if (courseList.get(i).getCourse_status().contains("Pending")) coursesPending++;
                    //Gets course list items with the status in pending progress.
                    if (courseList.get(i).getCourse_status().contains("In-Progress"))
                        coursesPending++;
                    //Gets course list items with the status completed
                    if (courseList.get(i).getCourse_status().contains("Completed"))
                        coursesCompleted++;
                    //Gets the course list items with the status dropped.
                    if (courseList.get(i).getCourse_status().contains("Dropped")) coursesDropped++;
                }
            } catch (Exception e) {
                //Throws execution error to the stack if operation is unsuccessful
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < assessmentList.size(); i++) {
                    //Iterates the assessment list status count
                    assessment = assessmentList.size();
                    //Gets assessment list items with the status pending
                    if (assessmentList.get(i).getAssessment_status().contains("Pending"))
                        assessmentsPending++;
                    //Gets assessment list items with the status passed
                    if (assessmentList.get(i).getAssessment_status().contains("Passed"))
                        assessmentsPassed++;
                    //Gets assessment list items with the status failed
                    if (assessmentList.get(i).getAssessment_status().contains("Failed"))
                        assessmentsFailed++;
                }
                //Throws execution error to the stack if operation is unsuccessful
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Applies values if iteration is successful
        termData.setText(String.valueOf(term));
        //Applies the course pending text values of course pending.
        cPendingTxt.setText(String.valueOf(coursesPending));
        //Applies course completed string values of course completed
        cCompletedTxt.setText(String.valueOf(coursesCompleted));
        //Applies course dropped string values of courses dropped
        cDroppedTxt.setText(String.valueOf(coursesDropped));
        //Applies activity pending string values of course activity text.
        aPendingTxt.setText(String.valueOf(assessmentsPending));
        //Applies activity failed string values of course activity failed text.
        aFailedTxt.setText(String.valueOf(assessmentsFailed));
        //Applies activity passes string values of course activity passed text
        aPassedTxt.setText(String.valueOf(assessmentsPassed));
    }
    //Updates the home page view.
    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    /**
     * Creates the menu inflater for the home menu
     * @param menu Home page menu
     * @return Home page menu inflater
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        //Utilizes R method for menu
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    /**
     * Populates or resets the home menu database.
     * @param item Populate DB items or resets
     * @return Home page menu of populated database items or resets
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Called the database menu.
            case R.id.populateDBMenu:
                //Creates new instance to add the sample data.
                AddSampleData addSampleData = new AddSampleData();
                //Adds the sample data.
                addSampleData.populate(getApplicationContext());
                //Updates the home page views.
                updateViews();
                //Toast is created to inform student if Lite DB was inserted.
                Toast.makeText(this, "Lite DB Inserted", Toast.LENGTH_SHORT);
                return true;
            //Resets the light DB
            case R.id.resetDBMenu:
                //Clears all lite database tables.
                db.clearAllTables();
                //Updates the database table.
                updateViews();
                //Toast is created to inform the student that the lite DB was reset.
                Toast.makeText(this, "Lite DB Reset", Toast.LENGTH_SHORT);
                return true;
        }
        //Returns the selected option items.
        return super.onOptionsItemSelected(item);
    }
}