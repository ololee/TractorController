package com.hc.mixthebluetooth.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hc.basiclibrary.dialog.CommonDialog;
import com.hc.basiclibrary.ioc.ViewById;
import com.hc.basiclibrary.permission.PermissionUtil;
import com.hc.basiclibrary.titleBasic.DefaultNavigationBar;
import com.hc.basiclibrary.viewBasic.BasActivity;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.single.HoldBluetooth;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.customView.PopWindowMain;
import com.hc.mixthebluetooth.customView.dialog.CollectBluetooth;
import com.hc.mixthebluetooth.recyclerData.MainRecyclerAdapter;
import com.hc.mixthebluetooth.storage.Storage;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BasActivity {

    @ViewById(R.id.main_swipe)
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @ViewById(R.id.main_back_not)
    private LinearLayout mNotBluetooth;

    @ViewById(R.id.main_recycler)
    private RecyclerView mRecyclerView;
    private MainRecyclerAdapter mainRecyclerAdapter;

    private DefaultNavigationBar mTitle;

    private Storage mStorage;

    private List<DeviceModule> mModuleArray = new ArrayList<>();
    private List<DeviceModule> mFilterModuleArray = new ArrayList<>();

    private HoldBluetooth mHoldBluetooth;

    private int mStartDebug = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //????????????
        setTitle();
        setContext(this);
    }

    @Override
    public void initAll() {

        mStorage = new Storage(this);//sp??????

        //?????????????????????????????????????????????
        initHoldBluetooth();

        //???????????????
        initPermission();

        //?????????View
        initView();

        //?????????????????????
        initRefresh();

        //??????RecyclerView???Item???????????????
        setRecyclerListener();
    }

    private void initHoldBluetooth() {
        mHoldBluetooth = HoldBluetooth.getInstance();
        final HoldBluetooth.UpdateList updateList = new HoldBluetooth.UpdateList() {
            @Override
            public void update(boolean isStart,DeviceModule deviceModule) {
                if (isStart){
                    log("????????????..","w");
                    setMainBackIcon();
                    mModuleArray.add(deviceModule);
                    addFilterList(deviceModule,true);
                }else {
                    mTitle.updateLoadingState(false);
                }
            }

            @Override
            public void updateMessyCode(boolean isStart, DeviceModule deviceModule) {
                for(int i= 0; i<mModuleArray.size();i++){
                    if (mModuleArray.get(i).getMac().equals(deviceModule.getMac())){
                        mModuleArray.remove(mModuleArray.get(i));
                        mModuleArray.add(i,deviceModule);
                        upDateList();
                        break;
                    }
                }
            }
        };
        mHoldBluetooth.initHoldBluetooth(MainActivity.this,updateList);
    }

    private void initView() {
        setMainBackIcon();
        mainRecyclerAdapter = new MainRecyclerAdapter(this,mFilterModuleArray,R.layout.item_recycler_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mainRecyclerAdapter);
    }

    //?????????????????????
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//?????????????????????
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                refresh();
            }
        });
    }

    //?????????????????????
    private void refresh(){
        if (mHoldBluetooth.scan(mStorage.getData(PopWindowMain.BLE_KEY))){
            mModuleArray.clear();
            mFilterModuleArray.clear();
            mTitle.updateLoadingState(true);
        }
    }

    //??????????????????????????????????????????????????????
    private void addFilterList(DeviceModule deviceModule,boolean isRefresh){
        if (mStorage.getData(PopWindowMain.NAME_KEY)){
            if (deviceModule.getName().equals("N/A"))
                return;
        }

        if (mStorage.getData(PopWindowMain.BLE_KEY)){
            if (!deviceModule.isBLE())
                return;
        }

        if (mStorage.getData(PopWindowMain.FILTER_KEY) || mStorage.getData(PopWindowMain.CUSTOM_KEY)){
            if (!deviceModule.isHcModule(mStorage.getData(PopWindowMain.CUSTOM_KEY),mStorage.getDataString(PopWindowMain.DATA_KEY)))
                return;
        }
        deviceModule.isCollectName(MainActivity.this);
        mFilterModuleArray.add(deviceModule);
        if (isRefresh)
            mainRecyclerAdapter.notifyDataSetChanged();
    }

    //????????????
    private void setTitle() {
        mTitle = new DefaultNavigationBar
                .Builder(this,(ViewGroup)findViewById(R.id.main_name))
                .setLeftText("????????????")
                .hideLeftIcon()
                .setRightIcon()
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStartDebug % 4 ==0){
                            startActivity(DebugActivity.class);
                        }
                        mStartDebug++;
                    }
                })
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setPopWindow(v);
                        mTitle.updateRightImage(true);
                    }
                })
                .builer();
    }

    //??????????????????
    private void setPopWindow(View v){
        new PopWindowMain(v, MainActivity.this, new PopWindowMain.DismissListener() {
            @Override
            public void onDismissListener() {//???????????????????????????
               upDateList();
               mTitle.updateRightImage(false);
            }
        });
    }

    //??????????????????
    private void setRecyclerListener() {
        mainRecyclerAdapter.setOnItemClickListener((position, view) -> {
            log("viewId:"+view.getId()+" item_main_icon:"+R.id.item_main_icon);
            if (view.getId() == R.id.item_main_icon){
                setCollectWindow(position);//????????????
            }else {
                showPopup(position,view);
            }
        });
    }

    //????????????
    private void setCollectWindow(int position) {
        log("????????????..");
        CommonDialog.Builder collectBuilder = new CommonDialog.Builder(MainActivity.this);
        collectBuilder.setView(R.layout.hint_collect_vessel).fullWidth().loadAnimation().create().show();
        CollectBluetooth collectBluetooth = collectBuilder.getView(R.id.hint_collect_vessel_view);
        collectBluetooth.setBuilder(collectBuilder).setDevice(mFilterModuleArray.get(position))
                .setCallback(() -> upDateList());
    }

    //????????????
    private void upDateList(){
        mFilterModuleArray.clear();
        for (DeviceModule deviceModule : mModuleArray) {
            addFilterList(deviceModule,false);
        }
        mainRecyclerAdapter.notifyDataSetChanged();
        setMainBackIcon();
    }

    //???????????????????????????????????????
    private void setMainBackIcon(){
        if (mFilterModuleArray.size() == 0){
            mNotBluetooth.setVisibility(View.VISIBLE);
        }else {
            mNotBluetooth.setVisibility(View.GONE);
        }
    }

    //?????????????????????
    private void initPermission(){
        PermissionUtil.requestEach(MainActivity.this, new PermissionUtil.OnPermissionListener() {
            @Override
            public void onSucceed() {
                //???????????????????????????
                log("????????????");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mHoldBluetooth.bluetoothState()){
                            if (Analysis.isOpenGPS(MainActivity.this))
                                refresh();
                            else
                                startLocation();
                        }
                    }
                },1000);

            }
            @Override
            public void onFailed(boolean showAgain) {
                log("??????","e");
            }
        }, PermissionUtil.LOCATION);
    }

    //??????????????????
    private void startLocation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("??????")
                .setMessage("????????????????????????????????????!")
                .setCancelable(false)
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 10);
                    }
                }).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //?????????????????????????????????????????????????????????
        mHoldBluetooth.stopScan();
    }

    private Context getContext(){
      return this;
    }


  private void showPopup(int pos,View v) {
    QMUIPopups.quickAction(getContext(),
        QMUIDisplayHelper.dp2px(getContext(), 56),
        QMUIDisplayHelper.dp2px(getContext(), 56))
        .shadow(true)
        .skinManager(QMUISkinManager.defaultInstance(getContext()))
        .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 20))
        .addAction(new QMUIQuickAction.Action().icon(R.drawable.auto_icon)
            .text(getText(R.string.auto_controller))
            .onClick(
                (quickAction, action, position) -> {
                  quickAction.dismiss();
                  startSecondActivity(pos,AutoControlActivity.class);
                }
            ))
        .addAction(new QMUIQuickAction.Action().icon(R.drawable.manual_icon)
            .text(getText(R.string.remote_controller))
            .onClick(
                (quickAction, action, position) -> {
                  quickAction.dismiss();
                  startSecondActivity(pos, ManualControllerActivity.class);
                }
            ))

        .show(v);
  }

  private void startSecondActivity(int position,Class clazz){
    mHoldBluetooth.setDevelopmentMode(MainActivity.this);//??????????????????????????????
    mHoldBluetooth.connect(mFilterModuleArray.get(position));
    startActivity(clazz);
  }
}