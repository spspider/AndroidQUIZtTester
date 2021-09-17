package com.example.ttester_paukov;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ttester_paukov.Utils.Alist;
import com.example.ttester_paukov.Utils.AllThemes;
import com.example.ttester_paukov.Utils.ArrayAdapter;
import com.example.ttester_paukov.Utils.Question;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tanja on 25.04.2017.
 */

public class Search_Fragment extends Fragment {
    // List view
    private ListView lv;
    List<Question> quesList;
    List<Question> quesList_cut;
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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.searchwindow, null);
//        setContentView(R.layout.searchwindow);
        DbHelper db = new DbHelper(this.getActivity());
        //quesList = db.getAllQuestions(-1);
        ArrayList<AllThemes> themeList = db.getAllThemes();
        List<Question> quesList = new ArrayList<>();
       quesList.clear();
        //List<Question>quesList = null;

        for (int i1 = 0; i1 < themeList.size(); i1++) {
            int theme_theme_ID = themeList.get(i1).getTHEME_id();
            Log.d("MSG", String.valueOf(themeList.get(i1).getTHEME_id()));
            List<Question> Listquestion = (db.getAllQuestions(theme_theme_ID));
            //Log.d("MSG_TAG", String.valueOf(quesList.isEmpty()));
            if (Listquestion!=null){
            quesList.addAll(Listquestion);
            }
//            for (int i = 0; i < Listquestion.size(); i++) {
//
//            }
        }
        List<Question> quesList_cut = new ArrayList<>();

        final String products[] = new String[quesList.size()];
        ArrayList<Alist> answers_list = new ArrayList<>();
        // for (int i1 = 0; i1 < themeList.size(); i1++) {

        for (int i = 0; i < quesList.size(); i++) {
            ArrayList<String> answers = new ArrayList<>();
            //if (themeList.get())
//            int themeID_inquestion = quesList.get(i).getTHEMEID();
//            boolean question_is_exist = false;
//            for (int i1 = 0; i1 < themeList.size(); i1++) {
//                int theme_theme_ID = themeList.get(i1).getTHEME_id();
//                if (theme_theme_ID == themeID_inquestion) {
//                    question_is_exist = true;
//                    break;
//                }else{
//
//                }
//            }
//
//
//            //if (theme_theme_ID==themeID_inquestion) {
//            if (question_is_exist) {
                currentQ = quesList.get(i);
                quesList_cut.add(quesList.get(i));
                //products[i] = currentQ.getQUESTION()+currentQ.getTHEMEID();
                products[i] = currentQ.getQUESTION();
            //}
            //quesList_cut.add(quesList.get(i));
            // }
//            answers.add(currentQ.getOPT1());
//            answers.add(currentQ.getOPT2());
//            answers.add(currentQ.getOPT3());
//            answers.add(currentQ.getOPT4());
//            int RAnswer = currentQ.getANSWER();
            // rightAnswer= currentQ.getANSWER();
            //products[i]=currentQ.getQUESTION()+":\n"+answers.get(currentQ.getANSWER());
            //answers_list.add(new Alist(currentQ.getQUESTION(),answers.get(currentQ.getANSWER())));

        }
        //  }
        //products[0]="123";
        //quesList.
        // Listview Data


        lv = (ListView) v.findViewById(R.id.list_view);
        inputSearch = (EditText) v.findViewById(R.id.inputSearch);

        // Adding items to listview
        adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.list_item, R.id.product_name, products);
        adapter.setQuestionList(quesList_cut);
        adapter.setThemeList(themeList);
        lv.setAdapter(adapter);


        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //String st = cs.toString().trim();
                Search_Fragment.this.adapter.getFilter().filter(cs);

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
        return v;
    }
}
