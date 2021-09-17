package com.example.ttester_paukov.ExternalDB;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.util.Log;

import com.example.ttester_paukov.DbHelper;
import com.example.ttester_paukov.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;
import static com.example.ttester_paukov.OpenFileActivity.activity;

/**
 * Created by sp_1 on 03.04.2017.
 */
public class DatabaseOpener {
    private Context myContext;
    //public static String DB_NAME2= activity.getString(R.string.dbName);
    public static String DB_NAME = activity.getString(R.string.dbname_gradle);
    //R.string.dbName;
    static int DB_VERSION = 1;
    // SQLiteDatabase myDataBase;
    static String DB_PATH ;

    public DatabaseOpener(Context context) throws IOException {
        myContext = context;
        if (Build.VERSION.SDK_INT >= 4.2) {
            DB_PATH = myContext.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + myContext.getPackageName() + "/databases/";
        }


        //  super(context, DB_NAME, null, DB_VERSION);
        boolean dbexist = checkDataBase();
        if (dbexist) {
            openDataBase();
        } else {
            System.out.println("Database doesn't exist");
            createDataBase();
        }
    }

    public void createDataBase() throws IOException {
        boolean dbexistAsset = false;

            dbexistAsset = checkDataBaseAssets();

        //this.getReadableDatabase();
        //this.close();
        try {
            // String filePath = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
            //copyDataBase();
            File file = new File(DB_PATH + DB_NAME);
            if (dbexistAsset) {
                copyDataBase2(file, DB_NAME);
            } else {
                CreateDataBase(DB_NAME);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "не копируется!!!!!!!!!!!!!!!");
            throw new Error("Error copying database");

        }
    }


    boolean checkDataBase() {

        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private boolean checkDataBaseAssets() throws IOException {

        boolean checkdb = false;
        AssetManager mg = myContext.getResources().getAssets();
        InputStream is = null;
        try {
            is = mg.open("databases/" + DB_NAME);
            //File exists so do something with it
            return true;
        } catch (IOException ex) {
            //file does not exist
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return checkdb;
    }


    private void copyDataBase2(File dbFile, String db_name) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        SQLiteDatabase db = myContext.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        //sqldb=db;
        db.close();
        try {
            is = myContext.getAssets().open("databases/" + db_name);
            os = new FileOutputStream(dbFile);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                os.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw (e);
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private SQLiteDatabase CreateDataBase(String dbName) {
        // SQLiteDatabase db = myContext.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        // sqldb=db;
        //  db.close();
        //  DbHelper dbHelper=new DbHelper(myContext);
        // dbHelper.onCreate(db);
        //SQLiteDatabase myDataBase;
        //String mypath = DB_PATH + DB_NAME;
        //myDataBase = SQLiteDatabase.openOrCreateDatabase(mypath, null);
        SQLiteDatabase db = myContext.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        // DbHelper dbHelper = new DbHelper(myContext);
        // dbHelper.onCreate(myDataBase);
        return db;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        // Open the database
        //MySQLiteOpenHelper helper = new MySQLiteOpenHelper(myContext,DB_PATH +DB_NAME);
        //SQLiteDatabase database = helper.getReadableDatabase();
        //String filePath = database.getPath();
        // database.close();
        SQLiteDatabase myDataBase;
        String mypath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
        // DbHelper dbHelper=new DbHelper(myContext);
        // dbHelper.onCreate(myDataBase);
        //  myDataBase=dbHelper.getReadableDatabase();
        return myDataBase;
    }


}