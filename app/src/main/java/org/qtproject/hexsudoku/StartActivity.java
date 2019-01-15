package org.qtproject.hexsudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.qtproject.hexsudoku.hepers.DeviceHelper;

public class StartActivity extends AppCompatActivity  {

    private Utils utils;
    private DeviceHelper deviceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = new Utils(this);
        deviceHelper = new DeviceHelper(this);

        deviceHelper.chengeGenymotionData();
//        deviceDataHepler.checngeGoogleAdvertisingID(true);

        utils.initGA();

        // ADB - Android - Getting the name of the current activity
        // adb shell dumpsys window windows | grep -E 'mCurrentFocus|mFocusedApp'

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
