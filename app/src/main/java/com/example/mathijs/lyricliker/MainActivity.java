package com.example.mathijs.lyricliker;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tutorial used for textinputlayout:
        // https://code.tutsplus.com/tutorials/creating-a-login-screen-using-textinputlayout--cms-24168
        // Set up Text Input Layouts with hints
        final TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        emailWrapper.setHint("Email Address");
        passwordWrapper.setHint("Password");

        // Set up listeners for the buttons
        Button logInButton = (Button) findViewById(R.id.logInButton);
        Button createAccountButton = (Button) findViewById(R.id.createAccountButton);
        logInButton.setOnClickListener(new logInListener());
        createAccountButton.setOnClickListener(new createAccountListener());

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and proceed to the app.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        proceedToApp(currentUser);
    }

    private void proceedToApp(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(this, SongListActivity.class);
            startActivity(intent);
        }
    }

    private class createAccountListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        }
    }

    private class logInListener implements View.OnClickListener {
        // Regex for email validation:
        // http://www.regular-expressions.info/email.html
        private static final String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        private Matcher matcher;

        @Override
        public void onClick(View view) {
            // Hide the keyboard when login is pressed
            hideKeyboard();

            final TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
            final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

            String email = emailWrapper.getEditText().getText().toString();
            String password = passwordWrapper.getEditText().getText().toString();
            if (!validateEmail(email)) {
                // Validate email address and throw error if invalid
                emailWrapper.setError("Not a valid email address!");
            } else if (!validatePassword(password)) {
                // Idem for the password
                passwordWrapper.setError("Passwords should be at least 6 characters long");
            } else {
                // Login when fields are valid
                emailWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);
                logIn(email, password);
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
    }

    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Success", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            proceedToApp(user);
                        } else {
                            Log.w("Fail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
