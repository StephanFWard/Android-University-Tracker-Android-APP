package edu.wgu.Stephan_Ward_C196.Utilities;

import androidx.appcompat.app.AppCompatActivity;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;
import java.util.List;
import edu.wgu.Stephan_Ward_C196.Entity.Course;
import edu.wgu.Stephan_Ward_C196.Entity.Assessment;
import edu.wgu.Stephan_Ward_C196.Entity.Term;
import edu.wgu.Stephan_Ward_C196.Entity.CourseTeacher;
import android.util.Log;
import android.content.Context;
import java.util.Calendar;

/**
 * Adds sample date to the SQL Lite Database
 * @author Stephan Ward
 * @since 07/15/2021
 */
public class AddSampleData extends AppCompatActivity {
    public static String LOG_TAG = "Lite DB Inserted";
    //Terms to be added
    Term tempTerm1 = new Term();
    Term tempTerm2 = new Term();
    Term tempTerm3 = new Term();
    //Courses to be added
    Course tempCourse1 = new Course();
    Course tempCourse2 = new Course();
    Course tempCourse3 = new Course();
    //Assessments to tbe added
    Assessment tempAssessment1 = new Assessment();
    //Course teacher to be addded
    CourseTeacher tempTeacher = new CourseTeacher();
    //Calls the local DB
    LocalDB db;

    /**
     * Populates the database with context items
     * @param context Items to be added to the app when the DB is populated
     */
    public void populate(Context context) {
        db = LocalDB.getInstance(context);
        try {
            insertTerms();
            insertCourses();
            insertAssessments();
            insertCourseTeachers();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Add Lite DB failed");
        }
    }

    /**
     * Inserts term items into the SQLLite DB
     */
    private void insertTerms() {
        Calendar start;
        Calendar end;
        //The calender time, name, status are added for term 1
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, +1);
        end.add(Calendar.MONTH, +4);
        tempTerm1.setTerm_name("Fall 2021");
        tempTerm1.setTerm_start(start.getTime());
        tempTerm1.setTerm_status("In-Progress");
        tempTerm1.setTerm_end(end.getTime());
        //The calender time, name, status are added for term 2
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, +6);
        end.add(Calendar.MONTH, +10);
        tempTerm2.setTerm_name("Spring 2022");
        tempTerm2.setTerm_start(start.getTime());
        tempTerm2.setTerm_status("Not Enrolled");
        tempTerm2.setTerm_end(end.getTime());
        //The calender time, name, status are added for term 3
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, +11);
        end.add(Calendar.MONTH, +13);
        tempTerm3.setTerm_name("Summer 2022");
        tempTerm3.setTerm_start(start.getTime());
        tempTerm3.setTerm_status("Not Enrolled");
        tempTerm3.setTerm_end(end.getTime());
        //Inserts the term items into the term data access object
        db.termDao().insertAllTerms(tempTerm1, tempTerm2, tempTerm3);
    }

    /**
     * Inserts course items into the SQLLite DB
     */
    private void insertCourses() {
        Calendar start;
        Calendar end;
        List<Term> TermList = db.termDao().getTermList();
        if (TermList == null) return;
        //Applies the course calender, time, name, status, and notes to the Term Course 1
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, +2);
        end.add(Calendar.MONTH, +4);
        tempCourse1.setCourse_name("C195 Software 1");
        tempCourse1.setCourse_start(start.getTime());
        tempCourse1.setCourse_end(end.getTime());
        tempCourse1.setCourse_status("Pending");
        tempCourse1.setCourse_notes("Here are some example notes.");
        tempCourse1.setTerm_id_fk(TermList.get(0).getTerm_id());
        //Applies the course calender, time, name, status, and notes to the Term Course 2
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, +2);
        end.add(Calendar.MONTH, +4);
        tempCourse2.setCourse_name("C196 Mobile App");
        tempCourse2.setCourse_start(start.getTime());
        tempCourse2.setCourse_end(end.getTime());
        tempCourse2.setCourse_status("Completed");
        tempCourse2.setCourse_notes("Here are some example notes.");
        tempCourse2.setTerm_id_fk(TermList.get(0).getTerm_id());
        //Applies the course calender, time, name, status, and notes to the Term Course 3
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, +2);
        end.add(Calendar.MONTH, +4);
        tempCourse3.setCourse_name("sqlLite Class");
        tempCourse3.setCourse_start(start.getTime());
        tempCourse3.setCourse_end(end.getTime());
        tempCourse3.setCourse_status("Dropped");
        tempCourse3.setCourse_notes("Here are some example notes.");
        tempCourse3.setTerm_id_fk(TermList.get(0).getTerm_id());
        //Inserts all term course information into the course data access object.
        db.courseDao().insertAllCourses(tempCourse1, tempCourse2, tempCourse3);
    }

    /**
     * Inserts teachers into the SQLLite database
     */
    private void insertCourseTeachers() {
        //Applies the term and course list first
        List<Term> TermList = db.termDao().getTermList();
        List<Course> CourseList = db.courseDao().getCourseList(TermList.get(0).getTerm_id());
        //Checks to see if course list is available to be added.
        if (CourseList == null) return;
        //Creates and sets a temporary teacher
        tempTeacher.setTeacher_name("Stephan Ward");
        tempTeacher.setTeacher_email("stephan.f.ward@gmail.com");
        tempTeacher.setTeacher_phone("910-770-2726");
        tempTeacher.setCourse_id_fk(CourseList.get(0).getCourse_id());
        //Applies the temporary teacher to the course teacher data access object
        db.courseTeacherDAO().insertAllCourseTeachers(tempTeacher);
    }

    /**
     * Inserts assessments into the SQLLite database
     */
    private void insertAssessments() {
        Calendar start;
        Calendar end;
        List<Term> TermList = db.termDao().getTermList();
        List<Course> CourseList = db.courseDao().getCourseList(TermList.get(0).getTerm_id());
        //Ensourse the course list is available
        if (CourseList == null) return;
        //Sets the calender objects for the assessments
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, +1);
        end.add(Calendar.MONTH, +4);
        tempAssessment1.setAssessment_name("WGU Project 1");
        tempAssessment1.setAssessment_due_date(start.getTime());
        tempAssessment1.setAssessment_type("Objective");
        tempAssessment1.setCourse_id_fk(CourseList.get(0).getCourse_id());
        tempAssessment1.setAssessment_status("Pending");
        //Inserts all the assessments into the assessment data access object.
        db.assessmentDao().insertAllAssessments(tempAssessment1);
    }
}