package com.min.core.helper.image;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.min.common.util.FileIOUtils;
import com.min.common.util.FileUtils;
import com.min.common.util.ImageUtils;
import com.min.common.util.PathUtils;
import com.min.common.util.StringUtils;
import com.min.common.util.UriUtils;
import com.min.core.base.BaseActivity;
import com.min.core.base.BaseFragment;
import com.min.core.helper.PermissionHelper;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

public class EasyImageSelector {

    public static final int REQ_PICK_PICTURE_FROM_GALLERY = 7458;
    public static final int REQ_PICK_PICTURE_FROM_CAMERA = 7459;

    private static final int MAX_IAMGE_FILE_COUNT = 80;  //保留的最大图片个数

    private static String DIR_IMAGE = "images";

    private static String mSuffix = ".jpg";

    private static String mPrefix = "";
    private static String mCameraImageFilePath;

    public enum ImageSource {
        GALLERY, CAMERA
    }

    public interface Callbacks {
        void onImagePickerError(Exception e, ImageSource source);

        void onImagePicked(File imageFile, ImageSource source);
    }

    public static void openCamera(final String prefix, final Object obj) {
        if (obj == null) return;

        final BaseFragment fragment = obj instanceof BaseFragment ? (BaseFragment) obj : null;
        final BaseActivity activity = obj instanceof BaseActivity ? (BaseActivity) obj : null;
        if (fragment == null && activity == null) return;

        final Context context = fragment != null ? fragment.getContext() : activity;

        PermissionHelper.requestCameraPermission(new PermissionHelper.SimplePermissionCallback() {
            @Override
            public void grantSuccess() {
                mPrefix = prefix;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File image = getImageFile();
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

    public static void openGalleryPicker(final String prefix, final Object obj) {
        if (obj == null) return;

        final BaseFragment fragment = obj instanceof BaseFragment ? (BaseFragment) obj : null;
        final BaseActivity activity = obj instanceof BaseActivity ? (BaseActivity) obj : null;
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
        if (StringUtils.isEmpty(mCameraImageFilePath) || FileUtils.isFileExists(mCameraImageFilePath)) {
            throw new RuntimeException("select image fail");
        }
        File imageFile = new File(mCameraImageFilePath);
        return imageFile;
    }

    public static File pickedGalleryPicture(Context context, Uri photoPath) throws Exception {
        InputStream pictureInputStream = context.getContentResolver().openInputStream(photoPath);
        File photoFile = getImageFile();
        FileIOUtils.writeFileFromIS(photoFile, pictureInputStream);
        return photoFile;
    }

    public static File getImageFile() {
        File imageFile = new File(getImageFilesDir(), getImageFileNameByTime());
        return imageFile;
    }

    private static String getImageFileNameByTime() {
        return mPrefix + "_" + System.currentTimeMillis() + mSuffix;
    }

    public static File getImageFilesDir() {
        File fileDir = new File(PathUtils.getExternalAppCachePath(), DIR_IMAGE);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        return fileDir;
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data, Context context, final Callbacks callbacks) {
        if (resultCode != AppCompatActivity.RESULT_OK) {
            return;
        }
        deleteFilesWhenArrMaxCount(context);
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

    private static void deleteFilesWhenArrMaxCount(Context context) {
        try {
            File imageDir = getImageFilesDir();
            File[] files = imageDir.listFiles();
            if (files != null && files.length >= MAX_IAMGE_FILE_COUNT) {
                Arrays.sort(files, new FileComparator());
                int tempCnt = files.length - MAX_IAMGE_FILE_COUNT;
                for (int i = 0; i < tempCnt; i++) {
                    files[i].delete();
                }
            }
        } catch (Exception e) {
        }
    }

    public static class FileComparator implements Comparator<File> {
        @Override
        public int compare(File lhs, File rhs) {
            Long l = lhs.lastModified();
            Long r = rhs.lastModified();
            return l.compareTo(r);
        }
    }

}
