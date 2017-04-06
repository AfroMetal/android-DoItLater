package com.afrometal.radoslaw.bicycleviewer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public MyArrayAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.list_item, parent, false);
        }
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView details = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        textView.setText(values[position]);
        details.setText("Rating: ");

        TypedArray imgs = context.getResources().obtainTypedArray(R.array.photos);

        InputStream is = context.getResources().openRawResource(imgs.getResourceId(position, 0));
        Bitmap mThumbnail = scaleBitmap(BitmapFactory.decodeStream(is));

        imageView.setImageBitmap(mThumbnail);

        imgs.recycle();

        return rowView;
    }

    private static Bitmap scaleBitmap(Bitmap source) {
        int maxSize = source.getWidth() > source.getHeight() ? source.getWidth() : source.getHeight();
        return Bitmap.createScaledBitmap(source, source.getWidth() * 96 / maxSize, source.getHeight() * 96 / maxSize, true);
    }
}