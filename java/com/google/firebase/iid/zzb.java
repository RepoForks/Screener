package com.google.firebase.iid;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.iid.MessengerCompat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class zzb extends Service {
    private int zzaSJ;
    private int zzaSK = 0;
    MessengerCompat zzaUg = new MessengerCompat(new Handler(this, Looper.getMainLooper()) {
        final /* synthetic */ zzb zzbSA;

        public void handleMessage(Message message) {
            int zzc = MessengerCompat.zzc(message);
            zzf.zzaX(this.zzbSA);
            this.zzbSA.getPackageManager();
            if (zzc == zzf.zzaUs || zzc == zzf.zzaUr) {
                this.zzbSA.zzm((Intent) message.obj);
                return;
            }
            int i = zzf.zzaUr;
            Log.w("FirebaseInstanceId", "Message from unexpected caller " + zzc + " mine=" + i + " appid=" + zzf.zzaUs);
        }
    });
    final ExecutorService zzbqx = Executors.newSingleThreadExecutor();
    private final Object zzpp = new Object();

    public final IBinder onBind(Intent intent) {
        return (intent == null || !"com.google.firebase.INSTANCE_ID_EVENT".equals(intent.getAction())) ? null : this.zzaUg.getBinder();
    }

    public final int onStartCommand(android.content.Intent r4, int r5, int r6) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextEntry(HashMap.java:854)
	at java.util.HashMap$KeyIterator.next(HashMap.java:885)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:286)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:173)
*/
        /*
        r3 = this;
        r1 = r3.zzpp;
        monitor-enter(r1);
        r3.zzaSJ = r6;
        r0 = r3.zzaSK;
        r0 = r0 + 1;
        r3.zzaSK = r0;
        monitor-exit(r1);
        r1 = r3.zzz(r4);
        if (r1 != 0) goto L_0x001a;
    L_0x0012:
        r3.zzBL();
        r0 = 2;
    L_0x0016:
        return r0;
    L_0x0017:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
    L_0x001a:
        r0 = r3.zzA(r1);	 Catch:{ all -> 0x002a }
        r2 = "from";
        r2 = r1.getStringExtra(r2);
        if (r2 == 0) goto L_0x0016;
    L_0x0026:
        android.support.v4.content.WakefulBroadcastReceiver.completeWakefulIntent(r1);
        goto L_0x0016;
    L_0x002a:
        r0 = move-exception;
        r2 = "from";
        r2 = r1.getStringExtra(r2);
        if (r2 == 0) goto L_0x0036;
    L_0x0033:
        android.support.v4.content.WakefulBroadcastReceiver.completeWakefulIntent(r1);
    L_0x0036:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzb.onStartCommand(android.content.Intent, int, int):int");
    }

    protected int zzA(final Intent intent) {
        this.zzbqx.execute(new Runnable(this) {
            final /* synthetic */ zzb zzbSA;

            public void run() {
                this.zzbSA.zzm(intent);
                this.zzbSA.zzBL();
            }
        });
        return 3;
    }

    protected void zzBL() {
        synchronized (this.zzpp) {
            this.zzaSK--;
            if (this.zzaSK == 0) {
                zzhE(this.zzaSJ);
            }
        }
    }

    boolean zzhE(int i) {
        return stopSelfResult(i);
    }

    public abstract void zzm(Intent intent);

    protected abstract Intent zzz(Intent intent);
}
