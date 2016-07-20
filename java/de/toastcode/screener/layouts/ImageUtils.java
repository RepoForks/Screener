package de.toastcode.screener.layouts;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {
    public static ImageUtils mInstant;

    public static ImageUtils getInstant() {
        if (mInstant == null) {
            mInstant = new ImageUtils();
        }
        return mInstant;
    }

    public Bitmap getCompressedBitmap(String imagePath) {
        IOException e;
        OutputStream out;
        byte[] byteArray;
        Bitmap scaledBitmap = null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = ((float) actualWidth) / ((float) actualHeight);
        float maxRatio = 1080.0f / 1920.0f;
        if (((float) actualHeight) > 1920.0f || ((float) actualWidth) > 1080.0f) {
            if (imgRatio < maxRatio) {
                actualWidth = (int) (((float) actualWidth) * (1920.0f / ((float) actualHeight)));
                actualHeight = (int) 1920.0f;
            } else if (imgRatio > maxRatio) {
                actualHeight = (int) (((float) actualHeight) * (1080.0f / ((float) actualWidth)));
                actualWidth = (int) 1080.0f;
            } else {
                actualHeight = (int) 1920.0f;
                actualWidth = (int) 1080.0f;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16384];
        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888);
        } catch (OutOfMemoryError exception2) {
            exception2.printStackTrace();
        }
        float ratioX = ((float) actualWidth) / ((float) options.outWidth);
        float ratioY = ((float) actualHeight) / ((float) options.outHeight);
        float middleX = ((float) actualWidth) / 2.0f;
        float middleY = ((float) actualHeight) / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - ((float) (bmp.getWidth() / 2)), middleY - ((float) (bmp.getHeight() / 2)), new Paint(2));
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            ExifInterface exifInterface2;
            try {
                int orientation = exifInterface.getAttributeInt("Orientation", 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90.0f);
                } else if (orientation == 3) {
                    matrix.postRotate(180.0f);
                } else if (orientation == 8) {
                    matrix.postRotate(270.0f);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                exifInterface2 = exifInterface;
            } catch (IOException e2) {
                e = e2;
                exifInterface2 = exifInterface;
                e.printStackTrace();
                out = new ByteArrayOutputStream();
                scaledBitmap.compress(CompressFormat.JPEG, 85, out);
                byteArray = out.toByteArray();
                return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            out = new ByteArrayOutputStream();
            scaledBitmap.compress(CompressFormat.JPEG, 85, out);
            byteArray = out.toByteArray();
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        out = new ByteArrayOutputStream();
        scaledBitmap.compress(CompressFormat.JPEG, 85, out);
        byteArray = out.toByteArray();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    private int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(((float) height) / ((float) reqHeight));
            int widthRatio = Math.round(((float) width) / ((float) reqWidth));
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio;
            } else {
                inSampleSize = widthRatio;
            }
        }
        while (((float) (width * height)) / ((float) (inSampleSize * inSampleSize)) > ((float) ((reqWidth * reqHeight) * 2))) {
            inSampleSize++;
        }
        return inSampleSize;
    }
}
