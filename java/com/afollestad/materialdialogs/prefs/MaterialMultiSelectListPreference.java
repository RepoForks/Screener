package com.afollestad.materialdialogs.prefs;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.MultiSelectListPreference;
import android.preference.Preference.BaseSavedState;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackMultiChoice;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.zhanghai.android.materialprogressbar.R;

@TargetApi(11)
public class MaterialMultiSelectListPreference extends MultiSelectListPreference {
    private Context context;
    private MaterialDialog mDialog;

    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$afollestad$materialdialogs$DialogAction = new int[DialogAction.values().length];

        static {
            try {
                $SwitchMap$com$afollestad$materialdialogs$DialogAction[DialogAction.NEUTRAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$afollestad$materialdialogs$DialogAction[DialogAction.NEGATIVE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Bundle dialogBundle;
        boolean isDialogShowing;

        public SavedState(Parcel source) {
            boolean z = true;
            super(source);
            if (source.readInt() != 1) {
                z = false;
            }
            this.isDialogShowing = z;
            this.dialogBundle = source.readBundle();
        }

        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.isDialogShowing ? 1 : 0);
            dest.writeBundle(this.dialogBundle);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public MaterialMultiSelectListPreference(Context context) {
        super(context);
        init(context, null);
    }

    public MaterialMultiSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(21)
    public MaterialMultiSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public MaterialMultiSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setEntries(CharSequence[] entries) {
        super.setEntries(entries);
        if (this.mDialog != null) {
            this.mDialog.setItems(entries);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        PrefUtil.setLayoutResource(context, this, attrs);
        if (VERSION.SDK_INT <= 10) {
            setWidgetLayoutResource(0);
        }
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    protected void showDialog(Bundle state) {
        List<Integer> indices = new ArrayList();
        for (String s : getValues()) {
            if (findIndexOfValue(s) >= 0) {
                indices.add(Integer.valueOf(findIndexOfValue(s)));
            }
        }
        Builder builder = new Builder(this.context).title(getDialogTitle()).icon(getDialogIcon()).negativeText(getNegativeButtonText()).positiveText(getPositiveButtonText()).onAny(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                switch (AnonymousClass3.$SwitchMap$com$afollestad$materialdialogs$DialogAction[which.ordinal()]) {
                    case R.styleable.View_android_focusable /*1*/:
                        MaterialMultiSelectListPreference.this.onClick(dialog, -3);
                        return;
                    case R.styleable.View_paddingStart /*2*/:
                        MaterialMultiSelectListPreference.this.onClick(dialog, -2);
                        return;
                    default:
                        MaterialMultiSelectListPreference.this.onClick(dialog, -1);
                        return;
                }
            }
        }).items(getEntries()).itemsCallbackMultiChoice((Integer[]) indices.toArray(new Integer[indices.size()]), new ListCallbackMultiChoice() {
            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                MaterialMultiSelectListPreference.this.onClick(null, -1);
                dialog.dismiss();
                Set<String> values = new HashSet();
                for (Integer intValue : which) {
                    values.add(MaterialMultiSelectListPreference.this.getEntryValues()[intValue.intValue()].toString());
                }
                if (MaterialMultiSelectListPreference.this.callChangeListener(values)) {
                    MaterialMultiSelectListPreference.this.setValues(values);
                }
                return true;
            }
        }).dismissListener(this);
        View contentView = onCreateDialogView();
        if (contentView != null) {
            onBindDialogView(contentView);
            builder.customView(contentView, false);
        } else {
            builder.content(getDialogMessage());
        }
        PrefUtil.registerOnActivityDestroyListener(this, this);
        this.mDialog = builder.build();
        if (state != null) {
            this.mDialog.onRestoreInstanceState(state);
        }
        this.mDialog.show();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        PrefUtil.unregisterOnActivityDestroyListener(this, this);
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Dialog dialog = getDialog();
        if (dialog == null || !dialog.isShowing()) {
            return superState;
        }
        Parcelable myState = new SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = dialog.onSaveInstanceState();
        return myState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }
}
