package com.event.review.collect_data_sensor.common;

import android.util.SparseArray;
import android.view.View;

/**
 * <a href="http://www.piwai.info/android-adapter-good-practices/">Source</a>
 */
public class ViewHolder {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View parent, int childViewId) {
        SparseArray<View> viewHolder = (SparseArray<View>) parent.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            parent.setTag(viewHolder);
        }

        View childView = viewHolder.get(childViewId);
        if (childView == null) {
            childView = parent.findViewById(childViewId);
            viewHolder.put(childViewId, childView);
        }

        return (T) childView;
    }
}
