package edu.wgu.Stephan_Ward_C196.DAO;

import edu.wgu.Stephan_Ward_C196.Entity.CourseTeacher;
import java.util.List;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Update;

/**
 * CRUD course teacher operations for the SQLLITE database.
 * @author Stephan Ward
 * @since 07/13/2021
 */
@Dao
public interface CourseTeacherDAO {
    //Query that selects course teacher list from course teacher table where foreign key is course ID and ordered by teacher ID
    @Query("SELECT * FROM COURSE_TEACHER_TABLE WHERE course_id_fk = :courseID ORDER BY teacher_id")
    List<CourseTeacher> getTeacherList(int courseID);
    //Query to get teacher from course teacher table where foreign key is course ID and includes the teacher ID
    @Query("SELECT * FROM COURSE_TEACHER_TABLE WHERE course_id_fk = :courseID and teacher_id = :teacherID")
    CourseTeacher getTeacher(int courseID, int teacherID);
    //Inserts the course teacher into the data access assessment object
    @Insert
    void insertTeacher(CourseTeacher courseTeacher);
    //Inserts the all teachers into the data access assessment object
    @Insert
    void insertAllCourseTeachers(CourseTeacher... courseTeacher);
    //Updates the teacher into the data access assessment object
    @Update
    void updateTeacher(CourseTeacher courseTeacher);
    //Deletes the teacher into the data access assessment object
    @Delete
    void deleteTeacher(CourseTeacher courseTeacher);
}
