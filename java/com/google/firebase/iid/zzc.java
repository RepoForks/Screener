package com.google.firebase.iid;

import android.support.annotation.Nullable;

public class zzc {
    private final FirebaseInstanceId baD;

    private zzc(FirebaseInstanceId firebaseInstanceId) {
        this.baD = firebaseInstanceId;
    }

    public static zzc zzcwt() {
        return new zzc(FirebaseInstanceId.getInstance());
    }

    public String getId() {
        return this.baD.getId();
    }

    @Nullable
    public String getToken() {
        return this.baD.getToken();
    }
}
