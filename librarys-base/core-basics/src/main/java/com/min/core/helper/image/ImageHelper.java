package com.min.core.helper.image;

import com.min.common.util.FileUtils;
import com.min.common.util.LogUtils;

public class ImageHelper {

//    public static File compressImageToFile(File sourceFile) {
//        return compressImageToFile(sourceFile, 720, 1280, 800);
//    }
//
//    public static File compressImageToFile(File sourceFile, int reqWidth, int reqHeight, int maxSizeKb) {
//        File resultFile = sourceFile;
//        try {
//            if (FileUtils.getFileSize().getFileSize(sourceFile) / 1024 > maxSizeKb) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
//
//                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//                options.inJustDecodeBounds = false;
//                Bitmap bitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
//                int quality = 100;
//                do {
//                    saveImageFileToFile_(bitmap, outFile, quality);
//                    quality -= 10;
//                    if (quality <= 50) {
//                        break;
//                    }
//                    resultFile = outFile;
//                } while (FileUtil.getFileSize(outFile) / 1024 > maxSizeKb);
//
//                if (bitmap != null && !bitmap.isRecycled()) {
//                    bitmap.recycle();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        LogUtils.dTag("ImageUtils", "原图片：%s ---> 压缩后 %s", FileUtil.getAutoFileOrFilesSize(sourceFile.getAbsolutePath()), FileUtil.getAutoFileOrFilesSize(resultFile.getAbsolutePath()));
//        return resultFile;
//    }
//
//    private static void saveImageFileToFile(Bitmap bitmap, File destFile, int quality) {
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(destFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
//            fos.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//        if (height > reqHeight || width > reqWidth) {
//            if (width > height) {
//                inSampleSize = Math.round((float) height / (float) reqHeight);
//            } else {
//                inSampleSize = Math.round((float) width / (float) reqWidth);
//            }
//        }
//        return inSampleSize;
//    }

}
