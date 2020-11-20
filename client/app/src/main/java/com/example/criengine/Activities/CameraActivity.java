package com.example.criengine.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.R;

/**
 * This class is used to open a camera to take a photo. That image will be used for a book.
 * The main points from this class like accessing the camera were taken from this site:
 * https://www.geeksforgeeks.org/android-how-to-open-camera-through-intent-and-display-captured-image/
 * Accessed on 2020-11-18.
 */
public class CameraActivity extends AppCompatActivity {
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private ImageView newImage;
    private static final int pic_id = 123;
    private Bitmap photo;
    private Book book;
    private AlertDialog confirmDialog;
    DatabaseWrapper dbw = DatabaseWrapper.getWrapper();

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Grabs the book.
        if (getIntent().getExtras() != null) {
            book = (Book) getIntent().getSerializableExtra("Book");
            photo = (Bitmap) getIntent().getParcelableExtra("photo");
        } else {
            Intent intent = new Intent(this, SomethingWentWrong.class);
            startActivity(intent);
            return;
        }

        // Set the view objects.
        deleteButton = findViewById(R.id.delete_picture);
        cancelButton = findViewById(R.id.cancel_picture);
        saveButton = findViewById(R.id.save_camera_image);
        newImage = findViewById(R.id.camera_image);
        confirmDialog = confirmDelete();

        // If the book already has an image, then use that. Otherwise set a default image.
        if (photo != null) {
            newImage.setImageBitmap(photo);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            // Only show the delete button if there is an image.
            newImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_book));
            deleteButton.setVisibility(View.GONE);
        }

        // Define the actions to perform when clicking the delete button.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.setImageURL(null);
                confirmDialog.show();
            }
        });

        // Define the actions to perform when clicking the cancel button.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        // Open the camera when clicking on the image.
        newImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);
            }
        });

        // Define the actions to perform when clicking the save button.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo != null) {
                    dbw.uploadBookImage(book, photo);
                }
                goBack();
            }
        });
    }

    /**
     * Retrieved from:
     * https://stackoverflow.com/questions/11740311/android-confirmation-message-for-delete
     * @return The confirmation dialog. If user selects to delete the book, then redirect to
     *          the main activity.
     */
    private AlertDialog confirmDelete()
    {
        return new AlertDialog.Builder(this)
                // set title and message and button behaviors
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // TODO: Image for this book has already been set to null. Push changes to db.
                        dialog.dismiss();
                        goBack();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    /**
     * Sets the image to the newly generated one.
     * @param requestCode The request code.
     * @param resultCode The result code.
     * @param data The data sent from the camera.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id && data.getExtras() != null) {
            // BitMap is data structure of image file which store the image in memory.
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // Set the image in imageview for display
            newImage.setImageBitmap(photo);
        }
    }

    /**
     * Returns to the previous screen and prevents the need to load the database when we land.
     * All information about the book is contained in the "Extra".
     */
    private void goBack() {
        Intent intent = new Intent(this, MyBookActivity.class);
        intent.putExtra("Book", book);
        startActivity(intent);
    }

    /**
     * Override the back button so it returns to the previous activity and not in the editing
     * screen.
     */
    @Override
    public void onBackPressed() {
        goBack();
    }
}