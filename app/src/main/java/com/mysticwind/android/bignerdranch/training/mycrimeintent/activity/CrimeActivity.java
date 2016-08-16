package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.CrimeFragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    public static final String CRIME_ID_EXTRA_KEY = "crimeId";

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }

    public static Intent newLaunchIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(CRIME_ID_EXTRA_KEY, crimeId);
        return intent;
    }
}
