package de.toastcode.screener;

import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

public class Application extends android.app.Application {
    private static Application instance = new Application();

    public Application() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Kit[]{new Crashlytics()});
    }
}
