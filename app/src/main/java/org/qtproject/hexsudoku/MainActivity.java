package org.qtproject.hexsudoku;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
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
import com.genymotion.api.GenymotionManager;

import io.fabric.sdk.android.Fabric;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private boolean isInApp = false;
    private boolean isSkipVideo = false;
    private boolean isInterstitial = false;

    private static final String BACK_SCRIPT = "backScript.sh";


    private static String TAG = "MainActivity";


    BannerView appodealBannerView;
    NativeAdViewNewsFeed nav_nf;
    private String androidDeviseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        GenymotionManager genymotion = GenymotionManager.getGenymotionManager(this);
        genymotion.getBattery().setLevel(50);

        genymotion.getId().setRandomAndroidId();



        BannerView appodealBannerView = (BannerView) findViewById(R.id.appodealBannerView);


        Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.debug);
//        Appodeal.setTesting(true);

        Appodeal.setBannerViewId(R.id.appodealBannerView);


        nav_nf = (NativeAdViewNewsFeed) findViewById(R.id.native_ad_view_news_feed);

//        offNetworks();


        Appodeal.initialize(this, "14b84e43b51b7a58cefa66808a6b05337d3a972a5ea49684", Appodeal.NATIVE | Appodeal.REWARDED_VIDEO | Appodeal.BANNER | Appodeal.INTERSTITIAL);

        Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.debug);

        Appodeal.cache(this, Appodeal.NATIVE, 1);

        setCallbacks();


        try {
            Runtime.getRuntime().exec(new String[] {"su", "-c", "rm -f /data/data/com.google.android.gms/shared_prefs/adid_settings.xml"});
        } catch (Exception e) {
            Log.d(TAG, "Exception", e);
        }


//        showNativeAD();


        getAndroidDeviseID();
//        createScriptFile();


//        adb shell input tap 800 830


//        "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"

        String[] paths = {"/system/app/Superuser.apk", "/sbin/su",
                "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su"};

        String root = Environment.getExternalStorageDirectory().toString();

        for (String s: paths) {

            File fdelete = new File(s);
            File fdeleteTo = new File(s + "1");

//            File fdeleteTo = new File("/system/xbin", "su1");



            if (fdelete.exists()) {
                Log.e(TAG, "fdelete.exists: " + fdelete);

                try {

                    if (fdelete.renameTo(fdeleteTo)) {
                        Log.e(TAG, "file Deleted: "  + fdelete);
                    } else {
                        Log.e(TAG, "file not Deleted: " + fdelete);
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }




//                try {
//                    BufferedWriter output = new BufferedWriter(new FileWriter(fdeleteTo));
//                    output.close();
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }
//
//
//                try {
//                    copy(fdelete, fdeleteTo);
//                } catch (IOException e) {
//
//                    Log.e(TAG, e.toString());
//                }


            } else {


                Log.e(TAG, "fdelete NOT exists: " + fdelete);
            }
        }

    }


    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }




    private void createScriptFile() {
        try {
            if (isExternalStorageWritable()) {
                File debugFile = new File(Environment.getExternalStorageDirectory(), BACK_SCRIPT);
                BufferedWriter output = new BufferedWriter(new FileWriter(debugFile));

                try {
                    output.write("input keyevent 4");

                } finally {
                    try {
                        output.close();
                        Log.d(TAG, "createScriptFile: ok");
                        Log.d(TAG, "Environment.getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory());
                    } catch (Exception e) {
                        Log.e(TAG, "e:" + e.toString());
                    }
                }
            } else {
                Log.d(TAG, "Debug File: Storage not writable");
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception", e);
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    private void clickBask(int delay) {
        Log.d(TAG, "clickBask() delay: " + delay);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String sss = "sh " + Environment.getExternalStorageDirectory() + "/" + BACK_SCRIPT;
                            Log.d(TAG, "sss: " + sss);
//                            Runtime.getRuntime().exec(sss);

                            Runtime.getRuntime().exec(new String[] {"su", "-c", "input keyevent 4"});

//                            Runtime.getRuntime().exec(new String[] {"input keyevent 4"});

                            Log.e(TAG, "START adb shell keyevent");
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }
        }, delay);



    }

    private void startBaskInApp() {
        Log.d(TAG, "startBaskInApp()");
//        clickBask(10000);
//        checkFocusInApp(14000);
    }

    private void checkFocusInApp(int delay) {
        Log.d(TAG, "checkFocusInApp() delay: " + delay);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isInApp) {
                    Log.e(TAG, "checkFocusInApp() NOT in App ");
                    clickBask(500);
                    checkFocusInApp(4000);
                } else {
                    Log.d(TAG, "checkFocusInApp() In App ");
                }
            }
        }, delay);
    }


    private void startClickOnAd() {
        clickOnAd(12000);

    }


    private void clickOnAd(int delay) {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
//                    Runtime.getRuntime().exec("input tap 800 830");
                    Log.e(TAG, "START adb shell TAP");

                    checkFocusInApp(5000);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }, delay);
    }




    @SuppressLint("HardwareIds")
    private void getAndroidDeviseID() {
        try {
            androidDeviseID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            androidDeviseID = "";
        }
        Log.d(TAG, "ANDROID_DEVISE_ID=" + androidDeviseID);
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        isInApp = true;
        isSkipVideo = false;
        isInterstitial = false;

        Appodeal.onResume(this, Appodeal.NATIVE | Appodeal.BANNER);
    }


    public void bannerprog(View view) {
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);
    }

    public void bannerxml(View view) {
        Appodeal.show(this, Appodeal.BANNER_VIEW);
    }

    public void Interstitial(View view) {
        Appodeal.show(this, Appodeal.INTERSTITIAL);
    }

    public void Rewarded(View view) {
        Appodeal.show(this, Appodeal.REWARDED_VIDEO);
    }




    public void onClickGetFps(View view) {
        Appodeal.startTestActivity(this);
    }




    private void setCallbacks() {


        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                Log.d("Appodeal", "onNativeLoaded ");


            }

            @Override
            public void onNativeShown(NativeAd nativeAd) {
                Log.d("Appodeal", "onNativeShown");


            }

            @Override
            public void onNativeClicked(NativeAd nativeAd) {
                Log.d("Appodeal", "onNativeClicked");
            }



            @Override
            public void onNativeFailedToLoad() {
                Log.d("Appodeal", "onNativeFailedToLoad");
            }

            @Override
            public void onNativeExpired() {

            }
        });


        Appodeal.setBannerCallbacks(new BannerCallbacks() {

            @Override
            public void onBannerLoaded(int i, boolean b) {
                Log.d(TAG, "onBannerLoaded start");
            }

            @Override
            public void onBannerFailedToLoad() {
                Log.d("Appodeal", "onBannerFailedToLoad");
            }

            @Override
            public void onBannerShown() {

            }

            @Override
            public void onBannerClicked() {

                Log.d(TAG, "onBannerClicked");
                isInApp = false;
                startBaskInApp();


            }

            @Override
            public void onBannerExpired() {

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


    public void onClickStartNewActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void onClickReboot(View view) {
        try {
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
            proc.waitFor();
        } catch (Exception ex) {
            Log.i(TAG, "Could not reboot: ", ex);
        }
    }



}


