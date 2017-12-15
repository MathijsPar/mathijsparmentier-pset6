package com.example.mathijs.lyricliker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set up Text Input Layouts with hints
        final TextInputLayout usernameSUWrapper = (TextInputLayout) findViewById(R.id.usernameSUWrapper);
        final TextInputLayout emailSUWrapper = (TextInputLayout) findViewById(R.id.emailSUWrapper);
        final TextInputLayout passwordSUWrapper = (TextInputLayout) findViewById(R.id.passwordSUWrapper);
        final TextInputLayout repeatPasswordSUWrapper = (TextInputLayout) findViewById(R.id.repeatPasswordSUWrapper);

        usernameSUWrapper.setHint("Username");
        emailSUWrapper.setHint("Email Address");
        passwordSUWrapper.setHint("Password");
        repeatPasswordSUWrapper.setHint("Repeat password");

        // Set up listeners for the button
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new signUpListener());

        mAuth = FirebaseAuth.getInstance();

    }

    private class signUpListener implements View.OnClickListener {
        // Regex for email validation:
        // http://www.regular-expressions.info/email.html
        private static final String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        private Matcher matcher;

        @Override
        public void onClick(View view) {
            final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameSUWrapper);
            final TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.emailSUWrapper);
            final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordSUWrapper);
            final TextInputLayout repeatPasswordWrapper = (TextInputLayout) findViewById(R.id.repeatPasswordSUWrapper);

            String username = emailWrapper.getEditText().getText().toString();
            String email = emailWrapper.getEditText().getText().toString();
            String password = passwordWrapper.getEditText().getText().toString();
            String repeatPassword = repeatPasswordWrapper.getEditText().getText().toString();

            hideKeyboard();

            if (!validateEmail(email)) {
                // Validate email address and throw error if invalid
                emailWrapper.setError("Not a valid email address!");
            } else if (!validatePassword(password)) {
                // Idem for the password
                passwordWrapper.setError("Passwords should be at least 6 characters long");
            } else if (!validateRepeatPassword(password, repeatPassword)) {
                // Make sure the repeated password is the same
                repeatPasswordWrapper.setError("Passwords don't match");
            } else {
                // Make the account when all fields are valid
                makeAccount(username, email, password);
            }
        }

        private void hideKeyboard() {
            View view = getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(view.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        private boolean validateEmail(String email) {
            matcher = pattern.matcher(email);
            return matcher.matches();
        }

        private boolean validatePassword(String password) {
            return password.length() > 5;
        }

        private boolean validateRepeatPassword(String password, String repeatPassword) {
            return password.equals(repeatPassword);
        }
    }

    private void makeAccount(final String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, log in with the signed-in user's information
                            Log.d("Success", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            proceedToApp(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            FirebaseException e = (FirebaseException )task.getException();
                            Toast.makeText(getApplicationContext(), "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            Log.w("Fail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            proceedToApp(null);
                        }
                    }
                });
    }

    private void proceedToApp(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(this, SongListActivity.class);
            startActivity(intent);
        }
    }
}