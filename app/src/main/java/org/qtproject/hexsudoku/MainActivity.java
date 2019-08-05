package org.qtproject.hexsudoku;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.BannerView;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.NonSkippableVideoCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;
import com.crashlytics.android.Crashlytics;

import org.qtproject.hexsudoku.constants.AppConstants;
import org.qtproject.hexsudoku.hepers.DeviceHelper;
import org.qtproject.hexsudoku.hepers.RootHelper;
import org.qtproject.hexsudoku.hepers.UserHelper;

import io.fabric.sdk.android.Fabric;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();
    private Utils utils;


    private boolean isInApp = false;
    private boolean isSkipVideo = false;
    private boolean isInterstitial = false;


    private BannerView appodealBannerView;
    private NativeAdViewNewsFeed nav_nf;

    private RootHelper rootHelper;
    private UserHelper userHelper;
    private DeviceHelper deviceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        utils = new Utils(this);
        rootHelper = new RootHelper(this);
        userHelper = new UserHelper(this);
        deviceHelper = new DeviceHelper(this);

        initAppodealSdk();

        checkPermission();
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        isInApp = true;
        isSkipVideo = false;
        isInterstitial = false;

        Appodeal.onResume(this, Appodeal.NATIVE | Appodeal.BANNER);
    }



    /**
     * click on Ad and return to app
     */
    private void clickBack(int delay) {
        Log.d(TAG, "clickBack() delay: " + delay);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec(new String[] {AppConstants.NAME_SU, "-c", "input keyevent 4"});

                            Log.i(TAG, "START adb shell keyevent clickBack");
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }
        }, delay);

    }

    private void clickOnAd(int delay) {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
//                    Runtime.getRuntime().exec("input tap 800 830");
                    Log.i(TAG, "START adb shell TAP");

                    backFocusInApp(5000);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }, delay);
    }

    private void backFocusInApp(int delay) {
        Log.d(TAG, "backFocusInApp() delay: " + delay);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isInApp) {
                    Log.i(TAG, "backFocusInApp() NOT in App ");
                    clickBack(500);
                    backFocusInApp(4000);
                } else {
                    Log.d(TAG, "backFocusInApp() In App ");
                }
            }
        }, delay);
    }

    private void startBaskInApp() {
        Log.d(TAG, "startBaskInApp()");
//        clickBack(10000);
//        backFocusInApp(14000);
    }

    private void startClickOnAd() {
        clickOnAd(12000);
    }
    //*************************************




    /**
     * work with View Button
     */
    public void onClickBannerShowProg(View view) {
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);
    }

    public void onClickBannerShowXml(View view) {
        Appodeal.show(this, Appodeal.BANNER_VIEW);
    }

    public void onClickInterstitialShow(View view) {
        Appodeal.show(this, Appodeal.INTERSTITIAL);
    }

    public void onClickRewardedShow(View view) {
        Appodeal.show(this, Appodeal.REWARDED_VIDEO);
    }

    public void onClickStartTestActivityAppodeal(View view) {
        Appodeal.startTestActivity(this);
    }

    public void onClickStartNewActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickRebootPhone(View view) {
       utils.rebootPhone();
    }

    public void onClickTestCrash(View view) {
        Crashlytics.getInstance().crash();
    }



    public void onRoot(View view) {
        rootHelper.onRoot();
    }

    public void offRoot(View view) {
        rootHelper.offRoot();
    }

    public void checkRoot(View view) {
        rootHelper.checkRoot();
    }
    //*************************************



    /**
     * work with AppodealSdk
     */
    private void initAppodealSdk() {
        BannerView appodealBannerView = (BannerView) findViewById(R.id.appodealBannerView);

        Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.none);
//        Appodeal.setTesting(true);

        Appodeal.setBannerViewId(R.id.appodealBannerView);
        nav_nf = findViewById(R.id.native_ad_view_news_feed);

        offNetworks();

        if (!Appodeal.isInitialized(AppConstants.APPODEAL_AD_TYPES)) {
            Appodeal.initialize(this, AppConstants.APPODEAL_APP_KEY, AppConstants.APPODEAL_AD_TYPES);
        }

        Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.none);
        Appodeal.cache(this, Appodeal.NATIVE, 1);

        setAppodealCallbacks();

//        showNativeAD();
    }

    private void offNetworks() {
//        Appodeal.disableNetwork(this, "adcolony", AppConstants.APPODEAL_AD_TYPES);
//        Appodeal.disableNetwork(this, "admob", AppConstants.APPODEAL_AD_TYPES);
    }

    private void showNativeAD() {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<NativeAd> nativeAds = Appodeal.getNativeAds(1);
                if (nativeAds.size() > 0) {
                    nav_nf.setNativeAd(nativeAds.get(0));
                }
                Appodeal.cache(MainActivity.this, Appodeal.NATIVE, 1);
                showNativeAD();
            }
        },5000);
    }

    private void setAppodealCallbacks() {

        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                Log.d(TAG, "onNativeLoaded");
            }

            @Override
            public void onNativeShown(NativeAd nativeAd) {
                Log.d(TAG, "onNativeShown");
            }

            @Override
            public void onNativeClicked(NativeAd nativeAd) {
                Log.d(TAG, "onNativeClicked");
            }

            @Override
            public void onNativeFailedToLoad() {
                Log.d(TAG, "onNativeFailedToLoad");
            }

            @Override
            public void onNativeExpired() {
                Log.d(TAG, "onNativeExpired");
            }
        });

        Appodeal.setBannerCallbacks(new BannerCallbacks() {

            @Override
            public void onBannerLoaded(int i, boolean b) {
                Log.d(TAG, "onBannerLoaded");
            }

            @Override
            public void onBannerFailedToLoad() {
                Log.d(TAG, "onBannerFailedToLoad");
            }

            @Override
            public void onBannerShown() {
                Log.d(TAG, "onBannerShown");
            }

            @Override
            public void onBannerClicked() {
                Log.d(TAG, "onBannerClicked");
                isInApp = false;
                startBaskInApp();
            }

            @Override
            public void onBannerExpired() {
                Log.d(TAG, "onBannerExpired");
            }
        });

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean b) {
                Log.d(TAG, "onInterstitialLoaded");
            }

            @Override
            public void onInterstitialFailedToLoad() {
                Log.d(TAG, "onInterstitialFailedToLoad");
            }

            @Override
            public void onInterstitialShown() {
                Log.d(TAG, "onInterstitialShown start");
                isInApp = false;
                isInterstitial = true;
//                startBaskInApp();

                startClickOnAd();
            }

            @Override
            public void onInterstitialClicked() {
                Log.d(TAG, "onInterstitialClicked");
            }

            @Override
            public void onInterstitialClosed() {
                Log.d(TAG, "onInterstitialClosed");
            }

            @Override
            public void onInterstitialExpired() {
                Log.d(TAG, "onInterstitialExpired");
            }
        });

        Appodeal.setNonSkippableVideoCallbacks(new NonSkippableVideoCallbacks() {
            @Override
            public void onNonSkippableVideoLoaded(boolean b) {
                Log.d(TAG, "onNonSkippableVideoLoaded");
            }

            @Override
            public void onNonSkippableVideoFailedToLoad() {
                Log.d(TAG, "onNonSkippableVideoFailedToLoad");
            }

            @Override
            public void onNonSkippableVideoShown() {
                Log.d(TAG, "onNonSkippableVideoShown start");
                isInApp = false;
                isSkipVideo = true;
                startBaskInApp();
            }

            @Override
            public void onNonSkippableVideoFinished() {
                Log.d(TAG, "onNonSkippableVideoFinished");
            }

            @Override
            public void onNonSkippableVideoClosed(boolean b) {
                Log.d(TAG, "onNonSkippableVideoClosed");
            }

            @Override
            public void onNonSkippableVideoExpired() {
                Log.d(TAG, "onNonSkippableVideoExpired");
            }
        });

        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded(boolean b) {
                Log.d(TAG, "onRewardedVideoLoaded");
            }

            @Override
            public void onRewardedVideoFailedToLoad() {
                Log.d(TAG, "onRewardedVideoFailedToLoad");
            }

            @Override
            public void onRewardedVideoShown() {
                Log.d(TAG, "onRewardedVideoShown");
            }

            @Override
            public void onRewardedVideoFinished(double v, String s) {
                Log.d(TAG, "onRewardedVideoFinished");
            }

            @Override
            public void onRewardedVideoClosed(boolean b) {
                Log.d(TAG, "onRewardedVideoClosed");
            }

            @Override
            public void onRewardedVideoExpired() {
                Log.d(TAG, "onRewardedVideoExpired");
            }

        });
    }

    public void onCreateUsers(View view) {
        userHelper.createUsers();
    }

    public void onCangeUser(View view) {
        userHelper.chengeUser();
    }

    public void onShowUsers(View view) {
        userHelper.showUsers();
    }

    public void onDeleteUsers(View view) {
        userHelper.deleteUsers();
    }

    public void onTest(View view) {
//        Log.d(AppConstants.TOTAL_TAG, "getPoverLevel(): " + String.valueOf(deviceHelper.getAdvertisingIdAppodeal()));

        deviceHelper.getCurrentUserData();


    }


    //*************************************
}


