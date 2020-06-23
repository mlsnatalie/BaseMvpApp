package com.libs.core.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.libs.core.common.dialog.ToastDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveHelper {

    public static void savePhoto(final Context context, final Bitmap bmp, final SaveResultCallback saveResultCallback) {
        final File sdDir = getSDPath();
        if (sdDir == null) {
            ToastDialog.makeText(context, "设备自带的存储不可用", ToastDialog.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                File appDir = new File(sdDir, "out_photo");
                if (!appDir.exists()) {
                    boolean isSuccess = appDir.mkdir();
                    Log.e("SaveHelper", "创建目录：" + isSuccess);
                }
                String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE).format(new Date()) + ".png";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    saveResultCallback.onSavedSuccess();
                } catch (FileNotFoundException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                } catch (IOException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                }

                // 保存后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }).start();
    }


    private static File getSDPath() {
        File sdDir = null;
        //判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            //获取跟目录
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir;
    }

    public interface SaveResultCallback {
        void onSavedSuccess();

        void onSavedFailed();
    }
}
