package com.mikepenz.aboutlibraries.util;

import android.content.Context;
import android.text.TextUtils;
import com.mikepenz.aboutlibraries.Libs;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class GenericsUtil {
    public static String[] getFields(Context ctx) {
        Class rStringClass = resolveRClass(ctx.getPackageName());
        if (rStringClass != null) {
            return Libs.toStringArray(rStringClass.getFields());
        }
        return new String[0];
    }

    private static Class resolveRClass(String packageName) {
        do {
            try {
                return Class.forName(packageName + ".R$string");
            } catch (ClassNotFoundException e) {
                packageName = packageName.contains(".") ? packageName.substring(0, packageName.lastIndexOf(46)) : BuildConfig.FLAVOR;
                if (TextUtils.isEmpty(packageName)) {
                    return null;
                }
            }
        } while (TextUtils.isEmpty(packageName));
        return null;
    }
}
