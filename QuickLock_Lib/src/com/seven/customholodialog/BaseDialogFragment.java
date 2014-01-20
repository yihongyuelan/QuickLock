
package com.seven.customholodialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.SDL_Dialog);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity(), inflater, container);
        return build(builder).create();
    }

    public abstract Builder build(Builder initialBuilder);

    public class Builder {
        private final LayoutInflater mInflater;
        private final ViewGroup mContainer;
        private final Context mContext;
        private String mTitle = null;
        private String mMessage = null;
        private CharSequence mPositiveButtonText;
        private View.OnClickListener mPositiveButtonListener;
        private CharSequence mNegativeButtonText;
        private View.OnClickListener mNegativeButtonListener;
        private CheckBox mCheckBox = null;
        private View mView;

        public Builder(Context context, LayoutInflater inflater, ViewGroup container) {
            this.mContext = context;
            this.mInflater = inflater;
            this.mContainer = container;
        }

        public void setTitle(String title) {
            this.mTitle = title;
        }

        public void setTitle(int titleResourceId) {
            this.mTitle = mContext.getString(titleResourceId);
        }

        public void setMessage(String message) {
            this.mMessage = message;
        }

        public void setMessage(int messageResourceId) {
            this.mMessage = mContext.getString(messageResourceId);
        }

        public void setPositiveButton(int textId, final View.OnClickListener listener) {
            mPositiveButtonText = mContext.getText(textId);
            mPositiveButtonListener = listener;
        }

        public void setPositiveButton(CharSequence text, final View.OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
        }

        public void setNegativeButton(int textId, final View.OnClickListener listener) {
            mNegativeButtonText = mContext.getText(textId);
            mNegativeButtonListener = listener;
        }

        public void setNegativeButton(CharSequence text, final View.OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonListener = listener;
        }

        public void setView(View view) {
            mView = view;
        }

        public void setDismissListener() {
            
        }

        public View create() {
//                if (null == mCheckBox) {
//                    mCheckBox = (CheckBox) viewMessage.findViewById(R.id.dialog_checkbox);
//                }
            View v = getDialogLayoutAndInitTitle();
            LinearLayout content = (LinearLayout) v.findViewById(R.id.sdl__content);
            if (null != mMessage) {
                View viewMessage = mInflater.inflate(R.layout.dialog_part_message, content, false);
                TextView tvMessage = (TextView) viewMessage.findViewById(R.id.sdl__message);
                tvMessage.setText(mMessage);
                content.addView(viewMessage);
            }
            if (mView != null) {
                FrameLayout customPanel = (FrameLayout) mInflater.inflate(
                        R.layout.dialog_part_custom, content, false);
                FrameLayout custom = (FrameLayout) customPanel.findViewById(R.id.sdl__custom);
                custom.addView(mView, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                if (mViewSpacingSpecified) {
//                    custom.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight,
//                            mViewSpacingBottom);
//                }
                content.addView(customPanel);
            }
            addButtons(content);
            return v;
        }

        private View getDialogLayoutAndInitTitle() {
            View v = mInflater.inflate(R.layout.dialog_part_title, mContainer, false);
            TextView tvTitle = (TextView) v.findViewById(R.id.sdl__title);
            View viewTitleDivider = v.findViewById(R.id.sdl__titleDivider);
            if (mTitle != null) {
                tvTitle.setText(mTitle);
            } else {
                tvTitle.setVisibility(View.GONE);
                viewTitleDivider.setVisibility(View.GONE);
            }
            return v;
        }

        private void addButtons(LinearLayout llListDialog) {
            if (mNegativeButtonText != null || mPositiveButtonText != null) {
                View viewButtonPanel = mInflater.inflate(R.layout.dialog_part_button_panel, llListDialog, false);
                LinearLayout llButtonPanel = (LinearLayout) viewButtonPanel.findViewById(R.id.dialog_button_panel);

                boolean addDivider = false;

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    addDivider = addPositiveButton(llButtonPanel, addDivider);
                } else {
                    addDivider = addNegativeButton(llButtonPanel, addDivider);
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    addNegativeButton(llButtonPanel, addDivider);
                } else {
                    addPositiveButton(llButtonPanel, addDivider);
                }

                llListDialog.addView(viewButtonPanel);
            }
        }

        private boolean addNegativeButton(ViewGroup parent, boolean addDivider) {
            if (mNegativeButtonText != null) {
                if (addDivider) {
                    addDivider(parent);
                }
                Button btn = (Button) mInflater.inflate(R.layout.dialog_part_button, parent, false);
                btn.setId(R.id.sdl_negative_button);
                btn.setText(mNegativeButtonText);
                //btn.setTextColor(mButtonTextColor);
                //btn.setBackgroundDrawable(getButtonBackground());
                btn.setOnClickListener(mNegativeButtonListener);
                parent.addView(btn);
                return true;
            }
            return addDivider;
        }

        private boolean addPositiveButton(ViewGroup parent, boolean addDivider) {
            if (mPositiveButtonText != null) {
                if (addDivider) {
                    addDivider(parent);
                }
                Button btn = (Button) mInflater.inflate(R.layout.dialog_part_button, parent, false);
                btn.setId(R.id.sdl_positive_button);
                btn.setText(mPositiveButtonText);
                //btn.setTextColor(mButtonTextColor);
                //btn.setBackgroundDrawable(getButtonBackground());
                btn.setOnClickListener(mPositiveButtonListener);
                parent.addView(btn);
                return true;
            }
            return addDivider;
        }

        private void addDivider(ViewGroup parent) {
            View view = mInflater.inflate(R.layout.dialog_part_button_separator, parent, false);
            //view.findViewById(R.id.dialog_button_separator).setBackgroundDrawable(new ColorDrawable(mButtonSeparatorColor));
            parent.addView(view);
        }
    }

}
