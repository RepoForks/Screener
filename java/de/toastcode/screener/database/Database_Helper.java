package de.toastcode.screener.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.toastcode.screener.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Database_Helper extends SQLiteOpenHelper {
    private static String DB_DIR = "/data/data/de.toastcode.screener/databases/";
    private static String DB_NAME = "devices.scr";
    private static String DB_PATH = (DB_DIR + DB_NAME);
    private static String OLD_DB_PATH = (DB_DIR + "old_" + DB_NAME);
    private boolean createDatabase = false;
    private Context mContext;
    private boolean upgradeDatabase = false;

    public Database_Helper(Context context) {
        super(context, DB_NAME, null, context.getResources().getInteger(R.integer.databaseVersion));
        this.mContext = context;
        DB_PATH = this.mContext.getDatabasePath(DB_NAME).getAbsolutePath();
    }

    public void initializeDataBase() {
        getWritableDatabase();
        if (!this.createDatabase && this.upgradeDatabase) {
            try {
                FileHelper.copyFile(DB_PATH, OLD_DB_PATH);
                copyDataBase();
                SQLiteDatabase old_db = SQLiteDatabase.openDatabase(OLD_DB_PATH, null, 0);
                SQLiteDatabase.openDatabase(DB_PATH, null, 0);
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException {
        close();
        FileHelper.copyFile(this.mContext.getAssets().open(DB_NAME), new FileOutputStream(DB_PATH));
        getWritableDatabase().close();
    }

    public void onCreate(SQLiteDatabase db) {
        this.createDatabase = true;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.upgradeDatabase = true;
        try {
            for (String statement : FileHelper.parseSqlFile(this.mContext.getResources().getAssets().open("devices.scr"))) {
                db.execSQL(statement);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void _copydatabase() throws IOException {
        if (!new File(DB_PATH).exists()) {
            OutputStream myOutput = new FileOutputStream(DB_PATH);
            byte[] buffer = new byte[1024];
            InputStream myInput = this.mContext.getAssets().open(DB_NAME);
            while (true) {
                int length = myInput.read(buffer);
                if (length > 0) {
                    myOutput.write(buffer, 0, length);
                } else {
                    myInput.close();
                    myOutput.flush();
                    myOutput.close();
                    return;
                }
            }
        }
    }
}
