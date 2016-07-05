package de.toastcode.screener.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import de.toastcode.screener.R;
import de.toastcode.screener.activities.Choose_CompanyActivity;
import de.toastcode.screener.adapters.GridRecyclerAdapter;
import de.toastcode.screener.database.Database_Helper;
import java.util.ArrayList;

public class Minimals extends Fragment {
    Cursor c;
    Database_Helper dbh;
    ArrayList<String> device_name;
    GridRecyclerAdapter mAdapterGrid;
    LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    SQLiteDatabase myDB;
    LinearLayout noFrame;
    View rootView;
    public String sql;
    Button startDownload;
    String tablequery = "minimals";
    ArrayList<String> thumb_image;

    public void selectFromDB(java.lang.String r10) {
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
        r9 = this;
        r8 = 0;
        r5 = new de.toastcode.screener.database.Database_Helper;
        r6 = r9.getActivity();
        r6 = r6.getBaseContext();
        r5.<init>(r6);
        r9.dbh = r5;
        r5 = r9.dbh;
        r5.initializeDataBase();
        r5 = r9.getActivity();
        r5 = r5.getBaseContext();
        r6 = "devices.scr";
        r7 = 0;
        r5 = r5.openOrCreateDatabase(r6, r7, r8);
        r9.myDB = r5;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r9.device_name = r5;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r9.thumb_image = r5;
        r5 = r9.dbh;
        r6 = r9.myDB;
        r5.onOpen(r6);
        r5 = r9.myDB;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = 0;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r5.rawQuery(r10, r6);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r9.c = r5;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r9.getActivity();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = r9.c;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5.startManagingCursor(r6);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r9.c;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        if (r5 == 0) goto L_0x00cb;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
    L_0x0051:
        r5 = r9.c;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r5.moveToFirst();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        if (r5 == 0) goto L_0x00c6;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
    L_0x0059:
        r0 = new de.sr.library.Crypy;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r0.<init>();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5.<init>();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = r9.c;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r7 = 2;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = ".png";	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r5.toString();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r4 = r0.encrypt(r5);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5.<init>();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = r6.getPath();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = "/Android/data/de.toastcode.screener/";	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r5.append(r4);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r3 = r5.toString();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r2 = new java.io.File;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r2.<init>(r3);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r2.exists();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        if (r5 == 0) goto L_0x00be;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
    L_0x00a6:
        r5 = r9.device_name;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = r9.c;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r7 = 1;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5.add(r6);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r9.thumb_image;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = r9.c;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r7 = 2;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5.add(r6);	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
    L_0x00be:
        r5 = r9.c;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r5.moveToNext();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        if (r5 != 0) goto L_0x0059;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
    L_0x00c6:
        r5 = r9.c;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5.moveToFirst();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
    L_0x00cb:
        r5 = r9.myDB;
        if (r5 == 0) goto L_0x00d4;
    L_0x00cf:
        r5 = r9.dbh;
        r5.close();
    L_0x00d4:
        return;
    L_0x00d5:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r9.myDB;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5.close();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r9.dbh;	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5.close();	 Catch:{ Exception -> 0x00d5, all -> 0x00ed }
        r5 = r9.myDB;
        if (r5 == 0) goto L_0x00d4;
    L_0x00e7:
        r5 = r9.dbh;
        r5.close();
        goto L_0x00d4;
    L_0x00ed:
        r5 = move-exception;
        r6 = r9.myDB;
        if (r6 == 0) goto L_0x00f7;
    L_0x00f2:
        r6 = r9.dbh;
        r6.close();
    L_0x00f7:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.toastcode.screener.fragments.Minimals.selectFromDB(java.lang.String):void");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.activity_main_recycler, container, false);
        this.noFrame = (LinearLayout) this.rootView.findViewById(R.id.no_frame_ll);
        this.startDownload = (Button) this.rootView.findViewById(R.id.startDownload);
        this.startDownload.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Minimals.this.startActivity(new Intent(Minimals.this.getActivity(), Choose_CompanyActivity.class));
            }
        });
        this.mRecyclerView = (RecyclerView) this.rootView.findViewById(R.id.recycler_view);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(getActivity(), 2);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        setHasOptionsMenu(true);
        return this.rootView;
    }

    public void onStart() {
        super.onStart();
        this.sql = "SELECT _id, name, thumb_image FROM '" + this.tablequery + "' ORDER BY name";
        selectFromDB(this.sql);
        if (this.device_name.size() == 0) {
            this.noFrame.setVisibility(0);
            this.mRecyclerView.setVisibility(8);
        } else {
            this.noFrame.setVisibility(8);
            this.mRecyclerView.setVisibility(0);
        }
        this.mAdapterGrid = new GridRecyclerAdapter(getActivity(), this.device_name, this.thumb_image, this.tablequery);
        this.mRecyclerView.setAdapter(this.mAdapterGrid);
    }

    public void onResume() {
        super.onResume();
        this.dbh = new Database_Helper(getActivity().getBaseContext());
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("devices.scr", 0, null);
        this.dbh.onOpen(db);
        db.close();
        this.dbh.close();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        ((SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search))).setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                Minimals.this.mAdapterGrid.filter(query);
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                Minimals.this.mAdapterGrid.filter(newText);
                return true;
            }
        });
    }
}
