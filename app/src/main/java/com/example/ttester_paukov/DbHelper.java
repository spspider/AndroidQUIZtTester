package com.example.ttester_paukov;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ttester_paukov.ExternalDB.DatabaseOpener;
import com.example.ttester_paukov.Utils.AllThemes;
import com.example.ttester_paukov.Utils.Question;
import com.example.ttester_paukov.Utils.Singleton_ALLANSWERS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;
import static com.example.ttester_paukov.OpenFileActivity.activity;

public class DbHelper extends SQLiteOpenHelper {


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
        throw new SQLiteException("Can't downgrade database from version " +
                oldVersion + " to " + newVersion);
    }

    private Context myContext;
    //public static String DB_NAME2= activity.getString(R.string.dbName);
    public static String DB_NAME = activity.getString(R.string.dbname_gradle);
    //R.string.dbName;
    static int DB_VERSION = 1;


    private static final String ANSWERS = "answers_table";
    private static final String ID_THEME = "id_theme";
    private static final String ID_QUESTION = "id_question";
    private static final String ID_RIGHT_ANSWERS = "id_right_answer";
    private static final String ID_YOUR_ANSWERS = "id_your_answer";

    private static final String TABLE_NANSWERS = "TABLE_ANSWERS";
    private static final String KEY_ID_OF_QUEST = "ID_OF_QUESTION";
    private static final String KEY_ID_OF_NANSWERS = "ID_OF_NANSWERS";


    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = DatabaseOpener.DB_NAME;
    // tasks table name
    public static final String TABLE_QUEST = "quest";
    private static final String TABLE_THEME = "THEME_TABLE";
    private static final String KEY_NAME_THEME = "theme_name";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_THEME = "theme";
    private static final String KEY_QUES = "question";
    private static final String KEY_ANSWER = "answer"; //correct option
    private static final String KEY_OPT1 = "opt1"; //option a
    private static final String KEY_OPT2 = "opt2"; //option b
    private static final String KEY_OPT3 = "opt3"; //option c
    private static final String KEY_OPT4 = "opt4"; //option c
    private static final String TEXT_NANSWERS = "NANSWERS";
    private SQLiteDatabase dbase;
    String selectQuery;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //onCreate(getReadableDatabase());
//dbase=getReadableDatabase();
        // super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;

        try {
            DatabaseOpener databaseOpener = new DatabaseOpener(myContext);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //@Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Create DBHelper ok");
        System.out.println("database created");
        dbase = db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_THEME + " INTEGER, "
                + KEY_QUES + " TEXT, "
                + KEY_ANSWER + " INTEGER, "
                + KEY_OPT1 + " TEXT, "
                + KEY_OPT2 + " TEXT, "
                + KEY_OPT3 + " TEXT, "
                + KEY_OPT4 + " TEXT" +
                ")";
        //
        //addQuestions();
        //db.close();
        db.execSQL(sql);
        String sql4 = "CREATE TABLE IF NOT EXISTS " + TABLE_NANSWERS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_THEME + " INTEGER, "
                + KEY_ID_OF_QUEST + " INTEGER, "
                + KEY_ID_OF_NANSWERS + " INTEGER, "
                + TEXT_NANSWERS + " TEXT " +
                ")";
        //
        //addQuestions();
        //db.close();
        db.execSQL(sql4);
        Log.d(LOG_TAG, sql);
        String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_THEME + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME_THEME + " TEXT" +
                ")";
        db.execSQL(sql2);
        Log.d(LOG_TAG, sql2);
        String sql3 = "CREATE TABLE IF NOT EXISTS " + ANSWERS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_THEME + " INTEGER,"
                + ID_QUESTION + " INTEGER,"
                + ID_YOUR_ANSWERS + " INTEGER,"
                + ID_RIGHT_ANSWERS + " INTEGER"
                + ")";
        db.execSQL(sql3);
        Log.d(LOG_TAG, sql3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THEME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + ANSWERS);
        // Create tables again
        //onCreate(db);
    }

    // Adding new question
    public long addTheme(AllThemes theme) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_THEME, theme.getTHEME());
        // Inserting Row
        Log.d(LOG_TAG, String.valueOf(values));
        long id = db.insert(TABLE_THEME, null, values);
        return id;
    }

    public void addQuestion(Question quest) {
        //dbase.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_THEME, quest.getTHEMEID());
        values.put(KEY_QUES, quest.getQUESTION());
        values.put(KEY_OPT1, quest.getOPT1());
        values.put(KEY_OPT2, quest.getOPT2());
        values.put(KEY_OPT3, quest.getOPT3());
        values.put(KEY_OPT4, quest.getOPT4());
        values.put(KEY_ANSWER, quest.getANSWER());
        // Inserting Row
        Log.d(LOG_TAG, String.valueOf(values));
        long getidofQUEST = db.insert(TABLE_QUEST, null, values);

        for (int i = 0; i < quest.getANSWERS().size(); i++) {
            ContentValues valuesNANSWERS = new ContentValues();
            valuesNANSWERS.put(KEY_THEME, quest.getTHEMEID());
            valuesNANSWERS.put(KEY_ID_OF_QUEST, getidofQUEST);
            valuesNANSWERS.put(KEY_ID_OF_NANSWERS, i);
            valuesNANSWERS.put(TEXT_NANSWERS, quest.getANSWERS().get(i));
            long getidofNANSWERS = db.insert(TABLE_NANSWERS, null, valuesNANSWERS);
        }
    }


    public List<Question> getAllQuestions(Integer QuestionID) {
        List<Question> quesList = new ArrayList<Question>();
        // Select All Query
        dbase = this.getReadableDatabase();
        if (QuestionID == -1) {
            //selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_QUEST + " WHERE " + KEY_THEME + " = " + QuestionID;
        }
//        dbase.close();

        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Log.d(LOG_TAG,cursor.getString(2));

                Question quest = new Question();
                //quest.setID(cursor.getInt(0));
                int IDQuestion = cursor.getInt(0);
                quest.setID(IDQuestion);
                //quest.se(cursor.getString(1));
                quest.setTHEMEID(cursor.getInt(1));
                quest.setQUESTION(cursor.getString(2));
                quest.setANSWER(cursor.getInt(3));
                quest.setOPT1(cursor.getString(4));
                quest.setOPT2(cursor.getString(5));
                quest.setOPT3(cursor.getString(6));
                quest.setOPT4(cursor.getString(7));

///////////////////////
                /*
                ArrayList<String> AnswersArrayList = new ArrayList<>();
                ArrayList<Singleton_ALLANSWERS> getAllAnswers_List;
                getAllAnswers_List = getAllAnswers(IDQuestion);
                int i = 0;
                while (i < getAllAnswers_List.size()) {
                    String nAnswer = getAllAnswers_List.get(i).getTEX_ANSWERS();
                    AnswersArrayList.add(nAnswer);
                    i++;


                }
                quest.setANSWERS(AnswersArrayList);

                Log.d(LOG_TAG,AnswersArrayList.toString());
                */

                ///////////////////////
                // quest.setANSWERS(getAllAnswers_String(IDQuestion));
                // Log.d(LOG_TAG,getAllAnswers_String(IDQuestion).toString());
                //getAllAnswers().get(0);
                //singleton_allanswers.;
                //quest.setANSWERS();
                //quest.setANSWERS();
                quesList.add(quest);
            } while (cursor.moveToNext());
            cursor.close();
        }
        //dbase.close();

        dbase.close();
        // return quest list
        return quesList;
    }

    public ArrayList<Singleton_ALLANSWERS> getAllAnswers(int Question) {
        ArrayList<Singleton_ALLANSWERS> singleton_allanswers = new ArrayList<>();
        dbase = this.getReadableDatabase();
        selectQuery = "SELECT  * FROM " + TABLE_NANSWERS + " WHERE " + KEY_ID_OF_QUEST + " = " + Question;
        //dbase = this.getReadableDatabase();
        Cursor cursorANSWER = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        //ArrayList<Singleton_ALLANSWERS> singleton_allanswers = new ArrayList<>();


        // ArrayList<String> nAnswers = new ArrayList<>();
        if (cursorANSWER.moveToFirst()) {
            do {
                Singleton_ALLANSWERS singleton_allanswers1 = new Singleton_ALLANSWERS();
                // Singleton_ALLANSWERS singleton_allanswers = new Singleton_ALLANSWERS();
                // singleton_allanswers.setQuestionNumber();
                singleton_allanswers1.setKEY_THEME(cursorANSWER.getString(1));//KEY_THEME
                singleton_allanswers1.setKEY_ID_OF_QUEST(cursorANSWER.getString(2));//KEY_ID_OF_QUEST
                singleton_allanswers1.setKEY_ID_OF_NANSWERS(cursorANSWER.getString(3));//KEY_ID_OF_NANSWERS
                singleton_allanswers1.setTEX_ANSWERS(cursorANSWER.getString(4));//TEX_ANSWERS
                singleton_allanswers.add(singleton_allanswers1);
            } while (cursorANSWER.moveToNext());
            cursorANSWER.close();

        }
        dbase.close();
        return singleton_allanswers;
    }

    public ArrayList<String> getAllAnswers_String(int Question) {
        ArrayList<String> singleton_allanswers = new ArrayList<>();
        dbase = this.getReadableDatabase();
        selectQuery = "SELECT  * FROM " + TABLE_NANSWERS + " WHERE " + KEY_ID_OF_QUEST + " = " + Question;
        //dbase = this.getReadableDatabase();
        Cursor cursorANSWER = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        //ArrayList<Singleton_ALLANSWERS> singleton_allanswers = new ArrayList<>();
        // ArrayList<String> nAnswers = new ArrayList<>();
        if (cursorANSWER.moveToFirst()) {
            do {
                singleton_allanswers.add(cursorANSWER.getString(4));

            } while (cursorANSWER.moveToNext());
            cursorANSWER.close();

        }
        dbase.close();
        return singleton_allanswers;
    }

    public ArrayList<AllThemes> getAllThemes() {
        ArrayList<AllThemes> AllThemes_list = new ArrayList<AllThemes>();

        String selectQuery = "SELECT  * FROM " + TABLE_THEME;
        dbase = this.getReadableDatabase();
        // onCreate(dbase);
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                AllThemes themes = new AllThemes();
                themes.setTHEME_id(cursor.getInt(0));
                themes.setTHEME(cursor.getString(1));
                themes.setRowCount(getRowCount(themes));
                AllThemes_list.add(themes);
            } while (cursor.moveToNext());
            cursor.close();
        }
        dbase.close();

        return AllThemes_list;
    }


    public int getRowCount(AllThemes themes) {
/*
	String selectQueryTheme = "SELECT  * FROM " + TABLE_THEME+" WHERE "+KEY_NAME_THEME+" = " + theme.trim();
	dbase=this.getReadableDatabase();
	Cursor cursorTheme = dbase.rawQuery(selectQueryTheme, null);
	int KeyTheme = Integer.parseInt(cursorTheme.getString(0));
*/

        int row_count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST + " WHERE " + KEY_THEME + " = '" + themes.getTHEME_id() + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row_count = cursor.getCount();
        return row_count;


/*
	int row_count = 0;
	quesList = db.getAllQuestions(-1);
	AllThemes_id.add(allThemesClass.getTHEME_id());


	for (int qid = 0; qid < quesList.size(); qid++) {
		currentQ = quesList.get(qid);

		if (allThemesClass.getTHEME_id() == currentQ.getTHEMEID()) {
			row_count++;//получаем кол-во вопросов данной теме
		}
		//ArrayRowCount.add(allThemesClass.getRowCount());
	}
	*/
    }


    public int rowcount() {
        int row = 0;
        selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row = cursor.getCount();
        return row;
    }


    public void deleteTables(ArrayList<Integer> pagesToDelete) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d(LOG_TAG, "key:" + String.valueOf(pagesToDelete));
        for (int i = 0; i < pagesToDelete.size(); i++) {
            db.execSQL("DELETE FROM " + TABLE_QUEST + " WHERE " + KEY_THEME + "='" + pagesToDelete.get(i) + "'");
        }
        for (int i = 0; i < pagesToDelete.size(); i++) {
            db.execSQL("DELETE FROM " + TABLE_THEME + " WHERE " + KEY_ID + "='" + pagesToDelete.get(i) + "'");
        }
        db.close();
        OpenFileActivity.makeRefresh();
    }

    public void deleteinBackground(final Activity activity, final int pagesToDelete, final int themeid) {

        new Thread() {
            public void run() {

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        deleteRows(pagesToDelete, themeid);
                    }
                });
            }
        }.start();
    }

    public void deleteRows(int pagesToDelete, int themeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_QUEST + " WHERE " + KEY_ID + "='" + pagesToDelete + "'";
        db.execSQL(sql);
        Log.d(LOG_TAG, sql);

        String selectQuery = "SELECT  * FROM " + TABLE_QUEST + " WHERE " + KEY_THEME + "='" + themeid + "'";
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int row = cursor.getCount();
        Log.d(LOG_TAG, "rows:" + row);
        if (row == 0) {
            String sql2 = "DELETE FROM " + TABLE_THEME + " WHERE " + KEY_ID + "='" + themeid + "'";
            Log.d(LOG_TAG, sql2);
            db.execSQL(sql2);
        }
        db.close();

        OpenFileActivity.makeRefresh();
    }


}
