package com.hc.mixthebluetooth.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hc.mixthebluetooth.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoteControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoteControllerFragment extends Fragment {

  public RemoteControllerFragment() {
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
  public static RemoteControllerFragment newInstance() {
    RemoteControllerFragment fragment = new RemoteControllerFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_remote_controller, container, false);
  }
}