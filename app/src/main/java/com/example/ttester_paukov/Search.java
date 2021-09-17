package com.example.ttester_paukov;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ttester_paukov.Utils.ArrayAdapter;
import com.example.ttester_paukov.Utils.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tanja on 25.04.2017.
 */

public class Search extends Activity{
    // List view
    private ListView lv;
    List<Question> quesList;
    Question currentQ;
    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchwindow);
        DbHelper db=new DbHelper(this);
        quesList=db.getAllQuestions(-1);

        final String products[] = new String[quesList.size()];
        for(int i=0;i<quesList.size();i++){
            ArrayList<String> answers = new ArrayList<>();
            currentQ=quesList.get(i);
            answers.add(currentQ.getOPT1());
            answers.add(currentQ.getOPT2());
            answers.add(currentQ.getOPT3());
            answers.add(currentQ.getOPT4());


            products[i]=currentQ.getQUESTION()+":\n"+answers.get(currentQ.getANSWER());
        }
        //products[0]="123";
        //quesList.
        // Listview Data

        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        // Adding items to listview
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, products);
        lv.setAdapter(adapter);


        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //String st = cs.toString().trim();
                Search.this.adapter.getFilter().filter(cs);

                int textlength;
                textlength = inputSearch.getText().length();
                // adapter.clear();

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {

                // TODO Auto-generated method stub
            }
        });
    }

}
