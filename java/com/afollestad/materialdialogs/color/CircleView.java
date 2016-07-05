package com.afollestad.materialdialogs.color;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.afollestad.materialdialogs.util.DialogUtils;

public class CircleView extends FrameLayout {
    private final int borderWidthLarge;
    private final int borderWidthSmall;
    private final Paint innerPaint;
    private boolean mSelected;
    private final Paint outerPaint;
    private final Paint whitePaint;

    public CircleView(Context context) {
        this(context, null, 0);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources r = getResources();
        this.borderWidthSmall = (int) TypedValue.applyDimension(1, 3.0f, r.getDisplayMetrics());
        this.borderWidthLarge = (int) TypedValue.applyDimension(1, 5.0f, r.getDisplayMetrics());
        this.whitePaint = new Paint();
        this.whitePaint.setAntiAlias(true);
        this.whitePaint.setColor(-1);
        this.innerPaint = new Paint();
        this.innerPaint.setAntiAlias(true);
        this.outerPaint = new Paint();
        this.outerPaint.setAntiAlias(true);
        update(-12303292);
        setWillNotDraw(false);
    }

    private void update(@ColorInt int color) {
        this.innerPaint.setColor(color);
        this.outerPaint.setColor(shiftColorDown(color));
        Drawable selector = createSelector(color);
        if (VERSION.SDK_INT >= 21) {
            int[][] states = new int[1][];
            states[0] = new int[]{16842919};
            setForeground(new RippleDrawable(new ColorStateList(states, new int[]{shiftColorUp(color)}), selector, null));
            return;
        }
        setForeground(selector);
    }

    public void setBackgroundColor(@ColorInt int color) {
        update(color);
        requestLayout();
        invalidate();
    }

    public void setBackgroundResource(@ColorRes int color) {
        setBackgroundColor(DialogUtils.getColor(getContext(), color));
    }

    @Deprecated
    public void setBackground(Drawable background) {
        throw new IllegalStateException("Cannot use setBackground() on CircleView.");
    }

    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        throw new IllegalStateException("Cannot use setBackgroundDrawable() on CircleView.");
    }

    @Deprecated
    public void setActivated(boolean activated) {
        throw new IllegalStateException("Cannot use setActivated() on CircleView.");
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
        requestLayout();
        invalidate();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int outerRadius = getMeasuredWidth() / 2;
        if (this.mSelected) {
            int whiteRadius = outerRadius - this.borderWidthLarge;
            int innerRadius = whiteRadius - this.borderWidthSmall;
            canvas.drawCircle((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), (float) outerRadius, this.outerPaint);
            canvas.drawCircle((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), (float) whiteRadius, this.whitePaint);
            canvas.drawCircle((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), (float) innerRadius, this.innerPaint);
            return;
        }
        canvas.drawCircle((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), (float) outerRadius, this.innerPaint);
    }

    @ColorInt
    private static int translucentColor(int color) {
        return Color.argb(Math.round(((float) Color.alpha(color)) * 0.7f), Color.red(color), Color.green(color), Color.blue(color));
    }

    private Drawable createSelector(int color) {
        ShapeDrawable darkerCircle = new ShapeDrawable(new OvalShape());
        darkerCircle.getPaint().setColor(translucentColor(shiftColorUp(color)));
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, darkerCircle);
        return stateListDrawable;
    }

    @ColorInt
    public static int shiftColor(@ColorInt int color, @FloatRange(from = 0.0d, to = 2.0d) float by) {
        if (by == 1.0f) {
            return color;
        }
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] * by;
        return Color.HSVToColor(hsv);
    }

    @ColorInt
    public static int shiftColorDown(@ColorInt int color) {
        return shiftColor(color, 0.9f);
    }

    @ColorInt
    public static int shiftColorUp(@ColorInt int color) {
        return shiftColor(color, 1.1f);
    }

    public void showHint(int color) {
        int[] screenPos = new int[2];
        Rect displayFrame = new Rect();
        getLocationOnScreen(screenPos);
        getWindowVisibleDisplayFrame(displayFrame);
        Context context = getContext();
        int width = getWidth();
        int height = getHeight();
        int midy = screenPos[1] + (height / 2);
        int referenceX = screenPos[0] + (width / 2);
        if (ViewCompat.getLayoutDirection(this) == 0) {
            referenceX = context.getResources().getDisplayMetrics().widthPixels - referenceX;
        }
        Toast cheatSheet = Toast.makeText(context, String.format("#%06X", new Object[]{Integer.valueOf(16777215 & color)}), 0);
        if (midy < displayFrame.height()) {
            cheatSheet.setGravity(8388661, referenceX, (screenPos[1] + height) - displayFrame.top);
        } else {
            cheatSheet.setGravity(81, 0, height);
        }
        cheatSheet.show();
    }
}
