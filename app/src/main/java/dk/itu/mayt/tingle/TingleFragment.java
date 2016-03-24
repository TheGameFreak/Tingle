package dk.itu.mayt.tingle;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by May Ji on 26-02-2016.
 */
public class TingleFragment extends Fragment {


    // GUI variables
    private Button addThing, listAllThings;
    private TextView lastAdded;
    private TextView newWhat, newWhere;
    //private ListView listView;


    private static ThingsDB thingsDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_tingle, container, false);

        thingsDB= ThingsDB.get(getActivity());


        addThing = (Button) v.findViewById(R.id.add_button);
        listAllThings = (Button) v.findViewById(R.id.list_all);

        lastAdded= (TextView) v.findViewById(R.id.last_thing);
        updateUI();

        //listView = (ListView) v.findViewById(R.id.thing_list_view);
        newWhat= (TextView) v.findViewById(R.id.what_text);
        newWhere= (TextView) v.findViewById(R.id.where_text);

        newWhere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String whatAdd = newWhat.getText().toString();
                        String whereAdd = newWhere.getText().toString();
                        tryAdd(whatAdd, whereAdd);

                        InputMethodManager inputManager =
                                (InputMethodManager) getActivity().
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        return true;
                    default:
                        return false;
                }
            }
        });

        // view products click event
        addThing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
            String whatAdd = newWhat.getText().toString();
            String whereAdd = newWhere.getText().toString();
            tryAdd(whatAdd, whereAdd);

            }});




        listAllThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    //if not start new activity then what????
                    Intent i = new Intent(getActivity(), ListActivity.class);
                    startActivity(i);
                    return;
                }
                else
                {
                    TingleFragmentList fragmentList = new TingleFragmentList();
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container_list, fragmentList).addToBackStack(null).commit();
                    return;
                }
            }
        });


        return v;
    }

    private void tryAdd(String whatAdd, String whereAdd)
    {
        if ((whatAdd.length() > 0) && (whereAdd.length() > 0
        )) {
            thingsDB.addThing(
                    new Thing(whatAdd,
                            whereAdd));
            newWhat.setText("");
            newWhere.setText("");

            //used when updating fragment
            TingleFragmentList fragmentList = (TingleFragmentList) getFragmentManager().findFragmentById(R.id.fragment_container_list);
            if(fragmentList != null)
                fragmentList.updateListView();

            updateUI();

        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    }
    // This method fill a few things into ThingsDB for testing
    //currently not used.
    private void fillThingsDB() {
        thingsDB.addThing(new Thing("Android Phone", "Desk"));
        thingsDB.addThing(new Thing("Big Nerd book", "Desk"));
    }
    private void updateUI(){
        int s= thingsDB.size();

        if (s>0) {
            lastAdded.setText(thingsDB.get(s-1).toString());
        }
    }



}
