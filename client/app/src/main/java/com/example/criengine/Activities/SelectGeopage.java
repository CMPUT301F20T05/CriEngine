package com.example.criengine.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * Drop a marker at a specific location and then perform
 * reverse geocoding to retrieve and display the location's address
 *
 * Adapted from source: https://docs.mapbox.com/android/maps/examples/location-picker/
 */
public class SelectGeopage extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Button confirmLocationButton;
    private PermissionsManager permissionsManager;
    private ImageView hoveringMarker;
    private LocationEngine locationEngine;
    private LatLng givenLocation;
    private Layer droppedMarkerLayer;
    private DatabaseWrapper dbw;
    private String acceptedUserID;
    private ArrayList<String> requesters;
    private Book requestedBook;


    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grabs the accepted user, and book to accept.
        if (getIntent().getExtras() != null) {
            acceptedUserID = (String) getIntent().getSerializableExtra("acceptedUser");
            requesters = (ArrayList<String>) getIntent().getSerializableExtra("users");
            requestedBook = (Book) getIntent().getSerializableExtra("book");
        } else {
            Intent intent = new Intent(this, SomethingWentWrong.class);
            startActivity(intent);
            return;
        }
        dbw = DatabaseWrapper.getWrapper();

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_select_geopage);

        // check if a location was passed in
        if (getIntent().getExtras() != null) {
            givenLocation = (LatLng) getIntent().getSerializableExtra("location");
        } else {
            givenLocation = null;
        }

        // Initialize the mapboxMap view
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    /**
     * Called when the map is ready to be drawn to
     * @param mapboxMap The MapboxMap instance
     */
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        SelectGeopage.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onStyleLoaded(@NonNull final Style style) {
                if (givenLocation == null) {
                    enableLocationPlugin(style);

                    // When user is still picking a location, we hover a marker above the mapboxMap in the center.
                    // This is done by using an image view with the default marker found in the SDK. You can
                    // swap out for your own marker image, just make sure it matches up with the dropped marker.
                    hoveringMarker = new ImageView(SelectGeopage.this);
                    hoveringMarker.setImageResource(R.drawable.mapbox_marker_icon_default);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                    hoveringMarker.setLayoutParams(params);
                    mapView.addView(hoveringMarker);

                    initDroppedMarker(style);

                    // Button for user to drop marker or to pick marker back up.
                    confirmLocationButton = findViewById(R.id.confirm_location_button);
                    confirmLocationButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Use the map target's coordinates to store the location
                            final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;
                            requestedBook.setGeolocation(mapTargetLatLng.toString());
                            dbw.addBook(requestedBook);
                            // Reject every request except the accepted one
                            for (int i = 0; i < requesters.size(); i++) {
                                if (requesters.get(i).equals(acceptedUserID)) {
                                    dbw.acceptRequest(requesters.get(i), requestedBook.getBookID());
                                } else {
                                    dbw.declineRequest(requesters.get(i), requestedBook.getBookID());
                                }
                            }
//                            finish();
                            Intent intent = new Intent(view.getContext(), RootActivity.class);
                            intent.putExtra("Index", RootActivity.PAGE.MY_BOOKS);
                            view.getContext().startActivity(intent);
                        }
                    });
                } else {
                    mapboxMap.setCameraPosition(new CameraPosition.Builder()
                            .target(givenLocation)
                            .zoom(10)
                            .build());
                    GeoJsonSource source = style.getSourceAs("dropped-marker-source-id");
                    if (source != null) {
                        source.setGeoJson(Point.fromLngLat(givenLocation.getLongitude(), givenLocation.getLatitude()));
                    }
                    droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                    if (droppedMarkerLayer != null) {
                        droppedMarkerLayer.setProperties(visibility(VISIBLE));
                    }
                    confirmLocationButton.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Creates the persistent marker that's invisible upon creation
     * @param loadedMapStyle The Mapbox style object of this instance
     */
    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
        // Add the marker image to map
        loadedMapStyle.addImage("dropped-icon-image", BitmapFactory.decodeResource(
                getResources(), R.drawable.mapbox_marker_icon_default));
        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                visibility(NONE),
                iconImage("dropped-icon-image"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        ));
    }

    /**
     * When the app is resumed, the map resumes
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * When the app is started, the map starts
     */
    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * When the app is stopped, the map stops
     */
    @Override
    protected void onStop() {
        super.onStop();

        mapView.onStop();
    }

    /**
     * When the app is paused, the map is paused
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * The saved instance state of the app and map
     * @param outState The bundle being saved
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * Destroy the map when the app is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Calls map onLowMemory when app onLowMemory is triggered
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Get permissions from the device
     * @param requestCode The request code
     * @param permissions The permissions being asked for
     * @param grantResults The users result to be put in
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Gives more information about the permission being requested
     * @param permissionsToExplain The permissions needing more explanation
     */
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Please give permission to access location", Toast.LENGTH_LONG).show();
    }

    /**
     * Updates when permission is given
     * @param granted If permission was given
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPermissionResult(boolean granted) {
        if (granted && mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                enableLocationPlugin(style);
            }
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Sets the location to the device's current location
     * @param loadedMapStyle The Mapbox Style object of this instance
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (
            PermissionsManager.areLocationPermissionsGranted(this)
            && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional
            // parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.setLocationComponentEnabled(false);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
}