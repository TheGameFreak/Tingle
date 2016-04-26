package dk.itu.mayt.tingle;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by May Ji & Micki on 11-04-2016.
 */
public class ThingCursorWrapper extends CursorWrapper {

    public ThingCursorWrapper (Cursor cursor)
    {
        super(cursor);
    }


    public Thing getThing()
    {
        String whatString = getString(getColumnIndex(ThingsDbSchema.ThingTable.Cols.WHAT));
        String whereString = getString(getColumnIndex(ThingsDbSchema.ThingTable.Cols.WHERE));
        int id = getInt(getColumnIndex("_id"));
        int count = getInt(getColumnIndex(ThingsDbSchema.ThingTable.Cols.COUNT));

        Thing thing = new Thing(null, null);
        thing.setWhat(whatString);
        thing.setWhere(whereString);
        thing.setId(id);
        thing.setCount(count);

        return thing;
    }
}
