package org.qtproject.hexsudoku;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

class ClickHelper {

    private static String TAG = ClickHelper.class.getName();
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
                    output.write("input keyevent 4");

                } finally {
                    try {
                        output.close();
                        Log.d(TAG, "createScriptFile: ok");
                        Log.d(TAG, "Environment.getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory());
                    } catch (Exception e) {
                        Log.e(TAG, "e:" + e.toString());
                    }
                }
            } else {
                Log.d(TAG, "Debug File: Storage not writable");
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception", e);
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
