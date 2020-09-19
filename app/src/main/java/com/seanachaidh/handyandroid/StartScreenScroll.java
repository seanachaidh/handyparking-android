package com.seanachaidh.handyandroid;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import androidx.appcompat.widget.LinearLayoutCompat;

public class StartScreenScroll extends ScrollView {

    private class RegisterOnClick implements OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }

    private class LoginOnClick implements OnClickListener {

        @Override
        public void onClick(View view) {
            Log.d("clickbuttons", "Login has been clicked");
        }
    }

    private LinearLayoutCompat login_view;
    private LinearLayoutCompat register_view;

    private Button register_confirmation_button;
    private Button login_confirmation_button;

    private MainActivityClickListener clickListener;


    private Boolean use_login = false;
    public void switchLayout() {
        this.removeAllViews();
        if(use_login) {
            this.addView(this.register_view);
        } else {
            this.addView(this.login_view);
        }
        this.use_login = !this.use_login;
    }

    public void useLogin() {
        if(!use_login) {
            this.removeAllViews();
            this.addView(login_view);
            this.use_login = true;
        }
    }

    public void useRegister() {
        if(use_login) {
            this.removeAllViews();
            this.addView(register_view);
            this.use_login = false;
        }
    }

    private void createViews() {
        this.clickListener = new MainActivityClickListener();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.login_view = (LinearLayoutCompat) inflater.inflate(R.layout.login_layout, null);
        this.register_view = (LinearLayoutCompat) inflater.inflate(R.layout.register_layout, null);

        this.register_confirmation_button = this.register_view.findViewById(R.id.register_confirmation_button);
        this.register_confirmation_button.setOnClickListener(this.clickListener);

        this.login_confirmation_button = this.login_view.findViewById(R.id.login_confirmation_button);
        this.login_confirmation_button.setOnClickListener(this.clickListener);

        //we beginnen met register
        this.addView(this.register_view);
    }

    public StartScreenScroll(Context context) {
        super(context);
        createViews();
    }

    public StartScreenScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        createViews();
    }

    public StartScreenScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createViews();
    }

    public StartScreenScroll(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        createViews();
    }

    public void confirmRegister(View view) {
        Log.d("general", "Register confirmation");
    }

}
