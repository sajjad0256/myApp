package com.example.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login,register;
    //hi sajjad
    //welcome
    //min

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        login = findViewById(R.id.btn_login_start);
        register = findViewById(R.id.btn_register_start);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_login_start:
                startActivity(new Intent(StartActivity.this,LoginActivity.class));

                break;

            case R.id.btn_register_start:
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
                break;
        }

    }
}
