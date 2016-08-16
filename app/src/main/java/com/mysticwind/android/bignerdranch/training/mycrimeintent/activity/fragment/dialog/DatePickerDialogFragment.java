package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerDialogFragment extends DialogFragment {

    private static final String DATE_ARGUMENT_KEY = "date";
    private static final String DATE_EXTRA_KEY = "date";

    private DatePicker datePicker;

    public static DatePickerDialogFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(DATE_ARGUMENT_KEY, date);
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Date date = (Date) getArguments().getSerializable(DATE_ARGUMENT_KEY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_datepicker, null);

        datePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        datePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date selectedDate) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(DATE_EXTRA_KEY, selectedDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static Date extractDate(Intent data) {
        Date date = (Date) data.getSerializableExtra(DATE_EXTRA_KEY);
        return date;
    }
}
