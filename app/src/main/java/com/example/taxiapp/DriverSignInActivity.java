package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverSignInActivity extends AppCompatActivity {
    private TextInputLayout text_input_email;
    private TextInputLayout text_input_name;
    private TextInputLayout text_input_password;
    private TextInputLayout text_input_confirmation_password;
    private Button button_sign_in_driver;
    private TextView tap_to_login_text_view;
    private boolean isLoginModeActive;
    private static final  String TAG = "ololo";

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_in);
        firebaseInitAndSetup();

        if (firebaseAuth.getCurrentUser() !=null){
            startActivity(new Intent( DriverSignInActivity.this, DriverMapsActivity.class));
            finish();
        }
        initAndBuildViews();
    }

    private void firebaseInitAndSetup() {
        firebaseAuth = FirebaseAuth.getInstance();
    }



    private void initAndBuildViews() {
        text_input_email = findViewById(R.id.text_input_email);
        text_input_name = findViewById(R.id.text_input_name);
        text_input_password = findViewById(R.id.text_input_password);
        text_input_confirmation_password = findViewById(R.id.text_input_confirmation_password);
        button_sign_in_driver = findViewById(R.id.button_sign_in_driver);
        tap_to_login_text_view = findViewById(R.id.tap_to_login_text_view);
    }

    private boolean validateEmail() {
        String emailInput = text_input_email.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            text_input_email.setError("пожалуйста, введите свой адрес электронной почты");
            return false;
        } else {
            text_input_email.setError("");
            return true;
        }
    }

    private boolean validateName() {
        String nameInput = text_input_name.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()) {
            text_input_name.setError("пожалуйста, введите ваше имя");
            return false;
        } else if (nameInput.length() > 20) {
            text_input_name.setError("длина имени должна быть меньше 20");
            return false;
        } else {
            text_input_name.setError("");
            return true;
        }
    }

    private boolean validatePassword() {

        String passwordInput = text_input_password.getEditText().getText()
                .toString().trim();

        if (passwordInput.isEmpty()) {
            text_input_password.setError("Пожалуйста, введите ваш пароль");
            return false;
        } else if (passwordInput.length() < 7) {
            text_input_password.setError("Длина пароля должна быть больше 6");
            return false;
        } else {
            text_input_password.setError("");
            return true;
        }

    }

    private boolean validateConfirmPassword() {
        String passwordInput = text_input_password.getEditText().getText().toString().trim();
        String confirmPasswordInput = text_input_confirmation_password.getEditText().getText().toString().trim();
        if (!passwordInput.equals(confirmPasswordInput)) {
            text_input_password.setError("Пароли должны совпадать");
            return false;
        } else {
            text_input_password.setError("");
            return true;
        }
    }

    public void loginSignUpDriver(View view) {
        if (!validateEmail() | !validateName() | !validatePassword()){
            return;
        }

        if (isLoginModeActive){
            firebaseAuth.signInWithEmailAndPassword(
                    text_input_email.getEditText().getText().toString().trim(),
                    text_input_password.getEditText().getText().toString().trim()
            ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                startActivity(new Intent(DriverSignInActivity.this,DriverMapsActivity.class));
                                FirebaseUser user = firebaseAuth.getCurrentUser();
//                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(DriverSignInActivity.this, "Ошибка аутентификации. ",
                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }else {
            if (!validateEmail() | !validateName() | !validatePassword() | !validateConfirmPassword()){
                return;
            }
            firebaseAuth.createUserWithEmailAndPassword(
                    text_input_email.getEditText().getText().toString().trim(),
                    text_input_password.getEditText().getText().toString().trim()
            ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        startActivity(new Intent(DriverSignInActivity.this,DriverMapsActivity.class));
                        FirebaseUser user = firebaseAuth.getCurrentUser();
//                            updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(DriverSignInActivity.this, "Ошибка аутентификации.",
                                Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                    }

                    // ...
                }
            });
        }
    }

    public void toggleLoginSignUp(View view) {
        if (isLoginModeActive){
            isLoginModeActive = false;
            button_sign_in_driver.setText("Зарегистрироваться");
            tap_to_login_text_view.setText("или войти в систему");
            text_input_confirmation_password.setVisibility(View.VISIBLE);
        }else {
            isLoginModeActive = true;
            button_sign_in_driver.setText("Aвторизоваться");
            tap_to_login_text_view.setText("или зарегистрируйтесь");
            text_input_confirmation_password.setVisibility(View.GONE);
        }
    }
}