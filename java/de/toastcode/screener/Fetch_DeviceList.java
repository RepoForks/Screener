package de.toastcode.screener;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import me.zhanghai.android.materialprogressbar.BuildConfig;
import org.json.JSONObject;

public class Fetch_DeviceList {
    private static final String URL = "http://www.toastco.de/screener/select_all.php";

    public static JSONObject getJSON(Context context) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(((HttpURLConnection) new URL(URL).openConnection()).getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String str = BuildConfig.FLAVOR;
            while (true) {
                str = reader.readLine();
                if (str == null) {
                    break;
                }
                json.append(str).append("\n");
            }
            reader.close();
            JSONObject data = new JSONObject(json.toString());
            if (data.getJSONArray("devices").getString(0).contains("null")) {
                return null;
            }
            return data;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
