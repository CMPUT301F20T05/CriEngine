package com.example.criengine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.Objects.Book;
import com.example.criengine.Objects.Profile;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/**
 * Handles user account registration.
 * Outstanding issues:
 * - Does not push to the database.
 */
public class RegisterAccount extends AppCompatActivity {
//    DatabaseWrapper dbw;
    Button submitButton;
    EditText usernameField;
    EditText emailField;
    EditText passwordField;
    EditText firstnameField;
    EditText lastnameField;
    TextView warningMessage;
    String username;
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

        // Assign the view objects.
        submitButton = findViewById(R.id.submitAccount);
        usernameField = findViewById(R.id.editTextTextUsername);
        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);
        firstnameField = findViewById(R.id.firstName);
        lastnameField = findViewById(R.id.lastName);
        warningMessage = findViewById(R.id.accountWarning);

        email = emailField.getText().toString();

        // Assign a text change listener to see if the button should be enabled.
        enableSubmit(false);
        usernameField.addTextChangedListener(fieldTextWatcher);
        emailField.addTextChangedListener(fieldTextWatcher);
        passwordField.addTextChangedListener(fieldTextWatcher);
        firstnameField.addTextChangedListener(fieldTextWatcher);
        lastnameField.addTextChangedListener(fieldTextWatcher);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableSubmit(false);
                username = usernameField.getText().toString();
                email = emailField.getText().toString();
                password = passwordField.getText().toString();
                firstName = firstnameField.getText().toString();
                lastName = lastnameField.getText().toString();
                FirebaseFirestore.getInstance().collection("users").whereEqualTo("username",username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            if (query != null && query.isEmpty()) {
                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful() && task.getResult() != null) {
                                            AuthResult authResult = task.getResult();
                                            FirebaseUser user = authResult.getUser();
                                            Profile profile = new Profile(user.getUid(), email, username, firstName, lastName);
                                            DatabaseWrapper dbw = new DatabaseWrapper(user);
                                            dbw.addProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(v.getContext(), RootActivity.class);
                                                    v.getContext().startActivity(intent);
                                                }
                                            });
                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthWeakPasswordException e) {
                                                passwordField.setError(e.getReason());
                                            } catch (FirebaseAuthEmailException e) {
                                                emailField.setError(e.getMessage());
                                            } catch (FirebaseAuthUserCollisionException e) {
                                                emailField.setError(e.getMessage());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            enableSubmit(true);
                                            return;
                                        }
                                    }
                                });
                            } else {
                                usernameField.setError("This username is already taken!");
                                enableSubmit(true);
                            }
                        } else {
                            Log.d(TAG, "Get Failure: " + task.getException());
                            enableSubmit(true);
                            return;
                        }
                    }
                });
            }
        });
    }

    /**
     * Enable/Disable the submit button depending on input.
     * @param flag True if we want to enable the button.
     */
    public void enableSubmit(Boolean flag) {
        if (flag) {
            submitButton.setEnabled(true);
            submitButton.setText("Submit");
        } else {
            submitButton.setEnabled(false);
            submitButton.setText("Processing");
        }
    }

    /**
     * Checks all fields and runs validation on the email.
     * If all checks pass, this will enable the submit-button.
     */
    private void checkAllFields() {
        enableSubmit(true);
        warningMessage.setVisibility(View.GONE);
        if (isEmpty(usernameField) || isEmpty(emailField) || isEmpty(passwordField) ||
                isEmpty(firstnameField) || isEmpty(lastnameField) ||
                validateEmail(email)) {
            warningMessage.setVisibility(View.VISIBLE);
            enableSubmit(false);
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
