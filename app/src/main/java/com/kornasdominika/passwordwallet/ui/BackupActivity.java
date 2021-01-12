package com.kornasdominika.passwordwallet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kornasdominika.passwordwallet.R;
import com.kornasdominika.passwordwallet.model.DataChange;
import com.kornasdominika.passwordwallet.presenter.Backup;
import com.kornasdominika.passwordwallet.presenter.interfaces.IBackup;
import com.kornasdominika.passwordwallet.ui.adapters.FunctionsListAdapter;
import com.kornasdominika.passwordwallet.ui.interfaces.IBackupActivity;

public class BackupActivity extends AppCompatActivity implements IBackupActivity {

    private IBackup backup;

    private Toolbar toolbar;
    private Spinner spnAction;
    private ListView listView;

    private FunctionsListAdapter functionsListAdapter;

    public static String SELECTED_ACTION = "All actions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        backup = new Backup(this, getApplicationContext());

        findComponentsIds();
        setSpinner();
        setListAdapter(SELECTED_ACTION);
        setOnClick();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setListAdapter(String type) {
        if (type.equals("All actions")) {
            functionsListAdapter = new FunctionsListAdapter(this, backup.getAllUserFunctionsStatistics(getLoggedUserId()), true);
        } else {
            functionsListAdapter = new FunctionsListAdapter(this, backup.getAllUserFunctionsStatistics(getLoggedUserId(), type), true);
        }
        listView.setAdapter(functionsListAdapter);
    }

    private void findComponentsIds() {
        toolbar = findViewById(R.id.toolbar);
        spnAction = findViewById(R.id.action);
        listView = findViewById(R.id.lv);
    }

    private void setOnClick() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finishActivity());

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Cursor cursor = (Cursor) functionsListAdapter.getCursor();
            cursor.moveToPosition(i);
            int fid = cursor.getInt(0);
            String action = cursor.getString(3);
            createDialog(fid, action);
        });
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.action, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAction.setAdapter(adapter);

        spnAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SELECTED_ACTION = adapterView.getItemAtPosition(i).toString();
                setListAdapter(SELECTED_ACTION);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void finishActivity() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private int getLoggedUserId() {
        Intent intent = getIntent();
        return intent.getIntExtra(MainActivity.USER_ID, -1);
    }

    private void createDialog(int fid, String action) {
        DataChange dataChange = backup.getDataChangeToRestore(fid);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_restore, null);
        dialog.setView(dialogView);

        LinearLayout linearLayoutAll = dialogView.findViewById(R.id.ll_all);
        LinearLayout llPresentData = dialogView.findViewById(R.id.present_data);
        TextView tvTime = dialogView.findViewById(R.id.time);
        TextView tvHeader = dialogView.findViewById(R.id.header);
        TextView tvWeb = dialogView.findViewById(R.id.web_addr);
        TextView tvWeb2 = dialogView.findViewById(R.id.web_addr2);
        TextView tvDescription = dialogView.findViewById(R.id.description);
        TextView tvDescription2 = dialogView.findViewById(R.id.description2);
        TextView tvLogin = dialogView.findViewById(R.id.login);
        TextView tvLogin2 = dialogView.findViewById(R.id.login2);
        TextView tvPass = dialogView.findViewById(R.id.password);
        TextView tvPass2 = dialogView.findViewById(R.id.password2);
        TextView tvRestore = dialogView.findViewById(R.id.restore_info);
        ImageView ivRestore = dialogView.findViewById(R.id.restore);

        tvWeb.setMovementMethod(new ScrollingMovementMethod());
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        tvLogin.setMovementMethod(new ScrollingMovementMethod());
        tvPass.setMovementMethod(new ScrollingMovementMethod());
        tvWeb2.setMovementMethod(new ScrollingMovementMethod());
        tvDescription2.setMovementMethod(new ScrollingMovementMethod());
        tvLogin2.setMovementMethod(new ScrollingMovementMethod());
        tvPass2.setMovementMethod(new ScrollingMovementMethod());

        tvWeb.setHorizontallyScrolling(true);
        tvDescription.setHorizontallyScrolling(true);
        tvLogin.setHorizontallyScrolling(true);
        tvPass.setHorizontallyScrolling(true);
        tvWeb2.setHorizontallyScrolling(true);
        tvDescription2.setHorizontallyScrolling(true);
        tvLogin2.setHorizontallyScrolling(true);
        tvPass2.setHorizontallyScrolling(true);

        if (dataChange == null) {
            linearLayoutAll.setVisibility(View.GONE);
            tvRestore.setVisibility(View.VISIBLE);
        } else {
            tvTime.setText(dataChange.time);

            if (dataChange.isRestored == 1) {
                ivRestore.setImageResource(R.drawable.ic_restore_gray);
            }

            if (action.equals("Add password")) {
                String[] present = dataChange.presentValue.split("\\|");
                tvWeb.setText(present[1]);
                tvDescription.setText(present[2]);
                tvLogin.setText(present[3]);
                tvPass.setText(backup.showDecryptedPassword(getLoggedUserId(), present[0]));
            } else if (action.equals("Delete password")) {
                tvHeader.setText(getResources().getText(R.string.deleted_data));
                String[] previous = dataChange.previousValue.split("\\|");
                tvWeb.setText(previous[1]);
                tvDescription.setText(previous[2]);
                tvLogin.setText(previous[3]);
                tvPass.setText(backup.showDecryptedPassword(getLoggedUserId(), previous[0]));
            } else {
                tvHeader.setText(getResources().getText(R.string.previous_data));
                String[] previous = dataChange.previousValue.split("\\|");
                String[] present = dataChange.presentValue.split("\\|");
                llPresentData.setVisibility(View.VISIBLE);
                tvWeb.setText(previous[1]);
                tvDescription.setText(previous[2]);
                tvLogin.setText(previous[3]);
                tvPass.setText(backup.showDecryptedPassword(getLoggedUserId(), previous[0]));
                tvWeb2.setText(present[1]);
                tvDescription2.setText(present[2]);
                tvLogin2.setText(present[3]);
                tvPass2.setText(backup.showDecryptedPassword(getLoggedUserId(), present[0]));
            }
        }

        final AlertDialog alertDialog = dialog.create();

        ivRestore.setOnClickListener(view -> {
            if (dataChange != null) {
                if (dataChange.isRestored == 1) {
                    showMessage("This record has already been restored");
                } else {
                    ivRestore.setClickable(false);
                    backup.restoreSelectedRecord(action);
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }
}