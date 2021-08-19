package edu.wgu.Stephan_Ward_C196.DAO;

import edu.wgu.Stephan_Ward_C196.Entity.Term;
import java.util.List;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Update;

/**
 * CRUD course term operations for the SQLLITE database.
 * @author Stephan Ward
 * @since 07/13/2021
 */
@Dao
public interface TermDAO {
    //A query that selects all term list objects from the term table and orders them by term ID
    @Query("SELECT * FROM term_table ORDER BY term_id")
    List<Term> getTermList();
    //A query that gets term objects from the term table containing the termID and orders them by the term ID
    @Query("SELECT * FROM term_table WHERE term_id = :termID ORDER BY term_id")
    Term getTerm(int termID);
    //A query to get all term objects from the term table as a list for the data access object
    @Query("SELECT * FROM term_table")
    List<Term> getAllTerms();
    //A query to insert term object from the term table for the data access object
    @Insert
    void insertTerm(Term term);
    //A query to insert all term objects from the term table for the data access object
    @Insert
    void insertAllTerms(Term... term);
    //Updates the term into the data access assessment object
    @Update
    void updateTerm(Term term);
    //Deletes the term in the data access assessment object
    @Delete
    void deleteTerm(Term term);
}
