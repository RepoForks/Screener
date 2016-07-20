package de.toastcode.screener;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.LayoutParams;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.crashlytics.android.Crashlytics;
import com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.Drawer.OnDrawerListener;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.squareup.seismic.ShakeDetector;
import com.squareup.seismic.ShakeDetector.Listener;
import de.toastcode.screener.activities.Choose_CompanyActivity;
import de.toastcode.screener.adapters.TabAdapter;
import de.toastcode.screener.fragments.About;
import de.toastcode.screener.fragments.DreiD;
import de.toastcode.screener.fragments.Frame;
import de.toastcode.screener.fragments.Minimals;
import de.toastcode.screener.fragments.Preference_Fragment;
import de.toastcode.screener.fragments.Watches;
import de.toastcode.screener.layouts.ChangelogDialog;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.zhanghai.android.materialprogressbar.BuildConfig;
import me.zhanghai.android.materialprogressbar.R;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

public class MainActivity extends AppCompatActivity implements Listener {
    static FrameLayout content_frame;
    private final String PRIVATE_PREF = "myapp";
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 125;
    private final String VERSION_KEY = "version_number";
    Fragment about = new About();
    AppBarLayout appBarLayout;
    public String curr_time = BuildConfig.FLAVOR;
    Fragment dreid = new DreiD();
    Fragment flat = new Frame();
    private AccountHeader headerResult = null;
    private boolean isEnough = false;
    public String lastUpdate;
    private int lastpos = 0;
    Toolbar mToolbar;
    Fragment mini = new Minimals();
    SwitchPreference preference;
    SharedPreferences prefs;
    private Drawer result = null;
    ShakeDetector sd = new ShakeDetector(this);
    Fragment settings = new Preference_Fragment();
    private int startpos = 2;
    TabLayout tabLayout;
    Vibrator vibration;
    ViewPager viewPager;

    public class DBUpdate_Async extends AsyncTask<String, Void, String> {
        HttpClient httpClient;
        HttpGet httpGet;
        boolean isNotConnect = false;
        HashMap<String, String> map;
        HttpResponse response;

        protected String doInBackground(String... urls) {
            this.httpClient = new DefaultHttpClient();
            this.httpGet = new HttpGet("http://www.toastco.de/screener/database/devices.scr");
            try {
                this.httpGet.addHeader("Cache-Control", "no-cache");
                this.httpGet.addHeader("Cache-Control", "no-store");
                this.response = this.httpClient.execute(this.httpGet);
                Header[] headers = this.response.getAllHeaders();
                this.map = new HashMap();
                this.map = MainActivity.this.convertHeadersToHashMap(headers);
                MainActivity.this.curr_time = (String) this.map.get("Last-Modified");
                this.isNotConnect = false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                this.isNotConnect = true;
            } catch (IOException e2) {
                e2.printStackTrace();
                this.isNotConnect = true;
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            if (!this.isNotConnect) {
                MainActivity.this.loadPreferences("lastUpdate", 0);
                if (!MainActivity.this.curr_time.equals(MainActivity.this.lastUpdate)) {
                    MainActivity.this.showUpdateDialog();
                }
            }
            MainActivity.this.init();
        }
    }

    public class downloadDB_Async extends AsyncTask<String, Void, String> {
        String DB_DIR = "/data/data/de.toastcode.screener/databases/";
        String DB_NAME = "devices.scr";
        MaterialDialog pDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            this.pDialog = new Builder(MainActivity.this).content((int) R.string.database_download_dialog).progress(true, 0).cancelable(false).show();
        }

        protected String doInBackground(String... urls) {
            try {
                InputStream is = new URL("http://www.toastco.de/screener/database/devices.scr").openConnection().getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayBuffer baf = new ByteArrayBuffer(1024);
                while (true) {
                    int current = bis.read();
                    if (current == -1) {
                        break;
                    }
                    baf.append((byte) current);
                }
                OutputStream myOutput = new FileOutputStream(this.DB_DIR + this.DB_NAME);
                myOutput.write(baf.toByteArray());
                myOutput.flush();
                myOutput.close();
                bis.close();
                is.close();
                MainActivity.this.savePreferences("lastUpdate", MainActivity.this.curr_time);
            } catch (Exception e) {
                Log.e("DOWNLOAD", "downloadDatabase Error: ", e);
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            this.pDialog.dismiss();
            MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
            MainActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Kit[]{new Crashlytics()});
        setContentView(R.layout.drawer_main);
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        this.appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (this.viewPager != null) {
            setupViewPager(this.viewPager);
        }
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        SensorManager sensorManager = (SensorManager) getSystemService("sensor");
        this.sd.setSensitivity(15);
        this.sd.start(sensorManager);
        this.vibration = (Vibrator) getSystemService("vibrator");
        this.isEnough = true;
        if (this.isEnough) {
            this.preference = new SwitchPreference(this);
            this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(this.mToolbar);
            if (getResources().getBoolean(R.bool.portrait_only)) {
                setRequestedOrientation(1);
            } else {
                setRequestedOrientation(0);
            }
            this.headerResult = new AccountHeaderBuilder().withActivity(this).withHeaderBackground((int) R.drawable.header).build();
            this.result = new DrawerBuilder().withActivity(this).withActionBarDrawerToggleAnimated(true).withToolbar(this.mToolbar).withAccountHeader(this.headerResult).addDrawerItems(new SectionDrawerItem().withName((int) R.string.submenu), (IDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withName((int) R.string.menu2)).withIcon((IIcon) Icon.gmd_filter_list), (IDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withName((int) R.string.menu3)).withIcon((IIcon) Icon.gmd_filter_list), (IDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withName((int) R.string.menu10)).withIcon((IIcon) Icon.gmd_filter_list), new DividerDrawerItem(), (IDrawerItem) ((PrimaryDrawerItem) new SecondaryDrawerItem().withName((int) R.string.device_manager_drawer)).withIcon((IIcon) Icon.gmd_devices_other), (IDrawerItem) ((PrimaryDrawerItem) new SecondaryDrawerItem().withName((int) R.string.menu5)).withIcon((IIcon) Icon.gmd_settings), (IDrawerItem) ((PrimaryDrawerItem) new SecondaryDrawerItem().withName((int) R.string.menu4)).withIcon((IIcon) Icon.gmd_info_outline), (IDrawerItem) ((PrimaryDrawerItem) new SecondaryDrawerItem().withName((int) R.string.menu9)).withIcon((IIcon) Icon.gmd_thumb_up)).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
                public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                    if (iDrawerItem != null && (iDrawerItem instanceof Nameable)) {
                        if (i == 9 || i == 6) {
                            MainActivity.this.result.setSelectionAtPosition(MainActivity.this.lastpos);
                        } else {
                            MainActivity.this.getSupportActionBar().setTitle(((Nameable) iDrawerItem).getName().getText(MainActivity.this));
                        }
                        MainActivity.this.selectItem(i);
                    }
                    return false;
                }
            }).withOnDrawerListener(new OnDrawerListener() {
                public void onDrawerOpened(View drawerView) {
                    System.out.println("open");
                    MainActivity.this.invalidateOptionsMenu();
                    MainActivity.this.appBarLayout.setExpanded(true, true);
                }

                public void onDrawerClosed(View drawerView) {
                    MainActivity.this.invalidateOptionsMenu();
                    System.out.println("close");
                }

                public void onDrawerSlide(View view, float v) {
                    MainActivity.this.appBarLayout.setExpanded(true, true);
                }
            }).withFireOnInitialOnClick(true).withSavedInstance(savedInstanceState).withSelectedItemByPosition(this.startpos).build();
            if (VERSION.SDK_INT >= 23) {
                permissionWrapper();
                return;
            } else {
                nextStep();
                return;
            }
        }
        new Builder(this).title((int) R.string.attention).content((int) R.string.not_enough_ram).positiveText(17039370).callback(new ButtonCallback() {
            public void onPositive(MaterialDialog dialog) {
                MainActivity.this.finish();
            }
        }).show();
    }

    private void nextStep() {
        if (isDataAvailable()) {
            new DBUpdate_Async().execute(new String[0]);
        } else {
            init();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Frame(), getResources().getString(R.string.menu7));
        adapter.addFragment(new Watches(), getResources().getString(R.string.menu6));
        viewPager.setAdapter(adapter);
    }

    private void showUpdateDialog() {
        new Builder(this).title((int) R.string.update_avaible_title).iconRes(R.drawable.ic_cloud_download_24dp).content((int) R.string.update_avaible).cancelable(false).negativeText((int) R.string.yes).positiveText((int) R.string.no).callback(new ButtonCallback() {
            public void onPositive(MaterialDialog dialog) {
                dialog.cancel();
            }

            public void onNegative(MaterialDialog dialog) {
                new downloadDB_Async().execute(new String[0]);
            }
        }).show();
    }

    private HashMap<String, String> convertHeadersToHashMap(Header[] headers) {
        HashMap<String, String> result = new HashMap(headers.length);
        for (Header header : headers) {
            result.put(header.getName(), header.getValue());
        }
        return result;
    }

    public void loadPreferences(String KEY, int index) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (index == 0) {
            this.lastUpdate = sharedPreferences.getString(KEY, BuildConfig.FLAVOR);
        }
    }

    private void savePreferences(String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(key, value);
        editor.commit();
    }

    private boolean isDataAvailable() {
        ConnectivityManager conxMgr = (ConnectivityManager) getSystemService("connectivity");
        NetworkInfo mobileNwInfo = conxMgr.getNetworkInfo(0);
        NetworkInfo wifiNwInfo = conxMgr.getNetworkInfo(1);
        if (mobileNwInfo != null) {
            if (mobileNwInfo.isConnected()) {
                return true;
            }
        }
        if (wifiNwInfo != null && wifiNwInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private boolean checkIfEnoughRAM() {
        IOException ex;
        RandomAccessFile randomAccessFile;
        Throwable th;
        double totalMemory = 0.0d;
        if (VERSION.SDK_INT >= 16) {
            ActivityManager actManager = (ActivityManager) getSystemService("activity");
            MemoryInfo memInfo = new MemoryInfo();
            actManager.getMemoryInfo(memInfo);
            totalMemory = ((double) memInfo.totalMem) / 1.073741824E9d;
        } else {
            DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
            String lastValue = BuildConfig.FLAVOR;
            try {
                RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
                try {
                    Matcher m = Pattern.compile("(\\d+)").matcher(reader.readLine());
                    String value = BuildConfig.FLAVOR;
                    while (m.find()) {
                        value = m.group(1);
                    }
                    reader.close();
                    totalMemory = Double.parseDouble(value);
                } catch (IOException e) {
                    ex = e;
                    randomAccessFile = reader;
                    try {
                        ex.printStackTrace();
                        if (totalMemory > 1.2d) {
                            return false;
                        }
                        return true;
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    randomAccessFile = reader;
                    throw th;
                }
            } catch (IOException e2) {
                ex = e2;
                ex.printStackTrace();
                if (totalMemory > 1.2d) {
                    return true;
                }
                return false;
            }
        }
        if (totalMemory > 1.2d) {
            return true;
        }
        return false;
    }

    private void checkIfFromStore() {
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            if (!"com.android.vending".equals(packageManager.getInstallerPackageName(applicationInfo.packageName))) {
                new Builder(this).title((int) R.string.attention).content(packageManager.getInstallerPackageName(applicationInfo.packageName)).positiveText(17039370).callback(new ButtonCallback() {
                    public void onPositive(MaterialDialog dialog) {
                        MainActivity.this.finish();
                    }
                }).show();
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        SharedPreferences sharedPref = getSharedPreferences("myapp", 0);
        int currentVersionNumber = 0;
        int savedVersionNumber = sharedPref.getInt("version_number", 0);
        try {
            currentVersionNumber = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
        }
        if (currentVersionNumber > savedVersionNumber) {
            showWhatsNewDialog();
            Editor editor = sharedPref.edit();
            editor.putInt("version_number", currentVersionNumber);
            editor.commit();
        }
    }

    private void selectItem(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case R.styleable.View_paddingStart /*2*/:
                content_frame.setVisibility(8);
                this.viewPager.setVisibility(0);
                setScrollFlagToolbar(true);
                this.tabLayout.setVisibility(0);
                ft.replace(R.id.content_frame, this.flat);
                this.lastpos = position;
                break;
            case R.styleable.View_paddingEnd /*3*/:
                content_frame.setVisibility(0);
                this.viewPager.setVisibility(8);
                setScrollFlagToolbar(false);
                this.tabLayout.setVisibility(8);
                ft.replace(R.id.content_frame, this.dreid);
                this.lastpos = position;
                break;
            case R.styleable.View_theme /*4*/:
                content_frame.setVisibility(0);
                this.viewPager.setVisibility(8);
                setScrollFlagToolbar(false);
                this.tabLayout.setVisibility(8);
                ft.replace(R.id.content_frame, this.mini);
                this.lastpos = position;
                break;
            case R.styleable.Toolbar_contentInsetEnd /*6*/:
                startActivity(new Intent(this, Choose_CompanyActivity.class));
                break;
            case R.styleable.Toolbar_contentInsetLeft /*7*/:
                content_frame.setVisibility(0);
                this.viewPager.setVisibility(8);
                setScrollFlagToolbar(false);
                this.tabLayout.setVisibility(8);
                ft.replace(R.id.content_frame, this.settings);
                this.lastpos = position;
                break;
            case R.styleable.Toolbar_contentInsetRight /*8*/:
                content_frame.setVisibility(0);
                this.viewPager.setVisibility(8);
                setScrollFlagToolbar(false);
                this.tabLayout.setVisibility(8);
                ft.replace(R.id.content_frame, this.about);
                this.lastpos = position;
                break;
            case R.styleable.Toolbar_contentInsetStartWithNavigation /*9*/:
                Intent irate = new Intent("android.intent.action.VIEW");
                irate.setData(Uri.parse("market://details?id=de.toastcode.screener"));
                startActivity(irate);
                break;
        }
        ft.commit();
    }

    private void setScrollFlagToolbar(boolean isActive) {
        if (isActive) {
            ((LayoutParams) this.mToolbar.getLayoutParams()).setScrollFlags(21);
        } else {
            ((LayoutParams) this.mToolbar.getLayoutParams()).setScrollFlags(4);
        }
    }

    private void showWhatsNewDialog() {
        int accentColor = ThemeSingleton.get().widgetColor;
        if (accentColor == 0) {
            accentColor = getResources().getColor(R.color.primary);
        }
        ChangelogDialog.create(false, accentColor).show(getSupportFragmentManager(), "changelog");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(this.result.saveInstanceState(outState));
    }

    public void onBackPressed() {
        if (this.result == null || !this.result.isDrawerOpen()) {
            super.onBackPressed();
        } else {
            this.result.closeDrawer();
        }
    }

    public void onPause() {
        this.sd.stop();
        super.onPause();
    }

    public void onResume() {
        this.sd.start((SensorManager) getSystemService("sensor"));
        this.prefs = getSharedPreferences(BuildConfig.APPLICATION_ID, 0);
        if (this.prefs.getBoolean("firstrun_snack", true)) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.recycler_view), "Shake your phone to send feedback.", 0);
            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
            snackbar.show();
            this.prefs.edit().putBoolean("firstrun_snack", false).apply();
        }
        super.onResume();
    }

    public void maik(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/+MaikNeumann7")));
    }

    public void seb(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/u/0/+SebastianBasti25Rank")));
    }

    public void tim(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.google.com/+TimBremer")));
    }

    public void joy(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/u/0/100134671064037509205")));
    }

    public void fritz(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/u/0/118432170318937164249")));
    }

    public void andre(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/u/0/+AndreZimmermannSolidKakadu")));
    }

    public void lukas(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/u/0/+LukasK%C3%B6nig97")));
    }

    @TargetApi(23)
    private void permissionWrapper() {
        List<String> permissionsNeeded = new ArrayList();
        String perm2 = getResources().getString(R.string.permission2);
        String perm3 = getResources().getString(R.string.permission3);
        List<String> permissionsList = new ArrayList();
        if (!addPermission(permissionsList, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            permissionsNeeded.add(perm2);
        }
        if (!addPermission(permissionsList, "android.permission.READ_EXTERNAL_STORAGE")) {
            permissionsNeeded.add(perm3);
        }
        if (permissionsList.size() <= 0) {
            return;
        }
        if (permissionsNeeded.size() > 0) {
            requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 125);
        } else {
            requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 125);
        }
    }

    @TargetApi(23)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != 0) {
            permissionsList.add(permission);
            if (!shouldShowRequestPermissionRationale(permission)) {
                return false;
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, OnClickListener okListener) {
        new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 125:
                Map<String, Integer> perms = new HashMap();
                perms.put("android.permission.WRITE_EXTERNAL_STORAGE", Integer.valueOf(0));
                perms.put("android.permission.READ_EXTERNAL_STORAGE", Integer.valueOf(0));
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], Integer.valueOf(grantResults[i]));
                }
                if (!(((Integer) perms.get("android.permission.WRITE_EXTERNAL_STORAGE")).intValue() == 0 && ((Integer) perms.get("android.permission.READ_EXTERNAL_STORAGE")).intValue() == 0)) {
                    Toast.makeText(this, "Please grant the permissions, otherwise this app won't work.", 0).show();
                    break;
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
        nextStep();
    }

    public void hearShake() {
        IOException ex;
        RandomAccessFile randomAccessFile;
        String ram;
        Intent emailIntent;
        Throwable th;
        String phone = Build.MANUFACTURER + " " + Build.MODEL;
        String aversion = VERSION.RELEASE + " " + VERSION.CODENAME;
        String sversion = BuildConfig.VERSION_NAME;
        double totalMemory = 0.0d;
        if (VERSION.SDK_INT >= 16) {
            ActivityManager actManager = (ActivityManager) getSystemService("activity");
            MemoryInfo memInfo = new MemoryInfo();
            actManager.getMemoryInfo(memInfo);
            totalMemory = ((double) memInfo.totalMem) / 1.073741824E9d;
        } else {
            try {
                RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
                try {
                    Matcher m = Pattern.compile("(\\d+)").matcher(reader.readLine());
                    String value = BuildConfig.FLAVOR;
                    while (m.find()) {
                        value = m.group(1);
                    }
                    reader.close();
                    totalMemory = Double.parseDouble(value);
                } catch (IOException e) {
                    ex = e;
                    randomAccessFile = reader;
                    try {
                        ex.printStackTrace();
                        ram = Double.toString(totalMemory);
                        this.vibration.vibrate(250);
                        emailIntent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:help@toastco.de"));
                        emailIntent.putExtra("android.intent.extra.SUBJECT", "Screener Feedback");
                        emailIntent.putExtra("android.intent.extra.TEXT", "\n\n__________\nPlease write your notes above this line.\n\nPhone model: " + phone + "\nAndroid Version: " + aversion + "\nRAM: " + ram + "\nScreener Version: " + sversion);
                        startActivity(Intent.createChooser(emailIntent, "Send email"));
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    randomAccessFile = reader;
                    throw th;
                }
            } catch (IOException e2) {
                ex = e2;
                ex.printStackTrace();
                ram = Double.toString(totalMemory);
                this.vibration.vibrate(250);
                emailIntent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:help@toastco.de"));
                emailIntent.putExtra("android.intent.extra.SUBJECT", "Screener Feedback");
                emailIntent.putExtra("android.intent.extra.TEXT", "\n\n__________\nPlease write your notes above this line.\n\nPhone model: " + phone + "\nAndroid Version: " + aversion + "\nRAM: " + ram + "\nScreener Version: " + sversion);
                startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        }
        ram = Double.toString(totalMemory);
        this.vibration.vibrate(250);
        emailIntent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:help@toastco.de"));
        emailIntent.putExtra("android.intent.extra.SUBJECT", "Screener Feedback");
        emailIntent.putExtra("android.intent.extra.TEXT", "\n\n__________\nPlease write your notes above this line.\n\nPhone model: " + phone + "\nAndroid Version: " + aversion + "\nRAM: " + ram + "\nScreener Version: " + sversion);
        startActivity(Intent.createChooser(emailIntent, "Send email"));
    }
}
