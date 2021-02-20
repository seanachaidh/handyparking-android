package com.seanachaidh.handyandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.seanachaidh.handyparking.Resources.RevertResource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CompletableFuture;

@RunWith(AndroidJUnit4.class)
public class LoginLogoutTest {
    private SharedPreferences mainActivitySharedPrefs = null;
    private MainActivity ctx;
    private MainActivity mainActivity;

    @BeforeClass
    public void setUpClass() {

        /*
         * Clear shared preferences for a fresh start
         */
        mainActivity = (MainActivity) InstrumentationRegistry.getInstrumentation().getTargetContext();

        this.mainActivitySharedPrefs = mainActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = this.mainActivitySharedPrefs.edit();
        editor.clear();
        editor.apply();
    }

    @Test
    public void TestRegister() {
        MainActivity toTest = ApplicationProvider.getApplicationContext();

        toTest.findViewById(R.id.register_button).performClick();

        ((EditText) toTest.findViewById(R.id.register_name_text)).setText("jan");
        ((EditText) toTest.findViewById(R.id.register_email_text)).setText("jan@jan.com");
        ((EditText) toTest.findViewById(R.id.register_password_text)).setText("12345");

        toTest.findViewById(R.id.register_confirmation_button).performClick();

        Intent nextIntent = new Intent(toTest, AppActivity.class);

    }

    @AfterClass
    public void tearDownClass() {
        RevertResource revert = new RevertResource(ClientSingleton.getInstance().getClient());
        final CompletableFuture<Boolean> future = revert.post(null, null, null);
        future.whenComplete((aBoolean, throwable) -> {
        });
        try {
            future.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
