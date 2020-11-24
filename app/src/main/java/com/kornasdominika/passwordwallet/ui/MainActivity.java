package com.kornasdominika.passwordwallet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kornasdominika.passwordwallet.R;
import com.kornasdominika.passwordwallet.presenter.interfaces.ILogin;
import com.kornasdominika.passwordwallet.presenter.Login;
import com.kornasdominika.passwordwallet.ui.interfaces.ILoginActivity;

public class MainActivity extends AppCompatActivity implements ILoginActivity {

    private ILogin login;

    private EditText etLogin, etPassword, etIP;
    private ImageButton ivRefresh;
    private Button btnLog;
    private TextView tvSign;

    public static final String USER_ID = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = new Login(this, getApplicationContext());

        findComponentsIds();
        setOnClick();
    }

    @Override
    public void showMessageForUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startWalletActivity(int user) {
        Intent intent = new Intent(MainActivity.this, WalletActivity.class);
        intent.putExtra(USER_ID, user);
        startActivity(intent);
        finishAffinity();
    }

    private void findComponentsIds() {
        etIP = findViewById(R.id.fake_ip);
        etLogin = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        ivRefresh = findViewById(R.id.refresh);
        btnLog = findViewById(R.id.log_in);
        tvSign = findViewById(R.id.sign_up);
    }

    private void setOnClick() {
        btnLog.setOnClickListener(view -> {
            String ip = String.valueOf(etIP.getText());
            String username = String.valueOf(etLogin.getText());
            String password = String.valueOf(etPassword.getText());
            if(loginValidation(username, password)){
                login.loginIntoWallet(ip, username, password);
            }
        });

        ivRefresh.setOnClickListener(view -> login.refreshIP());

        tvSign.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
    }

    private boolean loginValidation(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    public void disableButton(){
        btnLog.setEnabled(false);
    }

    public void enableButton(){
        btnLog.setEnabled(true);
    }
}