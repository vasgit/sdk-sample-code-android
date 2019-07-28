package org.qtproject.hexsudoku.realmmodel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private String android_id;
    private String advertising_id;
    private String IMEI_id;
    private int ro_build_version_sdk;



    private DeviceDataRealm device;


    public UserRealm() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAndroid_id() { return android_id; }

    public void setAndroid_id(String android_id) { this.android_id = android_id; }

    public DeviceDataRealm getDevice() {
        return device;
    }

    public void setDevice(DeviceDataRealm device) {
        this.device = device;
    }

    public String getAdvertising_id() {
        return advertising_id;
    }

    public void setAdvertising_id(String advertising_id) {
        this.advertising_id = advertising_id;
    }

    public String getIMEI_id() {
        return IMEI_id;
    }

    public void setIMEI_id(String IMEI_id) {
        this.IMEI_id = IMEI_id;
    }

    public int getRo_build_version_sdk() {
        return ro_build_version_sdk;
    }

    public void setRo_build_version_sdk(int ro_build_version_sdk) {
        this.ro_build_version_sdk = ro_build_version_sdk;
    }
}
