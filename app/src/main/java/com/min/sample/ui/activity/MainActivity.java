package com.min.sample.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.min.common.widget.TitleBar;
import com.min.core.base.BaseActivity;
import com.min.core.base.BaseDialog;
import com.min.core.base.BasePopupWindow;
import com.min.core.helper.GsonHelper;
import com.min.router.GlobalRouter;
import com.min.sample.R;
import com.min.sample.data.local.db.delegate.SearchDaoDelegate;
import com.min.sample.data.model.SearchBean;
import com.min.sample.ui.dialog.ExampleDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mTitleBar.setTitle("架构示例");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.btn_order)
    void clickOpenOrder() {
        GlobalRouter.getInstance()
                .navigation("cg://native.com/order/OrderMainPage")
                .appendQueryParameter("company_name", "cheguo")
                .appendQueryParameter("user_id", "minych")
                .putExtra("isLogin", true)
                .start();
    }

    @OnClick(R.id.btn_weex)
    void clickOpenWeex() {
        GlobalRouter.getInstance()
                .navigation("cg://weex.com/page/SamplePage")
                .appendQueryParameter("company_name", "cheguo")
                .appendQueryParameter("user_id", "minych")
                .putExtra("isLogin", true)
                .start();
    }

    @OnClick(R.id.btn_web)
    void clickOpenWeb() {
        GlobalRouter.getInstance()
                .navigation("cg://h5.com")
                .appendQueryParameter("company_name", "cheguo")
                .appendQueryParameter("host", "www.baidu.com")
                .appendQueryParameter("scheme", "http")
                .start();
    }

    @OnClick(R.id.btn_popwin)
    void clickPopup() {
        BasePopupWindow popupWindow = new BasePopupWindow(this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void onViewCreate(View view) {
                super.onViewCreate(view);
                view.findViewById(R.id.view_fl).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }

            @Override
            protected int getLayoutId() {
                return R.layout.popup_example;
            }
        };
        popupWindow.showAsDropDown(mTitleBar);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ToastUtils.showShort("popup window dismiss");
            }
        });
    }

    @OnClick(R.id.btn_dialog)
    void clickDialog() {
        final BaseDialog dialog = new BaseDialog(this) {
            @Override
            protected int getLayoutId() {
                return R.layout.dialog_example;
            }

            @Override
            protected void onViewCreate(View view) {
                super.onViewCreate(view);
                view.findViewById(R.id.view_fl).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        };
        dialog.show();
    }

    @OnClick(R.id.btn_fragment_dialog)
    void clickFragmentDialog() {
        ExampleDialogFragment dialogFragment = new ExampleDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());
    }

    @OnClick(R.id.btn_login)
    void clickLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.btn_info_list)
    void clickInfoList() {
        startActivity(new Intent(this, InfoListActivity.class));
    }

    @OnClick(R.id.btn_db)
    void clickDb() {
        SearchDaoDelegate searchDaoDelegate = new SearchDaoDelegate();
        for (int i = 0; i < 5; i++) {
            searchDaoDelegate.save("" + i);
        }
        List<SearchBean> searchBeanList = searchDaoDelegate.query();
        LogUtils.d(GsonHelper.toPrettyJson(searchBeanList));
    }

}
