package com.event.review.review_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.event.review.R;

/**
 * Created by nongdenchet on 3/19/15.
 */
public class ItemAdapter extends BaseAdapter {
    private Context mConText;
    private LayoutInflater inflater;
    private int mSize;
    private final String TAG = "Adapter";

    public ItemAdapter(int size, Context context) {
        mSize = size;
        mConText = context;
        inflater = LayoutInflater.from(mConText);
    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "type: " + position % 3);
        return (position % 3);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // log out information
        Log.d(TAG, "position: " + position);

        // inflate row
        View row = convertView;
        if (row == null) {
            switch (getItemViewType(position)) {
                case 0:
                    row = inflater.inflate(R.layout.item_type_1, parent, false);
                    break;
                case 1:
                    row = inflater.inflate(R.layout.item_type_2, parent, false);
                    break;
                case 2:
                    row = inflater.inflate(R.layout.item_type_3, parent, false);
                    break;
            }
        }
        return row;
    }
}
