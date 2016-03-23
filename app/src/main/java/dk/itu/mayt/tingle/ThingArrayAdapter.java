package dk.itu.mayt.tingle;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by May Ji on 24-02-2016.
 */
public class ThingArrayAdapter extends ArrayAdapter<Thing> {
    private final Context context;
    private final List<Thing> values;

    public ThingArrayAdapter(Context context, List<Thing> values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int i, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView whatView = (TextView) rowView.findViewById(R.id.thing_what);
        whatView.append(values.get(i).getWhat());
        TextView whereView = (TextView) rowView.findViewById(R.id.thing_where);
        whereView.append(values.get(i).getWhere());
        return rowView;
    }
}