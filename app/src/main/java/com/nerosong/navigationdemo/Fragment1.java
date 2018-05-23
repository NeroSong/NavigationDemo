package com.nerosong.navigationdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDLocationListener;

import com.baidu.mapapi.model.LatLng;


public class Fragment1 extends Fragment {


    private MapView mMapView = null;
    public BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    public BDLocationListener mListener = new MyLocationListener();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SDKInitializer.initialize(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment1, container, false);
        mMapView = (MapView) view.findViewById(R.id.bmapView);

        initMap();

        return view;
    }

    private void initMap() {

        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);//屏蔽百度地图的放大按钮
        // 获取百度地图兵进行当前位置定位---------------------
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
        mLocationClient = new LocationClient(MainActivity.mMainAcitvity);
        mLocationClient.registerLocationListener(mListener);
        mBaiduMap.setMyLocationConfigeration(
                new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
        initLocation();
    }


    //设置定位的模式 坐标，需不需要开GPS等
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        // option.setScanSpan(0);
        // option.setIsNeedAddress(true);
        option.setOpenGps(true);
        // option.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(option);
    }

    //定位当前位置-----------------------------------
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            if (location == null) {
                return;
            }
//            main_BDlocation = location;
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(100)
                    .latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
//            if (isFirstLoc) {
//                isFirstLoc = false;
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        //在Fragment执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在Fragment执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
}


