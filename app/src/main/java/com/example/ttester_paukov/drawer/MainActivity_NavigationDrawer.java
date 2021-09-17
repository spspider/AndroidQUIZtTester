package com.example.ttester_paukov.drawer;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.ttester_paukov.OpenFileActivity;
import com.example.ttester_paukov.R;
import com.example.ttester_paukov.Search_Fragment;
import com.example.ttester_paukov.SettingsFragment;
import com.example.ttester_paukov.dynamicviewpager.LogFragment;

import java.util.ArrayList;


public class MainActivity_NavigationDrawer extends AppCompatActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    public static ArrayList<Fragment> fragments_Navi_drawer = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        //Activity activity = this.getCallingActivity();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();

        Class fragmentClass = null;
        switch (position){
            case 0:
                fragmentClass = OpenFileActivity.class;
                //Intent intent = new Intent(this, OpenFileActivity.class);
                //startActivity(intent);
                makeFragment(fragmentClass);
                break;
            case 1:
                //Intent intent = new Intent(this,MainActivity_ViewPager_quiz.class);
                //startActivity(intent);
                fragmentClass = Search_Fragment.class;
                makeFragment(fragmentClass);
                //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                //Intent intent = new Intent(this,MainActivity_ViewPager_quiz.class);
                //startActivity(intent);
                fragmentClass = SettingsFragment.class;
                makeFragment(fragmentClass);
                break;
            case 3://лог

                fragmentClass = LogFragment.class;
                makeFragment(fragmentClass);
                break;
            case 4://выход
                System.exit(0);
                //fragmentClass = LogFragment.class;
                //makeFragment(fragmentClass);
                break;
        }

    }

    public void makeFragment(final Class fragmentClass) {

        Fragment fragment = null;
        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            //Log.d(LOG_TAG,"BackStack"+fragmentClass.getName());
            //fragments_Navi_drawer.add(fragment);
            fragmentManager.beginTransaction().replace(R.id.container, fragment, fragmentClass.getName()).addToBackStack(fragmentClass.getName()).commit();
            //fragmentManager.notifyAll();
            //Log.d()
        }





        //return fragment;
    }
    int counter=0;
    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        //else
        //super.onBackPressed();





        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        if (fragmentManager.getBackStackEntryCount() > 0) {

            String lastFragmentName = fragmentManager.getBackStackEntryAt(
                    fragmentManager.getBackStackEntryCount() - 1).getName();
            Fragment currentFragment = fragmentManager.findFragmentByTag(lastFragmentName);
            if (currentFragment instanceof OnBackPressedListener) {
                ((OnBackPressedListener) currentFragment).doBack();
            }
            else {fragmentManager.popBackStack();}
        }

        //super.onBackPressed();
////////////////////////

        counter++;
        if(counter > 2){
            System.exit(0);
        }else{
            Toast.makeText(this, "еще раз для выхода", Toast.LENGTH_SHORT).show();
        }

        final long DELAY_TIME = 3000L;
        new Thread(new Runnable() {
            public void run(){
                try {
                    Thread.sleep(DELAY_TIME);
                    counter = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        ////////////////////////
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem searchItem=null; //= menu.findItem(R.id.action_search);

            if (searchItem != null) {
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

                // use this method for search process
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // use this method when query submitted
                        Toast.makeText(MainActivity_NavigationDrawer.this, query, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Class fragmentClass = null;
                        Fragment fragment=null;
                        // use this method for auto complete search process
                        //create
                        if (newText.length()>1) {
                            fragmentClass = Search_Fragment.class;
                            //makeFragment(getBaseContext(), fragmentClass);
                        }
                        else{

                        }
                        return false;
                    }
                });

            }
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }


}
