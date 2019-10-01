package com.bing.voicebroadcast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingFragment extends PreferenceFragment {

    Preference preference;
    SwitchPreference switchPreference;
    SwitchPreference startService;

    public SettingFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        //设置偏好
        final SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //开启服务
        final Intent intent = new Intent(getActivity().getApplicationContext(),MyNotifyService.class);
        getActivity().startService(intent);
        //实例化偏好
        preference = findPreference("permission");
        switchPreference =(SwitchPreference) findPreference("toast");
        startService = (SwitchPreference) findPreference("startService");
        //通知权限
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent1 = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivity(intent1);
                return false;
            }
        });
    }
}
