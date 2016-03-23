package dk.itu.mayt.tingle;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TingleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tingle);

        /*
        int currentOrientation = getResources().getConfiguration().orientation;

        if(currentOrientation == Configuration.ORIENTATION_PORTRAIT)
        {
            TingleFragment fragment = new TingleFragment();
            fragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        else
        {
            TingleFragment fragment = new TingleFragment();
            fragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            TingleFragmentList fragmentList = new TingleFragmentList();
            fragmentList.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.fragment_container_list, fragmentList).commit();
        }

        */

        //could not get getSupportFragmentManager to work, got incompatible error
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);


        if(fragment == null)
        {
            fragment = new TingleFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        Fragment fragmentlist = fm.findFragmentById(R.id.fragment_container_list);

        if(fragmentlist == null)
        {
            fragmentlist = new TingleFragmentList();
            fm.beginTransaction().add(R.id.fragment_container_list,fragmentlist).commit();
        }


    }

}
