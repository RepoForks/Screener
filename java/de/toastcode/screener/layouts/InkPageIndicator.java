package de.toastcode.screener.layouts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnAttachStateChangeListener;
import android.view.animation.Interpolator;
import de.toastcode.screener.R;
import java.util.Arrays;

public class InkPageIndicator extends View implements OnPageChangeListener, OnAttachStateChangeListener {
    private static final int DEFAULT_ANIM_DURATION = 400;
    private static final int DEFAULT_DOT_SIZE = 8;
    private static final int DEFAULT_GAP = 12;
    private static final int DEFAULT_SELECTED_COLOUR = -1;
    private static final int DEFAULT_UNSELECTED_COLOUR = -2130706433;
    private static final float INVALID_FRACTION = -1.0f;
    private static final float MINIMAL_REVEAL = 1.0E-5f;
    private long animDuration;
    private long animHalfDuration;
    private Path combinedUnselectedPath;
    float controlX1;
    float controlX2;
    float controlY1;
    float controlY2;
    private int currentPage;
    private float dotBottomY;
    private float[] dotCenterX;
    private float dotCenterY;
    private int dotDiameter;
    private float dotRadius;
    private float[] dotRevealFractions;
    private float dotTopY;
    float endX1;
    float endX2;
    float endY1;
    float endY2;
    private int gap;
    private float halfDotRadius;
    private final Interpolator interpolator;
    private boolean isAttachedToWindow;
    private AnimatorSet joiningAnimationSet;
    private float[] joiningFractions;
    private ValueAnimator moveAnimation;
    private boolean pageChanging;
    private int pageCount;
    private int previousPage;
    private final RectF rectF;
    private PendingRetreatAnimator retreatAnimation;
    private float retreatingJoinX1;
    private float retreatingJoinX2;
    private PendingRevealAnimator[] revealAnimations;
    private int selectedColour;
    private boolean selectedDotInPosition;
    private float selectedDotX;
    private final Paint selectedPaint;
    private int unselectedColour;
    private final Path unselectedDotLeftPath;
    private final Path unselectedDotPath;
    private final Path unselectedDotRightPath;
    private final Paint unselectedPaint;
    private ViewPager viewPager;

    public abstract class StartPredicate {
        protected float thresholdValue;

        abstract boolean shouldStart(float f);

        public StartPredicate(float thresholdValue) {
            this.thresholdValue = thresholdValue;
        }
    }

    public class LeftwardStartPredicate extends StartPredicate {
        public LeftwardStartPredicate(float thresholdValue) {
            super(thresholdValue);
        }

        boolean shouldStart(float currentValue) {
            return currentValue < this.thresholdValue;
        }
    }

    public abstract class PendingStartAnimator extends ValueAnimator {
        protected boolean hasStarted = false;
        protected StartPredicate predicate;

        public PendingStartAnimator(StartPredicate predicate) {
            this.predicate = predicate;
        }

        public void startIfNecessary(float currentValue) {
            if (!this.hasStarted && this.predicate.shouldStart(currentValue)) {
                start();
                this.hasStarted = true;
            }
        }
    }

    public class PendingRetreatAnimator extends PendingStartAnimator {
        public PendingRetreatAnimator(int was, int now, int steps, StartPredicate predicate) {
            float initialX1;
            float finalX1;
            float initialX2;
            float finalX2;
            super(predicate);
            setDuration(InkPageIndicator.this.animHalfDuration);
            setInterpolator(InkPageIndicator.this.interpolator);
            if (now > was) {
                initialX1 = Math.min(InkPageIndicator.this.dotCenterX[was], InkPageIndicator.this.selectedDotX) - InkPageIndicator.this.dotRadius;
            } else {
                initialX1 = InkPageIndicator.this.dotCenterX[now] - InkPageIndicator.this.dotRadius;
            }
            if (now > was) {
                finalX1 = InkPageIndicator.this.dotCenterX[now] - InkPageIndicator.this.dotRadius;
            } else {
                finalX1 = InkPageIndicator.this.dotCenterX[now] - InkPageIndicator.this.dotRadius;
            }
            if (now > was) {
                initialX2 = InkPageIndicator.this.dotCenterX[now] + InkPageIndicator.this.dotRadius;
            } else {
                initialX2 = Math.max(InkPageIndicator.this.dotCenterX[was], InkPageIndicator.this.selectedDotX) + InkPageIndicator.this.dotRadius;
            }
            if (now > was) {
                finalX2 = InkPageIndicator.this.dotCenterX[now] + InkPageIndicator.this.dotRadius;
            } else {
                finalX2 = InkPageIndicator.this.dotCenterX[now] + InkPageIndicator.this.dotRadius;
            }
            InkPageIndicator.this.revealAnimations = new PendingRevealAnimator[steps];
            final int[] dotsToHide = new int[steps];
            int i;
            if (initialX1 != finalX1) {
                setFloatValues(new float[]{initialX1, finalX1});
                for (i = 0; i < steps; i++) {
                    InkPageIndicator.this.revealAnimations[i] = new PendingRevealAnimator(was + i, new RightwardStartPredicate(InkPageIndicator.this.dotCenterX[was + i]));
                    dotsToHide[i] = was + i;
                }
                addUpdateListener(new AnimatorUpdateListener(InkPageIndicator.this) {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        InkPageIndicator.this.retreatingJoinX1 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                        ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
                        for (PendingRevealAnimator pendingReveal : InkPageIndicator.this.revealAnimations) {
                            pendingReveal.startIfNecessary(InkPageIndicator.this.retreatingJoinX1);
                        }
                    }
                });
            } else {
                setFloatValues(new float[]{initialX2, finalX2});
                for (i = 0; i < steps; i++) {
                    InkPageIndicator.this.revealAnimations[i] = new PendingRevealAnimator(was - i, new LeftwardStartPredicate(InkPageIndicator.this.dotCenterX[was - i]));
                    dotsToHide[i] = was - i;
                }
                addUpdateListener(new AnimatorUpdateListener(InkPageIndicator.this) {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        InkPageIndicator.this.retreatingJoinX2 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                        ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
                        for (PendingRevealAnimator pendingReveal : InkPageIndicator.this.revealAnimations) {
                            pendingReveal.startIfNecessary(InkPageIndicator.this.retreatingJoinX2);
                        }
                    }
                });
            }
            final InkPageIndicator inkPageIndicator = InkPageIndicator.this;
            addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation) {
                    InkPageIndicator.this.cancelJoiningAnimations();
                    InkPageIndicator.this.clearJoiningFractions();
                    for (int dot : dotsToHide) {
                        InkPageIndicator.this.setDotRevealFraction(dot, InkPageIndicator.MINIMAL_REVEAL);
                    }
                    InkPageIndicator.this.retreatingJoinX1 = initialX1;
                    InkPageIndicator.this.retreatingJoinX2 = initialX2;
                    ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
                }

                public void onAnimationEnd(Animator animation) {
                    InkPageIndicator.this.retreatingJoinX1 = InkPageIndicator.INVALID_FRACTION;
                    InkPageIndicator.this.retreatingJoinX2 = InkPageIndicator.INVALID_FRACTION;
                    ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
                }
            });
        }
    }

    public class PendingRevealAnimator extends PendingStartAnimator {
        private int dot;

        public PendingRevealAnimator(int dot, StartPredicate predicate) {
            super(predicate);
            setFloatValues(new float[]{InkPageIndicator.MINIMAL_REVEAL, 1.0f});
            this.dot = dot;
            setDuration(InkPageIndicator.this.animHalfDuration);
            setInterpolator(InkPageIndicator.this.interpolator);
            addUpdateListener(new AnimatorUpdateListener(InkPageIndicator.this) {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    InkPageIndicator.this.setDotRevealFraction(PendingRevealAnimator.this.dot, ((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
            });
            addListener(new AnimatorListenerAdapter(InkPageIndicator.this) {
                public void onAnimationEnd(Animator animation) {
                    InkPageIndicator.this.setDotRevealFraction(PendingRevealAnimator.this.dot, 0.0f);
                    ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
                }
            });
        }
    }

    public class RightwardStartPredicate extends StartPredicate {
        public RightwardStartPredicate(float thresholdValue) {
            super(thresholdValue);
        }

        boolean shouldStart(float currentValue) {
            return currentValue > this.thresholdValue;
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentPage = in.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.currentPage);
        }
    }

    public InkPageIndicator(Context context) {
        this(context, null, 0);
    }

    public InkPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InkPageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int density = (int) context.getResources().getDisplayMetrics().density;
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.InkPageIndicator, defStyle, 0);
        this.dotDiameter = a.getDimensionPixelSize(0, density * DEFAULT_DOT_SIZE);
        this.dotRadius = (float) (this.dotDiameter / 2);
        this.halfDotRadius = this.dotRadius / 2.0f;
        this.gap = a.getDimensionPixelSize(1, density * DEFAULT_GAP);
        this.animDuration = (long) a.getInteger(2, DEFAULT_ANIM_DURATION);
        this.animHalfDuration = this.animDuration / 2;
        this.unselectedColour = a.getColor(3, DEFAULT_UNSELECTED_COLOUR);
        this.selectedColour = a.getColor(4, DEFAULT_SELECTED_COLOUR);
        a.recycle();
        this.unselectedPaint = new Paint(1);
        this.unselectedPaint.setColor(this.unselectedColour);
        this.selectedPaint = new Paint(1);
        this.selectedPaint.setColor(this.selectedColour);
        this.interpolator = new FastOutSlowInInterpolator();
        this.combinedUnselectedPath = new Path();
        this.unselectedDotPath = new Path();
        this.unselectedDotLeftPath = new Path();
        this.unselectedDotRightPath = new Path();
        this.rectF = new RectF();
        addOnAttachStateChangeListener(this);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        setPageCount(viewPager.getAdapter().getCount());
        viewPager.getAdapter().registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                InkPageIndicator.this.setPageCount(InkPageIndicator.this.viewPager.getAdapter().getCount());
            }
        });
        setCurrentPageImmediate();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.isAttachedToWindow) {
            float fraction = positionOffset;
            int currentPosition = this.pageChanging ? this.previousPage : this.currentPage;
            int leftDotPosition = position;
            if (currentPosition != position) {
                fraction = 1.0f - positionOffset;
                if (fraction == 1.0f) {
                    leftDotPosition = Math.min(currentPosition, position);
                }
            }
            setJoiningFraction(leftDotPosition, fraction);
        }
    }

    public void onPageSelected(int position) {
        if (this.isAttachedToWindow) {
            setSelectedPage(position);
        } else {
            setCurrentPageImmediate();
        }
    }

    public void onPageScrollStateChanged(int state) {
    }

    private void setPageCount(int pages) {
        this.pageCount = pages;
        resetState();
        requestLayout();
    }

    private void calculateDotPositions(int width, int height) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int bottom = height - getPaddingBottom();
        float startLeft = ((float) (((((width - getPaddingRight()) - left) - getRequiredWidth()) / 2) + left)) + this.dotRadius;
        this.dotCenterX = new float[this.pageCount];
        for (int i = 0; i < this.pageCount; i++) {
            this.dotCenterX[i] = ((float) ((this.dotDiameter + this.gap) * i)) + startLeft;
        }
        this.dotTopY = (float) top;
        this.dotCenterY = ((float) top) + this.dotRadius;
        this.dotBottomY = (float) (this.dotDiameter + top);
        setCurrentPageImmediate();
    }

    private void setCurrentPageImmediate() {
        if (this.viewPager != null) {
            this.currentPage = this.viewPager.getCurrentItem();
        } else {
            this.currentPage = 0;
        }
        if (this.dotCenterX != null && this.dotCenterX.length > 0) {
            if (this.moveAnimation == null || !this.moveAnimation.isStarted()) {
                this.selectedDotX = this.dotCenterX[this.currentPage];
            }
        }
    }

    private void resetState() {
        this.joiningFractions = new float[(this.pageCount + DEFAULT_SELECTED_COLOUR)];
        Arrays.fill(this.joiningFractions, 0.0f);
        this.dotRevealFractions = new float[this.pageCount];
        Arrays.fill(this.dotRevealFractions, 0.0f);
        this.retreatingJoinX1 = INVALID_FRACTION;
        this.retreatingJoinX2 = INVALID_FRACTION;
        this.selectedDotInPosition = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int width;
        int desiredHeight = getDesiredHeight();
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case Integer.MIN_VALUE:
                height = Math.min(desiredHeight, MeasureSpec.getSize(heightMeasureSpec));
                break;
            case 1073741824:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            default:
                height = desiredHeight;
                break;
        }
        int desiredWidth = getDesiredWidth();
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case Integer.MIN_VALUE:
                width = Math.min(desiredWidth, MeasureSpec.getSize(widthMeasureSpec));
                break;
            case 1073741824:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            default:
                width = desiredWidth;
                break;
        }
        setMeasuredDimension(width, height);
        calculateDotPositions(width, height);
    }

    private int getDesiredHeight() {
        return (getPaddingTop() + this.dotDiameter) + getPaddingBottom();
    }

    private int getRequiredWidth() {
        return (this.pageCount * this.dotDiameter) + ((this.pageCount + DEFAULT_SELECTED_COLOUR) * this.gap);
    }

    private int getDesiredWidth() {
        return (getPaddingLeft() + getRequiredWidth()) + getPaddingRight();
    }

    public void onViewAttachedToWindow(View view) {
        this.isAttachedToWindow = true;
    }

    public void onViewDetachedFromWindow(View view) {
        this.isAttachedToWindow = false;
    }

    protected void onDraw(Canvas canvas) {
        if (this.viewPager != null && this.pageCount != 0) {
            drawUnselected(canvas);
            drawSelected(canvas);
        }
    }

    private void drawUnselected(Canvas canvas) {
        this.combinedUnselectedPath.rewind();
        int page = 0;
        while (page < this.pageCount) {
            Path unselectedPath = getUnselectedPath(page, this.dotCenterX[page], this.dotCenterX[page == this.pageCount + DEFAULT_SELECTED_COLOUR ? page : page + 1], page == this.pageCount + DEFAULT_SELECTED_COLOUR ? INVALID_FRACTION : this.joiningFractions[page], this.dotRevealFractions[page]);
            unselectedPath.addPath(this.combinedUnselectedPath);
            this.combinedUnselectedPath.addPath(unselectedPath);
            page++;
        }
        if (this.retreatingJoinX1 != INVALID_FRACTION) {
            this.combinedUnselectedPath.addPath(getRetreatingJoinPath());
        }
        canvas.drawPath(this.combinedUnselectedPath, this.unselectedPaint);
    }

    private Path getUnselectedPath(int page, float centerX, float nextCenterX, float joiningFraction, float dotRevealFraction) {
        this.unselectedDotPath.rewind();
        if ((joiningFraction == 0.0f || joiningFraction == INVALID_FRACTION) && dotRevealFraction == 0.0f && !(page == this.currentPage && this.selectedDotInPosition)) {
            this.unselectedDotPath.addCircle(this.dotCenterX[page], this.dotCenterY, this.dotRadius, Direction.CW);
        }
        if (joiningFraction > 0.0f && joiningFraction <= 0.5f && this.retreatingJoinX1 == INVALID_FRACTION) {
            this.unselectedDotLeftPath.rewind();
            this.unselectedDotLeftPath.moveTo(centerX, this.dotBottomY);
            this.rectF.set(centerX - this.dotRadius, this.dotTopY, this.dotRadius + centerX, this.dotBottomY);
            this.unselectedDotLeftPath.arcTo(this.rectF, 90.0f, 180.0f, true);
            this.endX1 = (this.dotRadius + centerX) + (((float) this.gap) * joiningFraction);
            this.endY1 = this.dotCenterY;
            this.controlX1 = this.halfDotRadius + centerX;
            this.controlY1 = this.dotTopY;
            this.controlX2 = this.endX1;
            this.controlY2 = this.endY1 - this.halfDotRadius;
            this.unselectedDotLeftPath.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX1, this.endY1);
            this.endX2 = centerX;
            this.endY2 = this.dotBottomY;
            this.controlX1 = this.endX1;
            this.controlY1 = this.endY1 + this.halfDotRadius;
            this.controlX2 = this.halfDotRadius + centerX;
            this.controlY2 = this.dotBottomY;
            this.unselectedDotLeftPath.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX2, this.endY2);
            this.unselectedDotPath.addPath(this.unselectedDotLeftPath);
            this.unselectedDotRightPath.rewind();
            this.unselectedDotRightPath.moveTo(nextCenterX, this.dotBottomY);
            this.rectF.set(nextCenterX - this.dotRadius, this.dotTopY, this.dotRadius + nextCenterX, this.dotBottomY);
            this.unselectedDotRightPath.arcTo(this.rectF, 90.0f, -180.0f, true);
            this.endX1 = (nextCenterX - this.dotRadius) - (((float) this.gap) * joiningFraction);
            this.endY1 = this.dotCenterY;
            this.controlX1 = nextCenterX - this.halfDotRadius;
            this.controlY1 = this.dotTopY;
            this.controlX2 = this.endX1;
            this.controlY2 = this.endY1 - this.halfDotRadius;
            this.unselectedDotRightPath.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX1, this.endY1);
            this.endX2 = nextCenterX;
            this.endY2 = this.dotBottomY;
            this.controlX1 = this.endX1;
            this.controlY1 = this.endY1 + this.halfDotRadius;
            this.controlX2 = this.endX2 - this.halfDotRadius;
            this.controlY2 = this.dotBottomY;
            this.unselectedDotRightPath.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX2, this.endY2);
            this.unselectedDotPath.addPath(this.unselectedDotRightPath);
        }
        if (joiningFraction > 0.5f && joiningFraction < 1.0f && this.retreatingJoinX1 == INVALID_FRACTION) {
            float adjustedFraction = (joiningFraction - 0.2f) * 1.25f;
            this.unselectedDotPath.moveTo(centerX, this.dotBottomY);
            this.rectF.set(centerX - this.dotRadius, this.dotTopY, this.dotRadius + centerX, this.dotBottomY);
            this.unselectedDotPath.arcTo(this.rectF, 90.0f, 180.0f, true);
            this.endX1 = (this.dotRadius + centerX) + ((float) (this.gap / 2));
            this.endY1 = this.dotCenterY - (this.dotRadius * adjustedFraction);
            this.controlX1 = this.endX1 - (this.dotRadius * adjustedFraction);
            this.controlY1 = this.dotTopY;
            this.controlX2 = this.endX1 - ((1.0f - adjustedFraction) * this.dotRadius);
            this.controlY2 = this.endY1;
            this.unselectedDotPath.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX1, this.endY1);
            this.endX2 = nextCenterX;
            this.endY2 = this.dotTopY;
            this.controlX1 = this.endX1 + ((1.0f - adjustedFraction) * this.dotRadius);
            this.controlY1 = this.endY1;
            this.controlX2 = this.endX1 + (this.dotRadius * adjustedFraction);
            this.controlY2 = this.dotTopY;
            this.unselectedDotPath.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX2, this.endY2);
            this.rectF.set(nextCenterX - this.dotRadius, this.dotTopY, this.dotRadius + nextCenterX, this.dotBottomY);
            this.unselectedDotPath.arcTo(this.rectF, 270.0f, 180.0f, true);
            this.endY1 = this.dotCenterY + (this.dotRadius * adjustedFraction);
            this.controlX1 = this.endX1 + (this.dotRadius * adjustedFraction);
            this.controlY1 = this.dotBottomY;
            this.controlX2 = this.endX1 + ((1.0f - adjustedFraction) * this.dotRadius);
            this.controlY2 = this.endY1;
            this.unselectedDotPath.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX1, this.endY1);
            this.endX2 = centerX;
            this.endY2 = this.dotBottomY;
            this.controlX1 = this.endX1 - ((1.0f - adjustedFraction) * this.dotRadius);
            this.controlY1 = this.endY1;
            this.controlX2 = this.endX1 - (this.dotRadius * adjustedFraction);
            this.controlY2 = this.endY2;
            this.unselectedDotPath.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX2, this.endY2);
        }
        if (joiningFraction == 1.0f && this.retreatingJoinX1 == INVALID_FRACTION) {
            this.rectF.set(centerX - this.dotRadius, this.dotTopY, this.dotRadius + nextCenterX, this.dotBottomY);
            this.unselectedDotPath.addRoundRect(this.rectF, this.dotRadius, this.dotRadius, Direction.CW);
        }
        if (dotRevealFraction > MINIMAL_REVEAL) {
            this.unselectedDotPath.addCircle(centerX, this.dotCenterY, this.dotRadius * dotRevealFraction, Direction.CW);
        }
        return this.unselectedDotPath;
    }

    private Path getRetreatingJoinPath() {
        this.unselectedDotPath.rewind();
        this.rectF.set(this.retreatingJoinX1, this.dotTopY, this.retreatingJoinX2, this.dotBottomY);
        this.unselectedDotPath.addRoundRect(this.rectF, this.dotRadius, this.dotRadius, Direction.CW);
        return this.unselectedDotPath;
    }

    private void drawSelected(Canvas canvas) {
        canvas.drawCircle(this.selectedDotX, this.dotCenterY, this.dotRadius, this.selectedPaint);
    }

    private void setSelectedPage(int now) {
        if (now != this.currentPage) {
            this.pageChanging = true;
            this.previousPage = this.currentPage;
            this.currentPage = now;
            int steps = Math.abs(now - this.previousPage);
            if (steps > 1) {
                int i;
                if (now > this.previousPage) {
                    for (i = 0; i < steps; i++) {
                        setJoiningFraction(this.previousPage + i, 1.0f);
                    }
                } else {
                    for (i = DEFAULT_SELECTED_COLOUR; i > (-steps); i += DEFAULT_SELECTED_COLOUR) {
                        setJoiningFraction(this.previousPage + i, 1.0f);
                    }
                }
            }
            this.moveAnimation = createMoveSelectedAnimator(this.dotCenterX[now], this.previousPage, now, steps);
            this.moveAnimation.start();
        }
    }

    private ValueAnimator createMoveSelectedAnimator(float moveTo, int was, int now, int steps) {
        StartPredicate rightwardStartPredicate;
        ValueAnimator moveSelected = ValueAnimator.ofFloat(new float[]{this.selectedDotX, moveTo});
        if (now > was) {
            rightwardStartPredicate = new RightwardStartPredicate(moveTo - ((moveTo - this.selectedDotX) * 0.25f));
        } else {
            rightwardStartPredicate = new LeftwardStartPredicate(((this.selectedDotX - moveTo) * 0.25f) + moveTo);
        }
        this.retreatAnimation = new PendingRetreatAnimator(was, now, steps, rightwardStartPredicate);
        this.retreatAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                InkPageIndicator.this.resetState();
                InkPageIndicator.this.pageChanging = false;
            }
        });
        moveSelected.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                InkPageIndicator.this.selectedDotX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                InkPageIndicator.this.retreatAnimation.startIfNecessary(InkPageIndicator.this.selectedDotX);
                ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
            }
        });
        moveSelected.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                InkPageIndicator.this.selectedDotInPosition = false;
            }

            public void onAnimationEnd(Animator animation) {
                InkPageIndicator.this.selectedDotInPosition = true;
            }
        });
        moveSelected.setStartDelay(this.selectedDotInPosition ? this.animDuration / 4 : 0);
        moveSelected.setDuration((this.animDuration * 3) / 4);
        moveSelected.setInterpolator(this.interpolator);
        return moveSelected;
    }

    private void setJoiningFraction(int leftDot, float fraction) {
        if (leftDot < this.joiningFractions.length) {
            if (leftDot == 1) {
                this.joiningFractions[leftDot] = fraction;
                ViewCompat.postInvalidateOnAnimation(this);
            } else {
                this.joiningFractions[leftDot] = fraction;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    private void clearJoiningFractions() {
        Arrays.fill(this.joiningFractions, 0.0f);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void setDotRevealFraction(int dot, float fraction) {
        if (dot < this.dotRevealFractions.length) {
            this.dotRevealFractions[dot] = fraction;
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void cancelJoiningAnimations() {
        if (this.joiningAnimationSet != null && this.joiningAnimationSet.isRunning()) {
            this.joiningAnimationSet.cancel();
        }
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.currentPage = savedState.currentPage;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentPage = this.currentPage;
        return savedState;
    }
}
