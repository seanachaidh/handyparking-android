package com.seanachaidh.handyandroid;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.seanachaidh.handyparking.Resources.LoginResource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    public void handleLogout(MenuItem item) {
        LoginResource resource = new LoginResource(ClientSingleton.getInstance().getClient());
        final AppActivity ctx = this;
        String token = this.getIntent().getStringExtra("token");
        if(!token.equals("")) {
            HashMap<String, String> headers = new HashMap<>();

            headers.put("Authorization", "bearer " + token);
            CompletableFuture<Boolean> result =  resource.post(null, null, headers);
            result.whenComplete(new BiConsumer<Boolean, Throwable>() {
                @Override
                public void accept(Boolean aBoolean, Throwable throwable) {
                    ctx.getIntent().putExtra("token", true);
                    finishActivity(1);
                }
            });
        }
    }
}