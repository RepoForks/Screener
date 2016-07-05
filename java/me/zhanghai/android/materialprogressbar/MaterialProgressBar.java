package me.zhanghai.android.materialprogressbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
import me.zhanghai.android.materialprogressbar.internal.DrawableCompat;

public class MaterialProgressBar extends ProgressBar {
    public static final int PROGRESS_STYLE_CIRCULAR = 0;
    public static final int PROGRESS_STYLE_HORIZONTAL = 1;
    private static final String TAG = MaterialProgressBar.class.getSimpleName();
    private int mProgressStyle;
    private TintInfo mProgressTint = new TintInfo();

    private static class TintInfo {
        boolean mHasTintList;
        boolean mHasTintMode;
        ColorStateList mTintList;
        Mode mTintMode;

        private TintInfo() {
        }
    }

    public MaterialProgressBar(Context context) {
        super(context);
        init(context, null, PROGRESS_STYLE_CIRCULAR, PROGRESS_STYLE_CIRCULAR);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, PROGRESS_STYLE_CIRCULAR, PROGRESS_STYLE_CIRCULAR);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, PROGRESS_STYLE_CIRCULAR);
    }

    @TargetApi(21)
    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        boolean z = false;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialProgressBar, defStyleAttr, defStyleRes);
        this.mProgressStyle = a.getInt(R.styleable.MaterialProgressBar_mpb_progressStyle, PROGRESS_STYLE_CIRCULAR);
        boolean setBothDrawables = a.getBoolean(R.styleable.MaterialProgressBar_mpb_setBothDrawables, false);
        boolean useIntrinsicPadding = a.getBoolean(R.styleable.MaterialProgressBar_mpb_useIntrinsicPadding, true);
        int i = R.styleable.MaterialProgressBar_mpb_showTrack;
        if (this.mProgressStyle == PROGRESS_STYLE_HORIZONTAL) {
            z = true;
        }
        boolean showTrack = a.getBoolean(i, z);
        if (a.hasValue(R.styleable.MaterialProgressBar_android_tint)) {
            this.mProgressTint.mTintList = a.getColorStateList(R.styleable.MaterialProgressBar_android_tint);
            this.mProgressTint.mHasTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_tintMode)) {
            this.mProgressTint.mTintMode = DrawableCompat.parseTintMode(a.getInt(R.styleable.MaterialProgressBar_mpb_tintMode, -1), null);
            this.mProgressTint.mHasTintMode = true;
        }
        a.recycle();
        switch (this.mProgressStyle) {
            case PROGRESS_STYLE_CIRCULAR /*0*/:
                if (isIndeterminate() && !setBothDrawables) {
                    setIndeterminateDrawable(new IndeterminateProgressDrawable(context));
                    break;
                }
                throw new UnsupportedOperationException("Determinate circular drawable is not yet supported");
                break;
            case PROGRESS_STYLE_HORIZONTAL /*1*/:
                if (isIndeterminate() || setBothDrawables) {
                    setIndeterminateDrawable(new IndeterminateHorizontalProgressDrawable(context));
                }
                if (!isIndeterminate() || setBothDrawables) {
                    setProgressDrawable(new HorizontalProgressDrawable(context));
                    break;
                }
            default:
                throw new IllegalArgumentException("Unknown progress style: " + this.mProgressStyle);
        }
        setUseIntrinsicPadding(useIntrinsicPadding);
        setShowTrack(showTrack);
    }

    public int getProgressStyle() {
        return this.mProgressStyle;
    }

    public Drawable getDrawable() {
        return isIndeterminate() ? getIndeterminateDrawable() : getProgressDrawable();
    }

    public boolean getUseIntrinsicPadding() {
        Drawable drawable = getDrawable();
        if (drawable instanceof IntrinsicPaddingDrawable) {
            return ((IntrinsicPaddingDrawable) drawable).getUseIntrinsicPadding();
        }
        throw new IllegalStateException("Drawable does not implement IntrinsicPaddingDrawable");
    }

    public void setUseIntrinsicPadding(boolean useIntrinsicPadding) {
        Drawable drawable = getDrawable();
        if (drawable instanceof IntrinsicPaddingDrawable) {
            ((IntrinsicPaddingDrawable) drawable).setUseIntrinsicPadding(useIntrinsicPadding);
            return;
        }
        throw new IllegalStateException("Drawable does not implement IntrinsicPaddingDrawable");
    }

    public boolean getShowTrack() {
        Drawable drawable = getDrawable();
        if (drawable instanceof ShowTrackDrawable) {
            return ((ShowTrackDrawable) drawable).getShowTrack();
        }
        return false;
    }

    public void setShowTrack(boolean showTrack) {
        Drawable drawable = getDrawable();
        if (drawable instanceof ShowTrackDrawable) {
            ((ShowTrackDrawable) drawable).setShowTrack(showTrack);
        } else if (showTrack) {
            throw new IllegalStateException("Drawable does not implement ShowTrackDrawable");
        }
    }

    public void setProgressDrawable(Drawable d) {
        super.setProgressDrawable(d);
        if (this.mProgressTint != null) {
            applyDeterminateProgressTint();
        }
    }

    public void setIndeterminateDrawable(Drawable d) {
        super.setIndeterminateDrawable(d);
        if (this.mProgressTint != null) {
            applyIndeterminateProgressTint();
        }
    }

    @Nullable
    public ColorStateList getProgressTintList() {
        return this.mProgressTint.mTintList;
    }

    public void setProgressTintList(@Nullable ColorStateList tint) {
        this.mProgressTint.mTintList = tint;
        this.mProgressTint.mHasTintList = true;
        applyProgressTint();
    }

    @Nullable
    public Mode getProgressTintMode() {
        return this.mProgressTint.mTintMode;
    }

    public void setProgressTintMode(@Nullable Mode tintMode) {
        this.mProgressTint.mTintMode = tintMode;
        this.mProgressTint.mHasTintMode = true;
        applyProgressTint();
    }

    private void applyProgressTint() {
        applyDeterminateProgressTint();
        applyIndeterminateProgressTint();
    }

    private void applyDeterminateProgressTint() {
        if (this.mProgressTint.mHasTintList || this.mProgressTint.mHasTintMode) {
            Drawable drawable = getProgressDrawable();
            if (drawable != null) {
                applyTintForDrawable(drawable, this.mProgressTint);
            }
        }
    }

    private void applyIndeterminateProgressTint() {
        if (this.mProgressTint.mHasTintList || this.mProgressTint.mHasTintMode) {
            Drawable drawable = getIndeterminateDrawable();
            if (drawable != null) {
                applyTintForDrawable(drawable, this.mProgressTint);
            }
        }
    }

    @SuppressLint({"NewApi"})
    private void applyTintForDrawable(Drawable drawable, TintInfo tint) {
        if (tint.mHasTintList || tint.mHasTintMode) {
            if (tint.mHasTintList) {
                if (drawable instanceof TintableDrawable) {
                    ((TintableDrawable) drawable).setTintList(tint.mTintList);
                } else {
                    Log.w(TAG, "drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
                    if (VERSION.SDK_INT >= 21) {
                        drawable.setTintList(tint.mTintList);
                    }
                }
            }
            if (tint.mHasTintMode) {
                if (drawable instanceof TintableDrawable) {
                    ((TintableDrawable) drawable).setTintMode(tint.mTintMode);
                } else {
                    Log.w(TAG, "drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
                    if (VERSION.SDK_INT >= 21) {
                        drawable.setTintMode(tint.mTintMode);
                    }
                }
            }
            if (drawable.isStateful()) {
                drawable.setState(getDrawableState());
            }
        }
    }
}
