package com.mikepenz.aboutlibraries;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LayoutAnimationController;
import com.mikepenz.aboutlibraries.Libs.ActivityStyle;
import com.mikepenz.aboutlibraries.Libs.LibraryFields;
import com.mikepenz.aboutlibraries.LibsConfiguration.LibsListener;
import com.mikepenz.aboutlibraries.LibsConfiguration.LibsRecyclerViewListener;
import com.mikepenz.aboutlibraries.LibsConfiguration.LibsUIListener;
import com.mikepenz.aboutlibraries.entity.Library;
import com.mikepenz.aboutlibraries.ui.LibsActivity;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.mikepenz.aboutlibraries.ui.item.LibraryItem;
import com.mikepenz.aboutlibraries.util.Colors;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LibsBuilder implements Serializable {
    public String aboutAppName = null;
    public String aboutAppSpecial1 = null;
    public String aboutAppSpecial1Description = null;
    public String aboutAppSpecial2 = null;
    public String aboutAppSpecial2Description = null;
    public String aboutAppSpecial3 = null;
    public String aboutAppSpecial3Description = null;
    public String aboutDescription = null;
    public Boolean aboutShowIcon = null;
    public Boolean aboutShowVersion = null;
    public Boolean aboutShowVersionCode = Boolean.valueOf(false);
    public Boolean aboutShowVersionName = Boolean.valueOf(false);
    public String aboutVersionString = null;
    public Colors activityColor = null;
    public ActivityStyle activityStyle = null;
    public Integer activityTheme = Integer.valueOf(-1);
    public String activityTitle = null;
    public Boolean autoDetect = Boolean.valueOf(true);
    public String[] excludeLibraries = null;
    public String[] fields = null;
    public String[] internalLibraries = null;
    public LibTaskExecutor libTaskExecutor = LibTaskExecutor.DEFAULT_EXECUTOR;
    public Comparator<Library> libraryComparator = null;
    public HashMap<String, HashMap<String, String>> libraryModification = null;
    public Boolean showLicense = Boolean.valueOf(false);
    public Boolean showLicenseDialog = Boolean.valueOf(true);
    public Boolean showVersion = Boolean.valueOf(false);
    public Boolean sort = Boolean.valueOf(true);

    public LibsBuilder withFields(Field[] fields) {
        return withFields(Libs.toStringArray(fields));
    }

    public LibsBuilder withFields(String... fields) {
        this.fields = fields;
        return this;
    }

    public LibsBuilder withLibraries(String... libraries) {
        this.internalLibraries = libraries;
        return this;
    }

    public LibsBuilder withExcludedLibraries(String... excludeLibraries) {
        this.excludeLibraries = excludeLibraries;
        return this;
    }

    public LibsBuilder withAutoDetect(boolean autoDetect) {
        this.autoDetect = Boolean.valueOf(autoDetect);
        return this;
    }

    public LibsBuilder withSortEnabled(boolean sort) {
        this.sort = Boolean.valueOf(sort);
        return this;
    }

    public LibsBuilder withLibraryComparator(Comparator<Library> libraryComparator) {
        this.libraryComparator = libraryComparator;
        this.sort = Boolean.valueOf(libraryComparator != null);
        return this;
    }

    public LibsBuilder withLicenseShown(boolean showLicense) {
        this.showLicense = Boolean.valueOf(showLicense);
        return this;
    }

    public LibsBuilder withLicenseDialog(boolean showLicenseDialog) {
        this.showLicenseDialog = Boolean.valueOf(showLicenseDialog);
        return this;
    }

    public LibsBuilder withVersionShown(boolean showVersion) {
        this.showVersion = Boolean.valueOf(showVersion);
        return this;
    }

    public LibsBuilder withAboutIconShown(boolean aboutShowIcon) {
        this.aboutShowIcon = Boolean.valueOf(aboutShowIcon);
        return this;
    }

    public LibsBuilder withAboutVersionShown(boolean aboutShowVersion) {
        this.aboutShowVersion = Boolean.valueOf(aboutShowVersion);
        this.aboutShowVersionName = Boolean.valueOf(aboutShowVersion);
        this.aboutShowVersionCode = Boolean.valueOf(aboutShowVersion);
        return this;
    }

    public LibsBuilder withAboutVersionShownName(boolean aboutShowVersion) {
        this.aboutShowVersionName = Boolean.valueOf(aboutShowVersion);
        return this;
    }

    public LibsBuilder withAboutVersionShownCode(boolean aboutShowVersion) {
        this.aboutShowVersionCode = Boolean.valueOf(aboutShowVersion);
        return this;
    }

    public LibsBuilder withAboutVersionString(String aboutVersionString) {
        this.aboutVersionString = aboutVersionString;
        return this;
    }

    public LibsBuilder withAboutAppName(String aboutAppName) {
        this.aboutAppName = aboutAppName;
        return this;
    }

    public LibsBuilder withAboutDescription(String aboutDescription) {
        this.aboutDescription = aboutDescription;
        return this;
    }

    public LibsBuilder withAboutSpecial1(String aboutAppSpecial1) {
        this.aboutAppSpecial1 = aboutAppSpecial1;
        return this;
    }

    public LibsBuilder withAboutSpecial1Description(String aboutAppSpecial1Description) {
        this.aboutAppSpecial1Description = aboutAppSpecial1Description;
        return this;
    }

    public LibsBuilder withAboutSpecial2(String aboutAppSpecial2) {
        this.aboutAppSpecial2 = aboutAppSpecial2;
        return this;
    }

    public LibsBuilder withAboutSpecial2Description(String aboutAppSpecial2Description) {
        this.aboutAppSpecial2Description = aboutAppSpecial2Description;
        return this;
    }

    public LibsBuilder withAboutSpecial3(String aboutAppSpecial3) {
        this.aboutAppSpecial3 = aboutAppSpecial3;
        return this;
    }

    public LibsBuilder withAboutSpecial3Description(String aboutAppSpecial3Description) {
        this.aboutAppSpecial3Description = aboutAppSpecial3Description;
        return this;
    }

    public LibsBuilder withActivityTheme(int activityTheme) {
        this.activityTheme = Integer.valueOf(activityTheme);
        return this;
    }

    public LibsBuilder withActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
        return this;
    }

    public LibsBuilder withActivityColor(Colors activityColor) {
        this.activityColor = activityColor;
        return this;
    }

    public LibsBuilder withActivityStyle(ActivityStyle libraryStyle) {
        this.activityStyle = libraryStyle;
        return this;
    }

    public LibsBuilder withLibraryModification(HashMap<String, HashMap<String, String>> libraryModification) {
        this.libraryModification = libraryModification;
        return this;
    }

    public LibsBuilder withLibraryModification(String library, LibraryFields modificationKey, String modificationValue) {
        if (this.libraryModification == null) {
            this.libraryModification = new HashMap();
        }
        if (!this.libraryModification.containsKey(library)) {
            this.libraryModification.put(library, new HashMap());
        }
        ((HashMap) this.libraryModification.get(library)).put(modificationKey.name(), modificationValue);
        return this;
    }

    public LibsBuilder withListener(LibsListener libsListener) {
        LibsConfiguration.getInstance().setListener(libsListener);
        return this;
    }

    public LibsBuilder withLibsRecyclerViewListener(LibsRecyclerViewListener recyclerViewListener) {
        LibsConfiguration.getInstance().setLibsRecyclerViewListener(recyclerViewListener);
        return this;
    }

    public LibsBuilder withUiListener(LibsUIListener uiListener) {
        LibsConfiguration.getInstance().setUiListener(uiListener);
        return this;
    }

    public LibsBuilder withLayoutAnimationController(LayoutAnimationController layoutAnimationController) {
        LibsConfiguration.getInstance().setLayoutAnimationController(layoutAnimationController);
        return this;
    }

    public LibsBuilder withLibTaskExecutor(LibTaskExecutor libTaskExecutor) {
        if (libTaskExecutor != null) {
            this.libTaskExecutor = libTaskExecutor;
        }
        return this;
    }

    public LibsBuilder withLibTaskCallback(LibTaskCallback libTaskCallback) {
        LibsConfiguration.getInstance().setLibTaskCallback(libTaskCallback);
        return this;
    }

    private void preCheck() {
        if (this.fields == null) {
            Log.w("AboutLibraries", "Have you missed to call withFields(R.string.class.getFields())? - autoDetect won't work - https://github.com/mikepenz/AboutLibraries/wiki/HOWTO:-Fragment");
        }
    }

    public FastItemAdapter adapter(Context context) {
        Libs libs;
        if (this.fields == null) {
            libs = new Libs(context);
        } else {
            libs = new Libs(context, this.fields);
        }
        libs.modifyLibraries(this.libraryModification);
        ArrayList<Library> libraries = libs.prepareLibraries(context, this.internalLibraries, this.excludeLibraries, this.autoDetect.booleanValue(), this.sort.booleanValue());
        FastItemAdapter adapter = new FastItemAdapter();
        List libraryItems = new ArrayList();
        Iterator it = libraries.iterator();
        while (it.hasNext()) {
            libraryItems.add(new LibraryItem().withLibrary((Library) it.next()).withLibsBuilder(this));
        }
        adapter.add(libraryItems);
        return adapter;
    }

    public Intent intent(Context ctx) {
        preCheck();
        Intent i = new Intent(ctx, LibsActivity.class);
        i.putExtra("data", this);
        i.putExtra(Libs.BUNDLE_THEME, this.activityTheme);
        if (this.activityTitle != null) {
            i.putExtra(Libs.BUNDLE_TITLE, this.activityTitle);
        }
        if (this.activityColor != null) {
            i.putExtra(Libs.BUNDLE_COLORS, this.activityColor);
        }
        if (this.activityStyle != null) {
            i.putExtra(Libs.BUNDLE_STYLE, this.activityStyle.name());
        }
        return i;
    }

    public void start(Context ctx) {
        ctx.startActivity(intent(ctx));
    }

    public void activity(Context ctx) {
        start(ctx);
    }

    public LibsSupportFragment supportFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", this);
        LibsSupportFragment fragment = new LibsSupportFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
