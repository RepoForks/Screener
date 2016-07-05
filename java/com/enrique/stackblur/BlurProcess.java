package com.enrique.stackblur;

import android.graphics.Bitmap;

interface BlurProcess {
    Bitmap blur(Bitmap bitmap, float f);
}
