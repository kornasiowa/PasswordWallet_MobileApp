package com.kornasdominika.passwordwallet.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.kornasdominika.passwordwallet.R;

public class PasswordsListAdapter extends CursorAdapter {

    public PasswordsListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.passwords_list_element, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvWebAddr = view.findViewById(R.id.web_address);
        TextView tvDescription = view.findViewById(R.id.description);

        String webAddr = cursor.getString(cursor.getColumnIndexOrThrow("web_address"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

        tvWebAddr.setText(webAddr);
        tvDescription.setText(description);
    }
}
