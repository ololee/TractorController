package com.hc.mixthebluetooth.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.hc.basiclibrary.viewBasic.BasFragment;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.CommunicationActivity;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.data.DataDealUtils;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;

public class ManualControllerFragment extends BasFragment implements View.OnClickListener {

  private Runnable mRunnable;//循环发送的线程
  private Handler mHandler;

  private DeviceModule module;


  public ManualControllerFragment() {
  }

  public static ManualControllerFragment newInstance() {
    ManualControllerFragment fragment = new ManualControllerFragment();
    return fragment;
  }

  @Override public int setFragmentViewId() {
    return R.layout.fragment_auto_controller;
  }

  @Override public void initAll() {
  }

  @Override public void initAll(View view, Context context) {
    super.initAll(view, context);
  }

  @Override public void setHandler(Handler handler) {
    this.mHandler = handler;
  }

  @Override public void readData(int state, Object o, byte[] data) {

  }

  public void sendData(int functionCode) {
    if (mHandler == null) {
      return;
    }
    byte[] sendDataCode = DataDealUtils.sendABCDPointsFunc(functionCode);
    FragmentMessageItem item =
        new FragmentMessageItem(true, sendDataCode, Analysis.getTime(), true, module, false);
    Message message = mHandler.obtainMessage();
    message.what = CommunicationActivity.DATA_TO_MODULE;
    message.obj = item;
    mHandler.sendMessage(message);
  }

  @Override public void onClick(View v) {
  }
}