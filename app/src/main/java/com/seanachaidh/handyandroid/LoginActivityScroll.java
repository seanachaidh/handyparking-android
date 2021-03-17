package com.seanachaidh.handyandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ScrollView;
import androidx.appcompat.widget.LinearLayoutCompat;

public class LoginActivityScroll extends ScrollView {

    private LinearLayoutCompat login_view;
    private LinearLayoutCompat register_view;

    private Boolean use_login = false;

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
        LoginActivityClickListener clickListener = new LoginActivityClickListener((LoginActivity) this.getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.login_view = (LinearLayoutCompat) inflater.inflate(R.layout.login_layout, null);
        this.register_view = (LinearLayoutCompat) inflater.inflate(R.layout.register_layout, null);

        Button register_confirmation_button = this.register_view.findViewById(R.id.register_confirmation_button);
        register_confirmation_button.setOnClickListener(clickListener);

        Button login_confirmation_button = this.login_view.findViewById(R.id.login_confirmation_button);
        login_confirmation_button.setOnClickListener(clickListener);

        //we beginnen met register
        this.addView(this.register_view);
    }

    public LoginActivityScroll(Context context) {
        super(context);
        createViews();
    }

    public LoginActivityScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        createViews();
    }

    public LoginActivityScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createViews();
    }

    public LoginActivityScroll(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        createViews();
    }

}
