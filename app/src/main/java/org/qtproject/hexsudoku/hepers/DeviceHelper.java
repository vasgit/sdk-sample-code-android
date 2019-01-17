package org.qtproject.hexsudoku.hepers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.genymotion.api.GenymotionManager;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class DeviceHelper {

    private static String TAG = DeviceHelper.class.getName();
    private Activity activity;

    public DeviceHelper(Activity activity) {
        this.activity = activity;
    }

    public void chengeGenymotionData() {
        GenymotionManager genymotion = GenymotionManager.getGenymotionManager(activity);
//        genymotion.getBattery().setLevel(50);

        getAndroidDeviseID();
        Log.d(TAG, "start change AndroidDeviseID");
//        genymotion.getId().setRandomAndroidId();
        getAndroidDeviseID();
    }

    @SuppressLint("HardwareIds")
    private void getAndroidDeviseID() {
        String androidDeviseID = "";
        try {
            androidDeviseID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.d(TAG, "androidDeviseID: " + androidDeviseID);
    }

    public void checngeGoogleAdvertisingID(final boolean needChecngeAdID) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(activity.getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try{
                    advertId = idInfo.getId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
                Log.d(TAG, "GoogleAdvertisingID: " + advertId);

                if (needChecngeAdID) {
                    Log.d(TAG, "start change GoogleAdvertisingID");
                    try {
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "rm -f /data/data/com.google.android.gms/shared_prefs/adid_settings.xml"});
                    } catch (Exception e) {
                        Log.d(TAG, "Exception", e);
                    }
                    checngeGoogleAdvertisingID(false);
                }
            }
        };
        task.execute();
    }

    public void chengeBuildProp() {


        File fileBuildProp = new File("/system/build.prop");
        if (fileBuildProp.exists()) {
            Log.d(TAG, "fileBuildProp exists");
            try {

//                File debugFile = new File("/system/build123.prop");
                File debugFile = new File(activity.getApplicationInfo().dataDir, "build.prop");
                BufferedWriter output = new BufferedWriter(new FileWriter(debugFile));

                BufferedReader br = new BufferedReader(new FileReader(fileBuildProp));
                try {

                    String line;
                    while ((line = br.readLine()) != null) {
                        Log.d(TAG, "line: " + line);
                        output.write(line);
                        output.newLine();

                        output.write("#vasvas");
                        output.newLine();
                    }
                } finally {
                    try {
                        output.close();
                        br.close();
                    } catch (Exception e) {
                        Log.e(TAG, "e:" + e.toString());
                    }
                }

                final String fileAbsolutePath = debugFile.getAbsolutePath();
                Log.e(TAG, "fileAbsolutePath: " + fileAbsolutePath);


                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec(new String[] {"su", "-c", "mv " + fileAbsolutePath + " /system/build.prop"});


                            Log.d(TAG, "mv is OK");
//                            /data/data/org.qtproject.hexsudoku/build.prop

//                            adb shell mv /data/data/org.qtproject.hexsudoku/build.prop /system/build.prop

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });

            } catch (Exception e) {
                Log.d(TAG, "Exception", e);
            }
        } else {
            Log.d(TAG, "fileBuildProp NOT exists");
        }
    }

}
