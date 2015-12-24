package com.jarman.touchstone.moshihor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Requisition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
