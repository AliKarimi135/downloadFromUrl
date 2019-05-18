package ir.aliprogramer.downloadproject;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadService  extends IntentService {

     private int result = Activity.RESULT_CANCELED;

    public static final String RESULT = "result";
    public static final String NOTIFICATION = "service receiver";

    public DownloadService() {
        super("DownloadService");
    }

    // Will be called asynchronously by OS.
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra("urlpath");
        //Extract file name from URL
       String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1, urlPath.length());

        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        //Append timestamp to file name
        fileName = timestamp + "_" + fileName;

        //External directory path to save file
        String folder = "/sdcard" + File.separator + "downloadFromUrl/";

        //Create downloadFromUrl folder if it does not exist
        File directory = new File(folder);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        int count;
        InputStream input = null;
        FileOutputStream output = null;
        try {

            URL url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lengthOfFile = connection.getContentLength();


            // input stream to read file - with 8k buffer
             input = new BufferedInputStream(url.openStream(), 8192);

             output = new FileOutputStream(folder + fileName);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data,0,count);
            }
            // Successful finished
            result = Activity.RESULT_OK;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // flushing output
            try {
                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        publishResults(folder+fileName, result);
    }

    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("filepath", outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}