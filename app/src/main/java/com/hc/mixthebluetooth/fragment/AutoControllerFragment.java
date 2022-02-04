package com.hc.mixthebluetooth.fragment;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hc.basiclibrary.viewBasic.BasFragment;
import com.hc.mixthebluetooth.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AutoControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AutoControllerFragment extends BasFragment  {

  public AutoControllerFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment RemoteControllerFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static AutoControllerFragment newInstance() {
    AutoControllerFragment fragment = new AutoControllerFragment();
    return fragment;
  }



  @Override public int setFragmentViewId() {
    return R.layout.fragment_remote_controller;
  }

  @Override public void initAll() {

  }

  @Override public void setHandler(Handler handler) {

  }
}