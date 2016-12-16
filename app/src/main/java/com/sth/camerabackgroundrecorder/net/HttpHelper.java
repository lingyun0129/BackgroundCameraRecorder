package com.sth.camerabackgroundrecorder.net;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 2016/7/5.
 * Power by cly
 */
public class HttpHelper {
    private static final String SERVER_ADDR = "http://10.250.4.38:8080/VedioServer/LoginServlet?";

    /**
     * @param registration_code;注册码
     * @return true ;登录成功;false；登录失败
     */
    public static boolean loginByPost(String registration_code) {
        boolean result = true;
        try {

            // 请求的地址
            String spec = "http://172.16.237.200:8080/video/login.do";
            // 根据地址创建URL对象
            URL url = new URL(spec);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            //不使用缓存
            urlConnection.setUseCaches(false);
            // 设置请求的超时时间
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            // 传递的数据
            String data = "registrationcode=" + URLEncoder.encode(registration_code, "UTF-8");
            // 设置请求的头
            urlConnection.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 设置请求的头
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes().length));
            // 设置请求的头
            urlConnection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            urlConnection.connect();
            //setDoInput的默认值就是true
            //获取输出流
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                baos.close();
                urlConnection.disconnect();
                // 返回字符串
                final String resultStr = new String(baos.toByteArray());
                if (resultStr.contains("ok")) {
                    result = true;
                }
                // 通过runOnUiThread方法进行修改主线程的控件内容
/*                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在这里把返回的数据写在控件上 会出现什么情况尼
                        tv_result.setText(result);
                    }
                });*/

            } else {
                System.out.println("链接失败.........");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param reg_code;//注册码
     * @param uuid;//uuid
     * @return ;//登录是否成功
     */
    public static boolean LoginByGet(String reg_code, String uuid) {
        boolean result = false;
        String getUrl = SERVER_ADDR + "registration_code=" + reg_code + "&imei=" + uuid;
        try {
            URL url = new URL(getUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5 * 1000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                String readLine = "";
                StringBuffer stringBuffer = new StringBuffer();
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while ((readLine = bufferReader.readLine()) != null) {
                    stringBuffer.append(readLine);
                }
                bufferReader.close();
                httpURLConnection.disconnect();
                Log.i("cai", "从网络获取到内容是：" + stringBuffer.toString());
                if (stringBuffer.toString().equals("登陆失败")) {
                    result = false;
                } else {
                    result = true;
                }
            } else {
                Log.d("cai", "doGet failed");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
