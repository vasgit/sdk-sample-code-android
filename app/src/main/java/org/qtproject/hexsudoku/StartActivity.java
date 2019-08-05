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
import org.qtproject.hexsudoku.hepers.RootHelper;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class StartActivity extends AppCompatActivity  {

    private Utils utils;
    private RootHelper rootHelper;

    public static final String appListFilter = "^?(?:com\\.android|com\\.google|com\\.sec|com\\.samsung|com\\.sonyericsson|com\\.sonymobile|com\\.motorola|com\\.htc).*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo: для коректної роботи при страті потрібно скопіювати рутові файлики і назвати AppConstants.NAME_SU

        utils = new Utils(this);
        rootHelper = new RootHelper(this);

        checkPermissionWtiteSystems();

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

        //todo: булоб добре видалити цы бандли
        // com.genymotion.systempatcher
        // com.genymotion.genyd
        // com.genymotion.superuser

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //дати доступ на читання та зміни в папці system
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


        //не працює ця штука
//        rootHelper.copySuToMu_DeleteSuRoot();
    }
}
