package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.app.mylibertadriver.R;

public class TermsActivity extends AppCompatActivity {
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        web = findViewById(R.id.web);
        web.loadUrl("http://myliberta.com/mobile/agreement.html");
    }
    public void onBack(View v) {
        finish();
    }
}
