package com.seanachaidh.handyandroid;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
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
        Intent actualIntent = shadowOf((ContextWrapper)ApplicationProvider.getApplicationContext()).getNextStartedActivity();

        assertEquals(nextIntent.getComponent(), actualIntent.getComponent());

    }

    @AfterClass
    public void tearDownClass() {

    }

}
