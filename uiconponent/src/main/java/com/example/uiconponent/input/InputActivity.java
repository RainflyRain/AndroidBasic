package com.example.uiconponent.input;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.uiconponent.R;

import java.util.ArrayList;
import java.util.List;

public class InputActivity extends AppCompatActivity {

    private final String TAG = "InputActivity";

    private ConversationInputPanel inputPanel;
    private InputAwareLayout awareLinearLayout;
    private RecyclerView msgRecyclerView;

    private InputAdapter inputAdapter;

    private List<String> stringList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        msgRecyclerView = findViewById(R.id.msgRecyclerView);
        awareLinearLayout = findViewById(R.id.kalLayout);
        inputPanel = findViewById(R.id.inputPanel);
        inputPanel.init(null,awareLinearLayout);

        msgRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputPanel.closeConversationInputPanel();
                return false;
            }
        });

        inputAdapter = new InputAdapter();
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgRecyclerView.setAdapter(inputAdapter);

        stringList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            stringList.add("position = "+i);
        }
        inputAdapter.addData(0,stringList);
        msgRecyclerView.scrollToPosition(inputAdapter.getItemCount()-1);

        
        inputPanel.setOnConversationInputPanelStateChangeListener(new ConversationInputPanel.OnConversationInputPanelStateChangeListener() {
            @Override
            public void onInputPanelExpanded() {
                msgRecyclerView.scrollToPosition(inputAdapter.getItemCount() - 1);
                Log.i(TAG, "onInputPanelExpanded: ");
            }

            @Override
            public void onInputPanelCollapsed() {
                Log.i(TAG, "onInputPanelCollapsed: ");
            }
        });

        awareLinearLayout.addOnKeyboardShownListener(new KeyboardAwareLinearLayout.OnKeyboardShownListener() {
            @Override
            public void onKeyboardShown() {
                msgRecyclerView.scrollToPosition(inputAdapter.getItemCount() - 1);
                Log.i(TAG, "onKeyboardShown: ");
            }
        });
    }
}