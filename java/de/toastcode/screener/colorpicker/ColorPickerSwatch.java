package de.toastcode.screener.colorpicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import de.toastcode.screener.R;

public class ColorPickerSwatch extends FrameLayout implements OnClickListener {
    private ImageView mCheckmarkImage = ((ImageView) findViewById(R.id.color_picker_checkmark));
    private int mColor;
    private OnColorSelectedListener mOnColorSelectedListener;
    private ImageView mSwatchImage = ((ImageView) findViewById(R.id.color_picker_swatch));

    public interface OnColorSelectedListener {
        void onColorSelected(int i);
    }

    public ColorPickerSwatch(Context context, int color, boolean checked, OnColorSelectedListener listener) {
        super(context);
        this.mColor = color;
        this.mOnColorSelectedListener = listener;
        LayoutInflater.from(context).inflate(R.layout.calendar_color_picker_swatch, this);
        setColor(color);
        setChecked(checked);
        setOnClickListener(this);
    }

    protected void setColor(int color) {
        Drawable[] colorDrawable = new Drawable[]{getContext().getResources().getDrawable(R.drawable.calendar_color_picker_swatch)};
        if (color == 16777215) {
            this.mSwatchImage.setImageDrawable(getResources().getDrawable(R.drawable.transparent));
        } else if (color == -1118482) {
            this.mSwatchImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_colorize_grey600_36dp));
        } else {
            this.mSwatchImage.setImageDrawable(new ColorStateDrawable(colorDrawable, color));
        }
    }

    private void setChecked(boolean checked) {
        if (checked) {
            this.mCheckmarkImage.setVisibility(0);
        } else {
            this.mCheckmarkImage.setVisibility(8);
        }
    }

    public void onClick(View v) {
        if (this.mOnColorSelectedListener != null) {
            this.mOnColorSelectedListener.onColorSelected(this.mColor);
        }
    }
}
