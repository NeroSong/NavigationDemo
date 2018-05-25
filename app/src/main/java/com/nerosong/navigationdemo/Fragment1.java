package com.nerosong.navigationdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import java.util.List;


public class Fragment1 extends Fragment implements View.OnClickListener{


    Button button2;

    private LocationClient mLocationClient = null;

    private Button startLocation;
    private MapView mapView;
    private BaiduMap mBaiduMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SDKInitializer.initialize(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment1, container, false);
        button2 = view.findViewById(R.id.button2);

        mapView = (MapView) view.findViewById(R.id.mapView);

        button2.setOnClickListener(this);

        mBaiduMap = mapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        BDLocationListener listener = new MyLocationListener();

        //此处需要注意：LocationClient类必须在主线程中声明。需要Context类型的参数。
        //Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context
        mLocationClient = new LocationClient(getActivity().getApplicationContext());

        //注册位置监听器
        mLocationClient.registerLocationListener(listener);


        return view;
    }


    @Override
    public void onClick(View v) {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000; //5秒发送一次
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setNeedDeviceDirect(true); //返回的定位结果包含手机机头方向
        mLocationClient.setLocOption(option);
        mLocationClient.start(); //启动位置请求
        mLocationClient.requestLocation();//发送请求

    }



    /**
     * 位置监听器
     * <p/>
     * BDLocationListener接口有1个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
     */
    class MyLocationListener implements BDLocationListener {

        /**
         * 接收位置的信息回调方法
         *
         * @param location
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.d("BaiduLocationApiDem", sb.toString());
             //latlngn哪一行，删掉了自定义的图标配置
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.maker);
            //创建一个图层选项
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            OverlayOptions options = new MarkerOptions().position(latlng).icon(bitmapDescriptor);
            mBaiduMap.addOverlay(options);
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(latlng)
                    .zoom(18)
                    .build();
//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);


        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLocationClient.isStarted() == false) {
            mLocationClient.start();// 开启定位

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume(); // 使百度地图地图控件和Fragment的生命周期保持一致

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause(); // 使百度地图地图控件和Fragment的生命周期保持一致
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();// 停止定位

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy(); // 使百度地图地图控件和Fragment的生命周期保持一致
        }
    }



}


