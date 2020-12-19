package com.seanachaidh.handyandroid;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class HandyMarkerWindow extends BasicInfoWindow {
    private int mImageId;
    private Marker mMarkerRef;

    /**
     * @param layoutResId layout that must contain these ids: bubble_title,bubble_description,
     *                    bubble_subdescription, bubble_image
     * @param mapView
     */
    public HandyMarkerWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
        Context ctx = mapView.getContext();
        mImageId = ctx.getResources().getIdentifier("id/bubble_image", null, ctx.getPackageName());
    }

    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
        mMarkerRef = (Marker)item;
        if (mView==null) {
            Log.w(IMapView.LOGTAG, "Error trapped, MarkerInfoWindow.open, mView is null!");
            return;
        }
        //handle image
        ImageView imageView = (ImageView)mView.findViewById(mImageId /*R.id.image*/);
        Drawable image = mMarkerRef.getImage();
        if (image != null){
            imageView.setBackground(image); //or setBackgroundDrawable(image)?
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setVisibility(View.VISIBLE);
        } else
            imageView.setVisibility(View.GONE);
    }
}
