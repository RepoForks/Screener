package de.toastcode.screener.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import de.toastcode.screener.R;
import de.toastcode.screener.adapters.PagerAdapter;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import java.util.List;
import java.util.Vector;

public class About extends Fragment {
    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.viewpager, container, false);
        initialisePaging();
        setHasOptionsMenu(true);
        return this.rootView;
    }

    private void initialisePaging() {
        List<Fragment> fragments = new Vector();
        fragments.add(Fragment.instantiate(getActivity(), Fragment_team.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), Fragment_maik.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), Fragment_seb.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), Fragment_tim.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), Fragment_joy.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), Fragment_fritz.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), Fragment_andre.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), Fragment_lukas.class.getName()));
        ((VerticalViewPager) this.rootView.findViewById(R.id.verticalviewpager)).setAdapter(new PagerAdapter(getChildFragmentManager(), fragments));
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
