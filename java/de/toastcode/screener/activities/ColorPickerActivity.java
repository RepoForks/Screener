package de.toastcode.screener.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import de.toastcode.screener.R;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class ColorPickerActivity extends AppCompatActivity implements OnSeekBarChangeListener {
    AlertDialog alertDialog;
    int blue;
    SeekBar blueSeekBar;
    TextView blueToolTip;
    EditText buttonSelector;
    ClipboardManager clipBoard;
    String color;
    View colorView;
    Display display;
    FloatingActionButton fab;
    int green;
    SeekBar greenSeekBar;
    TextView greenToolTip;
    ViewPropertyAnimator mAnimator;
    Toolbar mToolbar;
    int red;
    SeekBar redSeekBar;
    TextView redToolTip;
    int seekBarLeft;
    Rect thumbRect;
    Window window;

    public static boolean isHex(String str) {
        try {
            Integer.parseInt(str.replaceFirst("#", BuildConfig.FLAVOR), 16);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= 21) {
            setContentView(R.layout.layout_color_picker);
        } else {
            setContentView(R.layout.layout_16);
        }
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.mToolbar);
        this.mToolbar.setClickable(true);
        this.mToolbar.setTitle(R.string.action_settings);
        this.mToolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ColorPickerActivity.this.savePreferences("color", ColorPickerActivity.this.color);
                ColorPickerActivity.this.setResult(-1, new Intent());
                ColorPickerActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.display = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        SharedPreferences settings = getSharedPreferences("COLOR_SETTINGS", 0);
        this.red = settings.getInt("RED_COLOR", 0);
        this.green = settings.getInt("GREEN_COLOR", 0);
        this.blue = settings.getInt("BLUE_COLOR", 0);
        this.clipBoard = (ClipboardManager) getSystemService("clipboard");
        this.colorView = findViewById(R.id.colorView);
        this.window = getWindow();
        this.redSeekBar = (SeekBar) findViewById(R.id.redSeekBar);
        this.greenSeekBar = (SeekBar) findViewById(R.id.greenSeekBar);
        this.blueSeekBar = (SeekBar) findViewById(R.id.blueSeekBar);
        this.seekBarLeft = this.redSeekBar.getPaddingLeft();
        this.redToolTip = (TextView) findViewById(R.id.redToolTip);
        this.greenToolTip = (TextView) findViewById(R.id.greenToolTip);
        this.blueToolTip = (TextView) findViewById(R.id.blueToolTip);
        this.buttonSelector = (EditText) findViewById(R.id.buttonSelector);
        this.fab = (FloatingActionButton) findViewById(R.id.fab1);
        this.redSeekBar.setOnSeekBarChangeListener(this);
        this.greenSeekBar.setOnSeekBarChangeListener(this);
        this.blueSeekBar.setOnSeekBarChangeListener(this);
        this.redSeekBar.setProgress(this.red);
        this.greenSeekBar.setProgress(this.green);
        this.blueSeekBar.setProgress(this.blue);
        this.colorView.setBackgroundColor(Color.rgb(this.red, this.green, this.blue));
        this.fab.setRippleColor(ContextCompat.getColor(getApplicationContext(), R.color.white_trans_75));
        this.buttonSelector.setText(String.format("%02x%02x%02x", new Object[]{Integer.valueOf(this.red), Integer.valueOf(this.green), Integer.valueOf(this.blue)}));
        this.buttonSelector.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sColor = ColorPickerActivity.this.buttonSelector.getText().toString();
                if (sColor.contains("#")) {
                    sColor = sColor.replace("#", BuildConfig.FLAVOR);
                    ColorPickerActivity.this.buttonSelector.setText(sColor);
                }
                if (!ColorPickerActivity.isHex(sColor)) {
                    ColorPickerActivity.this.buttonSelector.setTextColor(ContextCompat.getColor(ColorPickerActivity.this.getApplicationContext(), R.color.red_500));
                    ColorPickerActivity.this.fab.setClickable(false);
                    ColorPickerActivity.this.fab.setRippleColor(ContextCompat.getColor(ColorPickerActivity.this.getApplicationContext(), R.color.grey_500));
                } else if (sColor.length() == 6) {
                    ColorPickerActivity.this.fab.setClickable(true);
                    ColorPickerActivity.this.buttonSelector.setTextColor(ContextCompat.getColor(ColorPickerActivity.this.getApplicationContext(), R.color.green_500));
                    ColorPickerActivity.this.fab.setRippleColor(ContextCompat.getColor(ColorPickerActivity.this.getApplicationContext(), R.color.white_trans_75));
                    int color = Integer.parseInt(sColor, 16);
                    ColorPickerActivity.this.red = (color >> 16) & 255;
                    ColorPickerActivity.this.green = (color >> 8) & 255;
                    ColorPickerActivity.this.blue = (color >> 0) & 255;
                }
                ColorPickerActivity.this.redSeekBar.setProgress(ColorPickerActivity.this.red);
                ColorPickerActivity.this.greenSeekBar.setProgress(ColorPickerActivity.this.green);
                ColorPickerActivity.this.blueSeekBar.setProgress(ColorPickerActivity.this.blue);
            }
        });
    }

    @TargetApi(16)
    public void onWindowFocusChanged(boolean hasFocus) {
        this.thumbRect = this.redSeekBar.getThumb().getBounds();
        this.redToolTip.setX((float) (this.seekBarLeft + this.thumbRect.left));
        if (this.red < 10) {
            this.redToolTip.setText("  " + this.red);
        } else if (this.red < 100) {
            this.redToolTip.setText(" " + this.red);
        } else {
            this.redToolTip.setText(this.red + BuildConfig.FLAVOR);
        }
        this.thumbRect = this.greenSeekBar.getThumb().getBounds();
        this.greenToolTip.setX((float) (this.seekBarLeft + this.thumbRect.left));
        if (this.green < 10) {
            this.greenToolTip.setText("  " + this.green);
        } else if (this.red < 100) {
            this.greenToolTip.setText(" " + this.green);
        } else {
            this.greenToolTip.setText(this.green + BuildConfig.FLAVOR);
        }
        this.thumbRect = this.blueSeekBar.getThumb().getBounds();
        this.blueToolTip.setX((float) (this.seekBarLeft + this.thumbRect.left));
        if (this.blue < 10) {
            this.blueToolTip.setText("  " + this.blue);
        } else if (this.blue < 100) {
            this.blueToolTip.setText(" " + this.blue);
        } else {
            this.blueToolTip.setText(this.blue + BuildConfig.FLAVOR);
        }
    }

    @TargetApi(16)
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.redSeekBar) {
            this.red = progress;
            this.thumbRect = seekBar.getThumb().getBounds();
            this.redToolTip.setX((float) (this.seekBarLeft + this.thumbRect.left));
            if (progress < 10) {
                this.redToolTip.setText("  " + this.red);
            } else if (progress < 100) {
                this.redToolTip.setText(" " + this.red);
            } else {
                this.redToolTip.setText(this.red + BuildConfig.FLAVOR);
            }
        } else if (seekBar.getId() == R.id.greenSeekBar) {
            this.green = progress;
            this.thumbRect = seekBar.getThumb().getBounds();
            this.greenToolTip.setX((float) (seekBar.getPaddingLeft() + this.thumbRect.left));
            if (progress < 10) {
                this.greenToolTip.setText("  " + this.green);
            } else if (progress < 100) {
                this.greenToolTip.setText(" " + this.green);
            } else {
                this.greenToolTip.setText(this.green + BuildConfig.FLAVOR);
            }
        } else if (seekBar.getId() == R.id.blueSeekBar) {
            this.blue = progress;
            this.thumbRect = seekBar.getThumb().getBounds();
            this.blueToolTip.setX((float) (this.seekBarLeft + this.thumbRect.left));
            if (progress < 10) {
                this.blueToolTip.setText("  " + this.blue);
            } else if (progress < 100) {
                this.blueToolTip.setText(" " + this.blue);
            } else {
                this.blueToolTip.setText(this.blue + BuildConfig.FLAVOR);
            }
        }
        this.colorView.setBackgroundColor(Color.rgb(this.red, this.green, this.blue));
        this.color = String.format("%02X%02X%02X", new Object[]{Integer.valueOf(this.red), Integer.valueOf(this.green), Integer.valueOf(this.blue)});
        this.buttonSelector.setText(this.color);
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void colorSelect(View view) {
        savePreferences("color", this.buttonSelector.getText().toString());
        Intent resultIntent = new Intent();
        if (getParent() == null) {
            setResult(-1, resultIntent);
        } else {
            getParent().setResult(-1, resultIntent);
        }
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        Editor editor = getSharedPreferences("COLOR_SETTINGS", 0).edit();
        editor.putInt("RED_COLOR", this.redSeekBar.getProgress());
        editor.putInt("GREEN_COLOR", this.greenSeekBar.getProgress());
        editor.putInt("BLUE_COLOR", this.blueSeekBar.getProgress());
        editor.apply();
        try {
            if (this.alertDialog.isShowing()) {
                this.alertDialog.dismiss();
            }
        } catch (NullPointerException e) {
        }
    }

    private void savePreferences(String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
