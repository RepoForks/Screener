package com.google.firebase.auth;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import java.util.List;

public abstract class FirebaseUser implements UserInfo {
    @NonNull
    public Task<Void> delete() {
        return zzcks().zzcjz().zzc(this);
    }

    @Nullable
    public abstract String getDisplayName();

    @Nullable
    public abstract String getEmail();

    @Nullable
    public abstract Uri getPhotoUrl();

    @NonNull
    public abstract List<? extends UserInfo> getProviderData();

    @NonNull
    public abstract String getProviderId();

    @Nullable
    public abstract List<String> getProviders();

    @NonNull
    public Task<GetTokenResult> getToken(boolean z) {
        return zzcks().zzcjz().zza(this, z);
    }

    @NonNull
    public abstract String getUid();

    public abstract boolean isAnonymous();

    @NonNull
    public Task<AuthResult> linkWithCredential(@NonNull AuthCredential authCredential) {
        zzab.zzaa(authCredential);
        return zzcks().zzcjz().zzb(this, authCredential);
    }

    public Task<Void> reauthenticate(@NonNull AuthCredential authCredential) {
        zzab.zzaa(authCredential);
        return zzcks().zzcjz().zza(this, authCredential);
    }

    @NonNull
    public Task<Void> reload() {
        return zzcks().zzcjz().zzb(this);
    }

    public Task<AuthResult> unlink(@NonNull String str) {
        zzab.zzhs(str);
        return zzcks().zzcjz().zza(this, str);
    }

    @NonNull
    public Task<Void> updateEmail(@NonNull String str) {
        zzab.zzhs(str);
        return zzcks().zzcjz().zzb(this, str);
    }

    @NonNull
    public Task<Void> updatePassword(@NonNull String str) {
        zzab.zzhs(str);
        return zzcks().zzcjz().zzc(this, str);
    }

    @NonNull
    public Task<Void> updateProfile(@NonNull UserProfileChangeRequest userProfileChangeRequest) {
        zzab.zzaa(userProfileChangeRequest);
        return zzcks().zzcjz().zza(this, userProfileChangeRequest);
    }

    @NonNull
    public abstract FirebaseUser zzan(@NonNull List<? extends UserInfo> list);

    @NonNull
    public abstract FirebaseApp zzcks();

    @NonNull
    public abstract String zzckt();

    public abstract FirebaseUser zzcm(boolean z);

    public abstract void zzql(@NonNull String str);
}
