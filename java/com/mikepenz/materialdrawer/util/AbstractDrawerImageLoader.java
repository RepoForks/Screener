package com.mikepenz.materialdrawer.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import com.mikepenz.materialdrawer.util.DrawerImageLoader.IDrawerImageLoader;

public abstract class AbstractDrawerImageLoader implements IDrawerImageLoader {
    public void set(ImageView imageView, Uri uri, Drawable placeholder) {
        Log.i("MaterialDrawer", "you have not specified a ImageLoader implementation through the DrawerImageLoader.init(IDrawerImageLoader) method");
    }

    public void cancel(ImageView imageView) {
    }

    public Drawable placeholder(Context ctx) {
        return DrawerUIUtils.getPlaceHolder(ctx);
    }

    public Drawable placeholder(Context ctx, String tag) {
        return placeholder(ctx);
    }
}
