package com.kornasdominika.passwordwallet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kornasdominika.passwordwallet.R;
import com.kornasdominika.passwordwallet.presenter.ChangePassword;
import com.kornasdominika.passwordwallet.presenter.interfaces.IChangePassword;
import com.kornasdominika.passwordwallet.ui.adapters.LogsListAdapter;
import com.kornasdominika.passwordwallet.ui.adapters.PasswordsListAdapter;
import com.kornasdominika.passwordwallet.ui.interfaces.IChangePasswordActivity;

import static android.widget.Toast.makeText;

public class ChangePasswordActivity extends AppCompatActivity implements IChangePasswordActivity {

    private IChangePassword changePassword;

    private Toolbar toolbar;
    private EditText etCurrentPass, etNewPass, etNewPass2;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        changePassword = new ChangePassword(this, getApplicationContext());

        findComponentsIds();
        setOnClick();
        setListAdapter();
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

    @Override
    public int getLoggedUserId() {
        Intent intent = getIntent();
        return intent.getIntExtra(MainActivity.USER_ID, -1);
    }

    private void findComponentsIds() {
        toolbar = findViewById(R.id.toolbar);
        etCurrentPass = findViewById(R.id.password);
        etNewPass = findViewById(R.id.new_password);
        etNewPass2 = findViewById(R.id.new_password2);
        btnSave = findViewById(R.id.save);
    }

    private void setOnClick() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());

        btnSave.setOnClickListener(view -> {
            String currentPass = String.valueOf(etCurrentPass.getText());
            String newPass = String.valueOf(etNewPass.getText());
            String newPass2 = String.valueOf(etNewPass2.getText());

            if (changePasswordValidation(currentPass, newPass, newPass2)) {
                changePassword.changeUserMasterPassword(getLoggedUserId(), currentPass, newPass);
            }
        });

    }

    private boolean changePasswordValidation(String currentPassword, String newPassword, String newPassword2) {
        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword)) {
            makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (newPassword.length() < 8) {
            makeText(this, "The password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!newPassword.equals(newPassword2)) {
            makeText(this, "Different passwords", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private void setListAdapter() {
        ListView listView = findViewById(R.id.lv);
        LogsListAdapter logsListAdapter = new LogsListAdapter(this, changePassword.getUserLogs(getLoggedUserId()), true);
        listView.setAdapter(logsListAdapter);

        listView.setOnItemLongClickListener(((adapterView, view, i, l) -> {
            Cursor cursor = (Cursor) logsListAdapter.getCursor();
            cursor.moveToPosition(i);
            createResetProcedure(cursor.getInt(0));
            return true;
        }));
    }

    private void createResetProcedure(int lid) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Reset the number of invalid login attempts?");
        dialog.setMessage("Resetting the number of invalid login attempts allows you to log in from a permanently blocked IP address. " +
                "In order to protect the account against unauthorized access, it is also recommended to update the access password.");
        dialog.setPositiveButton("Reset", (dialogInterface, i) -> {
            changePassword.resetLoginAttempts(lid);
        });

        dialog.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}