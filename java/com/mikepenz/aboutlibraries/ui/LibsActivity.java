package com.mikepenz.aboutlibraries.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.Libs.ActivityStyle;
import com.mikepenz.aboutlibraries.R;
import com.mikepenz.aboutlibraries.util.Colors;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class LibsActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        boolean customTheme = false;
        ActivityStyle activityStyle = ActivityStyle.DARK;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int themeId = bundle.getInt(Libs.BUNDLE_THEME, -1);
            if (themeId != -1) {
                customTheme = true;
                setTheme(themeId);
            }
            String style = bundle.getString(Libs.BUNDLE_STYLE);
            if (style != null) {
                activityStyle = ActivityStyle.valueOf(style);
            }
        }
        if (!customTheme) {
            if (activityStyle == ActivityStyle.DARK) {
                setTheme(R.style.AboutLibrariesTheme);
            } else if (activityStyle == ActivityStyle.LIGHT) {
                setTheme(R.style.AboutLibrariesTheme_Light);
            } else if (activityStyle == ActivityStyle.LIGHT_DARK_TOOLBAR) {
                setTheme(R.style.AboutLibrariesTheme_Light_DarkToolbar);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opensource);
        String title = BuildConfig.FLAVOR;
        if (bundle != null && bundle.containsKey(Libs.BUNDLE_TITLE)) {
            title = bundle.getString(Libs.BUNDLE_TITLE);
        }
        LibsSupportFragment fragment = new LibsSupportFragment();
        fragment.setArguments(bundle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (activityStyle == ActivityStyle.LIGHT_DARK_TOOLBAR) {
            toolbar.setTitleTextColor(-1);
            toolbar.setSubtitleTextColor(-1);
        }
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            if (bundle != null && bundle.containsKey(Libs.BUNDLE_COLORS)) {
                Colors colors = (Colors) bundle.getSerializable(Libs.BUNDLE_COLORS);
                if (colors != null) {
                    ab.setBackgroundDrawable(new ColorDrawable(colors.appBarColor));
                    if (VERSION.SDK_INT >= 21) {
                        getWindow().setStatusBarColor(colors.statusBarColor);
                    }
                } else {
                    ab.setBackgroundDrawable(null);
                }
            }
            ab.setDisplayHomeAsUpEnabled(true);
            if (TextUtils.isEmpty(title)) {
                ab.setDisplayShowTitleEnabled(false);
            } else {
                ab.setDisplayShowTitleEnabled(true);
                ab.setTitle(title);
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return false;
        }
    }
}
