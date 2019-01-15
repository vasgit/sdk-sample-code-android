package org.qtproject.hexsudoku.hepers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.genymotion.api.GenymotionManager;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;


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

}
