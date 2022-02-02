package com.hc.mixthebluetooth.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hc.basiclibrary.titleBasic.DefaultNavigationBar;
import com.hc.basiclibrary.viewBasic.BasFragment;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.CommunicationActivity;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.databinding.FragmentAutoControllerBinding;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;
import com.hc.mixthebluetooth.storage.Storage;
import java.util.Arrays;

public class AutoControllerFragment extends BasFragment {
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private Runnable mRunnable;//循环发送的线程
  private Handler mHandler;

  private DeviceModule module;

  private String mParam1;
  private String mParam2;
  private FragmentAutoControllerBinding binding;

  public AutoControllerFragment() {
  }

  public static AutoControllerFragment newInstance(String param1, String param2) {
    AutoControllerFragment fragment = new AutoControllerFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

 /* @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_auto_controller, container, false);
  }*/

  @Override public int setFragmentViewId() {
    return R.layout.fragment_auto_controller;
  }

  @Override public void initAll() {
  }

  @Override public void initAll(View view, Context context) {
    super.initAll(view, context);
    binding = FragmentAutoControllerBinding.bind(view);
  }

  @Override public void setHandler(Handler handler) {
    this.mHandler = handler;
  }

  @Override public void readData(int state, Object o, byte[] data) {
    switch (state) {
      case CommunicationActivity.FRAGMENT_STATE_DATA:
        if (module == null) {
          module = (DeviceModule) o;
        }
        if (data != null) {
          String strData = Analysis.getByteToString(data, false);
          log("ololeeDetail: "+strData);
          String[] dataArray = strData.split("[a-zA-Z]");
          log("ololeeDetail: "+ Arrays.toString(dataArray));
          binding.lateralDeviationTv.setText(dataArray[1]);
          binding.courseDeviationTv.setText(dataArray[2]);
          binding.frontWheelAngleTv.setText(dataArray[3]);
          binding.vehicleDirectionTv.setText(dataArray[4]);
          //D0.236B-0.125C56.245H5.546
         /*
          mDataList.add(new FragmentMessageItem(Analysis.getByteToString(data,isReadHex), isShowTime?Analysis.getTime():null, false, module,isShowMyData));
          mAdapter.notifyDataSetChanged();
          mRecyclerView.smoothScrollToPosition(mDataList.size());
          mReadNumberTV.setText(String.valueOf(Integer.parseInt(mReadNumberTV.getText().toString())+Analysis.lengthArray(data)));
          */

        }
        break;
      case CommunicationActivity.FRAGMENT_STATE_NUMBER:
       /* mSendNumberTv.setText(
            String.valueOf(Integer.parseInt(mSendNumberTv.getText().toString()) + ((int) o)));
        setUnsentNumberTv();*/
        break;
      case CommunicationActivity.FRAGMENT_STATE_SEND_SEND_TITLE:
        //mTitle = (DefaultNavigationBar) o;
        break;
    }
  }
}