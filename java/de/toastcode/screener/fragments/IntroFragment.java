package de.toastcode.screener.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import de.toastcode.screener.MainActivity;
import de.toastcode.screener.activities.Choose_CompanyActivity;
import me.zhanghai.android.materialprogressbar.R;

public class IntroFragment extends Fragment {
    private static final String PAGE = "page";
    private static final String SETTINGS = "settings";
    private FloatingActionButton fab_choose;
    private FloatingActionButton fab_finish;
    private int mPage;
    private int mValue = 0;

    public static IntroFragment newInstance(int page, int value) {
        IntroFragment frag = new IntroFragment();
        Bundle b = new Bundle();
        b.putInt(PAGE, page);
        b.putInt(SETTINGS, value);
        frag.setArguments(b);
        return frag;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(PAGE)) {
            this.mPage = getArguments().getInt(PAGE);
            this.mValue = getArguments().getInt(SETTINGS);
            return;
        }
        throw new RuntimeException("Fragment must contain a \"page\" argument!");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutResId;
        switch (this.mPage) {
            case R.styleable.View_android_theme /*0*/:
                layoutResId = de.toastcode.screener.R.layout.intro1;
                break;
            case R.styleable.View_android_focusable /*1*/:
                layoutResId = de.toastcode.screener.R.layout.intro2;
                break;
            case R.styleable.View_paddingStart /*2*/:
                layoutResId = de.toastcode.screener.R.layout.intro3;
                break;
            case R.styleable.View_paddingEnd /*3*/:
                layoutResId = de.toastcode.screener.R.layout.intro4;
                break;
            default:
                layoutResId = de.toastcode.screener.R.layout.intro5;
                break;
        }
        View view = getActivity().getLayoutInflater().inflate(layoutResId, container, false);
        view.setTag(Integer.valueOf(this.mPage));
        this.fab_choose = (FloatingActionButton) view.findViewById(de.toastcode.screener.R.id.fab_choose);
        this.fab_finish = (FloatingActionButton) view.findViewById(de.toastcode.screener.R.id.fab_done);
        ((TextView) view.findViewById(de.toastcode.screener.R.id.description)).setMovementMethod(LinkMovementMethod.getInstance());
        if (this.fab_choose != null) {
            this.fab_choose.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    IntroFragment.this.startActivity(new Intent(IntroFragment.this.getActivity(), Choose_CompanyActivity.class));
                }
            });
        }
        if (this.fab_finish != null) {
            this.fab_finish.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (IntroFragment.this.mValue == 0) {
                        IntroFragment.this.loadMainActivity();
                    }
                    IntroFragment.this.getActivity().finish();
                }
            });
        }
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadMainActivity() {
        startActivity(new Intent(getContext(), MainActivity.class));
    }
}
