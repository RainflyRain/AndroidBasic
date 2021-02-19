/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.example.uiconponent.input;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uiconponent.R;

public class ConversationInputPanel extends FrameLayout{

    LinearLayout inputContainerLinearLayout;
    TextView disableInputTipTextView;

    EditText editText;
    ImageView emotionImageView;
    ImageView extImageView;

    KeyboardHeightFrameLayout extContainerFrameLayout;

    private InputAwareLayout rootLinearLayout;

    private SharedPreferences sharedPreferences;

    private OnConversationInputPanelStateChangeListener onConversationInputPanelStateChangeListener;

    public ConversationInputPanel(@NonNull Context context) {
        super(context);
    }

    public ConversationInputPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public ConversationInputPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConversationInputPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public void setOnConversationInputPanelStateChangeListener(OnConversationInputPanelStateChangeListener onConversationInputPanelStateChangeListener) {
        this.onConversationInputPanelStateChangeListener = onConversationInputPanelStateChangeListener;
    }

    public void bind(FragmentActivity activity, InputAwareLayout rootInputAwareLayout) {

    }



    public void disableInput(String tip) {
        closeConversationInputPanel();
        inputContainerLinearLayout.setVisibility(GONE);
        disableInputTipTextView.setVisibility(VISIBLE);
        disableInputTipTextView.setText(tip);
    }

    public void enableInput() {
        inputContainerLinearLayout.setVisibility(VISIBLE);
        disableInputTipTextView.setVisibility(GONE);
    }

    public void init(Fragment fragment, final InputAwareLayout rootInputAwareLayout) {
        LayoutInflater.from(getContext()).inflate(R.layout.conversation_input_panel, this, true);

        this.rootLinearLayout = rootInputAwareLayout;

        sharedPreferences = getContext().getSharedPreferences("sticker", Context.MODE_PRIVATE);

        extImageView = findViewById(R.id.extImageView);
        extImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showConversationExtension();
            }
        });
        extContainerFrameLayout = findViewById(R.id.extContainerContainerLayout);
        editText = findViewById(R.id.editText);

        emotionImageView = findViewById(R.id.emotionImageView);
        emotionImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideConversationExtension();
                rootInputAwareLayout.showSoftkey(editText);
            }
        });

    }

    public void setInputText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        editText.setText(text);
        editText.setSelection(text.length());
        editText.requestFocus();
        rootLinearLayout.showSoftkey(editText);
    }

    private void showConversationExtension() {
        rootLinearLayout.show(editText, extContainerFrameLayout);
        if (onConversationInputPanelStateChangeListener != null) {
            onConversationInputPanelStateChangeListener.onInputPanelExpanded();
        }
    }

    private void hideConversationExtension() {
        if (onConversationInputPanelStateChangeListener != null) {
            onConversationInputPanelStateChangeListener.onInputPanelCollapsed();
        }
    }

    void closeConversationInputPanel() {
        rootLinearLayout.hideAttachedInput(true);
        rootLinearLayout.hideCurrentInput(editText);
    }

    public interface OnConversationInputPanelStateChangeListener {
        /**
         * 输入面板展开
         */
        void onInputPanelExpanded();

        /**
         * 输入面板关闭
         */
        void onInputPanelCollapsed();
    }
}
