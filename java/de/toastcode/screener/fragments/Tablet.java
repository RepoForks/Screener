package de.toastcode.screener.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.toastcode.screener.R;
import de.toastcode.screener.database.Database_Helper;
import java.util.ArrayList;

public class Tablet extends Fragment {
    Cursor c;
    Database_Helper dbh;
    ArrayList<String> device_name;
    Adapter mAdapter;
    LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    SQLiteDatabase myDB;
    View rootView;
    public String sql;
    String tablequery = "tablet";
    ArrayList<String> thumb_image;

    public void onStart() {
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
        r10 = this;
        r8 = 0;
        super.onStart();
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "SELECT _id, name, thumb_image FROM '";
        r5 = r5.append(r6);
        r6 = r10.tablequery;
        r5 = r5.append(r6);
        r6 = "' ORDER BY name";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r10.sql = r5;
        r5 = new de.toastcode.screener.database.Database_Helper;
        r6 = r10.getActivity();
        r6 = r6.getBaseContext();
        r5.<init>(r6);
        r10.dbh = r5;
        r5 = r10.dbh;
        r5.initializeDataBase();
        r5 = r10.getActivity();
        r5 = r5.getBaseContext();
        r6 = "devices.scr";
        r7 = 0;
        r5 = r5.openOrCreateDatabase(r6, r7, r8);
        r10.myDB = r5;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r10.device_name = r5;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r10.thumb_image = r5;
        r5 = r10.dbh;
        r6 = r10.myDB;
        r5.onOpen(r6);
        r5 = r10.myDB;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r10.sql;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r7 = 0;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r5.rawQuery(r6, r7);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r10.c = r5;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r10.getActivity();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r10.c;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.startManagingCursor(r6);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r10.c;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        if (r5 == 0) goto L_0x00ed;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
    L_0x0073:
        r5 = r10.c;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r5.moveToFirst();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        if (r5 == 0) goto L_0x00e8;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
    L_0x007b:
        r0 = new de.sr.library.Crypy;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r0.<init>();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.<init>();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r10.c;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r7 = 2;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = ".png";	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r4 = r0.encrypt(r5);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.<init>();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r6.getPath();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = "/Android/data/de.toastcode.screener/";	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r5.append(r4);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r3 = r5.toString();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r2 = new java.io.File;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r2.exists();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        if (r5 == 0) goto L_0x00e0;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
    L_0x00c8:
        r5 = r10.device_name;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r10.c;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r7 = 1;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.add(r6);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r10.thumb_image;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r10.c;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r7 = 2;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.add(r6);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
    L_0x00e0:
        r5 = r10.c;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r5.moveToNext();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        if (r5 != 0) goto L_0x007b;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
    L_0x00e8:
        r5 = r10.c;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.moveToFirst();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
    L_0x00ed:
        r5 = new de.toastcode.screener.adapters.GridRecyclerAdapter;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r10.getActivity();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r7 = r10.device_name;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r8 = r10.thumb_image;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r9 = r10.tablequery;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.<init>(r6, r7, r8, r9);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r10.mAdapter = r5;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r10.mRecyclerView;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r6 = r10.mAdapter;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.setAdapter(r6);	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r10.myDB;
        if (r5 == 0) goto L_0x010e;
    L_0x0109:
        r5 = r10.dbh;
        r5.close();
    L_0x010e:
        return;
    L_0x010f:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r10.myDB;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.close();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r10.dbh;	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5.close();	 Catch:{ Exception -> 0x010f, all -> 0x0127 }
        r5 = r10.myDB;
        if (r5 == 0) goto L_0x010e;
    L_0x0121:
        r5 = r10.dbh;
        r5.close();
        goto L_0x010e;
    L_0x0127:
        r5 = move-exception;
        r6 = r10.myDB;
        if (r6 == 0) goto L_0x0131;
    L_0x012c:
        r6 = r10.dbh;
        r6.close();
    L_0x0131:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.toastcode.screener.fragments.Tablet.onStart():void");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.activity_main_recycler, container, false);
        this.mRecyclerView = (RecyclerView) this.rootView.findViewById(R.id.recycler_view);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(getActivity(), 2);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        return this.rootView;
    }

    public void onResume() {
        super.onResume();
        this.dbh = new Database_Helper(getActivity().getBaseContext());
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("devices.scr", 0, null);
        this.dbh.onOpen(db);
        db.close();
        this.dbh.close();
    }
}
