package com.min.core.helper.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.min.common.util.FileIOUtils;
import com.min.common.util.FileUtils;
import com.min.common.util.StringUtils;
import com.min.common.util.UriUtils;
import com.min.core.base.BaseActivity;
import com.min.core.base.BaseFragment;
import com.min.core.helper.PermissionHelper;

import java.io.File;
import java.io.InputStream;

public class EasyImageSelector {

    public static final int REQ_PICK_PICTURE_FROM_GALLERY = 7458;
    public static final int REQ_PICK_PICTURE_FROM_CAMERA = 7459;

    private static String mPrefix = "";
    private static String mCameraImageFilePath;

    public enum ImageSource {
        GALLERY, CAMERA
    }

    public interface Callbacks {
        void onImagePickerError(Exception e, ImageSource source);

        void onImagePicked(File imageFile, ImageSource source);
    }

    /**
     * 拍照
     *
     * @param prefix 如果为空，则使用默认文件前缀
     * @param obj
     */
    public static void openCamera(final Object obj, final String prefix) {
        if (obj == null) return;
        final Fragment fragment = obj instanceof Fragment ? (Fragment) obj : null;
        final Activity activity = obj instanceof Activity ? (Activity) obj : null;
        if (fragment == null && activity == null) return;
        PermissionHelper.requestCameraPermission(new PermissionHelper.SimplePermissionCallback() {
            @Override
            public void grantSuccess() {
                mPrefix = prefix;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File image = ImageHelper.generateTempImageFile(mPrefix);
                mCameraImageFilePath = image.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, UriUtils.file2Uri(image));
                if (fragment != null) {
                    fragment.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_CAMERA);
                } else {
                    activity.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_CAMERA);
                }
            }
        });
    }

    /**
     * 拍照
     *
     * @param prefix 如果为空，则使用默认文件前缀
     * @param obj
     */
    public static void openGalleryPicker(final Object obj, final String prefix) {
        if (obj == null) return;
        final Fragment fragment = obj instanceof Fragment ? (Fragment) obj : null;
        final Activity activity = obj instanceof Activity ? (Activity) obj : null;
        if (fragment == null && activity == null) return;
        PermissionHelper.requestCameraPermission(new PermissionHelper.SimplePermissionCallback() {
            @Override
            public void grantSuccess() {
                mPrefix = prefix;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (fragment != null) {
                    fragment.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_GALLERY);
                } else {
                    activity.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_GALLERY);
                }
            }
        });
    }

    public static File takenCameraPicture(Context context) throws Exception {
        if (StringUtils.isEmpty(mCameraImageFilePath) || !FileUtils.isFileExists(mCameraImageFilePath)) {
            throw new RuntimeException("select image fail");
        }
        File imageFile = new File(mCameraImageFilePath);
        File compressFile = ImageHelper.compressImageToFile(imageFile);
        return compressFile;
    }

    public static File pickedGalleryPicture(Context context, Uri photoPath) throws Exception {
        InputStream pictureInputStream = context.getContentResolver().openInputStream(photoPath);
        File imageFile = ImageHelper.generateTempImageFile(mPrefix);
        FileIOUtils.writeFileFromIS(imageFile, pictureInputStream);
        File compressFile = ImageHelper.compressImageToFile(imageFile);
        return compressFile;
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data, Context context, final Callbacks callbacks) {
        if (resultCode != AppCompatActivity.RESULT_OK) {
            return;
        }
        if (requestCode == REQ_PICK_PICTURE_FROM_GALLERY) {
            try {
                Uri photoPath = data.getData();
                File photoFile = pickedGalleryPicture(context, photoPath);
                if (photoFile != null) {
                    callbacks.onImagePicked(photoFile, ImageSource.GALLERY);
                } else {
                    throw new Exception("copy image from gallery error");
                }
            } catch (Exception e) {
                callbacks.onImagePickerError(e, ImageSource.GALLERY);
            }
        } else if (requestCode == REQ_PICK_PICTURE_FROM_CAMERA) {
            try {
                File photoFile = takenCameraPicture(context);
                callbacks.onImagePicked(photoFile, ImageSource.CAMERA);
            } catch (Exception e) {
                callbacks.onImagePickerError(e, ImageSource.CAMERA);
            }
        }
    }

}
