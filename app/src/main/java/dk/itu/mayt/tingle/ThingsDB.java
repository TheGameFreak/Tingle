package dk.itu.mayt.tingle;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by May Ji on 24-02-2016.
 */
public class ThingsDB {
    private static ThingsDB sThingsDB;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    //fake database
    //private List<Thing> mThingsDB;

    public static ThingsDB get(Context context) {
        if (sThingsDB == null) {
            sThingsDB= new ThingsDB(context);
        }
        return sThingsDB;
    }
    public List<Thing> getThingsDB()
    {
        List<Thing> things = new ArrayList<>();

        ThingCursorWrapper cursor = queryThings(null, null);

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                things.add(cursor.getThing());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return things;
    }
    public void addThing(Thing thing)
    {
        ContentValues values = getContentValues(thing);
        Integer Id = (int) mDatabase.insert(ThingsDbSchema.ThingTable.NAME, null, values);
        thing.setId(Id);

    }

    //update will be used when updating click count..
    public void updateThing (Thing thing)
    {
        String idString = thing.getId().toString();
        ContentValues values = getContentValues(thing);

        //using _id to find item and then update it
        mDatabase.update(ThingsDbSchema.ThingTable.NAME, values,
                "_id = ?",
                new String[]{idString});
    }

    public List<Thing> searchThing (String what)
    {
        return searchThing("what = ?", new String[]{what});
    }


    public int size()
    {
        return getThingsDB().size();
        //return (int)DatabaseUtils.queryNumEntries(mDatabase, ThingsDbSchema.ThingTable.NAME);
    }


    public Thing get(Integer i)
    {
        ThingCursorWrapper cursor = queryThings("_id = ?",
                new String[]{i.toString()});

        Thing thing = null;//new Thing ("no item", "no item");

        try
        {
            cursor.moveToFirst();
            if(!cursor.isAfterLast())
                thing = cursor.getThing();
        }
        finally {
            cursor.close();
        }
        return thing;
    }


    // Fill database for testing purposes
    private ThingsDB(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ThingsBaseHelper(mContext).getWritableDatabase();

    }

    /*
     * This function assumes that the user wants to remove all registered instances of the item
     */
    public void  deleteThing(Thing thing) {

        mDatabase.delete(ThingsDbSchema.ThingTable.NAME, "_id = " + thing.getId() + "", null);

    }

    private static ContentValues getContentValues(Thing thing)
    {
        //TODO consider putting click count in Thing object
        ContentValues values = new ContentValues();
        values.put(ThingsDbSchema.ThingTable.Cols.WHAT, thing.getWhat());
        values.put(ThingsDbSchema.ThingTable.Cols.WHERE, thing.getWhere());
        values.put(ThingsDbSchema.ThingTable.Cols.COUNT, 0);    //TODO Check if can get value somehow

        return values;
    }

    private ThingCursorWrapper queryThings(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                ThingsDbSchema.ThingTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ThingCursorWrapper(cursor);
    }

    public Thing lastAdded()
    {
        Cursor _cursor = mDatabase.query(
                ThingsDbSchema.ThingTable.NAME,
                null,
                null,
                null,
                null,
                null,
                "_id DESC"
        );
        Thing thing = null;//new Thing ("no item", "no item");
        ThingCursorWrapper cursor = new ThingCursorWrapper(_cursor);

        try
        {
            cursor.moveToFirst();
            if(!cursor.isAfterLast())
                thing = cursor.getThing();
        }
        finally {
            cursor.close();
        }
        return thing;

    }

    public List<Thing> searchThing(String whereClause, String[] whereArgs)
    {
        List<Thing> result = new ArrayList<Thing>();

        Cursor _cursor = mDatabase.query(
                ThingsDbSchema.ThingTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                ThingsDbSchema.ThingTable.Cols.COUNT + " DESC"
        );
        ThingCursorWrapper cursor = new ThingCursorWrapper(_cursor);

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                result.add(cursor.getThing());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return result;

    }


}
