package de.realliferpg.app.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import de.realliferpg.app.R;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyMaintenance")
                .setSmallIcon(R.drawable.realliferpg_logo)
                .setContentTitle(context.getResources().getString(R.string.str_notifications_reminder_maintenance_title))
                .setContentText(context.getResources().getString(R.string.str_notifications_reminder_maintenance_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        manager.notify(200, builder.build());
    }
}
