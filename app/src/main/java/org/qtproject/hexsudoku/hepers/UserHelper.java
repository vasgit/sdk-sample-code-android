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
        int userId = 0;

        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<UserRealm> userRealms = mRealm.where(UserRealm.class).equalTo("id", userId).findAll();
        if (userRealms != null && !userRealms.isEmpty()) {
            chengeUser(userRealms.get(0));
        }
    }

    private void chengeUser(UserRealm userRealm) {
        //todo poverLevel 16% - 100%

        Random r = new Random();
        int poverLevel = r.nextInt(100 - 16) + 16;

        Log.d(AppConstants.TOTAL_TAG, "start chengeUser:");
        Log.d(AppConstants.TOTAL_TAG, "getAndroid_id: " + userRealm.getAndroid_id());
        Log.d(AppConstants.TOTAL_TAG, "getIMEI_id: " + userRealm.getIMEI_id());
        Log.d(AppConstants.TOTAL_TAG, "poverLevel: " + poverLevel);

        deviceHelper.chengeAndroidID(userRealm.getAndroid_id(), userRealm.getIMEI_id(), poverLevel);
    }


    public void checkNeedGenerateUsers() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<UserRealm> userRealms = mRealm.where(UserRealm.class).findAll();
        //todo
//        if (userRealms == null || userRealms.isEmpty()) {
            generateUsers();
//        }
    }

    private void generateUsers() {
        Realm mRealm = Realm.getDefaultInstance();
        final RealmResults<DeviceDataRealm> deviceDataRealms = mRealm.where(DeviceDataRealm.class)
                .equalTo("ro_build_version_sdk", android.os.Build.VERSION.SDK_INT)
                .findAll();
        if (deviceDataRealms != null && !deviceDataRealms.isEmpty()) {
            int i = 0;
            for (DeviceDataRealm device : deviceDataRealms) {
                final UserRealm userRealm = new UserRealm();
                userRealm.setId(i);
                userRealm.setAdvertising_id(generateAdvertisingID());
                userRealm.setAndroid_id(generateAndroidID());
                userRealm.setIMEI_id(generateIMEI());
                userRealm.setDevice(device);
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(userRealm);
                    }
                });
                i++;
            }
        }
    }

    private String generateIMEI() {
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
