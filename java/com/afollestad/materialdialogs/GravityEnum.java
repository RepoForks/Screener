package com.afollestad.materialdialogs;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import me.zhanghai.android.materialprogressbar.R;

public enum GravityEnum {
    START,
    CENTER,
    END;
    
    private static final boolean HAS_RTL = false;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$afollestad$materialdialogs$GravityEnum = null;

        static {
            $SwitchMap$com$afollestad$materialdialogs$GravityEnum = new int[GravityEnum.values().length];
            try {
                $SwitchMap$com$afollestad$materialdialogs$GravityEnum[GravityEnum.START.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$afollestad$materialdialogs$GravityEnum[GravityEnum.CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$afollestad$materialdialogs$GravityEnum[GravityEnum.END.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    @SuppressLint({"RtlHardcoded"})
    public int getGravityInt() {
        switch (AnonymousClass1.$SwitchMap$com$afollestad$materialdialogs$GravityEnum[ordinal()]) {
            case R.styleable.View_android_focusable /*1*/:
                return HAS_RTL ? 8388611 : 3;
            case R.styleable.View_paddingStart /*2*/:
                return 1;
            case R.styleable.View_paddingEnd /*3*/:
                return HAS_RTL ? 8388613 : 5;
            default:
                throw new IllegalStateException("Invalid gravity constant");
        }
    }

    @TargetApi(17)
    public int getTextAlignment() {
        switch (AnonymousClass1.$SwitchMap$com$afollestad$materialdialogs$GravityEnum[ordinal()]) {
            case R.styleable.View_paddingStart /*2*/:
                return 4;
            case R.styleable.View_paddingEnd /*3*/:
                return 6;
            default:
                return 5;
        }
    }
}
