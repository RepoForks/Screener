package de.toastcode.screener.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;
import java.util.Vector;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments = new Vector();

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int arg0) {
        return (Fragment) this.fragments.get(arg0);
    }

    public int getCount() {
        return this.fragments.size();
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
    }
}
