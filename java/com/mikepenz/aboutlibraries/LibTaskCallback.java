package com.mikepenz.aboutlibraries;

import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import java.io.Serializable;

public interface LibTaskCallback extends Serializable {
    void onLibTaskFinished(FastItemAdapter fastItemAdapter);

    void onLibTaskStarted();
}
