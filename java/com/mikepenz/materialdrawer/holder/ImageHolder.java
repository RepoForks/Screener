package com.mikepenz.materialdrawer.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialize.util.UIUtils;
import java.io.FileNotFoundException;

public class ImageHolder extends com.mikepenz.materialize.holder.ImageHolder {
    private IIcon mIIcon;

    public ImageHolder(String url) {
        super(url);
    }

    public ImageHolder(Uri uri) {
        super(uri);
    }

    public ImageHolder(Drawable icon) {
        super(icon);
    }

    public ImageHolder(Bitmap bitmap) {
        super(bitmap);
    }

    public ImageHolder(@DrawableRes int iconRes) {
        super(iconRes);
    }

    public ImageHolder(IIcon iicon) {
        super((Bitmap) null);
        this.mIIcon = iicon;
    }

    public IIcon getIIcon() {
        return this.mIIcon;
    }

    public void setIIcon(IIcon mIIcon) {
        this.mIIcon = mIIcon;
    }

    public boolean applyTo(ImageView imageView, String tag) {
        if (getUri() != null) {
            if (!DrawerImageLoader.getInstance().setImage(imageView, getUri(), tag)) {
                imageView.setImageURI(getUri());
            }
        } else if (getIcon() != null) {
            imageView.setImageDrawable(getIcon());
        } else if (getBitmap() != null) {
            imageView.setImageBitmap(getBitmap());
        } else if (getIconRes() != -1) {
            imageView.setImageResource(getIconRes());
        } else if (this.mIIcon != null) {
            imageView.setImageDrawable(new IconicsDrawable(imageView.getContext(), this.mIIcon).actionBar());
        } else {
            imageView.setImageBitmap(null);
            return false;
        }
        return true;
    }

    public Drawable decideIcon(Context ctx, int iconColor, boolean tint, int paddingDp) {
        Drawable icon = getIcon();
        if (this.mIIcon != null) {
            icon = new IconicsDrawable(ctx, this.mIIcon).color(iconColor).sizeDp(24).paddingDp(paddingDp);
        } else if (getIconRes() != -1) {
            icon = UIUtils.getCompatDrawable(ctx, getIconRes());
        } else if (getUri() != null) {
            try {
                icon = Drawable.createFromStream(ctx.getContentResolver().openInputStream(getUri()), getUri().toString());
            } catch (FileNotFoundException e) {
            }
        }
        if (icon == null || !tint || this.mIIcon != null) {
            return icon;
        }
        icon = icon.mutate();
        icon.setColorFilter(iconColor, Mode.SRC_IN);
        return icon;
    }

    public static Drawable decideIcon(ImageHolder imageHolder, Context ctx, int iconColor, boolean tint, int paddingDp) {
        if (imageHolder == null) {
            return null;
        }
        return imageHolder.decideIcon(ctx, iconColor, tint, paddingDp);
    }

    public static void applyDecidedIconOrSetGone(ImageHolder imageHolder, ImageView imageView, int iconColor, boolean tint, int paddingDp) {
        if (imageHolder != null && imageView != null) {
            Drawable drawable = decideIcon(imageHolder, imageView.getContext(), iconColor, tint, paddingDp);
            if (drawable != null) {
                imageView.setImageDrawable(drawable);
                imageView.setVisibility(0);
            } else if (imageHolder.getBitmap() != null) {
                imageView.setImageBitmap(imageHolder.getBitmap());
                imageView.setVisibility(0);
            } else {
                imageView.setVisibility(8);
            }
        } else if (imageView != null) {
            imageView.setVisibility(8);
        }
    }
}
