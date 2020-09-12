import android.content.Context;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;

public class HandyWebView extends WebView {
    private class HandyClient extends WebChromeClient {

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            /*
            TODO: Vind hier een betere methode voor
             */
            callback.invoke(origin, true, false);
        }
    }

    public HandyWebView(@NonNull Context context) {
        super(context);
        doSettings();
    }

    private void doSettings() {
        this.getSettings().setGeolocationEnabled(true);
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setAppCacheEnabled(true);
        this.getSettings().setDatabaseEnabled(true);
        this.getSettings().setDomStorageEnabled(true);

        // Eigenlijk geen instelling, maar ik zet dit hier lekker toch
        this.setWebChromeClient(new HandyClient());

    }
}
