package com.example.yuemosafe.activity.lost.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yueyue on 2017/4/22.
 */

/**
 * 线程池
 */
public class AsyncDownloadApk {
// TODO: 2017/4/22 这里是之前用于检查xutils下载file问题使用的,使用的时候需要注意一下参数问题

    public static void downloadNewApk(String apkUrl, final Activity mActivity, final String filePath) {

        new AsyncTask<String, Integer, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ProgressDialog mProgressDialog=new ProgressDialog(mActivity);
                mProgressDialog = new ProgressDialog(mActivity);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setMessage("准备下载!!!");
                mProgressDialog.show();

            }

            //当前下载文件的总大小
            int maxLength=0;
            FileOutputStream fos=null;

            @Override
            protected Void doInBackground(String... params) {
                String apkUpdatePath = params[0];
                System.out.println("线程池里面的APK下载路径:" + apkUpdatePath);
                try {
                    URL url = new URL(apkUpdatePath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();
                    maxLength = conn.getContentLength();

                    if (conn.getResponseCode()==200) {
                        InputStream is = conn.getInputStream();
                        fos=new FileOutputStream(filePath);
                        int len=-1;
                        byte[] buffer=new byte[1024];
                        while((len=is.read(buffer))!=-1){
                            publishProgress(len);
                            fos.write(buffer,0,len);
                        }
                        //关闭流,释放资源
                        if (is!=null) {
                            is.close();
                        }
                        if (fos!=null) {
                            fos.close();
                        }

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                   // mProgressDialog.dismiss();
                   // mHandler.sendEmptyMessage(101);
                } catch (IOException e) {
                    e.printStackTrace();
                  //  mProgressDialog.dismiss();
                  //  mHandler.sendEmptyMessage(102);
                }


                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                int progressLen =values[0];
              //  mProgressDialog.incrementProgressBy(progressLen);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                /*if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }*/
                super.onPostExecute(aVoid);
            }
        }.execute(apkUrl);
    }
}
