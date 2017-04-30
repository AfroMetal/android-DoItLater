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
import java.util.concurrent.TimeUnit;

/**
 * Created by radoslaw on 30.04.17.
 */

public class MyArrayAdapter extends ArrayAdapter<ToDoListItem> {
    private static final int TYPE_EXPIRED = -1;
    private static final int TYPE_SOON = 0;
    private static final int TYPE_NORMAL = 1;

    private final Context context;
    private LayoutInflater inflater;

    public MyArrayAdapter(Context context, ArrayList<ToDoListItem> items) {
        super(context, -1, items);
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        Calendar dueCalendar = Calendar.getInstance();
        dueCalendar.setTimeInMillis(this.getItem(position).due);

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(System.currentTimeMillis());

        Calendar soonCalendar = Calendar.getInstance();
        soonCalendar.setTimeInMillis(System.currentTimeMillis());
        soonCalendar.add(Calendar.DAY_OF_MONTH, 7);

        if (dueCalendar.before(nowCalendar)){
            return TYPE_EXPIRED;
        } else if (dueCalendar.before(soonCalendar)) {
            return TYPE_SOON;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        int type = getItemViewType(position);

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
        Calendar dueCalendar = Calendar.getInstance();
        dueCalendar.setTimeInMillis(date);
        dateView.setText("created: " + sdf.format(dueCalendar.getTime()));

        sdf = new SimpleDateFormat("dd MMM yyyy");
        dueCalendar.setTimeInMillis(item.due);
        dueView.setText(sdf.format(dueCalendar.getTime()));

        switch (type) {
            case TYPE_EXPIRED:
                textView.setTypeface(null, Typeface.BOLD);
                dueView.setTypeface(null, Typeface.BOLD);
            case TYPE_SOON:
                dueView.setTextColor(context.getResources().getColor(R.color.colorOver, null));
                break;
        }

        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        setNotifyOnChange(false);
        super.sort(new Comparator<ToDoListItem>() {
            @Override
            public int compare(ToDoListItem item1, ToDoListItem item2) {
                Calendar item1Cal = Calendar.getInstance();
                item1Cal.setTimeInMillis(item1.due);

                Calendar item2Cal = Calendar.getInstance();
                item2Cal.setTimeInMillis(item2.due);

                return item1Cal.compareTo(item2Cal);
            }
        });
        super.notifyDataSetChanged();
    }

    public void editView(Long id, String title, String date, Long due, int position) {
        this.remove(this.getItem(position));
        this.insert(new ToDoListItem(id, title, date, due), position);
    }

    public void addView(Long id, String title, String date, Long due) {
        this.add(new ToDoListItem(id, title, date, due));
    }

    public void removeView(int position) {
        this.remove(this.getItem(position));
    }
}