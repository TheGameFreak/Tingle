package dk.itu.mayt.tingle;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by May Ji on 28-02-2016.
 */
public class TingleFragmentList extends ListFragment {

/*
    * OBS!!!! updateUI needs to be called somehow, probably need to implement similar function, otherwise last added will be wrong!
    * */

    private ThingArrayAdapter listAdapter;


    private static ThingsDB thingsDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //super.onCreateView(inflater, container, savedInstanceState);

        //View v = inflater.inflate(R.layout.list_fragment_tingle, container, false);
        //listView = (ListView)v.findViewById(R.id.thing_list_view);

        thingsDB= ThingsDB.get(getActivity());


        listAdapter = new ThingArrayAdapter(getActivity(), thingsDB.getThingsDB());


        setListAdapter(listAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);

        //return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Delete?");

        final String thingToRemove = thingsDB.get(position).getWhat();
        adb.setMessage("Do you want to delete this item?");
        adb.setNegativeButton("Cancel",null);
        adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                thingsDB.deleteThing(thingToRemove);
                listAdapter.notifyDataSetChanged();
            }
        });
        adb.show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }


    public void updateListView()
    {
        if(listAdapter != null)
            listAdapter.notifyDataSetChanged();
    }
}
