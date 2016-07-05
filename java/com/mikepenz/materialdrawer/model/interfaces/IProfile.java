package com.mikepenz.materialdrawer.model.interfaces;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import com.mikepenz.fastadapter.IIdentifyable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;

public interface IProfile<T> extends IIdentifyable<T> {
    StringHolder getEmail();

    ImageHolder getIcon();

    StringHolder getName();

    boolean isSelectable();

    T withEmail(String str);

    T withIcon(@DrawableRes int i);

    T withIcon(Bitmap bitmap);

    T withIcon(Drawable drawable);

    T withIcon(Uri uri);

    T withIcon(IIcon iIcon);

    T withIcon(String str);

    T withName(String str);

    T withSelectable(boolean z);
}
