package com.seven.customholodialog;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;


public abstract class CheckBoxDialogFragment extends BaseDialogFragment{

    private CheckBox mCheckBox;
    private String content;

    public CheckBoxDialogFragment(String content) {
        this.content = content;
    }

    public CheckBoxDialogFragment(int contentResourceId) {
        this.content = getActivity().getString(contentResourceId);
    }

    public abstract Builder buildCheckBox(BuilderCheckBox builder);

    public class BuilderCheckBox {
        
    }

    @Override
    public Builder build(Builder builder) {
        View viewCheckBox = (CheckBox) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_part_checkbox, null);
        mCheckBox = (CheckBox) viewCheckBox.findViewById(R.id.dialog_checkbox);
        if (null != content) {
            mCheckBox.setText(content);
        }
        builder.setView(viewCheckBox);
        return builder;
    }

}
