package com.mikepenz.aboutlibraries;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mikepenz.aboutlibraries.entity.Library;
import com.mikepenz.aboutlibraries.ui.item.HeaderItem;
import com.mikepenz.aboutlibraries.ui.item.LibraryItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.zhanghai.android.materialprogressbar.R;

public class LibsFragmentCompat {
    private static ArrayList<Library> libraries;
    private LibsBuilder builder = null;
    private Comparator<Library> comparator;
    private FastItemAdapter mAdapter;
    private LibraryTask mLibTask;
    private RecyclerView mRecyclerView;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$mikepenz$aboutlibraries$LibTaskExecutor = new int[LibTaskExecutor.values().length];

        static {
            try {
                $SwitchMap$com$mikepenz$aboutlibraries$LibTaskExecutor[LibTaskExecutor.THREAD_POOL_EXECUTOR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$mikepenz$aboutlibraries$LibTaskExecutor[LibTaskExecutor.SERIAL_EXECUTOR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$mikepenz$aboutlibraries$LibTaskExecutor[LibTaskExecutor.DEFAULT_EXECUTOR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public class LibraryTask extends AsyncTask<String, String, String> {
        Context ctx;
        Drawable icon = null;
        Integer versionCode;
        String versionName;

        public LibraryTask(Context ctx) {
            this.ctx = ctx;
        }

        protected void onPreExecute() {
            if (LibsConfiguration.getInstance().getLibTaskCallback() != null) {
                LibsConfiguration.getInstance().getLibTaskCallback().onLibTaskStarted();
            }
        }

        protected String doInBackground(String... strings) {
            Libs libs;
            boolean doDefaultSort = false;
            if (LibsFragmentCompat.this.builder.fields == null) {
                libs = new Libs(this.ctx);
            } else {
                libs = new Libs(this.ctx, LibsFragmentCompat.this.builder.fields);
            }
            LibsFragmentCompat.this.builder.aboutShowIcon = LibsFragmentCompat.this.extractBooleanBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutShowIcon, "aboutLibraries_description_showIcon");
            LibsFragmentCompat.this.builder.aboutShowVersion = LibsFragmentCompat.this.extractBooleanBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutShowVersion, "aboutLibraries_description_showVersion");
            LibsFragmentCompat.this.builder.aboutShowVersionName = LibsFragmentCompat.this.extractBooleanBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutShowVersionName, "aboutLibraries_description_showVersionName");
            LibsFragmentCompat.this.builder.aboutShowVersionCode = LibsFragmentCompat.this.extractBooleanBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutShowVersionCode, "aboutLibraries_description_showVersionCode");
            LibsFragmentCompat.this.builder.aboutAppName = LibsFragmentCompat.this.extractStringBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutAppName, "aboutLibraries_description_name");
            LibsFragmentCompat.this.builder.aboutDescription = LibsFragmentCompat.this.extractStringBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutDescription, "aboutLibraries_description_text");
            LibsFragmentCompat.this.builder.aboutAppSpecial1 = LibsFragmentCompat.this.extractStringBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutAppSpecial1, "aboutLibraries_description_special1_name");
            LibsFragmentCompat.this.builder.aboutAppSpecial1Description = LibsFragmentCompat.this.extractStringBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutAppSpecial1Description, "aboutLibraries_description_special1_text");
            LibsFragmentCompat.this.builder.aboutAppSpecial2 = LibsFragmentCompat.this.extractStringBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutAppSpecial2, "aboutLibraries_description_special2_name");
            LibsFragmentCompat.this.builder.aboutAppSpecial2Description = LibsFragmentCompat.this.extractStringBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutAppSpecial2Description, "aboutLibraries_description_special2_text");
            LibsFragmentCompat.this.builder.aboutAppSpecial3 = LibsFragmentCompat.this.extractStringBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutAppSpecial3, "aboutLibraries_description_special3_name");
            LibsFragmentCompat.this.builder.aboutAppSpecial3Description = LibsFragmentCompat.this.extractStringBundleOrResource(this.ctx, libs, LibsFragmentCompat.this.builder.aboutAppSpecial3Description, "aboutLibraries_description_special3_text");
            if (LibsFragmentCompat.libraries == null) {
                libs.modifyLibraries(LibsFragmentCompat.this.builder.libraryModification);
                if (LibsFragmentCompat.this.builder.sort.booleanValue() && LibsFragmentCompat.this.builder.libraryComparator == null && LibsFragmentCompat.this.comparator == null) {
                    doDefaultSort = true;
                }
                LibsFragmentCompat.libraries = libs.prepareLibraries(this.ctx, LibsFragmentCompat.this.builder.internalLibraries, LibsFragmentCompat.this.builder.excludeLibraries, LibsFragmentCompat.this.builder.autoDetect.booleanValue(), doDefaultSort);
                if (LibsFragmentCompat.this.comparator != null) {
                    Collections.sort(LibsFragmentCompat.libraries, LibsFragmentCompat.this.comparator);
                } else if (LibsFragmentCompat.this.builder.libraryComparator != null) {
                    Collections.sort(LibsFragmentCompat.libraries, LibsFragmentCompat.this.builder.libraryComparator);
                }
            }
            if (!(LibsFragmentCompat.this.builder.aboutShowIcon == null || (LibsFragmentCompat.this.builder.aboutShowVersion == null && LibsFragmentCompat.this.builder.aboutShowVersionName == null && !LibsFragmentCompat.this.builder.aboutShowVersionCode.booleanValue()))) {
                PackageManager pm = this.ctx.getPackageManager();
                String packageName = this.ctx.getPackageName();
                ApplicationInfo applicationInfo = null;
                PackageInfo packageInfo = null;
                try {
                    applicationInfo = pm.getApplicationInfo(packageName, 0);
                    packageInfo = pm.getPackageInfo(packageName, 0);
                } catch (Exception e) {
                }
                if (LibsFragmentCompat.this.builder.aboutShowIcon.booleanValue() && applicationInfo != null) {
                    this.icon = applicationInfo.loadIcon(pm);
                }
                this.versionName = null;
                this.versionCode = null;
                if (packageInfo != null) {
                    this.versionName = packageInfo.versionName;
                    this.versionCode = Integer.valueOf(packageInfo.versionCode);
                }
            }
            return null;
        }

        protected void onPostExecute(String s) {
            if (!(LibsFragmentCompat.this.builder.aboutShowIcon == null || (LibsFragmentCompat.this.builder.aboutShowVersion == null && LibsFragmentCompat.this.builder.aboutShowVersionName == null && !LibsFragmentCompat.this.builder.aboutShowVersionCode.booleanValue()))) {
                LibsFragmentCompat.this.mAdapter.add(new HeaderItem().withLibsBuilder(LibsFragmentCompat.this.builder).withAboutVersionName(this.versionName).withAboutVersionCode(this.versionCode).withAboutIcon(this.icon));
            }
            List libraryItems = new ArrayList();
            Iterator it = LibsFragmentCompat.libraries.iterator();
            while (it.hasNext()) {
                libraryItems.add(new LibraryItem().withLibrary((Library) it.next()).withLibsBuilder(LibsFragmentCompat.this.builder));
            }
            LibsFragmentCompat.this.mAdapter.add(libraryItems);
            super.onPostExecute(s);
            if (LibsConfiguration.getInstance().getLibTaskCallback() != null) {
                LibsConfiguration.getInstance().getLibTaskCallback().onLibTaskFinished(LibsFragmentCompat.this.mAdapter);
            }
            this.ctx = null;
        }
    }

    public void setLibraryComparator(Comparator<Library> comparator) {
        this.comparator = comparator;
    }

    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Bundle arguments) {
        if (arguments != null) {
            this.builder = (LibsBuilder) arguments.getSerializable("data");
        } else {
            Log.e("AboutLibraries", "The AboutLibraries fragment can't be build without the bundle containing the LibsBuilder");
        }
        View view = inflater.inflate(R.layout.fragment_opensource, container, false);
        if (LibsConfiguration.getInstance().getUiListener() != null) {
            view = LibsConfiguration.getInstance().getUiListener().preOnCreateView(view);
        }
        if (view.getId() == R.id.cardListView) {
            this.mRecyclerView = (RecyclerView) view;
        } else {
            this.mRecyclerView = (RecyclerView) view.findViewById(R.id.cardListView);
        }
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (LibsConfiguration.getInstance().getItemAnimator() != null) {
            this.mRecyclerView.setItemAnimator(LibsConfiguration.getInstance().getItemAnimator());
        } else {
            this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        if (this.builder != null) {
            this.mAdapter = new FastItemAdapter();
            this.mRecyclerView.setAdapter(this.mAdapter);
        }
        if (LibsConfiguration.getInstance().getUiListener() != null) {
            return LibsConfiguration.getInstance().getUiListener().postOnCreateView(view);
        }
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (view.getContext() != null && this.builder != null) {
            this.mLibTask = new LibraryTask(view.getContext());
            executeLibTask(this.mLibTask);
        }
    }

    protected void executeLibTask(LibraryTask libraryTask) {
        if (libraryTask == null) {
            return;
        }
        if (VERSION.SDK_INT >= 11) {
            switch (AnonymousClass1.$SwitchMap$com$mikepenz$aboutlibraries$LibTaskExecutor[this.builder.libTaskExecutor.ordinal()]) {
                case R.styleable.View_android_focusable /*1*/:
                    libraryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
                    return;
                case R.styleable.View_paddingStart /*2*/:
                    libraryTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new String[0]);
                    return;
                default:
                    libraryTask.execute(new String[0]);
                    return;
            }
        }
        libraryTask.execute(new String[0]);
    }

    public void onDestroyView() {
        if (this.mLibTask != null) {
            this.mLibTask.cancel(true);
            this.mLibTask = null;
        }
    }

    private Boolean extractBooleanBundleOrResource(Context ctx, Libs libs, Boolean value, String resName) {
        Boolean result = null;
        if (value != null) {
            return value;
        }
        String descriptionShowVersion = libs.getStringResourceByName(ctx, resName);
        if (TextUtils.isEmpty(descriptionShowVersion)) {
            return result;
        }
        try {
            return Boolean.valueOf(Boolean.parseBoolean(descriptionShowVersion));
        } catch (Exception e) {
            return result;
        }
    }

    private String extractStringBundleOrResource(Context ctx, Libs libs, String value, String resName) {
        if (value != null) {
            return value;
        }
        String descriptionShowVersion = libs.getStringResourceByName(ctx, resName);
        if (TextUtils.isEmpty(descriptionShowVersion)) {
            return null;
        }
        return descriptionShowVersion;
    }
}
