
package com.seven.quicklock;

import com.seven.customholodialog.CheckBoxDialogFragment;
import com.seven.customholodialog.DialogInfo;
import com.seven.quicklock.QuickLockActiveDialog.CheckChangeListener;
import com.seven.quicklock.QuickLockActiveDialog.NegativeButtonListener;
import com.seven.quicklock.QuickLockActiveDialog.PositiveButtonListener;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class QuickLockMainActivity extends Activity {
    private static final String PKG_NAME = "com.seven.quicklock";
    private static final String TAG = "QuickLockActiveDialog";
    private static final int mNotificationId = 0x00;
    private DevicePolicyManager mPolicyManager;
    private boolean isAddShortCut = false;
    private Context mContext;
    private Activity mActivity;
    private ComponentName mComponetName;
    private DialogInfo mDialogInfo;
    private CheckBoxDialogFragment mDialogFragment;
    private QuickLockUtils mUtils;
    private boolean shouldCreateShortcuts = true;
    private boolean isFromIntent = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        isFromIntent = getIntent().getBooleanExtra("NotiClicked", false);
        if (isFromIntent) {
            lockScreen();
        }else {
            createNotification();
            lockScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Seven", "xxxxxxx");
        if (resultCode == RESULT_OK) {
            if (shouldCreateShortcuts) {
                mUtils.addShortCut(mContext, PKG_NAME);
            }
        }
        exit();
    }

    @Override
    protected void onDestroy() {
        Log.e("Seven", "0000000");
        super.onDestroy();
    }

    private void init() {
        mContext = this;
        mActivity = this;
        mUtils = QuickLockUtils.getInstance();
        mPolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
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
            activeDevice();
            dismissActiveDialog();
        }
    }

    public class NegativeButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismissActiveDialog();
            exit();
        }
    }

    public class CheckChangeListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            shouldCreateShortcuts = isChecked;
        }
    }

    private void lockScreen() {
        boolean isActive = mPolicyManager.isAdminActive(mComponetName);
        if (isActive) {
            mPolicyManager.lockNow();
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
        startActivityForResult(intent, 23);
    }

    private void exit() {
        finish();
    }

    private void createNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                 .setSmallIcon(R.drawable.quicklock)
                 .setPriority(Notification.PRIORITY_MIN)
                 .setOngoing(true)
                 .setContentTitle("Quick lock is here")
                 .setContentText("Click to lock now");

        Intent resultIntent = new Intent(this, QuickLockMainActivity.class);
        resultIntent.putExtra("NotiClicked", true);
        PendingIntent resultPendingIntent =
            PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}
