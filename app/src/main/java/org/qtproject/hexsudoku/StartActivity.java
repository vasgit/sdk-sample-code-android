package org.qtproject.hexsudoku;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.qtproject.hexsudoku.constants.AppConstants;
import org.qtproject.hexsudoku.hepers.DeviceHelper;
import org.qtproject.hexsudoku.hepers.RootHelper;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class StartActivity extends AppCompatActivity  {

    private Utils utils;
    private DeviceHelper deviceHelper;
    private RootHelper rootHelper;

    public static final String appListFilter = "^?(?:com\\.android|com\\.google|com\\.sec|com\\.samsung|com\\.sonyericsson|com\\.sonymobile|com\\.motorola|com\\.htc).*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

        utils = new Utils(this);
        rootHelper = new RootHelper(this);
        deviceHelper = new DeviceHelper(this);

        checkPermissionWtiteSystems();

        deviceHelper.chengeGenymotionData();
//        deviceDataHepler.checngeGoogleAdvertisingID(true);

        utils.initGA();

        // ADB - Android - Getting the name of the current activity
        // adb shell dumpsys window windows | grep -E 'mCurrentFocus|mFocusedApp'


//        Simply remount as rw (Read/Write):
//        # mount -o rw,remount /system


//        Once you are done making changes, remount to ro (read-only):
//        # mount -o ro,remount /system


//        adb shell mount -o rw,remount /system

//        todo: fill if need
//        new FillRealmData(this).fill();



        Log.d(AppConstants.TOTAL_TAG, "Apps:");
        PackageManager packageManager = getPackageManager();
        Pattern pattern = Pattern.compile(appListFilter);
        List<ApplicationInfo> appInfos = packageManager.getInstalledApplications(0);
        if (appInfos != null) {
            for (ApplicationInfo packageInfo : appInfos) {
                String packageName = packageInfo.packageName;
                if (!pattern.matcher(packageName).matches()
                        && !packageName.equals("android")) {
                    Log.d(AppConstants.TOTAL_TAG, packageName);
                }
            }
        }

        Log.d(AppConstants.TOTAL_TAG, "Apps end:");



        // com.genymotion.systempatcher
        // com.genymotion.genyd
        // com.genymotion.superuser

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.READ_EXTERNAL_STORAGE_PERMISSION_CODE);
            }
        }
    }

    private void checkPermissionWtiteSystems() {
        //set params for /system/build.prop and read /system
        File bin_su = new File("/system/bin/su");
        File xbin_su = new File("/system/xbin/su");
        File bin_Mu = new File("/system/bin/mu");
        File xbin_Mu = new File("/system/xbin/mu");

        if (bin_su.exists() || xbin_su.exists()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "mount -o rw,remount /system"});
                    } catch (Exception e1) {
                        Log.e(AppConstants.TOTAL_TAG, e1.toString());
                    }
                }
            });
        }
        if (bin_Mu.exists() || xbin_Mu.exists()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mount -o rw,remount /system"});
                    } catch (Exception e1) {
                        Log.e(AppConstants.TOTAL_TAG, e1.toString());
                    }
                }
            });
        }

        rootHelper.copySuToMu_DeleteSuRoot();
    }
}
