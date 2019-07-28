package org.qtproject.hexsudoku;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.io.File;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyApplication extends MultiDexApplication {


    private static final String TAG = MyApplication.class.getName();
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        final File default_realm = new File(this.getFilesDir() + "/default.realm");
        if (!default_realm.exists()) {
            Utils.copyBundledRealmFile(this.getResources().openRawResource(R.raw.default_realm_v4),
                    "default.realm", this.getFilesDir());
        }

        RealmMigration migration = new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                Log.d(TAG, "oldVersion = " + oldVersion);
                Log.d(TAG, "newVersion = " + newVersion);

                RealmSchema schema = realm.getSchema();
                if (oldVersion == 0) {
                    schema.get("UserRealm")
                            .addField("advertising_id", String.class)
                            .addField("IMEI_id", String.class)
                            .addField("ro_build_version_sdk", int.class)
                    ;
                    oldVersion++;
                }
            }
        };

        RealmConfiguration config = new RealmConfiguration
                .Builder(this)
                .schemaVersion(1) // Must be bumped when the schema changes
                .migration(migration) // Migration to run instead of throwing an exception
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
