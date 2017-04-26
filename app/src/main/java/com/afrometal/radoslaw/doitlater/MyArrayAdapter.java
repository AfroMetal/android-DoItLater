package com.afrometal.radoslaw.doitlater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class MyArrayAdapter extends ArrayAdapter<ToDoListItem> {
    private final Context context;
    private LayoutInflater inflater;

    public MyArrayAdapter(Context context, ArrayList<ToDoListItem> items) {
        super(context, -1, items);
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView dateView = (TextView) rowView.findViewById(R.id.date);

        ToDoListItem item = this.getItem(position);

        rowView.setTag(item.id);
        textView.setText(item.title);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(item.date));
        dateView.setText(sdf.format(calendar.getTime()));

        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        setNotifyOnChange(false);
        super.sort(new Comparator<ToDoListItem>() {
            @Override
            public int compare(ToDoListItem item1, ToDoListItem item2) {
                return item1.date.compareTo(item2.date) * -1;
            }
        });
        super.notifyDataSetChanged();
    }

    public void editView(Long id, String title, String date, int position) {
        this.remove(this.getItem(position));
        this.insert(new ToDoListItem(id, title, date), position);
    }

    public void addView(Long id, String title, String date) {
        this.add(new ToDoListItem(id, title, date));
    }

    public void removeView(int position) {
        this.remove(this.getItem(position));
    }
}