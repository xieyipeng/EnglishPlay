package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class ReadActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean hasGotToken = false;
    private AlertDialog.Builder alertDialog;
    private static final int REQUEST_CODE_GENERAL = 105;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private TextView show;
//    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
//    private static final int REQUEST_CODE_ACCURATE = 108;
//    private static final int REQUEST_CODE_GENERAL_ENHANCED = 109;
//    private static final int REQUEST_CODE_GENERAL_WEBIMAGE = 110;
//    private static final int REQUEST_CODE_BANKCARD = 111;
//    private static final int REQUEST_CODE_VEHICLE_LICENSE = 120;
//    private static final int REQUEST_CODE_DRIVING_LICENSE = 121;
//    private static final int REQUEST_CODE_LICENSE_PLATE = 122;
//    private static final int REQUEST_CODE_BUSINESS_LICENSE = 123;
//    private static final int REQUEST_CODE_RECEIPT = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_read);

        show = findViewById(R.id.show_tv);
        show.setOnClickListener(this);

        //alertDialog = new AlertDialog.Builder(this);
        // 通用文字识别
        findViewById(R.id.CAP_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(ReadActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
            }
        });

//        // 通用文字识别(高精度版)
//        findViewById(R.id.accurate_basic_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
//            }
//        });
//
//        // 通用文字识别（含位置信息版）
//        findViewById(R.id.general_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_GENERAL);
//            }
//        });
//
//        // 通用文字识别（含位置信息高精度版）
//        findViewById(R.id.accurate_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_ACCURATE);
//            }
//        });
//
//        // 通用文字识别（含生僻字版）
//        findViewById(R.id.general_enhance_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_GENERAL_ENHANCED);
//            }
//        });
//
//        // 网络图片识别
//        findViewById(R.id.general_webimage_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_GENERAL_WEBIMAGE);
//            }
//        });
//
//        // 身份证识别
//        findViewById(R.id.idcard_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, IDCardActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // 银行卡识别
//        findViewById(R.id.bankcard_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_BANK_CARD);
//                startActivityForResult(intent, REQUEST_CODE_BANKCARD);
//            }
//        });
//
//        // 行驶证识别
//        findViewById(R.id.vehicle_license_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_VEHICLE_LICENSE);
//            }
//        });
//
//        // 驾驶证识别
//        findViewById(R.id.driving_license_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_DRIVING_LICENSE);
//            }
//        });
//
//        // 车牌识别
//        findViewById(R.id.license_plate_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_LICENSE_PLATE);
//            }
//        });
//
//        // 营业执照识别
//        findViewById(R.id.business_license_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_BUSINESS_LICENSE);
//            }
//        });
//
//        // 通用票据识别
//        findViewById(R.id.receipt_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity1.class);
//                intent.putExtra(CameraActivity1.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity1.KEY_CONTENT_TYPE,
//                        CameraActivity1.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_RECEIPT);
//            }
//        });



        // 请选择您的初始化方式
        // initAccessToken();
        initAccessTokenWithAkSk();
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    private void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }
            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }
            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "SMNkwkFh3t4duCp90Rzxr5hT", "du8usF36TFlAdtregCl2GqACFiKz8LqY");
    }

    private void alertText(final String title, final String message) {
        Log.d("ReadActivity", "alertText: hello");
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("ReadActivity","message" + message);
                try {
//                    JSONObject jsonObject = new JSONObject(message);
//                    String result = jsonObject.getString("words_result");
//                    Log.d("ReadActivity","id is " + result);
//
//                    result.replace("[","");
//                    result.replace("]","");
//
//                    Log.d("ReadActivity", "run: replace"+result);
//                        show.setText(result);
                    Toast.makeText(ReadActivity.this, message, Toast.LENGTH_SHORT).show();
                    List<OCRJSon.WordsResultBean> words_result = new Gson().fromJson(message, OCRJSon.class).getWords_result();
                    for (int i = 0; i < words_result.size(); i++) {
                        String word = words_result.get(i).getWords();
                        show.setText(word + "  ");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void infoPopText(final String result) {
        alertText("", result);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        // 识别成功回调，通用文字识别（含位置信息）
//        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，通用文字识别（含位置信息高精度版）
//        if (requestCode == REQUEST_CODE_ACCURATE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recAccurate(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }


        // 识别成功回调，通用文字识别
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralBasic(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }
//        // 识别成功回调，通用文字识别（高精度版）
//        if (requestCode == REQUEST_CODE_ACCURATE_BASIC && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recAccurateBasic(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，通用文字识别（含生僻字版）
//        if (requestCode == REQUEST_CODE_GENERAL_ENHANCED && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recGeneralEnhanced(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，网络图片文字识别
//        if (requestCode == REQUEST_CODE_GENERAL_WEBIMAGE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recWebimage(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，银行卡识别
//        if (requestCode == REQUEST_CODE_BANKCARD && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recBankCard(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，行驶证识别
//        if (requestCode == REQUEST_CODE_VEHICLE_LICENSE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recVehicleLicense(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，驾驶证识别
//        if (requestCode == REQUEST_CODE_DRIVING_LICENSE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recDrivingLicense(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，车牌识别
//        if (requestCode == REQUEST_CODE_LICENSE_PLATE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recLicensePlate(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，营业执照识别
//        if (requestCode == REQUEST_CODE_BUSINESS_LICENSE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recBusinessLicense(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，通用票据识别
//        if (requestCode == REQUEST_CODE_RECEIPT && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recReceipt(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance().release();
    }

    @Override
    public void onClick(View view) {

    }
}
