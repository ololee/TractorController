package com.hc.mixthebluetooth.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
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
import com.hc.mixthebluetooth.data.DataDealUtils;
import com.hc.mixthebluetooth.data.DataModel;
import com.hc.mixthebluetooth.databinding.FragmentAutoControllerBinding;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;
import com.hc.mixthebluetooth.storage.Storage;
import java.util.Arrays;

public class AutoControllerFragment extends BasFragment implements View.OnClickListener {

  private Runnable mRunnable;//循环发送的线程
  private Handler mHandler;

  private DeviceModule module;

  private FragmentAutoControllerBinding binding;

  public AutoControllerFragment() {
  }

  public static AutoControllerFragment newInstance() {
    AutoControllerFragment fragment = new AutoControllerFragment();
    return fragment;
  }

  @Override public int setFragmentViewId() {
    return R.layout.fragment_auto_controller;
  }

  @Override public void initAll() {
  }

  @Override public void initAll(View view, Context context) {
    super.initAll(view, context);
    binding = FragmentAutoControllerBinding.bind(view);
    View btnA = binding.locationBtns.findViewById(R.id.btn_a);
    View btnB = binding.locationBtns.findViewById(R.id.btn_b);
    View btnC = binding.locationBtns.findViewById(R.id.btn_c);
    View btnD = binding.locationBtns.findViewById(R.id.btn_d);
    btnA.setOnClickListener(this);
    btnB.setOnClickListener(this);
    btnC.setOnClickListener(this);
    btnD.setOnClickListener(this);
    binding.btnTurnLeft.setOnClickListener(this);
    binding.btnTurnRight.setOnClickListener(this);
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
          DataModel dataModel = DataDealUtils.formatData(data);
          binding.lateralDeviationTv.setText(dataModel.getLateralDeviation() + "");
          binding.courseDeviationTv.setText(dataModel.getCourseDeviation() + "");
          binding.frontWheelAngleTv.setText(dataModel.getFrontWheelAngle() + "");
          binding.vehicleDirectionTv.setText(dataModel.getRtkDirection() + "");
        }
        break;
      case CommunicationActivity.FRAGMENT_STATE_NUMBER:
        break;
      case CommunicationActivity.FRAGMENT_STATE_SEND_SEND_TITLE:
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
    log("==========sendData===============");
  }

  @Override public void onClick(View v) {
    Toast.makeText(getContext(), "press", Toast.LENGTH_SHORT).show();
    switch (v.getId()) {
      case R.id.btn_a:
        sendData(0xaa);
        break;
      case R.id.btn_b:
        sendData(0xbb);
        break;
      case R.id.btn_c:
        sendData(0xcc);
        break;
      case R.id.btn_d:
        sendData(0xdd);
        break;
      case R.id.btn_turn_left:
        sendData(0xe1);
        break;
      case R.id.btn_turn_right:
        sendData(0xe2);
        break;
    }
  }
}