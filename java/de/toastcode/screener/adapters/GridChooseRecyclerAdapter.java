package de.toastcode.screener.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import de.toastcode.screener.ObjectSerializer;
import de.toastcode.screener.R;
import de.toastcode.screener.activities.Device_DownloadActivity;
import de.toastcode.screener.layouts.Company;
import de.toastcode.screener.layouts.Devices;
import de.toastcode.screener.layouts.FrameSquareImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import me.zhanghai.android.materialprogressbar.BuildConfig;

public class GridChooseRecyclerAdapter extends Adapter<ViewHolder> {
    boolean add = false;
    ArrayList<String> del_frames = new ArrayList();
    ArrayList<Devices> dev;
    boolean deviceToDelete;
    int device_count = 0;
    boolean isWatch;
    Activity mActivity;
    ArrayList<Company> mCompanyList = new ArrayList();
    SortedMap<String, ArrayList<Devices>> mDeviceList = new TreeMap();
    int mIndex;
    String mTablequery;
    ArrayList<String> sel_frames;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public String device;
        public String device_image;
        public FrameSquareImageView image = null;
        public ImageView iv_check = null;
        public ImageView iv_grey = null;
        public ImageView iv_thumb = null;
        public View mView;
        public LinearLayout name_ll = null;
        public int position;
        public TextView tv_device_name = null;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            this.iv_thumb = (ImageView) itemView.findViewById(R.id.picture);
            this.iv_check = (ImageView) itemView.findViewById(R.id.iv_check);
            this.iv_grey = (ImageView) itemView.findViewById(R.id.iv_grey);
            this.tv_device_name = (TextView) itemView.findViewById(R.id.device);
            this.image = (FrameSquareImageView) itemView.findViewById(R.id.picture);
            this.name_ll = (LinearLayout) itemView.findViewById(R.id.name_ll);
        }
    }

    public GridChooseRecyclerAdapter(Activity activity, ArrayList<Company> companies, String value, int index) {
        this.mActivity = activity;
        this.mCompanyList = companies;
        this.mTablequery = value;
        this.mIndex = index;
    }

    public GridChooseRecyclerAdapter(Activity activity, SortedMap<String, ArrayList<Devices>> devices, int index) {
        this.mActivity = activity;
        this.mDeviceList = devices;
        this.mIndex = index;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.position = position;
        holder.device = getDeviceValue(position);
        holder.device_image = getImageValue(position);
        holder.tv_device_name.setText(getDeviceValue(position));
        holder.mView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (GridChooseRecyclerAdapter.this.mIndex == 0) {
                    Bundle b = new Bundle();
                    Intent i = new Intent(GridChooseRecyclerAdapter.this.mActivity, Device_DownloadActivity.class);
                    b.putString("company", holder.device);
                    i.putExtras(b);
                    GridChooseRecyclerAdapter.this.mActivity.overridePendingTransition(R.anim.push_up_in, R.anim.nichts);
                    GridChooseRecyclerAdapter.this.mActivity.startActivity(i);
                    return;
                }
                GridChooseRecyclerAdapter.this.loadPreferences("count", 2);
                int i2;
                if (GridChooseRecyclerAdapter.this.getDownloaded(position)) {
                    GridChooseRecyclerAdapter.this.dev = (ArrayList) GridChooseRecyclerAdapter.this.mDeviceList.get(String.valueOf(GridChooseRecyclerAdapter.this.mDeviceList.keySet().toArray()[position]));
                    for (i2 = 0; i2 < GridChooseRecyclerAdapter.this.dev.size(); i2++) {
                        if (GridChooseRecyclerAdapter.this.del_frames.contains(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_name())) {
                            holder.iv_check.setVisibility(8);
                            GridChooseRecyclerAdapter.this.del_frames.remove(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_name());
                            GridChooseRecyclerAdapter.this.del_frames.remove(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_thumb());
                            GridChooseRecyclerAdapter.this.del_frames.remove(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_glare());
                            GridChooseRecyclerAdapter.this.del_frames.remove(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_shadow());
                        } else {
                            holder.iv_check.setVisibility(0);
                            GridChooseRecyclerAdapter.this.del_frames.add(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_name());
                            GridChooseRecyclerAdapter.this.del_frames.add(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_thumb());
                            GridChooseRecyclerAdapter.this.del_frames.add(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_glare());
                            GridChooseRecyclerAdapter.this.del_frames.add(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_shadow());
                        }
                    }
                    GridChooseRecyclerAdapter.this.savePreferences("delete", GridChooseRecyclerAdapter.this.del_frames);
                    return;
                }
                GridChooseRecyclerAdapter.this.dev = (ArrayList) GridChooseRecyclerAdapter.this.mDeviceList.get(String.valueOf(GridChooseRecyclerAdapter.this.mDeviceList.keySet().toArray()[position]));
                for (i2 = 0; i2 < GridChooseRecyclerAdapter.this.dev.size(); i2++) {
                    if (GridChooseRecyclerAdapter.this.sel_frames.contains(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_name())) {
                        holder.iv_check.setVisibility(8);
                        GridChooseRecyclerAdapter.this.sel_frames.remove(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_name());
                        GridChooseRecyclerAdapter.this.sel_frames.remove(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_thumb());
                        GridChooseRecyclerAdapter.this.sel_frames.remove(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_glare());
                        GridChooseRecyclerAdapter.this.sel_frames.remove(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_shadow());
                        GridChooseRecyclerAdapter.this.add = false;
                    } else {
                        holder.iv_check.setVisibility(0);
                        GridChooseRecyclerAdapter.this.sel_frames.add(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_name());
                        GridChooseRecyclerAdapter.this.sel_frames.add(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_thumb());
                        GridChooseRecyclerAdapter.this.sel_frames.add(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_glare());
                        GridChooseRecyclerAdapter.this.sel_frames.add(((Devices) GridChooseRecyclerAdapter.this.dev.get(i2)).getmFile_shadow());
                        GridChooseRecyclerAdapter.this.add = true;
                    }
                }
                GridChooseRecyclerAdapter gridChooseRecyclerAdapter;
                if (GridChooseRecyclerAdapter.this.add) {
                    gridChooseRecyclerAdapter = GridChooseRecyclerAdapter.this;
                    gridChooseRecyclerAdapter.device_count++;
                } else {
                    gridChooseRecyclerAdapter = GridChooseRecyclerAdapter.this;
                    gridChooseRecyclerAdapter.device_count--;
                }
                GridChooseRecyclerAdapter.this.savePreferences("selected", GridChooseRecyclerAdapter.this.sel_frames);
                if (GridChooseRecyclerAdapter.this.device_count < 0) {
                    GridChooseRecyclerAdapter.this.device_count = 0;
                }
                GridChooseRecyclerAdapter.this.saveStringPreferences("count", GridChooseRecyclerAdapter.this.device_count + BuildConfig.FLAVOR);
            }
        });
        loadPreferences("selected", 1);
        if (this.mIndex == 1) {
            if (getDownloaded(position)) {
                holder.iv_grey.setVisibility(8);
                if (isConains(position, this.del_frames)) {
                    holder.iv_check.setVisibility(0);
                } else {
                    holder.iv_check.setVisibility(8);
                }
                saveBooleanPreferences("deviceToDelete", true);
                this.deviceToDelete = true;
            } else {
                holder.iv_grey.setVisibility(0);
                if (isConains(position, this.sel_frames)) {
                    holder.iv_check.setVisibility(0);
                } else {
                    holder.iv_check.setVisibility(8);
                }
                if (!this.deviceToDelete) {
                    saveBooleanPreferences("deviceToDelete", false);
                }
            }
        }
        if (this.isWatch) {
            Glide.with(holder.iv_thumb.getContext()).load(holder.device_image).placeholder(R.drawable.no_settings).error(R.drawable.broken_image_w).crossFade().into(holder.iv_thumb);
        } else {
            Glide.with(holder.iv_thumb.getContext()).load(holder.device_image).placeholder(R.drawable.no_settings).error(R.drawable.broken_image).crossFade().into(holder.iv_thumb);
        }
    }

    public int getItemCount() {
        if (this.mIndex == 1) {
            return this.mDeviceList.keySet().size();
        }
        return this.mCompanyList.size();
    }

    public boolean getDownloaded(int position) {
        this.dev = (ArrayList) this.mDeviceList.get(String.valueOf(this.mDeviceList.keySet().toArray()[position]));
        return ((Devices) this.dev.get(0)).getmDevice_downloaded();
    }

    public boolean isConains(int position, ArrayList<String> arrString) {
        this.dev = (ArrayList) this.mDeviceList.get(String.valueOf(this.mDeviceList.keySet().toArray()[position]));
        return arrString.contains(((Devices) this.dev.get(0)).getmFile_name());
    }

    public String getImageValue(int position) {
        if (this.mIndex == 1) {
            String server_url = "http://toastco.de/screener/images/";
            String watches = "watches/";
            String flat = "flat/";
            String ending = ".png";
            String furl = BuildConfig.FLAVOR;
            this.dev = (ArrayList) this.mDeviceList.get(String.valueOf(this.mDeviceList.keySet().toArray()[position]));
            int flat_pos = 0;
            for (int i = 0; i < this.dev.size(); i++) {
                if (((Devices) this.dev.get(i)).getmFile_thumb().contains("flat")) {
                    flat_pos = i;
                    break;
                }
            }
            String id = ((Devices) this.dev.get(flat_pos)).getmFile_thumb();
            if (id.contains("watch")) {
                this.isWatch = true;
                return server_url + watches + id + ending;
            }
            this.isWatch = false;
            return server_url + flat + id + ending;
        }
        if (((Company) this.mCompanyList.get(position)).getmThumbId().contains("watch")) {
            this.isWatch = true;
        } else {
            this.isWatch = false;
        }
        return ((Company) this.mCompanyList.get(position)).getmThumbId();
    }

    public String getDeviceValue(int position) {
        if (this.mIndex == 1) {
            return String.valueOf(this.mDeviceList.keySet().toArray()[position]);
        }
        return ((Company) this.mCompanyList.get(position)).getmCompany();
    }

    private void savePreferences(String key, ArrayList<String> value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this.mActivity).edit();
        try {
            editor.putString(key, ObjectSerializer.serialize(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    private void saveBooleanPreferences(String key, boolean value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this.mActivity).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void saveStringPreferences(String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this.mActivity).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void loadPreferences(String KEY, int index) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mActivity);
        if (index == 1) {
            try {
                this.sel_frames = (ArrayList) ObjectSerializer.deserialize(sharedPreferences.getString(KEY, ObjectSerializer.serialize(new ArrayList())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (index == 2) {
            this.device_count = Integer.parseInt(sharedPreferences.getString("count", "0"));
        }
    }
}
