package de.toastcode.screener.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import com.mikepenz.aboutlibraries.LibsBuilder;
import de.toastcode.screener.BuildConfig;
import de.toastcode.screener.R;
import de.toastcode.screener.R.string;

public class License_Activity extends AppCompatActivity {
    Toolbar mToolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license);
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.mToolbar);
        this.mToolbar.setClickable(true);
        this.mToolbar.setTitle(R.string.pref_license);
        this.mToolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                License_Activity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String versionName = BuildConfig.VERSION_NAME;
        getSupportFragmentManager().beginTransaction().replace(R.id.license_container, new LibsBuilder().withFields(string.class.getFields()).withAboutIconShown(true).withAboutAppName("Screener").withAboutDescription("Version " + versionName + " / " + Integer.toString(15)).withVersionShown(true).supportFragment()).commit();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
