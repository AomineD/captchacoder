package com.dagf.captchacoder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dagf.captchadagf.CaptchaDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CaptchaDialog di = new CaptchaDialog(this, new CaptchaDialog.CaptchaListener() {
            @Override
            public void onAcceptCorrect() {
                Toast.makeText(MainActivity.this, "CORRECTO PAPAA", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
finish();
            }
        });

        if(di.shouldPresent()){
            di.show();
        }
    }
}
