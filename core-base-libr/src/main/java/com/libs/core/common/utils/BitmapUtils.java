package com.libs.core.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    public static Bitmap getCacheBitmapFromView(View view) {
        return getCacheBitmapFromView(view, 1.0f);
    }

    /**
     * @param view
     * @param rate 宽高比
     * @return
     */
    /*public static Bitmap getCacheBitmapFromView(View view, float rate) {

        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            int height = (int) (drawingCache.getWidth() / rate);
            if (height < drawingCache.getHeight()) {
                bitmap = Bitmap.createBitmap(drawingCache, 0, 0, drawingCache.getWidth(), height);
            } else {
                bitmap = Bitmap.createBitmap(drawingCache, 0, 0, drawingCache.getWidth(), drawingCache.getHeight());
            }

            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        view.destroyDrawingCache();

        return bitmap;
    }*/
    public static Bitmap getCacheBitmapFromView(View view, float rate) {

        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            /*int height = (int) (drawingCache.getWidth() / rate);
            if (height < drawingCache.getHeight()) {
                bitmap = Bitmap.createBitmap(drawingCache, 0, 0, drawingCache.getWidth(), height);
            } else {
                bitmap = Bitmap.createBitmap(drawingCache, 0, 0, drawingCache.getWidth(), drawingCache.getHeight());
            }*/
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = loadBitmapFromView(view);
        }
        view.destroyDrawingCache();

        return bitmap;
    }

    public static Bitmap loadBitmapFromView(View v) {
        /*Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);*/
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return b;
    }

    /**
     * @param source
     * @param maxSize 单位是kb
     * @return
     */
    public static byte[] compressBitmap(Bitmap source, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //这里100表示不压缩，把压缩后的数据存放到baos中
        source.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 95;
        //如果压缩后的大小超出所要求的，继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            baos.reset();
            source.compress(Bitmap.CompressFormat.JPEG, options, baos);
            int length = source.getByteCount();
            //每次减少5%质量
            if (options > 5) {//避免出现options<=0
                options -= 5;
            } else {
                break;
            }
        }

        return baos.toByteArray();
    }

    /**
     * Bitmap保存为文件
     *
     * @param bitmap
     * @param path
     * @return
     */
    public static File saveBitmapFile(Bitmap bitmap, String path) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new File(path);
    }

    /**
     * Bitmap 转 bytes
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                if (byteArrayOutputStream.toByteArray() == null) {
                    LogUtils.e("BitmapUtils", "bitmap2Bytes byteArrayOutputStream toByteArray=null");
                }
                return byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                LogUtils.e("BitmapUtils", e.toString());
            } finally {
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException var14) {

                    }
                }
            }

            return null;
        } else {
            LogUtils.e("BitmapUtils", "bitmap2Bytes bitmap == null or bitmap.isRecycled()");
            return null;
        }
    }

    /**
     * 压缩图片
     * 在保证质量的情况下尽可能压缩 不保证压缩到指定字节
     *
     * @param datas
     * @param byteCount 指定压缩字节数
     * @return
     */
    public static byte[] compressBitmap(byte[] datas, int byteCount) {
        boolean isFinish = false;
        if (datas != null && datas.length > byteCount) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Bitmap tmpBitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
            int times = 1;
            double percentage = 1.0D;

            while (!isFinish && times <= 10) {
                percentage = Math.pow(0.8D, (double) times);
                int compress_datas = (int) (100.0D * percentage);
                tmpBitmap.compress(Bitmap.CompressFormat.JPEG, compress_datas, outputStream);
                if (outputStream != null && outputStream.size() < byteCount) {
                    isFinish = true;
                } else {
                    outputStream.reset();
                    ++times;
                }
            }

            if (outputStream != null) {
                byte[] outputStreamByte = outputStream.toByteArray();
                if (!tmpBitmap.isRecycled()) {
                    tmpBitmap.recycle();
                }

                if (outputStreamByte.length > byteCount) {
                    LogUtils.e("BitmapUtils", "compressBitmap cannot compress to " + byteCount + ", jnAfter compress size=" + outputStreamByte.length);
                }

                return outputStreamByte;
            }
        }

        return datas;
    }
}
