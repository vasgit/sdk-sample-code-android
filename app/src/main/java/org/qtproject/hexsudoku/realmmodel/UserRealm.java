package org.qtproject.hexsudoku.realmmodel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserRealm extends RealmObject {
    @PrimaryKey
    private int id;

    private String android_id;

    private DeviceDataRealm device;

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
}
