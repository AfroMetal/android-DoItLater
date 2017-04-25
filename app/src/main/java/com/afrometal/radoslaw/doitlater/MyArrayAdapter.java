package com.afrometal.radoslaw.doitlater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] titles;
    private final String[] shorts;
    private LayoutInflater inflater;

    public MyArrayAdapter(Context context, String[] titles, String[] shorts) {
        super(context, -1, titles);
        this.context = context;
        this.titles = titles;
        this.shorts = shorts;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView shortView = (TextView) rowView.findViewById(R.id.secondLine);

        textView.setText(titles[position]);
        shortView.setText(shorts[position]);

        return rowView;
    }
}