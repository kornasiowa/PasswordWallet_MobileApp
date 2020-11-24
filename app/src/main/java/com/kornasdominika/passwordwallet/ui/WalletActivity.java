package com.kornasdominika.passwordwallet.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kornasdominika.passwordwallet.R;
import com.kornasdominika.passwordwallet.presenter.Wallet;
import com.kornasdominika.passwordwallet.presenter.interfaces.IWallet;
import com.kornasdominika.passwordwallet.ui.adapters.PasswordsListAdapter;
import com.kornasdominika.passwordwallet.ui.interfaces.IWalletActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WalletActivity extends AppCompatActivity implements IWalletActivity {

    private IWallet wallet;

    private Toolbar toolbar;
    private FloatingActionButton fab;

    public static final String USER_ID = "user";

    private PasswordsListAdapter passwordsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        wallet = new Wallet(this, getApplicationContext());

        findComponentsIds();
        setOnClick();
        setListAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_pass:
                startNewActivity(ChangePasswordActivity.class, 2);
                return true;
            case R.id.log_out: {
                startActivity(new Intent(WalletActivity.this, MainActivity.class));
                finishAffinity();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLoggedUserId() {
        Intent intent = getIntent();
        return intent.getIntExtra(MainActivity.USER_ID, -1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                passwordsListAdapter.changeCursor(wallet.getUserPasswords(getLoggedUserId()));
            }

        }
    }

    private void findComponentsIds() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
    }

    private void setOnClick() {
        setSupportActionBar(toolbar);

        fab.setOnClickListener(view -> {
            startNewActivity(AddPasswordActivity.class, 1);
        });
    }

    private void setListAdapter() {
        ListView listView = findViewById(R.id.lv);
        passwordsListAdapter = new PasswordsListAdapter(this, wallet.getUserPasswords(getLoggedUserId()), true);
        listView.setAdapter(passwordsListAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Cursor cursor = (Cursor) passwordsListAdapter.getCursor();
            cursor.moveToPosition(i);
            createDialog(cursor);
        });
    }

    private void createDialog(Cursor cursor){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_show_password, null);

        TextView tvLogin = dialogView.findViewById(R.id.login);
        TextView tvpassword = dialogView.findViewById(R.id.password);
        ImageView ivKey = dialogView.findViewById(R.id.show_password);

        tvLogin.setMovementMethod(new ScrollingMovementMethod());
        tvpassword.setMovementMethod(new ScrollingMovementMethod());

        tvLogin.setHorizontallyScrolling(true);
        tvpassword.setHorizontallyScrolling(true);

        tvLogin.setText(cursor.getString(3));
        tvpassword.setText(cursor.getString(4));
        ivKey.setOnClickListener(view -> {
            tvpassword.setInputType(InputType.TYPE_CLASS_TEXT);
            tvpassword.setText(wallet.showDecryptedPassword(getLoggedUserId(), cursor.getString(4)));
        });

        dialog.setView(dialogView);
        dialog.create();
        dialog.show();
    }

    private void startNewActivity(Class activityClass, int requestCode){
        Intent intent = new Intent(WalletActivity.this, activityClass);
        intent.putExtra(USER_ID, getLoggedUserId());
        startActivityForResult(intent, requestCode);
    }
}