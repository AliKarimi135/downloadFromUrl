package ir.aliprogramer.downloadproject;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    Toolbar toolbar;
    EditText address;
    AppCompatButton btnDownload1, btnDownloadService;
    private static final int WRITE_REQUEST_CODE = 300;
    ProgressDialog progressDialog = null;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        address = findViewById(R.id.address);
        btnDownload1 = findViewById(R.id.btnDownload1);
        btnDownloadService = findViewById(R.id.btnDownload2);
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);

        btnDownload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckForSDCard.isSDCardPresent()) {
                    if (EasyPermissions.hasPermissions(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        url = address.getText().toString().trim();
                        new DownloadFile(MainActivity.this).execute(url);
                    } else {
                        EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "SD Card یافت نشد", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, MainActivity.this);

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Download the file once permission is granted
        url = address.getText().toString().trim();
        new DownloadFile(MainActivity.this).execute(url);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(getApplicationContext(), "عدم دسترسی نوشتن در حافظه", Toast.LENGTH_LONG).show();
    }

    /* Show progress dialog. */
    public void showProgressDialog() {
        // Set progress dialog display message.
        progressDialog.setMessage("  در حال دانلود...");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // The progress dialog can not be cancelled.
        progressDialog.setCancelable(false);

        // Show it.
        progressDialog.show();
    }

    public void setProgressDialog(int progress) {
        progressDialog.setProgress(progress);
    }

    /* Hide progress dialog. */
    public void hideProgressDialog() {
        // Close it.
        progressDialog.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String string = bundle.getString("filepath");
                int resultCode = bundle.getInt(DownloadService.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this,
                            "Download complete. Download URI: " + string, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Download failed", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public void downloadByService(View view) {
            Intent intent=new Intent(this,DownloadService.class);
            intent.putExtra("urlpath", address.getText().toString().trim());
            startService(intent);
    }
}


