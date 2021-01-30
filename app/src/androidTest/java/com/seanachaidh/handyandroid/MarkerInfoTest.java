package com.seanachaidh.handyandroid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.util.ReflectionUtil;
import androidx.test.platform.app.InstrumentationRegistry;

import com.seanachaidh.handyparking.Coordinate;
import com.seanachaidh.handyparking.ParkingSpot;
import com.seanachaidh.handyparking.Resources.ParkingspotSpecificResource;

import org.apache.hc.core5.concurrent.CompletedFuture;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.robolectric.util.ReflectionHelpers;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MarkerInfoTest {

    private static ParkingspotSpecificResource parkingspotSpecificResource = ClientSingleton
            .getInstance()
            .getParkingspotSpecificResource();

    @Rule
    public ActivityScenarioRule<MarkerInfoActivity> activityRule = new ActivityScenarioRule<>(MarkerInfoActivity.class);

    @BeforeClass
    public static void setUp() {



    }


    private static ParkingSpot createParking() {
        ParkingSpot retval = new ParkingSpot(null, (float) 5.0, false, new Coordinate(0,0));
        ReflectionHelpers.setField(retval, "id", "123");
        return retval;
    }
    @Before
    public void setUpAll() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", "123");
        ParkingSpot parkingSpot = createParking();
        ParkingSpot[] parkingSpots = {parkingSpot};

        CompletableFuture<ParkingSpot[]> parkingspotFuture = CompletableFuture.completedFuture(parkingSpots);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Drawable drawable = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getDrawable(R.drawable.kaart_brussel_openstreetmap);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        CompletableFuture<ByteBuffer> future = CompletableFuture.completedFuture(ByteBuffer.wrap(outputStream.toByteArray()));

        when(parkingSpot.downloadImage(ClientSingleton.getInstance().getClient())).thenReturn(future);
        when(parkingspotSpecificResource.get(params, null, null)).thenReturn(parkingspotFuture);
        
    }
}
