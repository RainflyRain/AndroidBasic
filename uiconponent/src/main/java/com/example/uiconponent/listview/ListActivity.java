package com.example.uiconponent.listview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.example.uiconponent.R;
import com.example.uiconponent.smartslide.dummy.DummyContent;

public class ListActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    ConstraintLayout fragmentContainer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
    }

    private void init() {
        fragmentContainer = findViewById(R.id.ui_fragment_list_container);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ui_fragment_list_container,ItemFragment.newInstance(1))
                .commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Toast.makeText(this,item.details,Toast.LENGTH_SHORT).show();
    }
}