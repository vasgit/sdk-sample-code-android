package org.qtproject.hexsudoku.hepers;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.qtproject.hexsudoku.constants.AppConstants;

import java.io.File;

public class RootHelper {

    private Activity activity;

    public RootHelper(Activity activity) {
        this.activity = activity;
    }


    public void offRoot() {

        for (final String s: AppConstants.suPaths) {

            File from = new File(s);
            if (from.exists()) {
                Log.d(AppConstants.TOTAL_TAG, "from.exists: " + from);

                final String fileAbsolutePath = from.getAbsolutePath();
                Log.i(AppConstants.TOTAL_TAG, "fileAbsolutePath: " + fileAbsolutePath);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec(new String[]{AppConstants.NAME_SU, "-c", "mount -o rw,remount /system"});
                        } catch (Exception e1) {
                            Log.e(AppConstants.TOTAL_TAG, e1.toString());
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
                                    Log.e(AppConstants.TOTAL_TAG, e.toString());
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
                                    Log.e(AppConstants.TOTAL_TAG, e.toString());
                                }
                            }
                        });
                    }
                }, 3000);

            } else {
                Log.e(AppConstants.TOTAL_TAG, "from NOT exists: " + from);
            }
        }

        Log.d(AppConstants.TOTAL_TAG, "------------");
    }

    public void onRoot() {
        for (final String s: AppConstants.suPaths) {
            File from = new File(s + AppConstants.addTextToRoot);
            if (from.exists()) {
                Log.d(AppConstants.TOTAL_TAG, "from.exists: " + from);
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
                                                    Log.e(AppConstants.TOTAL_TAG, e.toString());
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
                                                    Log.e(AppConstants.TOTAL_TAG, e.toString());
                                                }
                                            }
                                        });
                                    }
                                }, 3000);

                                Log.d(AppConstants.TOTAL_TAG, "mu ok");
                            } catch (Exception e) {
                                Log.e(AppConstants.TOTAL_TAG, e.toString());
                            }
                        }
                    });

                    Log.e(AppConstants.TOTAL_TAG, "  ");

                } catch (Exception e) {
                    Log.e(AppConstants.TOTAL_TAG, e.toString());
                }
            } else {
                Log.e(AppConstants.TOTAL_TAG, "from NOT exists: " + from);
            }
        }

        Log.d(AppConstants.TOTAL_TAG, "------------");


    }

    public void checkRoot() {
        Log.d(AppConstants.TOTAL_TAG, " ");
        for (final String s: AppConstants.suPaths) {
            final File file1 = new File(s);
            final File file2 = new File(s + AppConstants.addTextToRoot);

            if (file1.exists()) {
                Log.e(AppConstants.TOTAL_TAG, "exists: " + file1);
            } else {
                Log.d(AppConstants.TOTAL_TAG, "NOT exists: " + file1);
            }

            if (file2.exists()) {
                Log.e(AppConstants.TOTAL_TAG, "exists: " + file2);
            } else {
                Log.d(AppConstants.TOTAL_TAG, "NOT exists: " + file2);
            }

        }
    }

    public void copySuToMu_DeleteSuRoot() {
        //todo поки роблю вручну, програмно копіюється криво, руками норм

//        /system/bin/su
//        /system/xbin/su

        final File bin_su = new File("/system/bin/su");
        final File xbin_su = new File("/system/xbin/su");
        final File bin_Mu = new File("/system/bin/mu");
        final File xbin_Mu = new File("/system/xbin/mu");

        //todo try copy su to mu programly
//        if (!bin_Mu.exists() && bin_su.exists() ) {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Runtime.getRuntime().exec(new String[]{"su", "-c", "cp " + bin_su.getAbsolutePath() + " " + bin_Mu.getAbsolutePath()});
//                    } catch (Exception e) {
//                        Log.e(AppConstants.TOTAL_TAG, e.toString());
//                    }
//                }
//            });
//        }
//        if (!xbin_Mu.exists() && xbin_su.exists()) {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Runtime.getRuntime().exec(new String[]{"su", "-c", "cp " + xbin_su.getAbsolutePath() + " " + xbin_Mu.getAbsolutePath()});
//                    } catch (Exception e) {
//                        Log.e(AppConstants.TOTAL_TAG, e.toString());
//                    }
//                }
//            });
//        }

        if (bin_su.exists() && xbin_su.exists()) {
            offRoot();
        }
    }
}
