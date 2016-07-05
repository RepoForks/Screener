package com.mikepenz.materialize.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;
import com.mikepenz.materialize.drawable.PressedEffectStateListDrawable;
import com.mikepenz.materialize.util.UIUtils;
import java.io.FileNotFoundException;

public class ImageHolder {
    private Bitmap mBitmap;
    private Drawable mIcon;
    private int mIconRes = -1;
    private Uri mUri;

    public ImageHolder(String url) {
        this.mUri = Uri.parse(url);
    }

    public ImageHolder(Uri uri) {
        this.mUri = uri;
    }

    public ImageHolder(Drawable icon) {
        this.mIcon = icon;
    }

    public ImageHolder(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public ImageHolder(@DrawableRes int iconRes) {
        this.mIconRes = iconRes;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public void setIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public int getIconRes() {
        return this.mIconRes;
    }

    public void setIconRes(int mIconRes) {
        this.mIconRes = mIconRes;
    }

    public boolean applyTo(ImageView imageView) {
        return applyTo(imageView, null);
    }

    public boolean applyTo(ImageView imageView, String tag) {
        if (this.mUri != null) {
            imageView.setImageURI(this.mUri);
        } else if (this.mIcon != null) {
            imageView.setImageDrawable(this.mIcon);
        } else if (this.mBitmap != null) {
            imageView.setImageBitmap(this.mBitmap);
        } else if (this.mIconRes != -1) {
            imageView.setImageResource(this.mIconRes);
        } else {
            imageView.setImageBitmap(null);
            return false;
        }
        return true;
    }

    public Drawable decideIcon(Context ctx, int iconColor, boolean tint) {
        Drawable icon = this.mIcon;
        if (this.mIconRes != -1) {
            icon = UIUtils.getCompatDrawable(ctx, this.mIconRes);
        } else if (this.mUri != null) {
            try {
                icon = Drawable.createFromStream(ctx.getContentResolver().openInputStream(this.mUri), this.mUri.toString());
            } catch (FileNotFoundException e) {
            }
        }
        if (icon == null || !tint) {
            return icon;
        }
        icon = icon.mutate();
        icon.setColorFilter(iconColor, Mode.SRC_IN);
        return icon;
    }

    public static boolean applyTo(ImageHolder imageHolder, ImageView imageView) {
        return applyTo(imageHolder, imageView, null);
    }

    public static boolean applyTo(ImageHolder imageHolder, ImageView imageView, String tag) {
        if (imageHolder == null || imageView == null) {
            return false;
        }
        return imageHolder.applyTo(imageView, tag);
    }

    public static void applyToOrSetInvisible(ImageHolder imageHolder, ImageView imageView) {
        applyToOrSetInvisible(imageHolder, imageView, null);
    }

    public static void applyToOrSetInvisible(ImageHolder imageHolder, ImageView imageView, String tag) {
        boolean imageSet = applyTo(imageHolder, imageView, tag);
        if (imageView == null) {
            return;
        }
        if (imageSet) {
            imageView.setVisibility(0);
        } else {
            imageView.setVisibility(4);
        }
    }

    public static void applyToOrSetGone(ImageHolder imageHolder, ImageView imageView) {
        applyToOrSetGone(imageHolder, imageView, null);
    }

    public static void applyToOrSetGone(ImageHolder imageHolder, ImageView imageView, String tag) {
        boolean imageSet = applyTo(imageHolder, imageView, tag);
        if (imageView == null) {
            return;
        }
        if (imageSet) {
            imageView.setVisibility(0);
        } else {
            imageView.setVisibility(8);
        }
    }

    public static Drawable decideIcon(ImageHolder imageHolder, Context ctx, int iconColor, boolean tint) {
        if (imageHolder == null) {
            return null;
        }
        return imageHolder.decideIcon(ctx, iconColor, tint);
    }

    public static void applyDecidedIconOrSetGone(ImageHolder imageHolder, ImageView imageView, int iconColor, boolean tint) {
        if (imageHolder != null && imageView != null) {
            Drawable drawable = decideIcon(imageHolder, imageView.getContext(), iconColor, tint);
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

    public static void applyMultiIconTo(Drawable icon, int iconColor, Drawable selectedIcon, int selectedIconColor, boolean tinted, ImageView imageView) {
        if (icon != null) {
            if (selectedIcon != null) {
                if (tinted) {
                    imageView.setImageDrawable(new PressedEffectStateListDrawable(icon, selectedIcon, iconColor, selectedIconColor));
                } else {
                    imageView.setImageDrawable(UIUtils.getIconStateList(icon, selectedIcon));
                }
            } else if (tinted) {
                imageView.setImageDrawable(new PressedEffectStateListDrawable(icon, iconColor, selectedIconColor));
            } else {
                imageView.setImageDrawable(icon);
            }
            imageView.setVisibility(0);
            return;
        }
        imageView.setVisibility(8);
    }
}
