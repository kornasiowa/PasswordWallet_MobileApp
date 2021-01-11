package com.kornasdominika.passwordwallet.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kornasdominika.passwordwallet.R;
import com.kornasdominika.passwordwallet.model.Password;
import com.kornasdominika.passwordwallet.presenter.Wallet;
import com.kornasdominika.passwordwallet.presenter.interfaces.IWallet;
import com.kornasdominika.passwordwallet.ui.adapters.PasswordsListAdapter;
import com.kornasdominika.passwordwallet.ui.interfaces.IWalletActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WalletActivity extends AppCompatActivity implements IWalletActivity {

    private IWallet wallet;

    private Toolbar toolbar;
    private TextView banner;
    private FloatingActionButton fab;
    private ListView listView;

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
        setBannerView();
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
                startNewActivity(SettingsActivity.class, 2);
                return true;
            case R.id.log_out: {
                startActivity(new Intent(WalletActivity.this, MainActivity.class));
                SettingsActivity.MODIFY_MODE = false;
                finishAffinity();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateList() {
        passwordsListAdapter.changeCursor(wallet.getUserPasswords(getLoggedUserId()));
    }

    @Override
    public void setListAdapter() {
        passwordsListAdapter = new PasswordsListAdapter(this, wallet.getUserPasswords(getLoggedUserId()), true);
        listView.setAdapter(passwordsListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                //updateList();
                setListAdapter();
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    private void findComponentsIds() {
        toolbar = findViewById(R.id.toolbar);
        banner = findViewById(R.id.banner);
        fab = findViewById(R.id.fab);
        listView = findViewById(R.id.lv);
    }

    private void setOnClick() {
        setSupportActionBar(toolbar);

        fab.setOnClickListener(view -> startNewActivity(AddPasswordActivity.class, 1));

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Cursor cursor = (Cursor) passwordsListAdapter.getCursor();
            cursor.moveToPosition(i);
            createDialog(cursor);
        });
    }

    private void setBannerView() {
        if (SettingsActivity.MODIFY_MODE) {
            banner.setText(getResources().getText(R.string.banner2));
        }
    }

    private void startNewActivity(Class activityClass, int requestCode) {
        Intent intent = new Intent(WalletActivity.this, activityClass);
        intent.putExtra(USER_ID, getLoggedUserId());
        startActivityForResult(intent, requestCode);
    }

    private int getLoggedUserId() {
        Intent intent = getIntent();
        return intent.getIntExtra(MainActivity.USER_ID, -1);
    }

    private void createDialog(Cursor cursor) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_show_password, null);
        dialog.setView(dialogView);

        LinearLayout linearLayout = dialogView.findViewById(R.id.from);
        TextView tvOwner = dialogView.findViewById(R.id.owner);
        TextView tvLogin = dialogView.findViewById(R.id.login);
        TextView tvPassword = dialogView.findViewById(R.id.password);
        ImageView ivKey = dialogView.findViewById(R.id.show_password);
        ImageView ivShare = dialogView.findViewById(R.id.share);
        ImageView ivEdit = dialogView.findViewById(R.id.edit);
        ImageView ivDelete = dialogView.findViewById(R.id.delete);

        tvLogin.setMovementMethod(new ScrollingMovementMethod());
        tvPassword.setMovementMethod(new ScrollingMovementMethod());

        tvLogin.setHorizontallyScrolling(true);
        tvPassword.setHorizontallyScrolling(true);

        int pid = cursor.getInt(0);
        int mid = cursor.getInt(6);
        int uid = cursor.getInt(7);
        int owner = cursor.getInt(5);
        String webAddr = cursor.getString(1);
        String description = cursor.getString(2);
        String login = cursor.getString(3);
        String password = cursor.getString(4);

        tvLogin.setText(login);
        tvPassword.setText(password);

        if (owner != -2) {
            ivShare.setImageResource(R.drawable.ic_block);
            ivEdit.setImageResource(R.drawable.ic_edit_gray);
            ivDelete.setImageResource(R.drawable.ic_delete_gray);
            linearLayout.setVisibility(View.VISIBLE);
            tvOwner.setText(wallet.getUserLogin(owner));
        }

        if (!SettingsActivity.MODIFY_MODE) {
            ivEdit.setImageResource(R.drawable.ic_edit_gray);
            ivDelete.setImageResource(R.drawable.ic_delete_gray);
        }

        final AlertDialog alertDialog = dialog.create();

        ivKey.setOnClickListener(view -> {
            ivKey.setClickable(false);
            tvPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            if (owner == -2) {
                tvPassword.setText(wallet.showDecryptedPassword(getLoggedUserId(), password));
            } else {
                tvPassword.setText(wallet.showDecryptedPassword(owner, password));
            }
        });

        ivShare.setOnClickListener(view -> {
            if (owner == -2) {
                Password sharedPassword = new Password(password, 0, webAddr, description, login, uid, pid);
                createShareDialog(sharedPassword);
            } else {
                wallet.stopPreviewingPassword(pid);
                alertDialog.dismiss();
            }
        });

        ivEdit.setOnClickListener(view -> {
            if (SettingsActivity.MODIFY_MODE) {
                if (owner == -2) {
                    Password editPassword = new Password(pid, password, uid, webAddr, description, login, owner, mid);
                    Intent intent = new Intent(WalletActivity.this, EditPasswordActivity.class);
                    intent.putExtra("PASSWORD", editPassword);
                    startActivityForResult(intent, 3);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(this, "You have to be the owner to edit this password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "You have to turn on MODIFY MODE to perform this action", Toast.LENGTH_SHORT).show();
            }
        });

        ivDelete.setOnClickListener(view -> {
            if (SettingsActivity.MODIFY_MODE) {
                if (owner == -2) {
                    ivDelete.setClickable(false);
                    Password deletedPassword = new Password(pid, password, uid, webAddr, description, login, -2, -1);
                    wallet.deletePassword(pid, deletedPassword);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(this, "You have to be the owner to delete this password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "You have to turn on MODIFY MODE to perform this action", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    private void createShareDialog(Password password) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_share, null);
        dialog.setView(dialogView);

        EditText etUsername = dialogView.findViewById(R.id.send_to);
        ImageView ivSend = dialogView.findViewById(R.id.send);

        final AlertDialog alertDialog = dialog.create();

        ivSend.setOnClickListener(view -> {
            if (TextUtils.isEmpty(String.valueOf(etUsername.getText()))) {
                Toast.makeText(this, "You have not entered a username", Toast.LENGTH_SHORT).show();
            } else {
                wallet.sharePassword(String.valueOf(etUsername.getText()), password, alertDialog);
            }
        });
        alertDialog.show();
    }

}