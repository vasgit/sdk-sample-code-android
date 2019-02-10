package org.qtproject.hexsudoku.hepers;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.qtproject.hexsudoku.constants.AppConstants;

import java.io.File;

public class RootHelper {

    private static String TAG = RootHelper.class.getName();

    private Activity activity;

    public RootHelper(Activity activity) {
        this.activity = activity;
    }

    private String[] paths = {"/system/app/Superuser.apk", "/sbin/su",
            "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
            "/system/bin/failsafe/su", "/data/local/su"};

    public void offRoot() {

        for (final String s: paths) {

            File from = new File(s);
            if (from.exists()) {
                Log.d(TAG, "from.exists: " + from);

                final String fileAbsolutePath = from.getAbsolutePath();
                Log.i(TAG, "fileAbsolutePath: " + fileAbsolutePath);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mount -o rw,remount /system"});
                        } catch (Exception e1) {
                            Log.e(TAG, e1.toString());
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
                                    Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "cp " + fileAbsolutePath + " " + fileAbsolutePath + AppConstants.addTextToRoot});
                                } catch (Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            }
                        });
                    }
                }, 500);

                Handler uiHandler2 = new Handler(Looper.getMainLooper());
                uiHandler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "rm -f " + fileAbsolutePath});
                                } catch (Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            }
                        });
                    }
                }, 3000);

            } else {
                Log.e(TAG, "from NOT exists: " + from);
            }
        }

        Log.d(TAG, "------------");
    }

    public void onRoot() {
        for (final String s: paths) {
            File from = new File(s + AppConstants.addTextToRoot);
            if (from.exists()) {
                Log.d(TAG, "from.exists: " + from);
                try {

                    final String fileAbsolutePath = from.getAbsolutePath();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mount -o rw,remount /system"});

                                Handler uiHandler1 = new Handler(Looper.getMainLooper());
                                uiHandler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "cp " + fileAbsolutePath + " " + fileAbsolutePath.replace(AppConstants.addTextToRoot, "")});
                                                } catch (Exception e) {
                                                    Log.e(TAG, e.toString());
                                                }
                                            }
                                        });
                                    }
                                }, 500);

                                Handler uiHandler2 = new Handler(Looper.getMainLooper());
                                uiHandler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "rm -f " + fileAbsolutePath});
                                                } catch (Exception e) {
                                                    Log.e(TAG, e.toString());
                                                }
                                            }
                                        });
                                    }
                                }, 3000);

                                Log.d(TAG, "mu ok");
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    });

                    Log.e(TAG, "  ");

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Log.e(TAG, "from NOT exists: " + from);
            }
        }

        Log.d(TAG, "------------");


    }

    public void checkRoot() {
        Log.d(TAG, " ");
        for (final String s: paths) {
            final File file1 = new File(s);
            final File file2 = new File(s + AppConstants.addTextToRoot);

            if (file1.exists()) {
                Log.e(TAG, "exists: " + file1);
            } else {
                Log.d(TAG, "NOT exists: " + file1);
            }

            if (file2.exists()) {
                Log.e(TAG, "exists: " + file2);
            } else {
                Log.d(TAG, "NOT exists: " + file2);
            }

        }
    }

}
