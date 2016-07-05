package com.afollestad.materialdialogs.color;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.afollestad.materialdialogs.Theme;
import com.afollestad.materialdialogs.commons.R;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.util.DialogUtils;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ColorChooserDialog extends DialogFragment implements OnClickListener, OnLongClickListener {
    public static final String TAG_ACCENT = "[MD_COLOR_CHOOSER]";
    public static final String TAG_CUSTOM = "[MD_COLOR_CHOOSER]";
    public static final String TAG_PRIMARY = "[MD_COLOR_CHOOSER]";
    private ColorCallback mCallback;
    private int mCircleSize;
    private View mColorChooserCustomFrame;
    @Nullable
    private int[][] mColorsSub;
    @NonNull
    private int[] mColorsTop;
    private EditText mCustomColorHex;
    private View mCustomColorIndicator;
    private OnSeekBarChangeListener mCustomColorRgbListener;
    private TextWatcher mCustomColorTextWatcher;
    private SeekBar mCustomSeekA;
    private TextView mCustomSeekAValue;
    private SeekBar mCustomSeekB;
    private TextView mCustomSeekBValue;
    private SeekBar mCustomSeekG;
    private TextView mCustomSeekGValue;
    private SeekBar mCustomSeekR;
    private TextView mCustomSeekRValue;
    private GridView mGrid;
    private int mSelectedCustomColor;

    public static class Builder implements Serializable {
        protected boolean mAccentMode = false;
        protected boolean mAllowUserCustom = true;
        protected boolean mAllowUserCustomAlpha = true;
        @StringRes
        protected int mBackBtn = R.string.md_back_label;
        @StringRes
        protected int mCancelBtn = R.string.md_cancel_label;
        @Nullable
        protected int[][] mColorsSub;
        @Nullable
        protected int[] mColorsTop;
        @NonNull
        protected final transient AppCompatActivity mContext;
        @StringRes
        protected int mCustomBtn = R.string.md_custom_label;
        @StringRes
        protected int mDoneBtn = R.string.md_done_label;
        protected boolean mDynamicButtonColor = true;
        @ColorInt
        protected int mPreselect;
        @StringRes
        protected int mPresetsBtn = R.string.md_presets_label;
        protected boolean mSetPreselectionColor = false;
        @Nullable
        protected String mTag;
        @Nullable
        protected Theme mTheme;
        @StringRes
        protected final int mTitle;
        @StringRes
        protected int mTitleSub;

        public <ActivityType extends AppCompatActivity & ColorCallback> Builder(@NonNull ActivityType context, @StringRes int title) {
            this.mContext = context;
            this.mTitle = title;
        }

        @NonNull
        public Builder titleSub(@StringRes int titleSub) {
            this.mTitleSub = titleSub;
            return this;
        }

        @NonNull
        public Builder tag(@Nullable String tag) {
            this.mTag = tag;
            return this;
        }

        @NonNull
        public Builder theme(@NonNull Theme theme) {
            this.mTheme = theme;
            return this;
        }

        @NonNull
        public Builder preselect(@ColorInt int preselect) {
            this.mPreselect = preselect;
            this.mSetPreselectionColor = true;
            return this;
        }

        @NonNull
        public Builder accentMode(boolean accentMode) {
            this.mAccentMode = accentMode;
            return this;
        }

        @NonNull
        public Builder doneButton(@StringRes int text) {
            this.mDoneBtn = text;
            return this;
        }

        @NonNull
        public Builder backButton(@StringRes int text) {
            this.mBackBtn = text;
            return this;
        }

        @NonNull
        public Builder cancelButton(@StringRes int text) {
            this.mCancelBtn = text;
            return this;
        }

        @NonNull
        public Builder customButton(@StringRes int text) {
            this.mCustomBtn = text;
            return this;
        }

        @NonNull
        public Builder presetsButton(@StringRes int text) {
            this.mPresetsBtn = text;
            return this;
        }

        @NonNull
        public Builder dynamicButtonColor(boolean enabled) {
            this.mDynamicButtonColor = enabled;
            return this;
        }

        @NonNull
        public Builder customColors(@NonNull int[] topLevel, @Nullable int[][] subLevel) {
            this.mColorsTop = topLevel;
            this.mColorsSub = subLevel;
            return this;
        }

        @NonNull
        public Builder customColors(@ArrayRes int topLevel, @Nullable int[][] subLevel) {
            this.mColorsTop = DialogUtils.getColorArray(this.mContext, topLevel);
            this.mColorsSub = subLevel;
            return this;
        }

        @NonNull
        public Builder allowUserColorInput(boolean allow) {
            this.mAllowUserCustom = allow;
            return this;
        }

        @NonNull
        public Builder allowUserColorInputAlpha(boolean allow) {
            this.mAllowUserCustomAlpha = allow;
            return this;
        }

        @NonNull
        public ColorChooserDialog build() {
            ColorChooserDialog dialog = new ColorChooserDialog();
            Bundle args = new Bundle();
            args.putSerializable("builder", this);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        public ColorChooserDialog show() {
            ColorChooserDialog dialog = build();
            dialog.show(this.mContext);
            return dialog;
        }
    }

    public interface ColorCallback {
        void onColorSelection(@NonNull ColorChooserDialog colorChooserDialog, @ColorInt int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorChooserTag {
    }

    private class ColorGridAdapter extends BaseAdapter {
        public int getCount() {
            if (ColorChooserDialog.this.isInSub()) {
                return ColorChooserDialog.this.mColorsSub[ColorChooserDialog.this.topIndex()].length;
            }
            return ColorChooserDialog.this.mColorsTop.length;
        }

        public Object getItem(int position) {
            if (ColorChooserDialog.this.isInSub()) {
                return Integer.valueOf(ColorChooserDialog.this.mColorsSub[ColorChooserDialog.this.topIndex()][position]);
            }
            return Integer.valueOf(ColorChooserDialog.this.mColorsTop[position]);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        @SuppressLint({"DefaultLocale"})
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new CircleView(ColorChooserDialog.this.getContext());
                convertView.setLayoutParams(new LayoutParams(ColorChooserDialog.this.mCircleSize, ColorChooserDialog.this.mCircleSize));
            }
            CircleView child = (CircleView) convertView;
            child.setBackgroundColor(ColorChooserDialog.this.isInSub() ? ColorChooserDialog.this.mColorsSub[ColorChooserDialog.this.topIndex()][position] : ColorChooserDialog.this.mColorsTop[position]);
            if (ColorChooserDialog.this.isInSub()) {
                boolean z;
                if (ColorChooserDialog.this.subIndex() == position) {
                    z = true;
                } else {
                    z = false;
                }
                child.setSelected(z);
            } else {
                child.setSelected(ColorChooserDialog.this.topIndex() == position);
            }
            child.setTag(String.format("%d:%d", new Object[]{Integer.valueOf(position), Integer.valueOf(color)}));
            child.setOnClickListener(ColorChooserDialog.this);
            child.setOnLongClickListener(ColorChooserDialog.this);
            return convertView;
        }
    }

    private void generateColors() {
        Builder builder = getBuilder();
        if (builder.mColorsTop != null) {
            this.mColorsTop = builder.mColorsTop;
            this.mColorsSub = builder.mColorsSub;
        } else if (builder.mAccentMode) {
            this.mColorsTop = ColorPalette.ACCENT_COLORS;
            this.mColorsSub = ColorPalette.ACCENT_COLORS_SUB;
        } else {
            this.mColorsTop = ColorPalette.PRIMARY_COLORS;
            this.mColorsSub = ColorPalette.PRIMARY_COLORS_SUB;
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("top_index", topIndex());
        outState.putBoolean("in_sub", isInSub());
        outState.putInt("sub_index", subIndex());
        String str = "in_custom";
        boolean z = this.mColorChooserCustomFrame != null && this.mColorChooserCustomFrame.getVisibility() == 0;
        outState.putBoolean(str, z);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ColorCallback) {
            this.mCallback = (ColorCallback) activity;
            return;
        }
        throw new IllegalStateException("ColorChooserDialog needs to be shown from an Activity implementing ColorCallback.");
    }

    private boolean isInSub() {
        return getArguments().getBoolean("in_sub", false);
    }

    private void isInSub(boolean value) {
        getArguments().putBoolean("in_sub", value);
    }

    private int topIndex() {
        return getArguments().getInt("top_index", -1);
    }

    private void topIndex(int value) {
        if (topIndex() != value && value > -1) {
            findSubIndexForColor(value, this.mColorsTop[value]);
        }
        getArguments().putInt("top_index", value);
    }

    private int subIndex() {
        if (this.mColorsSub == null) {
            return -1;
        }
        return getArguments().getInt("sub_index", -1);
    }

    private void subIndex(int value) {
        if (this.mColorsSub != null) {
            getArguments().putInt("sub_index", value);
        }
    }

    @StringRes
    public int getTitle() {
        int title;
        Builder builder = getBuilder();
        if (isInSub()) {
            title = builder.mTitleSub;
        } else {
            title = builder.mTitle;
        }
        if (title == 0) {
            return builder.mTitle;
        }
        return title;
    }

    public String tag() {
        Builder builder = getBuilder();
        if (builder.mTag != null) {
            return builder.mTag;
        }
        return super.getTag();
    }

    public boolean isAccentMode() {
        return getBuilder().mAccentMode;
    }

    public void onClick(View v) {
        if (v.getTag() != null) {
            int index = Integer.parseInt(((String) v.getTag()).split(":")[0]);
            MaterialDialog dialog = (MaterialDialog) getDialog();
            Builder builder = getBuilder();
            if (isInSub()) {
                subIndex(index);
            } else {
                topIndex(index);
                if (this.mColorsSub != null && index < this.mColorsSub.length) {
                    dialog.setActionButton(DialogAction.NEGATIVE, builder.mBackBtn);
                    isInSub(true);
                }
            }
            if (builder.mAllowUserCustom) {
                this.mSelectedCustomColor = getSelectedColor();
            }
            invalidateDynamicButtonColors();
            invalidate();
        }
    }

    public boolean onLongClick(View v) {
        if (v.getTag() == null) {
            return false;
        }
        ((CircleView) v).showHint(Integer.parseInt(((String) v.getTag()).split(":")[1]));
        return true;
    }

    private void invalidateDynamicButtonColors() {
        MaterialDialog dialog = (MaterialDialog) getDialog();
        if (dialog != null && getBuilder().mDynamicButtonColor) {
            int selectedColor = getSelectedColor();
            if (Color.alpha(selectedColor) < 64 || (Color.red(selectedColor) > 247 && Color.green(selectedColor) > 247 && Color.blue(selectedColor) > 247)) {
                selectedColor = Color.parseColor("#DEDEDE");
            }
            if (getBuilder().mDynamicButtonColor) {
                dialog.getActionButton(DialogAction.POSITIVE).setTextColor(selectedColor);
                dialog.getActionButton(DialogAction.NEGATIVE).setTextColor(selectedColor);
                dialog.getActionButton(DialogAction.NEUTRAL).setTextColor(selectedColor);
            }
            if (this.mCustomSeekR != null) {
                if (this.mCustomSeekA.getVisibility() == 0) {
                    MDTintHelper.setTint(this.mCustomSeekA, selectedColor);
                }
                MDTintHelper.setTint(this.mCustomSeekR, selectedColor);
                MDTintHelper.setTint(this.mCustomSeekG, selectedColor);
                MDTintHelper.setTint(this.mCustomSeekB, selectedColor);
            }
        }
    }

    @ColorInt
    private int getSelectedColor() {
        if (this.mColorChooserCustomFrame != null && this.mColorChooserCustomFrame.getVisibility() == 0) {
            return this.mSelectedCustomColor;
        }
        int color = 0;
        if (subIndex() > -1) {
            color = this.mColorsSub[topIndex()][subIndex()];
        } else if (topIndex() > -1) {
            color = this.mColorsTop[topIndex()];
        }
        if (color != 0) {
            return color;
        }
        int fallback = 0;
        if (VERSION.SDK_INT >= 21) {
            fallback = DialogUtils.resolveColor(getActivity(), 16843829);
        }
        return DialogUtils.resolveColor(getActivity(), R.attr.colorAccent, fallback);
    }

    private void findSubIndexForColor(int topIndex, int color) {
        if (this.mColorsSub != null && this.mColorsSub.length - 1 >= topIndex) {
            int[] subColors = this.mColorsSub[topIndex];
            for (int subIndex = 0; subIndex < subColors.length; subIndex++) {
                if (subColors[subIndex] == color) {
                    subIndex(subIndex);
                    return;
                }
            }
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() == null || !getArguments().containsKey("builder")) {
            throw new IllegalStateException("ColorChooserDialog should be created using its Builder interface.");
        }
        int preselectColor;
        int i;
        generateColors();
        boolean foundPreselectColor = false;
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("in_custom", false)) {
                foundPreselectColor = false;
            } else {
                foundPreselectColor = true;
            }
            preselectColor = getSelectedColor();
        } else if (getBuilder().mSetPreselectionColor) {
            preselectColor = getBuilder().mPreselect;
            if (preselectColor != 0) {
                int topIndex = 0;
                while (topIndex < this.mColorsTop.length) {
                    if (this.mColorsTop[topIndex] == preselectColor) {
                        foundPreselectColor = true;
                        topIndex(topIndex);
                        if (getBuilder().mAccentMode) {
                            subIndex(2);
                        } else if (this.mColorsSub != null) {
                            findSubIndexForColor(topIndex, preselectColor);
                        } else {
                            subIndex(5);
                        }
                    } else {
                        if (this.mColorsSub != null) {
                            for (int subIndex = 0; subIndex < this.mColorsSub[topIndex].length; subIndex++) {
                                if (this.mColorsSub[topIndex][subIndex] == preselectColor) {
                                    foundPreselectColor = true;
                                    topIndex(topIndex);
                                    subIndex(subIndex);
                                    break;
                                }
                            }
                            if (foundPreselectColor) {
                                break;
                            }
                        }
                        topIndex++;
                    }
                }
            }
        } else {
            preselectColor = -16777216;
            foundPreselectColor = true;
        }
        this.mCircleSize = getResources().getDimensionPixelSize(R.dimen.md_colorchooser_circlesize);
        Builder builder = getBuilder();
        com.afollestad.materialdialogs.MaterialDialog.Builder positiveText = new com.afollestad.materialdialogs.MaterialDialog.Builder(getActivity()).title(getTitle()).autoDismiss(false).customView(R.layout.md_dialog_colorchooser, false).negativeText(builder.mCancelBtn).positiveText(builder.mDoneBtn);
        if (builder.mAllowUserCustom) {
            i = builder.mCustomBtn;
        } else {
            i = 0;
        }
        com.afollestad.materialdialogs.MaterialDialog.Builder bd = positiveText.neutralText(i).onPositive(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                ColorChooserDialog.this.mCallback.onColorSelection(ColorChooserDialog.this, ColorChooserDialog.this.getSelectedColor());
                ColorChooserDialog.this.dismiss();
            }
        }).onNegative(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (ColorChooserDialog.this.isInSub()) {
                    dialog.setActionButton(DialogAction.NEGATIVE, ColorChooserDialog.this.getBuilder().mCancelBtn);
                    ColorChooserDialog.this.isInSub(false);
                    ColorChooserDialog.this.invalidate();
                    return;
                }
                dialog.cancel();
            }
        }).onNeutral(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                ColorChooserDialog.this.toggleCustom(dialog);
            }
        }).showListener(new OnShowListener() {
            public void onShow(DialogInterface dialog) {
                ColorChooserDialog.this.invalidateDynamicButtonColors();
            }
        });
        if (builder.mTheme != null) {
            bd.theme(builder.mTheme);
        }
        MaterialDialog dialog = bd.build();
        View v = dialog.getCustomView();
        this.mGrid = (GridView) v.findViewById(R.id.grid);
        if (builder.mAllowUserCustom) {
            this.mSelectedCustomColor = preselectColor;
            this.mColorChooserCustomFrame = v.findViewById(R.id.colorChooserCustomFrame);
            this.mCustomColorHex = (EditText) v.findViewById(R.id.hexInput);
            this.mCustomColorIndicator = v.findViewById(R.id.colorIndicator);
            this.mCustomSeekA = (SeekBar) v.findViewById(R.id.colorA);
            this.mCustomSeekAValue = (TextView) v.findViewById(R.id.colorAValue);
            this.mCustomSeekR = (SeekBar) v.findViewById(R.id.colorR);
            this.mCustomSeekRValue = (TextView) v.findViewById(R.id.colorRValue);
            this.mCustomSeekG = (SeekBar) v.findViewById(R.id.colorG);
            this.mCustomSeekGValue = (TextView) v.findViewById(R.id.colorGValue);
            this.mCustomSeekB = (SeekBar) v.findViewById(R.id.colorB);
            this.mCustomSeekBValue = (TextView) v.findViewById(R.id.colorBValue);
            if (builder.mAllowUserCustomAlpha) {
                this.mCustomColorHex.setHint("FF2196F3");
                this.mCustomColorHex.setFilters(new InputFilter[]{new LengthFilter(8)});
            } else {
                v.findViewById(R.id.colorALabel).setVisibility(8);
                this.mCustomSeekA.setVisibility(8);
                this.mCustomSeekAValue.setVisibility(8);
                this.mCustomColorHex.setHint("2196F3");
                this.mCustomColorHex.setFilters(new InputFilter[]{new LengthFilter(6)});
            }
            if (!foundPreselectColor) {
                toggleCustom(dialog);
            }
        }
        invalidate();
        return dialog;
    }

    private void toggleCustom(MaterialDialog dialog) {
        if (dialog == null) {
            dialog = (MaterialDialog) getDialog();
        }
        if (this.mGrid.getVisibility() == 0) {
            dialog.setTitle(getBuilder().mCustomBtn);
            dialog.setActionButton(DialogAction.NEUTRAL, getBuilder().mPresetsBtn);
            dialog.setActionButton(DialogAction.NEGATIVE, getBuilder().mCancelBtn);
            this.mGrid.setVisibility(4);
            this.mColorChooserCustomFrame.setVisibility(0);
            this.mCustomColorTextWatcher = new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        ColorChooserDialog.this.mSelectedCustomColor = Color.parseColor("#" + s.toString());
                    } catch (IllegalArgumentException e) {
                        ColorChooserDialog.this.mSelectedCustomColor = -16777216;
                    }
                    ColorChooserDialog.this.mCustomColorIndicator.setBackgroundColor(ColorChooserDialog.this.mSelectedCustomColor);
                    if (ColorChooserDialog.this.mCustomSeekA.getVisibility() == 0) {
                        ColorChooserDialog.this.mCustomSeekA.setProgress(Color.alpha(ColorChooserDialog.this.mSelectedCustomColor));
                        ColorChooserDialog.this.mCustomSeekAValue.setText(String.format("%d", new Object[]{Integer.valueOf(alpha)}));
                    }
                    if (ColorChooserDialog.this.mCustomSeekA.getVisibility() == 0) {
                        ColorChooserDialog.this.mCustomSeekA.setProgress(Color.alpha(ColorChooserDialog.this.mSelectedCustomColor));
                    }
                    ColorChooserDialog.this.mCustomSeekR.setProgress(Color.red(ColorChooserDialog.this.mSelectedCustomColor));
                    ColorChooserDialog.this.mCustomSeekG.setProgress(Color.green(ColorChooserDialog.this.mSelectedCustomColor));
                    ColorChooserDialog.this.mCustomSeekB.setProgress(Color.blue(ColorChooserDialog.this.mSelectedCustomColor));
                    ColorChooserDialog.this.isInSub(false);
                    ColorChooserDialog.this.topIndex(-1);
                    ColorChooserDialog.this.subIndex(-1);
                    ColorChooserDialog.this.invalidateDynamicButtonColors();
                }

                public void afterTextChanged(Editable s) {
                }
            };
            this.mCustomColorHex.addTextChangedListener(this.mCustomColorTextWatcher);
            this.mCustomColorRgbListener = new OnSeekBarChangeListener() {
                @SuppressLint({"DefaultLocale"})
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        int color;
                        if (ColorChooserDialog.this.getBuilder().mAllowUserCustomAlpha) {
                            color = Color.argb(ColorChooserDialog.this.mCustomSeekA.getProgress(), ColorChooserDialog.this.mCustomSeekR.getProgress(), ColorChooserDialog.this.mCustomSeekG.getProgress(), ColorChooserDialog.this.mCustomSeekB.getProgress());
                            ColorChooserDialog.this.mCustomColorHex.setText(String.format("%08X", new Object[]{Integer.valueOf(color)}));
                        } else {
                            color = Color.rgb(ColorChooserDialog.this.mCustomSeekR.getProgress(), ColorChooserDialog.this.mCustomSeekG.getProgress(), ColorChooserDialog.this.mCustomSeekB.getProgress());
                            ColorChooserDialog.this.mCustomColorHex.setText(String.format("%06X", new Object[]{Integer.valueOf(16777215 & color)}));
                        }
                    }
                    ColorChooserDialog.this.mCustomSeekAValue.setText(String.format("%d", new Object[]{Integer.valueOf(ColorChooserDialog.this.mCustomSeekA.getProgress())}));
                    ColorChooserDialog.this.mCustomSeekRValue.setText(String.format("%d", new Object[]{Integer.valueOf(ColorChooserDialog.this.mCustomSeekR.getProgress())}));
                    ColorChooserDialog.this.mCustomSeekGValue.setText(String.format("%d", new Object[]{Integer.valueOf(ColorChooserDialog.this.mCustomSeekG.getProgress())}));
                    ColorChooserDialog.this.mCustomSeekBValue.setText(String.format("%d", new Object[]{Integer.valueOf(ColorChooserDialog.this.mCustomSeekB.getProgress())}));
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };
            this.mCustomSeekR.setOnSeekBarChangeListener(this.mCustomColorRgbListener);
            this.mCustomSeekG.setOnSeekBarChangeListener(this.mCustomColorRgbListener);
            this.mCustomSeekB.setOnSeekBarChangeListener(this.mCustomColorRgbListener);
            if (this.mCustomSeekA.getVisibility() == 0) {
                this.mCustomSeekA.setOnSeekBarChangeListener(this.mCustomColorRgbListener);
                this.mCustomColorHex.setText(String.format("%08X", new Object[]{Integer.valueOf(this.mSelectedCustomColor)}));
                return;
            }
            this.mCustomColorHex.setText(String.format("%06X", new Object[]{Integer.valueOf(16777215 & this.mSelectedCustomColor)}));
            return;
        }
        dialog.setTitle(getBuilder().mTitle);
        dialog.setActionButton(DialogAction.NEUTRAL, getBuilder().mCustomBtn);
        if (isInSub()) {
            dialog.setActionButton(DialogAction.NEGATIVE, getBuilder().mBackBtn);
        } else {
            dialog.setActionButton(DialogAction.NEGATIVE, getBuilder().mCancelBtn);
        }
        this.mGrid.setVisibility(0);
        this.mColorChooserCustomFrame.setVisibility(8);
        this.mCustomColorHex.removeTextChangedListener(this.mCustomColorTextWatcher);
        this.mCustomColorTextWatcher = null;
        this.mCustomSeekR.setOnSeekBarChangeListener(null);
        this.mCustomSeekG.setOnSeekBarChangeListener(null);
        this.mCustomSeekB.setOnSeekBarChangeListener(null);
        this.mCustomColorRgbListener = null;
    }

    private void invalidate() {
        if (this.mGrid.getAdapter() == null) {
            this.mGrid.setAdapter(new ColorGridAdapter());
            this.mGrid.setSelector(ResourcesCompat.getDrawable(getResources(), R.drawable.md_transparent, null));
        } else {
            ((BaseAdapter) this.mGrid.getAdapter()).notifyDataSetChanged();
        }
        if (getDialog() != null) {
            getDialog().setTitle(getTitle());
        }
    }

    private Builder getBuilder() {
        if (getArguments() == null || !getArguments().containsKey("builder")) {
            return null;
        }
        return (Builder) getArguments().getSerializable("builder");
    }

    private void dismissIfNecessary(AppCompatActivity context, String tag) {
        Fragment frag = context.getSupportFragmentManager().findFragmentByTag(tag);
        if (frag != null) {
            ((DialogFragment) frag).dismiss();
            context.getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }
    }

    @Nullable
    public static ColorChooserDialog findVisible(@NonNull AppCompatActivity context, String tag) {
        Fragment frag = context.getSupportFragmentManager().findFragmentByTag(tag);
        if (frag == null || !(frag instanceof ColorChooserDialog)) {
            return null;
        }
        return (ColorChooserDialog) frag;
    }

    @NonNull
    public ColorChooserDialog show(AppCompatActivity context) {
        String tag;
        Builder builder = getBuilder();
        if (builder.mColorsTop != null) {
            tag = TAG_PRIMARY;
        } else if (builder.mAccentMode) {
            tag = TAG_PRIMARY;
        } else {
            tag = TAG_PRIMARY;
        }
        dismissIfNecessary(context, tag);
        show(context.getSupportFragmentManager(), tag);
        return this;
    }
}
