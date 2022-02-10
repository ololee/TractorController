package com.hc.mixthebluetooth.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.hc.basiclibrary.utils.NumberFormatUtils;
import com.hc.basiclibrary.viewBasic.BasFragment;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.CommunicationActivity;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.data.DataDealUtils;
import com.hc.mixthebluetooth.data.DataModel;
import com.hc.mixthebluetooth.databinding.FragmentAutoControllerBinding;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;

public class AutoControllerFragment extends BasFragment implements View.OnClickListener {

  private Runnable mRunnable;//循环发送的线程
  private Handler mHandler;

  private DeviceModule module;

  private FragmentAutoControllerBinding binding;

  private boolean startBtnStatus = false;

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
    binding.startPauseBtn.setOnClickListener(this);
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
          binding.lateralDeviationTv.setText(
              NumberFormatUtils.formatFloat(dataModel.getLateralDeviation()));
          binding.courseDeviationTv.setText(
              NumberFormatUtils.formatFloat(dataModel.getCourseDeviation()));
          binding.frontWheelAngleTv.setText(
              NumberFormatUtils.formatFloat(dataModel.getFrontWheelAngle()));
          binding.vehicleDirectionTv.setText(
              NumberFormatUtils.formatFloat(dataModel.getRtkDirection()));
          binding.rtkModeTv.setText(NumberFormatUtils.formatFloat(dataModel.getRtkMode()));
          binding.baselineAngleTv.setText(
              NumberFormatUtils.formatFloat(dataModel.getBaseLineAngle()));
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
    byte[] sendDataCode = DataDealUtils.sendControlCodeFunc(functionCode);
    FragmentMessageItem item =
        new FragmentMessageItem(true, sendDataCode, Analysis.getTime(), true, module, false);
    Message message = mHandler.obtainMessage();
    message.what = CommunicationActivity.DATA_TO_MODULE;
    message.obj = item;
    mHandler.sendMessage(message);
  }

  @Override public void onClick(View v) {
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
      case R.id.start_pause_btn:
        binding.startPauseBtn.setText(startBtnStatus ? R.string.start : R.string.stop);
        binding.startPauseBtn.setTextColor(
            getResources().getColor(startBtnStatus ? R.color.green : R.color.red));
        sendData(startBtnStatus ? 0xE3 : 0xE4);
        startBtnStatus = !startBtnStatus;
    }
  }
}
