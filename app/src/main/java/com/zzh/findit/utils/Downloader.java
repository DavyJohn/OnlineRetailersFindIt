package com.zzh.findit.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

/**
 * Created by 腾翔信息 on 2018/3/22.
 */

public class Downloader implements Callable<File> {
    protected int connectTimeout = 30 * 1000;
    protected int readTimeout = 1 * 1000 * 1000;

    protected int speedRefreshInterval = 500;

    protected byte[] buffer;

    private URL url;
    private File file;

    private float averageSpeed;
    private float currentSpeed;

    public Downloader() {
        buffer = new byte[8 * 1024];
    }

    public void setUrlAndFile(URL url, File file) {
        this.url = url;
        this.file = file;
        this.averageSpeed = 0;
        this.currentSpeed = 0;
    }

    public URL getUrl() {
        return url;
    }

    public File getFile() {
        return file;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    @Override
    public File call() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();

        InputStream in = null;
        OutputStream out = null;
        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            conn.connect();

            in = conn.getInputStream();
            out = new FileOutputStream(file);

            int time = 0;
            int bytesInTime = 0;
            for (;;) {
                watch.split();
                int bytes = in.read(buffer);
                if (bytes == -1) {
                    break;
                }
                out.write(buffer, 0, bytes);

                time += watch.getTimeFromSplit();
                if (time >= speedRefreshInterval) {
                    currentSpeed = getSpeed(bytesInTime, time);
                    time = 0;
                    bytesInTime = 0;
                }
            }
        } catch (IOException e) {
            file.delete();
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        watch.stop();
        averageSpeed = getSpeed(file.length(), watch.getTime());

        return file;
    }

    private static float getSpeed(long bytesInTime, long time) {
        return (float) bytesInTime / 1024 / ((float) time / 1000);
    }
}
