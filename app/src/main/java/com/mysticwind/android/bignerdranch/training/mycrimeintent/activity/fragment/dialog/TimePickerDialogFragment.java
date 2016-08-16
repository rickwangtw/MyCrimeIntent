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
import android.widget.TimePicker;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.R;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.Time;

import java.util.Calendar;
import java.util.Date;

public class TimePickerDialogFragment extends DialogFragment {

    private static final String TIME_ARGUMENT_KEY = "time";
    private static final String TIME_EXTRA_KEY = "time";

    private TimePicker timePicker;

    public static TimePickerDialogFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(TIME_ARGUMENT_KEY, date);
        TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Date date = (Date) getArguments().getSerializable(TIME_ARGUMENT_KEY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_timepicker, null);

        timePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();
                        sendResult(Activity.RESULT_OK, new Time(hour, minute));
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Time selectedTime) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(TIME_EXTRA_KEY, selectedTime);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static Time extractTime(Intent data) {
        Time time = (Time) data.getSerializableExtra(TIME_EXTRA_KEY);
        return time;
    }
}
