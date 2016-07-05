package com.soundcloud.android.crop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Crop {
    public static final int REQUEST_CROP = 6709;
    public static final int REQUEST_PICK = 9162;
    public static final int RESULT_ERROR = 404;
    private Intent cropIntent = new Intent();

    interface Extra {
        public static final String ASPECT_X = "aspect_x";
        public static final String ASPECT_Y = "aspect_y";
        public static final String ERROR = "error";
        public static final String MAX_X = "max_x";
        public static final String MAX_Y = "max_y";
    }

    public static Crop of(Uri source, Uri destination) {
        return new Crop(source, destination);
    }

    private Crop(Uri source, Uri destination) {
        this.cropIntent.setData(source);
        this.cropIntent.putExtra("output", destination);
    }

    public Crop withAspect(int x, int y) {
        this.cropIntent.putExtra(Extra.ASPECT_X, x);
        this.cropIntent.putExtra(Extra.ASPECT_Y, y);
        return this;
    }

    public Crop asSquare() {
        this.cropIntent.putExtra(Extra.ASPECT_X, 1);
        this.cropIntent.putExtra(Extra.ASPECT_Y, 1);
        return this;
    }

    public Crop withMaxSize(int width, int height) {
        this.cropIntent.putExtra(Extra.MAX_X, width);
        this.cropIntent.putExtra(Extra.MAX_Y, height);
        return this;
    }

    public void start(Activity activity) {
        start(activity, (int) REQUEST_CROP);
    }

    public void start(Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    public void start(Context context, Fragment fragment) {
        start(context, fragment, (int) REQUEST_CROP);
    }

    public void start(Context context, android.support.v4.app.Fragment fragment) {
        start(context, fragment, (int) REQUEST_CROP);
    }

    @TargetApi(11)
    public void start(Context context, Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    public void start(Context context, android.support.v4.app.Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    public Intent getIntent(Context context) {
        this.cropIntent.setClass(context, CropImageActivity.class);
        return this.cropIntent;
    }

    public static Uri getOutput(Intent result) {
        return (Uri) result.getParcelableExtra("output");
    }

    public static Throwable getError(Intent result) {
        return (Throwable) result.getSerializableExtra(Extra.ERROR);
    }

    public static void pickImage(Activity activity) {
        pickImage(activity, (int) REQUEST_PICK);
    }

    public static void pickImage(Context context, Fragment fragment) {
        pickImage(context, fragment, (int) REQUEST_PICK);
    }

    public static void pickImage(Context context, android.support.v4.app.Fragment fragment) {
        pickImage(context, fragment, (int) REQUEST_PICK);
    }

    public static void pickImage(Activity activity, int requestCode) {
        try {
            activity.startActivityForResult(getImagePicker(), requestCode);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(activity);
        }
    }

    @TargetApi(11)
    public static void pickImage(Context context, Fragment fragment, int requestCode) {
        try {
            fragment.startActivityForResult(getImagePicker(), requestCode);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(context);
        }
    }

    public static void pickImage(Context context, android.support.v4.app.Fragment fragment, int requestCode) {
        try {
            fragment.startActivityForResult(getImagePicker(), requestCode);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(context);
        }
    }

    private static Intent getImagePicker() {
        return new Intent("android.intent.action.GET_CONTENT").setType("image/*");
    }

    private static void showImagePickerError(Context context) {
        Toast.makeText(context, R.string.crop__pick_error, 0).show();
    }
}
