package com.min.core.helper.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.min.common.util.FileUtils;
import com.min.common.util.LogUtils;
import com.min.common.util.PathUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;

public class ImageHelper {

    private static String GENERATE_IMAGE_DIR = "images";

    private static final int MAX_IMAGE_FILE_COUNT = 5;

    public static File compressImageToFile(File sourceFile) {
        return compressImageToFile(sourceFile, 720, 1280, 800);
    }

    public static File compressImageToFile(File sourceFile, int reqWidth, int reqHeight, int maxSizeKb) {
        File resultFile = sourceFile;
        if (FileUtils.getFileLength(sourceFile) / 1024 > maxSizeKb) {
            File compressImageFile = generateTempImageFile("compress");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
            int quality = 100;
            do {
                saveImageFileToFile(bitmap, compressImageFile, quality);
                quality -= 10;
                if (quality <= 50) {
                    break;
                }
                resultFile = compressImageFile;
            } while (FileUtils.getFileLength(compressImageFile) / 1024 > maxSizeKb);

            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        LogUtils.dTag_("ImageHelper", "原图片：%s ---> 压缩后 %s",
                FileUtils.getFileSize(sourceFile), FileUtils.getFileSize(resultFile));
        return resultFile;
    }

    private static void saveImageFileToFile(Bitmap bitmap, File destFile, int quality) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static File generateTempImageFile(String prefix) {
        deleteFilesWhenArrMaxCount();
        if (TextUtils.isEmpty(prefix)) {
            prefix = "temp";
        }
        return new File(getImageFilesDir(), prefix + "_" + System.currentTimeMillis() + ".jpg");
    }

    public static File getImageFilesDir() {
        File fileDir = new File(PathUtils.getExternalAppCachePath(), GENERATE_IMAGE_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        return fileDir;
    }

    private static void deleteFilesWhenArrMaxCount() {
        try {
            File imageDir = getImageFilesDir();
            File[] files = imageDir.listFiles();
            if (files != null && files.length >= MAX_IMAGE_FILE_COUNT) {
                Arrays.sort(files, new FileComparator());
                int tempCnt = files.length - MAX_IMAGE_FILE_COUNT;
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
