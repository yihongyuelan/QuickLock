package com.seven.quicklock;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;

public class MainActivity extends Activity {
	private DevicePolicyManager mPolicyManager;
	private ComponentName mComponetName;
	private PowerManager mPowerManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        
        mPolicyManager = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        mComponetName = new ComponentName(this, QuickLockDeviceReceiver.class);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        lockScreen();
        finish();
    }
    
    private void lockScreen(){
    	boolean isActive = mPolicyManager.isAdminActive(mComponetName);
    	if(isActive){
    		mPolicyManager.lockNow();
    		turnOffScreen();
    	}else {
			activeDevice();
		}
    }
    
    private void activeDevice(){
    	Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
    	intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponetName);
    	intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.admin_device_explain));
    	startActivity(intent);
    }
    
    /* turn off the screen when lockNow active*/
    private void turnOffScreen(){
//        mPowerManager.goToSleep(SystemClock.uptimeMillis());
//        mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "QuickLock...");
    }

}