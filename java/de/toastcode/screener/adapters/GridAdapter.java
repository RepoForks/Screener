package de.toastcode.screener.adapters;

import android.app.Activity;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import de.sr.library.Crypy;
import de.toastcode.screener.R;
import de.toastcode.screener.layouts.FrameSquareImageView;
import java.io.File;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    Crypy crypy = new Crypy();
    ArrayList<String> device_name;
    File imgFile;
    Activity mActivity;
    ArrayList<String> thumb_image;

    public GridAdapter(Activity mActivity, ArrayList<String> device_name, ArrayList<String> thumb_image) {
        this.mActivity = mActivity;
        this.device_name = device_name;
        this.thumb_image = thumb_image;
    }

    public int getCount() {
        return this.device_name.size();
    }

    public Object getItem(int position) {
        return this.device_name.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        TextView tv_device_name;
        ImageView iv_thumb;
        LayoutInflater inflater = this.mActivity.getLayoutInflater();
        if (null == null) {
            row = inflater.inflate(R.layout.grid_item, parent, false);
        } else {
            row = convertView;
        }
        if (null == null) {
            tv_device_name = (TextView) row.findViewById(R.id.device);
        } else {
            tv_device_name = (TextView) row;
        }
        if (null == null) {
            iv_thumb = (ImageView) row.findViewById(R.id.picture);
        } else {
            iv_thumb = (ImageView) row;
        }
        String s = this.crypy.encrypt(((String) this.thumb_image.get(position)) + ".png");
        tv_device_name.setText((CharSequence) this.device_name.get(position));
        this.imgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + s);
        if (this.imgFile.exists()) {
            Glide.with(this.mActivity).load(this.imgFile.getAbsolutePath()).placeholder(R.drawable.no_settings).crossFade().into(iv_thumb);
        } else {
            Glide.with(this.mActivity).load(Integer.valueOf(R.drawable.no_frame)).placeholder(R.drawable.no_frame).crossFade().into(iv_thumb);
            LinearLayout name_ll = (LinearLayout) row.findViewById(R.id.name_ll);
            ((FrameSquareImageView) row.findViewById(R.id.picture)).setBackgroundColor(ContextCompat.getColor(this.mActivity, R.color.grey_300));
            name_ll.setBackgroundColor(ContextCompat.getColor(this.mActivity, R.color.grey_300));
        }
        return row;
    }

    public boolean isEnabled(int position) {
        this.imgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + this.crypy.encrypt(((String) this.thumb_image.get(position)) + ".png"));
        return this.imgFile.exists();
    }
}
