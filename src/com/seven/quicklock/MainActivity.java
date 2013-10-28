
package com.seven.quicklock;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity {
    private DevicePolicyManager mPolicyManager;
    private ComponentName mComponetName;
    private PowerManager mPowerManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mComponetName = new ComponentName(this, QuickLockDeviceReceiver.class);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        lockScreen();
    }

    private void lockScreen() {
        boolean isActive = mPolicyManager.isAdminActive(mComponetName);
        if (isActive) {
            mPolicyManager.lockNow();
            turnOffScreen();
            exit();
        } else {
            showConfirmDialog();
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.dialog_title).setIcon(R.drawable.quicklock)
                .setMessage(R.string.dialog_content_msg)
                .setPositiveButton(R.string.dialog_active, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activeDevice();
                        exit();
                    }
                }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                    }
                }).show();
    }

    private void activeDevice() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponetName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.admin_device_explain));
        startActivity(intent);
    }

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
