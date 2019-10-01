package com.bing.voicebroadcast;

import android.app.Notification;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;
import java.util.Locale;

public class MyNotifyService extends NotificationListenerService  {

    String title="";
    String []content;
    String finalContent="";
    TextToSpeech textToSpeech;
    private static final String TAG = MyNotifyService.class.getSimpleName();

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d(TAG, "onNotificationRemove");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
        Notification notification = sbn.getNotification();
        Log.d("packageName",sbn.getPackageName());
        if (notification == null) {

            return;
        }
        //标题
        if (notification.tickerText != null) {
            title = notification.tickerText.toString();
            content = title.split(":");
            finalContent = content[0]+"发来消息"+content[1];
        }
        //播报
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean ifShowToast = pref.getBoolean("toast",false);
        if(ifShowToast){
            Toast.makeText(this,title,Toast.LENGTH_SHORT).show();
        }
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //判断是否播报
        if(preferences1.getBoolean("startService",false)){
            //判断是否添加播报
            if(preferences.getBoolean(sbn.getPackageName(),false)){
                initTTS();
            }
        }



    }

    private void initTTS() {
        //实例化自带语音对象
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
                        Toast.makeText(MyNotifyService.this, "暂不支持这种语言的朗读", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }
                // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                textToSpeech.setPitch(1.0f);
                // 设置语速
                textToSpeech.setSpeechRate(0.3f);
                textToSpeech.speak(finalContent,//输入中文，若不支持的设备则不会读出来
                        TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}

