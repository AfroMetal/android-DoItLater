package com.afrometal.radoslaw.doitlater;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by radoslaw on 30.04.17.
 */

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
        TextView dueView = (TextView) rowView.findViewById(R.id.due);

        ToDoListItem item = this.getItem(position);

        rowView.setTag(item.id);
        textView.setText(item.title);

        Long date = Long.parseLong(item.date);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        dateView.setText("created: " + sdf.format(calendar.getTime()));

        Long due = Long.parseLong(item.due);
        sdf = new SimpleDateFormat("dd MMM yyyy");
        calendar.setTimeInMillis(due);
        dueView.setText(sdf.format(calendar.getTime()));

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        if (System.currentTimeMillis() > due){
            textView.setTypeface(null, Typeface.BOLD);
            dueView.setTypeface(null, Typeface.BOLD);
            dueView.setTextColor(context.getResources().getColor(R.color.colorOver, null));
        } else if (calendar.getTimeInMillis() >= due) {
            dueView.setTextColor(context.getResources().getColor(R.color.colorOver, null));
        }

        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        setNotifyOnChange(false);
        super.sort(new Comparator<ToDoListItem>() {
            @Override
            public int compare(ToDoListItem item1, ToDoListItem item2) {
                return item1.due.compareTo(item2.due);
            }
        });
        super.notifyDataSetChanged();
    }

    public void editView(Long id, String title, String date, String due, int position) {
        this.remove(this.getItem(position));
        this.insert(new ToDoListItem(id, title, date, due), position);
    }

    public void addView(Long id, String title, String date, String due) {
        this.add(new ToDoListItem(id, title, date, due));
    }

    public void removeView(int position) {
        this.remove(this.getItem(position));
    }
}