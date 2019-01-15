package org.qtproject.hexsudoku;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

class Utils {

    private static String TAG = Utils.class.getName();
    private Activity activity;

    Utils(Activity activity) {
        this.activity = activity;
    }

    void initGA() {
        MyApplication application = (MyApplication) activity.getApplication();
        Tracker mTracker = application.getDefaultTracker();

        mTracker.setScreenName("onCreate");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("category_vas").setAction("action_vas").setLabel("label_vas").build());
    }

    void rebootPhone() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
            proc.waitFor();
        } catch (Exception ex) {
            Log.i(TAG, "Could not reboot: ", ex);
        }
    }


}
