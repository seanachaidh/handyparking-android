package com.seanachaidh.handyandroid;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.code.tempusfugit.temporal.Duration;
import com.google.code.tempusfugit.temporal.Timeout;
import com.google.code.tempusfugit.temporal.WaitFor;
import com.seanachaidh.handyparking.Coordinate;
import com.seanachaidh.handyparking.ParkingSpot;
import com.seanachaidh.handyparking.Resources.ParkingspotSpecificResource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MarkerInfoTest {

    private ParkingspotSpecificResource mockParkingSpecificResource;
    private ClientSingleton clientSingleton;

    @Before
    public void setupClient() {

    }


    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        mockParkingSpecificResource = mock(ParkingspotSpecificResource.class);
        clientSingleton = mock(ClientSingleton.class);
        ParkingSpot spot = makeParking(123, 50.0, 60.0);
        CompletableFuture<ParkingSpot[]> future = CompletableFuture.completedFuture(new ParkingSpot[]{spot});
        when(mockParkingSpecificResource.get(any(), nullable(HashMap.class), nullable(HashMap.class))).thenReturn(future);
        when(clientSingleton.getParkingspotSpecificResource()).thenReturn(mockParkingSpecificResource);
        Field singletonResourceField = ClientSingleton.class.getDeclaredField("instance");
        singletonResourceField.setAccessible(true);
        singletonResourceField.set(null, clientSingleton);
    }

    private ParkingSpot makeParking(int id, double longtitude, double latitude) throws NoSuchFieldException, IllegalAccessException {
        ParkingSpot parkingSpot = new ParkingSpot();
        Coordinate coordinate = new Coordinate(longtitude, latitude);
        parkingSpot.setCoordinate(coordinate);
        parkingSpot.setOccupied(false);
        Field idField = parkingSpot.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(parkingSpot, id);

        return parkingSpot;
    }

    @Test
    public void testShowAllInfo() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MarkerInfoActivity.class);
        intent.putExtra(ApplicationProvider.getApplicationContext().getString(R.string.INFO_VIEW_PARKINGID), 123);
        ActivityScenario<MarkerInfoActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {

            try {
                WaitFor.waitOrTimeout(activity::isLoaded, Timeout.timeout(Duration.millis(10000)));
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }

            TextView view = activity.findViewById(R.id.marker_info_longtitude);
            Assert.assertNotNull(view);
            double testLongtitude = Double.parseDouble(view.getText().toString());
            Assert.assertEquals(60.0, testLongtitude, 0.0);
        });
    }

}
