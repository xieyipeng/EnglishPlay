package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.bean.Word;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
显示单词的具体细节
 */
public class DetailActivity extends AppCompatActivity {

    private Word word = new Word();
    private String resultUrl;
    public static final String TAG = "DetailActivity";
    public String NAME;
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        NAME = intent.getStringExtra("name");

        Log.e("parseXMLWithPull", "onCreate: " + NAME);
        word.setKey(NAME);
        init();
    }

    public void init() {
        final TextView detail_tv1 = (TextView) findViewById(R.id.input);
        final TextView detail_tv2 = (TextView) findViewById(R.id.translation);
        final TextView detail_tv3 = (TextView) findViewById(R.id.webmeans);
        final TextView detail_tv4 = (TextView) findViewById(R.id.means);
        backImageView = findViewById(R.id.login_reback_btn);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        resultUrl = "http://dict-co.iciba.com/api/dictionary.php";
        new Thread(new Runnable() {
            @Override
            public void run() {

                String xmlData = HttpRequest.sendGet(resultUrl, "w=" + NAME + "&key=05C4F33DDAE7C18C6C24259B7C289136");
                detail_tv1.post(new Runnable() {
                    @Override
                    public void run() {
                        detail_tv1.setText(NAME);
                    }
                });

                /**
                 * 设置音标
                 * 正则表达式
                 */
                List<String> S_ps = new ArrayList<>();
                final String ps = "<ps>(.*?)</ps>";
                Pattern P_ps = Pattern.compile(ps);
                Matcher m1 = P_ps.matcher(xmlData);
                while (m1.find()) {
                    S_ps.add(m1.group());
                }
                //多余的字符串
                final int otherChar1 = 4;
                S_ps = stringSub(S_ps, otherChar1);
                if (S_ps.size() == 1) {
                    final String ps1 = "[" + S_ps.get(0) + "]";
                    detail_tv2.post(new Runnable() {
                        @Override
                        public void run() {
                            detail_tv2.setText(ps1);
                        }
                    });
                } else if (S_ps.size() == 2) {
                    final String c = "美：[" + S_ps.get(0) + "]\n英：[" + S_ps.get(1) + "]";
                    detail_tv2.post(new Runnable() {
                        @Override
                        public void run() {
                            detail_tv2.setText(c);
                        }
                    });
                }

                /**
                 * 注释
                 */
                List<String> S_pos = new ArrayList<>();
                List<String> S_acceptation = new ArrayList<>();
                final String pos = "<pos>(.*?)</pos>";
                final String acceptation = "<acceptation>(.*?)</acceptation>";
                Pattern P_pos = Pattern.compile(pos);
                Pattern P_acceptation = Pattern.compile(acceptation);

                Matcher m2 = P_pos.matcher(xmlData);
                while (m2.find()) {
                    S_pos.add(m2.group());
                }
                Matcher m3 = P_acceptation.matcher(xmlData);
                while (m3.find()) {
                    S_acceptation.add(m3.group());
                }
                final int otherChar2 = 5;
                final int otherChar3 = 13;
                S_pos = stringSub(S_pos, otherChar2);
                S_acceptation = stringSub(S_acceptation, otherChar3);
                String result = null;
                Log.e(TAG, "run: S: " + S_pos + "\n" + S_acceptation);
                Log.e(TAG, "run: size: " + S_pos.size() + "\n" + S_acceptation.size());
                if (!S_pos.get(0).equals("") && !S_acceptation.get(0).equals("")) {
                    Log.e(TAG, "run: time 1");
                    if (S_acceptation.size() == S_pos.size()) {
                        for (int i = 0; i < S_acceptation.size(); i++) {
                            result = result + S_pos.get(i) + "\n" + S_acceptation.get(i);
                            if (i != S_acceptation.size() - 1) {
                                result = result + "\n" + "\n";
                            }
                            Log.e(TAG, "run result : " + result);
                        }
                        assert result != null;
                        result = result.substring(4);
                    }
                    final String a = result;
                    detail_tv3.post(new Runnable() {
                        @Override
                        public void run() {
                            detail_tv3.setText(a);
                        }
                    });
                } else if (!S_acceptation.get(0).equals("")) {
                    Log.e(TAG, "run: time 2");
                    Log.e(TAG, "run: time a: " + S_acceptation.get(0));
                    final String a = S_acceptation.get(0);
                    detail_tv3.post(new Runnable() {
                        @Override
                        public void run() {
                            detail_tv3.setText(a);
                        }
                    });
                }

                /**
                 * 例句
                 */
                List<String> S_orig = new ArrayList<>();
                List<String> S_trans = new ArrayList<>();
                String orig = "<orig>(.*?)</orig>";
                String trans = "<trans>(.*?)</trans>";
                Pattern P_orig = Pattern.compile(orig);
                Pattern P_trans = Pattern.compile(trans);
                Matcher m4 = P_orig.matcher(xmlData);
                while (m4.find()) {
                    S_orig.add(m4.group());
                }
                Matcher m5 = P_trans.matcher(xmlData);
                while (m5.find()) {
                    S_trans.add(m5.group());
                }
                final int otherChar4 = 6;
                final int otherChar5 = 7;
                S_orig = stringSub(S_orig, otherChar4);
                S_trans = stringSub(S_trans, otherChar5);
                String resultTrans = null;
                if (!S_orig.get(0).equals("") && !S_trans.get(0).equals("")) {
                    if (S_trans.size() == S_orig.size()) {
                        for (int i = 0; i < S_trans.size(); i++) {
                            resultTrans = resultTrans + S_orig.get(i) + "\n" + S_trans.get(i);
                            if (i != S_trans.size() - 1) {
                                resultTrans = resultTrans + "\n" + "\n";
                            }
                        }
                        assert resultTrans != null;
                        //去掉resultTrans的开头null
                        resultTrans = resultTrans.substring(4);
                        Log.e(TAG, "run: resultTrans: " + resultTrans);
                    }
                }
                final String b = resultTrans;
                detail_tv4.post(new Runnable() {
                    @Override
                    public void run() {
                        detail_tv4.setText(b);
                    }
                });
//
            }

            /**
             * 截取"<ps>(.*?)</ps>"中的<ps>...</ps>
             * @param list 通过从xmlData中匹配到的所有字符串
             * @param number "<ps>(.*?)</ps>"中像"<ps>","</ps>"的多余字符串
             * @return 没有多余字符串的list
             */
            private List<String> stringSub(List<String> list, int number) {      //number == 4
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, list.get(i).substring(number, list.get(i).length() - number - 1));
                }
                return list;
            }

        }).start();
    }
//    private void parseXMLWithPull(String xmlData) {
//
//        Log.e(TAG, "parseXMLWithPull: xmlData: " + xmlData);
//        List<String> S_ps = new ArrayList<>();                //音标
//        String ps = "<ps>(.*)</ps>";
//        Pattern P_ps = Pattern.compile(ps);
//        Matcher m1 = P_ps.matcher(xmlData);
//        while (m1.find()) {
//            S_ps.add(m1.group());
//            Log.d(TAG, "parseXMLWithPull: time+1");
//        }
//        Log.e(TAG, "parseXMLWithPull: m1.find: " + S_ps);
//        if (S_ps.get(0) != null) {
//            xmlData = xmlData.replace(S_ps.get(0), "");
//        }
//        Log.e(TAG, "parseXMLWithPull: xmlData: " + xmlData);
//
//        Matcher m2 = P_ps.matcher(xmlData);
//        while (m2.find()) {
//            S_ps.add(m2.group());
//        }
//        if (S_ps.get(1) != null) {
//            xmlData = xmlData.replace(S_ps.get(1), "");
//        }
//        Log.e(TAG, "parseXMLWithPull: " + S_ps);
//        for (int i = 0; i < S_ps.size(); i++) {
//            S_ps.get(i).replace("<ps>", "");
//            S_ps.get(i).replace("</ps>", "");
//        }
//        detail_tv2.setText("美：[" + S_ps.get(0) + "]\n英：[" + S_ps.get(1) + "]");
//
//
//        List<String> S_pos = new ArrayList<>();               //注释
//        List<String> S_acceptation = new ArrayList<>();
//        String pos = "<pos>(.*)</pos>";
//        String acceptation = "<acceptation>(.*?)</acceptation>";
//        Pattern P_pos = Pattern.compile(pos);
//        Pattern P_acceptation = Pattern.compile(acceptation);
//        Matcher m3 = P_pos.matcher(xmlData);
//        while (m3.find()) {
//            S_pos.add(m3.group());
//        }
//        if (S_pos.get(0) != null) {
//            xmlData = xmlData.replace(S_pos.get(0), "");
//        }
//        Matcher m4 = P_pos.matcher(xmlData);
//        while (m4.find()) {
//            S_pos.add(m4.group());
//        }
//        if (S_pos.get(1) != null) {
//            xmlData = xmlData.replace(S_pos.get(1), "");
//        }
//        Matcher m5 = P_pos.matcher(xmlData);
//        while (m5.find()) {
//            S_pos.add(m5.group());
//        }
//        if (S_pos.get(2) != null) {
//            xmlData = xmlData.replace(S_pos.get(2), "");
//        }
//
//        Matcher m6 = P_acceptation.matcher(xmlData);
//        while (m6.find()) {
//            S_acceptation.add(m6.group());
//        }
//        if (S_acceptation.get(0) != null) {
//            xmlData = xmlData.replace(S_acceptation.get(0), "");
//        }
//        Matcher m7 = P_acceptation.matcher(xmlData);
//        while (m7.find()) {
//            S_acceptation.add(m7.group());
//        }
//        if (S_acceptation.get(1) != null) {
//            xmlData = xmlData.replace(S_acceptation.get(1), "");
//        }
//        Matcher m8 = P_acceptation.matcher(xmlData);
//        while (m8.find()) {
//            S_acceptation.add(m8.group());
//        }
//        if (S_acceptation.get(2) != null) {
//            xmlData = xmlData.replace(S_acceptation.get(2), "");
//        }
//        for (int i = 0; i < S_pos.size(); i++) {
//            S_pos.get(i).replace("<pos>", "");
//            S_pos.get(i).replace("</pos>", "");
//        }
//        for (int i = 0; i < S_acceptation.size(); i++) {
//            S_acceptation.get(i).replace("<acceptation>", "");
//            S_acceptation.get(i).replace("</acceptation>", "");
//        }
//        if (S_acceptation.size() == S_pos.size()) {
//            for (int i = 0; i < S_acceptation.size(); i++) {
//                resultText.append(S_pos.get(i) + "\n" + S_acceptation.get(i));
//                if (i != S_acceptation.size() - 1) {
//                    resultText.append("\n");
//                }
//            }
//        }
//        detail_tv3.setText(resultText);
//
//
//        List<String> S_orig = new ArrayList<>();              //例句
//        List<String> S_trans = new ArrayList<>();
//        String orig = "<orig>(.*)</orig>";
//        String trans = "<trans>(.*)</trans>";
//        Pattern P_orig = Pattern.compile(orig);
//        Pattern P_trans = Pattern.compile(trans);
//
//        Matcher m9 = P_orig.matcher(xmlData);
//        while (m9.find()) {
//            S_orig.add(m9.group());
//        }
//        if (S_orig.get(0) != null) {
//            xmlData = xmlData.replace(S_orig.get(0), "");
//        }
//        Matcher m10 = P_orig.matcher(xmlData);
//        while (m10.find()) {
//            S_orig.add(m10.group());
//        }
//        if (S_orig.get(1) != null) {
//            xmlData = xmlData.replace(S_orig.get(1), "");
//        }
//        Matcher m11 = P_orig.matcher(xmlData);
//        while (m11.find()) {
//            S_orig.add(m11.group());
//        }
//        if (S_orig.get(2) != null) {
//            xmlData = xmlData.replace(S_orig.get(2), "");
//        }
//
//        Matcher m12 = P_trans.matcher(xmlData);
//        while (m12.find()) {
//            S_trans.add(m12.group());
//        }
//        if (S_trans.get(0) != null) {
//            xmlData = xmlData.replace(S_trans.get(0), "");
//        }
//        Matcher m13 = P_trans.matcher(xmlData);
//        while (m13.find()) {
//            S_trans.add(m13.group());
//        }
//        if (S_trans.get(1) != null) {
//            xmlData = xmlData.replace(S_trans.get(1), "");
//        }
//        Matcher m14 = P_trans.matcher(xmlData);
//        while (m14.find()) {
//            S_trans.add(m14.group());
//        }
//        if (S_trans.get(2) != null) {
//            xmlData = xmlData.replace(S_trans.get(2), "");
//        }
//        for (int i = 0; i < S_orig.size(); i++) {
//            S_orig.get(i).replace("<orig>", "");
//            S_orig.get(i).replace("</orig>", "");
//        }
//        for (int i = 0; i < S_trans.size(); i++) {
//            S_trans.get(i).replace("<trans>", "");
//            S_trans.get(i).replace("</trans>", "");
//        }
//
//        if (S_trans.size() == S_orig.size()) {
//            for (int i = 0; i < S_trans.size(); i++) {
//                resultText.append(S_orig.get(i) + "\n" + S_trans.get(i));
//                if (i != S_trans.size() - 1) {
//                    resultText.append("\n");
//                }
//            }
//        }
//        detail_tv4.setText(resultText);
////        List<String> S_pron = new ArrayList<String>();
////        String pron = "<pron>(.*?)</pron>";
////        Pattern P_pron = Pattern.compile(pron);
//    }
}
