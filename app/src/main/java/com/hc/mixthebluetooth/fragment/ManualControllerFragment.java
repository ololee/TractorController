package com.hc.mixthebluetooth.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.hc.basiclibrary.viewBasic.BaseFragment;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.CommunicationActivity;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.data.DataDealUtils;
import com.hc.mixthebluetooth.databinding.FragmentManualControllerBinding;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;

public class ManualControllerFragment extends BaseFragment implements View.OnClickListener {
  private Handler mHandler;
  private DeviceModule module;
  private FragmentManualControllerBinding binding;
  private float lastThrottleValue, lastListThrottleValue, lastLaterDirectionValue = 0.0f;

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
    binding.lateralMoveBar.setSlideCallback(value -> {
      if (lastLaterDirectionValue != value) {
        sendDirectionCode(value);
      }
      lastLaterDirectionValue = value;
    });
    binding.btnForward.setOnClickListener(this);
    binding.btnStop.setOnClickListener(this);
    binding.btnBack.setOnClickListener(this);
    binding.liftThrottle.setSlideCallback(value -> {
      if (lastListThrottleValue != value) {
        sendLiftThrottle(value);
      }
      lastListThrottleValue = value;
    });
    binding.throttle.setSlideCallback(value -> {
      if (lastThrottleValue != value) {
        sendThrottle(value);
      }
      lastThrottleValue = value;
    });
    /**
     * 回中
     */
    binding.rudderCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
      binding.lateralMoveBar.setBackToCenter(isChecked);
    });
    binding.rudderCheckBox.setChecked(true);
    binding.lateralMoveBar.setBackToCenter(true);
    binding.liftThrottle.setTouchEnable(binding.liftSwitchCheckBox.isChecked());
    binding.liftSwitchCheckBox.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          binding.liftThrottle.setTouchEnable(isChecked);
        });
    binding.throttleSwitchCheckBox.setChecked(false);
    binding.throttle.setTouchEnable(binding.throttleSwitchCheckBox.isChecked());
    binding.throttleSwitchCheckBox.setOnCheckedChangeListener((v, isChecked) -> {
      binding.throttle.setTouchEnable(isChecked);
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

  public void sendDirectionCode(float x) {
    if (mHandler == null) {
      return;
    }
    int dir = (int) (x * 128) + 128;
    if (dir == 256) {
      dir = 255;
    }
    byte[] sendDataCode = DataDealUtils.sendDirectionCodeFunc(dir);
    FragmentMessageItem item =
        new FragmentMessageItem(true, sendDataCode, Analysis.getTime(), true, module, false);
    Message message = mHandler.obtainMessage();
    message.what = CommunicationActivity.DATA_TO_MODULE;
    message.obj = item;
    mHandler.sendMessage(message);
  }

  public void sendThrottle(float x) {
    if (mHandler == null) {
      return;
    }
    int throttle = (int) (x * -128) + 128;
    if (throttle == 256) {
      throttle = 255;
    }
    byte[] sendDataCode = DataDealUtils.sendThrottleCodeFunc(throttle);
    FragmentMessageItem item =
        new FragmentMessageItem(true, sendDataCode, Analysis.getTime(), true, module, false);
    Message message = mHandler.obtainMessage();
    message.what = CommunicationActivity.DATA_TO_MODULE;
    message.obj = item;
    mHandler.sendMessage(message);
  }

  public void sendLiftThrottle(float x) {
    if (mHandler == null) {
      return;
    }
    int throttle = (int) (x * -128) + 128;
    if (throttle == 256) {
      throttle = 255;
    }
    byte[] sendDataCode = DataDealUtils.sendLiftThrottleCodeFunc(throttle);
    FragmentMessageItem item =
        new FragmentMessageItem(true, sendDataCode, Analysis.getTime(), true, module, false);
    Message message = mHandler.obtainMessage();
    message.what = CommunicationActivity.DATA_TO_MODULE;
    message.obj = item;
    mHandler.sendMessage(message);
  }
}