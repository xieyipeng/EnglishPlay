package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.idl.util.FileUtil;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.simon.permissionlib.core.PermissionHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView see,read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionHelper.with(this).permissions(Manifest.permission.RECORD_AUDIO).requestCode(100).lisener(this).request();
        PermissionHelper.with(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).requestCode(100).lisener(this).request();
        PermissionHelper.with(this).permissions(Manifest.permission.CAMERA).requestCode(100).lisener(this).request();

        init();
    }

    private void init() {
        see = findViewById(R.id.see);
        read = findViewById(R.id.read);


        /**
         * 跳转相机界面
         */
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CameraActivity1.class);
                startActivity(intent);
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ReadActivity.class);
                startActivity(intent);
            }
        });
    }
}
