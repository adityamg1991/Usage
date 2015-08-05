package com.example.aditya.usage.Adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aditya.usage.Data.ProcessData;
import com.example.aditya.usage.R;
import com.example.aditya.usage.Utilities.Constants;
import com.example.aditya.usage.Utilities.UsageApplication;

import java.util.ArrayList;

/**
 * Created by aditya on 01/07/15.
 */
public class ProcessAdapter extends ArrayAdapter<ProcessData> {

    LayoutInflater inflater;
    ArrayList<ProcessData> data;
    Context con;
    private int headerSystemApps = -1;
    private int headerUnknownApps = -1;

    private final int VIEW_TYPE_NORMAL = 0;
    private final int VIEW_TYPE_HEADER_ONE = 1;
    private final int VIEW_TYPE_HEADER_TWO = 2;


    public void setHeaderSystemApps(int headerSystemApps) {
        this.headerSystemApps = headerSystemApps;
    }

    public void setHeaderUnknownApps(int headerUnknownApps) {
        this.headerUnknownApps = headerUnknownApps;
    }

    public ProcessAdapter(Context context, int resource, ArrayList<ProcessData> objects) {
        super(context, resource, objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = objects;
        con = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(null == convertView) {

            int viewType = getItemViewType(position);
            if(viewType == VIEW_TYPE_HEADER_ONE) {

                convertView = inflater.inflate(R.layout.process_list_item_header_one, parent, false);
            } else if(viewType == VIEW_TYPE_HEADER_TWO) {

                convertView = inflater.inflate(R.layout.process_list_item_header_two, parent, false);
            } else if (viewType == VIEW_TYPE_NORMAL){

                convertView = inflater.inflate(R.layout.process_list_item, parent, false);
                holder = new ViewHolder();
                holder.tvProcessName = (TextView) convertView.findViewById(R.id.tv_process_name);
                holder.tvProcessMemoryUsage = (TextView) convertView.findViewById(R.id.tv_process_memory_usage);
                holder.ivProcessImage = (ImageView) convertView.findViewById(R.id.iv_process_image);
                convertView.setTag(holder);
            }
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        if(null != holder) {

            ProcessData unitProcessData =  data.get(position);

            String appName = unitProcessData.getAppLabel();
            if(!TextUtils.isEmpty(appName)) {
                holder.tvProcessName.setText(unitProcessData.getAppLabel());
            } else {
                holder.tvProcessName.setText(unitProcessData.getPackageName());
            }

            holder.tvProcessMemoryUsage.setText(String.valueOf(unitProcessData.getMemInMB()) + " Mb");

            Drawable drawable = unitProcessData.getIconDrawable();
            if(null != drawable) {

                holder.ivProcessImage.setImageDrawable(drawable);
            } else {

                String setAppName = holder.tvProcessName.getText().toString();
                if(TextUtils.isEmpty(setAppName.trim())) {
                    Drawable transparentDrawable = new ColorDrawable(0x00ffffff);
                    holder.ivProcessImage.setImageDrawable(transparentDrawable);
                } else {
                    Drawable db = UsageApplication.getRoundedBitmapOfDesiredColor(con,
                            "" + setAppName.charAt(0), Constants.APP_THEME_HASH_CODE);
                    holder.ivProcessImage.setImageDrawable(db);
                }
            }
        }

        return convertView;
    }


    @Override
    public int getItemViewType(int position) {
        if(position == headerUnknownApps) {
            return VIEW_TYPE_HEADER_ONE;
        } else if (position == headerSystemApps) {
            return VIEW_TYPE_HEADER_TWO;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    private static class ViewHolder {
        TextView tvProcessName;
        TextView tvProcessMemoryUsage;
        ImageView ivProcessImage;
    }
}
