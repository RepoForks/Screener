package de.toastcode.screener.activities;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.sr.library.Crypy;
import de.toastcode.screener.R;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Download_Activity extends AppCompatActivity {
    int count = 0;
    String[] devices;
    TextView downloading;
    FloatingActionButton fab_cancel;
    int fab_click = 0;
    ImageView imgAni;
    private downloadFiles_Async mTask;
    ProgressBar pBar;
    TextView percent;
    int progress = 0;
    ArrayList<String> sel_devices;

    public class downloadFiles_Async extends AsyncTask<String, Integer, String> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... devices) {
            do {
                try {
                    if (Download_Activity.this.progress == 100) {
                        break;
                    }
                    for (int i = 0; i < devices.length; i++) {
                        URL url = null;
                        if (Download_Activity.exists("http://www.toastco.de/screener/images/flat/" + devices[i] + ".png")) {
                            url = new URL("http://www.toastco.de/screener/images/flat/" + devices[i] + ".png");
                        } else if (Download_Activity.exists("http://www.toastco.de/screener/images/dreid/" + devices[i] + ".png")) {
                            url = new URL("http://www.toastco.de/screener/images/dreid/" + devices[i] + ".png");
                        } else if (Download_Activity.exists("http://www.toastco.de/screener/images/minimals/" + devices[i] + ".png")) {
                            url = new URL("http://www.toastco.de/screener/images/minimals/" + devices[i] + ".png");
                        } else if (Download_Activity.exists("http://www.toastco.de/screener/images/watches/" + devices[i] + ".png")) {
                            url = new URL("http://www.toastco.de/screener/images/watches/" + devices[i] + ".png");
                        }
                        URLConnection conection = url.openConnection();
                        conection.connect();
                        int lenghtOfFile = conection.getContentLength();
                        InputStream input = new BufferedInputStream(url.openStream(), 8192);
                        System.out.println("Data::" + devices[i]);
                        File mFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/");
                        if (!mFolder.exists()) {
                            mFolder.mkdir();
                        }
                        OutputStream output = new FileOutputStream(new File(mFolder.getAbsolutePath(), new Crypy().encrypt(devices[i] + ".png")));
                        byte[] data = new byte[1024];
                        while (true) {
                            Download_Activity download_Activity = Download_Activity.this;
                            int read = input.read(data);
                            download_Activity.count = read;
                            if (read == -1) {
                                break;
                            }
                            output.write(data, 0, Download_Activity.this.count);
                        }
                        output.flush();
                        output.close();
                        input.close();
                        Download_Activity.this.progress = (i * 100) / devices.length;
                        publishProgress(new Integer[]{Integer.valueOf(Download_Activity.this.progress)});
                        Download_Activity.this.count = i;
                        if (isCancelled()) {
                            break;
                        }
                    }
                    if (isCancelled()) {
                        break;
                    }
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            } while (Download_Activity.this.count + 1 != devices.length);
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(new Integer[0]);
            Download_Activity.this.fab_cancel.setVisibility(0);
            Download_Activity.this.downloading.setText(Download_Activity.this.getResources().getString(R.string.download_in_progress));
            Download_Activity.this.pBar.setProgress(progress[0].intValue());
            Download_Activity.this.percent.setText(progress[0] + "%");
        }

        protected void onPostExecute(String feed) {
            Download_Activity.this.pBar.setProgress(100);
            Download_Activity.this.percent.setText("100%");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Download_Activity.this.finish();
                }
            }, 1000);
        }
    }

    public static boolean exists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            if (con.getResponseCode() == 200) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(1);
        } else {
            setRequestedOrientation(0);
        }
        this.sel_devices = getIntent().getExtras().getStringArrayList("devices");
        this.imgAni = (ImageView) findViewById(R.id.img_ani);
        this.percent = (TextView) findViewById(R.id.percent);
        this.downloading = (TextView) findViewById(R.id.download_text);
        this.pBar = (ProgressBar) findViewById(R.id.progress);
        this.fab_cancel = (FloatingActionButton) findViewById(R.id.fab_cancel);
        this.fab_cancel.setVisibility(4);
        this.imgAni.setBackgroundResource(R.drawable.download_ani);
        this.pBar.getProgressDrawable().setColorFilter(-1, Mode.SRC_IN);
        this.pBar.setMax(100);
        this.pBar.setIndeterminate(false);
        this.fab_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Download_Activity.this.fab_click == 0) {
                    Download_Activity.this.fab_click = 1;
                    Download_Activity.this.mTask.cancel(true);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (int j = 0; j < Download_Activity.this.count + 1; j++) {
                                File mFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/");
                                if (!mFolder.exists()) {
                                    mFolder.mkdir();
                                }
                                Crypy crypy = new Crypy();
                                System.out.println(Download_Activity.this.devices[j]);
                                new File(mFolder.getAbsolutePath(), crypy.encrypt(Download_Activity.this.devices[j] + ".png")).delete();
                            }
                            Download_Activity.this.finish();
                        }
                    }, 1500);
                }
            }
        });
        ((AnimationDrawable) this.imgAni.getBackground()).start();
        this.devices = (String[]) this.sel_devices.toArray(new String[this.sel_devices.size()]);
        this.mTask = (downloadFiles_Async) new downloadFiles_Async().execute(this.devices);
    }
}
