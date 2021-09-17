package com.example.ttester_paukov;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.widget.Toast;

import com.example.ttester_paukov.Utils.AllThemes;
import com.example.ttester_paukov.Utils.FileUtils;
import com.example.ttester_paukov.Utils.Question;
import com.example.ttester_paukov.dynamicviewpager.LogFragment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;

/**
 * Created by sp_1 on 30.03.2017.
 */

public class ImportTXTfile {
    boolean next_title = false;
    boolean next_themes = false;
    String theme = "", question = "", negative_answer = "", right_answer = "";
    String Question_is = "";
    ArrayList<String> NAnswers = new ArrayList();
    String RAnswer = "";
    int row = 0;
    int question_row = 0, Positive_row = 0, Negative_row = 0;
    boolean End_of_all_rows = false;
    Activity activity;
    private long theme_id;

    public ImportTXTfile(FragmentActivity activity) {
        this.activity = activity;
    }

    public static String openfileforread(Context context, String filename) {
        String aDataRow = "";
        try {
            File myFile = new File(filename);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new FileReader(myFile));

            final StringBuilder buffer = new StringBuilder(2048);

            while ((aDataRow = myReader.readLine()) != null) {
                // buffer.append(line);
                buffer.append(aDataRow).append("/n");
                //Log.d(LOG_TAG, buffer.toString());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aDataRow;
    }

    public void openFile(String filePath, int readFileSD) {
        FileInputStream fstream1 = null;

        try {
            fstream1 = new FileInputStream(filePath);
            DataInputStream in = new DataInputStream(fstream1);
//            BufferedReader br = new BufferedReader(new InputStreamReader(in, "windows-1251"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String themename = filePath;
            if (themename.contains("/")) {
                themename = themename.substring(themename.lastIndexOf("/") + 1);
                themename = themename.contains(".txt") ? themename.substring(0, themename.indexOf(".txt")) : themename;
            }

            setTheme(themename);
            StringBuffer strBuffer = null;
            String str;

            String line = null;
            String message = new String();
            Log.d(LOG_TAG, message);
            final StringBuilder buffer = new StringBuilder(2048);
            while ((line = br.readLine()) != null) {
                // buffer.append(line);
                buffer.append(line).append("\n");
            }
            message = buffer.toString();
            switch (readFileSD) {
                case 0:
                    readFileSD(message);
                    break;
                case 1:
                    readFileSD_old(message);
                    break;
            }

            //
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openFile2(Context context, String filePath, int readFileSD) {
        String themename = filePath;
        if (themename.contains("/")) {
            themename = themename.substring(themename.lastIndexOf("/") + 1);
            themename = themename.contains(".txt") ? themename.substring(0, themename.indexOf(".txt")) : themename;
        }
        String message = openfileforread(context, filePath);
        if (message.equals("")) {
            Log.d(LOG_TAG, "empty file");
            Toast.makeText(context, "проблема импорта", Toast.LENGTH_LONG).show();
            return;
        }
        setTheme(themename);
        switch (readFileSD) {
            case 0:
                readFileSD(message);
                break;
            case 1:
                readFileSD_old(message);
                break;
        }

    }


    private static String Substring_brakets(String str) {
        str = str.substring(str.indexOf(")") + 1);
        while (str.startsWith(" ")) {
            str = str.substring(1);
        }
        return str;
    }

    private void readFileSD_Meteo(String filepath2) {
        //br=filePath;
        String str = filepath2;
        // читаем содержимое
        String AllText;
        boolean next_question = false;
        boolean standby_for_question = false;
        int count_questions = 0;
        List<String> Answers = new ArrayList<>();
        while ((!str.isEmpty())) {
            String str_n_found;
            int n_index = str.indexOf("\n");
            if (n_index < 0) {
                break;
            } else {
                str_n_found = str.substring(0, n_index);
            }
            str = str.substring(n_index + 1);

            if (str_n_found.isEmpty()) {
                str = str.substring(str.indexOf("\n") + 1);
                continue;
            }
            while (str_n_found.startsWith(" ")) {
                str_n_found = str_n_found.substring(1);
            }
            String str_question_begin = str_n_found.length() > 2 ? str_n_found.substring(0, 2) : "";
            //System.out.println("\nAnswers:" + str_question_begin + "\n");
            boolean RA_found = false;
            if (str_question_begin.contains(")")) {
                for (int i = 0; i < Answers.size(); i++) {
                    String check = Answers.get(i);
                    if (check.substring(0, 2).equals(str_question_begin)) {
                        String right_answer = str_n_found;
                        Answers.remove(Answers.get(i));
                        right_answer = Substring_brakets(right_answer);


                        Answers.add(right_answer);
                        System.out.println("\nRAnswer:" + right_answer + "\n");
                        RAnswer = right_answer;
                        //Answers.clear();
                        RA_found = true;
                    }
                }
                if (!RA_found) {
                    Answers.add(str_n_found);
                    //System.out.println("\nAnswers:" + str_n_found + "\n");
                } else {
                    List<String> Answers_List = new ArrayList<>();
                    for (int i = 0; i < Answers.size(); i++) {
                        String answer = Substring_brakets(Answers.get(i));
                        Answers_List.add(answer);
                    }
                    Answers.clear();
                    System.out.println("\nAnswers_List:" + Answers_List + "\n");
                    NAnswers.clear();
                    NAnswers.addAll(Answers_List);
                    Answers_List.clear();
                    ///
                    if ((NAnswers.size() > 0 && (!Question_is.equals("")) && (!RAnswer.equals("")))) {
                        System.out.println("\n Question_is:" + Question_is + "\n");
                        System.out.println("\n RAnswer:" + RAnswer + "\n");
                        System.out.println("\n NAnswers:" + NAnswers.toString() + "\n");
                        sendValues();
                    }
                }
            } else {
                System.out.println("\nQuestion:" + str_n_found + "\n");
                Question_is = str_n_found;
                Answers.clear();

            }

            //int n_index = str.indexOf(")");
        }
        //System.out.println("\nEND:" + "\n");
    }

    void readFileSD(String filePath) {

        //br=filePath;
        String str = filePath;
        // читаем содержимое
        boolean next_question = false;
        boolean standby_for_question = false;
        int count_questions = 0;
        int Alllines = 0, thatlines = 0;
        String str_count = filePath;
        while (str_count.contains("\n")) {
            str_count = str_count.substring(str_count.indexOf("\n") + 1);
            Alllines++;
            //System.out.println(str_count.);
        }
        if (str_count.length() != 0) Alllines++;
        thatlines = Alllines;
        //System.out.println("Alllines"+Alllines);
        while (thatlines >= 0) {
            int n_index = str.indexOf("\n");
            String str_n_found;
            if (n_index != -1) {
                str_n_found = str.substring(0, n_index);
                str = str.substring(n_index + 1);
            } else {
                str_n_found = str;
            }
            thatlines--;
            //System.out.println("thatlines"+thatlines);
            while (str_n_found.startsWith(" ")) {//что бы убрать все пробелы перед вопросом
                str_n_found = str_n_found.substring(1);
            }
            if (str_n_found.startsWith("+ ")) {
                //RIGHT ANSWER
                String right_ansver = str_n_found.substring(2);
                //System.out.println("\n Right answer:" + right_ansver + "\n");
                RAnswer = right_ansver;
                NAnswers.add(right_ansver);
                // if (standby_for_question) {
                //     //System.out.println("\nERROR to many right variants\n");
                // }
                //standby_for_question = true;
            } else if ((next_question) || (count_questions == 0)) {
                if (!str_n_found.isEmpty()) {
                    count_questions++;
                    //standby_for_question = false;
                    //QUESTION
                    //System.out.println("\n Question:" + str_n_found + "END\n");
                    Question_is = str_n_found;
                    next_question = false;

                    NAnswers.clear();
                    RAnswer = null;
                }
            } else if (str_n_found.isEmpty()) {
                next_question = true;
            } else {
                //NEGATIVE ANSWER
                NAnswers.add(str_n_found);
                //System.out.println("\n NAnswer:" + str_n_found + "END\n");
            }
            if ((NAnswers.size() > 0 && (!Question_is.equals("")) && (RAnswer != null)) && ((next_question) || (thatlines == 0))) {
                System.out.println("\n Question_is:" + Question_is + "\n");
                System.out.println("\n RAnswer:" + RAnswer + "\n");
                System.out.println("\n NAnswers:" + NAnswers.toString() + "\n");
                sendValues();
            }

        }

        Toast.makeText(activity, "Новые вопросы импортированы" + count_questions, Toast.LENGTH_SHORT).show();

    }

    void readFileSD_old(String filePath) {
        String str = filePath;
        // читаем содержимое
        boolean next_question = false;
        boolean standby_for_question = false;
        int count_questions = 0;
        int Alllines = 0, thatlines = 0;
        int problem_question = -1;
        String str_count = filePath;
        while (str_count.contains("\n")) {
            str_count = str_count.substring(str_count.indexOf("\n") + 1);
            Alllines++;
            // System.out.println(str_count);
        }
        if (str_count.length() != 0) Alllines++;
        thatlines = Alllines;
        System.out.println("Alllines" + Alllines);
        int what_is_next = -1;
        RAnswer = null;
        while (thatlines >= 0) {

            int n_index = str.indexOf("\n");
            String str_n_found;
            if (n_index != -1) {
                str_n_found = str.substring(0, n_index);
                str = str.substring(n_index + 1);
            } else {
                str_n_found = str;
            }
            thatlines--;
            //System.out.println("thatlines" + thatlines);
            while (str_n_found.startsWith(" ")) {//что бы убрать все пробелы перед вопросом
                str_n_found = str_n_found.substring(1);
            }
            switch (what_is_next) {
                case 0://Question
                    Question_is = str_n_found;
                    NAnswers.clear();
                    RAnswer = null;
                    count_questions++;
                    break;
                case 1://RightAnswer
                    String right_ansver = str_n_found;
                    if (RAnswer == null) {
                        RAnswer = right_ansver;
                    } else {
                        problem_question = count_questions;
                    }
                    NAnswers.add(right_ansver);
                    break;
                case 2://Ansers
                    NAnswers.add(str_n_found);
                    break;
                case 3:
                    //clear_send_values = 1;
                    break;
            }
            if (str_n_found.contains("00:00")) {//next Question
                what_is_next = 0;
            } else if (str_n_found.equals("+ ")) {//nextRightAnswer
                what_is_next = 1;
            } else if (str_n_found.equals("-")) {//nextAnsers
                what_is_next = 2;
            } else if (str_n_found.isEmpty()) {
                what_is_next = 3;
            } else {
                what_is_next = -1;
            }
            if ((RAnswer != null) && (problem_question == -1) && ((what_is_next == 0) || (thatlines == 0))) {
                System.out.println("\n Question_is:" + Question_is + "\n");
                System.out.println("\n RAnswer:" + RAnswer + "\n");
                System.out.println("\n NAnswers:" + NAnswers.toString() + "\n");
                System.out.println("вопросо" + count_questions);
                sendValues();
            }
            if (problem_question != -1) {
                sendProblem(count_questions);
                break;
            }

        }
        System.out.println("вопросов" + count_questions);
        // Toast.makeText(activity, "Новые вопросы импортированы по старому" + count_questions, Toast.LENGTH_SHORT).show();
    }

    void sendProblem(int problem_question) {
        Toast.makeText(activity, "ошибка в вопросе" + problem_question, Toast.LENGTH_LONG).show();
    }

    void sendValues() {

        //Log.d(LOG_TAG, "!Question_is:" + Question_is);
        //Log.d(LOG_TAG, "!Positive_row:" + (NAnswers.indexOf(RAnswer)+1));
        //Log.d(LOG_TAG, "!Negative_row:" + String.valueOf(NAnswers));


        DbHelper DB = new DbHelper(OpenFileActivity.activity);
        int ID_right_answer = -1;
        ID_right_answer = NAnswers.indexOf(RAnswer);
        //NAnswers.size()

        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        Allthemelist = DB.getAllThemes();
        int themeid = -1;
        for (int i = 0; i < Allthemelist.size(); i++) {
            allThemesClass = Allthemelist.get(i);
            if (allThemesClass.getTHEME().equals(theme)) {
                themeid = allThemesClass.getTHEME_id();
            }
        }

        for (int i = 0; NAnswers.size() < 4; i++) {
            NAnswers.add("");
        }
        //themeid=;
        //Answers_singlton a1 = new Answers_singlton(NAnswers,theme_id);
        Question q1 = new Question((int) theme_id, Question_is, NAnswers.get(0), NAnswers.get(1), NAnswers.get(2), NAnswers.get(3), ID_right_answer, NAnswers);

        Log.d(LOG_TAG, q1.getOPT1());
        Log.d(LOG_TAG, q1.getOPT2());
        Log.d(LOG_TAG, q1.getOPT3());
        Log.d(LOG_TAG, q1.getOPT4());
        Log.d(LOG_TAG, String.valueOf(q1.getANSWER()));
        Log.d(LOG_TAG, String.valueOf(q1.getID()));

        LogFragment.Text += "вопрос:" + q1.getQUESTION() + "\n";
        LogFragment.Text += "ответ0:" + q1.getOPT1() + "\n";
        LogFragment.Text += "ответ1:" + q1.getOPT2() + "\n";
        LogFragment.Text += "ответ2:" + q1.getOPT3() + "\n";
        LogFragment.Text += "ответ3:" + q1.getOPT4() + "\n";
        LogFragment.Text += "верно:" + q1.getANSWER() + "\n";
        LogFragment.Text += "\n";
        DB.addQuestion(q1);


        Question_is = RAnswer = "";
        NAnswers.clear();
    }

    public void setTheme(String theme) {
        this.theme = theme;
        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        DbHelper DB = new DbHelper(activity);
        Allthemelist = DB.getAllThemes();
        boolean found_that_theme = false;
        ArrayList ArrayTheme = new ArrayList();
        for (int i = 0; i < Allthemelist.size(); i++) {
            allThemesClass = Allthemelist.get(i);
            if (allThemesClass.getTHEME().equals(theme)) {
                found_that_theme = true;
                Toast.makeText(activity, "Эта тема уже добавлена", Toast.LENGTH_SHORT).show();
                break;
            }

            //ArrayTheme.add(allThemesClass.getTHEME());
        }
        found_that_theme = false;

        if (found_that_theme == false) {

            //DbHelper DB = new DbHelper(OpenFileActivity.activity);
            //DB.addTheme(new AllThemes(theme).setTHEME(theme));
            //Question q1 = new Question(1,Question_is, NAnswers.get(0), NAnswers.get(1), NAnswers.get(2), NAnswers.get(3),ID_right_answer);
            AllThemes All = new AllThemes(theme);
            theme_id = DB.addTheme(All);

            Toast.makeText(activity, "Добавлена новая тема", Toast.LENGTH_SHORT).show();

        }
    }


}

