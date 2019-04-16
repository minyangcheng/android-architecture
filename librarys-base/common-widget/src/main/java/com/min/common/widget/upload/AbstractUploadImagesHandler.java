package com.min.common.widget.upload;

import android.content.Intent;
import android.widget.ImageView;

import java.io.File;

public abstract class AbstractUploadImagesHandler {

    public UploadImagesAreaView uploadImagesAreaView;

    public AbstractUploadImagesHandler(UploadImagesAreaView uploadImagesAreaView) {
        this.uploadImagesAreaView = uploadImagesAreaView;
    }

    public abstract void selectFile(Object hostObj);

    public abstract void handleFile(int requestCode, int resultCode, Intent data);

    protected void handleUploadImageBean(UploadImageBean uploadImageBean){
    }

    public abstract void uploadFile(File uploadFile, int pos);

    public abstract void displayImage(String uri, ImageView imageView);

    public abstract void playFile(int pos);

    public abstract void destroy();

}
