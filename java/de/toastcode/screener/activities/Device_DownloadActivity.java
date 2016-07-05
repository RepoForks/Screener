package de.toastcode.screener.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import de.sr.library.Crypy;
import de.toastcode.screener.ObjectSerializer;
import de.toastcode.screener.R;
import de.toastcode.screener.adapters.GridChooseRecyclerAdapter;
import de.toastcode.screener.database.Database_Helper;
import de.toastcode.screener.layouts.Devices;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedMap;

public class Device_DownloadActivity extends AppCompatActivity {
    Bundle b;
    Cursor c;
    String company;
    int count = 0;
    Database_Helper dbh;
    FloatingActionButton delete_fab;
    boolean deviceToDelete = false;
    FloatingActionButton download_fab;
    String lastDevice = null;
    ArrayList<Devices> listOfDevices;
    ArrayList<String> list_del_devices = new ArrayList();
    Adapter mAdapter;
    Context mContext;
    LayoutManager mLayoutManager;
    private SortedMap<String, ArrayList<Devices>> mListOfDevices;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    SQLiteDatabase myDB;
    Snackbar snack;
    String sql;

    public void selectFromDB(java.lang.String r18, int r19) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextEntry(HashMap.java:854)
	at java.util.HashMap$KeyIterator.next(HashMap.java:885)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:286)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:173)
*/
        /*
        r17 = this;
        r1 = new de.toastcode.screener.database.Database_Helper;
        r0 = r17;
        r1.<init>(r0);
        r0 = r17;
        r0.dbh = r1;
        r0 = r17;
        r1 = r0.dbh;
        r1.initializeDataBase();
        r1 = r17.getBaseContext();
        r2 = "devices.scr";
        r3 = 0;
        r5 = 0;
        r1 = r1.openOrCreateDatabase(r2, r3, r5);
        r0 = r17;
        r0.myDB = r1;
        if (r19 != 0) goto L_0x0036;
    L_0x0024:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r0 = r17;
        r0.listOfDevices = r1;
        r1 = new java.util.TreeMap;
        r1.<init>();
        r0 = r17;
        r0.mListOfDevices = r1;
    L_0x0036:
        r4 = 0;
        r0 = r17;
        r1 = r0.dbh;
        r0 = r17;
        r2 = r0.myDB;
        r1.onOpen(r2);
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.myDB;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = 0;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r18;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.rawQuery(r0, r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0.c = r1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0.startManagingCursor(r1);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        if (r1 == 0) goto L_0x0162;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x0060:
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.moveToFirst();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        if (r1 == 0) goto L_0x015b;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x006a:
        r9 = new de.sr.library.Crypy;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r9.<init>();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.<init>();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = 2;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r2.getString(r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = ".png";	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r13 = r9.encrypt(r1);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.<init>();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r2.getPath();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = "/Android/data/de.toastcode.screener/";	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.append(r13);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r12 = r1.toString();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r11 = new java.io.File;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r11.exists();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        if (r1 == 0) goto L_0x0170;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x00b9:
        r4 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x00ba:
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.getString(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r0.lastDevice;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.equals(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        if (r1 != 0) goto L_0x00d6;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x00cd:
        r1 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.<init>();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0.listOfDevices = r1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x00d6:
        if (r19 != 0) goto L_0x0209;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x00d8:
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.company;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = "No Device";	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.equals(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        if (r1 == 0) goto L_0x0173;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x00e4:
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r15 = r0.listOfDevices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = new de.toastcode.screener.layouts.Devices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r2.getString(r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = r17.getResources();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = "no_device";	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = "drawable";	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = r17.getPackageName();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = r3.getIdentifier(r5, r6, r7);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = 2;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = r5.getString(r6);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = 3;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r8 = 4;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = r7.getString(r8);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r8 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r16 = 5;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r16;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r8 = r8.getString(r0);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r15.add(r1);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.mListOfDevices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r2.getString(r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = r0.listOfDevices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.put(r2, r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.getString(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0.lastDevice = r1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x0151:
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.moveToNext();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        if (r1 != 0) goto L_0x006a;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x015b:
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.moveToFirst();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
    L_0x0162:
        r0 = r17;
        r1 = r0.myDB;
        if (r1 == 0) goto L_0x016f;
    L_0x0168:
        r0 = r17;
        r1 = r0.dbh;
        r1.close();
    L_0x016f:
        return;
    L_0x0170:
        r4 = 0;
        goto L_0x00ba;
    L_0x0173:
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r15 = r0.listOfDevices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = new de.toastcode.screener.layouts.Devices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r2.getString(r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = r17.getResources();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = r0.company;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = r5.toLowerCase();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = "drawable";	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = r17.getPackageName();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = r3.getIdentifier(r5, r6, r7);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = 2;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = r5.getString(r6);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = 3;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r8 = 4;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = r7.getString(r8);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r8 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r16 = 5;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r16;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r8 = r8.getString(r0);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r15.add(r1);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.mListOfDevices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r2.getString(r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = r0.listOfDevices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.put(r2, r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.getString(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0.lastDevice = r1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        goto L_0x0151;
    L_0x01e8:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.myDB;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.close();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.dbh;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.close();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;
        r1 = r0.myDB;
        if (r1 == 0) goto L_0x016f;
    L_0x0200:
        r0 = r17;
        r1 = r0.dbh;
        r1.close();
        goto L_0x016f;
    L_0x0209:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.<init>();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r0.company;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r2.toLowerCase();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = "w";	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r14 = r1.toString();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r15 = r0.listOfDevices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = new de.toastcode.screener.layouts.Devices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r2.getString(r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = r17.getResources();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = "drawable";	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = r17.getPackageName();	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = r3.getIdentifier(r14, r5, r6);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = 2;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r5 = r5.getString(r6);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = 3;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r8 = 4;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r7 = r7.getString(r8);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r8 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r16 = 5;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r16;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r8 = r8.getString(r0);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r15.add(r1);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.mListOfDevices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = r2.getString(r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r3 = r0.listOfDevices;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1.put(r2, r3);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r0.c;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r2 = 1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r1 = r1.getString(r2);	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0 = r17;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        r0.lastDevice = r1;	 Catch:{ Exception -> 0x01e8, all -> 0x0291 }
        goto L_0x0151;
    L_0x0291:
        r1 = move-exception;
        r0 = r17;
        r2 = r0.myDB;
        if (r2 == 0) goto L_0x029f;
    L_0x0298:
        r0 = r17;
        r2 = r0.dbh;
        r2.close();
    L_0x029f:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.toastcode.screener.activities.Device_DownloadActivity.selectFromDB(java.lang.String, int):void");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_download);
        this.b = getIntent().getExtras();
        this.company = this.b.getString("company");
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(1);
        } else {
            setRequestedOrientation(0);
        }
        this.download_fab = (FloatingActionButton) findViewById(R.id.fab_download);
        this.download_fab.setVisibility(8);
        this.delete_fab = (FloatingActionButton) findViewById(R.id.fab_delete);
        this.delete_fab.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Device_DownloadActivity.this.loadPreferences("delete", 0);
                if (Device_DownloadActivity.this.list_del_devices.size() > 0) {
                    for (int i = 0; i < Device_DownloadActivity.this.list_del_devices.size(); i++) {
                        File mFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/");
                        Crypy crypy = new Crypy();
                        Log.i("DELETE FRAME:", (String) Device_DownloadActivity.this.list_del_devices.get(i));
                        new File(mFolder.getAbsolutePath(), crypy.encrypt(((String) Device_DownloadActivity.this.list_del_devices.get(i)) + ".png")).delete();
                    }
                    Device_DownloadActivity.this.list_del_devices.clear();
                    Device_DownloadActivity.this.snack = Snackbar.make(Device_DownloadActivity.this.findViewById(R.id.main_recycler), Device_DownloadActivity.this.getResources().getString(R.string.frames_delete), 0);
                    ((TextView) Device_DownloadActivity.this.snack.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
                    Device_DownloadActivity.this.snack.show();
                    Device_DownloadActivity.this.savePreferences("delete", Device_DownloadActivity.this.list_del_devices);
                    Device_DownloadActivity.this.setGrid();
                    return;
                }
                Device_DownloadActivity.this.snack = Snackbar.make(Device_DownloadActivity.this.findViewById(R.id.main_recycler), Device_DownloadActivity.this.getResources().getString(R.string.please_choose_device_delete), 0);
                ((TextView) Device_DownloadActivity.this.snack.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
                Device_DownloadActivity.this.snack.show();
            }
        });
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(this, 2);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setTitle(this.company);
        this.mToolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        this.mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primarydark));
        }
        setGrid();
        loadPreferences("recreate", 2);
        if (this.count == 0) {
            recreate();
            saveIntPreferences("recreate", 1);
        } else {
            saveIntPreferences("recreate", 0);
        }
        loadPreferences("deviceToDelete", 1);
        if (this.deviceToDelete) {
            this.delete_fab.setVisibility(0);
        } else {
            this.delete_fab.setVisibility(8);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        overridePendingTransition(R.anim.nichts, R.anim.push_down_out);
        return true;
    }

    public void setGrid() {
        this.mAdapter = null;
        this.sql = "SELECT * FROM dreid WHERE name LIKE '%" + this.company + "%' UNION SELECT * FROM flat WHERE name LIKE '%" + this.company + "%' UNION SELECT * FROM minimals WHERE name LIKE '%" + this.company + "%' ORDER BY name";
        selectFromDB(this.sql, 0);
        this.mAdapter = new GridChooseRecyclerAdapter(this, this.mListOfDevices, 1);
        this.sql = "SELECT * FROM watches WHERE name LIKE '%" + this.company + "%' ORDER BY name";
        selectFromDB(this.sql, 1);
        this.mAdapter = new GridChooseRecyclerAdapter(this, this.mListOfDevices, 1);
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    public void loadPreferences(String KEY, int index) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (index == 0) {
            try {
                if (this.list_del_devices.isEmpty()) {
                    this.list_del_devices = (ArrayList) ObjectSerializer.deserialize(sharedPreferences.getString(KEY, ObjectSerializer.serialize(new ArrayList())));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (index == 1) {
            this.deviceToDelete = sharedPreferences.getBoolean(KEY, false);
        } else if (index == 2) {
            this.count = sharedPreferences.getInt("recreate", 0);
        }
    }

    private void saveIntPreferences(String key, int value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void savePreferences(String key, ArrayList<String> value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        try {
            editor.putString(key, ObjectSerializer.serialize(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }
}
