package com.example.ttester_paukov;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sp_1 on 03.04.2017.
 */

public class Info_screen extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // создание LinearLayout
        LinearLayout linLayout = new LinearLayout(this);
        // установим вертикальную ориентацию
        linLayout.setOrientation(LinearLayout.VERTICAL);
        // создаем LayoutParams
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // устанавливаем linLayout как корневой элемент экрана
        setContentView(linLayout, linLayoutParam);

        LayoutParams lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        WebView myBrowser= new WebView(this);
        //myBrowser.setId(0X100);
        myBrowser.setScrollContainer(false);
        myBrowser.setLayoutParams(linLayoutParam);
        myBrowser.loadUrl("file:///android_asset/html/info_screen.html");
        linLayout.addView(myBrowser);

        TextView tv = new TextView(this);
        tv.setText(R.string.InfoText);

        tv.setLayoutParams(lpView);
        linLayout.addView(tv);




    }
}
