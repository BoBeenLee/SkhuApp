package com.skhu.bobinlee.skhuapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**w
 * Created by BoBinLee on 2014-09-06.
 */
public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 서비스 할게 있을 때만
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Intent serviceIntent = new Intent(context, MainActivity.class);
//            context.startService(serviceIntent);
        }
    }
}