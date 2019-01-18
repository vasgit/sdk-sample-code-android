package org.qtproject.hexsudoku.hepers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.genymotion.api.GenymotionManager;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import org.qtproject.hexsudoku.R;
import org.qtproject.hexsudoku.Utils;
import org.qtproject.hexsudoku.constants.AppConstants;
import org.qtproject.hexsudoku.realmmodel.DeviceDataRealm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;


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
        Log.i(TAG, "androidDeviseID: " + androidDeviseID);
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
                Log.i(TAG, "GoogleAdvertisingID: " + advertId);

                if (needChecngeAdID) {
                    Log.d(TAG, "start change GoogleAdvertisingID");
                    try {
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "rm -f /data/data/com.google.android.gms/shared_prefs/adid_settings.xml"});
                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                    }
                    checngeGoogleAdvertisingID(false);
                }
            }
        };
        task.execute();
    }

    public void chengeBuildPropFile() {

        DeviceDataRealm device;

        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<DeviceDataRealm> sectionsRealms = mRealm.where(DeviceDataRealm.class)
                .equalTo("ro_build_version_sdk", android.os.Build.VERSION.SDK_INT)
                .findAll();
        if (sectionsRealms != null && !sectionsRealms.isEmpty()) {
            Random r = new Random();
            int i = r.nextInt(sectionsRealms.size() - 1);
            device = sectionsRealms.get(i);
        } else {
            return;
        }

        Utils.copyBundledRealmFile(activity.getResources().openRawResource(R.raw.build_prop),
                "build_in.prop",
                activity.getFilesDir());

        File inFile = new File(activity.getFilesDir(), "build_in.prop");
        if (inFile.exists()) {
            try {
                File outFile = new File(activity.getFilesDir(), "build_out.prop");
                BufferedWriter brOutFile = new BufferedWriter(new FileWriter(outFile));
                BufferedReader brInFile = new BufferedReader(new FileReader(inFile));
                try {
                    String line;
                    while ((line = brInFile.readLine()) != null) {
                        line = chengeLineBuildProp(line, device);
                        if (!line.contains("%%")) {
                            brOutFile.write(chengeLineBuildProp(line, device));
                            brOutFile.newLine();
                        }
                    }
                } finally {
                    try {
                        brOutFile.close();
                        brInFile.close();
                    } catch (Exception e) {
                        Log.e(TAG, "e:" + e.toString());
                    }
                }

                final String fileAbsolutePath = outFile.getAbsolutePath();
                Log.i(TAG, "fileAbsolutePath: " + fileAbsolutePath);

//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Runtime.getRuntime().exec(new String[]{"su", "-c", "mv " + fileAbsolutePath + " /system/build.prop"});
//
//                            Log.d(TAG, "mv is OK");
////                            /data/data/org.qtproject.hexsudoku/build.prop
////                            adb shell mv /data/data/org.qtproject.hexsudoku/build.prop /system/build.prop
//
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
//                        }
//                    }
//                });

            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        } else {
            Log.e(TAG, "inFile NOT exists");
        }
    }

    private String chengeLineBuildProp(String line, DeviceDataRealm device) {
        if (TextUtils.isEmpty(line)) {
            return line;
        }
        if (device.getRo_build_version_sdk() > 0 ) {
            line = line.replace("%%" + AppConstants.ro_build_version_sdk + "%",
                    String.valueOf(device.getRo_build_version_sdk()));
        }
        if (!TextUtils.isEmpty(device.getRo_build_version_release())) {
            line = line.replace("%%" + AppConstants.ro_build_version_release + "%",
                    device.getRo_build_version_release());
        }
        if (!TextUtils.isEmpty(device.getRo_build_id())) {
            line = line.replace("%%" + AppConstants.ro_build_id + "%",
                    device.getRo_build_id());
        }
        if (!TextUtils.isEmpty(device.getRo_build_display_id())) {
            line = line.replace("%%" + AppConstants.ro_build_display_id + "%",
                    device.getRo_build_display_id());
        }
        if (!TextUtils.isEmpty(device.getRo_build_version_incremental())) {
            line = line.replace("%%" + AppConstants.ro_build_version_incremental + "%",
                    device.getRo_build_version_incremental());
        }
        if (!TextUtils.isEmpty(device.getRo_build_date())) {
            line = line.replace("%%" + AppConstants.ro_build_date + "%",
                    device.getRo_build_date());
        }
        if (!TextUtils.isEmpty(device.getRo_build_date_utc())) {
            line = line.replace("%%" + AppConstants.ro_build_date_utc + "%",
                    device.getRo_build_date_utc());
        }
        if (!TextUtils.isEmpty(device.getRo_build_user())) {
            line = line.replace("%%" + AppConstants.ro_build_user + "%",
                    device.getRo_build_user());
        }
        if (!TextUtils.isEmpty(device.getRo_build_host())) {
            line = line.replace("%%" + AppConstants.ro_build_host + "%",
                    device.getRo_build_host());
        }
        if (!TextUtils.isEmpty(device.getRo_build_flavor())) {
            line = line.replace("%%" + AppConstants.ro_build_flavor + "%",
                    device.getRo_build_flavor());
        }
        if (!TextUtils.isEmpty(device.getRo_product_model())) {
            line = line.replace("%%" + AppConstants.ro_product_model + "%",
                    device.getRo_product_model());
        }
        if (!TextUtils.isEmpty(device.getRo_product_brand())) {
            line = line.replace("%%" + AppConstants.ro_product_brand + "%",
                    device.getRo_product_brand());
        }
        if (!TextUtils.isEmpty(device.getRo_product_name())) {
            line = line.replace("%%" + AppConstants.ro_product_name + "%",
                    device.getRo_product_name());
        }
        if (!TextUtils.isEmpty(device.getRo_product_device())) {
            line = line.replace("%%" + AppConstants.ro_product_device + "%",
                    device.getRo_product_device());
        }
        if (!TextUtils.isEmpty(device.getRo_product_board())) {
            line = line.replace("%%" + AppConstants.ro_product_board + "%",
                    device.getRo_product_board());
        }
        if (!TextUtils.isEmpty(device.getRo_product_manufacturer())) {
            line = line.replace("%%" + AppConstants.ro_product_manufacturer + "%",
                    device.getRo_product_manufacturer());
        }
        if (!TextUtils.isEmpty(device.getRo_product_locale_language())) {
            line = line.replace("%%" + AppConstants.ro_product_locale_language + "%",
                    device.getRo_product_locale_language());
        }
        if (!TextUtils.isEmpty(device.getRo_product_locale_region())) {
            line = line.replace("%%" + AppConstants.ro_product_locale_region + "%",
                    device.getRo_product_locale_region());
        }
        if (!TextUtils.isEmpty(device.getRo_build_description())) {
            line = line.replace("%%" + AppConstants.ro_build_description + "%",
                    device.getRo_build_description());
        }
        if (!TextUtils.isEmpty(device.getRo_build_fingerprint())) {
            line = line.replace("%%" + AppConstants.ro_build_fingerprint + "%",
                    device.getRo_build_fingerprint());
        }

        return line;
    }

}
