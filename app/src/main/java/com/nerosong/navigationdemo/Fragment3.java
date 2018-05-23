package com.nerosong.navigationdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Fragment3 extends Fragment {

    ListView listView;
    List<String> listInfo = new ArrayList<String>();
    List<String> listData = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);


        initData();

        listView = (ListView) view.findViewById(R.id.listView_user);
        UserInfoListAdapter listAdapter = new UserInfoListAdapter(MainActivity.mMainAcitvity, listInfo, listData);
        listView.setAdapter(listAdapter);

        initListItemOnClick();
        return view;
    }

    private void initData() {
        String[] info, data;
        info = getResources().getStringArray(R.array.myarray_info);
        data = getResources().getStringArray(R.array.myarray_item);


        for (int i = 0; i < info.length; i++) {
            listInfo.add(info[i]);
            listData.add(data[i]);
        }

    }

    private void initListItemOnClick() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textView = view.findViewById(R.id.textView_listdata);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mMainAcitvity);
                builder.setTitle("提示");
                builder.setMessage("你点击了" + textView.getText());
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                builder.show();

            }

        });
    }


}
