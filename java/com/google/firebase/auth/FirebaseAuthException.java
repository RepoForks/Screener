package com.google.firebase.auth;

import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzab;
import com.google.firebase.FirebaseException;

public class FirebaseAuthException extends FirebaseException {
    private final String aNs;

    public FirebaseAuthException(@NonNull String str, @NonNull String str2) {
        super(str2);
        this.aNs = zzab.zzhs(str);
    }

    @NonNull
    public String getErrorCode() {
        return this.aNs;
    }
}
