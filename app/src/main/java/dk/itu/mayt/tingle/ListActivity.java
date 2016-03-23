package dk.itu.mayt.tingle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {

    /*
    * OBS!!!! updateUI needs to be called somehow, probably need to implement similar function, otherwise last added will be wrong!
    * */
    private static ThingsDB thingsDB;

    @Override
    protected void  onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        thingsDB= ThingsDB.get(this);

        setContentView(R.layout.listall_activity_tingle);

        final ThingArrayAdapter listAdapter = new ThingArrayAdapter(this, thingsDB.getThingsDB());
        ListView lv = ((ListView) findViewById(R.id.thing_list_view));
        lv.setAdapter(listAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(ListActivity.this);
                adb.setTitle("Delete?");

                final String thingToRemove = thingsDB.get(position).getWhat();
                adb.setMessage("Do you want to delete this item?");
                adb.setNegativeButton("Cancel",null);
                adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                    //does this execute??
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("deleted " + thingToRemove);
                        thingsDB.deleteThing(thingToRemove);
                        listAdapter.notifyDataSetChanged();
                    }
                });
                adb.show();
            }
        });

    }



}
