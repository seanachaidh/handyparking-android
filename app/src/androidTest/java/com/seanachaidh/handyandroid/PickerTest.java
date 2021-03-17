package com.seanachaidh.handyandroid;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.code.tempusfugit.temporal.Duration;
import com.google.code.tempusfugit.temporal.Timeout;
import com.google.code.tempusfugit.temporal.WaitFor;
import com.seanachaidh.handyandroid.mainapp.MapViewActivity;
import com.seanachaidh.handyandroid.mainapp.MarkerMapView;
import com.seanachaidh.handyparking.Coordinate;
import com.seanachaidh.handyparking.ParkingSpot;
import com.seanachaidh.handyparking.Resources.ParkingspotResource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.osmdroid.views.overlay.Marker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class PickerTest {
    private Intent intent;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        //We starten eerst de mapviewActivity
        intent = new Intent(ApplicationProvider.getApplicationContext(), MapViewActivity.class);
        ParkingspotResource parkingspotResource = Mockito.mock(ParkingspotResource.class);
        ClientSingleton clientSingleton = mock(ClientSingleton.class);

        ParkingSpot parkingSpot1 = new ParkingSpot(null, 5.0f, false, new Coordinate(50.83232, 4.54444));
        ParkingSpot parkingSpot2 = new ParkingSpot(null, 3.0f, false, new Coordinate(50.83396, 4.5406));

        CompletableFuture<ParkingSpot[]> future = CompletableFuture.completedFuture(new ParkingSpot[]{parkingSpot1, parkingSpot2});
        when(parkingspotResource.get(any(), any(), any())).thenReturn(future);
        when(clientSingleton.getParkingspotResource()).thenReturn(parkingspotResource);
        Field singletonResourceField = ClientSingleton.class.getDeclaredField("instance");
        singletonResourceField.setAccessible(true);
        singletonResourceField.set(null, clientSingleton);
    }
    @Test
    public void testMarkersPlaced() {
        ActivityScenario<MapViewActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            MarkerMapView markerMapView = activity.findViewById(R.id.markerMap);

            try {
                WaitFor.waitOrTimeout(markerMapView::isLoaded, Timeout.timeout(Duration.millis(10000)));
            } catch (TimeoutException | InterruptedException exeption) {
                exeption.printStackTrace();
            }

            List<Marker> markers = markerMapView.getAllMarkders();
            Assert.assertEquals(2, markers.size());
        });
    }
}
