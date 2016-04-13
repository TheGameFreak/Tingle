package dk.itu.mayt.tingle;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static ThingsDB thingsDB;
    //ThingArrayAdapter listAdapter;

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

                final Thing thingToRemove = listAdapter.getItem(position);
                adb.setMessage("Do you want to delete "+ thingToRemove.getWhat() + "?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                    //does this execute??
                    public void onClick(DialogInterface dialog, int which) {
                        thingsDB.deleteThing(thingToRemove);
                        //listAdapter.setValues(thingsDB.getThingsDB());
                        //System.out.println("listAdapter stuff: " + listAdapter.getValues());
                        listAdapter.clear();
                        listAdapter.addAll(thingsDB.getThingsDB()); //have to manually add all because notifyDataSetChanged does not work as before

                        //TODO does not work, the fragment returned is null!
                        TingleFragment fragment = (TingleFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                        if(fragment != null)
                            fragment.itemDeleted();


                        listAdapter.notifyDataSetChanged();
                    }
                });
                adb.show();
            }
        });

    }


}
