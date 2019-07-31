package org.qtproject.hexsudoku.hepers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.genymotion.api.GenymotionManager;
import com.genymotion.genyd.IGenydService;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import org.qtproject.hexsudoku.R;
import org.qtproject.hexsudoku.Utils;
import org.qtproject.hexsudoku.constants.AppConstants;
import org.qtproject.hexsudoku.realmmodel.DeviceDataRealm;
import org.qtproject.hexsudoku.realmmodel.UserRealm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;


public class DeviceHelper {

    private Activity activity;
    private GenymotionManager genymotion;
    private UserRealm userRealm;
    private DeviceDataRealm deviceDataRealm;
    private IGenydService iGenydService;


    public DeviceHelper(Activity activity) {
        this.activity = activity;
    }

    public void changeDevice(UserRealm userRealm) {
        this.userRealm = userRealm;
        deviceDataRealm = userRealm.getDevice();
        genymotion = GenymotionManager.getGenymotionManager(activity.getApplicationContext());

        try {
            iGenydService = (IGenydService) Utils.getObjectByName(genymotion.getRadio(), "genyd", false, 0);
        } catch (Exception e) {
            Log.e(AppConstants.TOTAL_TAG, e.toString());
        }

        Log.d(AppConstants.TOTAL_TAG, "//--" );
        Log.d(AppConstants.TOTAL_TAG, "NEED change USER to:");
        Log.d(AppConstants.TOTAL_TAG, "PoverLevel: random");
        Log.d(AppConstants.TOTAL_TAG, "AndroidID: " + userRealm.getAndroid_id());
        Log.d(AppConstants.TOTAL_TAG, "AdvertisingId: " + userRealm.getAdvertising_id());
        Log.d(AppConstants.TOTAL_TAG, "IMEI: " + userRealm.getIMEI_id());
        Log.d(AppConstants.TOTAL_TAG, "-" );

        Log.d(AppConstants.TOTAL_TAG, "CURRENT User is:");
        getPoverLevel(AppConstants.CURRENT);
        getAndroidID(AppConstants.CURRENT, null);
        getAdvertisingId(AppConstants.CURRENT, null);
        getIMEI(AppConstants.CURRENT, null);

        setPoverLevel();
        setAndroidID();
        setAdvertisingId();
        setIMEI();
        Log.d(AppConstants.TOTAL_TAG, "-" );

        Handler uiHandler1 = new Handler(Looper.getMainLooper());
        uiHandler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkChangedUserData();
            }
        }, 900);

//        todo
//        changeBuildPropFile();
    }

    private void checkChangedUserData() {
        Log.d(AppConstants.TOTAL_TAG, "-" );
        Log.d(AppConstants.TOTAL_TAG, "START CHECK: " );
        getAndroidID(AppConstants.CURRENT, userRealm.getAndroid_id());
        getAdvertisingId(AppConstants.CURRENT, userRealm.getAdvertising_id());
        getIMEI(AppConstants.CURRENT, userRealm.getIMEI_id());
    }

    public int getPoverLevel(String tag) {
        int poverLevel = 0;
        try {
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = activity.registerReceiver(null, iFilter);

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            float batteryPct = level / (float) scale;

            poverLevel = (int) (batteryPct * 100);
        } catch (Exception e1) {
            Log.d(AppConstants.TOTAL_TAG, e1.toString());
        }
        Log.d(AppConstants.TOTAL_TAG, tag + " getPoverLevel: " + poverLevel);
        return poverLevel;
    }

    private void setPoverLevel() {
        //poverLevel 15% - 100%
        Random r = new Random();
        int poverLevel = r.nextInt(100 - 15) + 15;
        genymotion.getBattery().setLevel(poverLevel);
    }

    @SuppressLint("HardwareIds")
    private void getAndroidID(String tag, @Nullable String needed) {
        String androidDeviseID = "";
        try {
            androidDeviseID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            Log.e(AppConstants.TOTAL_TAG, e.toString());
        }
        if (needed == null) {
            Log.d(AppConstants.TOTAL_TAG, tag + " androidDeviseID: " + androidDeviseID);
        } else {
            if (androidDeviseID.equalsIgnoreCase(needed)) {
                Log.i(AppConstants.TOTAL_TAG, "CHECK androidDeviseID: - OK");
            } else {
                Log.e(AppConstants.TOTAL_TAG, "CHECK androidDeviseID: - NOT OK");
            }
        }
    }

    private void setAndroidID() {
        genymotion.getId().setAndroidId(userRealm.getAndroid_id());
    }

    private void getIMEI(String tag, @Nullable String needed) {
        try {
            if (needed == null) {
                Log.d(AppConstants.TOTAL_TAG, tag + " IMEI: " + iGenydService.getDeviceId());
            } else {
                if (iGenydService.getDeviceId().equalsIgnoreCase(needed)) {
                    Log.i(AppConstants.TOTAL_TAG, "CHECK IMEI: - OK");
                } else {
                    Log.e(AppConstants.TOTAL_TAG, "CHECK IMEI: - NOT OK");
                }
            }
        } catch (Exception e) {
            Log.e(AppConstants.TOTAL_TAG, e.toString());
        }
    }

    private void setIMEI() {
        genymotion.getRadio().setDeviceId(userRealm.getIMEI_id());
    }

    public void getAdvertisingId(final String tag, @Nullable final String needed) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(activity.getApplicationContext());
                } catch (Exception e) {
                    Log.d(AppConstants.TOTAL_TAG, e.toString());
                }
                String advertId = null;
                if (idInfo != null) {
                    advertId = idInfo.getId();
                }
                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
                if (needed == null) {
                    Log.d(AppConstants.TOTAL_TAG, tag + " getAdvertisingId: " + advertId);
                } else {
                    if (advertId.equalsIgnoreCase(needed)) {
                        Log.i(AppConstants.TOTAL_TAG, "CHECK getAdvertisingId: - OK");
                    } else {
                        Log.e(AppConstants.TOTAL_TAG, "CHECK getAdvertisingId: - NOT OK");
                    }
                }
            }
        };
        task.execute();
    }

    public void setAdvertisingId() {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mount -o rw,remount /data"});
                } catch (Exception e1) {
                    Log.e(AppConstants.TOTAL_TAG, e1.toString());
                }
            }
        });

        Utils.copyBundledRealmFile(activity.getResources().openRawResource(R.raw.adid_settings_xml),
                "adid_settings_in.xml",
                activity.getFilesDir());


        File inFile = new File(activity.getFilesDir(), "adid_settings_in.xml");
        if (inFile.exists()) {
            try {
                File outFile = new File(activity.getFilesDir(), "adid_settings_out.xml");
                BufferedWriter brOutFile = new BufferedWriter(new FileWriter(outFile));
                BufferedReader brInFile = new BufferedReader(new FileReader(inFile));
                try {
                    String line;
                    while ((line = brInFile.readLine()) != null) {
                        if (line.contains("%%")) {
                            line = line.replace("%%", userRealm.getAdvertising_id());
                        }
                        brOutFile.write(line);
                        brOutFile.newLine();
                    }
                } finally {
                    try {
                        brOutFile.close();
                        brInFile.close();
                    } catch (Exception e) {
                        Log.e(AppConstants.TOTAL_TAG, e.toString());
                    }
                }

                final String fileAbsolutePath = outFile.getAbsolutePath();
                Log.d(AppConstants.TOTAL_TAG, "fileAbsolutePath: " + fileAbsolutePath);


                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mount -o rw,remount /data/data/com.google.android.gms/shared_prefs"});
                        } catch (Exception e) {
                            Log.e(AppConstants.TOTAL_TAG, e.toString());
                        }
                    }
                });

                Handler uiHandler1 = new Handler(Looper.getMainLooper());
                uiHandler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "cp -f " + fileAbsolutePath + " /data/data/com.google.android.gms/shared_prefs/adid_settings.xml"});
                                } catch (Exception e) {
                                    Log.e(AppConstants.TOTAL_TAG, e.toString());
                                }
                            }
                        });
                    }
                }, 500);
            } catch (Exception e) {
                Log.e(AppConstants.TOTAL_TAG, e.toString());
            }
        } else {
            Log.e(AppConstants.TOTAL_TAG, "inFile NOT exists");
        }
    }


    public void changeBuildPropFile() {

        Log.d(AppConstants.TOTAL_TAG, "deviceDataRealm: " + deviceDataRealm.getName());

        Utils.copyBundledRealmFile(activity.getResources().openRawResource(R.raw.build_prop), "build_in.prop", activity.getFilesDir());

        File inFile = new File(activity.getFilesDir(), "build_in.prop");
        if (inFile.exists()) {
            try {
                File outFile = new File(activity.getFilesDir(), "build_out.prop");
                BufferedWriter brOutFile = new BufferedWriter(new FileWriter(outFile));
                BufferedReader brInFile = new BufferedReader(new FileReader(inFile));
                try {
                    String line;
                    while ((line = brInFile.readLine()) != null) {
                        line = changeLineBuildProp(line, deviceDataRealm);
                        if (!line.contains("%%")) {
                            brOutFile.write(line);
                            brOutFile.newLine();
                        }
                    }
                } finally {
                    try {
                        brOutFile.close();
                        brInFile.close();
                    } catch (Exception e) {
                        Log.e(AppConstants.TOTAL_TAG, "e:" + e.toString());
                    }
                }

                final String fileAbsolutePath = outFile.getAbsolutePath();
                Log.d(AppConstants.TOTAL_TAG, "fileAbsolutePath: " + fileAbsolutePath);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mount -o rw,remount /system"});
                            Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mv " + fileAbsolutePath + " /system/build.prop"});
//                            Runtime.getRuntime().exec(new String[]{"su", "-c", "mount -o ro,remount /system"});
//                            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });

                            Handler uiHandler = new Handler(Looper.getMainLooper());
                            uiHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    checkBuildPropFileIsChenged(deviceDataRealm);
                                }
                            }, 600);


                        } catch (Exception e) {
                            Log.e(AppConstants.TOTAL_TAG, e.toString());
                        }
                    }
                });

            } catch (Exception e) {
                Log.e(AppConstants.TOTAL_TAG, "Exception", e);
            }
        } else {
            Log.e(AppConstants.TOTAL_TAG, "inFile NOT exists");
        }
    }

    private void checkBuildPropFileIsChenged(DeviceDataRealm device) {

        //todo можливо добавити ще якісь для перевірки?
        boolean ro_build_id = false;
        boolean ro_build_version_incremental = false;
        boolean ro_product_model = false;
        boolean ro_product_brand = false;
        boolean ro_product_manufacturer = false;

        File inFile = new File("/system/build.prop");
        if (inFile.exists()) {
            try {
                BufferedReader brInFile = new BufferedReader(new FileReader(inFile));
                try {
                    String line;
                    while ((line = brInFile.readLine()) != null) {
                        if (line.contains(AppConstants.ro_build_id) && line.contains(device.getRo_build_id())) ro_build_id = true;
                        if (line.contains(AppConstants.ro_build_version_incremental) && line.contains(device.getRo_build_version_incremental())) ro_build_version_incremental = true;
                        if (line.contains(AppConstants.ro_product_model) && line.contains(device.getRo_product_model())) ro_product_model = true;
                        if (line.contains(AppConstants.ro_product_brand) && line.contains(device.getRo_product_brand())) ro_product_brand = true;
                        if (line.contains(AppConstants.ro_product_manufacturer) && line.contains(device.getRo_product_manufacturer())) ro_product_manufacturer = true;
                    }
                } finally {
                    try {
                        brInFile.close();
                    } catch (Exception e) {
                        Log.e(AppConstants.TOTAL_TAG, "e:" + e.toString());
                    }
                }
            } catch (Exception e) {
                Log.e(AppConstants.TOTAL_TAG, "Exception", e);
            }
        }

        if (ro_build_id && ro_build_version_incremental && ro_product_model && ro_product_brand && ro_product_manufacturer) {
            Toast.makeText(activity, "build.prop Chenged !" + device.getName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "build.prop NOT Chenged !", Toast.LENGTH_SHORT).show();
        }
    }

    private String changeLineBuildProp(String line, DeviceDataRealm device) {
        if (TextUtils.isEmpty(line)) {
            return line;
        }
        if (device.getRo_build_version_sdk() > 0 ) line = line.replace("%%" + AppConstants.ro_build_version_sdk + "%", String.valueOf(device.getRo_build_version_sdk()));
        if (!TextUtils.isEmpty(device.getRo_build_version_release())) line = line.replace("%%" + AppConstants.ro_build_version_release + "%", device.getRo_build_version_release());
        if (!TextUtils.isEmpty(device.getRo_build_id())) line = line.replace("%%" + AppConstants.ro_build_id + "%", device.getRo_build_id());
        if (!TextUtils.isEmpty(device.getRo_build_display_id())) line = line.replace("%%" + AppConstants.ro_build_display_id + "%", device.getRo_build_display_id());
        if (!TextUtils.isEmpty(device.getRo_build_version_incremental())) line = line.replace("%%" + AppConstants.ro_build_version_incremental + "%", device.getRo_build_version_incremental());
        if (!TextUtils.isEmpty(device.getRo_build_date())) line = line.replace("%%" + AppConstants.ro_build_date + "%", device.getRo_build_date());
        if (!TextUtils.isEmpty(device.getRo_build_date_utc())) line = line.replace("%%" + AppConstants.ro_build_date_utc + "%", device.getRo_build_date_utc());
        if (!TextUtils.isEmpty(device.getRo_build_user())) line = line.replace("%%" + AppConstants.ro_build_user + "%", device.getRo_build_user());
        if (!TextUtils.isEmpty(device.getRo_build_host())) line = line.replace("%%" + AppConstants.ro_build_host + "%", device.getRo_build_host());
        if (!TextUtils.isEmpty(device.getRo_build_flavor())) line = line.replace("%%" + AppConstants.ro_build_flavor + "%", device.getRo_build_flavor());
        if (!TextUtils.isEmpty(device.getRo_product_model())) line = line.replace("%%" + AppConstants.ro_product_model + "%", device.getRo_product_model());
        if (!TextUtils.isEmpty(device.getRo_product_brand())) line = line.replace("%%" + AppConstants.ro_product_brand + "%", device.getRo_product_brand());
        if (!TextUtils.isEmpty(device.getRo_product_name())) line = line.replace("%%" + AppConstants.ro_product_name + "%", device.getRo_product_name());
        if (!TextUtils.isEmpty(device.getRo_product_device())) line = line.replace("%%" + AppConstants.ro_product_device + "%", device.getRo_product_device());
        if (!TextUtils.isEmpty(device.getRo_product_board())) line = line.replace("%%" + AppConstants.ro_product_board + "%", device.getRo_product_board());
        if (!TextUtils.isEmpty(device.getRo_product_manufacturer())) line = line.replace("%%" + AppConstants.ro_product_manufacturer + "%", device.getRo_product_manufacturer());
        if (!TextUtils.isEmpty(device.getRo_product_locale_language())) line = line.replace("%%" + AppConstants.ro_product_locale_language + "%", device.getRo_product_locale_language());
        if (!TextUtils.isEmpty(device.getRo_product_locale_region())) line = line.replace("%%" + AppConstants.ro_product_locale_region + "%", device.getRo_product_locale_region());
        if (!TextUtils.isEmpty(device.getRo_build_description())) line = line.replace("%%" + AppConstants.ro_build_description + "%", device.getRo_build_description());
        if (!TextUtils.isEmpty(device.getRo_build_fingerprint())) line = line.replace("%%" + AppConstants.ro_build_fingerprint + "%", device.getRo_build_fingerprint());

        return line;
    }

}
