package com.example.waremoon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.waremoon.R;
import com.example.waremoon.sql.DBHandler;
import com.example.waremoon.interfaces.OnItemClickListener;
import com.example.waremoon.handler.SessionManagerHandler;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private List<byte[]> mUserPhotos;
    private List<Integer> mImageIds;
    private OnItemClickListener mItemClickListener;
    LayoutInflater layoutInflater;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public ImageAdapter(Context context) {
        mContext = context;
        mUserPhotos = getUserPhotosFromDatabase();
        mImageIds = getImageIdsFromDatabase();
    }

    public List<byte[]> getUserPhotosFromDatabase() {
        int userId = new SessionManagerHandler(mContext).getUserId();
        DBHandler dbHandler = new DBHandler(mContext);
        List<byte[]> userPhotos = dbHandler.getUserImages(userId);
        dbHandler.close();
        return userPhotos;
    }

    public List<Integer> getImageIdsFromDatabase() {
        int userId = new SessionManagerHandler(mContext).getUserId();
        DBHandler dbHandler = new DBHandler(mContext);
        List<Integer> imageIds = dbHandler.getImageIds(userId);
        dbHandler.close();
        return imageIds;
    }

    @Override
    public int getCount() {
        return mUserPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mImageIds.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridItemView = inflater.inflate(R.layout.grid_item, null);
        } else {
            gridItemView = convertView;
        }

        ImageView imageView = gridItemView.findViewById(R.id.gridImage);

        byte[] imageData = mUserPhotos.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        imageView.setImageBitmap(bitmap);

        imageView.setRotation(90);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position, mImageIds.get(position));
                }
            }
        });

        return gridItemView;
    }

    public void updateUserPhotos() {
        mUserPhotos = getUserPhotosFromDatabase();
        mImageIds = getImageIdsFromDatabase();
    }
}