package com.min.common.widget.upload;

import android.content.Intent;
import android.widget.ImageView;

import java.io.File;

public interface IUploadImagesHandler{

    void selectFile(String prefix, Object obj);

    void handleResult(UploadImagesAreaView uploadImagesAreaView,int requestCode, int resultCode, Intent data);

    void uploadFile(UploadImagesAreaView uploadImagesAreaView, File uploadFile, int pos);

    void loadImage(String uri, ImageView imageView);

}
