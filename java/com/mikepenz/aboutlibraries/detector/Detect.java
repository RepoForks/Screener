package com.mikepenz.aboutlibraries.detector;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import com.mikepenz.aboutlibraries.entity.Library;
import java.util.ArrayList;
import java.util.List;

public class Detect {
    public static List<Library> detect(Context mCtx, List<Library> libraries) {
        ArrayList<Library> foundLibraries = new ArrayList();
        for (Library library : libraries) {
            if (!TextUtils.isEmpty(library.getClassPath())) {
                try {
                    if (Class.forName(library.getClassPath(), false, mCtx.createPackageContext(mCtx.getPackageName(), 3).getClassLoader()) != null) {
                        foundLibraries.add(library);
                    }
                } catch (ClassNotFoundException e) {
                } catch (NameNotFoundException e2) {
                }
            }
        }
        return foundLibraries;
    }
}
