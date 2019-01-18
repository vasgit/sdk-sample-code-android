package org.qtproject.hexsudoku.realmmodel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeviceDataRealm extends RealmObject {
    @PrimaryKey
    private long id;

    private String name;
    private String ro_build_version_sdk;
    private String ro_build_version_release;
    private String ro_build_id;
    private String ro_build_display_id;
    private String ro_build_version_incremental;
    private String ro_build_date;
    private String ro_build_date_utc;
    private String ro_build_user;
    private String ro_build_host;
    private String ro_build_flavor;
    private String ro_product_model;
    private String ro_product_brand;
    private String ro_product_name;
    private String ro_product_device;
    private String ro_product_board;
    private String ro_product_manufacturer;
    private String ro_build_tags;
    private String ro_product_locale_language;
    private String ro_product_locale_region;
    private String ro_build_description;
    private String ro_build_fingerprint;
    private String ro_opengles_version;
    private String ro_carrier;

    public DeviceDataRealm() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRo_build_version_sdk() {
        return ro_build_version_sdk;
    }

    public String getRo_build_version_release() {
        return ro_build_version_release;
    }

    public String getRo_build_id() {
        return ro_build_id;
    }

    public String getRo_build_display_id() {
        return ro_build_display_id;
    }

    public String getRo_build_version_incremental() {
        return ro_build_version_incremental;
    }

    public String getRo_build_date() {
        return ro_build_date;
    }

    public String getRo_build_date_utc() {
        return ro_build_date_utc;
    }

    public String getRo_build_user() {
        return ro_build_user;
    }

    public String getRo_build_host() {
        return ro_build_host;
    }

    public String getRo_build_flavor() {
        return ro_build_flavor;
    }

    public String getRo_product_model() {
        return ro_product_model;
    }

    public String getRo_product_brand() {
        return ro_product_brand;
    }

    public String getRo_product_name() {
        return ro_product_name;
    }

    public String getRo_product_device() {
        return ro_product_device;
    }

    public String getRo_product_board() {
        return ro_product_board;
    }

    public String getRo_product_manufacturer() {
        return ro_product_manufacturer;
    }

    public String getRo_build_tags() {
        return ro_build_tags;
    }

    public String getRo_product_locale_language() {
        return ro_product_locale_language;
    }

    public String getRo_product_locale_region() {
        return ro_product_locale_region;
    }

    public String getRo_build_description() {
        return ro_build_description;
    }

    public String getRo_build_fingerprint() {
        return ro_build_fingerprint;
    }

    public String getRo_opengles_version() {
        return ro_opengles_version;
    }

    public String getRo_carrier() {
        return ro_carrier;
    }






    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRo_build_version_sdk(String ro_build_version_sdk) {
        this.ro_build_version_sdk = ro_build_version_sdk;
    }

    public void setRo_build_version_release(String ro_build_version_release) {
        this.ro_build_version_release = ro_build_version_release;
    }

    public void setRo_build_id(String ro_build_id) {
        this.ro_build_id = ro_build_id;
    }

    public void setRo_build_display_id(String ro_build_display_id) {
        this.ro_build_display_id = ro_build_display_id;
    }

    public void setRo_build_version_incremental(String ro_build_version_incremental) {
        this.ro_build_version_incremental = ro_build_version_incremental;
    }

    public void setRo_build_date(String ro_build_date) {
        this.ro_build_date = ro_build_date;
    }

    public void setRo_build_date_utc(String ro_build_date_utc) {
        this.ro_build_date_utc = ro_build_date_utc;
    }

    public void setRo_build_user(String ro_build_user) {
        this.ro_build_user = ro_build_user;
    }

    public void setRo_build_host(String ro_build_host) {
        this.ro_build_host = ro_build_host;
    }

    public void setRo_build_flavor(String ro_build_flavor) {
        this.ro_build_flavor = ro_build_flavor;
    }

    public void setRo_product_model(String ro_product_model) {
        this.ro_product_model = ro_product_model;
    }

    public void setRo_product_brand(String ro_product_brand) {
        this.ro_product_brand = ro_product_brand;
    }

    public void setRo_product_name(String ro_product_name) {
        this.ro_product_name = ro_product_name;
    }

    public void setRo_product_device(String ro_product_device) {
        this.ro_product_device = ro_product_device;
    }

    public void setRo_product_board(String ro_product_board) {
        this.ro_product_board = ro_product_board;
    }

    public void setRo_product_manufacturer(String ro_product_manufacturer) {
        this.ro_product_manufacturer = ro_product_manufacturer;
    }

    public void setRo_build_tags(String ro_build_tags) {
        this.ro_build_tags = ro_build_tags;
    }

    public void setRo_product_locale_language(String ro_product_locale_language) {
        this.ro_product_locale_language = ro_product_locale_language;
    }

    public void setRo_product_locale_region(String ro_product_locale_region) {
        this.ro_product_locale_region = ro_product_locale_region;
    }

    public void setRo_build_description(String ro_build_description) {
        this.ro_build_description = ro_build_description;
    }

    public void setRo_build_fingerprint(String ro_build_fingerprint) {
        this.ro_build_fingerprint = ro_build_fingerprint;
    }

    public void setRo_opengles_version(String ro_opengles_version) {
        this.ro_opengles_version = ro_opengles_version;
    }

    public void setRo_carrier(String ro_carrier) {
        this.ro_carrier = ro_carrier;
    }
}
