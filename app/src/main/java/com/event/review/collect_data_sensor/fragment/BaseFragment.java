package com.event.review.collect_data_sensor.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class BaseFragment extends Fragment {
    private int resLayoutId;

    /**
     * Implement this here so that subclasses can avoid such boilerplate code
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /**
     * Should be called at onCreate()
     *
     * @param resLayoutId Used to create view for this fragment.
     *                    This layout id is the same as one inflated at onCreateView()
     */
    public void setLayout(@LayoutRes int resLayoutId) {
        this.resLayoutId = resLayoutId;
    }

    /**
     * Implement this here so that subclasses can avoid such boilerplate code
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(resLayoutId, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    /**
     * Implement this here so that subclasses can avoid such boilerplate code
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
