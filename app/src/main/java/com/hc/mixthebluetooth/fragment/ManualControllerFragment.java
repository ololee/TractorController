package com.hc.mixthebluetooth.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.hc.basiclibrary.viewBasic.BasFragment;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.CommunicationActivity;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.data.DataDealUtils;
import com.hc.mixthebluetooth.data.DataModel;
import com.hc.mixthebluetooth.databinding.FragmentManualControllerBinding;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;
import com.hc.mixthebluetooth.view.LateralMoveBar;

public class ManualControllerFragment extends BasFragment implements View.OnClickListener,
    LateralMoveBar.SlideCallback {
  private Handler mHandler;
  private DeviceModule module;
  private FragmentManualControllerBinding binding;

  public ManualControllerFragment() {
  }

  public static ManualControllerFragment newInstance() {
    ManualControllerFragment fragment = new ManualControllerFragment();
    return fragment;
  }

  @Override public int setFragmentViewId() {
    return R.layout.fragment_manual_controller;
  }

  @Override public void initAll(View view, Context context) {
    super.initAll(view, context);
    binding = FragmentManualControllerBinding.bind(view);
    binding.lateralMoveBar.setSlideCallback(this);
    binding.btnForward.setOnClickListener(this);
    binding.btnStop.setOnClickListener(this);
    binding.btnBack.setOnClickListener(this);
  }

  @Override public void initAll() {

  }

  @Override public void readData(int state, Object o, byte[] data) {
    switch (state) {
      case CommunicationActivity.FRAGMENT_STATE_DATA:
        if (module == null) {
          module = (DeviceModule) o;
        }
        break;
      case CommunicationActivity.FRAGMENT_STATE_NUMBER:
        break;
      case CommunicationActivity.FRAGMENT_STATE_SEND_SEND_TITLE:
        break;
    }
  }

  @Override public void setHandler(Handler handler) {
    mHandler = handler;
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_forward:
        break;
      case R.id.btn_stop:
        break;
      case R.id.btn_back:
        break;
    }
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

  @Override public void slide(float x) {

  }
}