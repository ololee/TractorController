package com.hc.mixthebluetooth.fragment;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.basiclibrary.ioc.OnClick;
import com.hc.basiclibrary.ioc.ViewById;
import com.hc.basiclibrary.ioc.ViewByIds;
import com.hc.basiclibrary.titleBasic.DefaultNavigationBar;
import com.hc.basiclibrary.viewBasic.BaseFragment;
import com.hc.basiclibrary.viewBasic.manage.BasFragmentManage;
import com.hc.basiclibrary.viewBasic.tool.IMessageInterface;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.CommunicationActivity;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.customView.CheckBoxSample;
import com.hc.mixthebluetooth.customView.UnderlineTextView;
import com.hc.mixthebluetooth.recyclerData.FragmentMessAdapter;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentCustom extends BaseFragment {

    private static final int FRAGMENT_1 = 0x00;
    private static final int FRAGMENT_2 = 0x01;

    @ViewById(R.id.custom_fragment_recycler)
    private RecyclerView mRecyclerView;

    @ViewById(R.id.custom_fragment_read_check)
    private CheckBoxSample mCheckHex;

    private FragmentMessAdapter mAdapter;

    private List<FragmentMessageItem> mDataList = new ArrayList<>();

    private DeviceModule module;

    private BasFragmentManage mFragmentManager;

    private Handler mCommunicationHandler;

    private DefaultNavigationBar mTitle;

    private  boolean mHidden = false;

    @ViewByIds(value = {R.id.custom_fragment_group,R.id.custom_fragment_direction},name = {"mGroupButton","mDirectionButton"})
    private UnderlineTextView mGroupButton,mDirectionButton;

    @ViewById(R.id.custom_fragment)
    private FrameLayout mFragment;

    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_custom;
    }

    @Override
    public void initAll() {
        initRecycler();
        initFragment();
        mDirectionButton.setState(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewHeight();
            }
        },500);
    }


    @Override
    public void setHandler(Handler handler) {
        mCommunicationHandler = handler;
    }

    @Override
    public void updateState(int start) {
        if (start == CommunicationActivity.FRAGMENT_CUSTOM_HIDE){
            mHidden = false;
        }else if (start == CommunicationActivity.FRAGMENT_UNHIDDEN){
            mHidden = true;
        }
    }

    @Override
    public void readData(int state,Object o, byte[] data) {
        if (module == null && state == CommunicationActivity.FRAGMENT_STATE_DATA) {
            module = (DeviceModule) o;
        }
        if (data != null && state == CommunicationActivity.FRAGMENT_STATE_DATA && mHidden) {
            mDataList.add(new FragmentMessageItem(Analysis.getByteToString(data,mCheckHex.isChecked()), null, false, module,false));
            mAdapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(mDataList.size());
        }
        if (state == CommunicationActivity.FRAGMENT_STATE_SEND_SEND_TITLE){
            mTitle = (DefaultNavigationBar) o;
        }
    }

    private void initRecycler(){
        mAdapter = new FragmentMessAdapter(getContext(),mDataList,R.layout.item_message_fragment);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initFragment() {
        mFragmentManager = new BasFragmentManage(R.id.custom_fragment,getActivity());
        IMessageInterface group = mFragmentManager.addFragment(FRAGMENT_1, new FragmentCustomGroup());
        IMessageInterface direction = mFragmentManager.addFragment(FRAGMENT_2, new FragmentCustomDirection());
        mFragmentManager.showFragment(FRAGMENT_2);
        group.setHandler(mCommunicationHandler);
        direction.setHandler(mCommunicationHandler);
        group.readData(CommunicationActivity.FRAGMENT_STATE_SEND_SEND_TITLE,mTitle,null);
        direction.readData(CommunicationActivity.FRAGMENT_STATE_SEND_SEND_TITLE,mTitle,null);
    }

    private void setViewHeight() {//????????????fragment?????????
        mFragment.post(new Runnable() {
            @Override
            public void run() {
                int height = mFragment.getHeight();
                ViewGroup.LayoutParams params=mFragment.getLayoutParams();
                params.height= height;
                log("height is "+height,"e");
                mFragment.setLayoutParams(params);
            }
        });
    }

    @OnClick({R.id.custom_fragment_group,R.id.custom_fragment_direction,R.id.custom_fragment_read_check,R.id.custom_fragment_read_hex})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.custom_fragment_group:
                mGroupButton.setState(true);
                mDirectionButton.setState(false);
                mFragmentManager.showFragment(FRAGMENT_1);
                break;
            case R.id.custom_fragment_direction:
                mGroupButton.setState(false);
                mDirectionButton.setState(true);
                mFragmentManager.showFragment(FRAGMENT_2);
                break;
            case R.id.custom_fragment_read_check:
            case R.id.custom_fragment_read_hex:
                mCheckHex.toggle();
                break;
        }
    }
}
