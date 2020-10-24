package com.example.criengine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button myProfileActivityButton;
    private Button requestActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestActivityButton = findViewById(R.id.requested_books_activity);

        requestActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RequestActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        // router to other activities for testing
        myProfileActivityButton = findViewById(R.id.my_profile_activity_button);

        myProfileActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyProfileActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}