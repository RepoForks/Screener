package com.mikepenz.aboutlibraries;

import java.io.Serializable;

public enum LibTaskExecutor implements Serializable {
    DEFAULT_EXECUTOR,
    THREAD_POOL_EXECUTOR,
    SERIAL_EXECUTOR
}
