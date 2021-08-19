package edu.wgu.Stephan_Ward_C196.Activity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.text.format.DateFormat;
import android.widget.TextView;
import edu.wgu.Stephan_Ward_C196.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import edu.wgu.Stephan_Ward_C196.Entity.Assessment;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;

/**
 * Creates the the Assessment details model.
 * @author Stephan Ward
 * @since 07/12/2021
 */
public class AssessmentDetails extends AppCompatActivity {
    //Local database object
    LocalDB db;
    //TermID int
    int termID;
    //course ID int
    int courseID;
    //AssessmentID int
    int assessmentID;
    //Intent
    Intent intent;
    //Assessment details name text view
    TextView adName;
    //Assessment details type text view
    TextView adType;
    //Assessment details status text view
    TextView adStatus;
    //Assessment details due date text view
    TextView adDueDate;
    //Assessment details alert text view
    TextView adAlert;
    //The activity details floating action button to edit.
    ExtendedFloatingActionButton adEditFAB;

    /**
     * Created assessment details instance to be entered into the DB
     * @param savedInstanceState Layout of assessment details.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Adds the view by R method layout of the activity assessment details
        setContentView(R.layout.activity_assessment_details);
        //Gets the the intent that started the assessment details activity
        intent = getIntent();
        //Applies instance to the local database.
        db = LocalDB.getInstance(getApplicationContext());
        //Applies the term ID to the assessment details
        termID = intent.getIntExtra("termID", -1);
        //Applies the course ID to the assessment details
        courseID = intent.getIntExtra("courseID", -1);
        //Applies the Assessment ID to the assement details
        assessmentID = intent.getIntExtra("assessmentID", -1);
        //R method to add assessment details name
        adName = findViewById(R.id.adName);
        //R method to add assessment details type
        adType = findViewById(R.id.adType);
        //R method to add assessment details Status
        adStatus = findViewById(R.id.adStatus);
        //R method to add assessment details due date
        adDueDate = findViewById(R.id.adDueDate);
        //R method to add assessment details alert
        adAlert = findViewById(R.id.adAlert);
        //R method to add assessment details edit floating action button
        adEditFAB = findViewById(R.id.adEditFAB);
        setValues();
        //Allows the editing on click listening event for the assement details edit action button
        adEditFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Allows the editing of the assessment class.
                Intent intent = new Intent(getApplicationContext(), EditAssessment.class);
                intent.putExtra("termID", termID);
                intent.putExtra("courseID", courseID);
                intent.putExtra("assessmentID", assessmentID);
                startActivity(intent);
            }
        });
    }

    /**
     * Sets the assessment detail values. Creates alert notification bar
     */
    private void setValues() {
        //Calls for new assessment
        Assessment assessment = new Assessment();
        //Updates the local db assessmentDao with courseID and assessment ID
        assessment = db.assessmentDao().getAssessment(courseID, assessmentID);
        //Gets the assessment details name
        String name = assessment.getAssessment_name();
        //Gets the assessment details type
        String type = assessment.getAssessment_type();
        //Gets the assessment details status
        String status = assessment.getAssessment_status();
        //Gets the assessment details date
        String dDate = DateFormat.format("MM/dd/yyyy", assessment.getAssessment_due_date()).toString();
        boolean alert1 = assessment.getAssessment_alert();
        //Gets the assessment details alert bar
        String alert = "Off";
        if (alert1) {
            alert = "On";
        }
        //Sets assessment details Name
        adName.setText(name);
        //Sets assessment details type
        adType.setText(type);
        //Sets assessment details status
        adStatus.setText(status);
        //Sets assessment details due date
        adDueDate.setText(dDate);
        //Sets assessment details alert
        adAlert.setText(alert);
    }
}