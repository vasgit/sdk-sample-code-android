package org.qtproject.hexsudoku.realmmodel;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import org.qtproject.hexsudoku.constants.AppConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class FillRealmData {

    private static String TAG = FillRealmData.class.getName();
    private Activity activity;

    public FillRealmData(Activity activity) {
        this.activity = activity;
    }

    public void fill() {
//        fillDevaceData();
        fillFromFiles();
        fillUsers();

    }

    private void addDevaceToRealm(final DeviceDataRealm devaceData) {
        try {
            Realm mRealm = Realm.getDefaultInstance();
            mRealm.refresh();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(devaceData);
                }
            });
        } catch (Exception e3) {
            Log.e(TAG, e3.toString());
        }
    }

    private void addUserToRelm(final UserRealm userRealm) {
        try {
            Realm mRealm = Realm.getDefaultInstance();
            mRealm.refresh();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(userRealm);
                }
            });
        } catch (Exception e3) {
            Log.e(TAG, e3.toString());
        }
    }

    private void fillUsers() {
        UserRealm userRealm;

        userRealm = new UserRealm();
        userRealm.setId(0);
        userRealm.setAndroid_id("123234345");
        addUserToRelm(userRealm);

    }

    private void fillDevaceData() {
        DeviceDataRealm deviceData;



        deviceData = new DeviceDataRealm();

        deviceData.setId(0);
        deviceData.setName("samsung galaxy s5");

        //ro.build.version.sdk
        deviceData.setRo_build_version_sdk(22);
        //ro.build.version.release
        deviceData.setRo_build_version_release("5.1.1");
        //ro.build.id
        deviceData.setRo_build_id("LMY47X");
        //ro.build.display.id
        deviceData.setRo_build_display_id("LMY47X.G900AUCU4CPA1");
        //ro.build.version.incremental
        deviceData.setRo_build_version_incremental("G900AUCU4CPA1");
        //ro.build.date
        deviceData.setRo_build_date("Wed Jan 27 21:23:36 KST 2016");
        //ro.build.date.utc
        deviceData.setRo_build_date_utc("1453897416");
        //ro.build.user
        deviceData.setRo_build_user("dpi");
        //ro.build.host
        deviceData.setRo_build_host("SWHC3812");
        //ro.build.flavor
        deviceData.setRo_build_flavor("klteuc-user");
        //ro.product.model
        deviceData.setRo_product_model("SAMSUNG-SM-G900A");
        //ro.product.brand
        deviceData.setRo_product_brand("samsung");
        //ro.product.name
        deviceData.setRo_product_name("klteuc");
        //ro.product.device
        deviceData.setRo_product_device("klteatt");
        //ro.product.board
        deviceData.setRo_product_board("MSM8974");
        //ro.product.manufacturer
        deviceData.setRo_product_manufacturer("samsung");
        //ro.product.locale.language
        deviceData.setRo_product_locale_language("en");
        //ro.product.locale.region
        deviceData.setRo_product_locale_region("US");
        //ro.build.description
        deviceData.setRo_build_description("klteuc-user 5.1.1 LMY47X G900AUCU4CPA1 release-keys");
        //ro.build.fingerprint
        deviceData.setRo_build_fingerprint("samsung/klteuc/klteatt:5.1.1/LMY47X/G900AUCU4CPA1:user/release-keys");

        addDevaceToRealm(deviceData);


    }

    private void fillFromFiles() {

        int idForDevaceData = 0;
        DeviceDataRealm deviceData;
        List<File> files = getListFiles(new File(
                Environment.getExternalStorageDirectory().toString()
                        + "/Download/build_prop/build_prop/"));

        if (files.size() == 0) {
            Log.e(TAG, "/Download/build_prop/build_prop/ is EMPTY");
        }

        for (File file : files) {
            if (file.exists()) {
                try {
                    deviceData = new DeviceDataRealm();
                    deviceData.setId(idForDevaceData);
                    deviceData.setName(file.getName());

                    BufferedReader br = new BufferedReader(new FileReader(file));
                    try {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.contains(AppConstants.ro_build_version_sdk + "=")) {
                                try {
                                    deviceData.setRo_build_version_sdk(Integer.valueOf(line.replace(AppConstants.ro_build_version_sdk + "=", "")));
                                } catch (Exception e) {
                                    Log.e(TAG, "Exception", e);
                                }
                            } else if (line.contains(AppConstants.ro_build_version_release + "=")) {
                                deviceData.setRo_build_version_release(line.replace(AppConstants.ro_build_version_release + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_id + "=")) {
                                deviceData.setRo_build_id(line.replace(AppConstants.ro_build_id + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_display_id + "=")) {
                                deviceData.setRo_build_display_id(line.replace(AppConstants.ro_build_display_id + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_version_incremental + "=")) {
                                deviceData.setRo_build_version_incremental(line.replace(AppConstants.ro_build_version_incremental + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_date + "=")) {
                                deviceData.setRo_build_date(line.replace(AppConstants.ro_build_date + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_date_utc + "=")) {
                                deviceData.setRo_build_date_utc(line.replace(AppConstants.ro_build_date_utc + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_user + "=")) {
                                deviceData.setRo_build_user(line.replace(AppConstants.ro_build_user + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_host + "=")) {
                                deviceData.setRo_build_host(line.replace(AppConstants.ro_build_host + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_flavor + "=")) {
                                deviceData.setRo_build_flavor(line.replace(AppConstants.ro_build_flavor + "=", ""));

                            } else if (line.contains(AppConstants.ro_product_model + "=")) {
                                deviceData.setRo_product_model(line.replace(AppConstants.ro_product_model + "=", ""));

                            } else if (line.contains(AppConstants.ro_product_brand + "=")) {
                                deviceData.setRo_product_brand(line.replace(AppConstants.ro_product_brand + "=", ""));

                            } else if (line.contains(AppConstants.ro_product_name + "=")) {
                                deviceData.setRo_product_name(line.replace(AppConstants.ro_product_name + "=", ""));

                            } else if (line.contains(AppConstants.ro_product_device + "=")) {
                                deviceData.setRo_product_device(line.replace(AppConstants.ro_product_device + "=", ""));

                            } else if (line.contains(AppConstants.ro_product_board + "=")) {
                                deviceData.setRo_product_board(line.replace(AppConstants.ro_product_board + "=", ""));

                            } else if (line.contains(AppConstants.ro_product_manufacturer + "=")) {
                                deviceData.setRo_product_manufacturer(line.replace(AppConstants.ro_product_manufacturer + "=", ""));

                            } else if (line.contains(AppConstants.ro_product_locale_language + "=")) {
                                deviceData.setRo_product_locale_language(line.replace(AppConstants.ro_product_locale_language + "=", ""));

                            } else if (line.contains(AppConstants.ro_product_locale_region + "=")) {
                                deviceData.setRo_product_locale_region(line.replace(AppConstants.ro_product_locale_region + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_description + "=")) {
                                deviceData.setRo_build_description(line.replace(AppConstants.ro_build_description + "=", ""));

                            } else if (line.contains(AppConstants.ro_build_fingerprint + "=")) {
                                deviceData.setRo_build_fingerprint(line.replace(AppConstants.ro_build_fingerprint + "=", ""));
                            }
                        }
                    } finally {
                        try {
                            br.close();
                        } catch (Exception e) {
                            Log.e(TAG, "e:" + e.toString());
                        }
                    }

                    addDevaceToRealm(deviceData);
                    idForDevaceData++;
                } catch (Exception e) {
                    Log.e(TAG, "Exception", e);
                }
            }
        }
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        if (files != null && files.length > 0 ) {
            for (File file : files) {
                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file));
                } else {
                    if (file.getName().endsWith(".txt")) {
                        inFiles.add(file);
                    }
                }
            }
        }
        return inFiles;
    }




}
