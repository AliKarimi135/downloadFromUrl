package ir.aliprogramer.downloadproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText address;
    AppCompatButton btnDownload1,btnDownload2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        address=findViewById(R.id.address);
        btnDownload1=findViewById(R.id.btnDownload1);
        btnDownload2=findViewById(R.id.btnDownload2);
    }
}
