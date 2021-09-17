package com.example.ttester_paukov.dynamicviewpager;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ttester_paukov.R;

import java.util.ArrayList;

public class MainActivity_ViewPager_quiz extends Fragment {

    private ViewPager mViewPager;
    ArrayList<Fragment> fragments = new ArrayList<>();
    static DynamicViewPagerAdapter adapter;
    View v=null;
    int ListId;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if (v==null) {
            v = inflater.inflate(R.layout.activity_viewpager_quiz, container, false);
            mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
            adapter = new DynamicViewPagerAdapter(getActivity().getSupportFragmentManager());
            mViewPager.setAdapter(adapter);


            fragments.add(Quiz_Fragment.newInstance(fragments.size(), ListId));
            fragments.add(Quiz_Fragment.newInstance(fragments.size(), ListId));
            fragments.add(Quiz_Fragment.newInstance(fragments.size(), ListId));
            fragments.add(Quiz_Fragment.newInstance(fragments.size(), ListId));
            fragments.add(Quiz_Fragment.newInstance(fragments.size(), ListId));
            fragments.add(Quiz_Fragment.newInstance(fragments.size(), ListId));
            adapter.setItems(fragments);

            mViewPager.setCurrentItem(fragments.size() - 1);
            //Log.d(LOG_TAG,"ViewCreated");
        }
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getArguments();


        if (extras != null) {
           ListId = extras.getInt("ThemeIndexList",-1);
            //Log.d(LOG_TAG,"ListId"+ListId);
            // and get whatever type user account id is
            //Log.d(OpenFileActivity.LOG_TAG,"ListId:"+ListId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        v.destroyDrawingCache();
        //adapter.notifyDataSetChanged();
        //adapter.re
        //v.all
        super.onDestroyView();
    }

    /*
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_viewpager_quiz);

                    mViewPager = (ViewPager) findViewById(R.id.viewpager);
                    DynamicViewPagerAdapter adapter = new DynamicViewPagerAdapter(getSupportFragmentManager());
                    mViewPager.setAdapter(adapter);


                    fragments.add(Quiz_Fragment.newInstance(fragments.size()));
                    fragments.add(Quiz_Fragment.newInstance(fragments.size()));
                    fragments.add(Quiz_Fragment.newInstance(fragments.size()));
                    adapter.setItems(fragments);

                    mViewPager.setCurrentItem(1);
                }
            /*
                @Override
                protected void onDestroy() {
                    super.onDestroy();
                    mViewPager = null;
                }

                @Override
                public boolean onCreateOptionsMenu(Menu menu) {
                    // Inflate the menu; this adds items to the action bar if it is present.
                    //getMenuInflater().inflate(R.menu.menu_fullscreen, menu);
                    return true;
                }
            */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        int itemNow = mViewPager.getCurrentItem();
        //noinspection SimplifiableIfStatement
        if (id == 0) {

            //ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(Quiz_Fragment.newInstance(fragments.size(),ListId));


            DynamicViewPagerAdapter adapter = new DynamicViewPagerAdapter(getActivity().getSupportFragmentManager());
            adapter.setItems(fragments);

            mViewPager.setAdapter(adapter);
            mViewPager.setCurrentItem(itemNow);

            return true;
        }


        return false;
    }
}
