package com.afollestad.materialdialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnShowListener;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager.BadTokenException;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import com.afollestad.materialdialogs.internal.MDButton;
import com.afollestad.materialdialogs.internal.MDRootLayout;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.afollestad.materialdialogs.util.DialogUtils;
import com.afollestad.materialdialogs.util.RippleHelper;
import com.afollestad.materialdialogs.util.TypefaceHelper;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import me.zhanghai.android.materialprogressbar.R;

public class MaterialDialog extends DialogBase implements OnClickListener, OnItemClickListener {
    protected TextView content;
    protected FrameLayout customViewFrame;
    protected ImageView icon;
    protected EditText input;
    protected TextView inputMinMax;
    protected ListType listType;
    protected ListView listView;
    protected final Builder mBuilder;
    private final Handler mHandler = new Handler();
    protected ProgressBar mProgress;
    protected TextView mProgressLabel;
    protected TextView mProgressMinMax;
    protected MDButton negativeButton;
    protected MDButton neutralButton;
    protected MDButton positiveButton;
    protected List<Integer> selectedIndicesList;
    protected TextView title;
    protected View titleFrame;

    public interface ListCallback {
        void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence);
    }

    @Deprecated
    public static abstract class ButtonCallback {
        @Deprecated
        public void onAny(MaterialDialog dialog) {
        }

        @Deprecated
        public void onPositive(MaterialDialog dialog) {
        }

        @Deprecated
        public void onNegative(MaterialDialog dialog) {
        }

        @Deprecated
        public void onNeutral(MaterialDialog dialog) {
        }

        protected final Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public final boolean equals(Object o) {
            return super.equals(o);
        }

        protected final void finalize() throws Throwable {
            super.finalize();
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public final String toString() {
            return super.toString();
        }
    }

    public interface ListCallbackMultiChoice {
        boolean onSelection(MaterialDialog materialDialog, Integer[] numArr, CharSequence[] charSequenceArr);
    }

    public interface ListCallbackSingleChoice {
        boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence);
    }

    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$afollestad$materialdialogs$DialogAction = new int[DialogAction.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$afollestad$materialdialogs$MaterialDialog$ListType = new int[ListType.values().length];

        static {
            try {
                $SwitchMap$com$afollestad$materialdialogs$MaterialDialog$ListType[ListType.REGULAR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$afollestad$materialdialogs$MaterialDialog$ListType[ListType.SINGLE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$afollestad$materialdialogs$MaterialDialog$ListType[ListType.MULTI.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$afollestad$materialdialogs$DialogAction[DialogAction.NEUTRAL.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$afollestad$materialdialogs$DialogAction[DialogAction.NEGATIVE.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$afollestad$materialdialogs$DialogAction[DialogAction.POSITIVE.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public static class Builder {
        protected ListAdapter adapter;
        protected boolean alwaysCallInputCallback;
        protected boolean alwaysCallMultiChoiceCallback = false;
        protected boolean alwaysCallSingleChoiceCallback = false;
        protected boolean autoDismiss = true;
        protected int backgroundColor;
        @DrawableRes
        protected int btnSelectorNegative;
        @DrawableRes
        protected int btnSelectorNeutral;
        @DrawableRes
        protected int btnSelectorPositive;
        @DrawableRes
        protected int btnSelectorStacked;
        protected GravityEnum btnStackedGravity = GravityEnum.END;
        protected int buttonRippleColor = 0;
        protected GravityEnum buttonsGravity = GravityEnum.START;
        protected ButtonCallback callback;
        protected OnCancelListener cancelListener;
        protected boolean cancelable = true;
        protected boolean canceledOnTouchOutside = true;
        protected CharSequence content;
        protected int contentColor = -1;
        protected boolean contentColorSet = false;
        protected GravityEnum contentGravity = GravityEnum.START;
        protected float contentLineSpacingMultiplier = 1.2f;
        protected final Context context;
        protected View customView;
        protected OnDismissListener dismissListener;
        protected int dividerColor;
        protected boolean dividerColorSet = false;
        protected boolean forceStacking;
        protected Drawable icon;
        protected boolean indeterminateIsHorizontalProgress;
        protected boolean indeterminateProgress;
        protected boolean inputAllowEmpty;
        protected InputCallback inputCallback;
        protected CharSequence inputHint;
        protected int inputMaxLength = -1;
        protected int inputMinLength = -1;
        protected CharSequence inputPrefill;
        protected int inputRangeErrorColor = 0;
        protected int inputType = -1;
        protected int itemColor;
        protected boolean itemColorSet = false;
        protected int[] itemIds;
        protected CharSequence[] items;
        protected GravityEnum itemsGravity = GravityEnum.START;
        protected OnKeyListener keyListener;
        protected boolean limitIconToDefaultSize;
        protected ColorStateList linkColor;
        protected ListCallback listCallback;
        protected ListCallback listCallbackCustom;
        protected ListCallbackMultiChoice listCallbackMultiChoice;
        protected ListCallbackSingleChoice listCallbackSingleChoice;
        @DrawableRes
        protected int listSelector;
        protected int maxIconSize = -1;
        protected Typeface mediumFont;
        protected ColorStateList negativeColor;
        protected boolean negativeColorSet = false;
        protected CharSequence negativeText;
        protected ColorStateList neutralColor;
        protected boolean neutralColorSet = false;
        protected CharSequence neutralText;
        protected SingleButtonCallback onAnyCallback;
        protected SingleButtonCallback onNegativeCallback;
        protected SingleButtonCallback onNeutralCallback;
        protected SingleButtonCallback onPositiveCallback;
        protected ColorStateList positiveColor;
        protected boolean positiveColorSet = false;
        protected CharSequence positiveText;
        protected int progress = -2;
        protected int progressMax = 0;
        protected String progressNumberFormat;
        protected NumberFormat progressPercentFormat;
        protected Typeface regularFont;
        protected int selectedIndex = -1;
        protected Integer[] selectedIndices = null;
        protected OnShowListener showListener;
        protected boolean showMinMax;
        protected Theme theme = Theme.LIGHT;
        protected CharSequence title;
        protected int titleColor = -1;
        protected boolean titleColorSet = false;
        protected GravityEnum titleGravity = GravityEnum.START;
        protected int widgetColor;
        protected boolean widgetColorSet = false;
        protected boolean wrapCustomViewInScroll;

        public final Context getContext() {
            return this.context;
        }

        public final int getItemColor() {
            return this.itemColor;
        }

        public final Typeface getRegularFont() {
            return this.regularFont;
        }

        public Builder(@NonNull Context context) {
            this.context = context;
            this.widgetColor = DialogUtils.resolveColor(context, R.attr.colorAccent, DialogUtils.getColor(context, R.color.md_material_blue_600));
            if (VERSION.SDK_INT >= 21) {
                this.widgetColor = DialogUtils.resolveColor(context, 16843829, this.widgetColor);
            }
            this.positiveColor = DialogUtils.getActionTextStateList(context, this.widgetColor);
            this.negativeColor = DialogUtils.getActionTextStateList(context, this.widgetColor);
            this.neutralColor = DialogUtils.getActionTextStateList(context, this.widgetColor);
            this.linkColor = DialogUtils.getActionTextStateList(context, DialogUtils.resolveColor(context, R.attr.md_link_color, this.widgetColor));
            int fallback = 0;
            if (VERSION.SDK_INT >= 21) {
                fallback = DialogUtils.resolveColor(context, 16843820);
            }
            this.buttonRippleColor = DialogUtils.resolveColor(context, R.attr.md_btn_ripple_color, DialogUtils.resolveColor(context, R.attr.colorControlHighlight, fallback));
            this.progressPercentFormat = NumberFormat.getPercentInstance();
            this.progressNumberFormat = "%1d/%2d";
            this.theme = DialogUtils.isColorDark(DialogUtils.resolveColor(context, 16842806)) ? Theme.LIGHT : Theme.DARK;
            checkSingleton();
            this.titleGravity = DialogUtils.resolveGravityEnum(context, R.attr.md_title_gravity, this.titleGravity);
            this.contentGravity = DialogUtils.resolveGravityEnum(context, R.attr.md_content_gravity, this.contentGravity);
            this.btnStackedGravity = DialogUtils.resolveGravityEnum(context, R.attr.md_btnstacked_gravity, this.btnStackedGravity);
            this.itemsGravity = DialogUtils.resolveGravityEnum(context, R.attr.md_items_gravity, this.itemsGravity);
            this.buttonsGravity = DialogUtils.resolveGravityEnum(context, R.attr.md_buttons_gravity, this.buttonsGravity);
            typeface(DialogUtils.resolveString(context, R.attr.md_medium_font), DialogUtils.resolveString(context, R.attr.md_regular_font));
            if (this.mediumFont == null) {
                try {
                    if (VERSION.SDK_INT >= 21) {
                        this.mediumFont = Typeface.create("sans-serif-medium", 0);
                    } else {
                        this.mediumFont = Typeface.create("sans-serif", 1);
                    }
                } catch (Exception e) {
                }
            }
            if (this.regularFont == null) {
                try {
                    this.regularFont = Typeface.create("sans-serif", 0);
                } catch (Exception e2) {
                }
            }
        }

        private void checkSingleton() {
            if (ThemeSingleton.get(false) != null) {
                ThemeSingleton s = ThemeSingleton.get();
                if (s.darkTheme) {
                    this.theme = Theme.DARK;
                }
                if (s.titleColor != 0) {
                    this.titleColor = s.titleColor;
                }
                if (s.contentColor != 0) {
                    this.contentColor = s.contentColor;
                }
                if (s.positiveColor != null) {
                    this.positiveColor = s.positiveColor;
                }
                if (s.neutralColor != null) {
                    this.neutralColor = s.neutralColor;
                }
                if (s.negativeColor != null) {
                    this.negativeColor = s.negativeColor;
                }
                if (s.itemColor != 0) {
                    this.itemColor = s.itemColor;
                }
                if (s.icon != null) {
                    this.icon = s.icon;
                }
                if (s.backgroundColor != 0) {
                    this.backgroundColor = s.backgroundColor;
                }
                if (s.dividerColor != 0) {
                    this.dividerColor = s.dividerColor;
                }
                if (s.btnSelectorStacked != 0) {
                    this.btnSelectorStacked = s.btnSelectorStacked;
                }
                if (s.listSelector != 0) {
                    this.listSelector = s.listSelector;
                }
                if (s.btnSelectorPositive != 0) {
                    this.btnSelectorPositive = s.btnSelectorPositive;
                }
                if (s.btnSelectorNeutral != 0) {
                    this.btnSelectorNeutral = s.btnSelectorNeutral;
                }
                if (s.btnSelectorNegative != 0) {
                    this.btnSelectorNegative = s.btnSelectorNegative;
                }
                if (s.widgetColor != 0) {
                    this.widgetColor = s.widgetColor;
                }
                if (s.linkColor != null) {
                    this.linkColor = s.linkColor;
                }
                this.titleGravity = s.titleGravity;
                this.contentGravity = s.contentGravity;
                this.btnStackedGravity = s.btnStackedGravity;
                this.itemsGravity = s.itemsGravity;
                this.buttonsGravity = s.buttonsGravity;
            }
        }

        public Builder title(@StringRes int titleRes) {
            title(this.context.getText(titleRes));
            return this;
        }

        public Builder title(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder titleGravity(@NonNull GravityEnum gravity) {
            this.titleGravity = gravity;
            return this;
        }

        public Builder buttonRippleColor(@ColorInt int color) {
            this.buttonRippleColor = color;
            return this;
        }

        public Builder buttonRippleColorRes(@ColorRes int colorRes) {
            return buttonRippleColor(DialogUtils.getColor(this.context, colorRes));
        }

        public Builder buttonRippleColorAttr(@AttrRes int colorAttr) {
            return buttonRippleColor(DialogUtils.resolveColor(this.context, colorAttr));
        }

        public Builder titleColor(@ColorInt int color) {
            this.titleColor = color;
            this.titleColorSet = true;
            return this;
        }

        public Builder titleColorRes(@ColorRes int colorRes) {
            return titleColor(DialogUtils.getColor(this.context, colorRes));
        }

        public Builder titleColorAttr(@AttrRes int colorAttr) {
            return titleColor(DialogUtils.resolveColor(this.context, colorAttr));
        }

        public Builder typeface(@Nullable Typeface medium, @Nullable Typeface regular) {
            this.mediumFont = medium;
            this.regularFont = regular;
            return this;
        }

        public Builder typeface(@Nullable String medium, @Nullable String regular) {
            if (medium != null) {
                this.mediumFont = TypefaceHelper.get(this.context, medium);
                if (this.mediumFont == null) {
                    throw new IllegalArgumentException("No font asset found for " + medium);
                }
            }
            if (regular != null) {
                this.regularFont = TypefaceHelper.get(this.context, regular);
                if (this.regularFont == null) {
                    throw new IllegalArgumentException("No font asset found for " + regular);
                }
            }
            return this;
        }

        public Builder icon(@NonNull Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder iconRes(@DrawableRes int icon) {
            this.icon = ResourcesCompat.getDrawable(this.context.getResources(), icon, null);
            return this;
        }

        public Builder iconAttr(@AttrRes int iconAttr) {
            this.icon = DialogUtils.resolveDrawable(this.context, iconAttr);
            return this;
        }

        public Builder content(@StringRes int contentRes) {
            content(this.context.getText(contentRes));
            return this;
        }

        public Builder content(@NonNull CharSequence content) {
            if (this.customView != null) {
                throw new IllegalStateException("You cannot set content() when you're using a custom view.");
            }
            this.content = content;
            return this;
        }

        public Builder content(@StringRes int contentRes, Object... formatArgs) {
            content(this.context.getString(contentRes, formatArgs));
            return this;
        }

        public Builder contentColor(@ColorInt int color) {
            this.contentColor = color;
            this.contentColorSet = true;
            return this;
        }

        public Builder contentColorRes(@ColorRes int colorRes) {
            contentColor(DialogUtils.getColor(this.context, colorRes));
            return this;
        }

        public Builder contentColorAttr(@AttrRes int colorAttr) {
            contentColor(DialogUtils.resolveColor(this.context, colorAttr));
            return this;
        }

        public Builder contentGravity(@NonNull GravityEnum gravity) {
            this.contentGravity = gravity;
            return this;
        }

        public Builder contentLineSpacing(float multiplier) {
            this.contentLineSpacingMultiplier = multiplier;
            return this;
        }

        public Builder items(@NonNull Collection collection) {
            if (collection.size() > 0) {
                CharSequence[] array = new String[collection.size()];
                int i = 0;
                for (Object obj : collection) {
                    array[i] = obj.toString();
                    i++;
                }
                items(array);
            }
            return this;
        }

        public Builder items(@ArrayRes int itemsRes) {
            items(this.context.getResources().getTextArray(itemsRes));
            return this;
        }

        public Builder items(@NonNull CharSequence... items) {
            if (this.customView != null) {
                throw new IllegalStateException("You cannot set items() when you're using a custom view.");
            }
            this.items = items;
            return this;
        }

        public Builder itemsCallback(@NonNull ListCallback callback) {
            this.listCallback = callback;
            this.listCallbackSingleChoice = null;
            this.listCallbackMultiChoice = null;
            return this;
        }

        public Builder itemsColor(@ColorInt int color) {
            this.itemColor = color;
            this.itemColorSet = true;
            return this;
        }

        @Deprecated
        public Builder itemColor(@ColorInt int color) {
            return itemsColor(color);
        }

        public Builder itemsColorRes(@ColorRes int colorRes) {
            return itemsColor(DialogUtils.getColor(this.context, colorRes));
        }

        @Deprecated
        public Builder itemColorRes(@ColorRes int colorRes) {
            return itemsColorRes(colorRes);
        }

        public Builder itemsColorAttr(@AttrRes int colorAttr) {
            return itemsColor(DialogUtils.resolveColor(this.context, colorAttr));
        }

        @Deprecated
        public Builder itemColorAttr(@AttrRes int colorAttr) {
            return itemsColorAttr(colorAttr);
        }

        public Builder itemsGravity(@NonNull GravityEnum gravity) {
            this.itemsGravity = gravity;
            return this;
        }

        public Builder itemsIds(@NonNull int[] idsArray) {
            this.itemIds = idsArray;
            return this;
        }

        public Builder itemsIds(@ArrayRes int idsArrayRes) {
            return itemsIds(this.context.getResources().getIntArray(idsArrayRes));
        }

        public Builder buttonsGravity(@NonNull GravityEnum gravity) {
            this.buttonsGravity = gravity;
            return this;
        }

        public Builder itemsCallbackSingleChoice(int selectedIndex, @NonNull ListCallbackSingleChoice callback) {
            this.selectedIndex = selectedIndex;
            this.listCallback = null;
            this.listCallbackSingleChoice = callback;
            this.listCallbackMultiChoice = null;
            return this;
        }

        public Builder alwaysCallSingleChoiceCallback() {
            this.alwaysCallSingleChoiceCallback = true;
            return this;
        }

        public Builder itemsCallbackMultiChoice(@Nullable Integer[] selectedIndices, @NonNull ListCallbackMultiChoice callback) {
            this.selectedIndices = selectedIndices;
            this.listCallback = null;
            this.listCallbackSingleChoice = null;
            this.listCallbackMultiChoice = callback;
            return this;
        }

        public Builder alwaysCallMultiChoiceCallback() {
            this.alwaysCallMultiChoiceCallback = true;
            return this;
        }

        public Builder positiveText(@StringRes int postiveRes) {
            if (postiveRes != 0) {
                positiveText(this.context.getText(postiveRes));
            }
            return this;
        }

        public Builder positiveText(@NonNull CharSequence message) {
            this.positiveText = message;
            return this;
        }

        public Builder positiveColor(@ColorInt int color) {
            return positiveColor(DialogUtils.getActionTextStateList(this.context, color));
        }

        public Builder positiveColorRes(@ColorRes int colorRes) {
            return positiveColor(DialogUtils.getActionTextColorStateList(this.context, colorRes));
        }

        public Builder positiveColorAttr(@AttrRes int colorAttr) {
            return positiveColor(DialogUtils.resolveActionTextColorStateList(this.context, colorAttr, null));
        }

        public Builder positiveColor(@NonNull ColorStateList colorStateList) {
            this.positiveColor = colorStateList;
            this.positiveColorSet = true;
            return this;
        }

        public Builder neutralText(@StringRes int neutralRes) {
            return neutralRes == 0 ? this : neutralText(this.context.getText(neutralRes));
        }

        public Builder neutralText(@NonNull CharSequence message) {
            this.neutralText = message;
            return this;
        }

        public Builder negativeColor(@ColorInt int color) {
            return negativeColor(DialogUtils.getActionTextStateList(this.context, color));
        }

        public Builder negativeColorRes(@ColorRes int colorRes) {
            return negativeColor(DialogUtils.getActionTextColorStateList(this.context, colorRes));
        }

        public Builder negativeColorAttr(@AttrRes int colorAttr) {
            return negativeColor(DialogUtils.resolveActionTextColorStateList(this.context, colorAttr, null));
        }

        public Builder negativeColor(@NonNull ColorStateList colorStateList) {
            this.negativeColor = colorStateList;
            this.negativeColorSet = true;
            return this;
        }

        public Builder negativeText(@StringRes int negativeRes) {
            return negativeRes == 0 ? this : negativeText(this.context.getText(negativeRes));
        }

        public Builder negativeText(@NonNull CharSequence message) {
            this.negativeText = message;
            return this;
        }

        public Builder neutralColor(@ColorInt int color) {
            return neutralColor(DialogUtils.getActionTextStateList(this.context, color));
        }

        public Builder neutralColorRes(@ColorRes int colorRes) {
            return neutralColor(DialogUtils.getActionTextColorStateList(this.context, colorRes));
        }

        public Builder neutralColorAttr(@AttrRes int colorAttr) {
            return neutralColor(DialogUtils.resolveActionTextColorStateList(this.context, colorAttr, null));
        }

        public Builder neutralColor(@NonNull ColorStateList colorStateList) {
            this.neutralColor = colorStateList;
            this.neutralColorSet = true;
            return this;
        }

        public Builder linkColor(@ColorInt int color) {
            return linkColor(DialogUtils.getActionTextStateList(this.context, color));
        }

        public Builder linkColorRes(@ColorRes int colorRes) {
            return linkColor(DialogUtils.getActionTextColorStateList(this.context, colorRes));
        }

        public Builder linkColorAttr(@AttrRes int colorAttr) {
            return linkColor(DialogUtils.resolveActionTextColorStateList(this.context, colorAttr, null));
        }

        public Builder linkColor(@NonNull ColorStateList colorStateList) {
            this.linkColor = colorStateList;
            return this;
        }

        public Builder listSelector(@DrawableRes int selectorRes) {
            this.listSelector = selectorRes;
            return this;
        }

        public Builder btnSelectorStacked(@DrawableRes int selectorRes) {
            this.btnSelectorStacked = selectorRes;
            return this;
        }

        public Builder btnSelector(@DrawableRes int selectorRes) {
            this.btnSelectorPositive = selectorRes;
            this.btnSelectorNeutral = selectorRes;
            this.btnSelectorNegative = selectorRes;
            return this;
        }

        public Builder btnSelector(@DrawableRes int selectorRes, @NonNull DialogAction which) {
            switch (AnonymousClass4.$SwitchMap$com$afollestad$materialdialogs$DialogAction[which.ordinal()]) {
                case R.styleable.View_android_focusable /*1*/:
                    this.btnSelectorNeutral = selectorRes;
                    break;
                case R.styleable.View_paddingStart /*2*/:
                    this.btnSelectorNegative = selectorRes;
                    break;
                default:
                    this.btnSelectorPositive = selectorRes;
                    break;
            }
            return this;
        }

        public Builder btnStackedGravity(@NonNull GravityEnum gravity) {
            this.btnStackedGravity = gravity;
            return this;
        }

        public Builder customView(@LayoutRes int layoutRes, boolean wrapInScrollView) {
            return customView(LayoutInflater.from(this.context).inflate(layoutRes, null), wrapInScrollView);
        }

        public Builder customView(@NonNull View view, boolean wrapInScrollView) {
            if (this.content != null) {
                throw new IllegalStateException("You cannot use customView() when you have content set.");
            } else if (this.items != null) {
                throw new IllegalStateException("You cannot use customView() when you have items set.");
            } else if (this.inputCallback != null) {
                throw new IllegalStateException("You cannot use customView() with an input dialog");
            } else if (this.progress > -2 || this.indeterminateProgress) {
                throw new IllegalStateException("You cannot use customView() with a progress dialog");
            } else {
                if (view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
                this.customView = view;
                this.wrapCustomViewInScroll = wrapInScrollView;
                return this;
            }
        }

        public Builder progress(boolean indeterminate, int max) {
            if (this.customView != null) {
                throw new IllegalStateException("You cannot set progress() when you're using a custom view.");
            }
            if (indeterminate) {
                this.indeterminateProgress = true;
                this.progress = -2;
            } else {
                this.indeterminateProgress = false;
                this.progress = -1;
                this.progressMax = max;
            }
            return this;
        }

        public Builder progress(boolean indeterminate, int max, boolean showMinMax) {
            this.showMinMax = showMinMax;
            return progress(indeterminate, max);
        }

        public Builder progressNumberFormat(@NonNull String format) {
            this.progressNumberFormat = format;
            return this;
        }

        public Builder progressPercentFormat(@NonNull NumberFormat format) {
            this.progressPercentFormat = format;
            return this;
        }

        public Builder progressIndeterminateStyle(boolean horizontal) {
            this.indeterminateIsHorizontalProgress = horizontal;
            return this;
        }

        public Builder widgetColor(@ColorInt int color) {
            this.widgetColor = color;
            this.widgetColorSet = true;
            return this;
        }

        public Builder widgetColorRes(@ColorRes int colorRes) {
            return widgetColor(DialogUtils.getColor(this.context, colorRes));
        }

        public Builder widgetColorAttr(@AttrRes int colorAttr) {
            return widgetColorRes(DialogUtils.resolveColor(this.context, colorAttr));
        }

        public Builder dividerColor(@ColorInt int color) {
            this.dividerColor = color;
            this.dividerColorSet = true;
            return this;
        }

        public Builder dividerColorRes(@ColorRes int colorRes) {
            return dividerColor(DialogUtils.getColor(this.context, colorRes));
        }

        public Builder dividerColorAttr(@AttrRes int colorAttr) {
            return dividerColor(DialogUtils.resolveColor(this.context, colorAttr));
        }

        public Builder backgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder backgroundColorRes(@ColorRes int colorRes) {
            return backgroundColor(DialogUtils.getColor(this.context, colorRes));
        }

        public Builder backgroundColorAttr(@AttrRes int colorAttr) {
            return backgroundColor(DialogUtils.resolveColor(this.context, colorAttr));
        }

        public Builder callback(@NonNull ButtonCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder onPositive(@NonNull SingleButtonCallback callback) {
            this.onPositiveCallback = callback;
            return this;
        }

        public Builder onNegative(@NonNull SingleButtonCallback callback) {
            this.onNegativeCallback = callback;
            return this;
        }

        public Builder onNeutral(@NonNull SingleButtonCallback callback) {
            this.onNeutralCallback = callback;
            return this;
        }

        public Builder onAny(@NonNull SingleButtonCallback callback) {
            this.onAnyCallback = callback;
            return this;
        }

        public Builder theme(@NonNull Theme theme) {
            this.theme = theme;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            this.canceledOnTouchOutside = cancelable;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder autoDismiss(boolean dismiss) {
            this.autoDismiss = dismiss;
            return this;
        }

        public Builder adapter(@NonNull ListAdapter adapter, @Nullable ListCallback callback) {
            if (this.customView != null) {
                throw new IllegalStateException("You cannot set adapter() when you're using a custom view.");
            }
            this.adapter = adapter;
            this.listCallbackCustom = callback;
            return this;
        }

        public Builder limitIconToDefaultSize() {
            this.limitIconToDefaultSize = true;
            return this;
        }

        public Builder maxIconSize(int maxIconSize) {
            this.maxIconSize = maxIconSize;
            return this;
        }

        public Builder maxIconSizeRes(@DimenRes int maxIconSizeRes) {
            return maxIconSize((int) this.context.getResources().getDimension(maxIconSizeRes));
        }

        public Builder showListener(@NonNull OnShowListener listener) {
            this.showListener = listener;
            return this;
        }

        public Builder dismissListener(@NonNull OnDismissListener listener) {
            this.dismissListener = listener;
            return this;
        }

        public Builder cancelListener(@NonNull OnCancelListener listener) {
            this.cancelListener = listener;
            return this;
        }

        public Builder keyListener(@NonNull OnKeyListener listener) {
            this.keyListener = listener;
            return this;
        }

        public Builder forceStacking(boolean stacked) {
            this.forceStacking = stacked;
            return this;
        }

        public Builder input(@Nullable CharSequence hint, @Nullable CharSequence prefill, boolean allowEmptyInput, @NonNull InputCallback callback) {
            if (this.customView != null) {
                throw new IllegalStateException("You cannot set content() when you're using a custom view.");
            }
            this.inputCallback = callback;
            this.inputHint = hint;
            this.inputPrefill = prefill;
            this.inputAllowEmpty = allowEmptyInput;
            return this;
        }

        public Builder input(@Nullable CharSequence hint, @Nullable CharSequence prefill, @NonNull InputCallback callback) {
            return input(hint, prefill, true, callback);
        }

        public Builder input(@StringRes int hint, @StringRes int prefill, boolean allowEmptyInput, @NonNull InputCallback callback) {
            CharSequence charSequence = null;
            CharSequence text = hint == 0 ? null : this.context.getText(hint);
            if (prefill != 0) {
                charSequence = this.context.getText(prefill);
            }
            return input(text, charSequence, allowEmptyInput, callback);
        }

        public Builder input(@StringRes int hint, @StringRes int prefill, @NonNull InputCallback callback) {
            return input(hint, prefill, true, callback);
        }

        public Builder inputType(int type) {
            this.inputType = type;
            return this;
        }

        @Deprecated
        public Builder inputMaxLength(@IntRange(from = 1, to = 2147483647L) int maxLength) {
            return inputRange(0, maxLength, 0);
        }

        @Deprecated
        public Builder inputMaxLength(@IntRange(from = 1, to = 2147483647L) int maxLength, @ColorInt int errorColor) {
            return inputRange(0, maxLength, errorColor);
        }

        @Deprecated
        public Builder inputMaxLengthRes(@IntRange(from = 1, to = 2147483647L) int maxLength, @ColorRes int errorColor) {
            return inputRangeRes(0, maxLength, errorColor);
        }

        public Builder inputRange(@IntRange(from = 0, to = 2147483647L) int minLength, @IntRange(from = -1, to = 2147483647L) int maxLength) {
            return inputRange(minLength, maxLength, 0);
        }

        public Builder inputRange(@IntRange(from = 0, to = 2147483647L) int minLength, @IntRange(from = -1, to = 2147483647L) int maxLength, @ColorInt int errorColor) {
            if (minLength < 0) {
                throw new IllegalArgumentException("Min length for input dialogs cannot be less than 0.");
            }
            this.inputMinLength = minLength;
            this.inputMaxLength = maxLength;
            if (errorColor == 0) {
                this.inputRangeErrorColor = DialogUtils.getColor(this.context, R.color.md_edittext_error);
            } else {
                this.inputRangeErrorColor = errorColor;
            }
            return this;
        }

        public Builder inputRangeRes(@IntRange(from = 0, to = 2147483647L) int minLength, @IntRange(from = 1, to = 2147483647L) int maxLength, @ColorRes int errorColor) {
            return inputRange(minLength, maxLength, DialogUtils.getColor(this.context, errorColor));
        }

        public Builder alwaysCallInputCallback() {
            this.alwaysCallInputCallback = true;
            return this;
        }

        @UiThread
        public MaterialDialog build() {
            return new MaterialDialog(this);
        }

        @UiThread
        public MaterialDialog show() {
            MaterialDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }

    public static class DialogException extends BadTokenException {
        public DialogException(String message) {
            super(message);
        }
    }

    public interface InputCallback {
        void onInput(@NonNull MaterialDialog materialDialog, CharSequence charSequence);
    }

    protected enum ListType {
        REGULAR,
        SINGLE,
        MULTI;

        public static int getLayoutForType(ListType type) {
            switch (AnonymousClass4.$SwitchMap$com$afollestad$materialdialogs$MaterialDialog$ListType[type.ordinal()]) {
                case R.styleable.View_android_focusable /*1*/:
                    return R.layout.md_listitem;
                case R.styleable.View_paddingStart /*2*/:
                    return R.layout.md_listitem_singlechoice;
                case R.styleable.View_paddingEnd /*3*/:
                    return R.layout.md_listitem_multichoice;
                default:
                    throw new IllegalArgumentException("Not a valid list type");
            }
        }
    }

    public static class NotImplementedException extends Error {
        public NotImplementedException(String message) {
            super(message);
        }
    }

    public interface SingleButtonCallback {
        void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction);
    }

    public /* bridge */ /* synthetic */ View findViewById(int i) {
        return super.findViewById(i);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ void setContentView(View view, LayoutParams layoutParams) throws IllegalAccessError {
        super.setContentView(view, layoutParams);
    }

    public final Builder getBuilder() {
        return this.mBuilder;
    }

    @SuppressLint({"InflateParams"})
    protected MaterialDialog(Builder builder) {
        super(builder.context, DialogInit.getTheme(builder));
        this.mBuilder = builder;
        this.view = (MDRootLayout) LayoutInflater.from(builder.context).inflate(DialogInit.getInflateLayout(builder), null);
        DialogInit.init(this);
    }

    public final void setTypeface(TextView target, Typeface t) {
        if (t != null) {
            target.setPaintFlags(target.getPaintFlags() | 128);
            target.setTypeface(t);
        }
    }

    protected final void checkIfListInitScroll() {
        if (this.listView != null) {
            this.listView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (VERSION.SDK_INT < 16) {
                        MaterialDialog.this.listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        MaterialDialog.this.listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    if (MaterialDialog.this.listType == ListType.SINGLE || MaterialDialog.this.listType == ListType.MULTI) {
                        int selectedIndex;
                        if (MaterialDialog.this.listType == ListType.SINGLE) {
                            if (MaterialDialog.this.mBuilder.selectedIndex >= 0) {
                                selectedIndex = MaterialDialog.this.mBuilder.selectedIndex;
                            } else {
                                return;
                            }
                        } else if (MaterialDialog.this.selectedIndicesList != null && MaterialDialog.this.selectedIndicesList.size() != 0) {
                            Collections.sort(MaterialDialog.this.selectedIndicesList);
                            selectedIndex = ((Integer) MaterialDialog.this.selectedIndicesList.get(0)).intValue();
                        } else {
                            return;
                        }
                        if (MaterialDialog.this.listView.getLastVisiblePosition() < selectedIndex) {
                            int scrollIndex = selectedIndex - ((MaterialDialog.this.listView.getLastVisiblePosition() - MaterialDialog.this.listView.getFirstVisiblePosition()) / 2);
                            if (scrollIndex < 0) {
                                scrollIndex = 0;
                            }
                            final int fScrollIndex = scrollIndex;
                            MaterialDialog.this.listView.post(new Runnable() {
                                public void run() {
                                    MaterialDialog.this.listView.requestFocus();
                                    MaterialDialog.this.listView.setSelection(fScrollIndex);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    protected final void invalidateList() {
        if (this.listView != null) {
            if ((this.mBuilder.items != null && this.mBuilder.items.length != 0) || this.mBuilder.adapter != null) {
                this.listView.setAdapter(this.mBuilder.adapter);
                if (this.listType != null || this.mBuilder.listCallbackCustom != null) {
                    this.listView.setOnItemClickListener(this);
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (this.mBuilder.listCallbackCustom != null) {
            CharSequence text = null;
            if (view instanceof TextView) {
                text = ((TextView) view).getText();
            }
            this.mBuilder.listCallbackCustom.onSelection(this, view, position, text);
        } else if (this.listType == null || this.listType == ListType.REGULAR) {
            if (this.mBuilder.autoDismiss) {
                dismiss();
            }
            if (this.mBuilder.listCallback != null) {
                this.mBuilder.listCallback.onSelection(this, view, position, this.mBuilder.items[position]);
            }
        } else if (this.listType == ListType.MULTI) {
            CheckBox cb = (CheckBox) view.findViewById(R.id.control);
            if (!this.selectedIndicesList.contains(Integer.valueOf(position))) {
                this.selectedIndicesList.add(Integer.valueOf(position));
                if (!this.mBuilder.alwaysCallMultiChoiceCallback) {
                    cb.setChecked(true);
                    return;
                } else if (sendMultichoiceCallback()) {
                    cb.setChecked(true);
                    return;
                } else {
                    this.selectedIndicesList.remove(Integer.valueOf(position));
                    return;
                }
            }
            this.selectedIndicesList.remove(Integer.valueOf(position));
            cb.setChecked(false);
            if (this.mBuilder.alwaysCallMultiChoiceCallback) {
                sendMultichoiceCallback();
            }
        } else if (this.listType == ListType.SINGLE) {
            boolean allowSelection = true;
            DefaultAdapter adapter = this.mBuilder.adapter;
            RadioButton radio = (RadioButton) view.findViewById(R.id.control);
            if (this.mBuilder.autoDismiss && this.mBuilder.positiveText == null) {
                dismiss();
                allowSelection = false;
                this.mBuilder.selectedIndex = position;
                sendSingleChoiceCallback(view);
            } else if (this.mBuilder.alwaysCallSingleChoiceCallback) {
                int oldSelected = this.mBuilder.selectedIndex;
                this.mBuilder.selectedIndex = position;
                allowSelection = sendSingleChoiceCallback(view);
                this.mBuilder.selectedIndex = oldSelected;
            }
            if (allowSelection) {
                this.mBuilder.selectedIndex = position;
                radio.setChecked(true);
                adapter.notifyDataSetChanged();
            }
        }
    }

    protected final Drawable getListSelector() {
        if (this.mBuilder.listSelector != 0) {
            return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.listSelector, null);
        }
        Drawable d = DialogUtils.resolveDrawable(this.mBuilder.context, R.attr.md_list_selector);
        return d == null ? DialogUtils.resolveDrawable(getContext(), R.attr.md_list_selector) : d;
    }

    Drawable getButtonSelector(DialogAction which, boolean isStacked) {
        Drawable d;
        if (!isStacked) {
            switch (AnonymousClass4.$SwitchMap$com$afollestad$materialdialogs$DialogAction[which.ordinal()]) {
                case R.styleable.View_android_focusable /*1*/:
                    if (this.mBuilder.btnSelectorNeutral != 0) {
                        return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.btnSelectorNeutral, null);
                    }
                    d = DialogUtils.resolveDrawable(this.mBuilder.context, R.attr.md_btn_neutral_selector);
                    if (d != null) {
                        return d;
                    }
                    d = DialogUtils.resolveDrawable(getContext(), R.attr.md_btn_neutral_selector);
                    if (VERSION.SDK_INT < 21) {
                        return d;
                    }
                    RippleHelper.applyColor(d, this.mBuilder.buttonRippleColor);
                    return d;
                case R.styleable.View_paddingStart /*2*/:
                    if (this.mBuilder.btnSelectorNegative != 0) {
                        return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.btnSelectorNegative, null);
                    }
                    d = DialogUtils.resolveDrawable(this.mBuilder.context, R.attr.md_btn_negative_selector);
                    if (d != null) {
                        return d;
                    }
                    d = DialogUtils.resolveDrawable(getContext(), R.attr.md_btn_negative_selector);
                    if (VERSION.SDK_INT < 21) {
                        return d;
                    }
                    RippleHelper.applyColor(d, this.mBuilder.buttonRippleColor);
                    return d;
                default:
                    if (this.mBuilder.btnSelectorPositive != 0) {
                        return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.btnSelectorPositive, null);
                    }
                    d = DialogUtils.resolveDrawable(this.mBuilder.context, R.attr.md_btn_positive_selector);
                    if (d != null) {
                        return d;
                    }
                    d = DialogUtils.resolveDrawable(getContext(), R.attr.md_btn_positive_selector);
                    if (VERSION.SDK_INT < 21) {
                        return d;
                    }
                    RippleHelper.applyColor(d, this.mBuilder.buttonRippleColor);
                    return d;
            }
        } else if (this.mBuilder.btnSelectorStacked != 0) {
            return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.btnSelectorStacked, null);
        } else {
            d = DialogUtils.resolveDrawable(this.mBuilder.context, R.attr.md_btn_stacked_selector);
            if (d == null) {
                return DialogUtils.resolveDrawable(getContext(), R.attr.md_btn_stacked_selector);
            }
            return d;
        }
    }

    private boolean sendSingleChoiceCallback(View v) {
        if (this.mBuilder.listCallbackSingleChoice == null) {
            return false;
        }
        CharSequence text = null;
        if (this.mBuilder.selectedIndex >= 0 && this.mBuilder.selectedIndex < this.mBuilder.items.length) {
            text = this.mBuilder.items[this.mBuilder.selectedIndex];
        }
        return this.mBuilder.listCallbackSingleChoice.onSelection(this, v, this.mBuilder.selectedIndex, text);
    }

    private boolean sendMultichoiceCallback() {
        if (this.mBuilder.listCallbackMultiChoice == null) {
            return false;
        }
        Collections.sort(this.selectedIndicesList);
        List<CharSequence> selectedTitles = new ArrayList();
        for (Integer i : this.selectedIndicesList) {
            if (i.intValue() >= 0 && i.intValue() <= this.mBuilder.items.length - 1) {
                selectedTitles.add(this.mBuilder.items[i.intValue()]);
            }
        }
        return this.mBuilder.listCallbackMultiChoice.onSelection(this, (Integer[]) this.selectedIndicesList.toArray(new Integer[this.selectedIndicesList.size()]), (CharSequence[]) selectedTitles.toArray(new CharSequence[selectedTitles.size()]));
    }

    public final void onClick(View v) {
        DialogAction tag = (DialogAction) v.getTag();
        switch (AnonymousClass4.$SwitchMap$com$afollestad$materialdialogs$DialogAction[tag.ordinal()]) {
            case R.styleable.View_android_focusable /*1*/:
                if (this.mBuilder.callback != null) {
                    this.mBuilder.callback.onAny(this);
                    this.mBuilder.callback.onNeutral(this);
                }
                if (this.mBuilder.onNeutralCallback != null) {
                    this.mBuilder.onNeutralCallback.onClick(this, tag);
                }
                if (this.mBuilder.autoDismiss) {
                    dismiss();
                    break;
                }
                break;
            case R.styleable.View_paddingStart /*2*/:
                if (this.mBuilder.callback != null) {
                    this.mBuilder.callback.onAny(this);
                    this.mBuilder.callback.onNegative(this);
                }
                if (this.mBuilder.onNegativeCallback != null) {
                    this.mBuilder.onNegativeCallback.onClick(this, tag);
                }
                if (this.mBuilder.autoDismiss) {
                    dismiss();
                    break;
                }
                break;
            case R.styleable.View_paddingEnd /*3*/:
                if (this.mBuilder.callback != null) {
                    this.mBuilder.callback.onAny(this);
                    this.mBuilder.callback.onPositive(this);
                }
                if (this.mBuilder.onPositiveCallback != null) {
                    this.mBuilder.onPositiveCallback.onClick(this, tag);
                }
                if (!this.mBuilder.alwaysCallSingleChoiceCallback) {
                    sendSingleChoiceCallback(v);
                }
                if (!this.mBuilder.alwaysCallMultiChoiceCallback) {
                    sendMultichoiceCallback();
                }
                if (!(this.mBuilder.inputCallback == null || this.input == null || this.mBuilder.alwaysCallInputCallback)) {
                    this.mBuilder.inputCallback.onInput(this, this.input.getText());
                }
                if (this.mBuilder.autoDismiss) {
                    dismiss();
                    break;
                }
                break;
        }
        if (this.mBuilder.onAnyCallback != null) {
            this.mBuilder.onAnyCallback.onClick(this, tag);
        }
    }

    @UiThread
    public void show() {
        try {
            super.show();
        } catch (BadTokenException e) {
            throw new DialogException("Bad window token, you cannot show a dialog before an Activity is created or after it's hidden.");
        }
    }

    public final MDButton getActionButton(@NonNull DialogAction which) {
        switch (AnonymousClass4.$SwitchMap$com$afollestad$materialdialogs$DialogAction[which.ordinal()]) {
            case R.styleable.View_android_focusable /*1*/:
                return this.neutralButton;
            case R.styleable.View_paddingStart /*2*/:
                return this.negativeButton;
            default:
                return this.positiveButton;
        }
    }

    public final View getView() {
        return this.view;
    }

    @Nullable
    public final ListView getListView() {
        return this.listView;
    }

    @Nullable
    public final EditText getInputEditText() {
        return this.input;
    }

    public final TextView getTitleView() {
        return this.title;
    }

    public ImageView getIconView() {
        return this.icon;
    }

    @Nullable
    public final TextView getContentView() {
        return this.content;
    }

    @Nullable
    public final View getCustomView() {
        return this.mBuilder.customView;
    }

    @UiThread
    public final void setActionButton(@NonNull DialogAction which, CharSequence title) {
        int i = 8;
        MDButton mDButton;
        switch (AnonymousClass4.$SwitchMap$com$afollestad$materialdialogs$DialogAction[which.ordinal()]) {
            case R.styleable.View_android_focusable /*1*/:
                this.mBuilder.neutralText = title;
                this.neutralButton.setText(title);
                mDButton = this.neutralButton;
                if (title != null) {
                    i = 0;
                }
                mDButton.setVisibility(i);
                return;
            case R.styleable.View_paddingStart /*2*/:
                this.mBuilder.negativeText = title;
                this.negativeButton.setText(title);
                mDButton = this.negativeButton;
                if (title != null) {
                    i = 0;
                }
                mDButton.setVisibility(i);
                return;
            default:
                this.mBuilder.positiveText = title;
                this.positiveButton.setText(title);
                mDButton = this.positiveButton;
                if (title != null) {
                    i = 0;
                }
                mDButton.setVisibility(i);
                return;
        }
    }

    public final void setActionButton(DialogAction which, @StringRes int titleRes) {
        setActionButton(which, getContext().getText(titleRes));
    }

    public final boolean hasActionButtons() {
        return numberOfActionButtons() > 0;
    }

    public final int numberOfActionButtons() {
        int number = 0;
        if (this.mBuilder.positiveText != null && this.positiveButton.getVisibility() == 0) {
            number = 0 + 1;
        }
        if (this.mBuilder.neutralText != null && this.neutralButton.getVisibility() == 0) {
            number++;
        }
        if (this.mBuilder.negativeText == null || this.negativeButton.getVisibility() != 0) {
            return number;
        }
        return number + 1;
    }

    @UiThread
    public final void setTitle(@NonNull CharSequence newTitle) {
        this.title.setText(newTitle);
    }

    @UiThread
    public final void setTitle(@StringRes int newTitleRes) {
        setTitle(this.mBuilder.context.getString(newTitleRes));
    }

    @UiThread
    public final void setTitle(@StringRes int newTitleRes, @Nullable Object... formatArgs) {
        setTitle(this.mBuilder.context.getString(newTitleRes, formatArgs));
    }

    @UiThread
    public void setIcon(@DrawableRes int resId) {
        this.icon.setImageResource(resId);
        this.icon.setVisibility(resId != 0 ? 0 : 8);
    }

    @UiThread
    public void setIcon(Drawable d) {
        this.icon.setImageDrawable(d);
        this.icon.setVisibility(d != null ? 0 : 8);
    }

    @UiThread
    public void setIconAttribute(@AttrRes int attrId) {
        setIcon(DialogUtils.resolveDrawable(this.mBuilder.context, attrId));
    }

    @UiThread
    public final void setContent(CharSequence newContent) {
        this.content.setText(newContent);
        this.content.setVisibility(TextUtils.isEmpty(newContent) ? 8 : 0);
    }

    @UiThread
    public final void setContent(@StringRes int newContentRes) {
        setContent(this.mBuilder.context.getString(newContentRes));
    }

    @UiThread
    public final void setContent(@StringRes int newContentRes, @Nullable Object... formatArgs) {
        setContent(this.mBuilder.context.getString(newContentRes, formatArgs));
    }

    @Deprecated
    public void setMessage(CharSequence message) {
        setContent(message);
    }

    @UiThread
    public final void setItems(CharSequence... items) {
        if (this.mBuilder.adapter == null) {
            throw new IllegalStateException("This MaterialDialog instance does not yet have an adapter set to it. You cannot use setItems().");
        }
        this.mBuilder.items = items;
        if (this.mBuilder.adapter instanceof DefaultAdapter) {
            this.mBuilder.adapter = new DefaultAdapter(this, ListType.getLayoutForType(this.listType));
            this.listView.setAdapter(this.mBuilder.adapter);
            return;
        }
        throw new IllegalStateException("When using a custom adapter, setItems() cannot be used. Set items through the adapter instead.");
    }

    public final int getCurrentProgress() {
        if (this.mProgress == null) {
            return -1;
        }
        return this.mProgress.getProgress();
    }

    public ProgressBar getProgressBar() {
        return this.mProgress;
    }

    public final void incrementProgress(int by) {
        setProgress(getCurrentProgress() + by);
    }

    public final void setProgress(int progress) {
        if (this.mBuilder.progress <= -2) {
            throw new IllegalStateException("Cannot use setProgress() on this dialog.");
        }
        this.mProgress.setProgress(progress);
        this.mHandler.post(new Runnable() {
            public void run() {
                if (MaterialDialog.this.mProgressLabel != null) {
                    MaterialDialog.this.mProgressLabel.setText(MaterialDialog.this.mBuilder.progressPercentFormat.format((double) (((float) MaterialDialog.this.getCurrentProgress()) / ((float) MaterialDialog.this.getMaxProgress()))));
                }
                if (MaterialDialog.this.mProgressMinMax != null) {
                    MaterialDialog.this.mProgressMinMax.setText(String.format(MaterialDialog.this.mBuilder.progressNumberFormat, new Object[]{Integer.valueOf(MaterialDialog.this.getCurrentProgress()), Integer.valueOf(MaterialDialog.this.getMaxProgress())}));
                }
            }
        });
    }

    public final void setMaxProgress(int max) {
        if (this.mBuilder.progress <= -2) {
            throw new IllegalStateException("Cannot use setMaxProgress() on this dialog.");
        }
        this.mProgress.setMax(max);
    }

    public final boolean isIndeterminateProgress() {
        return this.mBuilder.indeterminateProgress;
    }

    public final int getMaxProgress() {
        if (this.mProgress == null) {
            return -1;
        }
        return this.mProgress.getMax();
    }

    public final void setProgressPercentFormat(NumberFormat format) {
        this.mBuilder.progressPercentFormat = format;
        setProgress(getCurrentProgress());
    }

    public final void setProgressNumberFormat(String format) {
        this.mBuilder.progressNumberFormat = format;
        setProgress(getCurrentProgress());
    }

    public final boolean isCancelled() {
        return !isShowing();
    }

    public int getSelectedIndex() {
        if (this.mBuilder.listCallbackSingleChoice != null) {
            return this.mBuilder.selectedIndex;
        }
        return -1;
    }

    @Nullable
    public Integer[] getSelectedIndices() {
        if (this.mBuilder.listCallbackMultiChoice != null) {
            return (Integer[]) this.selectedIndicesList.toArray(new Integer[this.selectedIndicesList.size()]);
        }
        return null;
    }

    @UiThread
    public void setSelectedIndex(int index) {
        this.mBuilder.selectedIndex = index;
        if (this.mBuilder.adapter == null || !(this.mBuilder.adapter instanceof DefaultAdapter)) {
            throw new IllegalStateException("You can only use setSelectedIndex() with the default adapter implementation.");
        }
        ((DefaultAdapter) this.mBuilder.adapter).notifyDataSetChanged();
    }

    @UiThread
    public void setSelectedIndices(@NonNull Integer[] indices) {
        this.selectedIndicesList = new ArrayList(Arrays.asList(indices));
        if (this.mBuilder.adapter == null || !(this.mBuilder.adapter instanceof DefaultAdapter)) {
            throw new IllegalStateException("You can only use setSelectedIndices() with the default adapter implementation.");
        }
        ((DefaultAdapter) this.mBuilder.adapter).notifyDataSetChanged();
    }

    public void clearSelectedIndices() {
        clearSelectedIndices(true);
    }

    public void clearSelectedIndices(boolean sendCallback) {
        if (this.listType == null || this.listType != ListType.MULTI) {
            throw new IllegalStateException("You can only use clearSelectedIndices() with multi choice list dialogs.");
        } else if (this.mBuilder.adapter == null || !(this.mBuilder.adapter instanceof DefaultAdapter)) {
            throw new IllegalStateException("You can only use clearSelectedIndices() with the default adapter implementation.");
        } else {
            if (this.selectedIndicesList != null) {
                this.selectedIndicesList.clear();
            }
            ((DefaultAdapter) this.mBuilder.adapter).notifyDataSetChanged();
            if (sendCallback && this.mBuilder.listCallbackMultiChoice != null) {
                sendMultichoiceCallback();
            }
        }
    }

    public void selectAllIndicies() {
        selectAllIndicies(true);
    }

    public void selectAllIndicies(boolean sendCallback) {
        if (this.listType == null || this.listType != ListType.MULTI) {
            throw new IllegalStateException("You can only use selectAllIndicies() with multi choice list dialogs.");
        } else if (this.mBuilder.adapter == null || !(this.mBuilder.adapter instanceof DefaultAdapter)) {
            throw new IllegalStateException("You can only use selectAllIndicies() with the default adapter implementation.");
        } else {
            if (this.selectedIndicesList == null) {
                this.selectedIndicesList = new ArrayList();
            }
            for (int i = 0; i < this.mBuilder.adapter.getCount(); i++) {
                if (!this.selectedIndicesList.contains(Integer.valueOf(i))) {
                    this.selectedIndicesList.add(Integer.valueOf(i));
                }
            }
            ((DefaultAdapter) this.mBuilder.adapter).notifyDataSetChanged();
            if (sendCallback && this.mBuilder.listCallbackMultiChoice != null) {
                sendMultichoiceCallback();
            }
        }
    }

    public final void onShow(DialogInterface dialog) {
        if (this.input != null) {
            DialogUtils.showKeyboard(this, this.mBuilder);
            if (this.input.getText().length() > 0) {
                this.input.setSelection(this.input.getText().length());
            }
        }
        super.onShow(dialog);
    }

    protected void setInternalInputCallback() {
        if (this.input != null) {
            this.input.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean z = true;
                    int length = s.toString().length();
                    boolean emptyDisabled = false;
                    if (!MaterialDialog.this.mBuilder.inputAllowEmpty) {
                        emptyDisabled = length == 0;
                        View positiveAb = MaterialDialog.this.getActionButton(DialogAction.POSITIVE);
                        if (emptyDisabled) {
                            z = false;
                        }
                        positiveAb.setEnabled(z);
                    }
                    MaterialDialog.this.invalidateInputMinMaxIndicator(length, emptyDisabled);
                    if (MaterialDialog.this.mBuilder.alwaysCallInputCallback) {
                        MaterialDialog.this.mBuilder.inputCallback.onInput(MaterialDialog.this, s);
                    }
                }

                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    protected void invalidateInputMinMaxIndicator(int currentLength, boolean emptyDisabled) {
        boolean z = true;
        if (this.inputMinMax != null) {
            boolean isDisabled;
            if (this.mBuilder.inputMaxLength > 0) {
                this.inputMinMax.setText(String.format(Locale.getDefault(), "%d/%d", new Object[]{Integer.valueOf(currentLength), Integer.valueOf(this.mBuilder.inputMaxLength)}));
                this.inputMinMax.setVisibility(0);
            } else {
                this.inputMinMax.setVisibility(8);
            }
            if (!(emptyDisabled && currentLength == 0) && ((this.mBuilder.inputMaxLength <= 0 || currentLength <= this.mBuilder.inputMaxLength) && currentLength >= this.mBuilder.inputMinLength)) {
                isDisabled = false;
            } else {
                isDisabled = true;
            }
            int colorText = isDisabled ? this.mBuilder.inputRangeErrorColor : this.mBuilder.contentColor;
            int colorWidget = isDisabled ? this.mBuilder.inputRangeErrorColor : this.mBuilder.widgetColor;
            if (this.mBuilder.inputMaxLength > 0) {
                this.inputMinMax.setTextColor(colorText);
            }
            MDTintHelper.setTint(this.input, colorWidget);
            View positiveAb = getActionButton(DialogAction.POSITIVE);
            if (isDisabled) {
                z = false;
            }
            positiveAb.setEnabled(z);
        }
    }

    public void dismiss() {
        if (this.input != null) {
            DialogUtils.hideKeyboard(this, this.mBuilder);
        }
        super.dismiss();
    }
}
