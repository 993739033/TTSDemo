package com.infrasys.ttsdemo.yzs;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Loki_Zhou
 * @Date 2020/11/17
 **/
public class TTSUtil {
    public static final String ttsPath = "/sdcard/tts/";
    public static final String frontFileName = "frontend_model";
    public static final String backFileName = "backend_female";

    public interface CallBack {
        void call();
    }

    public static void initTTSFiles(final Context context, final CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                copyFilesFromAssets(context, frontFileName);
                copyFilesFromAssets(context, backFileName);
                if (callBack != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.call();
                        }
                    });
                }
            }
        }).start();
    }


    //复制文件从assest文件夹下
    public static void copyFilesFromAssets(Context context, String fileName) {
        File jhPath = new File(ttsPath + fileName);
        //查看该文件是否存在
        if (jhPath.exists()) {
            Log.e("test", "该文件已存在");
        } else {
            //不存在先创建文件夹
            File path = new File(ttsPath);
            if (path.mkdir()) {
                Log.e("test", "创建成功");
            }
            try {
                //得到资源
                AssetManager am = context.getAssets();
                //得到该文件的输入流
                InputStream is = am.open("tts/"+fileName);
                //用输出流写到特定路径下
                FileOutputStream fos = new FileOutputStream(jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                //最后关闭流
                fos.flush();
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
