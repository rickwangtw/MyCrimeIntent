package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final Date date = (Date) getArguments().getSerializable(DATE_ARGUMENT_KEY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_datepicker, null);

        datePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        datePicker.init(year, month, day, null);

        final Button confirmButton = (Button) view.findViewById(R.id.dialog_date_picker_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                Date date = new GregorianCalendar(year, month, day).getTime();
                sendResult(Activity.RESULT_OK, date);
                DatePickerDialogFragment.this.dismiss();
            }
        });
        return view;
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
