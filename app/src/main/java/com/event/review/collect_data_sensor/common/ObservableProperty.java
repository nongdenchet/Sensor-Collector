package com.event.review.collect_data_sensor.common;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by nongdenchet on 4/30/15.
 */
public class ObservableProperty<T> implements Action1<T> {
    protected final BehaviorSubject<T> valueSubject;
    private T value;

    public ObservableProperty() {
        valueSubject = BehaviorSubject.create();
    }

    public ObservableProperty(T defaultValue) {
        this.value = defaultValue;
        valueSubject = BehaviorSubject.create(defaultValue);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        valueSubject.onNext(value);
    }

    public boolean hasValue() {
        return value != null;
    }

    public Observable<T> observe() {
        return valueSubject;
    }

    @Override
    public void call(T value) {
        setValue(value);
    }
}
