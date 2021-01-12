package com.kornasdominika.passwordwallet.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.kornasdominika.passwordwallet.R;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;

public class FunctionsListAdapter extends CursorAdapter {

    public FunctionsListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.functions_list_element, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvAction = view.findViewById(R.id.type);
        TextView tvTime = view.findViewById(R.id.time);

        String action = cursor.getString(cursor.getColumnIndexOrThrow("functionName"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));

        tvAction.setText(action);
        tvTime.setText(time);
    }

}
