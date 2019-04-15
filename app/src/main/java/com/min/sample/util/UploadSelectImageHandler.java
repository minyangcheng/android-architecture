package com.min.sample.util;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.ImageView;

import com.min.common.util.LogUtils;
import com.min.common.util.ToastUtils;
import com.min.common.widget.upload.AbstractUploadImagesHandler;
import com.min.common.widget.upload.UploadImageBean;
import com.min.common.widget.upload.UploadImagesAreaView;
import com.min.core.helper.ImageLoaderHelper;
import com.min.core.helper.MultipartHelper;
import com.min.core.helper.RxHttpResponseHelper;
import com.min.core.helper.image.EasyImageSelector;
import com.min.core.view.ImagePreviewDialog;
import com.min.sample.data.DataManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by minych on 2019/4/15 10:20
 */
public class UploadSelectImageHandler extends AbstractUploadImagesHandler {

    private Subscription subscription;

    public UploadSelectImageHandler(UploadImagesAreaView uploadImagesAreaView) {
        super(uploadImagesAreaView);
    }

    @Override
    public void selectFile(Object hostObj) {
        AlertDialog.Builder builder = new AlertDialog.Builder(uploadImagesAreaView.getContext())
                .setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            EasyImageSelector.openCamera(hostObj, uploadImagesAreaView.getNameSpace());
                        } else if (which == 1) {
                            EasyImageSelector.openGalleryPicker(hostObj, uploadImagesAreaView.getNameSpace());
                        }
                    }
                });
        builder.show();
    }

    @Override
    public void handleFile(int requestCode, int resultCode, Intent data) {
        EasyImageSelector.handleActivityResult(requestCode, resultCode, data, uploadImagesAreaView.getContext(), new EasyImageSelector.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImageSelector.ImageSource source) {
                LogUtils.e(e);
                ToastUtils.showShort("选择图片失败...");
            }

            @Override
            public void onImagePicked(File imageFile, EasyImageSelector.ImageSource source) {
                uploadImagesAreaView.imagePicked(imageFile);
            }
        });
    }

    @Override
    public void uploadFile(File uploadFile, int pos) {
        subscription = DataManager.getMobileService()
                .uploadFile(MultipartHelper.imageMultiPart("file", uploadFile))
                .compose(RxHttpResponseHelper.io_main())
                .subscribe(data -> {
                            LogUtils.d(data);
                            uploadImagesAreaView.updateStatusInPos_success(pos, data.getUrl());
                        }
                        , throwable -> {
                            uploadImagesAreaView.updateStatusInPos_fail(pos);
                        });
    }

    @Override
    public void displayImage(String uri, ImageView imageView) {
        ImageLoaderHelper.displayImage(uri, imageView);
    }

    @Override
    public void playFile(int pos) {
        List<String> imageList = new ArrayList<>();
        List<UploadImageBean> originalList = uploadImagesAreaView.getData();
        int temp = pos;
        for (int i = 0; i < originalList.size(); i++) {
            UploadImageBean uploadBean = originalList.get(i);
            if (TextUtils.isEmpty(uploadBean.url)) {
                if (i < pos) {
                    temp--;
                }
                continue;
            }
            imageList.add(uploadBean.url);
        }
        int size = imageList.size();
        if (size > 0 && temp < size) {
            ImagePreviewDialog.showImagePreview(uploadImagesAreaView.getContext(), imageList, pos);
        }
    }

    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
