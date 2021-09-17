package com.example.ttester_paukov;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ttester_paukov.ExternalDB_worked.MainActivity_externalDB;
import com.example.ttester_paukov.Utils.AllThemes;
import com.example.ttester_paukov.Utils.ArrayAdapter;
import com.example.ttester_paukov.Utils.FileUtils;
import com.example.ttester_paukov.Utils.FileUtils2;
import com.example.ttester_paukov.Utils.GetPath;
import com.example.ttester_paukov.Utils.Question;
import com.example.ttester_paukov.Utils.RealPathUtil;
import com.example.ttester_paukov.dynamicviewpager.LogFragment;
import com.example.ttester_paukov.dynamicviewpager.Quiz_Fragment;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sp_1 on 27.03.2017.
 */

public class OpenFileActivity extends Fragment {


    private static final int CHOOSE_FILE_REQUESTCODE = 101;
    public static final String LOG_TAG = "myLogs";

    public static Activity activity;
    static ListView lvMain = null;
    // private static final int FILE_SELECT_CODE = 0;

    final String FILENAME = "file";

    public static final String DIR_SD = "MyFiles";
    public static final String FILENAME_SD = "тест короткий.txt";

    /**
     * Called when the activity is first created.
     */

    private static final int MENU_ITEM_START = 1;
    private static final int MENU_ITEM_DELETE = 2;
    private static final int MENU_ITEM_IMPORT = 3;
    private static final int MENU_ITEM_EXTERNALDATABASE = 4;
    private static final int MENU_ITEM_ABOUT = 5;
    private static final int MENU_ITEM_SEARCH = 6;
    private static final int MENU_ITEM_IMPORT_OLD = 7;
    //private static final int MENU_ITEM_DELETE = 2;
    //Activity activity;
    private static View view;


    private static final int request_code = 1, FILE_SELECT_CODE = 101;
    private String readFileSD = "readFileSD";
    private int readFileSD_int = 0;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, MENU_ITEM_START, Menu.NONE, "Начать тестирование");
        menu.add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "Удалить тесты");
        menu.add(Menu.NONE, MENU_ITEM_IMPORT, Menu.NONE, "Импортировать тесты");
        //menu.add(Menu.NONE, MENU_ITEM_EXTERNALDATABASE, Menu.NONE, "внешняя база");
        menu.add(Menu.NONE, MENU_ITEM_ABOUT, Menu.NONE, "о приложении");
        menu.add(Menu.NONE, MENU_ITEM_SEARCH, Menu.NONE, "поиск");
        menu.add(Menu.NONE, MENU_ITEM_IMPORT_OLD, Menu.NONE, "Импортировать #");
        //return true;
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_START:

                return true;
            case MENU_ITEM_DELETE:
                deleteCheckedItems();
                //sba.
                return true;
            case MENU_ITEM_IMPORT:
                readFileSD_int = 0;
                openFile(readFileSD_int);
                return true;
            case MENU_ITEM_IMPORT_OLD:
                readFileSD_int = 1;
                openFile(readFileSD_int);
                return true;
            case MENU_ITEM_EXTERNALDATABASE:
                Intent intent = new Intent(this.getActivity(), MainActivity_externalDB.class);

                startActivity(intent);
                return true;
            case MENU_ITEM_ABOUT:
                Intent intent1 = new Intent(this.getActivity(), Info_screen.class);
                startActivity(intent1);
                return true;
            case MENU_ITEM_SEARCH:
                Intent intent2 = new Intent(this.getActivity(), Search.class);
                startActivity(intent2);
                return true;
            default:
                return false;
        }
    }

    private void deleteCheckedItems() {
        SparseBooleanArray sba = lvMain.getCheckedItemPositions();
        DbHelper DB = new DbHelper(this.getActivity());
        ArrayList<Integer> pagesToDelete = new ArrayList<>();
        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        DbHelper db = new DbHelper(activity);
        Allthemelist = db.getAllThemes();
        ArrayList<Integer> AllThemes_id = new ArrayList<>();
        for (int i = 0; i < Allthemelist.size(); i++) {
            allThemesClass = Allthemelist.get(i);
            AllThemes_id.add(allThemesClass.getTHEME_id());
        }
        for (int i = 0; i < sba.size(); ++i) {
            int key = sba.keyAt(i);
            if (sba.get(key)) {

                pagesToDelete.add(AllThemes_id.get(key));
                // System.out.print(lv.getItemAtPosition(key) + "|");
            }
        }
        DB.deleteTables(pagesToDelete);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.openfile, container, false);
        activity = this.getActivity();

        lvMain = (ListView) view.findViewById(R.id.listTHEMES);

        //AllTheme.
        SQLiteDatabase sqldb = makeDataBaseCopy();

        makeRefresh();


        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.openfile);
        setHasOptionsMenu(true);


        //}

        //DbHelper db=new DbHelper(this);
        // quesList=db.getAllQuestions();
    }

    private SQLiteDatabase makeDataBaseCopy() {
        //CopyDataBase copydatabase = new CopyDataBase(this);
        //copydatabase.openDatabase();
        //boolean copyed= copydatabase.copyDatabaseFrom``````````````````````Assets(this,"db",true);
        //Log.d(LOG_TAG, String.valueOf(copyed));
        //ContextWrapper cw =new ContextWrapper(this);
        //String DATABASE_PATH =cw.getFilesDir().getAbsolutePath()+ "/databases/"; //edited to databases
        //DBQuery db = new DBQuery(this);


        //DatabaseOpener db = new DatabaseOpener(this.getActivity());
        //  db.openDataBase();
        //   SQLiteDatabase sqldb = db.getSQLDatabase();

        //   db.close();
        //   return sqldb;


        DbHelper dbHelper = new DbHelper(activity);
        //  dbHelper.onCreate(dbHelper.getReadableDatabase());

        return null;
        //CopyDataBase.movedb(this, getDatabasePath(getDbName()), new File(Environment.getExternalStorageDirectory(), getDatabaseBackupPath()));
        //copydatabase.movedb(getDatabasePath("db"),new File((Environment.getExternalStorageDirectory()),DATABASE_PATH+"db"));
    }


    public static void makeRefresh() {

        List<Question> quesList;
        Question currentQ;
        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        DbHelper db = new DbHelper(activity);
        //db.onCreate(sqldb);
        //db.onCreate(db.getReadableDatabase());
        Allthemelist = db.getAllThemes();

        ArrayList ArrayTheme = new ArrayList();
        //ArrayList ArrayRowCount=new ArrayList();
        ArrayList<Integer> AllThemes_id = new ArrayList<>();
        for (int i = 0; i < Allthemelist.size(); i++) {
            allThemesClass = Allthemelist.get(i);
            //что бы работало немного быстрее нужно убрать это:
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
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


            //ArrayTheme.add(allThemesClass.getTHEME() + " :" + row_count);
            ArrayTheme.add(allThemesClass.getTHEME() + ":" + allThemesClass.getRowCount());
            //ArrayRowCount.add(allThemesClass.getRowCount());
        }


//Log.d(LOG_TAG,"size:"+Allthemelist.size());
        //DbHelper db=new DbHelper(ac);

        //quesList.
        //quesList.


        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvMain.setItemsCanFocus(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_multiple_choice, ArrayTheme);
        adapter.setSpanString("NGTV");
        //adapter.insert();
        lvMain.setAdapter(adapter);
        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                if (lvMain.isItemChecked(pos)) {
                    lvMain.setItemChecked(pos, false);
                } else {
                    lvMain.setItemChecked(pos, true);
                }
                ;
/*      //ViewPager
                Class fragmentClass;

                if (lvMain.isItemChecked(po                fragmentClass = MainActivity_ViewPager_quiz.class;
                Bundle bundle = new Bundle();
                bundle.putInt("ThemeIndexList", (int)id);
                //fragment.setArguments(bundle);
                makeFragment(fragmentClass,bundle);
*/
                // TODO Auto-generated method stubs)){lvMain.setItemChecked(pos,false);}else{lvMain.setItemChecked(pos,true);}
                //Log.v(LOG_TAG,"long clicked pos: " + pos);
                //lvMain.setSelection();

                return true;
            }
        });
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Class fragmentClass;
                fragmentClass = Quiz_Fragment.class;
                Bundle bundle = new Bundle();
                bundle.putInt("ThemeIndexList", (int) id);
                //fragment.setArguments(bundle);
                makeFragment(fragmentClass, bundle);
/*
                Intent intent = new Intent(activity,QuizActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ThemeIndexList",(int)id);
                intent.putExtras(bundle);
                activity.startActivity(intent);
*/
                //if (lvMain.isItemChecked(position)){lvMain.setItemChecked(position,false);}else{lvMain.setItemChecked(position,true);}

            }
        });
        //Log.d(LOG_TAG,"refresh");

    }

    private static Fragment makeFragment(Class fragmentClass, Bundle bundle) {
        Fragment fragment = null;
        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentActivity activity = (FragmentActivity) view.getContext();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.container, fragment, fragmentClass.getName()).addToBackStack(fragmentClass.getName()).commit();

        }
        return fragment;
    }

    public void openFile(int ReadFileSD) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // start runtime permission
            Boolean hasPermission = (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                Log.e(LOG_TAG, "get permision   ");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_code);
            } else {
                Log.e(LOG_TAG, "get permision-- already granted ");
                //showFileChooser();
            }
        } else {
            //readfile();
            //showFileChooser();
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(readFileSD, ReadFileSD);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this.getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == getActivity().RESULT_OK) {

                    Uri uri = data.getData();
                    String path = null;
                    //path = FileUtils.getPath2(uri);
                    path = FileUtils2.getPath(getContext(), uri);

                    Log.w(LOG_TAG, "File Path: " + path);
                    ImportTXTfile ITXT = new ImportTXTfile(this.getActivity());
                    LogFragment.Text = uri.getPath()+"/n";
                    LogFragment.Text += path+"/n";


                    ITXT.openFile(path, readFileSD_int);

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        makeRefresh();
    }

    public static OpenFileActivity newInstance() {
        OpenFileActivity openfileactivity = new OpenFileActivity();
        Bundle args = new Bundle();
        //args.putInt("pageIndex", pageIndex);
        openfileactivity.setArguments(args);
        return openfileactivity;
    }
}
