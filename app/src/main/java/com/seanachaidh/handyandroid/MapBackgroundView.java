package com.seanachaidh.handyandroid;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class MapBackgroundView extends WebView {
    private GeolocationPermissions.Callback geoCallback;

    private class BackgroundClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, true);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.d("JavaScript", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }

    public MapBackgroundView(Context context) {
        super(context);
        doSettings();

        String contents = loadStream(getResources().openRawResource(R.raw.loginbackground));
        loadUrl("https://seanachaidh.be/handybronnen/loginbackground.html");

    }

    public MapBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,  attrs, defStyleAttr);
        doSettings();

        String contents = loadStream(getResources().openRawResource(R.raw.loginbackground));
        loadUrl("https://seanachaidh.be/handybronnen/loginbackground.html");
    }

    public MapBackgroundView(Context context, AttributeSet attrs) {
        super(context,  attrs);
        doSettings();

        String contents = loadStream(getResources().openRawResource(R.raw.loginbackground));
        loadUrl("https://seanachaidh.be/handybronnen/loginbackground.html");
    }


    private String loadStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    private void loadWebpage(String contents) {
        //WebView mainview = (WebView) findViewById(R.id.backgroundWebview);
        //mainview.loadData(contents, "text/html", "UTF-8");
    }


    private void doSettings() {
        getSettings().setDomStorageEnabled(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setGeolocationEnabled(true);

        BackgroundClient client = new BackgroundClient();

        this.setWebChromeClient(client);
    }

}
