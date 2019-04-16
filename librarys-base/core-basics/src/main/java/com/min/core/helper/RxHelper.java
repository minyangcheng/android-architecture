package com.min.core.helper;

import com.min.common.util.GsonUtils;
import com.min.common.util.LogUtils;
import com.min.common.util.ToastUtils;
import com.min.core.CoreConstants;
import com.min.core.R;
import com.min.core.bean.BaseBean;
import com.min.core.exception.ServerApiException;

import java.net.SocketTimeoutException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxHelper {

    public static <T> Observable.Transformer<BaseBean<T>, T> handleServerResult() {
        return handleServerResult(true);
    }

    public static <T> Observable.Transformer<BaseBean<T>, T> handleServerResult_() {
        return handleServerResult(false);
    }

    private static <T> Observable.Transformer<BaseBean<T>, T> handleServerResult(final boolean isUIThread) {
        return new Observable.Transformer<BaseBean<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseBean<T>> tObservable) {
                if (isUIThread) {
                    tObservable = tObservable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                return tObservable.lift(new Observable.Operator<T, BaseBean<T>>() {
                    @Override
                    public Subscriber<? super BaseBean<T>> call(final Subscriber<? super T> subscriber) {
                        return new Subscriber<BaseBean<T>>() {
                            @Override
                            public void onCompleted() {
                                if (subscriber.isUnsubscribed()) {
                                    return;
                                }
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (subscriber.isUnsubscribed()) {
                                    return;
                                }
                                LogUtils.eTag(CoreConstants.HTTP_LOG, e);
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(BaseBean<T> tBaseBean) {
                                if (subscriber.isUnsubscribed()) {
                                    return;
                                }
                                LogUtils.dTag(CoreConstants.HTTP_LOG, GsonUtils.toPrettyJson(tBaseBean));  //http响应打印
                                if (tBaseBean.isSuccess()) {
                                    subscriber.onNext(tBaseBean.data);
                                } else if (tBaseBean.isSignOut()) {
                                    //退出登陆处理
                                } else {
                                    subscriber.onError(new ServerApiException(tBaseBean));
                                }
                            }

                        };
                    }
                });
            }
        };
    }

    public static <T> Observable.Transformer<T, T> io_main() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static void handlerError(Throwable throwable) {
        if (throwable == null) return;
        if (throwable instanceof ServerApiException) {
            ServerApiException exception = (ServerApiException) throwable;
            String mess = exception.getMessage();
            ToastUtils.showShort(mess);
        } else if (throwable instanceof SocketTimeoutException) {
            ToastUtils.showShort(R.string.http_request_timeout);
        } else {
            ToastUtils.showShort(R.string.http_request_error);
        }
    }

}
