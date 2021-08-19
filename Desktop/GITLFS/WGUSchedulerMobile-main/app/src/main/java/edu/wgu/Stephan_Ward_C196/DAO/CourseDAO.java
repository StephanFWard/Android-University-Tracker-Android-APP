package edu.wgu.Stephan_Ward_C196.DAO;

import edu.wgu.Stephan_Ward_C196.Entity.Course;
import java.util.List;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Update;

/**
 * CRUD course operations for the SQLLITE database.
 * @author Stephan Ward
 * @since 07/13/2021
 */
@Dao
public interface CourseDAO {
    //Query that selects course list from course table where foreign key is term ID and ordered by course ID
    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID ORDER BY course_id")
    List<Course> getCourseList(int termID);
    //Query to get courses from course table where foreign key is term ID and includes the course ID
    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID and course_id = :courseID")
    Course getCourse(int termID, int courseID);
    //Query that selects all courses from the courses table
    @Query("SELECT * FROM course_table")
    List<Course> getAllCourses();
    //Query that selects all current courses from the courses table where foreign key is the term ID and ordered by descending limit
    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID ORDER BY course_id DESC LIMIT 1")
    Course getCurrentCourse(int termID);
    //Inserts the course into the data access assessment object
    @Insert
    void insertCourse(Course course);
    //Inserts all courses into the data access assessment object
    @Insert
    void insertAllCourses(Course... course);
    //Updates the course into the data access assessment object
    @Update
    void updateCourse(Course course);
    //Deletes the course in the data access assessment object
    @Delete
    void deleteCourse(Course course);
}
