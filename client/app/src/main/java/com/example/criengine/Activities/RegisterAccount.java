package com.example.criengine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles user account registration.
 * Outstanding issues:
 * - Does not push to the database.
 */
public class RegisterAccount extends AppCompatActivity {
    DatabaseWrapper dbw;
    Button returnButton;
    Button submitButton;
    EditText emailField;
    EditText passwordField;
    EditText firstnameField;
    EditText lastnameField;
    TextView warningMessage;
    String email;
    String password;
    String firstName;
    String lastName;
    private TextWatcher fieldTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // This method will not be used but is required as part of the TextWatcher anon-class.
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // This method will not be used but is required as part of the TextWatcher anon-class.
        }

        /**
         * This is a text watcher that will run every time a field has been updated.
         * @param charSequence {CharSequence} The character sequence.
         * @param i {int} This will be an unused variable.
         * @param i1 {i1} This will be an unused variable.
         * @param i2 {i2} This will be an unused variable.
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            email = emailField.getText().toString();
            checkAllFields();
        }
    };


    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_account);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            if (dbw == null) {
                dbw = new DatabaseWrapper(mAuth.getCurrentUser());
            }
        }

        // Assign the view objects.
        returnButton = findViewById(R.id.returnToLogin);
        submitButton = findViewById(R.id.submitAccount);
        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);
        firstnameField = findViewById(R.id.firstName);
        lastnameField = findViewById(R.id.lastName);
        warningMessage = findViewById(R.id.accountWarning);

        // Disable the submit button.
        submitButton.setEnabled(false);

        email = emailField.getText().toString();

        // Assign a text change listener to see if the button should be enabled.
        emailField.addTextChangedListener(fieldTextWatcher);
        passwordField.addTextChangedListener(fieldTextWatcher);
        firstnameField.addTextChangedListener(fieldTextWatcher);
        lastnameField.addTextChangedListener(fieldTextWatcher);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailField.getText().toString();
                password = passwordField.getText().toString();
                firstName = firstnameField.getText().toString();
                lastName = lastnameField.getText().toString();

                // TODO: Create new account.

                // Proceed into the app.
                Intent intent = new Intent(v.getContext(), RootActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * Checks all fields and runs validation on the email.
     * If all checks pass, this will enable the submit-button.
     */
    private void checkAllFields() {
        submitButton.setEnabled(true);
        warningMessage.setVisibility(View.GONE);
        if (isEmpty(emailField) || isEmpty(passwordField) ||
                isEmpty(firstnameField) || isEmpty(lastnameField) ||
                validateEmail(email)) {
            warningMessage.setVisibility(View.VISIBLE);
            submitButton.setEnabled(false);
        }
    }

    /**
     * Checks if a field is empty.
     * @param field The field to check.
     * @return True if the field is empty.
     */
    private boolean isEmpty(EditText field) {
        return field.getText().toString().trim().length() == 0;
    }

    /**
     * Checks if the email entered is a valid email.
     * @param email The email entered.
     * @return False if the email IS valid.
     */
    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return !matcher.find();
    }
}
