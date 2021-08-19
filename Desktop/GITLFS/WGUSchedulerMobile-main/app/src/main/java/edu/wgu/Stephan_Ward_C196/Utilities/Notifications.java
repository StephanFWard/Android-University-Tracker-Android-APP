package edu.wgu.Stephan_Ward_C196.Utilities;

import android.content.Intent;
import android.os.Build;
import android.content.SharedPreferences;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.NotificationCompat;
import edu.wgu.Stephan_Ward_C196.R;
import edu.wgu.Stephan_Ward_C196.Activity.HomePage;
import android.app.NotificationChannel;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.content.Context;
import android.content.BroadcastReceiver;

/**
 * Notifications that are used throughout the application.
 * @author Stephan Ward
 * @since 07/15/2021
 */
//Notifications that extent the base class code for the broad cat receiver
public class Notifications extends BroadcastReceiver {
    public static final String alertFile = "alertFile";
    //Consider refactoring these codes as needed
    //The Example: public static final termAlertFile = "termAlertFile";
    //Also the Example: .......string courseAlertFile = "courseAlertFile".
    //Next Alert is called and the channel ID
    public static final String nextAlert = "nextAlertID";
    public static final String Channel_ID = "WGU C196";
    //Sets the alert
    public static void setAlert(Context context, int ID, long time, String title, String text) {
        //Alarm manager is used to receive the intent of time
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int nextAlertID = getNextAlertID(context);
        //Finds alert intent and context
        Intent intentAlert = new Intent(context, Notifications.class);
        //Extends the content to the notification class.
        intentAlert.putExtra("ID", ID);
        intentAlert.putExtra("title", title);
        intentAlert.putExtra("text", text);
        intentAlert.putExtra("nextAlertID", nextAlertID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, nextAlertID, intentAlert, PendingIntent.FLAG_UPDATE_CURRENT);
        //Wakes the device in UTC time
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        //Calls the alert file
        SharedPreferences sp = context.getSharedPreferences(alertFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Integer.toString(ID), nextAlertID);
        editor.apply();
        incrementNextAlertID(context);
    }

    /**
     * Sets the assessment alert
     * @param context The context of the assessment alert
     * @param ID The assessment ID
     * @param time The assessment alert time
     * @param title The assessment alert title
     * @param text The assessment alert text
     */
    public static void setAssessmentAlert(Context context, int ID, long time, String title, String text) {
        setAlert(context, ID, time, title, text);
    }

    /**
     * Sets for the course alert
     * @param context The alert Context
     * @param ID The alert ID
     * @param time The alert time
     * @param title The alert title
     * @param text The alert text
     */
    public static void setCourseAlert(Context context, int ID, long time, String title, String text) {
        setAlert(context, ID, time, title, text);
    }

    /**
     * Increments up to next alert ID
     * @param context Context preferences for next alert
     */
    private static void incrementNextAlertID(Context context) {
        SharedPreferences alertPrefs;
        alertPrefs = context.getSharedPreferences(alertFile, Context.MODE_PRIVATE);
        int nextAlertID = alertPrefs.getInt(nextAlert, 1);
        SharedPreferences.Editor alertEditor = alertPrefs.edit();
        alertEditor.putInt(nextAlert, nextAlertID + 1);
        alertEditor.apply();
    }


    /**
     * Gets next Alert ID
     * @param context Context of the alert ID
     * @return Next alert and Preferences
     */
    private static int getNextAlertID(Context context) {
        SharedPreferences alertPrefs;
        alertPrefs = context.getSharedPreferences(alertFile, Context.MODE_PRIVATE);
        return alertPrefs.getInt(nextAlert, 1);
    }

    /**
     * Receive notice method channel for the alerts
     * @param context Context of the notification channel
     * @param intent The intent of the notification channel
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //Alert title intent string
        String alertTitle = intent.getStringExtra("title");
        //Alert text intent string
        String alertText = intent.getStringExtra("text");
        //Alert next alert ID intent integer
        int nextAlertID = intent.getIntExtra("nextAlertID", getAndIncrementNextAlertID(context));
        //The Notification Channel context channel to be adopted and modified
        createNotificationChannel(context);
        //When tapped provides new action to the history stack
        Intent destination = new Intent(context, HomePage.class);
        destination.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{destination}, 0);
        //A builder for the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Channel_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground1)
                .setContentTitle(alertTitle)
                .setContentText(alertText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        //Calls the notification compatibility library for older android versions.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(nextAlertID, builder.build());
    }

    /**
     * Gets and Increments up to the next alert ID
     * @param context Context of the next alert ID
     * @return The alarm ID
     */
    private static int getAndIncrementNextAlertID(Context context) {
        int nextAlarmID = getNextAlertID(context);
        incrementNextAlertID(context);
        return nextAlarmID;
    }

    /**
     * Creates the notification channel to be used.
     * @param context Defaults of the notification Channel
     */
    private void createNotificationChannel(Context context) {
        //Applies the build version codes of the SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Channel name
            CharSequence name = "channel";
            //Description of the channel
            String description = "channel description";
            //Default importance is set to 3
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //Calls the WGU C196 notification channel ID
            NotificationChannel channel = new NotificationChannel(Notifications.Channel_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            //Calls the notification compatibility library for older android versions.
            notificationManager.createNotificationChannel(channel);
        }
    }
}
