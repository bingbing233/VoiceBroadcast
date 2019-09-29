package com.bing.voicebroadcast;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    Context context;
    List<App>apps;

    public AppAdapter(List<App>apps, Context context) {
        super();
        this.apps = apps;
        this.context = context;
    }

    @NonNull
    @Override
    public AppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AppAdapter.ViewHolder viewHolder, int i) {
        SharedPreferences pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        final App app = apps.get(i);
        viewHolder.appNameText.setText(apps.get(i).getAppName());
        viewHolder.packageNameText.setText(apps.get(i).getAppPackageName());
        viewHolder.imageView.setImageDrawable(apps.get(i).getAppIcon());
        viewHolder.checkBox.setChecked(pref.getBoolean(app.getAppPackageName(),false));
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
                app.setChecked(!app.getChecked());
                editor.putBoolean(app.getAppPackageName(),app.getChecked());
                editor.apply();
                viewHolder.checkBox.setChecked(app.getChecked());
                //notifyDataSetChanged();
            }
        });
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView packageNameText;
        TextView appNameText;
        CheckBox checkBox;
        View item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView;
            checkBox = itemView.findViewById(R.id.checkbox);
            imageView = itemView.findViewById(R.id.app_icon);
            packageNameText = itemView.findViewById(R.id.package_name);
            appNameText = itemView.findViewById(R.id.app_name);

        }
    }
}
