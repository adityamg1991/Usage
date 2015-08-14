package com.coffeemug.usage.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.coffeemug.usage.R;

/**
 * Created by aditya on 29/07/15.
 */
public class PhoneFactFragment extends UsageBaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.phone_facts_fragment, container, false);
    }

    public static PhoneFactFragment getInstance() {

        PhoneFactFragment fragment = new PhoneFactFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*
        Most used app
        App used most frequently

        */

        RecyclerView rvList = (RecyclerView) getActivity().findViewById(R.id.rv_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvList.setLayoutManager(gridLayoutManager);
        PhoneFactAdapter phoneFactAdapter = new PhoneFactAdapter();
        rvList.setAdapter(phoneFactAdapter);
    }


    private class PhoneFactAdapter extends RecyclerView.Adapter<PhoneFactAdapterHolder> {

        public PhoneFactAdapter() {

        }

        @Override
        public PhoneFactAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_facts_list_item, parent, false);

            PhoneFactAdapterHolder vh = new PhoneFactAdapterHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(PhoneFactAdapterHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }


    private class PhoneFactAdapterHolder extends RecyclerView.ViewHolder{

        ImageView ivAppLogo;
        TextView tvPhoneFact;
        TextView tvAppName;

        public PhoneFactAdapterHolder (View v) {
            super(v);

            ivAppLogo = (ImageView) v.findViewById(R.id.iv_app_image);
            tvPhoneFact = (TextView) v.findViewById(R.id.tv_phone_fact_name);
            tvAppName = (TextView) v.findViewById(R.id.tv_app_name);
        }
    }
}
