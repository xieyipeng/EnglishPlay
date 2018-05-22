package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.bean.Translate;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.simon.permissionlib.core.PermissionHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baidu.ocr.sdk.utils.Util.md5;

/*
调用相机识别物体类
 */
public class TranslateActivity extends AppCompatActivity {

    private ImageView comment_image;
    private TextView translationChinese;
    private TextView translationWords;
    private TextView voiceTextView;
    private ImageView backImageView;
    public static final String APP_ID = "20180515000159566";
    public static final String PASSWORD = "gijguhKtgTWcaFmet5Ak";
    public static final String TAG = "TranslationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Log.d(TAG, "onCreate: "+name);
//        PermissionHelper.with(TranslateActivity.this).permissions(Manifest.permission.RECORD_AUDIO).requestCode(100).lisener(TranslateActivity.this).request();
//        PermissionHelper.with(TranslateActivity.this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).requestCode(100).lisener(TranslateActivity.this).request();
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5afa2045");
        speech(name);
        initView(name);

    }

    private void initView(final String name) {
        comment_image = (ImageView) findViewById(R.id.commentimage);
        comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TranslateActivity.this, DetailActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

        translationChinese=findViewById(R.id.translate_chinese);
        translationWords=findViewById(R.id.translate_words);
        backImageView=findViewById(R.id.login_reback1_btn);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    String from = "en";
                    String to = "zh";
                    int salt = 1435660288;
                    String sign = APP_ID + name + salt + PASSWORD;
                    String overSign = md5(sign);
                    String UrlString = "http://api.fanyi.baidu.com/api/trans/vip/translate?" + "q=" + name + "&from=" + from + "&to=" + to + "&appid=" + APP_ID + "&salt=" + salt + "&sign=" + overSign;
                    UrlString = UrlString.replace(" ", "%20");
                    URL url = new URL(UrlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (MalformedURLException e) {
                    Log.e(TAG, "run: MalformedURLException:", e);
                } catch (IOException e) {
                    Log.e(TAG, "run: IOException:", e);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(TAG, "run: IOException", e);
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();

        voiceTextView=findViewById(R.id.fanyiBtn);
        voiceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech(name);
            }
        });
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                resultTranslationTextView.setText(response);
                Log.d(TAG, "run: response" + response);
                try {
                    Translate translate = new Translate();
                    //解析json
                    JSONObject jsonObject1 = new JSONObject(response);
                    String result1 = jsonObject1.getString("trans_result");
                    Log.d(TAG, "run: result" + result1);
                    //去掉
                    result1 = result1.replace("[", "");
                    result1 = result1.replace("]", "");
                    JSONObject jsonObject2 = new JSONObject(result1);
                    //设置文本
                    translate.setSrc(jsonObject2.getString("src"));
                    translate.setDst(jsonObject2.getString("dst"));
                    translationWords.setText(translate.getSrc());
                    translationChinese.setText(translate.getDst());
                } catch (JSONException e) {
                    Log.e(TAG, "run: JSONException", e);
                }
            }
        });
    }



    private void speech(String name) {
//1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(TranslateActivity.this, null);
//2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        mTts.setParameter(SpeechConstant.LANGUAGE, "en");
//设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
//保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
//如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
//3.开始合成
        mTts.startSpeaking(name, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }

    };
}