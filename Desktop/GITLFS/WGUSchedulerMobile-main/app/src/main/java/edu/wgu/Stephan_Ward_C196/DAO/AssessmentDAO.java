package edu.wgu.Stephan_Ward_C196.DAO;

import edu.wgu.Stephan_Ward_C196.Entity.Assessment;
import java.util.List;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Update;

/**
 * CRUD Assessment operations for the SQLLITE database.
 * @author Stephan Ward
 * @since 07/13/2021
 */
@Dao
public interface AssessmentDAO {
    //Query that selects Assessment list from assessment table where foreign key is course ID and ordered by assessment ID
    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseID ORDER BY assessment_id")
    List<Assessment> getAssessmentList(int courseID);
    //Query to get Assessments from assessment table where foreign key is course ID and includes assessment ID
    @Query("Select * from assessment_table WHERE course_id_fk = :courseID and assessment_id = :assessmentID")
    Assessment getAssessment(int courseID, int assessmentID);
    //Query that selects all assessments from the assessment table
    @Query("SELECT * FROM assessment_table")
    List<Assessment> getAllAssessments();
    //Query that selects all current assessments from the assessments table where foreign key is the course ID and ordered by descending limit
    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseID ORDER BY assessment_id DESC LIMIT 1")
    Assessment getCurrentAssessment(int courseID);
    //Inserts the assessment into the data access assessment object
    @Insert
    void insertAssessment(Assessment assessment);
    //Inserts all assessments into the data access assessment object
    @Insert
    void insertAllAssessments(Assessment... assessment);
    //Updates the assessment into the data access assessment object
    @Update
    void updateAssessment(Assessment assessment);
    //Deletes the assessment in the data access assessment object
    @Delete
    void deleteAssessment(Assessment assessment);
}
