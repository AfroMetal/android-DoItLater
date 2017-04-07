package com.afrometal.radoslaw.bicycleviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private LayoutInflater inflater;
    protected SharedPreferences data;

    public MyArrayAdapter(Context context, String[] values, SharedPreferences data) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.data = data;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView ratingView = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        String prefix = context.getResources().getString(R.string.ratingData);

        textView.setText(values[position]);
        float rating = data.getFloat(prefix+position, 0.0f);
        ratingView.setText(Float.toString(rating)); //TODO: set to proper rating
        ratingView.setTag(prefix+position);

        TypedArray imgs = context.getResources().obtainTypedArray(R.array.photos);

        InputStream is = context.getResources().openRawResource(imgs.getResourceId(position, 0));
        Bitmap mThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(is),128,128);

        imageView.setImageBitmap(mThumbnail);

        imgs.recycle();

        return rowView;
    }
}