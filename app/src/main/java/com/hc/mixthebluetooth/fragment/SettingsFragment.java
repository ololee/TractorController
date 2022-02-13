package com.hc.mixthebluetooth.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.hc.basiclibrary.viewBasic.BaseFragment;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.AutoControlActivity;
import com.hc.mixthebluetooth.activity.CommunicationActivity;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.data.DataDealUtils;
import com.hc.mixthebluetooth.databinding.FragmentSettingsBinding;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;

public class SettingsFragment extends BaseFragment {
  private FragmentSettingsBinding binding;
  private Handler mHandler;
  private DeviceModule module;

  public SettingsFragment() {
  }

  public static SettingsFragment newInstance() {
    SettingsFragment fragment = new SettingsFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public int setFragmentViewId() {
    return R.layout.fragment_settings;
  }

  @Override public void initAll(View view, Context context) {
    super.initAll(view, context);
    binding = FragmentSettingsBinding.bind(view);
  }

  @Override public void initAll() {
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
          String strData = Analysis.getByteToString(data, true);
          log("ololeeDetail: " + strData);
        }
        break;
      case CommunicationActivity.FRAGMENT_STATE_NUMBER:
        break;
      case CommunicationActivity.FRAGMENT_STATE_SEND_SEND_TITLE:
        break;
    }
  }

  public void sendSettingData(int functionCode) {
    if (mHandler == null) {
      return;
    }
    byte[] sendDataCode = DataDealUtils.sendControlCodeFunc(functionCode);
    FragmentMessageItem item =
        new FragmentMessageItem(true, sendDataCode, Analysis.getTime(), true, module, false);
    Message message = mHandler.obtainMessage();
    message.what = AutoControlActivity.SEND_SETTING_DATA;
    message.obj = item;
    mHandler.sendMessage(message);
  }


  @Override public boolean onBackPressed() {
    sendSettingData(0xf4);
    return true;
  }
}