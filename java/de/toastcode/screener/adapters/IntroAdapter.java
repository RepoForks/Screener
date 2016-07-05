package de.toastcode.screener.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import de.toastcode.screener.fragments.IntroFragment;
import me.zhanghai.android.materialprogressbar.R;

public class IntroAdapter extends FragmentPagerAdapter {
    int mValue = 0;

    public IntroAdapter(FragmentManager fm, int value) {
        super(fm);
        this.mValue = value;
    }

    public Fragment getItem(int position) {
        switch (position) {
            case R.styleable.View_android_theme /*0*/:
                return IntroFragment.newInstance(position, this.mValue);
            case R.styleable.View_android_focusable /*1*/:
                return IntroFragment.newInstance(position, this.mValue);
            case R.styleable.View_paddingStart /*2*/:
                return IntroFragment.newInstance(position, this.mValue);
            case R.styleable.View_paddingEnd /*3*/:
                return IntroFragment.newInstance(position, this.mValue);
            default:
                return IntroFragment.newInstance(position, this.mValue);
        }
    }

    public int getCount() {
        return 5;
    }
}
