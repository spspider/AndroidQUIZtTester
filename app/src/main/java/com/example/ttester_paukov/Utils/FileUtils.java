package com.example.ttester_paukov.Utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;

/**
 * Created by sp_1 on 01.04.2017.
 */

public class FileUtils {
    Activity activity;
    Context context;

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String readFromFile(Context context, String FileInput) {

        String ret = "";

        try {

            InputStream inputStream = context.openFileInput(FileInput);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }

    public static String getPath2(Uri uri) {
        String Path = null;
        Path = uri.getPath();
        //Log.d(LOG_TAG,uri.getPath());

        if (Path == null) return null;
        //String FirstPath=null;
        if (Path.contains("raw:")) {
            Path = Path.substring(Path.indexOf("raw:") + 4);
        } else if (Path.contains("primary:")) {
            String firstpath = "/storage/emulated/0/";
            Path = Path.substring(Path.indexOf("primary:") + 8);
            Path=firstpath.concat(Path);
        }
        return Path;
    }
    public static String getFileName(Uri uri, Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public String readFile(String fileName) {

        File file = new File(fileName);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    stringBuilder.append(System.lineSeparator());
                }
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            System.err.println("File : \"" + file.getAbsolutePath() + "\" Not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getColunmData(Context context, Uri uri, String selection, String[] selectarg) {
        String filepath = "";
        Cursor cursor = null;
        String colunm = "_data";
        String[] projection = {colunm};

        cursor = context.getContentResolver().query(uri, projection, selection, selectarg, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Log.e(LOG_TAG, " file path is " + cursor.getString(cursor.getColumnIndex(colunm)));
            filepath = cursor.getString(cursor.getColumnIndex(colunm));
        }
        if (cursor != null)
            cursor.close();
        return filepath;


    }

    public void readfile(File file) {
        // File file = new File(Environment.getExternalStorageDirectory(), "mytextfile.txt");
        StringBuilder builder = new StringBuilder();
        Log.e("main", "read start");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            br.close();
        } catch (Exception e) {
            Log.e("main", " error is " + e.toString());
        }
        Log.e("main", " read text is " + builder.toString());
    }

}