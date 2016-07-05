package de.toastcode.screener.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class FileHelper {
    public static void copyFile(java.io.FileInputStream r7, java.io.FileOutputStream r8) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x001b in list [B:7:0x0018]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:286)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:173)
*/
        /*
        r1 = r7.getChannel();
        r6 = r8.getChannel();
        r2 = 0;
        r4 = r1.size();	 Catch:{ all -> 0x0023, all -> 0x002f }
        r1.transferTo(r2, r4, r6);	 Catch:{ all -> 0x0023, all -> 0x002f }
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r1.close();	 Catch:{ all -> 0x001c }
    L_0x0016:
        if (r6 == 0) goto L_0x001b;
    L_0x0018:
        r6.close();
    L_0x001b:
        return;
    L_0x001c:
        r0 = move-exception;
        if (r6 == 0) goto L_0x0022;
    L_0x001f:
        r6.close();
    L_0x0022:
        throw r0;
    L_0x0023:
        r0 = move-exception;
        if (r1 == 0) goto L_0x0029;
    L_0x0026:
        r1.close();	 Catch:{ all -> 0x0023, all -> 0x002f }
    L_0x0029:
        if (r6 == 0) goto L_0x002e;
    L_0x002b:
        r6.close();
    L_0x002e:
        throw r0;
    L_0x002f:
        r0 = move-exception;
        if (r6 == 0) goto L_0x0035;
    L_0x0032:
        r6.close();
    L_0x0035:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.toastcode.screener.database.FileHelper.copyFile(java.io.FileInputStream, java.io.FileOutputStream):void");
    }

    public static void copyFile(InputStream fromFile, OutputStream toFile) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            try {
                int length = fromFile.read(buffer);
                if (length <= 0) {
                    break;
                }
                toFile.write(buffer, 0, length);
            } catch (Throwable th) {
                if (fromFile != null) {
                    fromFile.close();
                }
            }
        }
        if (toFile != null) {
            try {
                toFile.flush();
                toFile.close();
            } catch (Throwable th2) {
                if (fromFile != null) {
                    fromFile.close();
                }
            }
        }
        if (fromFile != null) {
            fromFile.close();
        }
    }

    public static void copyFile(String fromFile, String toFile) throws IOException {
        copyFile(new FileInputStream(fromFile), new FileOutputStream(toFile));
    }

    public static void copyFile(File fromFile, File toFile) throws IOException {
        copyFile(new FileInputStream(fromFile), new FileOutputStream(toFile));
    }

    public static String[] parseSqlFile(String sqlFile) throws IOException {
        return parseSqlFile(new BufferedReader(new FileReader(sqlFile)));
    }

    public static String[] parseSqlFile(InputStream sqlFile) throws IOException {
        return parseSqlFile(new BufferedReader(new InputStreamReader(sqlFile)));
    }

    public static String[] parseSqlFile(Reader sqlFile) throws IOException {
        return parseSqlFile(new BufferedReader(sqlFile));
    }

    public static String[] parseSqlFile(BufferedReader sqlFile) throws IOException {
        StringBuilder sql = new StringBuilder();
        String multiLineComment = null;
        while (true) {
            String line = sqlFile.readLine();
            if (line != null) {
                line = line.trim();
                if (multiLineComment == null) {
                    if (line.startsWith("/*")) {
                        if (!line.endsWith("}")) {
                            multiLineComment = "/*";
                        }
                    } else if (line.startsWith("{")) {
                        if (!line.endsWith("}")) {
                            multiLineComment = "{";
                        }
                    } else if (!(line.startsWith("--") || line.equals(BuildConfig.FLAVOR))) {
                        sql.append(line);
                    }
                } else if (multiLineComment.equals("/*")) {
                    if (line.endsWith("*/")) {
                        multiLineComment = null;
                    }
                } else if (multiLineComment.equals("{") && line.endsWith("}")) {
                    multiLineComment = null;
                }
            } else {
                sqlFile.close();
                return sql.toString().split(";");
            }
        }
    }
}
