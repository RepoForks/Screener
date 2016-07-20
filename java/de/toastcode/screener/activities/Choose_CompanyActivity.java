package de.toastcode.screener.activities;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import de.sr.library.Crypy;
import de.toastcode.screener.Fetch_DeviceList;
import de.toastcode.screener.ObjectSerializer;
import de.toastcode.screener.R;
import de.toastcode.screener.adapters.GridChooseRecyclerAdapter;
import de.toastcode.screener.database.Database_Helper;
import de.toastcode.screener.layouts.Company;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.zhanghai.android.materialprogressbar.BuildConfig;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

public class Choose_CompanyActivity extends AppCompatActivity {
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Cursor c;
    public String curr_time;
    Database_Helper dbh;
    FloatingActionButton delete_fab;
    FloatingActionButton download_fab;
    boolean isSelected = false;
    public String lastUpdate;
    ArrayList<Company> list_company;
    ArrayList<String> list_sel_devices = new ArrayList();
    Adapter mAdapter;
    LayoutManager mLayoutManager;
    int mNotifCount = 0;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    SQLiteDatabase myDB;
    Snackbar snack;
    public String sql;
    String tablequery = "companies";

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
                this.map = Choose_CompanyActivity.this.convertHeadersToHashMap(headers);
                Choose_CompanyActivity.this.curr_time = (String) this.map.get("Last-Modified");
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
            if (this.isNotConnect) {
                Choose_CompanyActivity.this.setGrid();
                return;
            }
            Choose_CompanyActivity.this.loadPreferences("lastUpdate", 0);
            if (Choose_CompanyActivity.this.curr_time.equals(Choose_CompanyActivity.this.lastUpdate)) {
                Choose_CompanyActivity.this.setGrid();
            } else {
                new downloadDB_Async().execute(new String[0]);
            }
        }
    }

    public class downloadDB_Async extends AsyncTask<String, Void, String> {
        String DB_DIR = "/data/data/de.toastcode.screener/databases/";
        String DB_NAME = "devices.scr";
        MaterialDialog pDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            this.pDialog = new Builder(Choose_CompanyActivity.this).content((int) R.string.database_download_dialog).progress(true, 0).cancelable(false).show();
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
                Choose_CompanyActivity.this.savePreferences("lastUpdate", Choose_CompanyActivity.this.curr_time);
            } catch (Exception e) {
                Log.e("DOWNLOAD", "downloadDatabase Error: ", e);
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            this.pDialog.dismiss();
            Choose_CompanyActivity.this.setGrid();
        }
    }

    public class selectAll_Async extends AsyncTask<String, Void, String> {
        int count = 0;
        boolean isNull = false;
        MaterialDialog pDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            this.pDialog = new Builder(Choose_CompanyActivity.this).content(Choose_CompanyActivity.this.getResources().getString(R.string.selectAll)).progress(true, 0).cancelable(false).show();
        }

        protected String doInBackground(String... urls) {
            try {
                JSONObject json = Fetch_DeviceList.getJSON(Choose_CompanyActivity.this.getApplicationContext());
                JSONArray arr = json.getJSONArray("devices");
                for (int i = 0; i < arr.length(); i++) {
                    if (!new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + new Crypy().encrypt(arr.getString(i) + ".png")).exists()) {
                        Choose_CompanyActivity.this.list_sel_devices.add(arr.getString(i));
                    }
                    this.count++;
                }
                System.out.println("Anzahl der verf\u00fcgbaren Bilder: " + this.count);
                System.out.println("Anzahl der ausgew\u00e4hlten Bilder: " + Choose_CompanyActivity.this.list_sel_devices.size());
                if (json == null) {
                    this.isNull = true;
                } else {
                    this.isNull = false;
                }
            } catch (Exception e) {
                Log.e("DOWNLOAD", "downloadDatabase Error: ", e);
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            this.pDialog.dismiss();
            if (this.isNull) {
                Toast.makeText(Choose_CompanyActivity.this.getApplicationContext(), Choose_CompanyActivity.this.getResources().getString(R.string.enable_network), 0).show();
                return;
            }
            Choose_CompanyActivity.this.setNotifCount(Choose_CompanyActivity.this.sumFromDB());
            Choose_CompanyActivity.this.snack = Snackbar.make(Choose_CompanyActivity.this.findViewById(R.id.main_recycler), Choose_CompanyActivity.this.getResources().getString(R.string.all_selected), 0);
            ((TextView) Choose_CompanyActivity.this.snack.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
            Choose_CompanyActivity.this.snack.show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_download);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(1);
        } else {
            setRequestedOrientation(0);
        }
        this.delete_fab = (FloatingActionButton) findViewById(R.id.fab_delete);
        this.delete_fab.setVisibility(8);
        this.download_fab = (FloatingActionButton) findViewById(R.id.fab_download);
        this.download_fab.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Choose_CompanyActivity.this.loadPreferences("selected", 1);
                if (!Choose_CompanyActivity.this.isDataAvailable()) {
                    return;
                }
                if (Choose_CompanyActivity.this.list_sel_devices.isEmpty()) {
                    Choose_CompanyActivity.this.snack = Snackbar.make(Choose_CompanyActivity.this.findViewById(R.id.main_recycler), Choose_CompanyActivity.this.getResources().getString(R.string.please_choose_device), 0);
                    ((TextView) Choose_CompanyActivity.this.snack.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
                    Choose_CompanyActivity.this.snack.show();
                    return;
                }
                Intent in = new Intent(Choose_CompanyActivity.this, Download_Activity.class);
                in.putStringArrayListExtra("devices", Choose_CompanyActivity.this.list_sel_devices);
                Choose_CompanyActivity.this.startActivity(in);
                Choose_CompanyActivity.this.savePreferences("count", "0");
                Choose_CompanyActivity.this.list_sel_devices.clear();
                Choose_CompanyActivity.this.savePreferencesArr("selected", Choose_CompanyActivity.this.list_sel_devices);
                Choose_CompanyActivity.this.finish();
            }
        });
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(this, 2);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setTitle(R.string.choose_device);
        this.mToolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        this.mToolbar.setNavigationIcon(R.drawable.ic_close_24dp);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primarydark));
        }
    }

    public void onStart() {
        super.onStart();
        if (VERSION.SDK_INT >= 23) {
            permissionWrapper();
        } else if (isDataAvailable()) {
            new DBUpdate_Async().execute(new String[0]);
        } else {
            setGrid();
            this.snack = Snackbar.make(findViewById(R.id.main_recycler), getResources().getString(R.string.enable_network), 0);
            ((TextView) this.snack.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
            this.snack.show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_download, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mItem = menu.findItem(R.id.badge);
        MenuItem mItem2 = menu.findItem(R.id.action_select);
        if (this.isSelected) {
            mItem2.setIcon(R.drawable.ic_deselect_all_24px);
        } else {
            mItem2.setIcon(R.drawable.ic_select_all_24dp);
        }
        if (this.mNotifCount == 0) {
            mItem.setVisible(false);
        } else {
            mItem.setVisible(true);
            MenuItemCompat.setActionView(mItem, R.layout.update_count);
            View count = MenuItemCompat.getActionView(mItem);
            System.out.print("wait");
            ((Button) count.findViewById(R.id.notif_count)).setText(String.valueOf(this.mNotifCount));
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 16908332) {
            savePreferences("count", "0");
            loadPreferences("selected", 1);
            this.list_sel_devices.clear();
            savePreferencesArr("selected", this.list_sel_devices);
            finish();
            overridePendingTransition(R.anim.nichts, R.anim.push_down_out);
            return true;
        }
        if (id == R.id.action_select) {
            if (this.isSelected) {
                this.list_sel_devices.clear();
                item.setIcon(R.drawable.ic_select_all_24dp);
                this.isSelected = false;
                setNotifCount(0);
            } else {
                selectAll();
                item.setIcon(R.drawable.ic_deselect_all_24px);
                this.isSelected = true;
            }
        } else if (id == R.id.badge) {
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        loadPreferences("count", 2);
        setNotifCount(this.mNotifCount);
        this.dbh = new Database_Helper(getBaseContext());
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("devices.scr", 0, null);
        this.dbh.onOpen(db);
        db.close();
        this.dbh.close();
    }

    public void onBackPressed() {
        savePreferences("count", "0");
        loadPreferences("selected", 1);
        this.list_sel_devices.clear();
        savePreferencesArr("selected", this.list_sel_devices);
        super.onBackPressed();
    }

    public void setGrid() {
        this.mAdapter = null;
        this.sql = "SELECT _id, company_name, company_device_url FROM '" + this.tablequery + "' ORDER BY company_name";
        selectFromDB();
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    public void selectAll() {
        new selectAll_Async().execute(new String[0]);
    }

    public int sumFromDB() {
        int sum = 0;
        String query = "SELECT sum(device_count) FROM companies";
        this.dbh = new Database_Helper(this);
        this.dbh.initializeDataBase();
        this.myDB = getBaseContext().openOrCreateDatabase("devices.scr", 0, null);
        this.dbh.onOpen(this.myDB);
        try {
            this.c = this.myDB.rawQuery(query, null);
            startManagingCursor(this.c);
            if (this.c != null) {
                if (this.c.moveToFirst()) {
                    do {
                        sum = this.c.getInt(0);
                    } while (this.c.moveToNext());
                }
                this.c.moveToFirst();
            }
            if (this.myDB != null) {
                this.dbh.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.myDB.close();
            this.dbh.close();
            if (this.myDB != null) {
                this.dbh.close();
            }
        } catch (Throwable th) {
            if (this.myDB != null) {
                this.dbh.close();
            }
        }
        return sum;
    }

    public void selectFromDB() {
        this.dbh = new Database_Helper(this);
        this.dbh.initializeDataBase();
        this.myDB = getBaseContext().openOrCreateDatabase("devices.scr", 0, null);
        this.list_company = new ArrayList();
        this.dbh.onOpen(this.myDB);
        try {
            this.c = this.myDB.rawQuery(this.sql, null);
            startManagingCursor(this.c);
            if (this.c != null) {
                if (this.c.moveToFirst()) {
                    do {
                        this.list_company.add(new Company(this.c.getString(1), this.c.getString(2)));
                    } while (this.c.moveToNext());
                }
                this.c.moveToFirst();
            }
            if (this.myDB != null) {
                this.dbh.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.myDB.close();
            this.dbh.close();
            if (this.myDB != null) {
                this.dbh.close();
            }
        } catch (Throwable th) {
            if (this.myDB != null) {
                this.dbh.close();
            }
        }
        this.mAdapter = new GridChooseRecyclerAdapter(this, this.list_company, this.tablequery, 0);
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
            String name = sharedPreferences.getString(KEY, BuildConfig.FLAVOR);
            if (name != null) {
                this.lastUpdate = name;
            } else {
                this.lastUpdate = BuildConfig.FLAVOR;
            }
        } else if (index == 1) {
            try {
                if (this.list_sel_devices.isEmpty()) {
                    this.list_sel_devices = (ArrayList) ObjectSerializer.deserialize(sharedPreferences.getString(KEY, ObjectSerializer.serialize(new ArrayList())));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (index == 2) {
            this.mNotifCount = Integer.parseInt(sharedPreferences.getString("count", "0"));
        }
    }

    private void savePreferences(String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void savePreferencesArr(String key, ArrayList<String> value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        try {
            editor.putString(key, ObjectSerializer.serialize(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @TargetApi(23)
    private void permissionWrapper() {
        List<String> permissionsNeeded = new ArrayList();
        String perm2 = getResources().getString(R.string.permission2);
        String perm3 = getResources().getString(R.string.permission3);
        final List<String> permissionsList = new ArrayList();
        if (!addPermission(permissionsList, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            permissionsNeeded.add(perm2);
        }
        if (!addPermission(permissionsList, "android.permission.READ_EXTERNAL_STORAGE")) {
            permissionsNeeded.add(perm3);
        }
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                showMessageOKCancel("You need to grant access to " + (permissionsNeeded.size() - 1) + " Permissions", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Choose_CompanyActivity.this.requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 124);
                    }
                });
            } else {
                requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 124);
            }
        } else if (isDataAvailable()) {
            new DBUpdate_Async().execute(new String[0]);
        } else {
            setGrid();
            this.snack = Snackbar.make(findViewById(R.id.main_recycler), getResources().getString(R.string.enable_network), 0);
            ((TextView) this.snack.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
            this.snack.show();
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

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 124:
                Map<String, Integer> perms = new HashMap();
                perms.put("android.permission.WRITE_EXTERNAL_STORAGE", Integer.valueOf(0));
                perms.put("android.permission.READ_EXTERNAL_STORAGE", Integer.valueOf(0));
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], Integer.valueOf(grantResults[i]));
                }
                if (!(((Integer) perms.get("android.permission.WRITE_EXTERNAL_STORAGE")).intValue() == 0 && ((Integer) perms.get("android.permission.READ_EXTERNAL_STORAGE")).intValue() == 0)) {
                    Toast.makeText(this, "Some Permission are Denied", 0).show();
                    break;
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
        if (isDataAvailable()) {
            new DBUpdate_Async().execute(new String[0]);
            return;
        }
        setGrid();
        this.snack = Snackbar.make(findViewById(R.id.main_recycler), getResources().getString(R.string.enable_network), 0);
        ((TextView) this.snack.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
        this.snack.show();
    }

    private void setNotifCount(int count) {
        this.mNotifCount = count;
        invalidateOptionsMenu();
    }
}
