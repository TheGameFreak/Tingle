package dk.itu.mayt.tingle;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by May Ji on 24-02-2016.
 */
public class ThingsDB {
    private static ThingsDB sThingsDB;
    //fake database
    private List<Thing> mThingsDB;
    public static ThingsDB get(Context context) {
        if (sThingsDB == null) {
            sThingsDB= new ThingsDB(context);
        }
        return sThingsDB;
    }
    public List<Thing> getThingsDB() {return mThingsDB; }
    public void addThing(Thing thing) { mThingsDB.add(thing); }
    public int size() {return mThingsDB.size(); }
    public Thing get(int i){ return mThingsDB.get(i); }
    // Fill database for testing purposes
    private ThingsDB(Context context) {
        mThingsDB= new ArrayList<Thing>();
        mThingsDB.add(new Thing("Android Phone", "Desk"));
        // add as many as you like
        mThingsDB.add(new Thing("Big Nerd book", "Desk"));
    }

    /*
     * This function assumes that the user wants to remove all registered instances of the item
     */
    public   void  deleteThing(String thingWhat) {
        List<Thing> toDelete = new ArrayList<Thing>();
        for(Thing thing : mThingsDB)
        {
            //System.out.println(thing.getWhat() + " vs. " + thingWhat);
            if(thing.getWhat().toLowerCase().equals(thingWhat.toLowerCase()))
            {
                toDelete.add(thing);
                //break;
            }
        }
        mThingsDB.removeAll(toDelete);
    }

    }
