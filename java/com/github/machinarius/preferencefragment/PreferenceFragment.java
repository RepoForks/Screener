package com.github.machinarius.preferencefragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.support.v4.preferencefragment.R;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ListView;

public abstract class PreferenceFragment extends Fragment implements OnPreferenceTreeClickListener {
    private static final int FIRST_REQUEST_CODE = 100;
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final String PREFERENCES_TAG = "android:preferences";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PreferenceFragment.MSG_BIND_PREFERENCES /*1*/:
                    PreferenceFragment.this.bindPreferences();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mHavePrefs;
    private boolean mInitDone;
    private ListView mList;
    private OnKeyListener mListOnKeyListener = new OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (PreferenceFragment.this.mList.getSelectedItem() instanceof Preference) {
                View selectedView = PreferenceFragment.this.mList.getSelectedView();
            }
            return false;
        }
    };
    private PreferenceManager mPreferenceManager;
    private final Runnable mRequestFocus = new Runnable() {
        public void run() {
            PreferenceFragment.this.mList.focusableViewAvailable(PreferenceFragment.this.mList);
        }
    };

    public interface OnPreferenceStartFragmentCallback {
        boolean onPreferenceStartFragment(PreferenceFragment preferenceFragment, Preference preference);
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.mPreferenceManager = PreferenceManagerCompat.newInstance(getActivity(), FIRST_REQUEST_CODE);
        PreferenceManagerCompat.setFragment(this.mPreferenceManager, this);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.preference_list_fragment, paramViewGroup, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.mHavePrefs) {
            bindPreferences();
        }
        this.mInitDone = true;
        if (savedInstanceState != null) {
            Bundle container = savedInstanceState.getBundle(PREFERENCES_TAG);
            if (container != null) {
                PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(container);
                }
            }
        }
    }

    public void onStart() {
        super.onStart();
        PreferenceManagerCompat.setOnPreferenceTreeClickListener(this.mPreferenceManager, this);
    }

    public void onStop() {
        super.onStop();
        PreferenceManagerCompat.dispatchActivityStop(this.mPreferenceManager);
        PreferenceManagerCompat.setOnPreferenceTreeClickListener(this.mPreferenceManager, null);
    }

    public void onDestroyView() {
        this.mList = null;
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mHandler.removeMessages(MSG_BIND_PREFERENCES);
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        PreferenceManagerCompat.dispatchActivityDestroy(this.mPreferenceManager);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            Bundle container = new Bundle();
            preferenceScreen.saveHierarchyState(container);
            outState.putBundle(PREFERENCES_TAG, container);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PreferenceManagerCompat.dispatchActivityResult(this.mPreferenceManager, requestCode, resultCode, data);
    }

    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        if (PreferenceManagerCompat.setPreferences(this.mPreferenceManager, preferenceScreen) && preferenceScreen != null) {
            this.mHavePrefs = true;
            if (this.mInitDone) {
                postBindPreferences();
            }
        }
    }

    public PreferenceScreen getPreferenceScreen() {
        return PreferenceManagerCompat.getPreferenceScreen(this.mPreferenceManager);
    }

    public void addPreferencesFromIntent(Intent intent) {
        requirePreferenceManager();
        setPreferenceScreen(PreferenceManagerCompat.inflateFromIntent(this.mPreferenceManager, intent, getPreferenceScreen()));
    }

    public void addPreferencesFromResource(int preferencesResId) {
        requirePreferenceManager();
        setPreferenceScreen(PreferenceManagerCompat.inflateFromResource(this.mPreferenceManager, getActivity(), preferencesResId, getPreferenceScreen()));
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (getActivity() instanceof OnPreferenceStartFragmentCallback) {
            return ((OnPreferenceStartFragmentCallback) getActivity()).onPreferenceStartFragment(this, preference);
        }
        return false;
    }

    public Preference findPreference(CharSequence key) {
        if (this.mPreferenceManager == null) {
            return null;
        }
        return this.mPreferenceManager.findPreference(key);
    }

    private void requirePreferenceManager() {
        if (this.mPreferenceManager == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }

    private void postBindPreferences() {
        if (!this.mHandler.hasMessages(MSG_BIND_PREFERENCES)) {
            this.mHandler.obtainMessage(MSG_BIND_PREFERENCES).sendToTarget();
        }
    }

    private void bindPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.bind(getListView());
        }
    }

    public ListView getListView() {
        ensureList();
        return this.mList;
    }

    private void ensureList() {
        if (this.mList == null) {
            View root = getView();
            if (root == null) {
                throw new IllegalStateException("Content view not yet created");
            }
            View rawListView = root.findViewById(16908298);
            if (rawListView instanceof ListView) {
                this.mList = (ListView) rawListView;
                if (this.mList == null) {
                    throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
                }
                this.mList.setOnKeyListener(this.mListOnKeyListener);
                this.mHandler.post(this.mRequestFocus);
                return;
            }
            throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
        }
    }
}
