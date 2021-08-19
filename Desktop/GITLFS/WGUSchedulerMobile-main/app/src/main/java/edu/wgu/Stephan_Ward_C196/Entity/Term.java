package edu.wgu.Stephan_Ward_C196.Entity;

import java.util.Date;
import androidx.room.PrimaryKey;
import androidx.room.Entity;
import androidx.room.ColumnInfo;

/**
 * Maps the term entity for the sql lite database
 * @author Stephan Ward
 * @since 07/13/2021
 */
//Assigns the term table name
@Entity(tableName = "term_table")
public class Term {
    //Auto generates primary key for the term id
    @PrimaryKey(autoGenerate = true)
    //Assigns the term id as integer
    private int term_id;
    //Names the term name column
    @ColumnInfo(name = "term_name")
    //Assign the term name as string
    private String term_name;
    //Names the term status column
    @ColumnInfo(name = "term_status")
    //Assigns the term status as string
    private String term_status;
    //Names the column term start date
    @ColumnInfo(name = "term_start")
    //Assigns date to term start date
    private Date term_start;
    //Names the term end date column
    @ColumnInfo(name = "term_end")
    //Assigns date to the term end date column
    private Date term_end;
    //Function to get term id
    public int getTerm_id() {
        return term_id;
    }
    //Function to set term id
    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }
    //Function to get term name
    public String getTerm_name() {
        return term_name;
    }
    //Function to set term name
    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }
    //Function to get term status
    public String getTerm_status() {
        return term_status;
    }
    //Function to get term status
    public void setTerm_status(String term_status) {
        this.term_status = term_status;
    }
    //Function to set term status
    public Date getTerm_start() {
        return term_start;
    }
    //Function to set term start date
    public void setTerm_start(Date term_start) {
        this.term_start = term_start;
    }
    //Function to get term end date
    public Date getTerm_end() {
        return term_end;
    }
    //Function to set term end date
    public void setTerm_end(Date term_end) {
        this.term_end = term_end;
    }
    //Public to string override to string function to get term name
    @Override
    public String toString() {
        return this.getTerm_name();
    }
}