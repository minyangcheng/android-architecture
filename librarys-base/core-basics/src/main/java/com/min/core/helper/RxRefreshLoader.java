package com.min.core.helper;

import com.blankj.utilcode.util.LogUtils;
import com.min.common.widget.recyclerview.BaseRecyclerViewAdapter;
import com.min.common.widget.refresh.RefreshLoaderDelegate;
import com.min.common.widget.refresh.RefreshLoaderView;
import com.min.core.bean.BaseBean;
import com.min.core.http.ResponseHandler;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by minyangcheng on 2016/7/29.
 */
public class RxRefreshLoader<DATA> extends RefreshLoaderDelegate<DATA> {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private Func1<Integer, Observable<BaseBean<List<DATA>>>> mFun1;

    public RxRefreshLoader(RefreshLoaderView refreshLoaderView
            , BaseRecyclerViewAdapter adapter
            , boolean pageEnable
            , Func1 func1) {
        super(refreshLoaderView, adapter, pageEnable);
        this.mFun1 = func1;
    }

    @Override
    protected void onRefreshData() {
        loadData(true, 0);
    }

    @Override
    protected void onLoadMoreData() {
        loadData(false, mNextPage);
    }

    protected void loadData(final boolean isRefresh, int page) {
        Subscription subscription = Observable.just(page)
                .flatMap(mFun1)
                .compose(ResponseHandler.<List<DATA>>handleServerResult())
                .subscribe(new Action1<List<DATA>>() {
                    @Override
                    public void call(List<DATA> dataList) {
                        if (mIsDestory) {
                            return;
                        }
                        if (isRefresh) {
                            setRefreshDataSuccess(dataList);
                        } else {
                            setLoadMoreDataSuccess(dataList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable);
                        if (mIsDestory) {
                            return;
                        }
                        if (isRefresh) {
                            setRefreshDataFail(throwable);
                        } else {
                            setLoadLoadMoreFail(throwable);
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
