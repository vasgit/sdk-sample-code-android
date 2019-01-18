package org.qtproject.hexsudoku;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends MultiDexApplication {


    private static final String TAG = MyApplication.class.getName();
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.copyBundledRealmFile(this.getResources().openRawResource(R.raw.default_realm_v4),
                "default.realm",
                this.getFilesDir());

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.globaltracker_net);
            mTracker.enableAutoActivityTracking(false);
            mTracker.enableAdvertisingIdCollection(true);
        }
        return mTracker;
    }


}
