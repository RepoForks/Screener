package de.toastcode.screener;

import java.io.IOException;

public class WrappedIOException {
    public static IOException wrap(Throwable e) {
        return wrap(e.getMessage(), e);
    }

    public static IOException wrap(String message, Throwable e) {
        IOException wrappedException = new IOException(message + " [" + e.getMessage() + "]");
        wrappedException.setStackTrace(e.getStackTrace());
        wrappedException.initCause(e);
        return wrappedException;
    }
}
