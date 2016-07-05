package de.toastcode.screener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import de.toastcode.screener.adapters.IntroAdapter;
import de.toastcode.screener.layouts.InkPageIndicator;
import de.toastcode.screener.layouts.IntroPageTransformer;
import java.io.File;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class LaunchActivity extends AppCompatActivity {
    boolean firstStart = true;
    Intent i;
    private ViewPager mViewPager;
    int value;

    public static void enableImmersiveMode(final View decorView) {
        decorView.setSystemUiVisibility(setSystemUiVisibility());
        decorView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & 4) == 0) {
                    decorView.setSystemUiVisibility(LaunchActivity.setSystemUiVisibility());
                }
            }
        });
    }

    public static int setSystemUiVisibility() {
        return 5894;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_Intro);
        setContentView(R.layout.intro_viewpager);
        this.i = getIntent();
        this.value = this.i.getIntExtra("setting", 0);
        loadPreferences("firstStart", 0);
        nextStep();
        enableImmersiveMode(getWindow().getDecorView());
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(1);
        } else {
            setRequestedOrientation(0);
        }
    }

    private void loadMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void loadPreferences(String KEY, int index) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (index == 0) {
            String name = sharedPreferences.getString(KEY, BuildConfig.FLAVOR);
            if (name.equals(BuildConfig.FLAVOR)) {
                this.firstStart = true;
            } else {
                this.firstStart = Boolean.parseBoolean(name);
            }
        }
    }

    private void savePreferences(String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void addPages() {
        this.mViewPager = (ViewPager) findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager(), this.value));
        this.mViewPager.setPageTransformer(false, new IntroPageTransformer());
        ((InkPageIndicator) findViewById(R.id.ink_pager_indicator)).setViewPager(this.mViewPager);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void clearApplicationData() {
        File appDir = new File(getCacheDir().getParent());
        if (appDir.exists()) {
            for (String s : appDir.list()) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    private void nextStep() {
        if (this.firstStart) {
            savePreferences("firstStart", "false");
            addPages();
            clearApplicationData();
        } else if (this.value == 1) {
            addPages();
        } else {
            loadMainActivity();
        }
    }
}
