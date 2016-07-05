package de.toastcode.screener.colorpicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import de.toastcode.screener.R;
import de.toastcode.screener.colorpicker.ColorPickerSwatch.OnColorSelectedListener;

public class ColorPickerDialog extends DialogFragment implements OnColorSelectedListener {
    protected static final String KEY_COLORS = "colors";
    protected static final String KEY_COLUMNS = "columns";
    protected static final String KEY_SELECTED_COLOR = "selected_color";
    protected static final String KEY_SIZE = "size";
    protected static final String KEY_TITLE_ID = "title_id";
    public static final int SIZE_LARGE = 1;
    public static final int SIZE_SMALL = 2;
    protected AlertDialog mAlertDialog;
    protected int[] mColors = null;
    protected int mColumns;
    protected OnColorSelectedListener mListener;
    private ColorPickerPalette mPalette;
    private ProgressBar mProgress;
    protected int mSelectedColor;
    protected int mSize;
    protected int mTitleResId = R.string.color_picker_default_title;

    public static ColorPickerDialog newInstance(int titleResId, int[] colors, int selectedColor, int columns, int size) {
        ColorPickerDialog ret = new ColorPickerDialog();
        ret.initialize(titleResId, colors, selectedColor, columns, size);
        return ret;
    }

    public void initialize(int titleResId, int[] colors, int selectedColor, int columns, int size) {
        setArguments(titleResId, columns, size);
        setColors(colors, selectedColor);
    }

    public void setArguments(int titleResId, int columns, int size) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TITLE_ID, titleResId);
        bundle.putInt(KEY_COLUMNS, columns);
        bundle.putInt(KEY_SIZE, size);
        setArguments(bundle);
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.mListener = listener;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mTitleResId = getArguments().getInt(KEY_TITLE_ID);
            this.mColumns = getArguments().getInt(KEY_COLUMNS);
            this.mSize = getArguments().getInt(KEY_SIZE);
        }
        if (savedInstanceState != null) {
            this.mColors = savedInstanceState.getIntArray(KEY_COLORS);
            this.mSelectedColor = ((Integer) savedInstanceState.getSerializable(KEY_SELECTED_COLOR)).intValue();
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.calendar_color_picker_dialog, null);
        this.mProgress = (ProgressBar) view.findViewById(16908301);
        this.mPalette = (ColorPickerPalette) view.findViewById(R.id.color_picker);
        this.mPalette.init(this.mSize, this.mColumns, this);
        if (this.mColors != null) {
            showPaletteView();
        }
        this.mAlertDialog = new Builder(activity).setTitle(this.mTitleResId).setView(view).create();
        return this.mAlertDialog;
    }

    public void onColorSelected(int color) {
        if (this.mListener != null) {
            this.mListener.onColorSelected(color);
        }
        if (getTargetFragment() instanceof OnColorSelectedListener) {
            ((OnColorSelectedListener) getTargetFragment()).onColorSelected(color);
        }
        if (color != this.mSelectedColor) {
            this.mSelectedColor = color;
            this.mPalette.drawPalette(this.mColors, this.mSelectedColor);
        }
        dismiss();
    }

    public void showPaletteView() {
        if (this.mProgress != null && this.mPalette != null) {
            this.mProgress.setVisibility(8);
            refreshPalette();
            this.mPalette.setVisibility(0);
        }
    }

    public void showProgressBarView() {
        if (this.mProgress != null && this.mPalette != null) {
            this.mProgress.setVisibility(0);
            this.mPalette.setVisibility(8);
        }
    }

    public void setColors(int[] colors, int selectedColor) {
        if (this.mColors != colors || this.mSelectedColor != selectedColor) {
            this.mColors = colors;
            this.mSelectedColor = selectedColor;
            refreshPalette();
        }
    }

    private void refreshPalette() {
        if (this.mPalette != null && this.mColors != null) {
            this.mPalette.drawPalette(this.mColors, this.mSelectedColor);
        }
    }

    public int[] getColors() {
        return this.mColors;
    }

    public void setColors(int[] colors) {
        if (this.mColors != colors) {
            this.mColors = colors;
            refreshPalette();
        }
    }

    public int getSelectedColor() {
        return this.mSelectedColor;
    }

    public void setSelectedColor(int color) {
        if (this.mSelectedColor != color) {
            this.mSelectedColor = color;
            refreshPalette();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_COLORS, this.mColors);
        outState.putSerializable(KEY_SELECTED_COLOR, Integer.valueOf(this.mSelectedColor));
    }
}
