package dk.itu.mayt.tingle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class TingleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tingle);

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
