package com.min.core.http;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.min.core.CoreConstants;
import com.min.core.R;
import com.min.core.bean.BaseBean;
import com.min.core.exception.ServerApiException;
import com.min.core.util.GsonUtil;

import java.net.SocketTimeoutException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ResponseHandler {

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
                                //http响应打印
                                LogUtils.dTag(CoreConstants.HTTP_LOG, e);
                                if (e instanceof SocketTimeoutException) {
                                    subscriber.onError(new ServerApiException(-1, e.getMessage()));
                                } else {
                                    subscriber.onError(new ServerApiException(-2, e.getMessage()));
                                }
                            }

                            @Override
                            public void onNext(BaseBean<T> tBaseBean) {
                                if (subscriber.isUnsubscribed()) {
                                    return;
                                }
                                //http响应打印
                                LogUtils.dTag(CoreConstants.HTTP_LOG, GsonUtil.toPrettyJson(tBaseBean));
                                if (tBaseBean.isSuccess()) {
                                    subscriber.onNext(tBaseBean.data);
                                } else if (tBaseBean.isSignOut()) {

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

    public static void handlerError(Context context, Throwable throwable) {
        if (context == null || throwable == null) return;
        if (throwable instanceof ServerApiException) {
            ServerApiException exception = (ServerApiException) throwable;
            int code = exception.getCode();
            if (code < 0) {
                //异常
                if (code == -1) {
                    ToastUtils.showShort(R.string.http_request_timeout);
                } else {
                    ToastUtils.showShort(R.string.http_request_error);
                }
            } else {
                //服务器返回错误
                String mess = exception.getMessage();
                ToastUtils.showShort(mess);
            }
        }
    }

}
