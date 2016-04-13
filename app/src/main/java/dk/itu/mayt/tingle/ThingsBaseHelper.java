package dk.itu.mayt.tingle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by May Ji on 04-04-2016.
 */
public class ThingsBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ThingsBaseHelper";
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "thingBase.db";

    public ThingsBaseHelper (Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //count will be used for sorting algorithm. maybe change to location if implement location through gps
        db.execSQL("CREATE TABLE " + ThingsDbSchema.ThingTable.NAME
                + " (" + " _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ThingsDbSchema.ThingTable.Cols.WHAT + " TEXT, "
                + ThingsDbSchema.ThingTable.Cols.WHERE + " TEXT, "
                + ThingsDbSchema.ThingTable.Cols.COUNT + " INTEGER)" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
