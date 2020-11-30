package com.example.criengine.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * Handles displaying information about a book. Items such as the title, author, description etc.
 */
public class NonOwnerBookViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button requestBookButton;
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDetail;
    private EditText bookISBN;
    private EditText bookStatus;
    private EditText bookOwner;
    private EditText bookBorrower;
    private TextView bookBorrowerLabel;
    private DatabaseWrapper dbw = DatabaseWrapper.getWrapper();
    private Book book;
    private ImageView image;
    private Profile userProfile;
    private RootActivity.PAGE previousPage;
    private MapView mapView;
    private TextView meetingLocationText;
    private LatLng givenLocation;
    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    private Layer droppedMarkerLayer;

    // Testing config
    private boolean PREVENT_BORROW_OWN_BOOK = true;
    /**
     * A custom onCreate() method. Allows for the usage for fragments in the activity.
     * Without this method, there is the possible issue of the fragment being null when it is called
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grabs the book.
        if (getIntent().getExtras() != null) {
            book = (Book) getIntent().getSerializableExtra("Book");
            previousPage = (RootActivity.PAGE) getIntent().getSerializableExtra("Page");
        } else {
            Intent intent = new Intent(this, SomethingWentWrong.class);
            startActivity(intent);
            return;
        }

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_non_owner_book_view);

        // My book UI components.
        requestBookButton = findViewById(R.id.request_book_button);
        bookTitle = findViewById(R.id.bookView_title);
        bookDetail = findViewById(R.id.bookView_detail);
        bookAuthor = findViewById(R.id.bookView_author);
        bookISBN = findViewById(R.id.bookView_ISBN);
        bookStatus = findViewById(R.id.bookView_status);
        bookOwner = findViewById(R.id.bookView_owner);
        bookBorrower = findViewById(R.id.bookView_borrower);
        bookBorrowerLabel = findViewById(R.id.bookView_borrower_label);
        image = findViewById(R.id.bookView_image);
        mapView = findViewById(R.id.mapView);
        meetingLocationText = findViewById(R.id.meeting_location);
        final ScrollView sv = (ScrollView) findViewById(R.id.bookView_scrollView);

        // Initially set the button to be false.
        requestBookButton.setEnabled(false);

        disableEditText(bookTitle);
        disableEditText(bookDetail);
        disableEditText(bookAuthor);
        disableEditText(bookISBN);
        disableEditText(bookStatus);
        disableEditText(bookOwner);
        disableEditText(bookBorrower);

        // Get the user profile and set the status of the button.
        dbw.getProfile(dbw.userId).addOnSuccessListener(
                new OnSuccessListener<Profile>() {
                    @Override
                    public void onSuccess(Profile profile) {
                        userProfile = profile;
                        checkIfAvailable();
                        if (book.getLatLng() != null && book.getRequesters().contains(userProfile.getUserID())) {
                            mapView.setVisibility(View.VISIBLE);
                            meetingLocationText.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        bookTitle.setText(book.getTitle());
        bookDetail.setText(book.getDescription());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(book.getIsbn());
        bookStatus.setText(book.getStatus());
        bookOwner.setText(book.getOwnerUsername());
        if (book.getBorrower() != null) {
            dbw.getProfile(book.getBorrower()).addOnSuccessListener(
                    profile -> {
                        bookBorrower.setText(profile.getUsername());
                        bookBorrower.setVisibility(View.VISIBLE);
                        bookBorrowerLabel.setVisibility(View.VISIBLE);
                    }
            );
        } else {
            bookBorrowerLabel.setVisibility(View.GONE);
            bookBorrower.setVisibility(View.GONE);
        }

        givenLocation = null;
        if (book.getLatLng() != null) {
            givenLocation = book.getLatLng();
            mapView.onCreate(savedInstanceState);

            mapView.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            sv.requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            sv.requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return mapView.onTouchEvent(event);
                }
            });

            mapView.getMapAsync(this);
        }

        // Set the image to a default icon if there is no URL stored in the database.
        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_book));
        if (book.getImageURL() != null) {
            dbw.downloadBookImage(book).addOnSuccessListener(new OnSuccessListener<Bitmap>() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    if (bitmap != null) {
                        image.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }

    /**
     * Check if the book can be requested. Sets the button to the appropriate text.
     */
    public void checkIfAvailable() {
        if (book.getRequesters().contains(userProfile.getUserID())) {
            // The user has already requested this book.
            requestBookButton.setEnabled(false);
            requestBookButton.setText("Request Sent");
        } else if (userProfile.getBooksOwned().contains(book.getBookID()) && PREVENT_BORROW_OWN_BOOK) {
            // The book is owned by the user.
            requestBookButton.setEnabled(false);
            requestBookButton.setText("This is your Book");
        } else if (!book.getStatus().equals("borrowed") && !book.getStatus().equals("accepted")) {
            // The book is available for being requested.
            requestBookButton.setEnabled(true);
            requestBookButton.setText("Request This Book");
            // Add the user ID to the list of requesters and update it in the database.
            requestBookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbw.makeRequest(userProfile.getUserID(), book.getBookID());
                    requestBookButton.setEnabled(false);
                    requestBookButton.setText("Request Sent");
                }
            });
        } else if ((book.getStatus().equals("borrowed") || book.getStatus().equals("accepted")) && !userProfile.getWishlist().contains(book.getBookID())) {
            // The book is available for being wished.
            requestBookButton.setEnabled(true);
            requestBookButton.setText("Add To Wishlist");
            // Add to wishlist
            requestBookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbw.wishForBook(userProfile.getUserID(), book.getBookID());
                    requestBookButton.setEnabled(false);
                    requestBookButton.setText("Added to wishlist");
                }
            });
        } else if ((book.getStatus().equals("borrowed") || book.getStatus().equals("accepted")) && userProfile.getWishlist().contains(book.getBookID())) {
            // The book is available for being wished.
            requestBookButton.setEnabled(true);
            requestBookButton.setText("Remove from Wishlist");
            requestBookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbw.cancelWish(userProfile.getUserID(), book.getBookID());
                    requestBookButton.setEnabled(false);
                    requestBookButton.setText("Removed from wishlist");
                }
            });
        }
    }

    /**
     * Allows for incorporating the "Component_book.xml" file into this activity.
     * Treats the EditText as a TextView but keeps the formatting the same.
     * @param editText The view object.
     */
    public void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.BLACK);
    }

    @Override
    public void onBackPressed() {
        // go back to previous activity
        Intent intent = new Intent(this, RootActivity.class);
        if (previousPage == null) {
            previousPage = RootActivity.PAGE.REQUESTS;
        }

        // Goes back to page that called NonOwnerBookActivity
        if (previousPage == RootActivity.PAGE.OTHER) {
            super.onBackPressed();
        } else {
            intent.putExtra("Index", previousPage);
            startActivity(intent);
        }
    }

    /**
     * Called when the map is ready to be drawn to
     * @param mapboxMap The MapboxMap instance
     */
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onStyleLoaded(@NonNull final Style style) {
                initDroppedMarker(style);
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
}