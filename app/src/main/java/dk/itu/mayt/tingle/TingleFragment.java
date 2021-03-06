package dk.itu.mayt.tingle;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by May Ji & Micki on 26-02-2016.
 */
public class TingleFragment extends Fragment {

    // GUI variables
    private Button addThing, listAllThings, searchThings;
    private ImageButton barcodeCamera;
    private TextView lastAdded;
    private TextView newWhat, newWhere, searchWhat;


    private static ThingsDB thingsDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_tingle, container, false);

        thingsDB= ThingsDB.get(getActivity());


        addThing = (Button) v.findViewById(R.id.add_button);
        listAllThings = (Button) v.findViewById(R.id.list_all);
        searchThings = (Button) v.findViewById(R.id.search);

        barcodeCamera = (ImageButton) v.findViewById(R.id.barcode_camera);

        lastAdded= (TextView) v.findViewById(R.id.last_thing);
        updateUI();

        newWhat= (TextView) v.findViewById(R.id.what_text);
        newWhere= (TextView) v.findViewById(R.id.where_text);
        newWhere.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchWhat = (TextView) v.findViewById(R.id.search_text);

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

        try {
            barcodeCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                    startActivityForResult(intent, 0);
                }
            });
        }
        catch (ActivityNotFoundException anfe)
        {
            Log.e("onCreate", "Scanner Not Found", anfe);
        }


        listAllThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    new Thread(new Runnable()
                    {
                        public void run ()
                        {
                            Intent i = new Intent(getActivity(), ListActivity.class);
                            startActivity(i);
                        }
                    }).start();

                }
                else
                {
                    TingleFragmentList fragmentList = new TingleFragmentList();
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container_list, fragmentList).addToBackStack(null).commit();
                }
            }
        });


        searchWhat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        String whatSearch = searchWhat.getText().toString();
                        List<Thing> result = thingsDB.searchThing(whatSearch);

                        TingleFragmentList fragmentList = (TingleFragmentList) getFragmentManager().findFragmentById(R.id.fragment_container_list);
                        if (fragmentList != null) {
                            fragmentList.searchListView(result);
                        }

                        InputMethodManager inputManager =
                                (InputMethodManager) getActivity().
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        searchWhat.setText("");
                        return true;
                    default:
                        return false;
                }
            }
        });

        searchThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String whatSearch = searchWhat.getText().toString();
                searchWhat.setText("");
                List<Thing> result = thingsDB.searchThing(whatSearch);

                TingleFragmentList fragmentList = (TingleFragmentList) getFragmentManager().findFragmentById(R.id.fragment_container_list);
                if (fragmentList != null) {
                    fragmentList.searchListView(result);
                }

            }
        });




        return v;
    }



    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

// Handle successful scan, go on the internet, fetch data and present.
                new FetchOutpanTask().execute(contents);

            } else if (resultCode == getActivity().RESULT_CANCELED) {
// Handle cancel
                Toast toast = Toast.makeText(getActivity(), "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        }
    }




    private void tryAdd(String whatAdd, String whereAdd)
    {
        if ((whatAdd.length() > 0) && (whereAdd.length() > 0))
        {
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
        Thing thing = thingsDB.lastAdded();
        if(thing == null)
            lastAdded.setText("No items added");
        else
            lastAdded.setText(thing.toString());

    }

    public void itemDeleted()
    {
        updateUI();
    }


    @Override
    public void onPause()
    {
        super.onPause();

    }




    private class FetchOutpanTask extends AsyncTask<String, Void, byte[] > {
        private static final String API_KEY = "cb4b195f4ae363f9f610b4b6eae3e3d2";
        private static final String TAG = "FetchOutpanTask";

        @Override
        protected byte[] doInBackground(String... params) {
            byte[] result = null;
            String item = params[0];
            try
            {
                result = new NetworkFetcher()
                        .getProductInfo("https://api.outpan.com/v2/products/"+item+"/?apikey="+API_KEY);
            }

            catch (IOException ioe)
            {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
            }
            return result;
        }
        @Override
        protected void onPostExecute(byte[] result) {
            String foo = new String(result);
            try {
                JSONObject json = new JSONObject(foo);
                String nameOfItem = json.getString("name");

                newWhat.setText(nameOfItem);

            }
            catch (JSONException je)
            {
                Log.e(TAG, "Failed to parse JSON", je);
            }
            catch (Exception e)
            {
                Log.e(TAG, "Some error happened", e);

            }

        }
    }




}
