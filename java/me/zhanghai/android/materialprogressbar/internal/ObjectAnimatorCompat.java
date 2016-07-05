package me.zhanghai.android.materialprogressbar.internal;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Build.VERSION;
import android.util.Property;

public class ObjectAnimatorCompat {
    private ObjectAnimatorCompat() {
    }

    public static ObjectAnimator ofArgb(Object target, String propertyName, int... values) {
        if (VERSION.SDK_INT >= 21) {
            return ObjectAnimatorCompatLollipop.ofArgb(target, propertyName, values);
        }
        return ObjectAnimatorCompatBase.ofArgb(target, propertyName, values);
    }

    public static <T> ObjectAnimator ofArgb(T target, Property<T, Integer> property, int... values) {
        if (VERSION.SDK_INT >= 21) {
            return ObjectAnimatorCompatLollipop.ofArgb((Object) target, (Property) property, values);
        }
        return ObjectAnimatorCompatBase.ofArgb((Object) target, (Property) property, values);
    }

    public static ObjectAnimator ofFloat(Object target, String xPropertyName, String yPropertyName, Path path) {
        if (VERSION.SDK_INT >= 21) {
            return ObjectAnimatorCompatLollipop.ofFloat(target, xPropertyName, yPropertyName, path);
        }
        return ObjectAnimatorCompatBase.ofFloat(target, xPropertyName, yPropertyName, path);
    }

    public static <T> ObjectAnimator ofFloat(T target, Property<T, Float> xProperty, Property<T, Float> yProperty, Path path) {
        if (VERSION.SDK_INT >= 21) {
            return ObjectAnimatorCompatLollipop.ofFloat((Object) target, (Property) xProperty, (Property) yProperty, path);
        }
        return ObjectAnimatorCompatBase.ofFloat((Object) target, (Property) xProperty, (Property) yProperty, path);
    }

    public static ObjectAnimator ofInt(Object target, String xPropertyName, String yPropertyName, Path path) {
        if (VERSION.SDK_INT >= 21) {
            return ObjectAnimatorCompatLollipop.ofInt(target, xPropertyName, yPropertyName, path);
        }
        return ObjectAnimatorCompatBase.ofInt(target, xPropertyName, yPropertyName, path);
    }

    public static <T> ObjectAnimator ofInt(T target, Property<T, Integer> xProperty, Property<T, Integer> yProperty, Path path) {
        if (VERSION.SDK_INT >= 21) {
            return ObjectAnimatorCompatLollipop.ofInt((Object) target, (Property) xProperty, (Property) yProperty, path);
        }
        return ObjectAnimatorCompatBase.ofInt((Object) target, (Property) xProperty, (Property) yProperty, path);
    }
}
