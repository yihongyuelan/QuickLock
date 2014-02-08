package com.seven.customholodialog;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;


public class CheckBoxDialogFragment extends BaseDialogFragment{

    private DialogInfo mInfo;

    public CheckBoxDialogFragment(DialogInfo info) {
        this.mInfo = info;
    }

    @Override
    public Builder build(Builder builder) {
        builder.setTitle(mInfo.title);
        builder.setMessage(mInfo.message);
        builder.setPositiveButton(mInfo.positiveButtonText, mInfo.positiveButtonListener);
        builder.setNegativeButton(mInfo.negativeButtonText, mInfo.negativeButtonListener);
        View viewCheckBox = (CheckBox) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_part_checkbox, null);
        CheckBox checkBox = (CheckBox) viewCheckBox.findViewById(R.id.dialog_checkbox);
        checkBox.setOnCheckedChangeListener(mInfo.checkedChangeListener);
        checkBox.setText(mInfo.checkBoxInfo);
        builder.setView(viewCheckBox);
        return builder;
    }

//    @Override
//    public void onPause() {
//        exit();
//        super.onPause();
//    }
//
//    private void exit() {
//        if (null != mInfo.activity) {
//            mInfo.activity.finish();
//        }
//    }

}
