package com.skhu.bobinlee.skhuapp.util;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.skhu.bobinlee.skhuapp.R;
import com.skhu.bobinlee.skhuapp.activity.HomeActivity;
import com.skhu.bobinlee.skhuapp.activity.MainActivity;
import com.skhu.bobinlee.skhuapp.activity.QnaActivity;
import com.skhu.bobinlee.skhuapp.model.DBType;


public class Notifier {
    private static int notify_id = 0;

    private final Context _context;

    public Notifier(Context context) throws Exception {
        if (context == null) throw new Exception("Notifer context is null");
        _context = context;
    }

    public void ShowToast(String text, int duration){
        Toast.makeText(_context, text, duration).show();
    }

    public void ShowToast(String text) {
        ShowToast(text, Toast.LENGTH_SHORT);
    }

    public void ShowDialog(String title, String message){
        new AlertDialog.Builder(_context)
                .setMessage(message)
                .setTitle(title)
                .setNeutralButton("Close", null)
                .show();
    }

    //TODO Other Dialogs methods
    public void Notify(String title, String message, String url, int dbType){
        NotificationManager notifManager = (NotificationManager) _context.getSystemService(_context.NOTIFICATION_SERVICE);

        // 해당 링크로 이동으로 하는게
        PendingIntent intent = PendingIntent.getActivity(_context, 0, new Intent(Intent.ACTION_VIEW, Uri.parse(url)), 0);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(_context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(intent)
                .setSound(soundUri)
                .setVibrate(new long[]{1000});

        mBuilder.setNumber(1);
        notifManager.notify(notify_id++, mBuilder.build());
//        Notification notifyDetails = new Notification(R.drawable.alarm,intent.getExtras().getString(KEY_TITLE),System.currentTimeMillis());
//        notifyDetails.number = 1; ////// here you can pass the counter value which will so you th
    }
}
