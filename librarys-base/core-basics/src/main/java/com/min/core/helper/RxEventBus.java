package com.min.core.helper;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * A simple event bus built with RxJava
 *
 * RxEventBus.getInstance().filteredObservable(AppEvent.class)
 *                 .filter(o -> o.filter(AppEvent.ChangeEventType.CONSUME_UPDO))
 *                 .compose(bindUntilEvent(ActivityEvent.DESTROY))
 *                 .subscribe(o -> {
 *                     mRxRefreshLoader.manualRefresh();
 *                 }, e -> {
 *                 }, () -> {
 *                 });
 *
 */
public class RxEventBus {

    public static RxEventBus rxEventBus = new RxEventBus();

    private final PublishSubject<Object> mBusSubject;

    public static RxEventBus getInstance() {
        return rxEventBus;
    }

    private RxEventBus() {
        mBusSubject = PublishSubject.create();
    }

    public void post(Object event) {
        mBusSubject.onNext(event);
    }

    public Observable<Object> observable() {
        return mBusSubject;
    }

    public <T> Observable<T> filteredObservable(final Class<T> eventClass) {
        return mBusSubject.ofType(eventClass);
    }
}