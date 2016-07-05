package com.mikepenz.aboutlibraries;

import android.annotation.TargetApi;
import android.os.Bundle;
import com.mikepenz.aboutlibraries.ui.LibsFragment;

@TargetApi(11)
public class LibsCompat {
    @TargetApi(11)
    public static LibsFragment fragment(LibsBuilder libsBuilder) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", libsBuilder);
        LibsFragment fragment = new LibsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
