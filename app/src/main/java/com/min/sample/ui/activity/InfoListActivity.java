package com.min.sample.ui.activity;

import android.os.Bundle;

import com.min.common.util.ToastUtils;
import com.min.common.widget.refresh.RefreshLoaderView;
import com.min.core.base.BaseActivity;
import com.min.core.bean.BaseBean;
import com.min.core.helper.RxRefreshLoader;
import com.min.sample.R;
import com.min.sample.data.model.InfoBean;
import com.min.sample.ui.adapter.InfoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class InfoListActivity extends BaseActivity {

    @BindView(R.id.rlv)
    RefreshLoaderView mRlv;

    private RxRefreshLoader<InfoBean> mRxRefreshLoader;
    private InfoAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_info_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mAdapter = new InfoAdapter(getContext());
        mRxRefreshLoader = new RxRefreshLoader(mRlv, mAdapter, true, new Func1<Integer, Observable<BaseBean<List<InfoBean>>>>() {
            @Override
            public Observable<BaseBean<List<InfoBean>>> call(Integer integer) {
//                return DataManager.getMobileService().getTransInfoList(mMerchantNo);
                return Observable.create(new Observable.OnSubscribe<BaseBean<List<InfoBean>>>() {
                    @Override
                    public void call(Subscriber<? super BaseBean<List<InfoBean>>> subscriber) {
                        List<InfoBean> dataList = new ArrayList<>();
                        if (integer < 2) {
                            for (int i = 0; i < 15; i++) {
                                dataList.add(new InfoBean("title-" + i));
                            }
                        } else {
                            for (int i = 0; i < 3; i++) {
                                dataList.add(new InfoBean("title-" + i));
                            }
                        }
                        BaseBean<List<InfoBean>> response = new BaseBean<>();
                        response.code = 10000;
                        response.data = dataList;
                        subscriber.onNext(response);
                        subscriber.onCompleted();
//                        throw new RuntimeException("数据请求失败");
                    }
                }).delay(5, TimeUnit.SECONDS);
            }
        });

        mAdapter.setOnItemClickLitener((v, p) -> ToastUtils.showShort("you click pos=" + p));

        mRxRefreshLoader.startLoad();
    }

    @Override
    protected void onDestroy() {
        if (mRxRefreshLoader != null) {
            mRxRefreshLoader.destroy();
        }
        super.onDestroy();
    }
}
