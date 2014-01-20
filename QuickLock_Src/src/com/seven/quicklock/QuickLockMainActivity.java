
package com.seven.quicklock;

import com.seven.customholodialog.CheckBoxDialogFragment;
import com.seven.customholodialog.DialogInfo;
import com.seven.quicklock.QuickLockActiveDialog.CheckChangeListener;
import com.seven.quicklock.QuickLockActiveDialog.NegativeButtonListener;
import com.seven.quicklock.QuickLockActiveDialog.PositiveButtonListener;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class QuickLockMainActivity extends Activity {
    private static final String PKG_NAME = "com.seven.quicklock";
    private static final String TAG = "QuickLockActiveDialog";
    private DevicePolicyManager mPolicyManager;
    private PowerManager mPowerManager;
    private boolean isAddShortCut = false;
    private Context mContext;
    private Activity mActivity;
    private ComponentName mComponetName;
    private DialogInfo mDialogInfo;
    private CheckBoxDialogFragment mDialogFragment;
    private QuickLockUtils mUtils;
    private boolean shouldCreateShortcuts = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        lockScreen();
    }

    private void init() {
        mContext = this;
        mActivity = this;
        mUtils = QuickLockUtils.getInstance();
        mPolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mComponetName = new ComponentName(this, QuickLockDeviceReceiver.class);
        mDialogInfo = getDialogInfo();
        mDialogFragment = new CheckBoxDialogFragment(mDialogInfo);
    }

    private DialogInfo getDialogInfo() {
        DialogInfo dialogInfo = new DialogInfo();
        dialogInfo.activity = mActivity;
        dialogInfo.title = getString(R.string.dialog_title);
        dialogInfo.message = getString(R.string.dialog_message);
        dialogInfo.checkBoxInfo = getString(R.string.dialog_checkbox_info);
        dialogInfo.positiveButtonText = getString(R.string.dialog_btn_positive);
        dialogInfo.negativeButtonText = getString(R.string.dialog_btn_negative);
        dialogInfo.positiveButtonListener = new PositiveButtonListener();
        dialogInfo.negativeButtonListener = new NegativeButtonListener();
        dialogInfo.checkedChangeListener = new CheckChangeListener();
        return dialogInfo;
    }

    private void showActiveDialog() {
        if (null != mDialogFragment) {
            mDialogFragment.show(mActivity.getFragmentManager(), TAG);
        }
    }

    private void dismissActiveDialog() {
        if (null != mDialogFragment) {
            mDialogFragment.dismiss();
            mDialogFragment = null;
        }
    }

    public class PositiveButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.i("Seven", "PositiveButton click");
            activeDevice();
            dismissActiveDialog();
        }
    }

    public class NegativeButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.i("Seven", "NegativeButton click");
            dismissActiveDialog();
        }
    }

    public class CheckChangeListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.i("Seven", "isChecked="+isChecked);
            shouldCreateShortcuts = isChecked;
        }
    }

    private void lockScreen() {
        boolean isActive = mPolicyManager.isAdminActive(mComponetName);
        if (isActive) {
            mPolicyManager.lockNow();
            turnOffScreen();
            exit();
        } else {
            showActiveDialog();
        }
    }

    private void activeDevice() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponetName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.admin_device_explain));
        startActivity(intent);
        if (shouldCreateShortcuts) {
            //can't work
            //mUtils.addShortCut(mContext, PKG_NAME);
        }
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
