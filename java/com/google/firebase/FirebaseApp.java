package com.google.firebase;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.internal.zzz;
import com.google.android.gms.common.util.zzc;
import com.google.android.gms.common.util.zzs;
import com.google.android.gms.internal.zzaiy;
import com.google.android.gms.internal.zzaiz;
import com.google.android.gms.internal.zzaja;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class FirebaseApp {
    public static final String DEFAULT_APP_NAME = "[DEFAULT]";
    static final Map<String, FirebaseApp> zzaTZ = new ArrayMap();
    private static final List<String> zzbEC = Arrays.asList(new String[]{"com.google.firebase.auth.FirebaseAuth", "com.google.firebase.iid.FirebaseInstanceId"});
    private static final List<String> zzbED = Collections.singletonList("com.google.firebase.crash.FirebaseCrash");
    private static final List<String> zzbEE = Arrays.asList(new String[]{"com.google.android.gms.measurement.AppMeasurement"});
    private static final Set<String> zzbEF = Collections.emptySet();
    private static final Object zzrs = new Object();
    private final String mName;
    private final FirebaseOptions zzbEG;
    private final AtomicBoolean zzbEH = new AtomicBoolean(true);
    private final AtomicBoolean zzbEI = new AtomicBoolean();
    private final List<zza> zzbEJ = new CopyOnWriteArrayList();
    private final List<zzb> zzbEK = new CopyOnWriteArrayList();
    private final List<Object> zzbEL = new CopyOnWriteArrayList();
    protected zzaja zzbEM;
    private final Context zztm;

    public interface zza {
        void zzb(@NonNull zzaja com_google_android_gms_internal_zzaja, @Nullable FirebaseUser firebaseUser);
    }

    public interface zzb {
        void zzaI(boolean z);
    }

    protected FirebaseApp(Context context, String str, FirebaseOptions firebaseOptions) {
        this.zztm = (Context) zzaa.zzz(context);
        this.mName = zzaa.zzdl(str);
        this.zzbEG = (FirebaseOptions) zzaa.zzz(firebaseOptions);
    }

    public static List<FirebaseApp> getApps(Context context) {
        return new ArrayList(zzaTZ.values());
    }

    @Nullable
    public static FirebaseApp getInstance() {
        return getInstance(DEFAULT_APP_NAME);
    }

    public static FirebaseApp getInstance(@NonNull String str) {
        FirebaseApp firebaseApp;
        synchronized (zzrs) {
            firebaseApp = (FirebaseApp) zzaTZ.get(zzhF(str));
            if (firebaseApp != null) {
            } else {
                String str2;
                Iterable zzNW = zzNW();
                if (zzNW.isEmpty()) {
                    str2 = BuildConfig.FLAVOR;
                } else {
                    String str3 = "Available app names: ";
                    str2 = String.valueOf(zzx.zzdk(", ").zza(zzNW));
                    str2 = str2.length() != 0 ? str3.concat(str2) : new String(str3);
                }
                throw new IllegalStateException(String.format("FirebaseApp with name %s doesn't exist. %s", new Object[]{str, str2}));
            }
        }
        return firebaseApp;
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions) {
        return initializeApp(context, firebaseOptions, DEFAULT_APP_NAME);
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions, String str) {
        FirebaseApp firebaseApp;
        zzaiz zzbB = zzaiz.zzbB(context);
        zzbv(context);
        String zzhF = zzhF(str);
        Context applicationContext = context.getApplicationContext();
        synchronized (zzrs) {
            zzaa.zza(!zzaTZ.containsKey(zzhF), new StringBuilder(String.valueOf(zzhF).length() + 33).append("FirebaseApp name ").append(zzhF).append(" already exists!").toString());
            zzaa.zzb(applicationContext, "Application context cannot be null.");
            firebaseApp = new FirebaseApp(applicationContext, zzhF, firebaseOptions);
            zzaTZ.put(zzhF, firebaseApp);
        }
        zzbB.zzf(firebaseApp);
        zza(FirebaseApp.class, firebaseApp, zzbEC);
        if (firebaseApp.zzNU()) {
            zza(FirebaseApp.class, firebaseApp, zzbED);
            zza(Context.class, firebaseApp.getApplicationContext(), zzbEE);
        }
        return firebaseApp;
    }

    private void zzNT() {
        zzaa.zza(!this.zzbEI.get(), "FirebaseApp was deleted");
    }

    private static List<String> zzNW() {
        com.google.android.gms.common.util.zza com_google_android_gms_common_util_zza = new com.google.android.gms.common.util.zza();
        synchronized (zzrs) {
            for (FirebaseApp name : zzaTZ.values()) {
                com_google_android_gms_common_util_zza.add(name.getName());
            }
            zzaiz zzUw = zzaiz.zzUw();
            if (zzUw != null) {
                com_google_android_gms_common_util_zza.addAll(zzUw.zzUx());
            }
        }
        List<String> arrayList = new ArrayList(com_google_android_gms_common_util_zza);
        Collections.sort(arrayList);
        return arrayList;
    }

    private static <T> void zza(Class<T> cls, T t, Iterable<String> iterable) {
        String valueOf;
        for (String valueOf2 : iterable) {
            try {
                Class cls2 = Class.forName(valueOf2);
                Method method = cls2.getMethod("getInstance", new Class[]{cls});
                if ((method.getModifiers() & 9) == 9) {
                    method.invoke(null, new Object[]{t});
                }
                String valueOf3 = String.valueOf(cls2);
                Log.d("FirebaseApp", new StringBuilder(String.valueOf(valueOf3).length() + 13).append("Initialized ").append(valueOf3).append(".").toString());
            } catch (ClassNotFoundException e) {
                if (zzbEF.contains(valueOf2)) {
                    throw new IllegalStateException(String.valueOf(valueOf2).concat(" is missing, but is required. Check if it has been removed by Proguard."));
                }
                Log.d("FirebaseApp", String.valueOf(valueOf2).concat(" is not linked. Skipping initialization."));
            } catch (NoSuchMethodException e2) {
                throw new IllegalStateException(String.valueOf(valueOf2).concat("#getInstance has been removed by Proguard. Add keep rule to prevent it."));
            } catch (Throwable e3) {
                Log.wtf("FirebaseApp", "Firebase API initialization failure.", e3);
            } catch (Throwable e4) {
                String str = "FirebaseApp";
                String str2 = "Failed to initialize ";
                valueOf2 = String.valueOf(valueOf2);
                Log.wtf(str, valueOf2.length() != 0 ? str2.concat(valueOf2) : new String(str2), e4);
            }
        }
    }

    public static void zzaI(boolean z) {
        synchronized (zzrs) {
            Iterator it = new ArrayList(zzaTZ.values()).iterator();
            while (it.hasNext()) {
                FirebaseApp firebaseApp = (FirebaseApp) it.next();
                if (firebaseApp.zzbEH.get()) {
                    firebaseApp.zzaJ(z);
                }
            }
        }
    }

    private void zzaJ(boolean z) {
        Log.d("FirebaseApp", "Notifying background state change listeners.");
        for (zzb zzaI : this.zzbEK) {
            zzaI.zzaI(z);
        }
    }

    public static FirebaseApp zzbu(Context context) {
        FirebaseOptions fromResource = FirebaseOptions.fromResource(context);
        return fromResource == null ? null : initializeApp(context, fromResource);
    }

    @TargetApi(14)
    private static void zzbv(Context context) {
        if (zzs.zzva() && (context.getApplicationContext() instanceof Application)) {
            zzaiy.zza((Application) context.getApplicationContext());
        }
    }

    private static String zzhF(@NonNull String str) {
        return str.trim();
    }

    public boolean equals(Object obj) {
        return !(obj instanceof FirebaseApp) ? false : this.mName.equals(((FirebaseApp) obj).getName());
    }

    @NonNull
    public Context getApplicationContext() {
        zzNT();
        return this.zztm;
    }

    @NonNull
    public String getName() {
        zzNT();
        return this.mName;
    }

    @NonNull
    public FirebaseOptions getOptions() {
        zzNT();
        return this.zzbEG;
    }

    public Task<GetTokenResult> getToken(boolean z) {
        zzNT();
        return this.zzbEM == null ? Tasks.forException(new FirebaseApiNotAvailableException("firebase-auth is not linked, please fall back to unauthenticated mode.")) : this.zzbEM.zza(this.zzbEM.getCurrentUser(), z);
    }

    public int hashCode() {
        return this.mName.hashCode();
    }

    public String toString() {
        return zzz.zzy(this).zzg("name", this.mName).zzg("options", this.zzbEG).toString();
    }

    public zzaja zzNS() {
        zzNT();
        return this.zzbEM;
    }

    public boolean zzNU() {
        return DEFAULT_APP_NAME.equals(getName());
    }

    public String zzNV() {
        String valueOf = String.valueOf(zzc.zzm(getName().getBytes()));
        String valueOf2 = String.valueOf(zzc.zzm(getOptions().getApplicationId().getBytes()));
        return new StringBuilder((String.valueOf(valueOf).length() + 1) + String.valueOf(valueOf2).length()).append(valueOf).append("+").append(valueOf2).toString();
    }

    public void zza(@NonNull zzaja com_google_android_gms_internal_zzaja) {
        this.zzbEM = (zzaja) zzaa.zzz(com_google_android_gms_internal_zzaja);
    }

    @UiThread
    public void zza(zzaja com_google_android_gms_internal_zzaja, FirebaseUser firebaseUser) {
        Log.d("FirebaseApp", "Notifying auth state listeners.");
        int i = 0;
        for (zza zzb : this.zzbEJ) {
            zzb.zzb(com_google_android_gms_internal_zzaja, firebaseUser);
            i++;
        }
        Log.d("FirebaseApp", String.format("Notified %d auth state listeners.", new Object[]{Integer.valueOf(i)}));
    }

    public void zza(@NonNull zza com_google_firebase_FirebaseApp_zza) {
        zzNT();
        zzaa.zzz(com_google_firebase_FirebaseApp_zza);
        this.zzbEJ.add(com_google_firebase_FirebaseApp_zza);
    }
}
