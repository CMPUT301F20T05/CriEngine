package com.example.criengine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.criengine.Database.DatabaseWrapper;
import com.example.criengine.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Handles the user login and password validation.
 */
public class LoginActivity extends AppCompatActivity {
    EditText loginEmail;
    EditText loginPassword;
    Button loginButton;
    DatabaseWrapper dbw;
    TextView register;

    /**
     * Called upon the creation of the activity. (Initializes the activity)
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            if (dbw == null) {
                dbw = new DatabaseWrapper(mAuth.getCurrentUser());
            }
        }

        // Assigns the view objects.
        register = findViewById(R.id.registerAccount);
        loginEmail = findViewById(R.id.loginEditTextEmail);
        loginPassword = findViewById(R.id.loginEditTextPassword);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View v) {
                    String email = loginEmail.getText().toString();
                    String password = loginPassword.getText().toString();
                    enableLogin(false);
                    if ((email.isEmpty() || password.isEmpty())) {
                        loginEmail.setError("Login Failed");
                        enableLogin(true);
                        return;
                    }

                    // Validates the user-password combo and then enters the app if successful.
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        final FirebaseUser user = mAuth.getCurrentUser();
                                        dbw = new DatabaseWrapper(user);

                                        Intent intent = new Intent(v.getContext(), RootActivity.class);
                                        v.getContext().startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        loginEmail.setError("Login Failed");
                                        enableLogin(true);
                                        // ...
                                    }
                                }
                            });

                }
            }
        );

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterAccount.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * Enables/Disables the login button.
     * @param flag True if we want to enable the button.
     */
    public void enableLogin(Boolean flag) {
        if (flag) {
            loginButton.setEnabled(true);
            loginButton.setText("Login");
        } else {
            loginButton.setEnabled(false);
            loginButton.setText("Processing");
        }
    }

    /**
     * Closes the app allowing the user to return to their home phone screen.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
};