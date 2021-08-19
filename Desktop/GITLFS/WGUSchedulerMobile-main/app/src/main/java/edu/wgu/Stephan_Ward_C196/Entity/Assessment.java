package edu.wgu.Stephan_Ward_C196.Entity;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import static androidx.room.ForeignKey.CASCADE;
import java.util.Date;

/**
 * Maps the assessment entity for the sql lite database
 * @author Stephan Ward
 * @since 07/13/2021
 */
@Entity(
        //Assigns the assessment table name
        tableName = "assessment_table",
        //Creates the foreign key for the assessment table
        foreignKeys = @ForeignKey(
                //Maps entity the course class
                entity = Course.class,
                //Locates parent column course ID
                parentColumns = "course_id",
                //Locates child column course id for foreign key
                childColumns = "course_id_fk",
                onDelete = CASCADE
        )
)
public class Assessment {
    //Auto generates primary key for the assessment id
    @PrimaryKey(autoGenerate = true)
    //Assigns primary key the assessment_id integer
    private int assessment_id;
    //Names the course id foreign key column
    @ColumnInfo(name = "course_id_fk")
    //Assigns the course id foreign key as a integer
    private int course_id_fk;
    //Names the assessment name column
    @ColumnInfo(name = "assessment_name")
    //Assigns the assessment name column as a string
    private String assessment_name;
    //Names the assessment type column
    @ColumnInfo(name = "assessment_type")
    //Assigns the assessment type column as a string
    private String assessment_type;
    //Names the assessment status column
    @ColumnInfo(name = "assessment_status")
    //Assigns the assessment status column as a string
    private String assessment_status;
    //Names the assessment due date column
    @ColumnInfo(name = "assessment_due_date")
    //Assigns date to the assessment due date
    private Date assessment_due_date;
    //Names the assessment alert column
    @ColumnInfo(name = "assessment_alert")
    //Assigns boolean to the assessment alert column
    private boolean assessment_alert;
    //function to get the assessment id
    public int getAssessment_id() {
        return assessment_id;
    }
    //function to set the assessment id
    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }
    //function to get course id foreign key
    public int getCourse_id_fk() {
        return course_id_fk;
    }
    //function to set course id foreign key
    public void setCourse_id_fk(int course_id_fk) {
        this.course_id_fk = course_id_fk;
    }
    //function to get assessment name
    public String getAssessment_name() {
        return assessment_name;
    }
    //function to set assessment name
    public void setAssessment_name(String assessment_name) {
        this.assessment_name = assessment_name;
    }
    //function to get assessment type
    public String getAssessment_type() {
        return assessment_type;
    }
    //function to set assessment type
    public void setAssessment_type(String assessment_type) {
        this.assessment_type = assessment_type;
    }
    //function to get assessment status
    public String getAssessment_status() {
        return assessment_status;
    }
    //function to set assessment status
    public void setAssessment_status(String assessment_status) {
        this.assessment_status = assessment_status;
    }
    //function to get assessment due date
    public Date getAssessment_due_date() {
        return assessment_due_date;
    }
    //function to set assessment due date
    public void setAssessment_due_date(Date assessment_due_date) {
        this.assessment_due_date = assessment_due_date;
    }
    //function to get assessment alert
    public boolean getAssessment_alert() {
        return assessment_alert;
    }
    //function to set assessment alert
    public void setAssessment_alert(boolean assessment_alert) {
        this.assessment_alert = assessment_alert;
    }
    //Public override to string function to get assessment name
    @Override
    //Returns the assessment name
    public String toString() {
        return this.getAssessment_name();
    }
}
