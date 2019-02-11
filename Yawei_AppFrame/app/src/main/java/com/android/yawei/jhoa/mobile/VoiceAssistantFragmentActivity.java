package com.android.yawei.jhoa.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.android.yawei.jhoa.Interface.BackHandledInterface;
import com.android.yawei.jhoa.adapter.BaseFragmentActivity;
import com.android.yawei.jhoa.fragment.BackHandledFragment;
import com.android.yawei.jhoa.fragment.VoiceAssistantFragment;
import com.yawei.jhoa.mobile.R;

/**
 *
 * class description: VoiceAssistantActivity
 * author: Yusz
 * Date: 2018-8-8
 */

public class VoiceAssistantFragmentActivity extends BaseFragmentActivity implements View.OnClickListener,BackHandledInterface,FragmentManager.OnBackStackChangedListener{

    private LinearLayout linBack;
    private FragmentManager mFragmentManager;
    private BackHandledFragment mBackHandedFragment;

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_voiceassistantfragment);
        try {
            Initview();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void Initview()throws Exception{
        linBack = (LinearLayout)findViewById(R.id.LinTopBack);
        linBack.setOnClickListener(this);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);
        VoiceAssistantFragment fragment = new VoiceAssistantFragment();
        replaceFragment(fragment);
    }
    public void replaceFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().replace(R.id.framelayout, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack("tag").commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LinTopBack:
                if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
                    if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        finish();
                        return;
                    }
                    getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }

    @Override
    public void onBackStackChanged() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            mFragmentManager.getBackStackEntryAt(count - 1);
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }
    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return;
            }
            getSupportFragmentManager().popBackStack();
        }
    }
}
