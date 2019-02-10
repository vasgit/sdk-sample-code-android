package org.qtproject.hexsudoku;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.qtproject.hexsudoku.constants.AppConstants;
import org.qtproject.hexsudoku.hepers.DeviceHelper;
import org.qtproject.hexsudoku.hepers.RootHelper;

public class StartActivity extends AppCompatActivity  {

    private static String TAG = StartActivity.class.getName();

    private Utils utils;
    private DeviceHelper deviceHelper;
    private RootHelper rootHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

        utils = new Utils(this);
        rootHelper = new RootHelper(this);
        deviceHelper = new DeviceHelper(this);



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

        //set params for /system/build.prop and read /system
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec(new String[]{"su", "-c", "mount -o rw,remount /system"});
                } catch (Exception e1) {
                    Log.e(TAG, e1.toString());
                }
                try {
                    Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mount -o rw,remount /system"});
                } catch (Exception e2) {
                    Log.e(TAG, e2.toString());
                }
            }
        });
    }
}
