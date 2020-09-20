package com.seanachaidh.handyandroid;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.seanachaidh.handyparking.Resources.UserResource;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class MainActivityClickListener implements View.OnClickListener {
    private CloseableHttpClient client;
    private UserResource userResource;

    private String generateMD5(String s) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md5 != null;
        byte[] b = md5.digest(s.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte i: b) {
            sb.append(String.format("%02x",i & 0xff));
        }
        return sb.toString();
    }

    private void registerClick(final View view) {
        Log.d("clickbuttons", "Register has been clicked");
        LinearLayoutCompat scroll = (LinearLayoutCompat) view.getParent();

        EditText name = (EditText) scroll.findViewById(R.id.register_name_text);
        EditText email = (EditText) scroll.findViewById(R.id.register_email_text);
        EditText password = (EditText) scroll.findViewById(R.id.register_password_text);
        CheckBox guide = (CheckBox) scroll.findViewById(R.id.register_guide_check);

        String md5Password = generateMD5(password.getText().toString());

        HashMap<String, String> postParams = new HashMap<String, String>();

        postParams.put("name", name.getText().toString());
        postParams.put("email", email.getText().toString());
        postParams.put("password", md5Password);
        String bool = guide.isChecked() ? "1" : "0";
        postParams.put("guide", bool);

        CompletableFuture<Boolean> retval = userResource.post(null, postParams);
        retval.whenComplete(new BiConsumer<Boolean, Throwable>() {
            @Override
            public void accept(Boolean aBoolean, Throwable throwable) {
                if(aBoolean) {
                    Toast t = Toast.makeText(view.getContext(), "User gemaakt", Toast.LENGTH_LONG);
                    t.show();
                } else {
                    Toast t = Toast.makeText(view.getContext(), "User niet gemaakt", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });
    }

    private void loginClick(View view) {
        Log.d("general", "Not yet implemented");
    }

    public MainActivityClickListener() {
        this.client = ClientSingleton.getInstance().getClient();
        this.userResource = new UserResource(this.client);
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
