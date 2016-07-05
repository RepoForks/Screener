package com.mikepenz.materialdrawer.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

public class BezelImageView extends ImageView {
    private boolean isPressed;
    private boolean isSelected;
    private Paint mBlackPaint;
    private Rect mBounds;
    private RectF mBoundsF;
    private Bitmap mCacheBitmap;
    private boolean mCacheValid;
    private int mCachedHeight;
    private int mCachedWidth;
    private ColorMatrixColorFilter mDesaturateColorFilter;
    private boolean mDrawCircularShadow;
    private Drawable mMaskDrawable;
    private Paint mMaskedPaint;
    private int mSelectorAlpha;
    private int mSelectorColor;
    private ColorFilter mSelectorFilter;
    private ColorMatrixColorFilter mTempDesaturateColorFilter;
    private ColorFilter mTempSelectorFilter;

    @TargetApi(21)
    private class CustomOutline extends ViewOutlineProvider {
        int height;
        int width;

        CustomOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, this.width, this.height);
        }
    }

    public BezelImageView(Context context) {
        this(context, null);
    }

    public BezelImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezelImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mDrawCircularShadow = true;
        this.mSelectorAlpha = 150;
        this.mCacheValid = false;
        this.isPressed = false;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BezelImageView, defStyle, R.style.BezelImageView);
        this.mMaskDrawable = a.getDrawable(R.styleable.BezelImageView_biv_maskDrawable);
        if (this.mMaskDrawable != null) {
            this.mMaskDrawable.setCallback(this);
        }
        this.mDrawCircularShadow = a.getBoolean(R.styleable.BezelImageView_biv_drawCircularShadow, true);
        this.mSelectorColor = a.getColor(R.styleable.BezelImageView_biv_selectorOnPress, 0);
        a.recycle();
        this.mBlackPaint = new Paint();
        this.mBlackPaint.setColor(-16777216);
        this.mMaskedPaint = new Paint();
        this.mMaskedPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        this.mCacheBitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        this.mDesaturateColorFilter = new ColorMatrixColorFilter(cm);
        if (this.mSelectorColor != 0) {
            this.mSelectorFilter = new PorterDuffColorFilter(Color.argb(this.mSelectorAlpha, Color.red(this.mSelectorColor), Color.green(this.mSelectorColor), Color.blue(this.mSelectorColor)), Mode.SRC_ATOP);
        }
    }

    protected void onSizeChanged(int w, int h, int old_w, int old_h) {
        if (VERSION.SDK_INT >= 21 && this.mDrawCircularShadow) {
            setOutlineProvider(new CustomOutline(w, h));
        }
    }

    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        this.mBounds = new Rect(0, 0, r - l, b - t);
        this.mBoundsF = new RectF(this.mBounds);
        if (this.mMaskDrawable != null) {
            this.mMaskDrawable.setBounds(this.mBounds);
        }
        if (changed) {
            this.mCacheValid = false;
        }
        return changed;
    }

    protected void onDraw(Canvas canvas) {
        if (this.mBounds != null) {
            int width = this.mBounds.width();
            int height = this.mBounds.height();
            if (width != 0 && height != 0) {
                if (!(this.mCacheValid && width == this.mCachedWidth && height == this.mCachedHeight && this.isSelected == this.isPressed)) {
                    if (width == this.mCachedWidth && height == this.mCachedHeight) {
                        this.mCacheBitmap.eraseColor(0);
                    } else {
                        this.mCacheBitmap.recycle();
                        this.mCacheBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                        this.mCachedWidth = width;
                        this.mCachedHeight = height;
                    }
                    Canvas cacheCanvas = new Canvas(this.mCacheBitmap);
                    int sc;
                    if (this.mMaskDrawable != null) {
                        sc = cacheCanvas.save();
                        this.mMaskDrawable.draw(cacheCanvas);
                        if (!this.isSelected) {
                            this.mMaskedPaint.setColorFilter(null);
                        } else if (this.mSelectorFilter != null) {
                            this.mMaskedPaint.setColorFilter(this.mSelectorFilter);
                        } else {
                            this.mMaskedPaint.setColorFilter(this.mDesaturateColorFilter);
                        }
                        cacheCanvas.saveLayer(this.mBoundsF, this.mMaskedPaint, 12);
                        super.onDraw(cacheCanvas);
                        cacheCanvas.restoreToCount(sc);
                    } else if (this.isSelected) {
                        sc = cacheCanvas.save();
                        cacheCanvas.drawRect(0.0f, 0.0f, (float) this.mCachedWidth, (float) this.mCachedHeight, this.mBlackPaint);
                        if (this.mSelectorFilter != null) {
                            this.mMaskedPaint.setColorFilter(this.mSelectorFilter);
                        } else {
                            this.mMaskedPaint.setColorFilter(this.mDesaturateColorFilter);
                        }
                        cacheCanvas.saveLayer(this.mBoundsF, this.mMaskedPaint, 12);
                        super.onDraw(cacheCanvas);
                        cacheCanvas.restoreToCount(sc);
                    } else {
                        super.onDraw(cacheCanvas);
                    }
                }
                canvas.drawBitmap(this.mCacheBitmap, (float) this.mBounds.left, (float) this.mBounds.top, null);
                this.isPressed = isPressed();
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isClickable()) {
            switch (event.getAction()) {
                case me.zhanghai.android.materialprogressbar.R.styleable.View_android_theme /*0*/:
                    this.isSelected = true;
                    break;
                case me.zhanghai.android.materialprogressbar.R.styleable.View_android_focusable /*1*/:
                case me.zhanghai.android.materialprogressbar.R.styleable.View_paddingEnd /*3*/:
                case me.zhanghai.android.materialprogressbar.R.styleable.View_theme /*4*/:
                case me.zhanghai.android.materialprogressbar.R.styleable.Toolbar_contentInsetRight /*8*/:
                    this.isSelected = false;
                    break;
            }
            invalidate();
            return super.dispatchTouchEvent(event);
        }
        this.isSelected = false;
        return super.onTouchEvent(event);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mMaskDrawable != null && this.mMaskDrawable.isStateful()) {
            this.mMaskDrawable.setState(getDrawableState());
        }
        if (isDuplicateParentStateEnabled()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void invalidateDrawable(Drawable who) {
        if (who == this.mMaskDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(who);
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return who == this.mMaskDrawable || super.verifyDrawable(who);
    }

    public void setSelectorColor(int selectorColor) {
        this.mSelectorColor = selectorColor;
        this.mSelectorFilter = new PorterDuffColorFilter(Color.argb(this.mSelectorAlpha, Color.red(this.mSelectorColor), Color.green(this.mSelectorColor), Color.blue(this.mSelectorColor)), Mode.SRC_ATOP);
        invalidate();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    public void setImageURI(Uri uri) {
        if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
            DrawerImageLoader.getInstance().setImage(this, uri, null);
        } else {
            super.setImageURI(uri);
        }
    }

    public void disableTouchFeedback(boolean disable) {
        if (disable) {
            this.mTempDesaturateColorFilter = this.mDesaturateColorFilter;
            this.mTempSelectorFilter = this.mSelectorFilter;
            this.mSelectorFilter = null;
            this.mDesaturateColorFilter = null;
            return;
        }
        if (this.mTempDesaturateColorFilter != null) {
            this.mDesaturateColorFilter = this.mTempDesaturateColorFilter;
        }
        if (this.mTempSelectorFilter != null) {
            this.mSelectorFilter = this.mTempSelectorFilter;
        }
    }
}
