package org.qtproject.hexsudoku.hepers;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import org.qtproject.hexsudoku.constants.AppConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

class ClickHelper {

    private Activity activity;

    private static final String BACK_SCRIPT = "backScript.sh";

    ClickHelper(Activity activity) {
        this.activity = activity;
    }

    void createScriptFile() {
        try {
            if (isExternalStorageWritable()) {
                File debugFile = new File(Environment.getExternalStorageDirectory(), BACK_SCRIPT);
                BufferedWriter output = new BufferedWriter(new FileWriter(debugFile));

                try {
                    Log.d(AppConstants.TOTAL_TAG, "input keyevent 4");

                } finally {
                    try {
                        output.close();
                        Log.d(AppConstants.TOTAL_TAG, "createScriptFile: ok");
                        Log.d(AppConstants.TOTAL_TAG, "Environment.getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory());
                    } catch (Exception e) {
                        Log.e(AppConstants.TOTAL_TAG, "e:" + e.toString());
                    }
                }
            } else {
                Log.e(AppConstants.TOTAL_TAG, "Debug File: Storage not writable");
            }
        } catch (Exception e) {
            Log.e(AppConstants.TOTAL_TAG, "Exception", e);
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
