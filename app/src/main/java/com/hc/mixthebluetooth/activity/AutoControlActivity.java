package com.hc.mixthebluetooth.activity;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.hc.basiclibrary.viewBasic.BasActivity;
import com.hc.basiclibrary.viewBasic.tool.IMessageInterface;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.single.HoldBluetooth;
import com.hc.mixthebluetooth.databinding.ActivityAutoControlBinding;
import com.hc.mixthebluetooth.fragment.AutoControllerFragment;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;
import java.util.List;

import static com.hc.mixthebluetooth.activity.CommunicationActivity.FRAGMENT_STATE_DATA;

public class AutoControlActivity extends BasActivity {
  private ActivityAutoControlBinding binding;
  private FragmentManager fragmentManager;
  private HoldBluetooth mHoldBluetooth;
  private List<DeviceModule> modules;
  private DeviceModule mErrorDisconnect;
  private IMessageInterface mMessage;


  private Handler mFragmentHandler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(@NonNull Message msg) {
      FragmentMessageItem item = (FragmentMessageItem) msg.obj;
      mHoldBluetooth.sendData(item.getModule(),item.getByteData().clone());
      return false;
    }
  });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityAutoControlBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    init();
  }

  @Override public void initAll() {
    mHoldBluetooth = HoldBluetooth.getInstance();
    initDataListener();
  }

  private void initDataListener() {
    HoldBluetooth.OnReadDataListener dataListener = new HoldBluetooth.OnReadDataListener() {
      @Override public void readData(String mac, byte[] data) {
        mMessage.readData(FRAGMENT_STATE_DATA,modules.get(0),data);
      }

      @Override public void reading(boolean isStart) {

      }

      @Override public void connectSucceed() {
        modules = mHoldBluetooth.getConnectedArray();
      }

      @Override public void errorDisconnect(DeviceModule deviceModule) {
        if (mErrorDisconnect == null) {//判断是否已经重复连接
          mErrorDisconnect = deviceModule;
          if (mHoldBluetooth != null && deviceModule != null) {
            mFragmentHandler.postDelayed(new Runnable() {
              @Override
              public void run() {
                mHoldBluetooth.connect(deviceModule);
              //  setState(CONNECTING);//设置正在连接状态
              }
            },2000);
            return;
          }
        }
       // setState(DISCONNECT);//设置断开状态
        if (deviceModule != null)
          toast("连接" + deviceModule.getName() + "失败，点击右上角的已断线可尝试重连", Toast.LENGTH_LONG);
        else
          toast("连接模块失败，请返回上一个页面重连", Toast.LENGTH_LONG);
      }

      @Override public void readNumber(int number) {

      }

      @Override public void readLog(String className, String data, String lv) {

      }
    };

    mHoldBluetooth.setOnReadListener(dataListener);
  }

  private void init() {
    mMessage = AutoControllerFragment.newInstance("","");
    fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.auto_controller_frame,
        (Fragment) mMessage);
    fragmentTransaction.commitAllowingStateLoss();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (modules != null)
      mHoldBluetooth.disconnect(modules.get(0));
  }
}