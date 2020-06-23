package com.libs.core.common.utils.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jince on 2017/12/13.
 */

public class PingConnector extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        long t1 = System.currentTimeMillis();
        s = pingNetwork("www.baidu.com");
        long t2 = System.currentTimeMillis();
        Log.i("ping", s + ",耗时：" + (t2 - t1) + "ms");
        return s;
    }

    public String pingNetwork(String str) {
        String result = "";
        Process p;
        try {
            //ping -c 3 -w 100  中  ，-c 是指ping的次数 3是指ping 3次 ，-w 100  以秒为单位指定超时间隔，是指超时时间为100秒
            p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + str);
            int status = p.waitFor();

            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println("Return ============" + buffer.toString());

            if (status == 0) {
                result = "success";
            } else {
                result = "faild";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}