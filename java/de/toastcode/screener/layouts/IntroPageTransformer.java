package de.toastcode.screener.layouts;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import me.zhanghai.android.materialprogressbar.R;

public class IntroPageTransformer implements PageTransformer {
    public void transformPage(View page, float position) {
        View title = page.findViewById(R.id.title);
        View description = page.findViewById(de.toastcode.screener.R.id.description);
        View device = page.findViewById(de.toastcode.screener.R.id.device);
        View fab_done = page.findViewById(de.toastcode.screener.R.id.fab_done);
        View fab_choose = page.findViewById(de.toastcode.screener.R.id.fab_choose);
        int pagePosition = ((Integer) page.getTag()).intValue();
        float pageWidthTimesPosition = ((float) page.getWidth()) * position;
        float absPosition = Math.abs(position);
        if (position > -1.0f && position < 1.0f && position != 0.0f) {
            if (position < 0.0f) {
                title.setAlpha(1.0f - absPosition);
                description.setAlpha(1.0f - absPosition);
                device.setAlpha(1.0f - absPosition);
                device.setTranslationY((-pageWidthTimesPosition) / 1.0f);
                if (fab_choose != null) {
                    fab_choose.setAlpha(1.0f - absPosition);
                }
                if (fab_done != null) {
                    fab_done.setAlpha(1.0f - absPosition);
                    return;
                }
                return;
            }
            title.setAlpha(1.0f - absPosition);
            description.setAlpha(1.0f - absPosition);
            device.setAlpha(1.0f - absPosition);
            device.setTranslationY(pageWidthTimesPosition / 1.0f);
            if (fab_choose != null) {
                fab_choose.setAlpha(1.0f - absPosition);
            }
            if (fab_done != null) {
                fab_done.setAlpha(1.0f - absPosition);
            }
        }
    }
}
