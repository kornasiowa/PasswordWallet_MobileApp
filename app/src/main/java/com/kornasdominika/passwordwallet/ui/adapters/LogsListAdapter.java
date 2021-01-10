package com.kornasdominika.passwordwallet.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.kornasdominika.passwordwallet.R;

public class LogsListAdapter extends CursorAdapter {

    public LogsListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.logs_list_element, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvIP = view.findViewById(R.id.ip);
        TextView tvDetails = view.findViewById(R.id.details);
        TextView tvTime = view.findViewById(R.id.time);

        String ip = cursor.getString(cursor.getColumnIndexOrThrow("ip"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));

        String details;
        if (cursor.getInt(cursor.getColumnIndexOrThrow("isLastSuccess")) == 1) {
            details = "successful";
        } else if(cursor.getInt(cursor.getColumnIndexOrThrow("failedAttempts")) >= 4){
            details = "blocked";
        } else
            details = "failed";

        tvIP.setText(ip);
        tvDetails.setText(details);
        tvTime.setText(time);
    }
}
