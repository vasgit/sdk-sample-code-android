package org.qtproject.hexsudoku.hepers;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.qtproject.hexsudoku.constants.AppConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
            File to = new File(s + "_");

            if (from.exists()) {
                Log.d(TAG, "from.exists: " + from);

//                try {
//
//                    if (from.renameTo(to)) {
//                        Log.d(TAG, "file renamed: "  + from);
//                    } else {
//                    } else {
//                        Log.d(TAG, "file NOT renamed: " + from);
//                    }
//                    Log.e(TAG, "  ");
//
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }

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

//        renameRootFile("", "_");
    }

    public void onRoot() {
//        createScriptFile();

        for (final String s: paths) {

            File from = new File(s + AppConstants.addTextToRoot);
            File to = new File(s);

            if (from.exists()) {
                Log.d(TAG, "from.exists: " + from);

                try {

//                    if (from.renameTo(to)) {
//                        Log.d(TAG, "file Renamed: "  + from);
//                    } else {
//                        Log.d(TAG, "file NOT Renamed: " + from);
//                    }

                    final String fileAbsolutePath = from.getAbsolutePath();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                String sss = "cd " + Environment.getExternalStorageDirectory();
//                                Log.d(TAG, "sss: " + sss);
//                                Runtime.getRuntime().exec(sss);


//                                Runtime.getRuntime().exec(new String[]{"adb shell /system/bin/su_"});

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





//                                Runtime.getRuntime().exec(new String[]{"su", "-c", "rm -f " + fileAbsolutePath});


//                            Runtime.getRuntime().exec(new String[]{"su", "-c", "mount -o ro,remount /system"});
//                            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });


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



//        renameRootFile("_", "");
    }


    private void createScriptFile() {
        try {
            if (isExternalStorageWritable()) {
                File debugFile = new File(Environment.getExternalStorageDirectory(), BACK_SCRIPT);
                BufferedWriter output = new BufferedWriter(new FileWriter(debugFile));

                try {
                    output.write("su_ -c");
                    output.write("cp /system/xbin/su_ /system/xbin/su");

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



    private static final String BACK_SCRIPT = "backScript.sh";

    private void renameRootFile(String addToFile1, final String addToFile2) {

        Log.d(TAG, " ");
        for (final String s: paths) {
            final File from = new File(s + addToFile1);
            if (from.exists()) {
                Log.d(TAG, "from.exists: " + from);
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mv " + from.getAbsolutePath() + " " + s + addToFile2});
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    });

                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (from.exists()) {
                                Log.d(TAG, "file NOT renamed: " + from);
                            } else {
                                Log.d(TAG, "file renamed: " + from + " to " + s + addToFile2);
                            }
                        }
                    }, 200);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Log.e(TAG, "from NOT exists: " + from);
            }
        }

        Log.d(TAG, "------------");
    }


}
