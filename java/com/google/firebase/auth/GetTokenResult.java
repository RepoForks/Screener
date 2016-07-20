package com.google.firebase.auth;

import android.support.annotation.Nullable;

public class GetTokenResult {
    private String ct;

    public GetTokenResult(String str) {
        this.ct = str;
    }

    @Nullable
    public String getToken() {
        return this.ct;
    }
}
