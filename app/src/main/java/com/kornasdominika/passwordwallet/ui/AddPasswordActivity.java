package com.kornasdominika.passwordwallet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kornasdominika.passwordwallet.R;
import com.kornasdominika.passwordwallet.presenter.AddPassword;
import com.kornasdominika.passwordwallet.presenter.interfaces.IAddPassword;
import com.kornasdominika.passwordwallet.ui.interfaces.IAddPasswordActivity;

import static android.widget.Toast.makeText;

public class AddPasswordActivity extends AppCompatActivity implements IAddPasswordActivity {

    private IAddPassword addPassword;

    private ImageView ivBack;
    private EditText etWebAddr,
            etDescription,
            etLogin,
            etPassword;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        addPassword = new AddPassword(this, getApplicationContext());

        findComponentsIds();
        setOnClick();
    }

    @Override
    public void showMessageForUser(String message) {
        makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void findComponentsIds() {
        ivBack = findViewById(R.id.back);
        etWebAddr = findViewById(R.id.web_address);
        etDescription = findViewById(R.id.description);
        etLogin = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnSave = findViewById(R.id.save);
    }

    private void setOnClick() {
        ivBack.setOnClickListener(view -> finish());

        btnSave.setOnClickListener(view -> {
            String webAddress = String.valueOf(etWebAddr.getText());
            String description = String.valueOf(etDescription.getText());
            String login = String.valueOf(etLogin.getText());
            String password = String.valueOf(etPassword.getText());
            if (addingNewPasswordValidation(webAddress, description, login, password)) {
                addPassword.addNewPasswordToWallet(getLoggedUserId(), password, webAddress, description, login);
            }
        });
    }

    private boolean addingNewPasswordValidation(String webAddress, String description, String login, String password) {
        if (TextUtils.isEmpty(webAddress) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private int getLoggedUserId() {
        Intent intent = getIntent();
        return intent.getIntExtra(MainActivity.USER_ID, -1);
    }

}