package com.mikepenz.aboutlibraries.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

public class Util {
    public static PackageInfo getPackageInfo(Context ctx) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (Exception e) {
        }
        return packageInfo;
    }

    public static ApplicationInfo getApplicationInfo(Context ctx) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), 0);
        } catch (Exception e) {
        }
        return appInfo;
    }
}
