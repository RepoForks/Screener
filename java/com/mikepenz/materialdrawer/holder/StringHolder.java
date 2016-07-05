package com.mikepenz.materialdrawer.holder;

import android.support.annotation.StringRes;

public class StringHolder extends com.mikepenz.materialize.holder.StringHolder {
    public StringHolder(String text) {
        super(text);
    }

    public StringHolder(@StringRes int textRes) {
        super(textRes);
    }
}
