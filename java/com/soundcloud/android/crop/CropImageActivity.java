package com.soundcloud.android.crop;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import com.soundcloud.android.crop.ImageViewTouchBase.Recycler;
import com.soundcloud.android.crop.MonitoredActivity.LifeCycleListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

public class CropImageActivity extends MonitoredActivity {
    private static final int SIZE_DEFAULT = 2048;
    private static final int SIZE_LIMIT = 4096;
    private int aspectX;
    private int aspectY;
    private HighlightView cropView;
    private int exifRotation;
    private final Handler handler = new Handler();
    private CropImageView imageView;
    private boolean isSaving;
    private int maxX;
    private int maxY;
    private RotateBitmap rotateBitmap;
    private int sampleSize;
    private Uri saveUri;
    private Uri sourceUri;

    private class Cropper {
        private Cropper() {
        }

        private void makeDefault() {
            boolean z = false;
            if (CropImageActivity.this.rotateBitmap != null) {
                HighlightView hv = new HighlightView(CropImageActivity.this.imageView);
                int width = CropImageActivity.this.rotateBitmap.getWidth();
                int height = CropImageActivity.this.rotateBitmap.getHeight();
                Rect imageRect = new Rect(0, 0, width, height);
                int cropWidth = (Math.min(width, height) * 4) / 5;
                int cropHeight = cropWidth;
                if (!(CropImageActivity.this.aspectX == 0 || CropImageActivity.this.aspectY == 0)) {
                    if (CropImageActivity.this.aspectX > CropImageActivity.this.aspectY) {
                        cropHeight = (CropImageActivity.this.aspectY * cropWidth) / CropImageActivity.this.aspectX;
                    } else {
                        cropWidth = (CropImageActivity.this.aspectX * cropHeight) / CropImageActivity.this.aspectY;
                    }
                }
                int x = (width - cropWidth) / 2;
                int y = (height - cropHeight) / 2;
                RectF cropRect = new RectF((float) x, (float) y, (float) (x + cropWidth), (float) (y + cropHeight));
                Matrix unrotatedMatrix = CropImageActivity.this.imageView.getUnrotatedMatrix();
                if (!(CropImageActivity.this.aspectX == 0 || CropImageActivity.this.aspectY == 0)) {
                    z = true;
                }
                hv.setup(unrotatedMatrix, imageRect, cropRect, z);
                CropImageActivity.this.imageView.add(hv);
            }
        }

        public void crop() {
            CropImageActivity.this.handler.post(new Runnable() {
                public void run() {
                    Cropper.this.makeDefault();
                    CropImageActivity.this.imageView.invalidate();
                    if (CropImageActivity.this.imageView.highlightViews.size() == 1) {
                        CropImageActivity.this.cropView = (HighlightView) CropImageActivity.this.imageView.highlightViews.get(0);
                        CropImageActivity.this.cropView.setFocus(true);
                    }
                }
            });
        }
    }

    public /* bridge */ /* synthetic */ void addLifeCycleListener(LifeCycleListener lifeCycleListener) {
        super.addLifeCycleListener(lifeCycleListener);
    }

    public /* bridge */ /* synthetic */ void removeLifeCycleListener(LifeCycleListener lifeCycleListener) {
        super.removeLifeCycleListener(lifeCycleListener);
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setupWindowFlags();
        setupViews();
        loadInput();
        if (this.rotateBitmap == null) {
            finish();
        } else {
            startCrop();
        }
    }

    @TargetApi(19)
    private void setupWindowFlags() {
        requestWindowFeature(1);
        if (VERSION.SDK_INT >= 19) {
            getWindow().clearFlags(67108864);
        }
    }

    private void setupViews() {
        setContentView(R.layout.crop__activity_crop);
        this.imageView = (CropImageView) findViewById(R.id.crop_image);
        this.imageView.context = this;
        this.imageView.setRecycler(new Recycler() {
            public void recycle(Bitmap b) {
                b.recycle();
                System.gc();
            }
        });
        findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CropImageActivity.this.setResult(0);
                CropImageActivity.this.finish();
            }
        });
        findViewById(R.id.btn_done).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CropImageActivity.this.onSaveClicked();
            }
        });
    }

    private void loadInput() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.aspectX = extras.getInt(Extra.ASPECT_X);
            this.aspectY = extras.getInt(Extra.ASPECT_Y);
            this.maxX = extras.getInt(Extra.MAX_X);
            this.maxY = extras.getInt(Extra.MAX_Y);
            this.saveUri = (Uri) extras.getParcelable("output");
        }
        this.sourceUri = intent.getData();
        if (this.sourceUri != null) {
            this.exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(this, getContentResolver(), this.sourceUri));
            InputStream inputStream = null;
            try {
                this.sampleSize = calculateBitmapSampleSize(this.sourceUri);
                inputStream = getContentResolver().openInputStream(this.sourceUri);
                Options option = new Options();
                option.inSampleSize = this.sampleSize;
                this.rotateBitmap = new RotateBitmap(BitmapFactory.decodeStream(inputStream, null, option), this.exifRotation);
            } catch (IOException e) {
                Log.e("Error reading image: " + e.getMessage(), e);
                setResultException(e);
            } catch (OutOfMemoryError e2) {
                Log.e("OOM reading image: " + e2.getMessage(), e2);
                setResultException(e2);
            } finally {
                CropUtil.closeSilently(inputStream);
            }
        }
    }

    private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
        InputStream is = null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        try {
            is = getContentResolver().openInputStream(bitmapUri);
            BitmapFactory.decodeStream(is, null, options);
            int maxSize = getMaxImageSize();
            int sampleSize = 1;
            while (true) {
                if (options.outHeight / sampleSize <= maxSize && options.outWidth / sampleSize <= maxSize) {
                    return sampleSize;
                }
                sampleSize <<= 1;
            }
        } finally {
            CropUtil.closeSilently(is);
        }
    }

    private int getMaxImageSize() {
        int textureLimit = getMaxTextureSize();
        if (textureLimit == 0) {
            return SIZE_DEFAULT;
        }
        return Math.min(textureLimit, SIZE_LIMIT);
    }

    private int getMaxTextureSize() {
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(3379, maxSize, 0);
        return maxSize[0];
    }

    private void startCrop() {
        if (!isFinishing()) {
            this.imageView.setImageRotateBitmapResetBase(this.rotateBitmap, true);
            CropUtil.startBackgroundJob(this, null, getResources().getString(R.string.crop__wait), new Runnable() {
                public void run() {
                    final CountDownLatch latch = new CountDownLatch(1);
                    CropImageActivity.this.handler.post(new Runnable() {
                        public void run() {
                            if (CropImageActivity.this.imageView.getScale() == 1.0f) {
                                CropImageActivity.this.imageView.center();
                            }
                            latch.countDown();
                        }
                    });
                    try {
                        latch.await();
                        new Cropper().crop();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, this.handler);
        }
    }

    private void onSaveClicked() {
        if (this.cropView != null && !this.isSaving) {
            this.isSaving = true;
            Rect r = this.cropView.getScaledCropRect((float) this.sampleSize);
            int width = r.width();
            int height = r.height();
            int outWidth = width;
            int outHeight = height;
            if (this.maxX > 0 && this.maxY > 0 && (width > this.maxX || height > this.maxY)) {
                float ratio = ((float) width) / ((float) height);
                if (((float) this.maxX) / ((float) this.maxY) > ratio) {
                    outHeight = this.maxY;
                    outWidth = (int) ((((float) this.maxY) * ratio) + 0.5f);
                } else {
                    outWidth = this.maxX;
                    outHeight = (int) ((((float) this.maxX) / ratio) + 0.5f);
                }
            }
            try {
                Bitmap croppedImage = decodeRegionCrop(r, outWidth, outHeight);
                if (croppedImage != null) {
                    this.imageView.setImageRotateBitmapResetBase(new RotateBitmap(croppedImage, this.exifRotation), true);
                    this.imageView.center();
                    this.imageView.highlightViews.clear();
                }
                saveImage(croppedImage);
            } catch (IllegalArgumentException e) {
                setResultException(e);
                finish();
            }
        }
    }

    private void saveImage(Bitmap croppedImage) {
        if (croppedImage != null) {
            final Bitmap b = croppedImage;
            CropUtil.startBackgroundJob(this, null, getResources().getString(R.string.crop__saving), new Runnable() {
                public void run() {
                    CropImageActivity.this.saveOutput(b);
                }
            }, this.handler);
            return;
        }
        finish();
    }

    private Bitmap decodeRegionCrop(Rect rect, int outWidth, int outHeight) {
        int width;
        int height;
        clearImageView();
        InputStream is = null;
        Bitmap croppedImage = null;
        try {
            Matrix matrix;
            is = getContentResolver().openInputStream(this.sourceUri);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);
            width = decoder.getWidth();
            height = decoder.getHeight();
            if (this.exifRotation != 0) {
                float f;
                float f2;
                matrix = new Matrix();
                matrix.setRotate((float) (-this.exifRotation));
                RectF adjusted = new RectF();
                matrix.mapRect(adjusted, new RectF(rect));
                if (adjusted.left < 0.0f) {
                    f = (float) width;
                } else {
                    f = 0.0f;
                }
                if (adjusted.top < 0.0f) {
                    f2 = (float) height;
                } else {
                    f2 = 0.0f;
                }
                adjusted.offset(f, f2);
                rect = new Rect((int) adjusted.left, (int) adjusted.top, (int) adjusted.right, (int) adjusted.bottom);
            }
            croppedImage = decoder.decodeRegion(rect, new Options());
            if (rect.width() > outWidth || rect.height() > outHeight) {
                matrix = new Matrix();
                matrix.postScale(((float) outWidth) / ((float) rect.width()), ((float) outHeight) / ((float) rect.height()));
                croppedImage = Bitmap.createBitmap(croppedImage, 0, 0, croppedImage.getWidth(), croppedImage.getHeight(), matrix, true);
            }
            CropUtil.closeSilently(is);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rectangle " + rect + " is outside of the image (" + width + "," + height + "," + this.exifRotation + ")", e);
        } catch (IOException e2) {
            try {
                Log.e("Error cropping image: " + e2.getMessage(), e2);
                setResultException(e2);
            } finally {
                CropUtil.closeSilently(is);
            }
        } catch (OutOfMemoryError e3) {
            Log.e("OOM cropping image: " + e3.getMessage(), e3);
            setResultException(e3);
            CropUtil.closeSilently(is);
        }
        return croppedImage;
    }

    private void clearImageView() {
        this.imageView.clear();
        if (this.rotateBitmap != null) {
            this.rotateBitmap.recycle();
        }
        System.gc();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void saveOutput(android.graphics.Bitmap r7) {
        /*
        r6 = this;
        r3 = r6.saveUri;
        if (r3 == 0) goto L_0x0037;
    L_0x0004:
        r2 = 0;
        r3 = r6.getContentResolver();	 Catch:{ IOException -> 0x0046 }
        r4 = r6.saveUri;	 Catch:{ IOException -> 0x0046 }
        r2 = r3.openOutputStream(r4);	 Catch:{ IOException -> 0x0046 }
        if (r2 == 0) goto L_0x0018;
    L_0x0011:
        r3 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ IOException -> 0x0046 }
        r4 = 90;
        r7.compress(r3, r4, r2);	 Catch:{ IOException -> 0x0046 }
    L_0x0018:
        com.soundcloud.android.crop.CropUtil.closeSilently(r2);
    L_0x001b:
        r3 = r6.getContentResolver();
        r4 = r6.sourceUri;
        r3 = com.soundcloud.android.crop.CropUtil.getFromMediaUri(r6, r3, r4);
        r4 = r6.getContentResolver();
        r5 = r6.saveUri;
        r4 = com.soundcloud.android.crop.CropUtil.getFromMediaUri(r6, r4, r5);
        com.soundcloud.android.crop.CropUtil.copyExifRotation(r3, r4);
        r3 = r6.saveUri;
        r6.setResultUri(r3);
    L_0x0037:
        r0 = r7;
        r3 = r6.handler;
        r4 = new com.soundcloud.android.crop.CropImageActivity$6;
        r4.<init>(r0);
        r3.post(r4);
        r6.finish();
        return;
    L_0x0046:
        r1 = move-exception;
        r6.setResultException(r1);	 Catch:{ all -> 0x0066 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0066 }
        r3.<init>();	 Catch:{ all -> 0x0066 }
        r4 = "Cannot open file: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0066 }
        r4 = r6.saveUri;	 Catch:{ all -> 0x0066 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0066 }
        r3 = r3.toString();	 Catch:{ all -> 0x0066 }
        com.soundcloud.android.crop.Log.e(r3, r1);	 Catch:{ all -> 0x0066 }
        com.soundcloud.android.crop.CropUtil.closeSilently(r2);
        goto L_0x001b;
    L_0x0066:
        r3 = move-exception;
        com.soundcloud.android.crop.CropUtil.closeSilently(r2);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.soundcloud.android.crop.CropImageActivity.saveOutput(android.graphics.Bitmap):void");
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.rotateBitmap != null) {
            this.rotateBitmap.recycle();
        }
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean isSaving() {
        return this.isSaving;
    }

    private void setResultUri(Uri uri) {
        setResult(-1, new Intent().putExtra("output", uri));
    }

    private void setResultException(Throwable throwable) {
        setResult(Crop.RESULT_ERROR, new Intent().putExtra(Extra.ERROR, throwable));
    }
}
