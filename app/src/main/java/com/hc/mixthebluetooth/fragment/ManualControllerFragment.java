package com.hc.mixthebluetooth.fragment;

import android.os.Handler;
import androidx.fragment.app.Fragment;
import com.hc.basiclibrary.viewBasic.BasFragment;
import com.hc.mixthebluetooth.R;

public class ManualControllerFragment extends BasFragment  {

  public ManualControllerFragment() {
  }

  public static ManualControllerFragment newInstance() {
    ManualControllerFragment fragment = new ManualControllerFragment();
    return fragment;
  }



  @Override public int setFragmentViewId() {
    return R.layout.fragment_manual_controller;
  }

  @Override public void initAll() {

  }

  @Override public void setHandler(Handler handler) {

  }
}