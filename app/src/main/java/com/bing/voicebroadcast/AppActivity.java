package com.bing.voicebroadcast;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AppActivity extends AppCompatActivity {

    List<App> apps = new ArrayList<App>();
    RecyclerView recyclerView;
    AppAdapter appAdapter;
    Handler handler;
    ProgressDialog progressDialog;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        recyclerView = findViewById(R.id.recyclerView);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("请稍等");
        progressDialog.setMessage("正在加载应用列表");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new MyThread().start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.cancel();
                appAdapter = new AppAdapter(apps, context);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(appAdapter);
            }
        };


    }

    //耗时操作放在子线程
    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //非系统应用
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    App app = new App();
                    app.setAppPackageName(packageInfo.packageName);
                    app.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                    app.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
                    app.setChecked(false);
                    apps.add(app);
                }

            }
            Message message = new Message();
            handler.sendMessage(message);
        }
    }

}

