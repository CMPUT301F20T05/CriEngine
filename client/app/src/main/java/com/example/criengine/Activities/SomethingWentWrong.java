package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.criengine.R;
import android.os.Bundle;

/**
 * An all purpose error screen.
 */
public class SomethingWentWrong extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.something_went_wrong);
    }
}