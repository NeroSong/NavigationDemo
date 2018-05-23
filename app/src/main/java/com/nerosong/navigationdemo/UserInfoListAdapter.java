package com.nerosong.navigationdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

public class UserInfoListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mListInfo;
    private List<String> mListData;

    private LayoutInflater mLayoutInflater;

    public static class ViewHolder {
        TextView textViewInfo;
        TextView textViewItem;
    }


    public UserInfoListAdapter(Context context, List<String> mListInfo, List<String> mListData) {
        this.mContext = context;
        this.mListInfo = mListInfo;
        this.mListData = mListData;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mListInfo == null | mListData == null) {
            return count;
        } else {
            return mListInfo.size();
        }
    }

    //i即为position
    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.list_user_info, null);
            holder.textViewInfo = view.findViewById(R.id.textView_listinfo);
            holder.textViewItem = view.findViewById(R.id.textView_listdata);
            //这个tag用的好，方便的保存了状态
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.textViewInfo.setText(mListInfo.get(i));
        holder.textViewItem.setText(mListData.get(i));

        return view;
    }

    public void notifyDataChange(ListView listView, int position) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();
        //仅在可视范围内更新，看不到的滑动后会由getView自动更新
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = listView.getChildAt(position - firstVisiblePosition);
            getView(position,view,listView);
        }
    }


}
