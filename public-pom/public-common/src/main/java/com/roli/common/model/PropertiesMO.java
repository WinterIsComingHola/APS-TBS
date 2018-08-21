package com.roli.common.model;

import com.roli.common.exception.SecurityException;

import java.util.Map;

import static com.roli.common.utils.security.AESHandle.decryptAES;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/1/26 0:05
 */
public class PropertiesMO {

    private int iD;
    private String profileName;
    private String appName;
    private String iP;
    private int ipAuth;
    private String key;
    private String val;
    private int encrypt;
    private int reload;

    public static String detail(Map<Object, Object> properties) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            builder.append(entry.getKey() + "\t");
        }
        return builder.toString();
    }


    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getiP() {
        return iP;
    }

    public void setiP(String iP) {
        this.iP = iP;
    }

    public int getIpAuth() {
        return ipAuth;
    }

    public void setIpAuth(int ipAuth) {
        this.ipAuth = ipAuth;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() throws SecurityException {

        if(encrypt == 1){
            return decryptAES(null,val);
        }

        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public int getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(int encrypt) {
        this.encrypt = encrypt;
    }

    public int getReload() {
        return reload;
    }

    public void setReload(int reload) {
        this.reload = reload;
    }
}
