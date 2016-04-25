package dk.itu.mayt.tingle;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
{
    private Menu mMenu;
    private static ThingsDB thingsDB;
    //make this searchable activity?  http://developer.android.com/guide/topics/search/search-dialog.html

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
                adb.setMessage("Do you want to delete " + thingToRemove.getWhat() + "?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                    //does this execute??
                    public void onClick(DialogInterface dialog, int which) {
                        thingsDB.deleteThing(thingToRemove);
                        //listAdapter.setValues(thingsDB.getThingsDB());
                        //System.out.println("listAdapter stuff: " + listAdapter.getValues());
                        listAdapter.clear();
                        listAdapter.addAll(thingsDB.getThingsDB()); //have to manually add all because notifyDataSetChanged does not work as before

                        TingleFragment fragment = (TingleFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                        if (fragment != null)
                            fragment.itemDeleted();


                        listAdapter.notifyDataSetChanged();
                    }
                });
                adb.show();
            }
        });

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }


    @Override
    public void onBackPressed()
    {
        Thread.currentThread().interrupt();
        super.onBackPressed();
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Return true to allow the action view to expand
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // When the action view is collapsed, reset the query
                        ListView lv = ((ListView) findViewById(R.id.thing_list_view));
                        ThingArrayAdapter listAdapter = (ThingArrayAdapter)lv.getAdapter();
                        listAdapter.clear();
                        listAdapter.addAll(thingsDB.getThingsDB());
                        // Return true to allow the action view to collapse
                        return true;
                    }
                });

        SearchView searchView = (SearchView) searchItem.getActionView();

        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

    }

    private void doMySearch(String query)
    {
        ListView lv = ((ListView) findViewById(R.id.thing_list_view));
        ThingArrayAdapter listAdapter = (ThingArrayAdapter)lv.getAdapter();

        List<Thing> things = thingsDB.searchThing(query);

        listAdapter.clear();
        listAdapter.addAll(things); //have to manually add all because notifyDataSetChanged does not work as before

    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                ListView lv = ((ListView) findViewById(R.id.thing_list_view));
                ThingArrayAdapter listAdapter = (ThingArrayAdapter)lv.getAdapter();
                listAdapter.clear();
                listAdapter.addAll(thingsDB.getThingsDB());
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onSearchRequested() {
            MenuItem mi = mMenu.findItem(R.id.search);
            if(mi.isActionViewExpanded()){
                mi.collapseActionView();
            } else{
                mi.expandActionView();
            }

        return super.onSearchRequested();
    }*/




    /*
    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }
*/


}
