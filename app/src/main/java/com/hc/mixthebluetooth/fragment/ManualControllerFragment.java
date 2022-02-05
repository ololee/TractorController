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
import com.hc.mixthebluetooth.databinding.FragmentManualControllerBinding;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;
import com.hc.mixthebluetooth.view.MoveBar;

public class ManualControllerFragment extends BasFragment implements View.OnClickListener,
    MoveBar.SlideCallback {
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
    binding.rudderCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
      binding.lateralMoveBar.setBackToCenter(isChecked);
    });
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
        controlSpeed(0x1f);
        break;
      case R.id.btn_stop:
        controlSpeed(0x00);
        break;
      case R.id.btn_back:
        controlSpeed(0x1b);
        break;
    }
  }

  public void controlSpeed(int speedCode) {
    if (mHandler == null) {
      return;
    }
    byte[] sendDataCode = DataDealUtils.sendControlCodeFunc(speedCode);
    FragmentMessageItem item =
        new FragmentMessageItem(true, sendDataCode, Analysis.getTime(), true, module, false);
    Message message = mHandler.obtainMessage();
    message.what = CommunicationActivity.DATA_TO_MODULE;
    message.obj = item;
    mHandler.sendMessage(message);
  }

  public void sendDirectionCode(float x){
    if (mHandler == null) {
      return;
    }
    byte[] sendDataCode = DataDealUtils.sendDirectionCodeFunc(x);
    FragmentMessageItem item =
        new FragmentMessageItem(true, sendDataCode, Analysis.getTime(), true, module, false);
    Message message = mHandler.obtainMessage();
    message.what = CommunicationActivity.DATA_TO_MODULE;
    message.obj = item;
    mHandler.sendMessage(message);
  }


  @Override public void slide(float value) {
    sendDirectionCode(value);
  }
}