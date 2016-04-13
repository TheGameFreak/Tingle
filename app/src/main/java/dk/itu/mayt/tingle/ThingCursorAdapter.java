package dk.itu.mayt.tingle;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by May Ji on 12-04-2016.
 */
public class ThingCursorAdapter extends ResourceCursorAdapter{

    public ThingCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView what = (TextView) view.findViewById(R.id.thing_what);
        what.setText(cursor.getString(cursor.getColumnIndex(ThingsDbSchema.ThingTable.Cols.WHAT)));

        TextView where = (TextView) view.findViewById(R.id.thing_where);
        where.setText(cursor.getString(cursor.getColumnIndex(ThingsDbSchema.ThingTable.Cols.WHERE)));
    }

}
