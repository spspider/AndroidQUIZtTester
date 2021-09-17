package com.example.ttester_paukov;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ttester_paukov.Utils.AllThemes;
import com.example.ttester_paukov.Utils.Question;
import com.example.ttester_paukov.Utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.key;
import static android.R.attr.value;
import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;
import static com.example.ttester_paukov.R.id.WriteToDeveloper;

public class QuizActivity extends Activity {
	List<Question> quesList;
	int score=0;
	int qid=0,arrayQuestion[];
	Question currentQ;
	TextView txtQuestion;
	TextView NumberQuextion;
	RadioButton rda, rdb, rdc,rdd;
	Button butNext;
	ImageButton writetodevloper;
	Activity activity;
	RadioGroup grp;
	int ListId = -1;
	ArrayList<Integer> ArrayThemeId=new ArrayList<>();
	ArrayList<String> ArrayThemeName=new ArrayList<>();
	//String theme;
	private static final int MENU_ITEM_ABOUT = 5;
	private static String QUIZ_APP="QuizSettings";
	boolean doubleBackToExitPressedOnce = false,doubleNextToExitPressedOnce=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		Bundle extras = getIntent().getExtras();

		grp=(RadioGroup)findViewById(R.id.radioGroup1);
		activity=this;
		if (extras != null) {
			ListId = extras.getInt("ThemeIndexList",-1);
			// and get whatever type user account id is
			//Log.d(OpenFileActivity.LOG_TAG,"ListId:"+ListId);
		}
		DbHelper db=new DbHelper(this);


		ArrayList<AllThemes> Allthemelist;
		AllThemes allThemesClass;
		Allthemelist = db.getAllThemes();


		for(int i = 0;i<Allthemelist.size();i++){
			allThemesClass = Allthemelist.get(i);
			//theme=allThemesClass.getTHEME();
			ArrayThemeId.add(allThemesClass.getTHEME_id());
			ArrayThemeName.add(allThemesClass.getTHEME());
		}
		Integer QuestionID=ArrayThemeId.get(ListId);
		quesList=db.getAllQuestions(QuestionID);
		//ThemeList=db.getAllThemes()
		//Allthemelist.
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		int Questions_size[]=new int[quesList.size()];
		for (int i=0;i<quesList.size();i++){Questions_size[i]=i;}
		arrayQuestion=shuffleArray(Questions_size);
		//allThemesClass.
		//allThemesClass.

		loadQuestions();
		//Log.d(LOG_TAG,"Question"+ Arrays.toString(Question));



		currentQ=quesList.get(arrayQuestion[qid]);
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//Log.d(LOG_TAG,"Array1"+ Arrays.toString(arrayQuestion));
		//int nowQiestion = ];

		txtQuestion=(TextView)findViewById(R.id.textView1);
		NumberQuextion=(TextView) findViewById(R.id.QuestionNamber);
		rda=(RadioButton)findViewById(R.id.radio0);
		rdb=(RadioButton)findViewById(R.id.radio1);
		rdc=(RadioButton)findViewById(R.id.radio2);
		rdd=(RadioButton)findViewById(R.id.radio3);
		butNext=(Button)findViewById(R.id.button1);
		writetodevloper= (ImageButton)findViewById(WriteToDeveloper);
		writetodevloper.setOnClickListener(buttonClickListener);
		butNext.setOnClickListener(buttonClickListener);
		//qid++;
		setQuestionView();

		final int Rows=db.rowcount();

		/*
		butNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		*/
	}

	private void saveQuestions() {
		SharedPreferences.Editor editor = getSharedPreferences(QUIZ_APP, MODE_PRIVATE).edit();
		for (int i=0;i<arrayQuestion.length;i++) {
			//editor.putInt("QuestionID"+i,quesList.get(arrayQuestion[i]).getID() );
			editor.putInt("QuestionID"+ListId+"N:"+i,arrayQuestion[i] );
		}
		editor.putInt("QuestionLenght"+ListId,arrayQuestion.length );
		//editor.putInt("idName", 12);
		editor.apply();
	}
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					//Yes button clicked
					button_yes_clicked();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					//No button clicked
					saveQuestions();
					break;
			}
		}
	};

	private void button_yes_clicked() {
		SharedPreferences prefs = getSharedPreferences(QUIZ_APP, MODE_PRIVATE);
		int QuestionLenght= prefs.getInt("QuestionLenght"+ListId, -1);

		int[] Question = new int[QuestionLenght];
		for (int i = 0; i < QuestionLenght; i++) {

			Question[i] = prefs.getInt("QuestionID"+ListId+"N:"+ i, -1);

		}
		Log.d(LOG_TAG, Arrays.toString(Question));
		arrayQuestion=Question;
		qid= prefs.getInt("Question_is"+ListId, 0);
		Log.d(LOG_TAG, "qid:"+String.valueOf(qid));
		currentQ=quesList.get(arrayQuestion[qid]);
		setQuestionView();

	}

	private void loadQuestions() {

		SharedPreferences prefs = getSharedPreferences(QUIZ_APP, MODE_PRIVATE);
		int QuestionLenght= prefs.getInt("QuestionLenght"+ListId, -1);
		if((QuestionLenght!=-1)&&(ListId!=1)&&(!ArrayThemeName.get(ListId).contains("NGTV"))) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Продолжаете?").setPositiveButton("Да", dialogClickListener)
					.setNegativeButton("Нет", dialogClickListener).show();



		}
		else
		{
			saveQuestions();
			Log.d(LOG_TAG,"QuestionLenght"+ListId+"не найден");
			//снова перемешиваем вопросы
		}
	}

	@Override
	public void onBackPressed() {


		if (qid > 0) {
			qid--;
			currentQ=quesList.get(arrayQuestion[qid]);
			setQuestionView();
		}
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		//Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce=false;
			}
		}, 300);
	}

	private View.OnClickListener buttonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v){
			switch (v.getId()) {
				case R.id.button1:
					nextQuestionPressed();
					//Toast.makeText(activity, "далее", Toast.LENGTH_SHORT).show();
					break;
				case View.NO_ID:

					break;
					case WriteToDeveloper:
						//Toast.makeText(activity, "нажали кнопку) зачем?", Toast.LENGTH_SHORT).show();
						Utils.WriteToDeveloper(activity);
					break;
				default:
					// TODO Auto-generated method stub
					break;
			}
		}
	};



	private void  nextQuestionPressed() {

		nextQuestion();
		if (doubleNextToExitPressedOnce) {
			if (qid < quesList.size()) {
				qid++;
				currentQ=quesList.get(arrayQuestion[qid]);




				setQuestionView();
			}
		}
		doubleNextToExitPressedOnce = true;
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleNextToExitPressedOnce=false;
			}
		}, 600);

	}

	private void nextQuestion() {

		//
		// Log.d(LOG_TAG,"qid"+qid);


		RadioGroup grp=(RadioGroup)findViewById(R.id.radioGroup1);
		RadioButton answer=(RadioButton)findViewById(grp.getCheckedRadioButtonId());
		if (answer!=null) {
			int RAnswer = currentQ.getANSWER();
			ArrayList<String> Answers = new ArrayList<String>();
			Answers.add(currentQ.getOPT1());
			Answers.add(currentQ.getOPT2());
			Answers.add(currentQ.getOPT3());
			Answers.add(currentQ.getOPT4());

			ArrayList<String> NAnswers = new ArrayList<String>();

			NAnswers.add(rda.getText().toString());
			NAnswers.add(rdb.getText().toString());
			NAnswers.add(rdc.getText().toString());
			NAnswers.add(rdd.getText().toString());



			int YouserAnswer = Answers.indexOf(answer.getText().toString());
			if (RAnswer == YouserAnswer) {
					Utils utils = new Utils();
				    utils.deleteNGTV(currentQ,this);
				if (qid < quesList.size()-1) {
					qid++;
					currentQ=quesList.get(arrayQuestion[qid]);
					setQuestionView();

					SharedPreferences.Editor editor = getSharedPreferences(QUIZ_APP, MODE_PRIVATE).edit();
					editor.putInt("Question_is"+ListId, qid);
					//editor.putInt("idName", 12);
					editor.apply();

				} else {
					Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
					Bundle b = new Bundle();
					b.putInt("score", score); //Your score
					intent.putExtras(b); //Put your score to your next Intent
					startActivity(intent);
					finish();
				}
			} else {

				Vibrator v1 = (Vibrator) getApplication().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
				v1.vibrate(30);

				for (int i = 0; i < NAnswers.size(); i++) {
					String RAnswerTXT = Answers.get(RAnswer);
					String NegativeAnswer = NAnswers.get(i);

					final SpannableStringBuilder sb = new SpannableStringBuilder(NegativeAnswer);

					ArrayList<HashMap<Integer, Integer>> mylist = new ArrayList<HashMap<Integer, Integer>>();
					Utils utils = new Utils();
					mylist = utils.getValue(RAnswerTXT, NegativeAnswer);
					for (HashMap<Integer, Integer> map : mylist) {
						for (Map.Entry<Integer, Integer> mapEntry : map.entrySet()) {
							Integer Start = mapEntry.getKey();
							Integer Finish = mapEntry.getValue();
							System.out.println(key + " " + value);
							//здесь добавить sb.setSpan
							final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(255, 158, 158));
							final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
							sb.setSpan(fcs,Start, Finish, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							sb.setSpan(bss, Start, Finish, Spannable.SPAN_COMPOSING);
							//Log.d(LOG_TAG,"span setted for:"+i);
						}
					}
					switch (i) {
						case 0:
							rda.setText(sb);
							break;
						case 1:
							rdb.setText(sb);
							break;
						case 2:
							rdc.setText(sb);
							break;
						case 3:
							rdd.setText(sb);
							break;
						default:
							return;
					}

					//}
					//rda.setText(sb);
					//item = (String) parent.getItemAtPosition(arg2);
					//((TextView) parent.getChildAt(0)).setTextColor(0x00000000);
					//Toast.makeText(activity, "неверно", Toast.LENGTH_SHORT).show();
				}
				Utils utils = new Utils();
				//if (currentQ.)
				utils.InsertNegativeQuestion(currentQ,this);
			}
		}
		else{
			//Toast.makeText(activity, "выберите ответ", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(Menu.NONE, MENU_ITEM_ABOUT, Menu.NONE, "о приложении");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ITEM_ABOUT:
				Intent intent1 = new Intent(this, Info_screen.class);
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
	private void setQuestionView()
	{

		txtQuestion.setText(currentQ.getQUESTION());
		ArrayList<String> answersList = new ArrayList<>();
		answersList.add(currentQ.getOPT1());
		answersList.add(currentQ.getOPT2());
		answersList.add(currentQ.getOPT3());
		answersList.add(currentQ.getOPT4());
		int n1[]={0,1,2,3};
		int n2[] = shuffleArray(n1);
		rda.setText(answersList.get(n2[0]));
		rdb.setText(answersList.get(n2[1]));
		rdc.setText(answersList.get(n2[2]));
		rdd.setText(answersList.get(n2[3]));
		if (answersList.get(n2[0]).equals("")){rda.setVisibility(View.GONE);}else{rda.setVisibility(View.VISIBLE);}
		if (answersList.get(n2[1]).equals("")){rdb.setVisibility(View.GONE);}else{rdb.setVisibility(View.VISIBLE);}
		if (answersList.get(n2[2]).equals("")){rdc.setVisibility(View.GONE);}else{rdc.setVisibility(View.VISIBLE);}
		if (answersList.get(n2[3]).equals("")){rdd.setVisibility(View.GONE);}else{rdd.setVisibility(View.VISIBLE);}
		NumberQuextion.setText("В: "+(qid+1)+"ВТ:"+arrayQuestion[qid]);
		NumberQuextion.setHint(R.string.hello_world);
		grp.clearCheck();

	}

}
