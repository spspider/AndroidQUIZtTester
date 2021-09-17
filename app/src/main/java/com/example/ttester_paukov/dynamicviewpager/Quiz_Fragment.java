package com.example.ttester_paukov.dynamicviewpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ttester_paukov.DbHelper;
import com.example.ttester_paukov.Info_screen;
import com.example.ttester_paukov.OpenFileActivity;
import com.example.ttester_paukov.R;
import com.example.ttester_paukov.ResultActivity;
import com.example.ttester_paukov.Utils.AllThemes;
import com.example.ttester_paukov.Utils.Question;
import com.example.ttester_paukov.Utils.Utils;
import com.example.ttester_paukov.Utils.Utils_onTouchSize;
import com.example.ttester_paukov.drawer.OnBackPressedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;
import static com.example.ttester_paukov.R.id.WriteToDeveloper;

/**
 * Created by anskaal on 2/29/16.
 */
public class Quiz_Fragment extends Fragment implements OnBackPressedListener {


    List<Question> quesList;
    int score = 0;
    int qid = 0, arrayQuestion[];
    Question currentQ;
    TextView txtQuestion;
    TextView NumberQuextion;
    RadioButton rda, rdb, rdc, rdd;
    Button butNext;
    ImageButton writetodevloper;
    Activity activity;
    RadioGroup grp;
    int ListId = -1;
    ArrayList<Integer> ArrayThemeId = new ArrayList<>();
    ArrayList<String> ArrayThemeName = new ArrayList<>();
    //String theme;
    private static final int MENU_ITEM_ABOUT = 5;
    private static String QUIZ_APP = "QuizSettings";
    boolean doubleBackToExitPressedOnce = false, doubleNextToExitPressedOnce = false;
    View rootView;
    private static final String ARG_KEY = "ARG_KEY";

    public static Fragment newInstance(int key, int listId) {

        Bundle args = new Bundle();
        Quiz_Fragment fragment = new Quiz_Fragment();
        args.putInt("ThemeIndexList", listId);
        args.putInt(ARG_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getArguments();
        if (extras != null) {
            ListId = extras.getInt("ThemeIndexList", -1);
            Log.d(LOG_TAG, "ListId" + ListId);
            // and get whatever type user account id is
            //Log.d(OpenFileActivity.LOG_TAG,"ListId:"+ListId);
        }
        activity = this.getActivity();
        DbHelper db = new DbHelper(this.getActivity());


        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        Allthemelist = db.getAllThemes();


        for (int i = 0; i < Allthemelist.size(); i++) {
            allThemesClass = Allthemelist.get(i);
            //theme=allThemesClass.getTHEME();
            ArrayThemeId.add(allThemesClass.getTHEME_id());
            ArrayThemeName.add(allThemesClass.getTHEME());
        }
        Integer QuestionID = ArrayThemeId.get(ListId);
        quesList = db.getAllQuestions(QuestionID);
        //ThemeList=db.getAllThemes()
        //Allthemelist.
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //allThemesClass.
        //allThemesClass.
        arrayQuestion = (getQuestions_size());
        currentQ = quesList.get(arrayQuestion[qid]);
        loadQuestions();
        //Log.d(LOG_TAG,"Question"+ Arrays.toString(Question));

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        //Log.d(LOG_TAG,"Array1"+ Arrays.toString(arrayQuestion));
        //int nowQiestion = ];


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_quiz, null);
        grp = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
        //grp.setOnTouchListener(myOnTouchListener);
        txtQuestion = (TextView) rootView.findViewById(R.id.textView1);
        NumberQuextion = (TextView) rootView.findViewById(R.id.QuestionNamber);
        rda = (RadioButton) rootView.findViewById(R.id.radio0);
        rdb = (RadioButton) rootView.findViewById(R.id.radio1);
        rdc = (RadioButton) rootView.findViewById(R.id.radio2);
        rdd = (RadioButton) rootView.findViewById(R.id.radio3);
        Utils_onTouchSize utils_onTouchSize = new Utils_onTouchSize();
        utils_onTouchSize.setRadioGroup(grp);
        //utils_onTouchSize.setPrevscale(txtQuestion.getTextSize()/utils_onTouchSize.multiplysize);
        //utils_onTouchSize.setPrevscaleRbtn(rda.getTextSize()/utils_onTouchSize.multiplysize);
        rda.setOnTouchListener(utils_onTouchSize.touchListener2);
        rdb.setOnTouchListener(utils_onTouchSize.touchListener2);
        rdc.setOnTouchListener(utils_onTouchSize.touchListener2);
        rdd.setOnTouchListener(utils_onTouchSize.touchListener2);
        txtQuestion.setOnTouchListener(utils_onTouchSize.touchListener2);
        utils_onTouchSize.setPrevscale(txtQuestion.getTextSize());
        utils_onTouchSize.setPrevscaleRbtn(rda.getTextSize());
        butNext = (Button) rootView.findViewById(R.id.button1);
        writetodevloper = (ImageButton) rootView.findViewById(WriteToDeveloper);
        writetodevloper.setOnClickListener(buttonClickListener);
        butNext.setOnClickListener(buttonClickListener);
        //qid++;
        setQuestionView();

        //final int Rows=db.rowcount();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Log.i(getTag(), "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //getActivity().getActionBar().show();
                    Log.d(LOG_TAG, "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    // String cameback="CameBack";
                    doBack();
                    return true;
                } else {
                    return false;
                }
            }
        });

        return rootView;

    }


    private void saveQuestions() {


        SharedPreferences.Editor editor = getActivity().getSharedPreferences(QUIZ_APP, MODE_PRIVATE).edit();
        for (int i = 0; i < arrayQuestion.length; i++) {
            //editor.putInt("QuestionID"+i,quesList.get(arrayQuestion[i]).getID() );
            editor.putInt("QuestionID" + ListId + "N:" + i, arrayQuestion[i]);
        }
        editor.putInt("QuestionLenght" + ListId, arrayQuestion.length);
        //editor.putInt("idName", 12);
        editor.apply();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    button_yes_clicked();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    askifNeedtoMix();
                    saveQuestions();
                    break;
            }
        }
    };

    DialogInterface.OnClickListener dialogClickListenerMix = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    arrayQuestion = shuffleArray(getQuestions_size());
                    currentQ = quesList.get(arrayQuestion[qid]);
                    setQuestionView();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    setQuestionView();
                    break;
            }
        }
    };

    private int[] getQuestions_size() {
        int Questions_size[] = new int[quesList.size()];
        for (int i = 0; i < quesList.size(); i++) {
            Questions_size[i] = i;
        }
        return Questions_size;
    }

    private void button_yes_clicked() {
        SharedPreferences prefs = getActivity().getSharedPreferences(QUIZ_APP, MODE_PRIVATE);
        int QuestionLenght = prefs.getInt("QuestionLenght" + ListId, -1);

        int[] Question = new int[QuestionLenght];
        for (int i = 0; i < QuestionLenght; i++) {

            Question[i] = prefs.getInt("QuestionID" + ListId + "N:" + i, -1);

        }
        Log.d(LOG_TAG, Arrays.toString(Question));
        arrayQuestion = Question;
        qid = prefs.getInt("Question_is" + ListId, 0);
        Log.d(LOG_TAG, "qid:" + String.valueOf(qid));
        currentQ = quesList.get(arrayQuestion[qid]);
        setQuestionView();

    }

    private void loadQuestions() {

        SharedPreferences prefs = getActivity().getSharedPreferences(QUIZ_APP, MODE_PRIVATE);
        int QuestionLenght = prefs.getInt("QuestionLenght" + ListId, -1);
        if ((QuestionLenght != -1) && (ListId != 1) && (!ArrayThemeName.get(ListId).contains("NGTV"))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage("Продолжаете?").setPositiveButton("Да", dialogClickListener)
                    .setNegativeButton("Нет", dialogClickListener).show();


        } else {
            askifNeedtoMix();
            saveQuestions();
            Log.d(LOG_TAG, "QuestionLenght" + ListId + "не найден");
            //снова перемешиваем вопросы
        }
    }

    private void askifNeedtoMix() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage("Перемешать вопросы?").setPositiveButton("Да", dialogClickListenerMix)
                .setNegativeButton("Нет", dialogClickListenerMix).show();
    }

    @Override
    public void doBack() {
        //Log.d(LOG_TAG,"doBack()");
        if (qid > 0) {
            qid--;
            currentQ = quesList.get(arrayQuestion[qid]);
            setQuestionView();
        }
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 300);
    }

    /*
        @Override
        public void onBackPressed() {



        }
    */
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button1:
                    nextQuestionPressed();
                    //Toast.makeText(activity, "далее", Toast.LENGTH_SHORT).show();
                    break;
                case View.NO_ID:

                    break;
                case WriteToDeveloper:
                    //Toast.makeText(activity, "нажали кнопку) зачем?", Toast.LENGTH_SHORT).show();
                    //Utils.WriteToDeveloper(activity);
                    break;
                default:
                    // TODO Auto-generated method stub
                    break;
            }
        }
    };


    private void nextQuestionPressed() {

        nextQuestion();
        if (doubleNextToExitPressedOnce) {
            if (qid < quesList.size()) {
                qid++;
                currentQ = quesList.get(arrayQuestion[qid]);


                setQuestionView();
            }
        }
        doubleNextToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleNextToExitPressedOnce = false;
            }
        }, 600);

    }

    private void nextQuestion() {

        //
        // Log.d(LOG_TAG,"qid"+qid);


        RadioGroup grp = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
        RadioButton answer = (RadioButton) rootView.findViewById(grp.getCheckedRadioButtonId());
        if (answer != null) {
            int RAnswer = currentQ.getANSWER();
            ArrayList<String> Answers = new ArrayList<String>();
            Answers.add(currentQ.getOPT1());
            Answers.add(currentQ.getOPT2());
            Answers.add(currentQ.getOPT3());
            Answers.add(currentQ.getOPT4());


            int YouserAnswer = Answers.indexOf(answer.getText().toString());
            if (RAnswer == YouserAnswer) {
                Utils utils = new Utils();
                utils.deleteNGTV(currentQ, this.getActivity());
                if (qid < quesList.size() - 1) {
                    qid++;
                    currentQ = quesList.get(arrayQuestion[qid]);
                    setQuestionView();

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(QUIZ_APP, MODE_PRIVATE).edit();
                    editor.putInt("Question_is" + ListId, qid);
                    //editor.putInt("idName", 12);
                    editor.apply();

                } else {
                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(OpenFileActivity.class.getName());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.getClass().getName()).addToBackStack(fragment.getClass().getName()).commit();

                    Intent intent = new Intent(this.getActivity(), ResultActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("score", score); //Your score
                    b.putInt("size", quesList.size()); //Your score
                    intent.putExtras(b); //Put your score to your next Intent
                    startActivity(intent);
                    //getActivity().finish();
                }
            } else {
                score++;
                Vibrator v1 = (Vibrator) getActivity().getApplication().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v1.vibrate(30);

                ArrayList<String> NAnswers = new ArrayList<String>();

                NAnswers.add(rda.getText().toString());
                NAnswers.add(rdb.getText().toString());
                NAnswers.add(rdc.getText().toString());
                NAnswers.add(rdd.getText().toString());


                Utils utils = new Utils();
                String RAnasverTXT = Answers.get(RAnswer);
                ArrayList<SpannableStringBuilder> span = new ArrayList<>();
                span = utils.setSpan(RAnasverTXT, NAnswers);
                rda.setText(span.get(0));
                rdb.setText(span.get(1));
                rdc.setText(span.get(2));
                rdd.setText(span.get(3));

                //Utils utils = new Utils();
                //if (currentQ.)
                utils.InsertNegativeQuestion(currentQ, this.getActivity());
            }
        } else {
            //Toast.makeText(activity, "выберите ответ", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            menu.add(Menu.NONE, MENU_ITEM_ABOUT, Menu.NONE, "о приложении");
            return true;
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_ABOUT:
                Intent intent1 = new Intent(this.getActivity(), Info_screen.class);
                startActivity(intent1);
                return true;
            default:
                return false;
        }
    }

    private int[] shuffleArray(int array[]) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = (int) Math.floor(Math.random() * (i + 1));
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return array;
    }

    private void setQuestionView() {

        txtQuestion.setText(currentQ.getQUESTION());
        ArrayList<String> answersList = new ArrayList<>();
        answersList.add(currentQ.getOPT1());
        answersList.add(currentQ.getOPT2());
        answersList.add(currentQ.getOPT3());
        answersList.add(currentQ.getOPT4());
        int n1[] = {0, 1, 2, 3};
        int n2[] = shuffleArray(n1);
        rda.setText(answersList.get(n2[0]));
        rdb.setText(answersList.get(n2[1]));
        rdc.setText(answersList.get(n2[2]));
        rdd.setText(answersList.get(n2[3]));
        if (answersList.get(n2[0]).equals("")) {
            rda.setVisibility(View.GONE);
        } else {
            rda.setVisibility(View.VISIBLE);
        }
        if (answersList.get(n2[1]).equals("")) {
            rdb.setVisibility(View.GONE);
        } else {
            rdb.setVisibility(View.VISIBLE);
        }
        if (answersList.get(n2[2]).equals("")) {
            rdc.setVisibility(View.GONE);
        } else {
            rdc.setVisibility(View.VISIBLE);
        }
        if (answersList.get(n2[3]).equals("")) {
            rdd.setVisibility(View.GONE);
        } else {
            rdd.setVisibility(View.VISIBLE);
        }
        NumberQuextion.setText("В: " + (qid + 1) + "ВТ:" + arrayQuestion[qid]);
        NumberQuextion.setHint(R.string.hello_world);
        grp.clearCheck();

    }


}
