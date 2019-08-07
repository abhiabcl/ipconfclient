package com.flavor.app.ipconfapp.att;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import static com.flavor.app.ipconfapp.att.SettingFragment.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    final Fragment fragment1 = new RoomFragment();
    final Fragment fragment2 = new DialOutFragment();
    final Fragment fragment3 = new SettingFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    String TAG="IPCONF";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    Log.i(TAG, "home clicked!");
                    fragment = new RoomFragment();
                    loadFragment(fragment);
                    break;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    Log.i(TAG, "dialout clicked!");
                    fragment = new DialOutFragment();
                    loadFragment(fragment);
                    break;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    Log.i(TAG, "setting clicked!");
                    fragment = new SettingFragment();
                    loadFragment(fragment);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new RoomFragment());

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if (prefs != null) {
            String server_url = prefs.getString("SERVER_URL", "");//"No name defined" is the default value.
            RestAPIHandler.setCreateConfURL("http://"+server_url + RestAPIHandler.createConfAPI);
            RestAPIHandler.setDialoutURL("http://"+server_url + RestAPIHandler.DialoutAPI);
            RestAPIHandler.setTermiConfURL("http://"+server_url + RestAPIHandler.TerminAPI);
            Log.i(TAG, "getting data from pref:"+ server_url);

        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }
}
