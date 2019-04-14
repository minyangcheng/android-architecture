package com.min.sample.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.min.common.util.GsonUtils;
import com.min.common.util.LogUtils;
import com.min.common.util.ReflectUtils;
import com.min.common.util.ToastUtils;
import com.min.common.widget.TitleBar;
import com.min.common.widget.divider.DividerGridItemDecoration;
import com.min.common.widget.recyclerview.BaseRecyclerViewAdapter;
import com.min.core.base.BaseActivity;
import com.min.core.helper.MultipartHelper;
import com.min.core.helper.RxHttpResponseHelper;
import com.min.core.helper.image.EasyImageSelector;
import com.min.core.view.ImagePreviewDialog;
import com.min.router.GlobalRouter;
import com.min.sample.R;
import com.min.sample.data.DataManager;
import com.min.sample.data.local.db.delegate.SearchDaoDelegate;
import com.min.sample.data.model.MainItem;
import com.min.sample.data.model.SearchBean;
import com.min.sample.ui.adapter.MainAdapter;
import com.min.sample.ui.dialog.CarPopWin;
import com.min.sample.ui.dialog.CarSelectDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    private TitleBar mTitleBar;
    private RecyclerView mListRv;
    private MainAdapter mainAdapter;

    private MainItem[] mItems = {
            new MainItem("刷新加载分页自动处理", RefreshLoaderListActivity.class),
            new MainItem("MVP使用", MVPActivity.class),
            new MainItem("TabViewPager", TabViewPagerActivity.class),
            new MainItem("组件化路由跳转", "openOrder"),
            new MainItem("使用weex容器", "openWeexContainer"),
            new MainItem("使用h5容器", "openH5Container"),
            new MainItem("图片预览", "imagePreview"),
            new MainItem("自定义Popupwindow", "openPopupWindow"),
            new MainItem("自定义DialogFragment", "openDialogFragment"),
            new MainItem("Database数据库操作", "operateDb"),
            new MainItem("网络请求", "httpRequest"),
            new MainItem("上传文件", "uploadFile"),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    private void findView() {
        mTitleBar = findViewById(R.id.title_bar);
        mListRv = findViewById(R.id.rv_list);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListRv.setLayoutManager(layoutManager);
        DividerGridItemDecoration decoration = new DividerGridItemDecoration(Color.parseColor("#d1d1d1"), 1);
        mListRv.addItemDecoration(decoration);
        mainAdapter = new MainAdapter(this);
        mListRv.setAdapter(mainAdapter);
        mainAdapter.setData(Arrays.asList(mItems));

        mainAdapter.setOnItemClickLitener(new BaseRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                MainItem item = mainAdapter.getData().get(position);
                if (item.destClass != null) {
                    Intent intent = new Intent(getContext(), item.destClass);
                    startActivity(intent);
                } else if (item.methodName != null) {
                    ReflectUtils.reflect(MainActivity.this).method(item.methodName);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    //=============================== 每个方法一个示例 ====================================================

    private void openOrder() {
        GlobalRouter.getInstance()
                .navigation("cg://native.com/order/OrderMainPage")
                .appendQueryParameter("company_name", "cheguo")
                .appendQueryParameter("user_id", "minych")
                .putExtra("isLogin", true)
                .start();
    }

    private void openWeexContainer() {
        GlobalRouter.getInstance()
                .navigation("cg://weex.com/page/SamplePage")
                .appendQueryParameter("company_name", "cheguo")
                .appendQueryParameter("user_id", "minych")
                .putExtra("isLogin", true)
                .start();
    }

    private void openH5Container() {
        GlobalRouter.getInstance()
                .navigation("cg://h5.com")
                .appendQueryParameter("company_name", "cheguo")
                .appendQueryParameter("host", "www.baidu.com")
                .appendQueryParameter("scheme", "http")
                .start();
    }

    private void imagePreview() {
        List<ImagePreviewDialog.ImageItem> imageItems = new ArrayList<>();
        imageItems.add(new ImagePreviewDialog.ImageItem("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554985053111&di=072f77d6e6f963a2a284499e538bc595&imgtype=0&src=http%3A%2F%2Fcdnq.duitang.com%2Fuploads%2Fitem%2F201201%2F31%2F20120131203259_QaWzT.jpg"));
        imageItems.add(new ImagePreviewDialog.ImageItem("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554985053111&di=d34dcd14441c8c0bc8171e59099c8761&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201412%2F23%2F20141223204109_QEi4v.jpeg"));
        imageItems.add(new ImagePreviewDialog.ImageItem("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554985053110&di=a27055988aa997f7317083f1cec6d230&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201610%2F24%2F20161024160810_xusFa.jpeg"));
        ImagePreviewDialog dialog = new ImagePreviewDialog(this, imageItems, 0);
        dialog.show();
    }

    private void operateDb() {
        showHudDialog(false);
        SearchDaoDelegate searchDaoDelegate = new SearchDaoDelegate();
        for (int i = 0; i < 5; i++) {
            searchDaoDelegate.save("" + i);
        }
        List<SearchBean> searchBeanList = searchDaoDelegate.query();
        LogUtils.d(GsonUtils.toPrettyJson(searchBeanList));
        hideHudDialog();
    }

    private void openPopupWindow() {
        CarPopWin popWin = new CarPopWin(this);
        popWin.showAsDropDown(mTitleBar, 0, 0, Gravity.RIGHT);
    }

    private void openDialogFragment() {
        CarSelectDialogFragment dialogFragment = new CarSelectDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());
    }

    private void httpRequest() {
        DataManager.getMobileService()
                .login("15257178923", "123456a")
                .compose(RxHttpResponseHelper.handleServerResult())
                .doOnSubscribe(() -> showHudDialog())
                .doOnTerminate(() -> hideHudDialog())
                .subscribe(data -> {
                        }
                        , throwable -> {
                        });
    }

    private void uploadFile() {
        EasyImageSelector.openCamera(this, null);
    }

    private void uploadFile_(File imageFile) {
        DataManager.getMobileService()
                .uploadFile(MultipartHelper.keyValuePart("name", "1111"), MultipartHelper.imageMultiPart("file", imageFile))
                .compose(RxHttpResponseHelper.io_main())
                .subscribe(data -> {
                        }
                        , throwable -> {
                        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImageSelector.handleActivityResult(requestCode, resultCode, data, this, new EasyImageSelector.Callbacks() {

            @Override
            public void onImagePickerError(Exception e, EasyImageSelector.ImageSource source) {
                ToastUtils.showShort("选择图片失败！");
            }

            @Override
            public void onImagePicked(File imageFile, EasyImageSelector.ImageSource source) {
                uploadFile_(imageFile);
            }
        });
    }
}
