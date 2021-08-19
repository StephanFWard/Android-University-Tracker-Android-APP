package edu.wgu.Stephan_Ward_C196.Entity;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import static androidx.room.ForeignKey.CASCADE;
import java.util.Date;

/**
 * Maps the course entity for the sql lite database
 * @author Stephan Ward
 * @since 07/13/2021
 */
@Entity(
        //Assigns the course table name
        tableName = "course_table",
        //Creates the foreign key for the course table
        foreignKeys = @ForeignKey(
                //Maps entity the term class
                entity = Term.class,
                //Locates parent column term ID
                parentColumns = "term_id",
                //Locates child column term id for foreign key
                childColumns = "term_id_fk",
                onDelete = CASCADE
        )
)
public class Course {
    //Auto generates primary key for the course id
    @PrimaryKey(autoGenerate = true)
    //Assigns primary key the course_id integer
    private int course_id;
    //Names the term id foreign key column
    @ColumnInfo(name = "term_id_fk")
    //Assigns the term id foreign key as a integer
    private int term_id_fk;
    //Names the course name column
    @ColumnInfo(name = "course_name")
    //Assigns the course name column as a string
    private String course_name;
    //Names the course start date column
    @ColumnInfo(name = "course_start")
    //Assigns date to the course start date column
    private Date course_start;
    //Names the course end date column
    @ColumnInfo(name = "course_end")
    //Assigns date to the course end date column
    private Date course_end;
    //Names the course status column
    @ColumnInfo(name = "course_status")
    //Assigns string to the course status column
    private String course_status;
    //Names the course notes column
    @ColumnInfo(name = "course_notes")
    //Assigns string to course notes column
    private String course_notes;
    //Names the course alert column
    @ColumnInfo(name = "course_alert")
    //Assigns boolean to course alert column
    private boolean course_alert;
    //Function to get course ID
    public int getCourse_id() {
        return course_id;
    }
    //Function to set course id
    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
    //Function to get Term ID foreign key
    public int getTerm_id_fk() {
        return term_id_fk;
    }
    //Function to set Term ID foreign key
    public void setTerm_id_fk(int term_id_fk) {
        this.term_id_fk = term_id_fk;
    }
    //Function to get course name
    public String getCourse_name() {
        return course_name;
    }
    //Function to set course name
    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
    //Function to get course start date
    public Date getCourse_start() {
        return course_start;
    }
    //Function to set course start date
    public void setCourse_start(Date course_start) {
        this.course_start = course_start;
    }
    //Function to get course end date
    public Date getCourse_end() {
        return course_end;
    }
    //Function to set course end date
    public void setCourse_end(Date course_end) {
        this.course_end = course_end;
    }
    //Function to get course status
    public String getCourse_status() {
        return course_status;
    }
    //Function to set course status
    public void setCourse_status(String course_status) {
        this.course_status = course_status;
    }
    //Function to get course notes
    public String getCourse_notes() {
        return course_notes;
    }
    //Function to set course notes
    public void setCourse_notes(String course_notes) {
        this.course_notes = course_notes;
    }
    //Boolean function to get course alert
    public boolean getCourse_alert() {
        return course_alert;
    }
    //Function to set Boolean course alert
    public void setCourse_alert(boolean course_alert) {
        this.course_alert = course_alert;
    }
    //Public override to string method to get course name
    @Override
    public String toString() {
        return this.getCourse_name();
    }
}
