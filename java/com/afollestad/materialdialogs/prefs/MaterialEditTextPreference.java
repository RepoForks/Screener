package com.afollestad.materialdialogs.prefs;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.EditTextPreference;
import android.preference.Preference.BaseSavedState;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.afollestad.materialdialogs.commons.R;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.util.DialogUtils;

public class MaterialEditTextPreference extends EditTextPreference {
    private int mColor = 0;
    private MaterialDialog mDialog;
    private EditText mEditText;

    static /* synthetic */ class AnonymousClass2 {
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

    public MaterialEditTextPreference(Context context) {
        super(context);
        init(context, null);
    }

    public MaterialEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(21)
    public MaterialEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public MaterialEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int fallback;
        PrefUtil.setLayoutResource(context, this, attrs);
        if (VERSION.SDK_INT >= 21) {
            fallback = DialogUtils.resolveColor(context, 16843829);
        } else {
            fallback = 0;
        }
        this.mColor = DialogUtils.resolveColor(context, R.attr.md_widget_color, DialogUtils.resolveColor(context, R.attr.colorAccent, fallback));
        this.mEditText = new AppCompatEditText(context, attrs);
        this.mEditText.setId(16908291);
        this.mEditText.setEnabled(true);
    }

    protected void onAddEditTextToDialogView(@NonNull View dialogView, @NonNull EditText editText) {
        ((ViewGroup) dialogView).addView(editText, new LayoutParams(-1, -2));
    }

    @SuppressLint({"MissingSuperCall"})
    protected void onBindDialogView(@NonNull View view) {
        EditText editText = this.mEditText;
        editText.setText(getText());
        if (editText.getText().length() > 0) {
            editText.setSelection(editText.length());
        }
        View oldParent = editText.getParent();
        if (oldParent != view) {
            if (oldParent != null) {
                ((ViewGroup) oldParent).removeView(editText);
            }
            onAddEditTextToDialogView(view, editText);
        }
    }

    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = this.mEditText.getText().toString();
            if (callChangeListener(value)) {
                setText(value);
            }
        }
    }

    public EditText getEditText() {
        return this.mEditText;
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    protected void showDialog(Bundle state) {
        Builder mBuilder = new Builder(getContext()).title(getDialogTitle()).icon(getDialogIcon()).positiveText(getPositiveButtonText()).negativeText(getNegativeButtonText()).dismissListener(this).onAny(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                switch (AnonymousClass2.$SwitchMap$com$afollestad$materialdialogs$DialogAction[which.ordinal()]) {
                    case me.zhanghai.android.materialprogressbar.R.styleable.View_android_focusable /*1*/:
                        MaterialEditTextPreference.this.onClick(dialog, -3);
                        return;
                    case me.zhanghai.android.materialprogressbar.R.styleable.View_paddingStart /*2*/:
                        MaterialEditTextPreference.this.onClick(dialog, -2);
                        return;
                    default:
                        MaterialEditTextPreference.this.onClick(dialog, -1);
                        return;
                }
            }
        }).dismissListener(this);
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.md_stub_inputpref, null);
        onBindDialogView(layout);
        MDTintHelper.setTint(this.mEditText, this.mColor);
        TextView message = (TextView) layout.findViewById(16908299);
        if (getDialogMessage() == null || getDialogMessage().toString().length() <= 0) {
            message.setVisibility(8);
        } else {
            message.setVisibility(0);
            message.setText(getDialogMessage());
        }
        mBuilder.customView(layout, false);
        PrefUtil.registerOnActivityDestroyListener(this, this);
        this.mDialog = mBuilder.build();
        if (state != null) {
            this.mDialog.onRestoreInstanceState(state);
        }
        requestInputMethod(this.mDialog);
        this.mDialog.show();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        PrefUtil.unregisterOnActivityDestroyListener(this, this);
    }

    private void requestInputMethod(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(5);
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
