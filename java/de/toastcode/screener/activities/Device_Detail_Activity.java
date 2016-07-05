package de.toastcode.screener.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.enrique.stackblur.StackBlurManager;
import com.soundcloud.android.crop.Crop;
import de.sr.library.Crypy;
import de.toastcode.screener.R;
import de.toastcode.screener.colorpicker.ColorPickerDialog;
import de.toastcode.screener.colorpicker.ColorPickerSwatch.OnColorSelectedListener;
import de.toastcode.screener.colorpicker.Utils;
import de.toastcode.screener.colorpicker.Utils.ColorUtils;
import de.toastcode.screener.database.Database_Helper;
import de.toastcode.screener.layouts.Vector_Color_Changer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class Device_Detail_Activity extends AppCompatActivity {
    int[] _2k_yxPosS_normal = new int[]{427, 427};
    Bundle b;
    boolean back;
    Bitmap bg;
    BitmapDrawable bitdraw;
    ImageButton btn_back;
    ImageButton btn_chooser;
    ImageButton btn_forward;
    ImageButton btn_glanz;
    ImageButton btn_schatten;
    Animation cardflip;
    Bitmap copy_bg;
    Crypy crypy = new Crypy();
    Cursor cursor = null;
    SQLiteDatabase db;
    Database_Helper dbh;
    String device;
    Bitmap device_frame;
    Bitmap device_frame_thumb;
    File f;
    boolean fasync;
    Bitmap final_bg;
    boolean glare_sel;
    boolean isBlur;
    boolean isColor;
    boolean isLandscape;
    boolean isReRotate = false;
    boolean isRoundedWatch;
    boolean isWatch;
    boolean is_HG_set;
    ImageView iv_frame;
    float lastX = 0.0f;
    Toolbar mToolbar;
    int[] n4_yxPosS_normal = new int[]{162, 162};
    int[] n4_yxPos_normal = new int[]{50, 50};
    Uri outputUri = null;
    ProgressDialog pDialog;
    int parsed_color = 16777215;
    int pos;
    RelativeLayout rel;
    boolean rotate;
    double rotation;
    String s;
    String saved_color;
    StackBlurManager sbManager;
    int sbheight = 0;
    boolean shadow_sel;
    SeekBar skbar;
    Snackbar snack;
    String sql;
    String tablequery;
    String tempPath;
    int[] thumb_yxPos_normal = new int[]{37, 37};
    Bitmap thumbnail;
    String title;
    boolean trans;
    TextView tv_menu;
    int unscaled_height;
    int unscaled_padding_height;
    int unscaled_padding_width;
    int unscaled_width;
    int uph;
    ViewFlipper viewFlip;
    int[] yxPosS_normal = new int[]{320, 320};
    int[] yxPos_normal = new int[]{50, 65};

    private class Async_Blur extends AsyncTask<String, Void, Bitmap> {
        private Async_Blur() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... params) {
            Device_Detail_Activity.this.onBlur(0, Device_Detail_Activity.this.sbManager);
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            Device_Detail_Activity.this.rel.setBackgroundDrawable(Device_Detail_Activity.this.bitdraw);
        }
    }

    private class Async_Save extends AsyncTask<String, Void, Bitmap> {
        File f1;
        Bitmap glares;
        int mSelectedColorCal0;
        Bitmap no_add;
        Bitmap resultbitmap;
        Bitmap screen;
        Bitmap shadows;

        private Async_Save() {
            this.no_add = null;
            this.mSelectedColorCal0 = Device_Detail_Activity.this.parsed_color;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Device_Detail_Activity.this.pDialog = new ProgressDialog(Device_Detail_Activity.this);
            Device_Detail_Activity.this.pDialog.setMessage(Device_Detail_Activity.this.getResources().getString(R.string.render_save));
            Device_Detail_Activity.this.pDialog.setIndeterminate(false);
            Device_Detail_Activity.this.pDialog.setCancelable(false);
            Device_Detail_Activity.this.pDialog.show();
            Answers.getInstance().logCustom(new CustomEvent("Rendered Frame"));
        }

        protected Bitmap doInBackground(String... params) {
            if (Device_Detail_Activity.this.thumbnail != null) {
                this.screen = Bitmap.createScaledBitmap(Device_Detail_Activity.this.thumbnail, Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else if (Device_Detail_Activity.this.isWatch) {
                this.screen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_screen_watch), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else {
                this.screen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_screen), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            }
            if (Device_Detail_Activity.this.final_bg == null) {
                Device_Detail_Activity.this.final_bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else {
                Device_Detail_Activity.this.final_bg = Bitmap.createScaledBitmap(Device_Detail_Activity.this.final_bg, Device_Detail_Activity.this.device_frame.getWidth(), Device_Detail_Activity.this.device_frame.getHeight(), false);
            }
            if (Device_Detail_Activity.this.glare_sel) {
                Device_Detail_Activity.this.s = Device_Detail_Activity.this.crypy.encrypt(Device_Detail_Activity.this.cursor.getString(4) + ".png");
                this.glares = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + Device_Detail_Activity.this.s).getAbsolutePath());
                this.no_add = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            } else {
                this.glares = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            }
            if (Device_Detail_Activity.this.shadow_sel) {
                Device_Detail_Activity.this.s = Device_Detail_Activity.this.crypy.encrypt(Device_Detail_Activity.this.cursor.getString(5) + ".png");
                this.shadows = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + Device_Detail_Activity.this.s).getAbsolutePath());
                this.no_add = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            } else {
                this.shadows = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            }
            if (Device_Detail_Activity.this.isRoundedWatch) {
                this.screen = Device_Detail_Activity.getRoundedCornerBitmap(this.screen, Device_Detail_Activity.this.title);
            }
            if (Device_Detail_Activity.this.rotate) {
                this.screen = Device_Detail_Activity.this.rotateScreen(this.screen);
                Device_Detail_Activity.this.rotate = true;
            }
            this.resultbitmap = Device_Detail_Activity.this.combineImages(this.glares, this.shadows, this.screen, Device_Detail_Activity.this.device_frame, Device_Detail_Activity.this.final_bg, this.mSelectedColorCal0, false);
            if (Device_Detail_Activity.this.isLandscape) {
                if (Device_Detail_Activity.this.title.equals("Nexus 4")) {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this.n4_yxPosS_normal[0], Device_Detail_Activity.this.n4_yxPosS_normal[1]);
                } else if (Device_Detail_Activity.this.cursor.getString(2).contains("_2k")) {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this._2k_yxPosS_normal[0], Device_Detail_Activity.this._2k_yxPosS_normal[1]);
                } else {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this.yxPosS_normal[0], Device_Detail_Activity.this.yxPosS_normal[1]);
                }
            }
            Device_Detail_Activity.this.saveImage(this.f1, this.resultbitmap, false);
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            Device_Detail_Activity.this.pDialog.dismiss();
            Device_Detail_Activity.this.snack = Snackbar.make(Device_Detail_Activity.this.findViewById(R.id.rel), Device_Detail_Activity.this.getResources().getString(R.string.save), 0);
            ((TextView) Device_Detail_Activity.this.snack.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
            Device_Detail_Activity.this.snack.show();
            new Async_Screen_priv().execute(new String[0]);
        }
    }

    private class Async_Screen extends AsyncTask<String, Void, Bitmap> {
        File f1;
        Bitmap glares;
        int mSelectedColorCal0;
        Bitmap no_add;
        Bitmap resultbitmap;
        Bitmap screen;
        Bitmap shadows;

        private Async_Screen() {
            this.no_add = null;
            this.mSelectedColorCal0 = Device_Detail_Activity.this.parsed_color;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Device_Detail_Activity.this.pDialog = new ProgressDialog(Device_Detail_Activity.this);
            Device_Detail_Activity.this.pDialog.setMessage(Device_Detail_Activity.this.getResources().getString(R.string.render_screen));
            Device_Detail_Activity.this.pDialog.setIndeterminate(false);
            Device_Detail_Activity.this.pDialog.setCancelable(false);
            Device_Detail_Activity.this.pDialog.show();
        }

        protected Bitmap doInBackground(String... params) {
            if (Device_Detail_Activity.this.thumbnail != null) {
                this.screen = Bitmap.createScaledBitmap(Device_Detail_Activity.this.thumbnail, Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else if (Device_Detail_Activity.this.isWatch) {
                this.screen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_screen_watch), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else {
                this.screen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_screen), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            }
            if (Device_Detail_Activity.this.final_bg == null) {
                Device_Detail_Activity.this.final_bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else {
                Device_Detail_Activity.this.final_bg = Bitmap.createScaledBitmap(Device_Detail_Activity.this.final_bg, Device_Detail_Activity.this.device_frame.getWidth(), Device_Detail_Activity.this.device_frame.getHeight(), false);
            }
            if (Device_Detail_Activity.this.glare_sel) {
                Device_Detail_Activity.this.s = Device_Detail_Activity.this.crypy.encrypt(Device_Detail_Activity.this.cursor.getString(4) + ".png");
                this.glares = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + Device_Detail_Activity.this.s).getAbsolutePath());
                this.no_add = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            } else {
                this.glares = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            }
            if (Device_Detail_Activity.this.shadow_sel) {
                Device_Detail_Activity.this.s = Device_Detail_Activity.this.crypy.encrypt(Device_Detail_Activity.this.cursor.getString(5) + ".png");
                this.shadows = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + Device_Detail_Activity.this.s).getAbsolutePath());
                this.no_add = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            } else {
                this.shadows = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            }
            if (Device_Detail_Activity.this.isRoundedWatch) {
                this.screen = Device_Detail_Activity.getRoundedCornerBitmap(this.screen, Device_Detail_Activity.this.title);
            }
            if (Device_Detail_Activity.this.rotate) {
                this.screen = Device_Detail_Activity.this.rotateScreen(this.screen);
                Device_Detail_Activity.this.rotate = true;
            }
            this.resultbitmap = Device_Detail_Activity.this.combineImages(this.glares, this.shadows, this.screen, Device_Detail_Activity.this.device_frame, Device_Detail_Activity.this.final_bg, this.mSelectedColorCal0, true);
            if (Device_Detail_Activity.this.isLandscape) {
                if (Device_Detail_Activity.this.title.equals("Nexus 4")) {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this.n4_yxPosS_normal[0], Device_Detail_Activity.this.n4_yxPosS_normal[1]);
                } else if (Device_Detail_Activity.this.cursor.getString(2).contains("_2k")) {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this._2k_yxPosS_normal[0], Device_Detail_Activity.this._2k_yxPosS_normal[1]);
                } else {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this.yxPosS_normal[0], Device_Detail_Activity.this.yxPosS_normal[1]);
                }
            }
            Device_Detail_Activity.this.saveImage(this.f1, this.resultbitmap, true);
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            Device_Detail_Activity.this.pDialog.dismiss();
            ImageView imgView = (ImageView) Device_Detail_Activity.this.findViewById(me.zhanghai.android.materialprogressbar.R.id.image);
            Device_Detail_Activity.this.btn_chooser.setImageDrawable(new BitmapDrawable(Device_Detail_Activity.this.getResources(), this.no_add));
            Device_Detail_Activity.this.loadPreferences();
            if (!Device_Detail_Activity.this.tempPath.equals(BuildConfig.FLAVOR)) {
                Glide.with(Device_Detail_Activity.this.getApplicationContext()).load(Device_Detail_Activity.this.tempPath).placeholder(imgView.getDrawable()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgView);
                imgView.invalidate();
                this.glares.recycle();
                this.shadows.recycle();
                this.screen = null;
                this.glares = null;
                this.shadows = null;
            }
        }
    }

    private class Async_Screen_priv extends AsyncTask<String, Void, Bitmap> {
        File f1;
        Bitmap glares;
        int mSelectedColorCal0;
        Bitmap no_add;
        Bitmap resultbitmap;
        Bitmap screen;
        Bitmap shadows;

        private Async_Screen_priv() {
            this.no_add = null;
            this.mSelectedColorCal0 = Device_Detail_Activity.this.parsed_color;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... params) {
            if (Device_Detail_Activity.this.thumbnail != null) {
                this.screen = Bitmap.createScaledBitmap(Device_Detail_Activity.this.thumbnail, Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else if (Device_Detail_Activity.this.isWatch) {
                this.screen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_screen_watch), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else {
                this.screen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_screen), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            }
            if (Device_Detail_Activity.this.final_bg == null) {
                Device_Detail_Activity.this.final_bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else {
                Device_Detail_Activity.this.final_bg = Bitmap.createScaledBitmap(Device_Detail_Activity.this.final_bg, Device_Detail_Activity.this.device_frame.getWidth(), Device_Detail_Activity.this.device_frame.getHeight(), false);
            }
            if (Device_Detail_Activity.this.glare_sel) {
                Device_Detail_Activity.this.s = Device_Detail_Activity.this.crypy.encrypt(Device_Detail_Activity.this.cursor.getString(4) + ".png");
                this.glares = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + Device_Detail_Activity.this.s).getAbsolutePath());
                this.no_add = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            } else {
                this.glares = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            }
            if (Device_Detail_Activity.this.shadow_sel) {
                Device_Detail_Activity.this.s = Device_Detail_Activity.this.crypy.encrypt(Device_Detail_Activity.this.cursor.getString(5) + ".png");
                this.shadows = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + Device_Detail_Activity.this.s).getAbsolutePath());
                this.no_add = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            } else {
                this.shadows = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            }
            if (Device_Detail_Activity.this.isRoundedWatch) {
                this.screen = Device_Detail_Activity.getRoundedCornerBitmap(this.screen, Device_Detail_Activity.this.title);
            }
            if (Device_Detail_Activity.this.rotate) {
                this.screen = Device_Detail_Activity.this.rotateScreen(this.screen);
                Device_Detail_Activity.this.rotate = true;
            }
            this.resultbitmap = Device_Detail_Activity.this.combineImages(this.glares, this.shadows, this.screen, Device_Detail_Activity.this.device_frame, Device_Detail_Activity.this.final_bg, this.mSelectedColorCal0, true);
            if (Device_Detail_Activity.this.isLandscape) {
                if (Device_Detail_Activity.this.title.equals("Nexus 4")) {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this.n4_yxPosS_normal[0], Device_Detail_Activity.this.n4_yxPosS_normal[1]);
                } else if (Device_Detail_Activity.this.cursor.getString(2).contains("_2k")) {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this._2k_yxPosS_normal[0], Device_Detail_Activity.this._2k_yxPosS_normal[1]);
                } else {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this.yxPosS_normal[0], Device_Detail_Activity.this.yxPosS_normal[1]);
                }
            }
            Device_Detail_Activity.this.saveImage(this.f1, this.resultbitmap, true);
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            ImageView imgView = (ImageView) Device_Detail_Activity.this.findViewById(me.zhanghai.android.materialprogressbar.R.id.image);
            Device_Detail_Activity.this.btn_chooser.setImageDrawable(new BitmapDrawable(Device_Detail_Activity.this.getResources(), this.no_add));
            Device_Detail_Activity.this.loadPreferences();
            if (!Device_Detail_Activity.this.tempPath.equals(BuildConfig.FLAVOR)) {
                Glide.with(Device_Detail_Activity.this.getApplicationContext()).load(Device_Detail_Activity.this.tempPath).placeholder(imgView.getDrawable()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgView);
                imgView.invalidate();
                this.glares.recycle();
                this.shadows.recycle();
                this.screen = null;
                this.glares = null;
                this.shadows = null;
            }
            if (Device_Detail_Activity.this.isColor) {
                Device_Detail_Activity.this.rel.setBackgroundColor(Device_Detail_Activity.this.parsed_color);
                Device_Detail_Activity.this.final_bg = null;
                Device_Detail_Activity.this.bg = null;
                if (Device_Detail_Activity.this.parsed_color == -1) {
                    Device_Detail_Activity.this.trans = false;
                    Device_Detail_Activity.this.mToolbar.setBackgroundColor(Device_Detail_Activity.this.getResources().getColor(R.color.primary));
                    Device_Detail_Activity.this.trans_statusbar(Device_Detail_Activity.this.getResources().getColor(R.color.primary_dark));
                } else if (Device_Detail_Activity.this.parsed_color == 16777215) {
                    Device_Detail_Activity.this.trans = false;
                    Device_Detail_Activity.this.mToolbar.setBackgroundColor(Device_Detail_Activity.this.getResources().getColor(R.color.primary));
                    Device_Detail_Activity.this.trans_statusbar(Device_Detail_Activity.this.getResources().getColor(R.color.primary_dark));
                } else {
                    Device_Detail_Activity.this.mToolbar.setBackgroundColor(Device_Detail_Activity.this.getResources().getColor(R.color.trans_toolbar));
                    Device_Detail_Activity.this.trans_statusbar(Color.parseColor("#26000000"));
                    Device_Detail_Activity.this.trans = true;
                }
                Device_Detail_Activity.this.isColor = false;
            }
        }
    }

    private class Async_Share extends AsyncTask<String, Void, Bitmap> {
        Bitmap glares;
        int mSelectedColorCal0;
        Bitmap no_add;
        Bitmap resultbitmap;
        Bitmap screen;
        Bitmap shadows;

        private Async_Share() {
            this.no_add = null;
            this.mSelectedColorCal0 = Device_Detail_Activity.this.parsed_color;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Device_Detail_Activity.this.pDialog = new ProgressDialog(Device_Detail_Activity.this);
            Device_Detail_Activity.this.pDialog.setMessage(Device_Detail_Activity.this.getResources().getString(R.string.render_share));
            Device_Detail_Activity.this.pDialog.setIndeterminate(false);
            Device_Detail_Activity.this.pDialog.setCancelable(false);
            Device_Detail_Activity.this.pDialog.show();
            Answers.getInstance().logCustom(new CustomEvent("Rendered Frame"));
        }

        protected Bitmap doInBackground(String... params) {
            if (Device_Detail_Activity.this.thumbnail != null) {
                this.screen = Bitmap.createScaledBitmap(Device_Detail_Activity.this.thumbnail, Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else if (Device_Detail_Activity.this.isWatch) {
                this.screen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_screen_watch), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else {
                this.screen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_screen), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            }
            if (Device_Detail_Activity.this.final_bg == null) {
                Device_Detail_Activity.this.final_bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings), Device_Detail_Activity.this.unscaled_width, Device_Detail_Activity.this.unscaled_height, false);
            } else {
                Device_Detail_Activity.this.final_bg = Bitmap.createScaledBitmap(Device_Detail_Activity.this.final_bg, Device_Detail_Activity.this.device_frame.getWidth(), Device_Detail_Activity.this.device_frame.getHeight(), false);
            }
            if (Device_Detail_Activity.this.glare_sel) {
                Device_Detail_Activity.this.s = Device_Detail_Activity.this.crypy.encrypt(Device_Detail_Activity.this.cursor.getString(4) + ".png");
                this.glares = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + Device_Detail_Activity.this.s).getAbsolutePath());
                this.no_add = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            } else {
                this.glares = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            }
            if (Device_Detail_Activity.this.shadow_sel) {
                Device_Detail_Activity.this.s = Device_Detail_Activity.this.crypy.encrypt(Device_Detail_Activity.this.cursor.getString(5) + ".png");
                this.shadows = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + Device_Detail_Activity.this.s).getAbsolutePath());
                this.no_add = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            } else {
                this.shadows = BitmapFactory.decodeResource(Device_Detail_Activity.this.getResources(), R.drawable.no_settings);
            }
            if (Device_Detail_Activity.this.isRoundedWatch) {
                this.screen = Device_Detail_Activity.getRoundedCornerBitmap(this.screen, Device_Detail_Activity.this.title);
            }
            if (Device_Detail_Activity.this.rotate) {
                this.screen = Device_Detail_Activity.this.rotateScreen(this.screen);
                Device_Detail_Activity.this.rotate = true;
            }
            this.resultbitmap = Device_Detail_Activity.this.combineImages(this.glares, this.shadows, this.screen, Device_Detail_Activity.this.device_frame, Device_Detail_Activity.this.final_bg, this.mSelectedColorCal0, false);
            if (Device_Detail_Activity.this.isLandscape) {
                if (Device_Detail_Activity.this.title.equals("Nexus 4")) {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this.n4_yxPosS_normal[0], Device_Detail_Activity.this.n4_yxPosS_normal[1]);
                } else if (Device_Detail_Activity.this.cursor.getString(2).contains("_2k")) {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this._2k_yxPosS_normal[0], Device_Detail_Activity.this._2k_yxPosS_normal[1]);
                } else {
                    this.resultbitmap = Device_Detail_Activity.this.rotateLandscape(this.resultbitmap, -90.0f, Device_Detail_Activity.this.yxPosS_normal[0], Device_Detail_Activity.this.yxPosS_normal[1]);
                }
            }
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/png");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            this.resultbitmap.compress(CompressFormat.PNG, 100, bytes);
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Screener");
            if (!file.exists()) {
                file.mkdirs();
            }
            String extStorageDirectory = file.toString();
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            String date = dateFormat.format(now);
            String time = simpleDateFormat.format(now);
            Device_Detail_Activity.this.f = new File(extStorageDirectory + File.separator + "screener_" + date + "(" + time + ")" + ".png");
            try {
                Device_Detail_Activity.this.f.createNewFile();
                new FileOutputStream(Device_Detail_Activity.this.f).write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.putExtra("android.intent.extra.STREAM", Uri.parse("file:///sdcard/Screener/screener_" + date + "(" + time + ")" + ".png"));
            Device_Detail_Activity.this.startActivityForResult(Intent.createChooser(intent, Device_Detail_Activity.this.getResources().getString(R.string.share)), 3);
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            Device_Detail_Activity.this.pDialog.dismiss();
            MediaScannerConnection.scanFile(Device_Detail_Activity.this, new String[]{Device_Detail_Activity.this.f.getPath()}, null, new OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
            new Async_Screen_priv().execute(new String[0]);
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, String title) {
        int corners;
        if (title.contains("ZenWatch")) {
            corners = 18;
        } else {
            corners = 320;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = (float) corners;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_detail);
        trans_statusbar(Color.parseColor("#26000000"));
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.btn_glanz = (ImageButton) findViewById(R.id.glanz);
        this.btn_schatten = (ImageButton) findViewById(R.id.schatten);
        this.btn_chooser = (ImageButton) findViewById(R.id.chooser);
        this.btn_forward = (ImageButton) findViewById(R.id.forward);
        this.btn_back = (ImageButton) findViewById(R.id.back);
        this.rel = (RelativeLayout) findViewById(R.id.rel);
        this.iv_frame = (ImageView) findViewById(me.zhanghai.android.materialprogressbar.R.id.image);
        this.viewFlip = (ViewFlipper) findViewById(R.id.vf);
        this.tv_menu = (TextView) findViewById(R.id.site_menu);
        this.skbar = (SeekBar) this.viewFlip.getChildAt(3).findViewById(R.id.seekbar);
        this.b = getIntent().getExtras();
        this.pos = this.b.getInt("position") + 1;
        this.tablequery = this.b.getString("table");
        this.device = this.b.getString("device");
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(1);
        } else {
            setRequestedOrientation(0);
        }
        this.btn_glanz.setImageDrawable(Vector_Color_Changer.changeColor(getApplicationContext(), R.drawable.ic_brightness_6_24dp, ContextCompat.getColor(getApplicationContext(), R.color.btn_grey)));
        this.btn_schatten.setImageDrawable(Vector_Color_Changer.changeColor(getApplicationContext(), R.drawable.ic_flip_to_front_24dp, ContextCompat.getColor(getApplicationContext(), R.color.btn_grey)));
        this.cardflip = AnimationUtils.loadAnimation(this, 17432576);
        this.cardflip.setDuration(1000);
        this.btn_glanz.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Device_Detail_Activity.this.glare_sel) {
                    Device_Detail_Activity.this.btn_glanz.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_brightness_6_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.btn_grey)));
                    Device_Detail_Activity.this.glare_sel = false;
                    new Async_Screen().execute(new String[0]);
                    return;
                }
                Device_Detail_Activity.this.btn_glanz.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_brightness_6_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.primary)));
                Device_Detail_Activity.this.glare_sel = true;
                new Async_Screen().execute(new String[0]);
            }
        });
        this.btn_schatten.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Device_Detail_Activity.this.shadow_sel) {
                    Device_Detail_Activity.this.btn_schatten.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_flip_to_front_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.btn_grey)));
                    Device_Detail_Activity.this.shadow_sel = false;
                    new Async_Screen().execute(new String[0]);
                    return;
                }
                Device_Detail_Activity.this.btn_schatten.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_flip_to_front_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.primary)));
                Device_Detail_Activity.this.shadow_sel = true;
                new Async_Screen().execute(new String[0]);
            }
        });
        this.btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Device_Detail_Activity.this.viewFlip.setInAnimation(Device_Detail_Activity.this, R.anim.right_in);
                Device_Detail_Activity.this.viewFlip.setOutAnimation(Device_Detail_Activity.this, R.anim.right_out);
                if (Device_Detail_Activity.this.isBlur) {
                    Device_Detail_Activity.this.viewFlip.setDisplayedChild(1);
                    Device_Detail_Activity.this.tv_menu.setText(Device_Detail_Activity.this.getResources().getString(R.string.background));
                    Device_Detail_Activity.this.tv_menu.startAnimation(Device_Detail_Activity.this.cardflip);
                    Device_Detail_Activity.this.btn_forward.setClickable(false);
                    Device_Detail_Activity.this.btn_forward.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_chevron_right_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.btn_grey)));
                    Device_Detail_Activity.this.btn_forward.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    Device_Detail_Activity.this.btn_back.setClickable(true);
                    Device_Detail_Activity.this.btn_back.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_chevron_left_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.white)));
                    Device_Detail_Activity.this.btn_back.setBackgroundColor(Device_Detail_Activity.this.getResources().getColor(R.color.primary));
                    Device_Detail_Activity.this.back = true;
                    Device_Detail_Activity.this.isBlur = false;
                    return;
                }
                Device_Detail_Activity.this.viewFlip.setDisplayedChild(0);
                Device_Detail_Activity.this.tv_menu.setText(Device_Detail_Activity.this.getResources().getString(R.string.frame));
                Device_Detail_Activity.this.tv_menu.startAnimation(Device_Detail_Activity.this.cardflip);
                Device_Detail_Activity.this.btn_forward.setClickable(true);
                Device_Detail_Activity.this.btn_forward.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_chevron_right_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.white)));
                Device_Detail_Activity.this.btn_forward.setBackgroundColor(Device_Detail_Activity.this.getResources().getColor(R.color.primary));
                Device_Detail_Activity.this.btn_back.setClickable(false);
                Device_Detail_Activity.this.btn_back.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_chevron_left_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.btn_grey)));
                Device_Detail_Activity.this.btn_back.setBackgroundColor(Color.parseColor("#e7e7e7"));
                Device_Detail_Activity.this.back = false;
                if (Device_Detail_Activity.this.glare_sel) {
                    Device_Detail_Activity.this.btn_glanz.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_brightness_6_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.primary)));
                } else {
                    Device_Detail_Activity.this.btn_glanz.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_brightness_6_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.btn_grey)));
                }
                if (Device_Detail_Activity.this.shadow_sel) {
                    Device_Detail_Activity.this.btn_schatten.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_flip_to_front_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.primary)));
                } else {
                    Device_Detail_Activity.this.btn_schatten.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_flip_to_front_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.btn_grey)));
                }
            }
        });
        this.btn_forward.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Device_Detail_Activity.this.viewFlip.setInAnimation(Device_Detail_Activity.this, R.anim.left_in);
                Device_Detail_Activity.this.viewFlip.setOutAnimation(Device_Detail_Activity.this, R.anim.left_out);
                Device_Detail_Activity.this.viewFlip.setDisplayedChild(1);
                Device_Detail_Activity.this.tv_menu.setText(Device_Detail_Activity.this.getResources().getString(R.string.background));
                Device_Detail_Activity.this.tv_menu.startAnimation(Device_Detail_Activity.this.cardflip);
                Device_Detail_Activity.this.btn_forward.setClickable(false);
                Device_Detail_Activity.this.btn_forward.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_chevron_right_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.btn_grey)));
                Device_Detail_Activity.this.btn_forward.setBackgroundColor(Color.parseColor("#e7e7e7"));
                Device_Detail_Activity.this.btn_back.setClickable(true);
                Device_Detail_Activity.this.btn_back.setImageDrawable(Vector_Color_Changer.changeColor(Device_Detail_Activity.this.getApplicationContext(), R.drawable.ic_chevron_left_24dp, ContextCompat.getColor(Device_Detail_Activity.this.getApplicationContext(), R.color.white)));
                Device_Detail_Activity.this.btn_back.setBackgroundColor(Device_Detail_Activity.this.getResources().getColor(R.color.primary));
                Device_Detail_Activity.this.back = true;
            }
        });
    }

    protected void onStart() {
        super.onStart();
        this.dbh = new Database_Helper(this);
        try {
            this.db = getBaseContext().openOrCreateDatabase("devices.scr", 0, null);
            if (this.tablequery.contains("watches")) {
                this.isWatch = true;
                if (this.device == null) {
                    this.sql = "SELECT _id, name, image, thumb_image, glare, shadow, unscaled_padding_height, unscaled_padding_width, unscaled_height, unscaled_width, imgbtn_height, imgbtn_width, imgbtn_rotation, rmargin, rounded FROM " + this.tablequery + " WHERE _id = " + this.pos;
                } else {
                    this.sql = "SELECT _id, name, image, thumb_image, glare, shadow, unscaled_padding_height, unscaled_padding_width, unscaled_height, unscaled_width, imgbtn_height, imgbtn_width, imgbtn_rotation, rmargin, rounded  FROM " + this.tablequery + " WHERE thumb_image = '" + this.device + "'";
                }
            } else if (this.device == null) {
                this.sql = "SELECT _id, name, image, thumb_image, glare, shadow, unscaled_padding_height, unscaled_padding_width, unscaled_height, unscaled_width, imgbtn_height, imgbtn_width, imgbtn_rotation, rmargin FROM " + this.tablequery + " WHERE _id = " + this.pos;
            } else {
                this.sql = "SELECT _id, name, image, thumb_image, glare, shadow, unscaled_padding_height, unscaled_padding_width, unscaled_height, unscaled_width, imgbtn_height, imgbtn_width, imgbtn_rotation, rmargin FROM " + this.tablequery + " WHERE thumb_image = '" + this.device + "'";
            }
            this.cursor = this.db.rawQuery(this.sql, null);
            startManagingCursor(this.cursor);
            if (this.cursor != null) {
                boolean z;
                if (this.cursor.moveToFirst()) {
                    do {
                    } while (this.cursor.moveToNext());
                }
                this.cursor.moveToFirst();
                this.title = this.cursor.getString(1);
                this.s = this.crypy.encrypt(this.cursor.getString(2) + ".png");
                this.device_frame = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + this.s).getAbsolutePath());
                this.s = this.crypy.encrypt(this.cursor.getString(3) + ".png");
                this.device_frame_thumb = BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + this.s).getAbsolutePath());
                this.iv_frame.setImageBitmap(this.device_frame_thumb);
                this.unscaled_padding_height = Integer.parseInt(this.cursor.getString(6));
                this.unscaled_padding_width = Integer.parseInt(this.cursor.getString(7));
                this.unscaled_height = Integer.parseInt(this.cursor.getString(8));
                this.unscaled_width = Integer.parseInt(this.cursor.getString(9));
                this.rotation = Double.parseDouble(this.cursor.getString(12));
                if (this.isWatch) {
                    this.isRoundedWatch = Boolean.parseBoolean(this.cursor.getString(14));
                }
                if (this.rotation != 0.0d) {
                    z = true;
                } else {
                    z = false;
                }
                this.rotate = z;
            }
            stopManagingCursor(this.cursor);
            if (this.db != null) {
                this.dbh.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.db.close();
            stopManagingCursor(this.cursor);
            if (this.db != null) {
                this.dbh.close();
            }
        } catch (Throwable th) {
            stopManagingCursor(this.cursor);
            if (this.db != null) {
                this.dbh.close();
            }
        }
        if (!this.trans) {
            this.mToolbar.setBackgroundColor(getResources().getColor(R.color.trans_toolbar));
        }
        this.mToolbar.setTitle(this.title);
        this.mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        this.mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        setSupportActionBar(this.mToolbar);
        if (this.back) {
            this.btn_forward.setClickable(false);
            this.btn_forward.setImageDrawable(Vector_Color_Changer.changeColor(getApplicationContext(), R.drawable.ic_chevron_right_24dp, ContextCompat.getColor(getApplicationContext(), R.color.btn_grey)));
            this.btn_forward.setBackgroundColor(Color.parseColor("#e7e7e7"));
            this.btn_back.setClickable(true);
            this.btn_back.setImageDrawable(Vector_Color_Changer.changeColor(getApplicationContext(), R.drawable.ic_chevron_left_24dp, ContextCompat.getColor(getApplicationContext(), R.color.white)));
            this.btn_back.setBackgroundColor(getResources().getColor(R.color.primary));
            return;
        }
        this.btn_forward.setClickable(true);
        this.btn_forward.setImageDrawable(Vector_Color_Changer.changeColor(getApplicationContext(), R.drawable.ic_chevron_right_24dp, ContextCompat.getColor(getApplicationContext(), R.color.white)));
        this.btn_back.setClickable(false);
        this.btn_back.setImageDrawable(Vector_Color_Changer.changeColor(getApplicationContext(), R.drawable.ic_chevron_left_24dp, ContextCompat.getColor(getApplicationContext(), R.color.btn_grey)));
        this.btn_back.setBackgroundColor(Color.parseColor("#e7e7e7"));
    }

    protected void onResume() {
        super.onResume();
        new Async_Screen_priv().execute(new String[0]);
    }

    public void onClick(View vw) {
        int i = 1;
        int id = vw.getId();
        if (id == R.id.custom_color) {
            int[] parsed_color_arr = new int[]{0};
            int[] mColor = ColorUtils.colorChoice(getBaseContext());
            this.is_HG_set = false;
            int i2 = parsed_color_arr[0];
            if (!Utils.isTablet(getBaseContext())) {
                i = 2;
            }
            final ColorPickerDialog colorcalendar = ColorPickerDialog.newInstance(R.string.color_picker_default_title, mColor, i2, 4, i);
            colorcalendar.setOnColorSelectedListener(new OnColorSelectedListener() {
                public void onColorSelected(int color) {
                    Device_Detail_Activity.this.isColor = true;
                    if (color == -1118482) {
                        colorcalendar.dismiss();
                        Device_Detail_Activity.this.startActivityForResult(new Intent(Device_Detail_Activity.this.getApplicationContext(), ColorPickerActivity.class), 4);
                        return;
                    }
                    Device_Detail_Activity.this.savePreferences("color", String.format("%06X", new Object[]{Integer.valueOf(16777215 & color)}));
                    Device_Detail_Activity.this.getApplicationContext().getSharedPreferences("mcpicker", 0).getString("color", null);
                    Device_Detail_Activity.this.parsed_color = color;
                    colorcalendar.dismiss();
                    new Async_Screen_priv().execute(new String[0]);
                }
            });
            colorcalendar.show(getFragmentManager(), "cal");
        } else if (id == R.id.custom_bg) {
            showCustombg();
        } else if (id == R.id.blur) {
            this.sbManager = new StackBlurManager(this.final_bg);
            if (this.final_bg == null || this.bg == null || !this.is_HG_set) {
                new Builder(this).title(getResources().getString(R.string.attention)).content(getResources().getString(R.string.choose_bgscreen)).positiveText((CharSequence) "OK").show();
                return;
            }
            this.isBlur = true;
            this.viewFlip.startAnimation(this.cardflip);
            this.viewFlip.setDisplayedChild(3);
            this.skbar.setMax(25);
            if (this.bg == null) {
                this.bg = BitmapFactory.decodeResource(getResources(), R.drawable.no_settings);
            }
            this.skbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                    System.out.println("onStopTrackingTouch");
                    new Async_Blur().execute(new String[0]);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    System.out.println("onStartTrackingTouch");
                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    System.out.println("onProgressChanged");
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 16908332) {
            finish();
            overridePendingTransition(R.anim.nichts, R.anim.push_down_out);
            return true;
        }
        if (id == R.id.action_save) {
            if (this.thumbnail == null) {
                new Builder(this).title(getResources().getString(R.string.attention)).content(getResources().getString(R.string.choose_screen)).positiveText((CharSequence) "OK").show();
            } else {
                new Async_Save().execute(new String[0]);
            }
        } else if (id == R.id.action_share) {
            if (this.thumbnail == null) {
                new Builder(this).title(getResources().getString(R.string.attention)).content(getResources().getString(R.string.choose_screen)).positiveText((CharSequence) "OK").show();
            } else {
                new Async_Share().execute(new String[0]);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void choose_screen(View v) {
        if (v.getId() == R.id.chooser) {
            Intent intent = new Intent();
            if (VERSION.SDK_INT >= 19) {
                intent.setAction("android.intent.action.OPEN_DOCUMENT");
            } else {
                intent.setAction("android.intent.action.GET_CONTENT");
            }
            intent.addCategory("android.intent.category.OPENABLE");
            intent.setType("image/*");
            try {
                startActivityForResult(intent, 1);
            } catch (ActivityNotFoundException e) {
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = BuildConfig.FLAVOR;
        if (resultCode == -1) {
            if (requestCode == 1) {
                if (data != null) {
                    Uri uri = data.getData();
                    this.thumbnail = null;
                    try {
                        this.thumbnail = Media.getBitmap(getContentResolver(), uri);
                        this.isLandscape = getOrientation(this.thumbnail);
                        if (this.isLandscape) {
                            if (this.tablequery.equals("watches")) {
                                this.isLandscape = false;
                            } else if (this.tablequery.equals("dreid")) {
                                this.isLandscape = false;
                                this.thumbnail = rotateLandscape(this.thumbnail, 90.0f, this.thumb_yxPos_normal[0], this.thumb_yxPos_normal[1]);
                            } else {
                                this.thumbnail = rotateLandscape(this.thumbnail, 90.0f, this.thumb_yxPos_normal[0], this.thumb_yxPos_normal[1]);
                            }
                        }
                        if (this.isReRotate && this.is_HG_set) {
                            Toast.makeText(getApplicationContext(), R.string.choose_bgscreen_please, 1).show();
                            showCustombg();
                            this.isReRotate = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == 2) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                Uri inputUri = null;
                this.skbar.setProgress(0);
                File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Screener" + File.separator + ".temp");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                String extStorageDirectory = folder.toString();
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String date = dateFormat.format(now);
                String filepath = extStorageDirectory + File.separator + "temp_" + date + simpleDateFormat.format(now) + ".png";
                if (data != null) {
                    inputUri = data.getData();
                    this.outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
                }
                LayoutParams parms;
                if (this.isLandscape) {
                    Crop.of(inputUri, this.outputUri).withAspect(metrics.heightPixels, metrics.widthPixels).start(this);
                    parms = (LayoutParams) this.rel.getLayoutParams();
                    parms.height = 720;
                    this.rel.setLayoutParams(parms);
                } else {
                    Crop.of(inputUri, this.outputUri).withAspect(metrics.widthPixels, metrics.heightPixels).start(this);
                    parms = (LayoutParams) this.rel.getLayoutParams();
                    parms.height = -1;
                    this.rel.setLayoutParams(parms);
                }
            } else if (requestCode == 3) {
                System.out.println("Teilen");
            } else if (requestCode == 4) {
                loadColorSavedPreferences();
                if (this.saved_color.equals(BuildConfig.FLAVOR)) {
                    this.saved_color = "00ffffff";
                }
                this.parsed_color = Color.parseColor("#" + this.saved_color);
                this.rel.setBackgroundColor(this.parsed_color);
                this.final_bg = null;
                this.bg = null;
                if (this.parsed_color == -1) {
                    this.trans = false;
                    this.mToolbar.setBackgroundColor(getResources().getColor(R.color.primary));
                    trans_statusbar(getResources().getColor(R.color.primary_dark));
                } else if (this.parsed_color == 16777215) {
                    this.trans = false;
                    this.mToolbar.setBackgroundColor(getResources().getColor(R.color.primary));
                    trans_statusbar(getResources().getColor(R.color.primary_dark));
                } else {
                    this.mToolbar.setBackgroundColor(getResources().getColor(R.color.trans_toolbar));
                    trans_statusbar(Color.parseColor("#26000000"));
                    this.trans = true;
                }
            } else if (requestCode == 6709 && resultCode == -1) {
                this.bg = null;
                this.final_bg = null;
                this.parsed_color = 0;
                try {
                    this.bg = Media.getBitmap(getContentResolver(), this.outputUri);
                    this.final_bg = Media.getBitmap(getContentResolver(), this.outputUri);
                    this.copy_bg = this.bg;
                    if (this.isLandscape) {
                        this.final_bg = rotateLandscape(this.final_bg, 90.0f, 0, 0);
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                Drawable d = new BitmapDrawable(getResources(), this.bg);
                if (VERSION.SDK_INT < 16) {
                    this.rel.setBackgroundDrawable(d);
                } else {
                    this.rel.setBackground(d);
                }
                this.mToolbar.setBackgroundColor(getResources().getColor(R.color.trans_toolbar));
                this.trans = true;
            }
        } else if (resultCode == 0) {
        }
        new Async_Screen().execute(new String[0]);
    }

    public Bitmap rotateLandscape(Bitmap bitmap, float rotation, int xPos, int yPos) {
        Bitmap mutableBitmap = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.save();
        canvas.rotate(rotation, (float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2));
        canvas.drawBitmap(bitmap, (float) ((bitmap.getWidth() / 5) + xPos), (float) ((bitmap.getWidth() / 5) + yPos), null);
        canvas.restore();
        return mutableBitmap;
    }

    public Bitmap combineImages(Bitmap glare, Bitmap shadow, Bitmap screenshot, Bitmap frames, Bitmap bg, int mSelectedColorCal0, boolean isPriv) {
        Bitmap cs = Bitmap.createBitmap(frames.getWidth(), frames.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(cs);
        canvas.drawColor(mSelectedColorCal0);
        if (!isPriv) {
            canvas.drawBitmap(bg, 0.0f, 0.0f, null);
        }
        canvas.drawBitmap(shadow, 0.0f, 0.0f, null);
        canvas.drawBitmap(frames, 0.0f, 0.0f, null);
        if (this.rotation != 0.0d) {
            canvas.drawBitmap(screenshot, (float) this.unscaled_padding_width, (float) this.uph, null);
        } else {
            canvas.drawBitmap(screenshot, (float) this.unscaled_padding_width, (float) this.unscaled_padding_height, null);
        }
        canvas.drawBitmap(glare, 0.0f, 0.0f, null);
        return cs;
    }

    public Bitmap rotateScreen(Bitmap screenshot) {
        float[] dst2;
        Bitmap bitmap2 = Bitmap.createBitmap(screenshot.getWidth(), screenshot.getHeight(), screenshot.getConfig());
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawColor(0, Mode.CLEAR);
        float deform_untenL;
        if (this.title.contains("Nexus 4")) {
            deform_untenL = ((float) bitmap2.getHeight()) - 290.0f;
            this.uph = this.unscaled_padding_height - ((int) 1125449728);
            dst2 = new float[]{0.0f, 0.0f + 149.0f, ((float) bitmap2.getWidth()) - 220.0f, 0.0f, ((float) bitmap2.getWidth()) - 220.0f, ((float) bitmap2.getHeight()) - 147.0f, 0.0f, deform_untenL, ((float) bitmap2.getHeight()) - 149.0f};
        } else {
            deform_untenL = ((float) bitmap2.getHeight()) - 155.0f;
            this.uph = this.unscaled_padding_height - ((int) 1125122048);
            dst2 = new float[]{0.0f, 0.0f + 144.0f, ((float) bitmap2.getWidth()) - 260.0f, 0.0f, ((float) bitmap2.getWidth()) - 260.0f, ((float) bitmap2.getHeight()) - 17.0f, 0.0f, deform_untenL, ((float) bitmap2.getHeight()) - 144.0f};
        }
        Matrix matrix2 = new Matrix();
        float[] src2 = new float[]{0.0f, 0.0f, (float) bitmap2.getWidth(), 0.0f, (float) bitmap2.getWidth(), (float) bitmap2.getHeight(), 0.0f, (float) bitmap2.getHeight()};
        matrix2.setPolyToPoly(src2, 0, dst2, 0, src2.length >> 1);
        canvas.drawBitmap(screenshot, matrix2, new Paint());
        return bitmap2;
    }

    public boolean getOrientation(Bitmap image) {
        boolean isLand;
        if (image.getWidth() > image.getHeight()) {
            isLand = true;
            if (true != this.isLandscape) {
                this.isReRotate = true;
            }
        } else {
            isLand = false;
            LayoutParams parms = (LayoutParams) this.rel.getLayoutParams();
            parms.height = -1;
            this.rel.setLayoutParams(parms);
            if (false != this.isLandscape) {
                this.isReRotate = true;
            }
        }
        return isLand;
    }

    @TargetApi(21)
    private void trans_statusbar(int color) {
        getStatusBarHeight();
        if (VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(1280);
            getWindow().setStatusBarColor(color);
            Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
            LayoutParams params = (LayoutParams) tb.getLayoutParams();
            params.setMargins(0, this.sbheight, 0, 0);
            tb.setLayoutParams(params);
        }
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            this.sbheight = getResources().getDimensionPixelSize(resourceId);
        }
        return this.sbheight;
    }

    private void showCustombg() {
        Intent intent = new Intent();
        if (VERSION.SDK_INT >= 19) {
            intent.setAction("android.intent.action.OPEN_DOCUMENT");
        } else {
            intent.setAction("android.intent.action.GET_CONTENT");
        }
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("image/*");
        this.is_HG_set = true;
        try {
            startActivityForResult(intent, 2);
        } catch (ActivityNotFoundException e) {
        }
    }

    public void loadColorSavedPreferences() {
        this.saved_color = PreferenceManager.getDefaultSharedPreferences(this).getString("color", BuildConfig.FLAVOR);
    }

    public void loadPreferences() {
        this.tempPath = PreferenceManager.getDefaultSharedPreferences(this).getString("TEMP_PATH", BuildConfig.FLAVOR);
    }

    private void savePreferences(String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void onBlur(int blurMode, StackBlurManager sbManager) {
        if (blurMode == 0) {
            int radius = this.skbar.getProgress() * 1;
            if (radius == 0) {
                this.final_bg = this.copy_bg;
                this.bg = this.copy_bg;
            }
            this.final_bg = sbManager.process(radius);
            this.bg = sbManager.process(radius);
            this.bitdraw = new BitmapDrawable(this.bg);
        }
    }

    public void saveImage(File f2, Bitmap bm, boolean temp) {
        File folder;
        String fName;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, bytes);
        if (temp) {
            folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Screener" + File.separator + "temp");
        } else {
            folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Screener");
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String extStorageDirectory = folder.toString();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(now);
        String time = timeFormat.format(now);
        if (temp) {
            fName = "screener.temp";
        } else {
            fName = "screener_" + date + "(" + time + ")" + ".png";
        }
        String filepath = extStorageDirectory + File.separator + fName;
        File file = new File(filepath);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            new FileOutputStream(file).write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (temp) {
            savePreferences("TEMP_PATH", filepath);
        } else {
            savePreferences("TEMP_PATH", BuildConfig.FLAVOR);
        }
        MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, null, new OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });
    }
}
