package com.example.waremoon;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private int[] mImageIds = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
    };

    public ImageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        // Zwraca liczbę elementów do wyświetlenia
        return mImageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mImageIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // Jeśli obiekt View nie jest ponownie używany, utwórz nowy obiekt ImageView
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300)); // Dostosuj rozmiar obrazu
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // Ustaw obraz dla danego indeksu 'position' za pomocą identyfikatora obrazu z tablicy
        imageView.setImageResource(mImageIds[position]);

        return imageView;
    }
}

