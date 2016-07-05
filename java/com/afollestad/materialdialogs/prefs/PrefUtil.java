package com.afollestad.materialdialogs.prefs;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceManager.OnActivityDestroyListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.afollestad.materialdialogs.commons.R;
import java.lang.reflect.Method;

class PrefUtil {
    private PrefUtil() {
    }

    public static void setLayoutResource(@NonNull Context context, @NonNull Preference preference, @Nullable AttributeSet attrs) {
        boolean foundLayout = false;
        if (attrs != null) {
            int i = 0;
            while (i < attrs.getAttributeCount()) {
                if (((XmlResourceParser) attrs).getAttributeNamespace(0).equals("http://schemas.android.com/apk/res/android") && attrs.getAttributeName(i).equals("layout")) {
                    foundLayout = true;
                    break;
                }
                i++;
            }
        }
        boolean useStockLayout = false;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Preference, 0, 0);
            try {
                useStockLayout = a.getBoolean(R.styleable.Preference_useStockLayout, false);
            } finally {
                a.recycle();
            }
        }
        if (!foundLayout && !useStockLayout) {
            preference.setLayoutResource(R.layout.md_preference_custom);
        }
    }

    public static void registerOnActivityDestroyListener(@NonNull Preference preference, @NonNull OnActivityDestroyListener listener) {
        try {
            PreferenceManager pm = preference.getPreferenceManager();
            Method method = pm.getClass().getDeclaredMethod("registerOnActivityDestroyListener", new Class[]{OnActivityDestroyListener.class});
            method.setAccessible(true);
            method.invoke(pm, new Object[]{listener});
        } catch (Exception e) {
        }
    }

    public static void unregisterOnActivityDestroyListener(@NonNull Preference preference, @NonNull OnActivityDestroyListener listener) {
        try {
            PreferenceManager pm = preference.getPreferenceManager();
            Method method = pm.getClass().getDeclaredMethod("unregisterOnActivityDestroyListener", new Class[]{OnActivityDestroyListener.class});
            method.setAccessible(true);
            method.invoke(pm, new Object[]{listener});
        } catch (Exception e) {
        }
    }
}
