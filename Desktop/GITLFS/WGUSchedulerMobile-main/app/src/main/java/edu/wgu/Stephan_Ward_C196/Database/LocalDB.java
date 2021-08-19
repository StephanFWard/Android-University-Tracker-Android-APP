package edu.wgu.Stephan_Ward_C196.Database;

import edu.wgu.Stephan_Ward_C196.DAO.CourseTeacherDAO;
import edu.wgu.Stephan_Ward_C196.Entity.Course;
import edu.wgu.Stephan_Ward_C196.Utilities.Converters;
import androidx.room.Room;
import android.content.Context;
import androidx.room.TypeConverters;
import edu.wgu.Stephan_Ward_C196.Entity.Assessment;
import edu.wgu.Stephan_Ward_C196.Entity.Term;
import edu.wgu.Stephan_Ward_C196.Entity.CourseTeacher;
import androidx.room.RoomDatabase;
import edu.wgu.Stephan_Ward_C196.DAO.CourseDAO;
import edu.wgu.Stephan_Ward_C196.DAO.AssessmentDAO;
import edu.wgu.Stephan_Ward_C196.DAO.TermDAO;

/**
 * The main sqlite database building operation that is utilized throughout the project
 * @author Stephan Ward
 * @since 07/13/2021
 */
@androidx.room.Database(entities = {Term.class, Course.class, CourseTeacher.class, Assessment.class}, exportSchema = false, version = 5)
@TypeConverters({Converters.class})
public abstract class LocalDB extends RoomDatabase {
    //Creates the final string name for the SQL database
    private static final String DB_Name = "SFW_C196.db";
    private static LocalDB instance;
    //Synchronizes builder to the local database instance
    public static synchronized LocalDB getInstance(Context context) {
        if (instance == null) {
            //Creates a new database builder room instance and is referenced through the main program
            instance = Room.databaseBuilder(context.getApplicationContext(), LocalDB.class, DB_Name).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }
    //References to the course teacher date access object through out the program.
    public abstract CourseTeacherDAO courseTeacherDAO();
    //References to the assessment date access object through out the program.
    public abstract AssessmentDAO assessmentDao();
    //References to the term date access object through out the program.
    public abstract TermDAO termDao();
    //References to the course date access object through out the program.
    public abstract CourseDAO courseDao();
}
