package edu.wgu.Stephan_Ward_C196.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;

/**
 * Maps the course teacher entity for the sql lite database
 * @author Stephan Ward
 * @since 07/13/2021
 */
@Entity(
        //Assigns the course teacher table name
        tableName = "course_teacher_table",
        //Creates the foreign key for the course teacher table
        foreignKeys = @ForeignKey(
                //Maps entity the course class
                entity = Course.class,
                //Locates parent column course ID
                parentColumns = "course_id",
                //Locates child column course ID foreign key
                childColumns = "course_id_fk",
                onDelete = CASCADE
        )
)
public class CourseTeacher {
    //Auto generates primary key for the teacher id
    @PrimaryKey(autoGenerate = true)
    //Assigns primary key the teacher_id integer
    private int teacher_id;
    //Names the course id foreign key column
    @ColumnInfo(name = "course_id_fk")
    //Assigns the course id foreign key as a integer
    private int course_id_fk;
    //Names the teacher name column
    @ColumnInfo(name = "teacher_name")
    //Assigns string to the teacher name
    private String teacher_name;
    //names the teacher phone column
    @ColumnInfo(name = "teacher_phone")
    //Assigns string value to teacher's phone number
    private String teacher_phone;
    //Names the teacher's email column
    @ColumnInfo(name = "teacher_email")
    //Assigns the string to the teacher's email column
    private String teacher_email;
    //Function to get teacher's id
    public int getTeacher_id() {
        return teacher_id;
    }
    //Function to set teacher's id
    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }
    //Function to get course id foreign key
    public int getCourse_id_fk() {
        return course_id_fk;
    }
    //Function to set course id foreign key
    public void setCourse_id_fk(int course_id_fk) {
        this.course_id_fk = course_id_fk;
    }
    //Function to get teacher name string
    public String getTeacher_name() {
        return teacher_name;
    }
    //Function to set teacher's name string
    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }
    //Function to get teacher's phone string
    public String getTeacher_phone() {
        return teacher_phone;
    }
    //Function to set teacher's phone string
    public void setTeacher_phone(String teacher_phone) {
        this.teacher_phone = teacher_phone;
    }
    //Function to set teacher's email string
    public String getTeacher_email() {
        return teacher_email;
    }
    //Function to set teacher's email string
    public void setTeacher_email(String teacher_email) {
        this.teacher_email = teacher_email;
    }
    //Public to string override method to get teacher's name
    @Override
    public String toString() {
        return this.getTeacher_name();
    }
}
