
package com.seven.quicklock;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class QuickLockMainActivity extends Activity {
    private static final String PKG_NAME = "com.seven.quicklock";
    private DevicePolicyManager mPolicyManager;
    private PowerManager mPowerManager;
    private boolean isAddShortCut = false;
    private Context mContext;
    private Activity mActivity;
    private ComponentName mComponetName;
    private QuickLockActiveDialogFragment mDialogFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        lockScreen();
    }

    private void init() {
        mContext = this;
        mActivity = this;
        mPolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mComponetName = new ComponentName(this, QuickLockDeviceReceiver.class);
        mDialogFragment = QuickLockActiveDialogFragment.getInstance(mActivity);
    }

    private void lockScreen() {
        boolean isActive = mPolicyManager.isAdminActive(mComponetName);
        if (isActive) {
            mPolicyManager.lockNow();
            turnOffScreen();
            exit();
        } else {
            showActiveDialog();
            //showConfirmDialog();
        }
    }

    private void showActiveDialog() {
        if (null != mDialogFragment) {
            mDialogFragment.show(mComponetName);
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_content_msg)
//                .setMultiChoiceItems(new String[]{"Add ShortCut?"}, new boolean[]{true}, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                        if (0 == which && isChecked) {
//                            isAddShortCut = true;
//                        }else {
//                            isAddShortCut = false;
//                        }
//                    }
//                })
                .setPositiveButton(R.string.dialog_active, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isAddShortCut) {
                            QuickLockUtils.getInstance().addShortCut(mContext, PKG_NAME);
                        }
//                        activeDevice();
                        exit();
                    }
                }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                    }
                }).show();
    }

//    private void activeDevice() {
//        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponetName);
//        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//                getString(R.string.admin_device_explain));
//        startActivity(intent);
//    }

    private void exit() {
        finish();
    }

    /* turn off the screen when lockNow active */
    private void turnOffScreen() {
        // mPowerManager.goToSleep(SystemClock.uptimeMillis());
        // mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
        // "QuickLock...");
    }

}
