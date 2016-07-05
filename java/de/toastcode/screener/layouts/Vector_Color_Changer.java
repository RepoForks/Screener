package de.toastcode.screener.layouts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

public class Vector_Color_Changer {
    public static Bitmap changeColor(Bitmap mBitmap, int color) {
        Paint p = new Paint(color);
        p.setColorFilter(new LightingColorFilter(0, color));
        Bitmap new_mBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Config.ARGB_8888);
        new Canvas(new_mBitmap).drawBitmap(mBitmap, 0.0f, 0.0f, p);
        return new_mBitmap;
    }

    public static Drawable changeColor(Context context, int mDrawable, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, mDrawable));
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    public static Bitmap getMyBitmap(Context context, ImageView iv, int drawable) {
        Drawable d = ContextCompat.getDrawable(context, drawable);
        Bitmap bitmap = Bitmap.createBitmap(iv.getMeasuredWidth(), iv.getMeasuredHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return bitmap;
    }
}
