package de.toastcode.screener.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import de.toastcode.screener.LaunchActivity;
import de.toastcode.screener.R;
import de.toastcode.screener.activities.License_Activity;

public class Preference_Fragment extends PreferenceFragment {
    int choice = 1;
    boolean iEnable;
    boolean schduledRestart;
    Snackbar snack;
    SharedPreferences sp;

    public void onResume() {
        super.onResume();
        if (this.schduledRestart) {
            this.schduledRestart = false;
            Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
            i.addFlags(67108864);
            startActivity(i);
            ListView lv = null;
            lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                }
            });
        }
        setHasOptionsMenu(true);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        this.sp = getPreferenceManager().getSharedPreferences();
        Preference license = findPreference("licenseKey");
        Preference intro = findPreference("introKey");
        license.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Preference_Fragment.this.startActivity(new Intent(Preference_Fragment.this.getActivity(), License_Activity.class));
                return true;
            }
        });
        intro.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent iintro = new Intent(Preference_Fragment.this.getActivity(), LaunchActivity.class);
                iintro.putExtra("setting", 1);
                Preference_Fragment.this.startActivity(iintro);
                return true;
            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
