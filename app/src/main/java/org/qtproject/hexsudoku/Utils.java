package org.qtproject.hexsudoku;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.qtproject.hexsudoku.constants.AppConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

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
            Process proc = Runtime.getRuntime().exec(new String[] {AppConstants.NAME_SU, "-c", "reboot" });
            proc.waitFor();
        } catch (Exception ex) {
            Log.i(TAG, "Could not reboot: ", ex);
        }
    }

    public static void copyBundledRealmFile(InputStream inputStream, String outFileName, File tempAppDir) {
        try {
            File file = new File(tempAppDir, outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
