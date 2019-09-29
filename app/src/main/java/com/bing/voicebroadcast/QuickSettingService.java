package com.bing.voicebroadcast;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickSettingService extends TileService {

    @Override
    public void onClick() {
        super.onClick();
       int state = getQsTile().getState();
        if (state == Tile.STATE_ACTIVE) {
            getQsTile().setState(Tile.STATE_INACTIVE);// 更改成非活跃状态
            stopService(new Intent(getApplicationContext(),MyNotifyService.class));
        } else {


            getQsTile().setState(Tile.STATE_ACTIVE);//更改成活跃状态
            startService(new Intent(getApplicationContext(),MyNotifyService.class));
        }

        getQsTile().updateTile();//更新Tile
    }
}
