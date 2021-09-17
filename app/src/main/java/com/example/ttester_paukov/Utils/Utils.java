package com.example.ttester_paukov.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.example.ttester_paukov.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sp_1 on 06.04.2017.
 */
public class Utils {
    public static void WriteToDeveloper(Activity activity) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "spspider@inbox.ru"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "проблема");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "тТестер");
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

        activity.startActivity(Intent.createChooser(emailIntent, "Написать разработчику через:"));
    }
public ArrayList<SpannableStringBuilder> setSpan(String RAnswer,ArrayList<String> NAnswers){
    ArrayList<SpannableStringBuilder> spannableArray= new ArrayList<>();
    //spannableArray
    for (int i = 0; i < NAnswers.size(); i++) {
        String NegativeAnswer = NAnswers.get(i);
        final SpannableStringBuilder sb = new SpannableStringBuilder(NegativeAnswer);
        ArrayList<HashMap<Integer, Integer>> mylist = new ArrayList<HashMap<Integer, Integer>>();
        //Utils utils = new Utils();
        if (!NegativeAnswer.equals(RAnswer)) {
            mylist = getValue(RAnswer, NegativeAnswer);
            for (HashMap<Integer, Integer> map : mylist) {
                for (Map.Entry<Integer, Integer> mapEntry : map.entrySet()) {
                    Integer Start = mapEntry.getKey();
                    Integer Finish = mapEntry.getValue();
                    //System.out.println(key + " " + value);
                    //здесь добавить sb.setSpan
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(255, 158, 158));
                    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                    sb.setSpan(fcs, Start, Finish, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sb.setSpan(bss, Start, Finish, Spannable.SPAN_COMPOSING);
                    //Log.d(LOG_TAG,"span setted for:"+i);
                }
            }
        }
        else{
            ArrayList<int[]> mylistIntRight = new ArrayList<>();
            mylistIntRight= getValueRight(RAnswer,NAnswers);
            //Log.d(LOG_TAG,"marks:"+ (mylistIntRight));
            for (int iAR=0;iAR<mylistIntRight.size();iAR++){
                int MarksRight[]= mylistIntRight.get(iAR);

                    Integer Start = MarksRight[0];
                    Integer Finish = MarksRight[1];
                //Log.d(LOG_TAG,"marks:"+ Start+" "+Finish);
                    //System.out.println(key + " " + value);
                    //здесь добавить sb.setSpan
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 188, 0));
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD_ITALIC);
                    sb.setSpan(fcs, Start, Finish, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sb.setSpan(bss, Start, Finish, Spannable.SPAN_COMPOSING);

            }
            //for (int A1=0;A1<)
        }

        spannableArray.add(i,sb);
    }
    return spannableArray;
}
    public ArrayList<HashMap<Integer, Integer>> getValue(String RAnswer, String NegativeAnswer) {

        String[] splitted;
        String[] splitted2;
        splitted=NegativeAnswer.replaceAll("([-+.^:,]')","").split(" ");
        splitted2=RAnswer.replaceAll("([-+.^:,]')","").split(" ");
        ArrayList<HashMap<Integer,Integer>> NotMatched=new ArrayList<>();
        //HashMap<Integer,Integer> NegativeWord=new HashMap<>();
        for (int i=0;i<splitted.length;i++){
                boolean founded=false;
                for (int i1 = 0; i1 < splitted2.length; i1++) {
                    if (splitted[i].equals(splitted2[i1])) {
                        if ((i-i1<4)||(i+i1<4)) {
                            founded=true;
                        }
                    }
                }
                if (!founded){
                    int Start=NegativeAnswer.indexOf(splitted[i]);
                    int Finish = NegativeAnswer.indexOf(splitted[i])+splitted[i].length();
                    HashMap<Integer, Integer> my = new HashMap();
                    my.put(Start,Finish);
                    NotMatched.add(my);
                }
            }

        return NotMatched;
    }
    private ArrayList<int[]> getValueRight(String RAnswer, ArrayList<String> NegativeAnswersArray) {

        String[] splittedN;
        String[] splittedR;
        ArrayList<int[]> NotMatched = new ArrayList<>();
        String NegativeAnswer="";
        for (int A=0;A<NegativeAnswersArray.size();A++) {
            if (!NegativeAnswersArray.get(A).equals(RAnswer)) {
                NegativeAnswer = NegativeAnswer + " " + NegativeAnswersArray.get(A);
            }
        }

        splittedN = NegativeAnswer.replaceAll("([-+.^:,]')","").split(" ");
        splittedR = RAnswer.replaceAll("([-+.^:,]')","").split(" ");

        //HashMap<Integer,Integer> NegativeWord=new HashMap<>();
        for (int i = 0; i < splittedR.length; i++) {

            boolean founded = false;
            for (int i1 = 0; i1 < splittedN.length; i1++) {
                if (splittedR[i].equals(splittedN[i1])) {
                    //if ((i - i1 < 4) || (i + i1 < 4)) {
                        founded = true;
                    //Log.d(LOG_TAG,"Found:"+splittedR[i]);
                    //}
                }
            }
            if (!founded) {

                //System.out.println("A:"+splittedR[i]);
                int Start = RAnswer.indexOf(splittedR[i]);
                int Finish = RAnswer.indexOf(splittedR[i]) + splittedR[i].length();
                //HashMap<Integer, Integer> my = new HashMap();
                //my.put(Start, Finish);
                //System.out.println("A:"+Start+"B"+Finish);
                int StartFinish[]={Start,Finish};
                //StartFinish[Start]
                NotMatched.add(StartFinish);
            }

            //}
        }
        if (NotMatched.size()==0) {
            for (int aArr = 0; aArr < NegativeAnswersArray.size(); aArr++) {
                splittedN = NegativeAnswersArray.get(aArr).replaceAll("([-+.^:,]')","").split(" ");
                for (int i = 0; i < splittedR.length; i++) {

                    boolean founded = false;
                    for (int i1 = 0; i1 < splittedN.length; i1++) {
                        if (splittedR[i].equals(splittedN[i1])) {
                            founded = true;
                        }
                    }
                    if (!founded) {

                        //System.out.println("A:" + splittedR[i]);
                        int Start = RAnswer.indexOf(splittedR[i]);
                        int Finish = RAnswer.indexOf(splittedR[i]) + splittedR[i].length();
                        //HashMap<Integer, Integer> my = new HashMap();
                        //my.put(Start, Finish);
                        //System.out.println("A:"+Start+"B"+Finish);
                        int StartFinish[] = {Start, Finish};
                        //StartFinish[Start]
                        NotMatched.add(StartFinish);
                    }

                    //}
                }
            }
        }
        return NotMatched;
    }
    public void InsertNegativeQuestion(Question currentQ, Activity activity) {

            String theme = null;
            DbHelper DB = new DbHelper(activity);
            int ID_right_answer=-1;
            //NAnswers.size()
            ArrayList<AllThemes> Allthemelist;
            AllThemes allThemesClass;
            Allthemelist = DB.getAllThemes();
            int themeid=-1;

            for(int i = 0;i<Allthemelist.size();i++){
                allThemesClass = Allthemelist.get(i);
                    if(allThemesClass.getTHEME_id()==currentQ.getTHEMEID()){
                        theme="NGTV:"+allThemesClass.getTHEME().replace("NGTV:","");
                        break;
                    //themeid = allThemesClass.getTHEME_id();
                }
            }
            for(int i = 0;i<Allthemelist.size();i++){
                allThemesClass = Allthemelist.get(i);
                if(allThemesClass.getTHEME().equals(theme)){
                    themeid=allThemesClass.getTHEME_id();
                    break;
                    //themeid = allThemesClass.getTHEME_id();
                }
            }
            if (themeid==-1){
                //создать тему с названием NEGATIVE
                AllThemes All = new AllThemes(theme);
                themeid=(int)DB.addTheme(All);
/*
                for(int i = 0;i<Allthemelist.size();i++){
                    allThemesClass = Allthemelist.get(i);
                    if(allThemesClass.getTHEME().equals(theme)){
                        themeid=allThemesClass.getTHEME_id();
                        break;
                        //themeid = allThemesClass.getTHEME_id();
                    }
                }
                */
            }

            Question q1 = new Question(themeid,currentQ.getQUESTION(), currentQ.getOPT1(), currentQ.getOPT2(), currentQ.getOPT3(), currentQ.getOPT4(),currentQ.getANSWER(),currentQ.getANSWERS());
            //Log.d(OpenFileActivity.LOG_TAG, String.valueOf(q1));
            DB.addQuestion(q1);
            //db.insert()


    }

    public void deleteNGTV(final Question currentQ, final Activity activity) {
        new Thread() {
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DbHelper db = new DbHelper(activity);
                        ArrayList<AllThemes> Allthemelist;
                        AllThemes allThemesClass;
                        Allthemelist = db.getAllThemes();
                        String theme = null;
                        for (int i = 0; i < Allthemelist.size(); i++) {
                            //Allthemelist.
                            allThemesClass = Allthemelist.get(i);
                            if (allThemesClass.getTHEME_id() == currentQ.getTHEMEID()) {
                                theme = allThemesClass.getTHEME();
                            }
                        }
                        if (theme.contains("NGTV")) {
                            DbHelper dbhelper = new DbHelper(activity);
                            //dbhelper.deleteRows(currentQ.getID(),currentQ.getTHEMEID());
                            dbhelper.deleteinBackground(activity, currentQ.getID(), currentQ.getTHEMEID());
                        }
                    }
                });
                //if (currentQ.)

//удалить темы
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        }.start();
    }
}
