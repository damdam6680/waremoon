package com.example.waremoon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<byte[]> mUserPhotos;  // lista przechowująca dane zdjęć jako byte[]
    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public ImageAdapter(Context context) {
        mContext = context;
        mUserPhotos = getUserPhotosFromDatabase();
    }

    public List<byte[]> getUserPhotosFromDatabase() {
        int userId = new SessionManager(mContext).getUserId();
        return new DBHandler(mContext).getUserImages(userId);
    }

    @Override
    public int getCount() {
        return mUserPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // Wczytaj obraz z danych binarnych z bazy danych
        byte[] imageData = mUserPhotos.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        imageView.setImageBitmap(bitmap);

        imageView.setRotation(90);

        // Dodaj obsługę zdarzenia kliknięcia
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });

        return imageView;
    }
}
