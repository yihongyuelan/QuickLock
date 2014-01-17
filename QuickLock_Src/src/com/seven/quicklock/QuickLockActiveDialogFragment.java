package com.seven.quicklock;

import com.seven.customholodialog.CheckBoxDialogFragment;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class QuickLockActiveDialogFragment extends CheckBoxDialogFragment{

    private static final String TAG = "QuickLockActiveDialogFragment";
    private Activity mActivity = null;
    private ComponentName mComponetName;
    private static QuickLockActiveDialogFragment mDialogFragment;

    private String mTitle;
    private String mMessage;
    private String mCheckBoxInfo;
    private String mPositiveButtonText;
    private String mNegativeButtonText;

    public QuickLockActiveDialogFragment(String checkBoxContent, Activity mActivity) {
        super(checkBoxContent);
        this.mActivity = mActivity;
    }

    public QuickLockActiveDialogFragment(int checkBoxContentResouceId, Activity mActivity) {
        super(checkBoxContentResouceId);
        this.mActivity = mActivity;
    }

    public static QuickLockActiveDialogFragment getInstance(Activity mActivity) {
        if (null == mDialogFragment) {
            mDialogFragment = new QuickLockActiveDialogFragment(mCheckBoxInfo, mActivity);
        }
        return mDialogFragment;
    }

    private void init() {
        mTitle = getString(R.string.dialog_title);
        mMessage = getString(R.string.dialog_message);
        mCheckBoxInfo = getString(R.string.dialog_checkbox_info);
        mPositiveButtonText = getString(R.string.dialog_btn_positive);
        mNegativeButtonText = getString(R.string.dialog_btn_negative);
    }

    public void show(ComponentName componentname) {
        mComponetName = componentname;
        mDialogFragment.show(mActivity.getFragmentManager(), TAG);
    }

    @Override
    public Builder build(Builder builder) {
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.dialog_btn_positive, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeDevice();
                dismiss();
            }
        });
        builder.setNegativeButton(R.string.dialog_btn_negative, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return super.build(builder);
    }

    private void activeDevice() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponetName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.admin_device_explain));
        startActivity(intent);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (null != mActivity) {
            Log.i("Seven", "onDismiss....before finish");
            mDialogFragment = null;
            mActivity.finish();
        }
    }

}
