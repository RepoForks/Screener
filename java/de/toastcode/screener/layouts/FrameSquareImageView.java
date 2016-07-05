package de.toastcode.screener.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FrameSquareImageView extends ImageView {
    public FrameSquareImageView(Context context) {
        super(context);
    }

    public FrameSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameSquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
