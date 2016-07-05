package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import me.zhanghai.android.materialprogressbar.BuildConfig;
import me.zhanghai.android.materialprogressbar.R;

public class FirebaseInstanceId {
    private static Map<String, FirebaseInstanceId> zzaTZ = new ArrayMap();
    private static zze zzbSC;
    private final FirebaseApp zzbSD;
    private final zzd zzbSE;
    private final String zzbSF = zzUm();

    private FirebaseInstanceId(FirebaseApp firebaseApp, zzd com_google_firebase_iid_zzd) {
        this.zzbSD = firebaseApp;
        this.zzbSE = com_google_firebase_iid_zzd;
        if (this.zzbSF == null) {
            throw new IllegalStateException("IID failing to initialize, FirebaseApp is missing project ID");
        }
        FirebaseInstanceIdService.zza(this.zzbSD.getApplicationContext(), this);
    }

    public static FirebaseInstanceId getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    @Keep
    public static synchronized FirebaseInstanceId getInstance(@NonNull FirebaseApp firebaseApp) {
        FirebaseInstanceId firebaseInstanceId;
        synchronized (FirebaseInstanceId.class) {
            firebaseInstanceId = (FirebaseInstanceId) zzaTZ.get(firebaseApp.getOptions().getApplicationId());
            if (firebaseInstanceId == null) {
                zzd zzb = zzd.zzb(firebaseApp.getApplicationContext(), null);
                if (zzbSC == null) {
                    zzbSC = new zze(zzb.zzUs());
                }
                firebaseInstanceId = new FirebaseInstanceId(firebaseApp, zzb);
                zzaTZ.put(firebaseApp.getOptions().getApplicationId(), firebaseInstanceId);
            }
        }
        return firebaseInstanceId;
    }

    static String zza(KeyPair keyPair) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA1").digest(keyPair.getPublic().getEncoded());
            digest[0] = (byte) (((digest[0] & 15) + R.styleable.AppCompatTheme_spinnerStyle) & 255);
            return Base64.encodeToString(digest, 0, 8, 11);
        } catch (NoSuchAlgorithmException e) {
            Log.w("FirebaseInstanceId", "Unexpected error, device missing required alghorithms");
            return null;
        }
    }

    static void zza(Context context, zzg com_google_firebase_iid_zzg) {
        com_google_firebase_iid_zzg.zzCk();
        Intent intent = new Intent();
        intent.putExtra("CMD", "RST");
        context.sendBroadcast(FirebaseInstanceIdInternalReceiver.zzh(context, intent));
    }

    static int zzaU(Context context) {
        int i = 0;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            String valueOf = String.valueOf(e);
            Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 38).append("Never happens: can't find own package ").append(valueOf).toString());
            return i;
        }
    }

    static String zzaV(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            String valueOf = String.valueOf(e);
            Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 38).append("Never happens: can't find own package ").append(valueOf).toString());
            return null;
        }
    }

    static void zzaW(Context context) {
        Intent intent = new Intent();
        intent.setPackage(context.getPackageName());
        intent.putExtra("CMD", "SYNC");
        context.sendBroadcast(FirebaseInstanceIdInternalReceiver.zzh(context, intent));
    }

    static String zzbx(Context context) {
        return getInstance().zzbSD.getOptions().getApplicationId();
    }

    static String zzp(byte[] bArr) {
        return Base64.encodeToString(bArr, 11);
    }

    public void deleteInstanceId() throws IOException {
        this.zzbSE.zzb("*", "*", null);
        this.zzbSE.zzCe();
    }

    @WorkerThread
    public void deleteToken(String str, String str2) throws IOException {
        this.zzbSE.zzb(str, str2, null);
    }

    public long getCreationTime() {
        return this.zzbSE.getCreationTime();
    }

    public String getId() {
        return zza(this.zzbSE.zzCd());
    }

    @Nullable
    public String getToken() {
        String zzUn = zzUn();
        if (zzUn == null) {
            FirebaseInstanceIdService.zzby(this.zzbSD.getApplicationContext());
        }
        return zzUn;
    }

    @WorkerThread
    public String getToken(String str, String str2) throws IOException {
        return this.zzbSE.getToken(str, str2, null);
    }

    String zzUm() {
        String gcmSenderId = this.zzbSD.getOptions().getGcmSenderId();
        if (gcmSenderId != null) {
            return gcmSenderId;
        }
        gcmSenderId = this.zzbSD.getOptions().getApplicationId();
        if (!gcmSenderId.startsWith("1:")) {
            return gcmSenderId;
        }
        String[] split = gcmSenderId.split(":");
        if (split.length < 2) {
            return null;
        }
        gcmSenderId = split[1];
        return gcmSenderId.isEmpty() ? null : gcmSenderId;
    }

    @Nullable
    String zzUn() {
        return this.zzbSE.zzUs().zzi(BuildConfig.FLAVOR, this.zzbSF, "*");
    }

    String zzUo() throws IOException {
        return getToken(this.zzbSF, "*");
    }

    zze zzUp() {
        return zzbSC;
    }

    void zziA(String str) throws IOException {
        if (getToken() == null) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String str2 = "gcm.topic";
        String valueOf = String.valueOf("/topics/");
        String valueOf2 = String.valueOf(str);
        bundle.putString(str2, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        zzd com_google_firebase_iid_zzd = this.zzbSE;
        valueOf = getToken();
        String valueOf3 = String.valueOf("/topics/");
        valueOf2 = String.valueOf(str);
        com_google_firebase_iid_zzd.zzb(valueOf, valueOf2.length() != 0 ? valueOf3.concat(valueOf2) : new String(valueOf3), bundle);
    }

    public void zziy(String str) {
        zzbSC.zziy(str);
        FirebaseInstanceIdService.zzby(this.zzbSD.getApplicationContext());
    }

    void zziz(String str) throws IOException {
        if (getToken() == null) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String str2 = "gcm.topic";
        String valueOf = String.valueOf("/topics/");
        String valueOf2 = String.valueOf(str);
        bundle.putString(str2, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        zzd com_google_firebase_iid_zzd = this.zzbSE;
        valueOf = getToken();
        String valueOf3 = String.valueOf("/topics/");
        valueOf2 = String.valueOf(str);
        com_google_firebase_iid_zzd.getToken(valueOf, valueOf2.length() != 0 ? valueOf3.concat(valueOf2) : new String(valueOf3), bundle);
    }
}
