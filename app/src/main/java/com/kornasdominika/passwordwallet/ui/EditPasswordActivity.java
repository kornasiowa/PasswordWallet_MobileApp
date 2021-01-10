package com.kornasdominika.passwordwallet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kornasdominika.passwordwallet.R;
import com.kornasdominika.passwordwallet.model.Password;
import com.kornasdominika.passwordwallet.presenter.EditPassword;
import com.kornasdominika.passwordwallet.presenter.interfaces.IEditPassword;
import com.kornasdominika.passwordwallet.ui.interfaces.IEditPasswordActivity;

public class EditPasswordActivity extends AppCompatActivity implements IEditPasswordActivity {

    private IEditPassword editPassword;

    private ImageView ivBack,
            ivLogo;
    private TextView tvInfo;
    private EditText etWebAddr,
            etDescription,
            etLogin,
            etPassword;
    private Button btnSave;

    private Password currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        editPassword = new EditPassword(this, getApplicationContext());
        getPasswordData();
        findComponentsIds();
        setStartData();
        setOnClick();
    }

    @Override
    public void showMessageForUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void findComponentsIds() {
        ivLogo = findViewById(R.id.logo);
        ivBack = findViewById(R.id.back);
        tvInfo = findViewById(R.id.info);
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
            if (editPasswordValidation(webAddress, description, login, password)) {
                currentPassword.setWebAddress(webAddress);
                currentPassword.setDescription(description);
                currentPassword.setLogin(login);
                currentPassword.setPassword(password);
                editPassword.updatePassword(currentPassword);
            }
        });
    }

    private void getPasswordData() {
        Intent intent = getIntent();
        currentPassword = (Password) intent.getSerializableExtra("PASSWORD");
    }

    private void setStartData() {
        ivLogo.setImageResource(R.drawable.red_logo);
        tvInfo.setText(getResources().getText(R.string.edit_pass));
        etWebAddr.setText(currentPassword.webAddress);
        etDescription.setText(currentPassword.description);
        etLogin.setText(currentPassword.login);
        etPassword.setText(editPassword.getCurrentPassword(currentPassword.uid, currentPassword.password));
    }

    private boolean editPasswordValidation(String webAddress, String description, String login, String password) {
        if (TextUtils.isEmpty(webAddress) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

}