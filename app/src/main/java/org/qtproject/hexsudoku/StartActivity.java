package org.qtproject.hexsudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.qtproject.hexsudoku.hepers.DeviceHelper;
import org.qtproject.hexsudoku.hepers.RootHelper;

public class StartActivity extends AppCompatActivity  {

    private Utils utils;
    private DeviceHelper deviceHelper;
    private RootHelper rootHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = new Utils(this);
        rootHelper = new RootHelper(this);
        deviceHelper = new DeviceHelper(this);

        rootHelper.renamaRootFiles();


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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
