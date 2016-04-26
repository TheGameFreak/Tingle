package dk.itu.mayt.tingle;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.List;

/**
 * Created by May Ji & Micki on 28-02-2016.
 */
public class TingleFragmentList extends ListFragment {


    private ThingArrayAdapter listAdapter;


    private static ThingsDB thingsDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        thingsDB= ThingsDB.get(getActivity());


        listAdapter = new ThingArrayAdapter(getActivity(), thingsDB.getThingsDB());


        setListAdapter(listAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Delete?");


        final Thing thingToRemove = listAdapter.getItem(position);
        thingToRemove.increaseCount();
        thingsDB.updateThing(thingToRemove);
        adb.setMessage("Do you want to delete " + thingToRemove.getWhat() + "?");
        adb.setNegativeButton("Cancel", null);
        adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                thingsDB.deleteThing(thingToRemove);
                listAdapter.clear();
                listAdapter.addAll(thingsDB.getThingsDB()); //have to manually add all because notifyDataSetChanged does not work as before

                TingleFragment fragment = (TingleFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                if(fragment != null)
                    fragment.itemDeleted();

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
        {
            listAdapter.clear();
            listAdapter.addAll(thingsDB.getThingsDB());

            listAdapter.notifyDataSetChanged();
        }

    }

    public void searchListView(List<Thing> things)
    {
        if(listAdapter != null)
        {
            listAdapter.clear();
            listAdapter.addAll(things);

            listAdapter.notifyDataSetChanged();
        }
    }
}
