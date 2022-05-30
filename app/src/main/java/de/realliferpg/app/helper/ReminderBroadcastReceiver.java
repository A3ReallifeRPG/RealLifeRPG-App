package de.realliferpg.app.helper;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import de.realliferpg.app.R;
import de.realliferpg.app.activities.MainActivity;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyMaintenance")
                .setSmallIcon(R.drawable.realliferpg_logo)
                .setContentTitle(context.getResources().getString(R.string.str_notifications_reminder_maintenance_title))
                .setContentText(context.getResources().getString(R.string.str_notifications_reminder_maintenance_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        manager.notify(200, builder.build());
    }
}
