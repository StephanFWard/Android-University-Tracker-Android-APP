package edu.wgu.Stephan_Ward_C196.Activity;

import android.os.Bundle;
import java.util.List;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Entity.Term;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import edu.wgu.Stephan_Ward_C196.Database.LocalDB;

/**
 * Creates the the term list model.
 * @author Stephan Ward
 * @since 07/13/2021
 */
public class TermList extends AppCompatActivity {
    //Local database for the term list
    LocalDB db;
    //The extended floating action button for the term list
    ExtendedFloatingActionButton addTerm_BTN;
    //All terms list for the term list model
    List<Term> allTerms;
    //The list view for the term list of the term list model
    ListView termList;

    /**
     * Creates the term list view instance
     * @param savedInstanceState View instance for the term list model.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calls the view layout for the term activity list.
        setContentView(R.layout.activity_term_list);
        //The R method view for the term date of the term list
        termList = findViewById(R.id.tdTermList);
        //Calls the local database instance for the term list.
        db = LocalDB.getInstance(getApplicationContext());
        //Calls the add term floating action button
        addTerm_BTN = findViewById(R.id.addTermFAB);
        //The term on click listener to update the term details class.
        termList.setOnItemClickListener((parent, view, position, id) -> {
            //The intent to add term details to the term details class
            Intent intent = new Intent(getApplicationContext(), TermDetails.class);
            //Extends the term ID to the intent
            intent.putExtra("termID", allTerms.get(position).getTerm_id());
            //Starts the term activity intent
            startActivity(intent);
            //System print out of the ID
            System.out.println(id);
        });
        //Updates the term list for the term list model
        updateTermList();
        //The add term on click listener to add the term to the term list model
        addTerm_BTN.setOnClickListener(view -> {
            //The intent the extends context to the add term class
            Intent intent = new Intent(getApplicationContext(), AddTerm.class);
            //Starts the intent activity
            startActivity(intent);
        });
    }

    /**
     * Updates the all term list for the term details
     */
    private void updateTermList() {
        //Applies the term list to the term DAO database
        List<Term> allTerms = db.termDao().getTermList();
        //Applies the array adapter to all terms R Layout method for all terms
        ArrayAdapter<Term> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allTerms);
        //Sets the term list adapter
        termList.setAdapter(adapter);
        //All terms applied
        this.allTerms = allTerms;
        //Notifies that the attached objects have changed.
        adapter.notifyDataSetChanged();
    }
}