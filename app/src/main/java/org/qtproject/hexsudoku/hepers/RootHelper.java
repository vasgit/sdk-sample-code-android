package org.qtproject.hexsudoku.hepers;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RootHelper {

    private static String TAG = RootHelper.class.getName();
    private Activity activity;

    public RootHelper(Activity activity) {
        this.activity = activity;
    }

    public void renamaRootFiles() {
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
                Log.d(TAG, "fdelete.exists: " + fdelete);

                try {

                    if (fdelete.renameTo(fdeleteTo)) {
                        Log.d(TAG, "file Deleted: "  + fdelete);
                    } else {
                        Log.d(TAG, "file not Deleted: " + fdelete);
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






}
