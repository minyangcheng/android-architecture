package com.min.sample.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.min.common.widget.CenterTitleToolbar;
import com.min.core.base.BaseActivity;
import com.min.core.base.BaseDialog;
import com.min.core.base.BasePopupWindow;
import com.min.core.util.GsonUtil;
import com.min.hybrid.H5ContainerActivity;
import com.min.sample.R;
import com.min.sample.data.local.db.delegate.SearchDaoDelegate;
import com.min.sample.data.model.SearchBean;
import com.min.sample.ui.dialog.ExampleDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.floo.Floo;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mToolbar.setTitle("架构示例");
        mToolbar.inflateMenu(R.menu.main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_setting) {
                    ToastUtils.showShort("you click setting");
                }
                return false;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.btn_order)
    void clickOpenOrder() {
        Floo.navigation(getContext(), "cg://cheguo.com/order")
                .appendQueryParameter("company_name", "cheguo")
                .appendQueryParameter("user_id", "minych")
                .putExtra("isLogin", true)
                .start();
    }

    @OnClick(R.id.btn_web)
    void clickOpenWeb() {
        H5ContainerActivity.startActivity(this, "http://10.10.12.170:8080");
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
        popupWindow.showAsDropDown(mToolbar);
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
        LogUtils.d(GsonUtil.toPrettyJson(searchBeanList));
    }

}
