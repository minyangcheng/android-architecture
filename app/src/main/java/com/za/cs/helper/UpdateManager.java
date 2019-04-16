package com.za.cs.helper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.widget.Button;

import com.min.common.util.AppUtils;
import com.min.common.util.GsonUtils;
import com.min.common.util.LogUtils;
import com.min.common.util.ServiceUtils;
import com.min.common.util.ToastUtils;
import com.min.core.CoreConstants;
import com.min.core.base.BaseActivity;
import com.min.core.helper.RxHelper;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.za.cs.BuildConfig;
import com.za.cs.data.DataManager;
import com.za.cs.data.model.UpdateBean;
import com.za.cs.service.UpdateService;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by minyangcheng on 2016/8/15.
 */
public class UpdateManager {

    private BaseActivity mActivity;
    private AlertDialog mDialog;

    private UpdateBean mUpdateResponse;

    public UpdateManager(BaseActivity activity) {
        this.mActivity = activity;
    }

    public void checkUpdate(final boolean isShowHUD) {
        if (ServiceUtils.isServiceRunning(UpdateService.class.getName())) {
            return;
        }
        DataManager.getMobileService()
                .checkUpdate()
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.io_main())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (isShowHUD) {
                            mActivity.showHudDialog();
                        }
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        mActivity.hideHudDialog();
                    }
                })
                .subscribe(new Action1<UpdateBean>() {
                    @Override
                    public void call(UpdateBean updateResponse) {
                        LogUtils.dTag(CoreConstants.HTTP_LOG, GsonUtils.toJson(updateResponse));
                        mUpdateResponse = updateResponse;
                        handleUpdateBean(isShowHUD);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.eTag(CoreConstants.HTTP_LOG, throwable);
                        if (isShowHUD) {
                            RxHelper.handlerError(throwable);
                        }
                    }
                });
    }

    private void handleUpdateBean(boolean isShowHUD) {
        if (validateResponse()) {
            boolean isForceUpdate = !mUpdateResponse.changelog.contains("非强制更新");
            if (isForceUpdate) {
                showForceUpdateDialog();
            } else {
                showAlertUpdateDialog();
            }
        } else {
            if (isShowHUD) {
                ToastUtils.showShort("当前已是最新版本");
            }
        }
    }

    private boolean validateResponse() {
        int temp = mUpdateResponse.version - BuildConfig.VERSION_CODE;
        return temp > 0;
    }

    private void showForceUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle("发现新版本")
                .setMessage(mUpdateResponse.changelog)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hackAlertDialog(dialog, false); //使之不能关闭(此为机关所在，其它语句相同)
                        AlertDialog alertDialog = (AlertDialog) dialog;
                        Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        btn.setText("请稍等，正在更新中...");
                        UpdateService.startService(mActivity, true, mUpdateResponse.installUrl, mUpdateResponse.md5);
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        //如果在强制更新的时候，点击回退键则直接退出应用
                        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                            hackAlertDialog(dialog, true);
                            dialog.dismiss();
                            AppUtils.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AppUtils.exitApp();
                                }
                            }, 100);
                            return true;
                        }
                        return false;
                    }
                });
        mDialog = builder.create();
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private void showAlertUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle("发现新版本")
                .setMessage(mUpdateResponse.changelog)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateService.startService(mActivity
                                , false, mUpdateResponse.installUrl
                                , mUpdateResponse.md5);
                    }
                })
                .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        mDialog = builder.create();
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    private void hackAlertDialog(DialogInterface dialog, boolean isShowing) {
        try {
            Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, isShowing);// false - 使之不能关闭(此为机关所在，其它语句相同)
        } catch (Exception e) {
        }
    }

}
