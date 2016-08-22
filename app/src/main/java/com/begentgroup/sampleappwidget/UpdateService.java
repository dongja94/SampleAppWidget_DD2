package com.begentgroup.sampleappwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;

public class UpdateService extends Service {
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    AppWidgetManager mAWM;
    @Override
    public void onCreate() {
        super.onCreate();
        mAWM = AppWidgetManager.getInstance(this);
        mHandler.post(updateRunnable);
    }

    Handler mHandler = new Handler(Looper.getMainLooper());

    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            ComponentName cn = new ComponentName(UpdateService.this, MyAppWidget.class);
            int[] appWidgetIds = mAWM.getAppWidgetIds(cn);
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(UpdateService.this, mAWM, appWidgetId);
                }

                count++;
            }
            mHandler.postDelayed(this, 2000);
        }
    };

    int count = 0;

    private static final int[] IMAGE_IDS = {
            R.drawable.gallery_photo_1,
            R.drawable.gallery_photo_2,
            R.drawable.gallery_photo_3,
            R.drawable.gallery_photo_4,
            R.drawable.gallery_photo_5,
            R.drawable.gallery_photo_6,
            R.drawable.gallery_photo_7,
            R.drawable.gallery_photo_8,
    };
    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        views.setTextViewText(R.id.text_title, "MyAppWidget : " + count);
        views.setImageViewResource(R.id.image_icon, IMAGE_IDS[count % IMAGE_IDS.length]);

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.image_icon, pi);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
