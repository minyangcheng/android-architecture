package com.min.core.helper;

import com.min.common.util.GsonUtils;
import com.min.common.util.LogUtils;
import com.min.common.util.ToastUtils;
import com.min.core.CoreConstants;
import com.min.core.R;
import com.min.core.bean.BaseBean;
import com.min.core.bean.CSBaseBean;
import com.min.core.exception.ServerApiException;

import java.net.SocketTimeoutException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CSRxHelper {

    public static <T> Observable.Transformer<CSBaseBean<T>, T> handleServerResult() {
        return handleServerResult(true);
    }

    public static <T> Observable.Transformer<CSBaseBean<T>, T> handleServerResult_() {
        return handleServerResult(false);
    }

    private static <T> Observable.Transformer<CSBaseBean<T>, T> handleServerResult(final boolean isUIThread) {
        return new Observable.Transformer<CSBaseBean<T>, T>() {
            @Override
            public Observable<T> call(Observable<CSBaseBean<T>> tObservable) {
                if (isUIThread) {
                    tObservable = tObservable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                return tObservable.lift(new Observable.Operator<T, CSBaseBean<T>>() {
                    @Override
                    public Subscriber<? super CSBaseBean<T>> call(final Subscriber<? super T> subscriber) {
                        return new Subscriber<CSBaseBean<T>>() {
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
                            public void onNext(CSBaseBean<T> tBaseBean) {
                                if (subscriber.isUnsubscribed()) {
                                    return;
                                }
                                LogUtils.dTag(CoreConstants.HTTP_LOG, GsonUtils.toPrettyJson(tBaseBean));  //http响应打印
                                if (tBaseBean.isSuccess()) {
                                    subscriber.onNext(tBaseBean.model);
                                } else if (tBaseBean.isSignOut()) {
                                    //退出登陆处理
                                } else {
                                    subscriber.onError(new ServerApiException(tBaseBean.resultCode,tBaseBean.message));
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
