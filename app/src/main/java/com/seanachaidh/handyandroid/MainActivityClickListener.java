package com.seanachaidh.handyandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.seanachaidh.handyparking.Resources.LoginResource;
import com.seanachaidh.handyparking.Resources.UserResource;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class MainActivityClickListener implements View.OnClickListener {
    private UserResource userResource;
    private LoginResource loginResource;
    private MainActivity activity;

    private String generateMD5(String s) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md5 != null;
        byte[] b = md5.digest(s.getBytes());
        StringBuilder sb = new StringBuilder();
        for(byte i: b) {
            sb.append(String.format("%02x",i & 0xff));
        }
        return sb.toString();
    }


    private void registerClick(final View view) {
        Log.d("clickbuttons", "Register has been clicked");
        final LinearLayoutCompat scroll = (LinearLayoutCompat) view.getParent();

        EditText name = scroll.findViewById(R.id.register_name_text);
        EditText email = scroll.findViewById(R.id.register_email_text);
        EditText password = scroll.findViewById(R.id.register_password_text);
        CheckBox guide = scroll.findViewById(R.id.register_guide_check);

        final String passwordText = password.getText().toString();
        String md5Password = generateMD5(passwordText);

        HashMap<String, String> postParams = new HashMap<>();

        postParams.put("name", name.getText().toString());
        final String emailText = email.getText().toString();
        postParams.put("email", emailText);
        postParams.put("password", md5Password);
        String bool = guide.isChecked() ? "1" : "0";
        postParams.put("guide", bool);

        CompletableFuture<Boolean> retval = userResource.post(null, postParams, null);
        retval.whenComplete(new BiConsumer<Boolean, Throwable>() {
            @Override
            public void accept(Boolean aBoolean, Throwable throwable) {
                if(aBoolean) {
                    MainActivity activity = (MainActivity) view.getContext().getApplicationContext();
                    performLogin(emailText, passwordText);
                } else {
                    Toast t = Toast.makeText(view.getContext(), "User niet gemaakt", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });
    }

    private String convertUsernameAndPassword(String username, String password) {
        // First the password needs to be MD5
        String md5Password = generateMD5(password);

        String toEncode = username + ":" + md5Password;
        Log.d("general", "Encoding username and password: " + toEncode);
        return Base64.encodeToString(toEncode.getBytes(), Base64.DEFAULT);
    }

    private void loginClick(final View view) {
        final SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        //TODO: maak hier de NULL token van
        String token = prefs.getString(view.getContext().getString(R.string.token_key), "");
        Log.d("general", "Current token: " + token);


        //get a new token
        LinearLayoutCompat scroll = (LinearLayoutCompat) view.getParent();
        EditText emailEdit = scroll.findViewById(R.id.login_email);
        EditText passwordEdit = scroll.findViewById(R.id.login_password);

        String username = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        performLogin(username, password);
    }

    private void performLogin(String username, String password) {
        String base64Credentials = convertUsernameAndPassword(username, password);
        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", "basic " + base64Credentials); // BASIC NIET VERGETEN!

        CompletableFuture<LoginResource.LoginResult[]> future = this.loginResource.get(null, null, header);
        future.whenComplete(new BiConsumer<LoginResource.LoginResult[], Throwable>() {
            @Override
            public void accept(LoginResource.LoginResult[] loginResults, Throwable throwable) {
                LoginResource.LoginResult first = loginResults[0];
                String token = first.token;
                SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putString(activity.getString(R.string.token_key), token);
                prefEditor.apply();

                Log.d("general", "new token: " + token);

                activity.startAppActivity(token);
            }
        });
    }

    public MainActivityClickListener(MainActivity parentActivity) {
        CloseableHttpClient client = ClientSingleton.getInstance().getClient();
        this.userResource = new UserResource(client);
        this.loginResource = new LoginResource(client);
        this.activity = parentActivity;
        Log.d("general", "Resource created: " + this.userResource.getFullURL());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_confirmation_button:
                loginClick(view);
                break;
            case R.id.register_confirmation_button:
                registerClick(view);
                break;
            default:
                break;
        }
    }
}
