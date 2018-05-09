package mao.com.nerdlauncher.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mao.com.nerdlauncher.R;

public class NerdLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<ResolveInfo> activities;//每个应用启动Activity的信息

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nerd_launcher, container,false);
        mRecyclerView  = view.findViewById(R.id.app_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        //根据启动条件获取Activity
        Intent startIntent =new Intent(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PackageManager pm=getActivity().getPackageManager();
        activities = pm.queryIntentActivities(startIntent, 0);
        //对activity标签排序
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo resolveInfo, ResolveInfo t1) {
                return String.CASE_INSENSITIVE_ORDER.compare(
                        resolveInfo.loadLabel(pm).toString(),t1.loadLabel(pm).toString()
                );
            }
        });
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
        Log.e("毛麒添", "Found " + activities.size() + " activities.");
    }

    public static NerdLauncherFragment newInstance() {

        Bundle args = new Bundle();

        NerdLauncherFragment fragment = new NerdLauncherFragment();
        fragment.setArguments(args);
        return fragment;
    }


    class Viewholder extends RecyclerView.ViewHolder{

        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;

        public Viewholder(View itemView) {
            super(itemView);
            mNameTextView= (TextView) itemView;
        }

        public void bindActivitys(ResolveInfo resolveInfo){
            this.mResolveInfo=resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();
            mNameTextView.setText(appName);
        }
    }

    class ActivityAdapter extends RecyclerView.Adapter<Viewholder>{

        private List<ResolveInfo> activitiesList;

        public ActivityAdapter (List<ResolveInfo> activitiesList){
            this.activitiesList=activitiesList;
        }

        @Override
        public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new Viewholder(view);
        }

        @Override
        public void onBindViewHolder(Viewholder holder, int position) {
               holder.bindActivitys(activitiesList.get(position));
        }

        @Override
        public int getItemCount() {
            return activitiesList.size();
        }
    }
}
