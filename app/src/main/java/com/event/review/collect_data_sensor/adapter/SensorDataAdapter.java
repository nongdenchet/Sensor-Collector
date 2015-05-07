package com.event.review.collect_data_sensor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.event.review.R;
import com.event.review.collect_data_sensor.common.ViewHolder;
import com.event.review.collect_data_sensor.model.SensorCollection;
import com.event.review.collect_data_sensor.util.DateUtils;
import com.event.review.collect_data_sensor.util.ExternalUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by nongdenchet on 3/22/15.
 */
public class SensorDataAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<SensorCollection> collections;
    private List<SensorCollection> originCollections;
    private final int TITLE = 0;
    private final int ROW = 1;

    public SensorDataAdapter(Context context, List<SensorCollection> collections) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.collections = collections;
        originCollections = collections;
    }

    public List<SensorCollection> getCollections() {
        return collections;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TITLE;
        return ROW;
    }

    public void addCollection(SensorCollection collection) {
        originCollections.add(collection);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return collections.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position != 0)
            return collections.get(position - 1);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            switch (getItemViewType(position)) {
                case TITLE:
                    row = mInflater.inflate(R.layout.title_layout, parent, false);
                    break;
                case ROW:
                    row = mInflater.inflate(R.layout.item_data_sensor, parent, false);
                    break;
            }
        }

        // add data
        switch (getItemViewType(position)) {
            case TITLE:
                fillDataForTitle(row);
                break;
            case ROW:
                fillDataForItem(row, position - 1);
                break;
        }
        return row;
    }

    public void removeCollection(int pos) {
        final SensorCollection c = collections.get(pos);
        removeDataSetTask(c.getName())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        notifyDataSetChanged();
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean value) {
                        if (value) {
                            collections.remove(c);
                            originCollections.remove(c);
                        } else {
                            // Toast
                        }
                    }
                });
    }

    public Observable<Boolean> removeDataSetTask(final String name) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(ExternalUtils.removeFile(mContext, name));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void fillDataForTitle(View row) {
        row.setClickable(false);
        row.setEnabled(false);
        row.setOnClickListener(null);
    }

    private void fillDataForItem(View row, int position) {
        SensorCollection collection = collections.get(position);
        TextView title = ViewHolder.get(row, R.id.title);
        title.setText(collection.getType().toString());
        TextView subTitle = ViewHolder.get(row, R.id.sub_title);
        subTitle.setText(DateUtils.getSensorDate(collection.getTimestamp(), false));
    }

    // Filter with type
    public void filterCollections(SensorCollection.TRANSPORT type) {
        if (type == SensorCollection.TRANSPORT.NONE)
            collections = originCollections;
        else {
            collections = new ArrayList<>();
            for (int i = 0; i < originCollections.size(); i++) {
                SensorCollection data = originCollections.get(i);
                if (data.getType().equals(type)) {
                    collections.add(data);
                }
            }
        }
        notifyDataSetChanged();
    }
}
