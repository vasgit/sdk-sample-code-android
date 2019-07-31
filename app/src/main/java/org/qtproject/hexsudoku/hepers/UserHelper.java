package org.qtproject.hexsudoku.hepers;

import android.app.Activity;
import android.util.Log;

import org.qtproject.hexsudoku.constants.AppConstants;
import org.qtproject.hexsudoku.realmmodel.DeviceDataRealm;
import org.qtproject.hexsudoku.realmmodel.UserRealm;

import java.security.SecureRandom;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserHelper {

    private Activity activity;
    private DeviceHelper deviceHelper;



    public UserHelper(Activity activity) {
        this.activity = activity;
        this.deviceHelper = new DeviceHelper(activity);
    }

    public void chengeUser() {
        //todo
        int userId = 0;

        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<UserRealm> userRealms = mRealm.where(UserRealm.class).findAll();
        if (userRealms != null && userRealms.size() > 0) {
            Random r = new Random();
            userId = r.nextInt(userRealms.size());
        }

        userRealms = mRealm.where(UserRealm.class).equalTo("id", userId).findAll();
        if (userRealms != null && !userRealms.isEmpty()) {
            deviceHelper.changeDevice(userRealms.get(0));
        }
    }

    public void showUsers() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<UserRealm> userRealms = mRealm.where(UserRealm.class).findAll();
        if (userRealms != null && !userRealms.isEmpty()) {
            for (UserRealm user : userRealms) {
                Log.d(AppConstants.TOTAL_TAG, "id: " + user.getId());

                Log.d(AppConstants.TOTAL_TAG, "android_id: " + user.getAndroid_id());
                Log.d(AppConstants.TOTAL_TAG, "advertising_id: " + user.getAdvertising_id());
                Log.d(AppConstants.TOTAL_TAG, "IMEI_id: " + user.getIMEI_id());
                Log.d(AppConstants.TOTAL_TAG, "ro_build_version_sdk: " + user.getRo_build_version_sdk());
                Log.d(AppConstants.TOTAL_TAG, "getDevice: " + user.getDevice());
            }
        }
    }

    public void deleteUsers() {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<UserRealm> userRealms = realm.where(UserRealm.class).findAll();
                if (userRealms != null && userRealms.size() > 0) {
                    userRealms.clear();
                    Log.d(AppConstants.TOTAL_TAG, "userRealms.clear()");
                }
            }
        });
    }

    public void createUsers() {
        Realm mRealm = Realm.getDefaultInstance();
        final RealmResults<DeviceDataRealm> deviceDataRealms = mRealm.where(DeviceDataRealm.class)
                .equalTo("ro_build_version_sdk", android.os.Build.VERSION.SDK_INT)
                .findAll();
        if (deviceDataRealms != null && !deviceDataRealms.isEmpty()) {
            int i = 0;
            for (DeviceDataRealm device : deviceDataRealms) {
                final UserRealm userRealm = new UserRealm();
                userRealm.setId(i);
                userRealm.setAndroid_id(generateAndroidID());
                userRealm.setAdvertising_id(generateAdvertisingID());
                userRealm.setIMEI_id(generateIMEI());
                userRealm.setDevice(device);
                userRealm.setRo_build_version_sdk(device.getRo_build_version_sdk());
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(userRealm);
                    }
                });
                Log.d(AppConstants.TOTAL_TAG, "createUser: " + i);
                i++;
            }
        }
    }

    private String generateIMEI() {
        //todo: передивитися
        String charForGenerate = "1029384756123456789065748392010987654321";
        StringBuilder sbId = new StringBuilder();

        for (int i = 0; i < 15; i++) {
            Random r = new Random();
            int idex = r.nextInt(charForGenerate.length() - 1);
            sbId.append(charForGenerate.charAt(idex));
        }

        Log.d(AppConstants.TOTAL_TAG, "generateIMEI: " + sbId.toString());
        return sbId.toString();
    }

    private String generateAndroidID() {
        SecureRandom random = new SecureRandom();
        String newAndroidIdValue = Long.toHexString(random.nextLong());

        Log.d(AppConstants.TOTAL_TAG, "generateAndroidID: " + newAndroidIdValue);
        return newAndroidIdValue;
    }


    private String generateAdvertisingID() {
        //todo: передивитися
        String charForGenerate = "1q2w3e4r5t6y7u8i9o0p1a2s3d4f5g6h7j8k9l0zxcvbnm";
        StringBuilder sbId = new StringBuilder();

        for (int i = 0; i < 36; i++) {
            if (i == 8 || i == 13 || i == 18 || i == 23) {
                sbId.append("-");
            } else {
                Random r = new Random();
                int idex = r.nextInt(charForGenerate.length() - 1);
                sbId.append(charForGenerate.charAt(idex));
            }
        }

        Log.d(AppConstants.TOTAL_TAG, "generateAdvertisingID: " + sbId.toString());
        return sbId.toString();
    }



}
