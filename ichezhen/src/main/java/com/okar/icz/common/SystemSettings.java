package com.okar.icz.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.util.HashSet;
import java.util.Set;

public class SystemSettings {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static SystemSettings getSystemSettings(Context context) {
        return new SystemSettings(context, "settings");
    }

    public SystemSettings(Context context, String file) {
        sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setUid(int uid) {
        editor.putInt("uid", uid);
        editor.commit();
    }

    public int getUid() {
        return sp.getInt("uid", 0);
    }

    public void setAccountId(int accountId) {
        editor.putInt("accountId", accountId);
        editor.commit();
    }

    public int getAccountId() {
        return sp.getInt("accountId", 0);
    }

    public static String getProp(Context context, String key) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object result = appInfo.metaData.get(key);
            if(result!=null)
                return result.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    
}