package com.kornasdominika.passwordwallet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kornasdominika.passwordwallet.R;
import com.kornasdominika.passwordwallet.presenter.interfaces.IRegister;
import com.kornasdominika.passwordwallet.presenter.Register;
import com.kornasdominika.passwordwallet.ui.interfaces.IRegisterActivity;

import static android.widget.Toast.makeText;

public class RegisterActivity extends AppCompatActivity implements IRegisterActivity {

    private IRegister register;

    private EditText etLogin,
            etPassword,
            etPassword2;
    private RadioGroup radioGroup;
    private Button btnSign;
    private TextView tvLog;

    private String encryption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = new Register(this, getApplicationContext());

        findComponentsIds();
        setOnClick();
    }

    @Override
    public void showMessageForUser(String message) {
        makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finishAffinity();
    }

    private void findComponentsIds() {
        etLogin = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        etPassword2 = findViewById(R.id.password2);
        radioGroup = findViewById(R.id.radio);
        btnSign = findViewById(R.id.sign_up);
        tvLog = findViewById(R.id.log_in);
    }

    private void setOnClick() {
        btnSign.setOnClickListener(view -> {
            String login = String.valueOf(etLogin.getText());
            String password = String.valueOf(etPassword.getText());
            String password2 = String.valueOf(etPassword2.getText());

            register.saveNewUserIntoDatabase(login, password, password2, encryption);
        });

        tvLog.setOnClickListener(view -> {
            startLoginActivity();
        });

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = (RadioButton) radioGroup.findViewById(i);
            if (null != rb) {
                encryption = (String) rb.getText();
            }
        });
    }

//    private boolean registerValidation(String login, String password, String password2) {
//        if (!UsernameVaildator.nameValidation(login)) {
//            makeText(this, "Username format is incorrect. Allow only alphabet, dots and underscore", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
//            makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (password.length() < 8) {
//            makeText(this, "The password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (!password.equals(password2)) {
//            makeText(this, "Different passwords", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (encryption == null) {
//            makeText(this, "Choose your encryption method", Toast.LENGTH_SHORT).show();
//            return false;
//        } else
//            return true;
//    }
}