package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.SingleFragmentActivity;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.dialog.DatePickerDialogFragment;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity {

    private static final String DATE_TIME_EXTRA_KEY = "dateTime";

    public static Intent newLaunchIntent(Context context, Date dateTime) {
        Intent intent = new Intent(context, DatePickerActivity.class);
        intent.putExtra(DATE_TIME_EXTRA_KEY, dateTime);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Date dateTime = (Date) getIntent().getSerializableExtra(DATE_TIME_EXTRA_KEY);
        return DatePickerDialogFragment.newInstance(dateTime);
    }
}
