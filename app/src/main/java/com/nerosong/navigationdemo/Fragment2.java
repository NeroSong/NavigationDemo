package com.nerosong.navigationdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nerosong.navigationdemo.util.CircleProgress;

public class Fragment2 extends Fragment implements View.OnClickListener {

    CircleProgress mCircleProgress1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        mCircleProgress1 = (CircleProgress) view.findViewById(R.id.circle_progress_bar1);
        mCircleProgress1.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        mCircleProgress1.setValue(3580);
    }
}
