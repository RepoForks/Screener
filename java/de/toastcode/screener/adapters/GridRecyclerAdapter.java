package de.toastcode.screener.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import de.sr.library.Crypy;
import de.toastcode.screener.R;
import de.toastcode.screener.activities.Device_Detail_Activity;
import de.toastcode.screener.layouts.FrameSquareImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class GridRecyclerAdapter extends Adapter<ViewHolder> {
    ArrayList<String> copyDevice_name = new ArrayList();
    ArrayList<String> copyThumb_image = new ArrayList();
    Crypy crypy = new Crypy();
    ArrayList<String> device_name;
    File imgFile;
    Activity mActivity;
    String path = null;
    String s = null;
    String tablequery;
    ArrayList<String> thumb_image;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public String device;
        public String device_image;
        public FrameSquareImageView image = null;
        public ImageView iv_thumb = null;
        public View mView;
        public LinearLayout name_ll = null;
        public int position;
        public TextView tv_device_name = null;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            this.iv_thumb = (ImageView) itemView.findViewById(R.id.picture);
            this.tv_device_name = (TextView) itemView.findViewById(R.id.device);
            this.image = (FrameSquareImageView) itemView.findViewById(R.id.picture);
            this.name_ll = (LinearLayout) itemView.findViewById(R.id.name_ll);
        }
    }

    public GridRecyclerAdapter(Activity mActivity, ArrayList<String> device_name, ArrayList<String> thumb_image, String table) {
        this.mActivity = mActivity;
        this.device_name = device_name;
        this.thumb_image = thumb_image;
        this.tablequery = table;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false));
        this.copyDevice_name.clear();
        this.copyThumb_image.clear();
        this.copyDevice_name.addAll(this.device_name);
        this.copyThumb_image.addAll(this.thumb_image);
        return viewHolder;
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.position = position;
        holder.device = getDeviceValue(position);
        holder.device_image = getImageValue(position);
        holder.mView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle b = new Bundle();
                Intent i = new Intent(GridRecyclerAdapter.this.mActivity, Device_Detail_Activity.class);
                b.putInt("position", holder.position);
                b.putString("table", GridRecyclerAdapter.this.tablequery);
                b.putString("device", holder.device_image);
                i.putExtras(b);
                GridRecyclerAdapter.this.mActivity.startActivity(i);
                GridRecyclerAdapter.this.mActivity.overridePendingTransition(R.anim.push_up_in, R.anim.nichts);
            }
        });
        this.s = this.crypy.encrypt(((String) this.thumb_image.get(position)) + ".png");
        holder.tv_device_name.setText(getDeviceValue(position));
        this.path = Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.toastcode.screener/" + this.s;
        this.imgFile = new File(this.path);
        if (this.imgFile.exists()) {
            Glide.with(holder.iv_thumb.getContext()).load(this.imgFile.getAbsolutePath()).placeholder(R.drawable.no_settings).crossFade().into(holder.iv_thumb);
            return;
        }
        Glide.with(holder.iv_thumb.getContext()).load(Integer.valueOf(R.drawable.no_frame)).placeholder(R.drawable.no_frame).crossFade().into(holder.iv_thumb);
        holder.image.setBackgroundColor(ContextCompat.getColor(this.mActivity, R.color.grey_300));
        holder.name_ll.setBackgroundColor(ContextCompat.getColor(this.mActivity, R.color.grey_300));
    }

    public int getItemCount() {
        return this.device_name.size();
    }

    public String getImageValue(int position) {
        return (String) this.thumb_image.get(position);
    }

    public String getDeviceValue(int position) {
        return (String) this.device_name.get(position);
    }

    public void filter(String text) {
        if (text.isEmpty()) {
            this.device_name.clear();
            this.thumb_image.clear();
            this.device_name.addAll(this.copyDevice_name);
            this.thumb_image.addAll(this.copyThumb_image);
        } else {
            int count = 0;
            ArrayList<String> resultDevice = new ArrayList();
            ArrayList<String> resultThumb = new ArrayList();
            text = text.toLowerCase();
            Iterator it = this.copyDevice_name.iterator();
            while (it.hasNext()) {
                String dev = (String) it.next();
                if (dev.toLowerCase().contains(text)) {
                    resultDevice.add(dev);
                    resultThumb.add(this.copyThumb_image.get(count));
                }
                count++;
            }
            this.device_name.clear();
            this.thumb_image.clear();
            this.device_name.addAll(resultDevice);
            this.thumb_image.addAll(resultThumb);
        }
        notifyDataSetChanged();
    }
}
