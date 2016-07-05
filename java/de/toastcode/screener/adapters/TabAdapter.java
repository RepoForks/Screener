package de.toastcode.screener.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {
    private final List<String> mFragmentTitles = new ArrayList();
    private final List<Fragment> mFragments = new ArrayList();

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        this.mFragments.add(fragment);
        this.mFragmentTitles.add(title);
    }

    public Fragment getItem(int position) {
        return (Fragment) this.mFragments.get(position);
    }

    public int getCount() {
        return this.mFragments.size();
    }

    public CharSequence getPageTitle(int position) {
        return (CharSequence) this.mFragmentTitles.get(position);
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
